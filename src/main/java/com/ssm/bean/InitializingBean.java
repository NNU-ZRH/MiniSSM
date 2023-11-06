package com.ssm.bean;

/**
 * @author zrh
 * @version 1.0.0
 * @title InitializingBean
 * @description <bean的初始化方法>
 * @create 2023/11/5 23:47
 **/
public interface InitializingBean {
    void afterPropertiesSet();
}
