package com.leezp.livedatabus;

import com.leezp.livedatabus.customize.CLiveDataBus;

public class MyThread extends Thread {

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        LiveDataBus.get().getChannel("event").postValue("我是从异步线程发送过来的");
        CLiveDataBus.get().getChannel("event").postValue("我是从异步线程发送过来的");
    }

}
