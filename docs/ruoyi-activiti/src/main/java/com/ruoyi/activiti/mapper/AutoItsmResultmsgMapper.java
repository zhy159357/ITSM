package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.AutoItsmResultmsg;

import java.util.List;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2021-03-22
 */
public interface AutoItsmResultmsgMapper 
{
    /**
     * 查询
     * 
     * @param resultId
     * @return
     */
    public AutoItsmResultmsg selectAutoItsmResultmsgById(String resultId);

    /**
     * 查询
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
     * 删除
     * 
     * @param resultId
     * @return 结果
     */
    public int deleteAutoItsmResultmsgById(String resultId);

    /**
     * 批量删除
     * 
     * @param resultIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteAutoItsmResultmsgByIds(String[] resultIds);
}
