package com.ssm.mapper;

import java.lang.annotation.*;

/**
 * @author zrh
 * @version 1.0.0
 * @title Select
 * @description <TODO description class purpose>
 * @create 2023/11/6 15:21
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Select {
    String value() default "";
}
