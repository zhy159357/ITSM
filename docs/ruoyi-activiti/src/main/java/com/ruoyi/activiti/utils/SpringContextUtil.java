package com.ruoyi.activiti.utils;

import com.ruoyi.activiti.adapter.IEndTaskAdapter;
import com.ruoyi.activiti.service.impl.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 容器中获取对应名称的Bean对象的工具类
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    /**
     * 自动注入容器对象
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextUtil.applicationContext==null){
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 根据对应的参数获取不同的bean对象
     * @param processKey
     * @return
     * @throws BeansException
     */
    public static IEndTaskAdapter getBean(String processKey) throws BeansException {

        IEndTaskAdapter endTaskAdapter = null;
        if (SpringContextUtil.getApplicationContext() == null){
            return null;
        }
        switch (processKey){
            case "FmBiz":
                endTaskAdapter = (EndTaskAdapterImplFmBiz)applicationContext.getBean("endTaskAdapterImplFmBiz");
                break;
            case "FmSw":
                endTaskAdapter = (EndTaskAdapterImplFmSw)applicationContext.getBean("endTaskAdapterImplFmSw");
                break;
            case "FmDd":
                endTaskAdapter = (EndTaskAdapterImplFmDd)applicationContext.getBean("endTaskAdapterImplFmDd");
                break;
            case "FmSJBG":
                endTaskAdapter = (EndTaskAdapterImplFmSJBG)applicationContext.getBean("endTaskAdapterImplFmSJBG");
                break;
            case "FmYx":
                endTaskAdapter = (EndTaskAdapterImplFmYx)applicationContext.getBean("endTaskAdapterImplFmYx");
                break;
            case "PMYH":
                endTaskAdapter = (EndTaskAdapterImplPMYH)applicationContext.getBean("endTaskAdapterImplPMYH");
                break;
            case "PMYY":
                endTaskAdapter = (EndTaskAdapterImplPMYY)applicationContext.getBean("endTaskAdapterImplPMYY");
                break;
            case "CmZy":
                endTaskAdapter = (EndTaskAdapterImplCmZy)applicationContext.getBean("endTaskAdapterImplCmZy");
                break;
            case "VmBn":
                endTaskAdapter = (EndTaskAdapterImplVmBn)applicationContext.getBean("endTaskAdapterImplVmBn");
                break;
            case "BGQQ":
                endTaskAdapter = (EndTaskAdapterImplBGQQ)applicationContext.getBean("endTaskAdapterImplBGQQ");
                break;
        }

        return endTaskAdapter;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
