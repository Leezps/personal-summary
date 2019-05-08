package com.leezp.eventbus.bus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus {

    private Map<Object, List<SubscribleMethod>> cacheMap;
    private static volatile EventBus instance;
    private Handler mHandler;

    private EventBus() {
        cacheMap = new HashMap<>();
        mHandler = new Handler();
    }

    public static EventBus getDefault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object obj) {
        List<SubscribleMethod> list = cacheMap.get(obj);
        if (list == null) {
            list = findSubscribleMethods(obj);
            cacheMap.put(obj, list);
        }
        // TODO 循环带有 stricky 的方法 map
    }

    private List<SubscribleMethod> findSubscribleMethods(Object obj) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            //凡是系统级别的父类，直接省略
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 找到带有subscrible注册的方法
                Subscrible subscrible = method.getAnnotation(Subscrible.class);
                if (subscrible == null) {
                    continue;
                }
                // 判断带有subscrible注解的方法中的参数类型
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    Log.e("错误", "eventbus only accept one para");
                    continue;
                }
                ThreadMode threadMode = subscrible.threadMode();
                // TODO 获取是否有 stricky，就直接保存
                SubscribleMethod subscribleMethod = new SubscribleMethod(method, threadMode, types[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public void post(final Object type) {
        // 直接循环cachemap然后找到对应的方法进行回调
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object obj = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(obj);
            for (final SubscribleMethod subscribleMethod : list) {
                // a (if条件前面的对象) 对象所对应的类是不是b (if条件后面的对象) 对象所对应的类信息的父类或者接口
                if (subscribleMethod.getType().isAssignableFrom(type.getClass())) {
                    switch (subscribleMethod.getmThreadMode()) {
                        case MAIN:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                // 主线程-主线程
                                invoke(subscribleMethod, obj, type);
                            } else {
                                // 子线程-主线程
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, obj, type);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:
                            // TODO 主线程-子线程

                            // TODO 子线程-子线程
                            break;
                    }
                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object obj, Object type) {
        Method method = subscribleMethod.getmMethod();
        try {
            method.invoke(obj, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
