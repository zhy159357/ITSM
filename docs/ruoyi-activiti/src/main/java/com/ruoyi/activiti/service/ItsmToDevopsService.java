package com.ruoyi.activiti.service;

import java.util.Map;

public interface ItsmToDevopsService {
    /**
     * 发送网络自动化excl及相关信息
     *
     * @param workId        单号
     * @param worktTitle    变更主题
     * @param planBeginTime 计划开始时间
     * @param planEndTime   计划结束时间
     * @param groupName     附件所属工作组
     * @param fileFast      fast附件名
     * @return state-状态 mes-返回信息
     */
    public Map<String, Object> toAutoByExcl(String workId, String worktTitle,String planImplPer,String planReiwPer, String ifAuto, String planBeginTime, String planEndTime, String groupName, String fileFast);

    /**
     * 根据变更单号去网络自动化获取编排任务状态验证是否可提交变更单
     *
     * @param changeCode 变更单号
     * @return Map 状态 pdf附件的所属工作组和fast文件名
     */
    public Map<String, String> getFinalPdfPath(String changeCode);

    /**
     * 传入信息调用网络自动化接口启动预启任务
     *
     * @param serialNumber  单号
     * @param worktTitle    主题
     * @param applicantId   操作人
     * @param planImplPer   实施人
     * @param planReiwPer   复核人
     * @param ifauto        是否手工启动
     * @param planBeginTime 计划开始时间
     * @param planEndTime   计划结束时间
     * @return
     */
    public Map<String, String> startChangeTask(String serialNumber, String worktTitle, String applicantId, String planImplPer,
                                               String planReiwPer, String ifauto, String planBeginTime, String planEndTime);

    public String toPage(String workId, String worktTitle, String planImplPer,
                         String planReiwPer,String ifAuto, String planBeginTime, String planEndTime);


}
