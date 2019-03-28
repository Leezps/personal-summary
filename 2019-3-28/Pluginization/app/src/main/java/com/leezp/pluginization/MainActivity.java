package com.leezp.pluginization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leezp.pluginlib.PluginManager;
import com.leezp.pluginlib.ProxyActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManager.getInstance().init(this);
        findViewById(R.id.activity_main_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载apk文件
                String apkPath = Utils.copyAssetAndWrite(MainActivity.this, "pluginapp.apk");
                PluginManager.getInstance().loadApk(apkPath);
            }
        });

        findViewById(R.id.activity_main_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到指定的Activity
                Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
                intent.putExtra("className", "com.leezp.pluginapp.MainActivity");
                startActivity(intent);
            }
        });
    }
}
