package com.ssm.bean;

/**
 * @author zrh
 * @version 1.0.0
 * @title BeanPostProcessor
 * @description <bean的后置处理器>
 * @create 2023/11/5 23:49
 **/
public interface BeanPostProcessor {
    void postProcessBeforeInitialization(String beanName,Object bean);
    void postProcessAfterInitialization(String beanName,Object bean);
}
