package com.leezp.pluginlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static final PluginManager instance = new PluginManager();
    private Context mContext;
    private PluginApp mPluginApp;

    public static PluginManager getInstance() {
        return instance;
    }

    private PluginManager() {
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public PluginApp getmPluginApp() {
        return mPluginApp;
    }

    //加载插件apk
    public void loadApk(String apkPath) {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) {
            return;
        }
        DexClassLoader classLoader = createDexClassLoader(apkPath);
        AssetManager am = createAssetManager(apkPath);
        Resources resource = createResource(am);
        mPluginApp = new PluginApp(packageInfo, resource, classLoader);
    }

    //创建访问插件apk的DexClassLoader对象
    private DexClassLoader createDexClassLoader(String apkPath) {
        File file = mContext.getDir("dex", Context.MODE_PRIVATE);
        return new DexClassLoader(apkPath, file.getAbsolutePath(), null, mContext.getClassLoader());
    }

    //创建AssetManager
    private AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager am = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(am, apkPath);
            return am;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //创建Resource对象
    private Resources createResource(AssetManager am) {
        Resources res = mContext.getResources();
        return new Resources(am, res.getDisplayMetrics(), res.getConfiguration());
    }
}
