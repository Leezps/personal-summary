package com.leezp.networkokhttp.okhttp.listener;

import java.io.InputStream;

// 回调接口
public interface CallBackListener {
    void onSuccess(InputStream inputStream);

    void onFailure();
}
