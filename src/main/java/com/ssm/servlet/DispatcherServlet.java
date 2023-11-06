package com.ssm.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.annotation.Controller;
import com.ssm.annotation.RequestMapping;
import com.ssm.annotation.RequestPara;
import com.ssm.annotation.ResponseBody;
import com.ssm.context.WebApplicationContext;
import com.ssm.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zrh
 * @version 1.0.0
 * @title DispatcherServlet
 * @description <中央控制器>
 * @create 2023/11/3 21:21
 **/
public class DispatcherServlet extends HttpServlet {
    WebApplicationContext webApplicationContext;
    //存储url 对象 方法的映射关系
    List<Handler> handlerList = new ArrayList<>();
    /**
     * @author zrh
     * @date 2023/11/4 00:46
     * @description <web.xml文件里配置了Tomcat一启动就会就会创建这个servlet 所以可以在这初始化容器>
     */
    @Override
    public void init(){
        System.out.println("DispatcherServlet init...  begin");
        //获取初始化参数classPath:miniSSM.xml
        String contextConfigLocation = this.getServletConfig().getInitParameter("contextConfigLocation");
        System.out.println("获取到DispatcherServlet初始化参数：MiniSSM的配置文件位置："+contextConfigLocation);
        System.out.println("开始创建Spring容器");
        webApplicationContext = new WebApplicationContext(contextConfigLocation);
        System.out.println("初始化Spring容器");
        webApplicationContext.refresh();

        initHandleMapping();
        System.out.println("初始化请求映射关系:"+handlerList.get(0)+"放入到handlerList");

        System.out.println("DispatcherServlet init...  over");
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //进行请求分发
        System.out.println("进行请求分发");
        dispatching(req,resp);
    }

    /**
     * @author zrh
     * @date 2023/11/6 00:19
     * @description <初始化请求映射>
     */
    private void initHandleMapping(){
        for (Map.Entry<String,Object> entry:webApplicationContext.singletonObjects.entrySet()){
            Class<?> aClass = entry.getValue().getClass();
            if (aClass.isAnnotationPresent(Controller.class)){
                for (Method declaredMethod : aClass.getDeclaredMethods()) {
                    if (declaredMethod.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMappingAnnotation = declaredMethod.getAnnotation(RequestMapping.class);
                        String url = requestMappingAnnotation.value();
                        Handler handler = new Handler(url, entry.getValue(), declaredMethod);
                        handlerList.add(handler);
                    }
                }

            }
        }
    }


    /**
     * @author zrh
     * @date 2023/11/6 00:54
     * @description <进行请求的分发>
     */
    private void dispatching(HttpServletRequest req, HttpServletResponse resp) {
        Handler handler = getHandler(req);
        if (handler==null){
            try {
                resp.getWriter().print("<h1>404 NOT FOUND!</h1>");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("当前请求路径是："+handler.getUrl());
            Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
            //定义一个参数数组
            Object[] params = new Object[parameterTypes.length];
            //1. 填充HTTP参数
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if ("HttpServletRequest".equals(parameterType.getSimpleName())){
                    params[i] = req;
                }else if ("HttpServletResponse".equals(parameterType.getSimpleName())){
                    params[i] = resp;
                }
            }
            //2. 获取请求中的参数填充到参数数组里
            Map<String, String[]> reqParameterMap = req.getParameterMap();
            for (Map.Entry<String, String[]> entry : reqParameterMap.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue()[0];

                Parameter[] parameters = handler.getMethod().getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i].isAnnotationPresent(RequestPara.class)){
                        String value = parameters[i].getAnnotation(RequestPara.class).value();
                        if (value.equals(paramName)){
                            params[i] = paramValue;
                        }
                    }
                }
            }
            System.out.println("反射获取请求参数:"+ Arrays.toString(params));
            try {
                Object returnValue = handler.getMethod().invoke(handler.getController(), params);

                //返回的是一个字符串
                if (returnValue instanceof String){
                    String returnValue1 = (String) returnValue;
                    if (returnValue1.contains(":")){
                        String[] split = returnValue1.split(":");
                        String viewType = split[0];
                        String viewPage = split[1];
                        if (viewType.equals("forward")){
                            System.out.println("请求转发："+viewPage);
                            req.getRequestDispatcher(viewPage).forward(req,resp);
                        }else if (viewType.equals("redirect")){
                            System.out.println("请求重定向："+viewPage);
                            resp.sendRedirect(viewPage);
                        }else {
                            System.out.println("请求转发："+returnValue);
                            req.getRequestDispatcher(returnValue1).forward(req,resp);
                        }
                    }
                }
                //返回JSON
                else {
                    if (handler.getMethod().isAnnotationPresent(ResponseBody.class)){
                        //把返回值用JSON工具转换为JSON字符串
                        String json = new ObjectMapper().writeValueAsString(returnValue);
                        System.out.println("返回json:"+json);
                        PrintWriter out = resp.getWriter();
                        out.print(json);
                        out.flush();
                        out.close();
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException | ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Handler getHandler(HttpServletRequest req){
        String requestURL = req.getRequestURI();
        for (Handler handler : handlerList) {
            if (requestURL.equals(handler.getUrl()))return handler;
        }
        return null;
    }
}
