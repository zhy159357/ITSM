package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.core.domain.entity.VmBizTaskinfo;

import java.util.List;
import java.util.Map;

/**
 * 版本任务Service接口
 *
 * @author ruoyi
 * @date 2021-01-06
 */
public interface IVmBizTaskinfoService {
    /**
     * 查询版本任务
     *
     * @param taskId 版本任务ID
     * @return 版本任务
     */
    public VmBizTaskinfo selectVmBizTaskinfoById(String taskId);

    /**
     * 查询版本任务
     *
     * @param taskNo 版本任务编号
     * @return 版本任务
     */
    public VmBizTaskinfo selectVmBizTaskinfoByTaskNo(String taskNo);

    /**
     * 查询版本任务列表
     *
     * @param vmBizTaskinfo 版本任务
     * @return 版本任务集合
     */
    public List<VmBizTaskinfo> selectVmBizTaskinfoList(VmBizTaskinfo vmBizTaskinfo);

    /**
     * union all 联查
     * @param vmBizTaskinfo
     * @return
     */
    public List<VmBizTaskinfo> selectVmBizTaskinfoListUnion(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 新增版本任务
     *
     * @param vmBizTaskinfo 版本任务
     * @return 结果
     */
    public int insertVmBizTaskinfo(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 修改版本任务
     *
     * @param vmBizTaskinfo 版本任务
     * @return 结果
     */
    public int updateVmBizTaskinfo(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 批量删除版本任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteVmBizTaskinfoByIds(String ids);

    /**
     * 删除版本任务信息
     *
     * @param taskId 版本任务ID
     * @return 结果
     */
    public int deleteVmBizTaskinfoById(String taskId);

    /**
     * 下载版本
     *
     * @param vmBiztaskInfo
     * @return
     */
    public int downloadVersion(VmBizTaskinfo vmBiztaskInfo);

    /**
     * 确认版本
     *
     * @param vmBiztaskInfo
     * @return
     */
    public int completeVersion(VmBizTaskinfo vmBiztaskInfo);

    /**
     * 向指定人员发送短信信息
     *
     * @param pId
     * @param msg
     */
    void sendMsg(String pId, String msg);

    /**
     * 启动自动化
     *
     * @param vmBizInfo
     * @param attachment
     * @return
     */
    Map sendMessageInstanceStartup(VmBizInfo vmBizInfo, Attachment attachment);

    /**
     * 查询自动化结果
     * @param vmBizInfo
     * @param attachment
     * @return
     */
    List<Map> selectResultMsg(VmBizInfo vmBizInfo, Attachment attachment);

    /**
     * 判断升级后评估页面是否上传截图
     * @param taskId
     * @return
     */
    boolean judgeVersionTaskInfoImage(String taskId);
}
