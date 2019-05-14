package com.leezp.networkokhttp.okhttp.listener;

// 请求封装的接口
public interface IHttpRequest {
    void setUrl(String url);

    void setData(byte[] data);

    void setListener(CallBackListener listener);

    void execute();
}
