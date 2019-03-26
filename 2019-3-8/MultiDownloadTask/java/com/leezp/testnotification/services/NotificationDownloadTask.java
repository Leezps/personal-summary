package com.leezp.testnotification.services;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.leezp.testnotification.db.MultiThreadDAOImpl;
import com.leezp.testnotification.db.ThreadDAO;
import com.leezp.testnotification.entities.FileInfo;
import com.leezp.testnotification.entities.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationDownloadTask {
    private Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAO mDao = null;
    private int mFinished = 0;
    public boolean isPause = false;
    private int mThreadCount = 1;   //线程数量
    private List<DownloadThread> mThreadList = null;    //线程集合
    public static ExecutorService sExecutorService = Executors.newCachedThreadPool();   //线程池
    private Timer mTimer = new Timer(); //定时器
    private Messenger mMessenger = null;

    public NotificationDownloadTask(Context mContext, Messenger mMessenger, FileInfo mFileInfo, int mThreadCount) {
        this.mContext = mContext;
        this.mMessenger = mMessenger;
        this.mFileInfo = mFileInfo;
        this.mThreadCount = mThreadCount;
        mDao = new MultiThreadDAOImpl(mContext);
    }

    public void download() {
        //读取数据库的线程信息
        List<ThreadInfo> threadInfos = mDao.getThreads(mFileInfo.getUrl());
        if (threadInfos.size() == 0) {
            //获得每个线程下载的长度
            int length = mFileInfo.getLength() / mThreadCount;
            for (int i = 0; i < mThreadCount; i++) {
                //创建线程信息
                ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(), length * i, (i + 1) * length - 1, 0);
                if (i == mThreadCount-1) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }
                //添加到线程信息集合中
                threadInfos.add(threadInfo);
                //向数据库插入线程信息
                mDao.insertThread(threadInfo);
            }
        }
        mThreadList = new ArrayList<>();
        //启动多个线程进行下载
        for (ThreadInfo info : threadInfos) {
            DownloadThread thread = new DownloadThread(info);
//            thread.start();
            NotificationDownloadTask.sExecutorService.execute(thread);
            //添加线程到集合中
            mThreadList.add(thread);
        }
        //启动定时任务
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //发送广播修改Activity进度
                //发送进度到Activity
//                Intent intent = new Intent(NotificationDownloadService.ACTION_UPDATE);
                int currentProgress = (int) ((mFinished/(float)mFileInfo.getLength())*100);
//                intent.putExtra("finished", currentProgress);
//                intent.putExtra("id",mFileInfo.getId());
//                mContext.sendBroadcast(intent);
                Message message = new Message();
                message.what = NotificationDownloadService.MSG_UPDATE;
                message.arg1 = currentProgress;
                message.arg2 = mFileInfo.getId();
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },1000, 1000);
    }

    /**
     * 判断是否所有线程都执行完毕
     */
    private synchronized void chechAllThreadsFinished() {
        boolean allFinished = true;
        //遍历线程集合，判断线程是否都执行完毕
        for (DownloadThread thread : mThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) {
            //取消定时器
            mTimer.cancel();
            //删除线程信息
            ((MultiThreadDAOImpl) mDao).deleteThreadByUrl(mFileInfo.getUrl());
//            Intent intent = new Intent(NotificationDownloadService.ACTION_FINISH);
//            intent.putExtra("fileInfo", mFileInfo);
//            mContext.sendBroadcast(intent);
            Message message = new Message();
            message.what = NotificationDownloadService.MSG_FINISH;
            message.obj = mFileInfo;
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo = null;
        public boolean isFinished = false;  //线程是否执行完毕

        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range","bytes="+start+"-"+mThreadInfo.getEnd());
                //设置文件写入位置
                File file = new File(NotificationDownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                mFinished += mThreadInfo.getFinished();
                //开始下载
                if (conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    while((len = input.read(buffer)) != -1) {
                        //写入文件
                        raf.write(buffer,0,len);
                        //累加整个文件完成进度
                        mFinished += len;
                        //累加每个线程完成的进度
                        mThreadInfo.setFinished(mThreadInfo.getFinished() + len);

                        //在下载暂停时，保存下载进度
                        if (isPause) {
                            mTimer.cancel();
                            //保存进度到数据库
                            mDao.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mThreadInfo.getFinished());
                            return;
                        }
                    }
                    //标识线程执行完毕
                    isFinished = true;
                    //检查下载任务是否执行完毕
                    chechAllThreadsFinished();
                }
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
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
