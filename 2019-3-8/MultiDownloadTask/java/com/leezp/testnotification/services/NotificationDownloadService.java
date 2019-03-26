package com.leezp.testnotification.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.leezp.testnotification.entities.FileInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class NotificationDownloadService extends Service {

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/";
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISH = "ACTION_FINISH";
    public static final String TAG = "DownloadService";
    public static final int MSG_INIT = 0;
    //绑定标识
    public static final int MSG_BIND = 1;
    public static final int MSG_START = 2;
    public static final int MSG_STOP = 3;
    public static final int MSG_UPDATE = 4;
    public static final int MSG_FINISH = 5;
    //下载任务的集合
    private Map<Integer, NotificationDownloadTask> mTasks = new LinkedHashMap<>();
    private Messenger mActivityMessenger = null;    //来自Activity的Messenger

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得Activity传来的参数
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i(TAG, "Start: "+fileInfo.toString());
            //启动初始化线程
//            new InitThread(fileInfo).start();
            NotificationDownloadTask.sExecutorService.execute(new InitThread(fileInfo));
        } else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i(TAG, "Stop: "+fileInfo.toString());
            //从集合中取出下载任务
            NotificationDownloadTask task = mTasks.get(fileInfo.getId());
            if (task != null) {
                //停止下载任务
                task.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //创建一个Messenger对象包含Handler的引用
        Messenger messenger = new Messenger(mHandler);
        //返回Messenger的Binder
        return messenger.getBinder();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FileInfo fileInfo = null;
            NotificationDownloadTask task = null;
            switch (msg.what) {
                case MSG_INIT:
                    fileInfo = (FileInfo) msg.obj;
                    Log.i(TAG, "Init: "+fileInfo);
                    //启动下载任务
                    task = new NotificationDownloadTask(NotificationDownloadService.this, mActivityMessenger, fileInfo,3);
                    task.download();
                    //把下载任务添加到集合中
                    mTasks.put(fileInfo.getId(),task);
                    //发送启动命令的广播
//                    Intent intent = new Intent(NotificationDownloadService.ACTION_START);
//                    intent.putExtra("fileInfo", fileInfo);
//                    sendBroadcast(intent);
                    Message msg1 = new Message();
                    msg1.what = MSG_START;
                    msg1.obj = fileInfo;
                    try {
                        mActivityMessenger.send(msg1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_BIND:
                    //处理绑定Messenger
                    mActivityMessenger = msg.replyTo;
                    break;
                case MSG_START:
                    fileInfo = (FileInfo) msg.obj;
                    Log.i(TAG, "Start: "+fileInfo.toString());
                    //启动初始化线程
                    NotificationDownloadTask.sExecutorService.execute(new InitThread(fileInfo));
                    break;
                case MSG_STOP:
                    fileInfo = (FileInfo) msg.obj;
                    Log.i(TAG, "Stop: "+fileInfo.toString());
                    //从集合中取出下载任务
                    task = mTasks.get(fileInfo.getId());
                    if (task != null) {
                        //停止下载任务
                        task.isPause = true;
                    }
                    break;
            }
        }
    };

    /**
     * 初始化子线程
     */
    class InitThread extends Thread {
        private FileInfo mFileInfo = null;

        public InitThread(FileInfo fileInfo) {
            this.mFileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                //连接网络文件
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    //获取文件长度
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                //在本地创建文件
                File file = new File(dir, mFileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                //设置文件长度
                raf.setLength(length);
                mFileInfo.setLength(length);
                mHandler.obtainMessage(MSG_INIT,mFileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
