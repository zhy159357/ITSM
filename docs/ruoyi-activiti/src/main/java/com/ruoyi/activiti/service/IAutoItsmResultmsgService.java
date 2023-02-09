package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.entity.AutoItsmResultmsg;

import java.util.List;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2021-03-22
 */
public interface IAutoItsmResultmsgService 
{
    /**
     * 查询
     * 
     * @param resultId
     * @return
     */
    public AutoItsmResultmsg selectAutoItsmResultmsgById(String resultId);

    /**
     * 查询列表
     * 
     * @param autoItsmResultmsg
     * @return 集合
     */
    public List<AutoItsmResultmsg> selectAutoItsmResultmsgList(AutoItsmResultmsg autoItsmResultmsg);

    /**
     * 新增
     * 
     * @param autoItsmResultmsg
     * @return 结果
     */
    public int insertAutoItsmResultmsg(AutoItsmResultmsg autoItsmResultmsg);

    /**
     * 修改
     * 
     * @param autoItsmResultmsg
     * @return 结果
     */
    public int updateAutoItsmResultmsg(AutoItsmResultmsg autoItsmResultmsg);

    /**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAutoItsmResultmsgByIds(String ids);

    /**
     * 删除信息
     * 
     * @param resultId
     * @return 结果
     */
    public int deleteAutoItsmResultmsgById(String resultId);
}
