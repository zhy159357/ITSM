package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.SmBizTaskinfo;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfoThree;
import com.ruoyi.common.core.domain.entity.SmBizTaskinfoTwo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
public interface SmBizTaskinfoMapper 
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
     * 删除【请填写功能名称】
     * 
     * @param taskFormId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSmBizTaskinfoById(String taskFormId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param taskFormIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmBizTaskinfoByIds(String[] taskFormIds);

    /**
     *
     * @param
     * @return
     */
    List<SmBizTaskinfoTwo> selectSmBizTaskinfoSList(SmBizTaskinfoTwo smBizTaskinfoTwo);


    /**
     *
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
     * @param
     * @return
     */
    List<SmBizTaskinfoThree> selectSmBizTaskinfoListEvo(SmBizTaskinfoThree smBizTaskinfoThree);

    /**
     *
     * @param taskId
     */
    int deleteSmBizTaskinfoZyById(String taskId);

    /**
     *
     * @param taskId
     * @return
     */
    SmBizTaskinfo selectSmBizTaskinfoByTaskId(String taskId);

    List<SmBizTaskinfoTwo> selectSmBizTaskinfoTwoSList(SmBizTaskinfoTwo smBizTaskinfoTwo);

    /**
     * 根据taskId删除
     * @param taskId
     */
    void deleteSmBizTaskinfoTaskByIds(String taskId);


    int updateSmBizTaskinfoByTaskId(SmBizTaskinfo smBizTaskinfo);


    List<SmBizTaskinfo> selectFillLxbgList(SmBizTaskinfo smBizTaskinfo);


}
