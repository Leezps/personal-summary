package com.leezp.note_permission_sample;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leezp.annotation.NeedsPermission;
import com.leezp.annotation.OnNeverAskAgain;
import com.leezp.annotation.OnPermissionDenied;
import com.leezp.annotation.OnShowRationale;
import com.leezp.library.PermissionManager;
import com.leezp.library.listener.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void camera(View view) {
        PermissionManager.request(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS});
    }

    // 获取所有权限之后需要执行的操作
    @NeedsPermission()
    void showCamera() {
        Log.e("leezp >>> ", "showCamera()");
    }

    // 当上一次拒绝权限并“未”点击不再询问之后，第二次提示用户为何要开启权限
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {
        Log.e("leezp >>> ", "showRationaleForCamera()");
        new AlertDialog.Builder(this)
                .setMessage("提示用户为何要开启权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 再次执行权限请求
                        request.proceed();
                    }
                }).show();
    }

    // 用户选择拒绝打开权限之后的操作
    @OnPermissionDenied()
    void showDeniedForCamera() {
        Log.e("leezp >>> ", "showDeniedForCamera()");
    }

    // 用户选择不再询问后的提示
    @OnNeverAskAgain()
    void showNeverAskForCamera() {
        Log.e("leezp >>> ", "showNeverAskForCamera()");
        new AlertDialog.Builder(this)
                .setMessage("用户选择不再询问后的提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("leezp >>> ", "OnNeverAskAgain()");
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("leezp >>> ", "onRequestPermissionsResult()");
        PermissionManager.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
