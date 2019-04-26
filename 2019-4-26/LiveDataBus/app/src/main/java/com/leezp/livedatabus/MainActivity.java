package com.leezp.livedatabus;

//import android.arch.lifecycle.Observer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.leezp.livedatabus.customize.CLiveDataBus;

import java.net.InterfaceAddress;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LiveDataBus.get().getChannel("event").setValue("我比订阅者还早诞生……");
        LiveDataBus.get().getChannel("event", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //UI 线程
                Toast.makeText(MainActivity.this,"------------------->"+s,Toast.LENGTH_SHORT).show();
            }
        });

//        CLiveDataBus.get().getChannel("event", String.class).observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //UI 线程
//                Toast.makeText(MainActivity.this, "------------------->" + s, Toast.LENGTH_SHORT).show();
//            };
//        });

//        MyThread thread = new MyThread();
//        thread.start();

        show = findViewById(R.id.activity_main_show);

//        LiveDataTimerViewModel viewModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel.class);
//        viewModel.getmTimer().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                show.setText(s);
//            }
//        });
    }

    public void jump(View view) {
        switch (view.getId()) {
            case R.id.activity_main_pop_event:
//              LiveDataBus.get().getChannel("event").setValue("我是从UI线程发送过来的");
                CLiveDataBus.get().getChannel("event").setValue("我是从UI线程发送过来的");
                break;
            case R.id.activity_main_jump:
                startActivity(new Intent(this,SecondActivity.class));
        }

    }
}
