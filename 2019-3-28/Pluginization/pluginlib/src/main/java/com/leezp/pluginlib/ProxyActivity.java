package com.leezp.pluginlib;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class ProxyActivity extends Activity {
    private String mClassName;
    private PluginApp mPluginApp;
    private IPlugin mIplugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassName = getIntent().getStringExtra("className");
        mPluginApp = PluginManager.getInstance().getmPluginApp();

        launchPluginActivity();
    }

    private void launchPluginActivity() {
        if (mPluginApp == null) {
            throw new RuntimeException("请先加载插件apk");
        }
        try {
            Class<?> clazz = mPluginApp.mClassLoader.loadClass(mClassName);
            //这里就是Activity的实例对象，注意：这里的clazz没有生命周期，也没有上下文
            Object object = clazz.newInstance();
            if (object instanceof IPlugin) {
                mIplugin = ((IPlugin) object);
                mIplugin.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("FROM", IPlugin.FROM_EXTERNAL);
                mIplugin.onCreate(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return mPluginApp != null ? mPluginApp.mResource : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mPluginApp != null ? mPluginApp.mAssetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginApp != null ? mPluginApp.mClassLoader : super.getClassLoader();
    }
}
