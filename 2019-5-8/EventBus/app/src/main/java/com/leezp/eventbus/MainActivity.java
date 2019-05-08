package com.leezp.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leezp.eventbus.bus.EventBus;
import com.leezp.eventbus.bus.Subscrible;
import com.leezp.eventbus.bus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        findViewById(R.id.tvTiaozhuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    // Subscrible是个注解，它的作用是：只是一个标记，它标记的方法都将被 EventBus 给收录
    // ThreadMode 一个枚举，它代表着 EventBus 通过 post 发送消息时，接收的线程是在主线程还是子线程
    @Subscrible(threadMode = ThreadMode.MAIN)
    public void getMessage(EventBean bean) {
        Log.e("===>Main", bean.toString());
        Log.e("=== Main ThreadName ", Thread.currentThread().getName());
    }

//    @Subscrible
//    public void getMessage1(EventBean bean) {
//        Log.e("===>Main", bean.toString());
//    }
}
