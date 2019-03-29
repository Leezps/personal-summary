package com.leezp.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)      //该注解作用在属性之上
@Retention(RetentionPolicy.RUNTIME)     //jvm运行时通过反射获取该注解的值
public @interface InjectView {
    int value();
}
