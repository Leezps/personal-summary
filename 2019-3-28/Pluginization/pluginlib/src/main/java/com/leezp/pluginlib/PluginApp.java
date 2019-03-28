package com.leezp.pluginlib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class PluginApp {
    //插件apk的实体对象
    public PackageInfo mPackageInfo;
    public Resources mResource;
    public AssetManager mAssetManager;
    public DexClassLoader mClassLoader;

    public PluginApp(PackageInfo mPackageInfo, Resources mResource, DexClassLoader mClassLoader) {
        this.mPackageInfo = mPackageInfo;
        this.mResource = mResource;
        this.mAssetManager = mResource.getAssets();
        this.mClassLoader = mClassLoader;
    }

}
