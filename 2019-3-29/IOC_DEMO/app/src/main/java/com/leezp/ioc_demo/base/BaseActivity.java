package com.leezp.ioc_demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.leezp.library.InjectManager;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //帮助所有子类进行：布局、控件、事件的注入
        InjectManager.inject(this);
    }
}
