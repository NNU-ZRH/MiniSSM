package com.ssm.annotation;

import java.lang.annotation.*;

/**
 * @author zrh
 * @version 1.0.0
 * @title RequestPara
 * @description <TODO description class purpose>
 * @create 2023/11/4 00:00
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPara {
    String value() default "";
}
