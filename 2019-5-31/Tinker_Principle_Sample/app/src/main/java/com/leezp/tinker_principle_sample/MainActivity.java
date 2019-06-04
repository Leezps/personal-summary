package com.leezp.tinker_principle_sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.leezp.tinker_principle_sample.acts.SecondActivity;

//将它放置到主包
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(perms, 200);
            }
        }

    }

    public void jump(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
