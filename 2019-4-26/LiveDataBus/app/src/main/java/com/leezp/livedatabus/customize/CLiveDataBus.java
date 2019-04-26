package com.leezp.livedatabus.customize;

import java.util.HashMap;
import java.util.Map;

//事件总线框架
public class CLiveDataBus {
    //LiveData 消息通道
    private Map<String, LiveData<Object>> bus = new HashMap<>();
    private static final CLiveDataBus ourInstance = new CLiveDataBus();

    public static CLiveDataBus get() {return ourInstance;}

    private CLiveDataBus() {
    }

    public <T> LiveData<T> getChannel(String target, Class<T> type) {
        if (!bus.containsKey(target)) {
            bus.put(target, new LiveData<>());
        }
        return ((LiveData) bus.get(target));
    }

    public LiveData<Object> getChannel(String target){
        return getChannel(target, Object.class);
    }
}
