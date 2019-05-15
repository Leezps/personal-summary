package com.leezp.incrementupdate_bsdiff_demo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leezp.incrementupdate_bsdiff_demo.utils.UriParseUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Activity mActivity;

    // 用于在应用程序启动时，加载“本地Lib”库
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        TextView tv = findViewById(R.id.version);
        tv.setText(BuildConfig.VERSION_NAME);

        // 运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
    }

    /**
     * 合成安装包
     *
     * @param oldApk 旧版本安装包，如1.1.1版本的路径
     * @param patch  差分包，patch 文件
     * @param output 合成之后的新文件，新版本apk的输出/保存路径
     */
    public native void bsPatch(String oldApk, String patch, String output);

    // 点击
    public void update(View view) {
        // 服务器下载patch文件(不模拟网络下载了)，反正最终要到手机里边
        new AsyncTask<Void, Void, File>() {

            // 异步任务，耗时操作在后台执行，直到完成了，才调用onPostExecute()
            @Override
            protected File doInBackground(Void... voids) {
                // 合成新版本 APK
                String oldApk = getApplicationInfo().sourceDir;
                // 模拟已经下载到了 SDCard 中
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                // 我想把新文件也放在 SDCard,出于测试
                String output = createNewApk().getAbsolutePath();
                bsPatch(oldApk, patch, output);
                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                // 已经得到新版本 APK, 开始安装新版本
                UriParseUtils.installApk(mActivity, file);
            }
        }.execute();    //千万别忘了
    }

    // 创建合成后的新版本apk文件（占坑）
    private File createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(), "bsdiff.apk");
        if (!newApk.exists()) {
            try {
                // 创建了一个占位的空文件
                newApk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newApk;
    }
}
