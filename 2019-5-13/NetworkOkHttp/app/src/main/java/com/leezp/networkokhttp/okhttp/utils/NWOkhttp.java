package com.leezp.networkokhttp.okhttp.utils;

import com.leezp.networkokhttp.okhttp.HttpTask;
import com.leezp.networkokhttp.okhttp.ThreadPoolManager;
import com.leezp.networkokhttp.okhttp.listener.CallBackListener;
import com.leezp.networkokhttp.okhttp.listener.IHttpRequest;
import com.leezp.networkokhttp.okhttp.listener.IJsonDataListener;
import com.leezp.networkokhttp.okhttp.listener.impl.JsonCallBackListener;
import com.leezp.networkokhttp.okhttp.listener.impl.JsonHttpRequest;

public class NWOkhttp {
    public static <T, M> void sendJsonRequest(T requestData, String url, Class<M> response, IJsonDataListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallBackListener callBackListener = new JsonCallBackListener<>(response, listener);
        HttpTask ht = new HttpTask(url, requestData, httpRequest, callBackListener);
        ThreadPoolManager.getInstance().addTask(ht);
    }
}
