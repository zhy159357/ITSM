package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.SmBizTaskinfo;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfoThree;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfoTwo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
public interface ISmBizTaskinfoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param taskFormId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SmBizTaskinfo selectSmBizTaskinfoById(String taskFormId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SmBizTaskinfo> selectSmBizTaskinfoList(SmBizTaskinfo smBizTaskinfo);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SmBizTaskinfo> selectSmBizTaskinfoListtwo(SmBizTaskinfo smBizTaskinfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 结果
     */
    public int insertSmBizTaskinfo(SmBizTaskinfo smBizTaskinfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param smBizTaskinfo 【请填写功能名称】
     * @return 结果
     */
    public int updateSmBizTaskinfo(SmBizTaskinfo smBizTaskinfo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmBizTaskinfoByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param taskFormId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSmBizTaskinfoById(String taskFormId);

    /**
     * 查询例行作业任务
     * @param
     * @return
     */
    List<SmBizTaskinfoTwo> selectSmBizTaskinfoSList(SmBizTaskinfoTwo smBizTaskinfoTwo);

    /**
     * 查询作业信息
     * @param id
     * @return
     */
    SmBizTaskinfo selectSmBizTaskinfoSelById(String id);

    /**
     * 自動發佈查詢
     */
    public List<SmBizTaskinfo> selectPubLishTask();

    /**
     * 查询任务列表
     * @param smBizTaskinfoThree
     * @return
     */
    List<SmBizTaskinfoThree> selectSmBizTaskinfoListEvo(SmBizTaskinfoThree smBizTaskinfoThree);

    /**
     * 根据taskId查询
     * @param taskId
     */
    int deleteSmBizTaskinfoZyById(String taskId);

    /**
     * 根据taskId查询列表
     * @param taskId
     * @return
     */
    SmBizTaskinfo selectSmBizTaskinfoByTaskId(String taskId);


    /**
     * 导出列表展示
     * @param smBizTaskinfoTwo
     * @return
     */
    List<SmBizTaskinfoTwo> selectSmBizTaskinfoTwoSList(SmBizTaskinfoTwo smBizTaskinfoTwo);

    /**
     * 根据taskId修改任务表信息
     * @param smBizTaskinfo
     */
    public int updateSmBizTaskinfoByTaskId(SmBizTaskinfo smBizTaskinfo);


    /**
     * 查询待填报的任务单
     * @param smBizTaskinfo
     * @return
     */
    List<SmBizTaskinfo> selectFillLxbgList(SmBizTaskinfo smBizTaskinfo);


}
