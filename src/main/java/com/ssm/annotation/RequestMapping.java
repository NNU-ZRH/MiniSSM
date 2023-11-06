package com.ssm.annotation;

import java.lang.annotation.*;

/**
 * @author zrh
 * @version 1.0.0
 * @title RequestMapping
 * @description <TODO description class purpose>
 * @create 2023/11/3 23:59
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
