package com.leezp.testnotification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leezp.testnotification.entities.FileInfo;
import com.leezp.testnotification.services.DownloadService;

public class MainActivity extends Activity {

    private TextView mTvFileName;
    private ProgressBar mPbProgress;
    private Button mBtnStop;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化组件
        mTvFileName =  findViewById(R.id.tvFileName);
        mPbProgress = findViewById(R.id.pbProgress);
        mBtnStop = findViewById(R.id.buttonStop);
        mBtnStart = findViewById(R.id.buttonStart);
        mPbProgress.setMax(100);
        //创建文件信息对象
        final FileInfo fileInfo = new FileInfo(0,"http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_9.6.1.apk","BaiduNetdisk_9.6.1.apk",0,0);
        mTvFileName.setText(fileInfo.getFileName());
        //添加事件监听
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent传递参数给Service
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent传递参数给Service
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
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
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished",0);
                mPbProgress.setProgress(finished);
            }
        }
    };
}
