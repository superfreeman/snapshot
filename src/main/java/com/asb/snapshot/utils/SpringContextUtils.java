package com.asb.snapshot.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-13
 * Time: 下午1:59
 * To change this template use File | Settings | File Templates.
 */
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    //单例模式
    private static final SpringContextUtils instance = new SpringContextUtils();

    public static SpringContextUtils instance() {
        return instance;
    }

    /**
     * 防止初始化 *
     */
    private SpringContextUtils() {
        System.out.println("SpringContextUtils construct");
    }

    /**
     * ApplicationContextAware interface ,get by spring
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * get the spring object*
     */
    public ApplicationContext getContext() {
        return applicationContext;
    }


    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }


}