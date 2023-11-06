package com.ssm.annotation;

import java.lang.annotation.*;

/**
 * @author zrh
 * @version 1.0.0
 * @title Scope
 * @description <TODO description class purpose>
 * @create 2023/11/5 00:00
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
    String value() default "singleton";
}
