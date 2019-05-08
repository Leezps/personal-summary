package com.leezp.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leezp.eventbus.bus.EventBus;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.tvSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  发送信息给 MainActivity
//                EventBus.getDefault().post(new EventBean("依依", "小鸟依人"));

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        EventBus.getDefault().post(new EventBean("依依", "小鸟依人"));
                        Log.e("=== Second ", Thread.currentThread().getName());
                    }
                }.start();
            }
        });
    }
}
