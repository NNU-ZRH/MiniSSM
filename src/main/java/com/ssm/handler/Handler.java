package com.ssm.handler;

import java.lang.reflect.Method;

/**
 * @author zrh
 * @version 1.0.0
 * @title Handler
 * @description <url 对象 方法的映射关系>
 * @create 2023/11/6 00:25
 **/
public class Handler {
    private String url;
    private Object controller;
    private Method method;

    @Override
    public String toString() {
        return "Handler{" +
                "url='" + url + '\'' +
                ", controller=" + controller +
                ", method=" + method +
                '}';
    }

    public Handler() {
    }

    public Handler(String url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
