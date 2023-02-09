package com.ruoyi.form.activiti;

public class ChangeUtil {
    /*调用ADPM接口的线程池工具*/
    public static ChangeThreadPool ADPM_POOL = new ChangeThreadPool();
    /*调用告警平台接口的线程池工具*/
    public static ChangeThreadPool ALARM_POOL = new ChangeThreadPool();
    /*变更流程用*/
    public static ChangeThreadPool CHANGE_POOL = new ChangeThreadPool();

}
