package com.leezp.networkokhttp.okhttp.listener;

// Json 请求将字符流转换成对象之后的回调接口
public interface IJsonDataListener<T> {
    void onSuccess(T m);

    void onFailure();
}
