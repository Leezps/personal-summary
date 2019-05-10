package com.leezp.library;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.leezp.library.core.NetworkCallbackImpl;
import com.leezp.library.listener.NetChangeObserver;
import com.leezp.library.utils.Constants;

public class NetworkManager {
    //单例
    private static volatile NetworkManager instance;
    private Application application;
    private NetStateReceiver receiver;

    private NetworkManager() {
        receiver = new NetStateReceiver();
    }

//    public void setListener(NetChangeObserver listener) {
//        receiver.setListener(listener);
//    }

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    //  初始化方法
    public void init(Application application) {
        if (application == null) return;
        this.application = application;
        //  动态广播注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);

//        //  第二种方式监听，不通过广播
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();
//            NetworkRequest.Builder builder = new NetworkRequest.Builder();
//            NetworkRequest request = builder.build();
//            ConnectivityManager cmgr = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (cmgr != null) {
//                cmgr.registerNetworkCallback(request, networkCallback);
//            } else {
//
//            }
//        }

    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("application not init()");
        }
        return application;
    }

    public void registerObserver(Object register) {
        receiver.registerObserver(register);
    }

    public void unRegisterObserver(Object register) {
        receiver.unRegisterObserver(register);
    }

    public void unRegisterAllObserver() {
        receiver.unRegisterAllObserver();
    }
}
