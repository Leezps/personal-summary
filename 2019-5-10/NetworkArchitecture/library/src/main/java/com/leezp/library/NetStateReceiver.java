package com.leezp.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.leezp.library.annotation.Network;
import com.leezp.library.bean.MethodManager;
import com.leezp.library.listener.NetChangeObserver;
import com.leezp.library.type.NetType;
import com.leezp.library.utils.Constants;
import com.leezp.library.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;    // 网络类型
    //    private NetChangeObserver listener;     // 网络监听
    private Map<Object, List<MethodManager>> networkList;

    public NetStateReceiver() {
        //  初始化网络
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

//    public void setListener(NetChangeObserver listener) {
//        this.listener = listener;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        //处理广播的事件
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.LOG_TAG, "onReceive: 网络发生改变了");
            netType = NetworkUtils.getNetType();    // 获取当前网络类型
            if (NetworkUtils.isNetworkAvailable()) {    // 判断有无网络
                Log.e(Constants.LOG_TAG, "网络连接成功");
//                if (listener != null) {
//                    listener.onConnect(netType);
//                }
            } else {
                Log.e(Constants.LOG_TAG, "没有网络连接");
//                if (listener != null) {
//                    listener.onDisConnect();
//                }
            }
            post(netType);
        }
    }

    // 消息分发到所有的Activity
    private void post(NetType netType) {
        //  获取所有的Activity
        Set<Object> set = networkList.keySet();
        // 循环所有的Activity
        for (Object getter : set) {
            List<MethodManager> methodList = networkList.get(getter);
            if (methodList != null) {
                // 循环每个方法，发送网络变更消息
                for (MethodManager method : methodList) {
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, getter, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager method, Object getter, NetType netType) {
        Method execute = method.getMethod();
        try {
            execute.invoke(getter, netType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  注册
    public void registerObserver(Object register) {
        // 将MainActivity中所有网络注解的监听方法加入集合
        List<MethodManager> methodList = networkList.get(register);
        if (methodList == null) {
            //  开始添加
            methodList = findAnnotationMethod(register);
            networkList.put(register, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodList = new ArrayList<>();

        Class<?> clazz = register.getClass();
        //  获取register中的所有方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            // 获取方法的注解
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }
            // 注解方法的校验
            Type returnType = method.getGenericReturnType();
            if (!"void".equalsIgnoreCase(returnType.toString())) {
                Log.e(Constants.LOG_TAG, method.getName() + "方法返回不是void");
                // 抛异常
                throw new RuntimeException(register.getClass().getSimpleName() + "中的" + method.getName() + "方法返回不是void");
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(register.getClass().getSimpleName() + "中的" + method.getName() + "方法有且只有一个参数");
            }
            //  过滤掉不需要的方法，找到了开始添加到集合
            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodList.add(manager);
        }
        return methodList;
    }

    public void unRegisterObserver(Object register) {
        if (!networkList.isEmpty()) {
            networkList.remove(register);
        }
        Log.e(Constants.LOG_TAG, register.getClass().getName() + "注销成功");
    }

    //  移除所有
    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
        // 注销广播
        NetworkManager.getDefault().getApplication().unregisterReceiver(this);
        networkList = null;
        Log.e(Constants.LOG_TAG, "注销所有监听成功");
    }
}
