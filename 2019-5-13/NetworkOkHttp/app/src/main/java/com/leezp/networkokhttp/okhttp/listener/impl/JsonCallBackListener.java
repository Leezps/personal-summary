package com.leezp.networkokhttp.okhttp.listener.impl;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.leezp.networkokhttp.okhttp.listener.CallBackListener;
import com.leezp.networkokhttp.okhttp.listener.IJsonDataListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Json 请求的回调函数
public class JsonCallBackListener<T> implements CallBackListener {
    private Class<T> responseClass;
    private IJsonDataListener iJsonDataListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallBackListener(Class<T> responseClass, IJsonDataListener listener) {
        this.responseClass = responseClass;
        this.iJsonDataListener = listener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        // 将 inputStream 对象转换成 responseClass 对象
        String response = getContent(inputStream);
        final T clazz = JSON.parseObject(response, responseClass);
        // 将子线程的对象数据传递到主线程
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error=" + e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error=" + e.toString());
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onFailure() {

    }
}
