package com.leezp.tinker_principle_sample;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.leezp.library.FixDexUtils;

// 主包
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        FixDexUtils.loadFixedDex(this);
    }
}
