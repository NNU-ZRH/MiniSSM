package com.ssm.annotation;

import java.lang.annotation.*;

/**
 * @author zrh
 * @version 1.0.0
 * @title AutoWired
 * @description <TODO description class purpose>
 * @create 2023/11/3 23:58
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWired {
}
