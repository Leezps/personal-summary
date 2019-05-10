package com.leezp.networkarchitecture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import com.leezp.library.NetworkManager;
//import com.leezp.library.listener.NetChangeObserver;
import com.leezp.library.NetworkManager;
import com.leezp.library.annotation.Network;
import com.leezp.library.type.NetType;
import com.leezp.library.utils.Constants;

public class MainActivity extends AppCompatActivity /* implements NetChangeObserver */ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NetworkManager.getDefault().init(getApplication());
//        NetworkManager.getDefault().setListener(this);

        NetworkManager.getDefault().registerObserver(this);
    }

    //  不需要
    public void view() {

    }

    //  要网络监听
    public void submit() {

    }

    @Network(netType = NetType.WIFI)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.LOG_TAG,"network >>> WIFI");
                break;
            case CMNET:
            case CMWAP:
                Log.e(Constants.LOG_TAG, "流量：" + netType.name());
                break;
            case NONE:
                //  没有网络，提示用户Dialog方式，跳转到设置界面
                Log.e(Constants.LOG_TAG,"没有网络");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 反注册，解绑
        NetworkManager.getDefault().unRegisterObserver(this);
        //  移除所有
        NetworkManager.getDefault().unRegisterAllObserver();
    }

    //    @Override
//    public void onConnect(NetType netType) {
//        Log.e(Constants.LOG_TAG, "onConnect: " + netType.name());
//    }
//
//    @Override
//    public void onDisConnect() {
//        Log.e(Constants.LOG_TAG, "onDisConnect: 没有网络");
//    }
}
