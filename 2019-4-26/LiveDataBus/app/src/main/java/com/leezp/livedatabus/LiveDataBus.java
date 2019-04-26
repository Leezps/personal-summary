package com.leezp.livedatabus;

import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;
//事件总线框架
public class LiveDataBus {
    //MutableLiveData 消息通道
    private Map<String, MutableLiveData<Object>> bus = new HashMap<>();
    private static final LiveDataBus ourInstance = new LiveDataBus();

    public static LiveDataBus get() {return ourInstance;}

    private LiveDataBus() {
    }

    public <T> MutableLiveData<T> getChannel(String target, Class<T> type) {
        if (!bus.containsKey(target)) {
            bus.put(target, new MutableLiveData<>());
        }
        return ((MutableLiveData) bus.get(target));
    }

    public MutableLiveData<Object> getChannel(String target){
        return getChannel(target, Object.class);
    }
}
