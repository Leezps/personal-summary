package com.leezp.networkarchitecture;

import android.app.Application;

import com.leezp.library.NetworkManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
