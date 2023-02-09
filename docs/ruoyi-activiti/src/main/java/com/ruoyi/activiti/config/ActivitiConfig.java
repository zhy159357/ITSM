package com.ruoyi.activiti.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private ICustomProcessDiagramGenerator customProcessDiagramGenerator;

    /**
     * 解決工作流生成图片乱码问题
     *
     * @param processEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setProcessDiagramGenerator(customProcessDiagramGenerator);
       // processEngineConfiguration.setAsyncExecutor(true);//定义为true，使用AsyncExecutor代替默认的JobExecutor;
       // processEngineConfiguration.setJobExecutorActivate(true);
        //processEngineConfiguration.setAsyncExecutorActivate(true);//定义为true，工作流引擎在启动时就建立启动AsyncExecutor线程
    }
}
