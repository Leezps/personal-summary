package com.leezp.livedatabus;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leezp.livedatabus.customize.CLiveDataBus;
import com.leezp.livedatabus.customize.Observer;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        CLiveDataBus.get().getChannel("event", String.class).observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //UI线程
//                Toast.makeText(SecondActivity.this, "------------>" + s, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void send(View view) {
        CLiveDataBus.get().getChannel("event").setValue("我是第二个界面发送过来的");
    }
}
