package com.ssm.annotation;

import java.lang.annotation.*;

/**
 * @author zrh
 * @version 1.0.0
 * @title ResponseBody
 * @description <TODO description class purpose>
 * @create 2023/11/6 11:50
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
}
