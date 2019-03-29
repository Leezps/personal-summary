package com.leezp.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)    //元注解，作用在注解之上
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBus {
    //事件的3个重要成员

    //1.setOnXXXClickListener 方法名
    String listenerSetter();

    //2.监听的对象   View.OnXXXClickListener
    Class<?> listenerType();

    //3.回调方法 onXXXClick(View v)
    String callBackListener();
}
