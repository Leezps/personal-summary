package com.leezp.testnotification;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import com.leezp.testnotification.adapter.NotificationFileListAdapter;
import com.leezp.testnotification.entities.FileInfo;
import com.leezp.testnotification.services.NotificationDownloadService;
import com.leezp.testnotification.utils.NotificationUtil;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends Activity {
    private ListView mLvFile = null;
    private List<FileInfo> mFileList = null;
    private NotificationFileListAdapter mAdapter = null;
    private NotificationUtil mNotificationUtil = null;
    private Messenger mServiceMessenger = null; //Service中的Messenger

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mLvFile = findViewById(R.id.lvFile);
        //创建文件集合
        mFileList = new ArrayList<>();
        //创建文件对象
        FileInfo fileInfo_1 = new FileInfo(0, "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_9.6.8.apk", "BaiduNetdisk_9.6.8.apk", 0, 0);
        FileInfo fileInfo_2 = new FileInfo(1, "http://file.mukewang.com/imoocweb/webroot/mobile/imooc7.0.710102001android.apk", "imooc_7.0.7.apk", 0, 0);
        FileInfo fileInfo_3 = new FileInfo(2, "https://dl.hdslb.com/mobile/latest/iBiliPlayer-bili.apk", "bilibili.apk", 0, 0);
        FileInfo fileInfo_4 = new FileInfo(3, "https://umcdn.uc.cn/down/ab235fdcb823d83f/Youku_V7.6.6.0225.0001_ab235fdcb823d83f.apk", "youku_7.6.6.apk", 0, 0);
        FileInfo fileInfo_5 = new FileInfo(4, "http://s1.xmcdn.com/apk/MainApp_v6.5.63.3_c227_release_proguard_190304_and-a1.apk", "mainapp_6.5.6.apk", 0, 0);
        mFileList.add(fileInfo_1);
        mFileList.add(fileInfo_2);
        mFileList.add(fileInfo_3);
        mFileList.add(fileInfo_4);
        mFileList.add(fileInfo_5);
        //创建适配器
        mAdapter = new NotificationFileListAdapter(this, mFileList);
        //设置ListView
        mLvFile.setAdapter(mAdapter);
        //注册广播接收器
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(NotificationDownloadService.ACTION_UPDATE);
//        filter.addAction(NotificationDownloadService.ACTION_FINISH);
//        filter.addAction(NotificationDownloadService.ACTION_START);
//        registerReceiver(mReceiver, filter);

        mNotificationUtil = new NotificationUtil(this);
        //绑定Service
        Intent intent = new Intent(this, NotificationDownloadService.class);
        bindService(intent,mConnection, Service.BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获得Service中的Messenger
            mServiceMessenger = new Messenger(service);
            //设置适配器中的Messenger
            mAdapter.setMessenger(mServiceMessenger);
            //创建Activity中的Messenger
            Messenger messenger = new Messenger(mHandler);
            //创建消息
            Message msg = new Message();
            msg.what = NotificationDownloadService.MSG_BIND;
            msg.replyTo = messenger;
            //使用Service的Messenger发送Activity中的Messenger
            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mReceiver);
//    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NotificationDownloadService.MSG_UPDATE:
                    //更新进度条
                    int finished = msg.arg1;
                    int id = msg.arg2;
                    mAdapter.updateProgress(id,finished);
                    //更新通知里的进度
                    mNotificationUtil.updateNotification(id, finished);
                    break;
                case NotificationDownloadService.MSG_FINISH:
                    //下载结束
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    //更新进度为100
                    mAdapter.updateProgress(fileInfo.getId(),100);
                    Toast.makeText(ThirdActivity.this,mFileList.get(fileInfo.getId()).getFileName()+"下载完毕",Toast.LENGTH_SHORT).show();
                    //取消通知
                    mNotificationUtil.cancelNotification(fileInfo.getId());
                    break;
                case NotificationDownloadService.MSG_START:
                    //提示通知
                    mNotificationUtil.showNotification((FileInfo) msg.obj);
            }
        }
    };

    /**
     * 更新UI的广播接收器
     */
//    BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (NotificationDownloadService.ACTION_UPDATE.equals(intent.getAction())) {
//                //更新进度条
//                int finished = intent.getIntExtra("finished",0);
//                int id = intent.getIntExtra("id", 0);
//                mAdapter.updateProgress(id,finished);
//                //更新通知里的进度
//                mNotificationUtil.updateNotification(id, finished);
//            } else if (NotificationDownloadService.ACTION_FINISH.equals(intent.getAction())) {
//                //下载结束
//                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
//                //更新进度为100
//                mAdapter.updateProgress(fileInfo.getId(),100);
//                Toast.makeText(ThirdActivity.this,mFileList.get(fileInfo.getId()).getFileName()+"下载完毕",Toast.LENGTH_SHORT).show();
//                //取消通知
//                mNotificationUtil.cancelNotification(fileInfo.getId());
//            } else if (NotificationDownloadService.ACTION_START.equals(intent.getAction())) {
//                //提示通知
//                mNotificationUtil.showNotification((FileInfo) intent.getSerializableExtra("fileInfo"));
//            }
//        }
//    };
}
