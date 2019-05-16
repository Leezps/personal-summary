package com.leezp.universalinterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leezp.universalinterface.bean.UserBean;
import com.leezp.universalinterface.interfaces.FunctionManager;
import com.leezp.universalinterface.interfaces.functions.FunctionHasParamHasResult;
import com.leezp.universalinterface.interfaces.functions.FunctionHasParamNoResult;
import com.leezp.universalinterface.interfaces.functions.FunctionNoParamHasResult;
import com.leezp.universalinterface.interfaces.functions.FunctionNoParamNoResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FunctionManager.getInstance().addFunction(new FunctionNoParamNoResult("MainActivity_NoParamNoResult") {
            @Override
            public void function() {
                Log.e("MainActivity", "Hello from second Activity");
                // 不能在此处执行耗时操作
            }
        });

        FunctionManager.getInstance().addFunction(new FunctionNoParamHasResult<UserBean>("MainActivity_NoParamHasResult") {
            @Override
            public UserBean function() {
                return new UserBean("王老二", "123456");
            }
        });

        FunctionManager.getInstance().addFunction(new FunctionHasParamNoResult<UserBean>("MainActivity_HasParamNoResult") {
            @Override
            public void function(UserBean userBean) {
                Log.e("MainActivity", "Hello from Second Activity—" + userBean.getmUserName() + "  " + userBean.getmPassWord());
            }
        });

        FunctionManager.getInstance().addFunction(new FunctionHasParamHasResult<UserBean, UserBean>("MainActivity_HasParamHasResult") {
            @Override
            public UserBean function(UserBean userBean) {
                Log.e("MainActivity", "Hello from Second Activity—" + userBean.getmUserName() + "  " + userBean.getmPassWord());
                return new UserBean("秋香", "456321");
            }
        });

        Intent intent = new Intent();
        intent.setClass(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FunctionManager.getInstance().removeFunction("MainActivity_NoParamNoResult");
        FunctionManager.getInstance().removeFunction("MainActivity_NoParamHasResult");
        FunctionManager.getInstance().removeFunction("MainActivity_HasParamNoResult");
        FunctionManager.getInstance().removeFunction("MainActivity_HasParamHasResult");

        //理论上 dispose 这个函数应该在 Application 中的 onStop 的函数中调用
        FunctionManager.getInstance().dispose();
    }
}
