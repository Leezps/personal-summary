package com.leezp.library.listener;

import com.leezp.library.type.NetType;

//  网络监听
public interface NetChangeObserver {

    //  网络连接时
    void onConnect(NetType netType);

    //  没有网络
    void onDisConnect();
}
