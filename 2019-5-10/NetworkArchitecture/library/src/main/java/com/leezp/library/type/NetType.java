package com.leezp.library.type;

//  网络类型
public enum NetType {
    //  只要有网络，不关心是wifi/gprs
    AUTO,

    //  WIFI网络
    WIFI,

    //  主要是PC/笔记本电脑/PAD上网
    CMNET,

    //  手机上网
    CMWAP,

    //  没有任何网络
    NONE
}
