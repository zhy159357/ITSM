package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.VmBizInfo;

import java.util.List;
import java.util.Map;

/**
 * 版本发布Service接口
 * 
 * @author ruoyi
 * @date 2020-12-15
 */
public interface IVmBizInfoService 
{
    /**
     * 查询列表
     * 
     * @param vmBizInfo
     * @return 集合
     */
    public List<VmBizInfo> selectVmBizInfoList(VmBizInfo vmBizInfo);

    /**
     * 根据id查询
     *
     * @param id
     * @return 集合
     */
    public VmBizInfo selectVmBizInfoById(String id);

    /**
     * 根据id查询
     *
     * @param ids
     * @return 集合
     */
    public List<VmBizInfo> selectVmBizInfoByIdList(List<String> ids);

    /**
     * 根据id查询
     *
     * @param id
     * @return 集合
     */
    public VmBizInfo selectVmBizInfoByIdNoConvert(String id);

    /**
     * 根据版本号查询
     *
     * @param versionInfoNo
     * @return 集合
     */
    public VmBizInfo selectVmBizInfoByNo(String versionInfoNo);

    /**
     * 根据条件查询
     *
     * @param vmBizInfo
     * @return
     */
    public VmBizInfo selectVmBizInfoByCondition(VmBizInfo vmBizInfo);

    /**
     * 定时任务查询用
     *
     * @param id
     * @return
     */
    public VmBizInfo selectVmBizInfoByQuartz(String id);

    /**
     * 根据版本单号查询总数
     * @param versionInfoNo
     * @return
     */
    public int selectCountByVersionInfoNo(String versionInfoNo);

    /**
     * 新增
     * 
     * @param vmBizInfo
     * @return 结果
     */
    public int insertVmBizInfo(VmBizInfo vmBizInfo);

    /**
     * 修改
     * 
     * @param vmBizInfo
     * @return 结果
     */
    public int updateVmBizInfo(VmBizInfo vmBizInfo);

    /**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteVmBizInfoByIds(String ids);

    /**
     * 过滤业务部门数据
     * @param vmBizInfo
     * @return
     */
    public List<Map> getBusinessList(VmBizInfo vmBizInfo);

    /**
     * 生成版本单名称
     * @param vmBizInfo
     */
    public void getVersionInfoName(VmBizInfo vmBizInfo);

    /**
     * 获取分行|技术局｜总行中心|版本类型的复选框html字符串
     * @param vmBizInfo
     * @return
     */
    public Map getOrgMap(VmBizInfo vmBizInfo);

    /**
     * 启动流程
     * @param vmBizInfo
     */
    public void startProcess(VmBizInfo vmBizInfo);

    /**
     * 发送短信
     * @param setSmsText
     * @param person
     */
    public void sendSms(String setSmsText, OgPerson person);

    /**
     * 生成版本任务并发送短信
     * @param vmBizInfo
     */
    public void saveVmBizTaskInfo(VmBizInfo vmBizInfo);

    /**
     * 根据版本发布Id查询excel附件并发送自动化校验
     * 校验通过返回true，校验不通过或者自动化标识为手动的情况返回false
     * @param vmBizInfo
     * @return
     */
    public Map checkVersionAttachmentExcel(VmBizInfo vmBizInfo);

    /**
     * 版本发布单作废并结束工作流
     * 作废状态为2、3、4、6、7、8
     */
    public void cancelVmBizInfoByVersionStatus();

    /**
     * 撤回流程通知自动化删除数据
     * @param vmBizInfo
     */
    public void forbiddenVersion(VmBizInfo vmBizInfo);

    /**
     * 关闭流程单
     * @param id
     */
    public void closeVmBizVersion(String id);

    /**
     * 查询版本审核待办
     * @param userId
     * @param type
     * @return
     */
    public List<Map<String, Object>> getVmBnUserTask(String userId, String type);

    public AjaxResult vmBnAppApproval(String userId, String bizId, String comment, String trend, String vmBnApprovalFlag, String jjspApprovalId);

    /**
     * 根据单号查询状态
     * @param versionInfoNo
     * @return
     */
    public String selectStatusByVersionInfoNo(String versionInfoNo);
}
