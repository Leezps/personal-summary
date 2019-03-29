package com.leezp.library;

import android.app.Activity;
import android.view.View;

import com.leezp.library.annotation.ContentView;
import com.leezp.library.annotation.EventBus;
import com.leezp.library.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {
    public static void inject(Activity activity) {
        //布局注入
        injectLayout(activity);

        //控件注入
        injectViews(activity);

        //事件注入
        injectEvents(activity);
    }

    //布局注入
    private static void injectLayout(Activity activity) {
        // 获取类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取类的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            //获取到布局的值
            int layoutId = contentView.value();
            //第一种方式：
//            activity.setContentView(layoutId);
            //第二种方式（反射）
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //控件注入
    private static void injectViews(Activity activity) {
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取类的所有属性
        Field[] fields = clazz.getDeclaredFields();
        //循环每个属性
        for (Field field : fields) {
            //获取属性上的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {  //并不是都有属性都有这个注解
                //获取注解的值
                int viewId = injectView.value();
                //设置该属性可以访问，哪怕是private修饰的，所以后面才可以对它使用set方法
                field.setAccessible(true);
//                //第一种方式
//                View view = activity.findViewById(viewId);
//                try {
//                    field.set(activity, view);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //第二种方式（反射）
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //事件注入
    private static void injectEvents(Activity activity) {
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取当前所有方法
        Method[] methods = clazz.getDeclaredMethods();
        //遍历
        for (Method method : methods) {
            //获取每个方法的注解，可能有多个注解
            Annotation[] annotations = method.getAnnotations();
            //遍历注解
            for (Annotation annotation : annotations) {
                //获取OnClick注解上的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    EventBus eventBus = annotationType.getAnnotation(EventBus.class);
                    //事件3个重要成员
                    String listenerSetter = eventBus.listenerSetter();
                    Class<?> listenerType = eventBus.listenerType();
                    String callBackListener = eventBus.callBackListener();

                    try {
                        //通过annotationType获取onClick注解的value值，拿到R.id.xxx
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                        //R.id.tv,R.id.btn
                        int[] viewIds = (int[]) valueMethod.invoke(annotation);

                        //拦截方式，执行自定义方法
                        ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                        handler.addMethod(callBackListener, method);
                        //代理方式完成
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                        for (int viewId : viewIds) {
                            // 获取当前的view
                            View view = activity.findViewById(viewId);
                            // 获取方法
                            Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                            setter.invoke(view, listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
