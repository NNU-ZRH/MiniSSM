package com.ssm.bean;

/**
 * @author zrh
 * @version 1.0.0
 * @title BeanDefinition
 * @description <TODO description class purpose>
 * @create 2023/11/4 23:57
 **/
public class BeanDefinition {
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    private  Class<?> type;//class对象
    private String scope;//bean的作用域 singleton或者
}
