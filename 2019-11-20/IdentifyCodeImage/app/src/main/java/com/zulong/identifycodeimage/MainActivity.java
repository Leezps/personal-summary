package com.zulong.identifycodeimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.zulong.identifycodeimage.utils.ZLCodeImageUtils;
import com.zulong.identifycodeimage.utils.ZLPermissionUtils;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    private final String TAG = getClass().getSimpleName();
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImage = findViewById(R.id.activity_main_image);
        ZLPermissionUtils.setActivityWeakReference(this);
        ZLPermissionUtils.setPermissionsListener(new ZLPermissionUtils.PermissionsListener() {
            @Override
            public void allPermissionsGranted() {
                doSomething();
            }

            @Override
            public void somePermissionsDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "没有同意权限，无法正确显示", Toast.LENGTH_SHORT).show();
            }
        });
        HashMap<String, String> params = new HashMap<>();
        params.put(ZLPermissionUtils.PROMPT_TITLE, "许可权请求：");
        params.put(ZLPermissionUtils.PROMPT_CONTENT, "为了更好的游戏体验，请你开启以下许可权限：\n存放资料：游戏需要将游戏资料存放在你的档案库中，方便让你顺利进行游戏。\n录音：游戏的语音聊天功能需要使用你的麦克风！");
        params.put(ZLPermissionUtils.PROMPT_POSITIVE_TEXT, "确认");
        params.put(ZLPermissionUtils.QUERY_TITLE, "权限说明");
        params.put(ZLPermissionUtils.QUERY_CONTENT, "这些权限是必须权限，否则无法正常启动该功能,因为你已勾选不再询问，所以无法在应用界面授权，需去设置界面授权，是否进入设置界面重新授权？");
        params.put(ZLPermissionUtils.QUERY_POSITIVE_TEXT, "是");
        params.put(ZLPermissionUtils.QUERY_NEGATIVE_TEXT, "否");
        params.put(ZLPermissionUtils.DENY_TITLE, "权限说明");
        params.put(ZLPermissionUtils.DENY_CONTENT, "这些权限是必须权限，否则无法正常启动该功能,是否重新申请权限？");
        params.put(ZLPermissionUtils.DENY_POSITIVE_TEXT, "重新申请");
        params.put(ZLPermissionUtils.DENY_NEGATIVE_TEXT, "取消申请");
        ZLPermissionUtils.setPermissionsParams(params);
        ZLPermissionUtils.beginRequestPermission(permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZLPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ZLPermissionUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void doSomething() {
        ZLCodeImageUtils.getInstance().setCode("95421654");
        Bitmap bitmap = ZLCodeImageUtils.getInstance().generateBitmap();
        mImage.setImageBitmap(bitmap);
        boolean b = ZLCodeImageUtils.getInstance().savePngImageToPictures(this, bitmap);
        Log.e(TAG, "onCreate: " + b);
        String identify = ZLCodeImageUtils.getInstance().getIdentifyFromPictures();
        Log.e(TAG, "identify: " + identify);
    }
}
