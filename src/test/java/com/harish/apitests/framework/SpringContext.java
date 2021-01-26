package com.harish.apitests.framework;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringContext {
    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public static ApplicationContext getApplicationContext(){return context;}

}
