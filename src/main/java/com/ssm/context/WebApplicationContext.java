package com.ssm.context;

import com.ssm.annotation.*;
import com.ssm.bean.BeanDefinition;
import com.ssm.bean.BeanPostProcessor;
import com.ssm.mapper.Mapper;
import com.ssm.xml.XmlPasser;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zrh
 * @version 1.0.0
 * @title WebApplicationContext
 * @description <Spring容器>
 * @create 2023/11/4 00:44
 **/
public class WebApplicationContext {
    public String contextConfigLocation;

    //存放扫描到的类的路径[com.atzrh.controller.UserController, com.atzrh.service.impl.UserServiceImpl, com.atzrh.service.UserService]
    public List<String> classNameList =  new ArrayList<>();

    //BeanDefinition map
    public Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    //singletonObjects List
    public Map<String,Object> singletonObjects = new ConcurrentHashMap<>();

    //bean的后置处理器
    public ArrayList<BeanPostProcessor> beanPostProcessorsList = new ArrayList<>();


    public WebApplicationContext(String contextConfigLocation){
        this.contextConfigLocation = contextConfigLocation;
    }


    /**
     * @author zrh
     * @date 2023/11/5 23:26
     * @description <refresh>
     */
    public void refresh(){
        System.out.println("调用refresh方法...");
        //classPath:miniSSM.xml ---> miniSSM.xml ---> com.atzrh.service,com.atzrh.controller
        String basePackage = XmlPasser.getBasePackage(contextConfigLocation.split(":")[1]);
        String[] basePackages = basePackage.split(",");
        System.out.println("借用XmlPasser解析miniSSM.xml获取到需要扫描的位置："+Arrays.toString(basePackages));
        System.out.println("开始扫描");
        if (basePackages.length>0){
            for (String p : basePackages) {
                //com.atzrh.service
                //com.atzrh.controller
                scanPackage(p);
            }
        }
        System.out.println("扫描到的类："+classNameList);

        System.out.println("开始实例化");
        instantiate();
        System.out.println("实例化完成");

        System.out.println("beanDefinitionMap："+beanDefinitionMap);
        System.out.println("singletonObjects："+singletonObjects);

        System.out.println("执行依赖注入");
        autoWired();

        // TODO: 2023/11/6
        System.out.println("看是否实现InitializingBean接口，执行一些bean的初始化操作");

        // TODO: 2023/11/6
        System.out.println("遍历beanPostProcessorsList，执行后置处理器方法（AOP等）");


        System.out.println("refresh方法执行完毕");
    }


    /**
     * @author zrh
     * @date 2023/11/5 22:56
     * @description <执行依赖注入>
     */
    private void autoWired() {
        for (Map.Entry<String,Object> entry:singletonObjects.entrySet()){
            Object bean = entry.getValue();
            for (Field declaredField : bean.getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(AutoWired.class)){
                    declaredField.setAccessible(true);
                    Class<?> aClass = declaredField.getType();
                    String s = aClass.getSimpleName().substring(0, 1).toLowerCase() + aClass.getSimpleName().substring(1);
                    try {
                        declaredField.set(bean,getBean(s));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    /**
     * @author zrh
     * @date 2023/11/5 19:39
     * @description <扫描 放入classNameList>
     */
    public void scanPackage(String pass){
        //  com.atzrh.service --->  /com/atzrh/service 替换得到路径
        URL url = this.getClass().getClassLoader().getResource("/" + pass.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for(File file: dir.listFiles()){
            //如果是文件夹，递归的去查找
            if (file.isDirectory()){
                scanPackage(pass+"."+file.getName());
            }else {
                //com.atzrh.service.UserService
                String className = pass + "." + file.getName().replaceAll(".class", "");
                classNameList.add(className);
            }
        }
    }

    /**
     * @author zrh
     * @date 2023/11/5 19:41
     * @description <实例化容器>
     */
    public void instantiate(){
        if(classNameList.size()==0){
            throw new RuntimeException("classNameList里没有需要实例化的对象");
        }
        for (String className:classNameList){
            try {
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(Service.class)||aClass.isAnnotationPresent(Component.class)){
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setType(aClass);
                    //有scope注解，就判断其是单例还是原型bean 没有scope注解，默认单例
                    if (aClass.isAnnotationPresent(Scope.class)){
                        Scope scope = aClass.getAnnotation(Scope.class);
                        String scopeValue = scope.value();
                        if ("singleton".equals(scopeValue)){
                            beanDefinition.setScope("singleton");
                        }else if ("prototype".equals(scopeValue)){
                            beanDefinition.setScope("prototype");
                        }else {
                            throw new RuntimeException("无效的作用域范围");
                        }
                    }else {
                        beanDefinition.setScope("singleton");
                    }
                    if(aClass.isAnnotationPresent(Service.class)){
                        //UserServiceImpl --->  userServiceImpl
                        Class<?>[] interfaces = aClass.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            String beanName = anInterface.getSimpleName().substring(0,1).toLowerCase()+anInterface.getSimpleName().substring(1);
                            beanDefinitionMap.put(beanName,beanDefinition);
                        }
                    }else {
                        //component组件，就不用看他实现的接口了
                        String beanName = aClass.getSimpleName().substring(0,1).toLowerCase()+aClass.getSimpleName().substring(1);
                        beanDefinitionMap.put(beanName,beanDefinition);
                    }
                }
                if(aClass.isAnnotationPresent(Controller.class)){
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setType(aClass);
                    beanDefinition.setScope("singleton");
                    //UserController -->  userController
                    String beanName = aClass.getSimpleName().substring(0,1).toLowerCase()+aClass.getSimpleName().substring(1);
                    beanDefinitionMap.put(beanName,beanDefinition);
                }
                if (aClass.isAnnotationPresent(Mapper.class)){
                    String beanName = aClass.getSimpleName().substring(0,1).toLowerCase()+aClass.getSimpleName().substring(1);

                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        //初始化单例池
        for (Map.Entry<String,BeanDefinition> entry: beanDefinitionMap.entrySet()){
            if ("singleton".equals(entry.getValue().getScope())){
                singletonObjects.put(entry.getKey(), createBean(entry.getValue()));
            }
        }
    }



    public Object getBean(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new RuntimeException("beanDefinitionMap没有这个类型");
        }
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)){
            return singletonObjects.get(beanName);
        }else {
            return createBean(beanDefinition);
        }
    }
    private Object createBean(BeanDefinition beanDefinition){
        Class<?> aClass = beanDefinition.getType();
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
