package com.leezp.testnotification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import com.leezp.testnotification.adapter.FileListAdapter;
import com.leezp.testnotification.entities.FileInfo;
import com.leezp.testnotification.services.MultiDownloadService;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends Activity {
    private ListView mLvFile = null;
    private List<FileInfo> mFileList = null;
    private FileListAdapter mAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
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
        mAdapter = new FileListAdapter(this, mFileList);
        //设置ListView
        mLvFile.setAdapter(mAdapter);
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(MultiDownloadService.ACTION_UPDATE);
        filter.addAction(MultiDownloadService.ACTION_FINISH);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MultiDownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished",0);
                int id = intent.getIntExtra("id", 0);
                mAdapter.updateProgress(id,finished);
            } else if (MultiDownloadService.ACTION_FINISH.equals(intent.getAction())) {
                //下载结束
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                //更新进度为0
                mAdapter.updateProgress(fileInfo.getId(),100);
                Toast.makeText(SecondActivity.this,mFileList.get(fileInfo.getId()).getFileName()+"下载完毕",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
