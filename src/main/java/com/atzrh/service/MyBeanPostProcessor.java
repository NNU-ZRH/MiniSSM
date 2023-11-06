package com.atzrh.service;

import com.ssm.annotation.Service;
import com.ssm.bean.BeanPostProcessor;

/**
 * @author zrh
 * @version 1.0.0
 * @title MyBeanPostProcessor
 * @description <TODO description class purpose>
 * @create 2023/11/5 23:58
 **/
@Service
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {

    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {

    }
}
