package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizTool;
import java.util.List;

/**
 * 工具使用情况Service接口
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
public interface IFmBizToolService 
{
    /**
     * 查询工具使用情况
     * 
     * @param fbtId 工具使用情况ID
     * @return 工具使用情况
     */
    public FmBizTool selectFmBizToolById(String fbtId);

    /**
     * 查询工具使用情况列表
     * 
     * @param fmBizTool 工具使用情况
     * @return 工具使用情况集合
     */
    public List<FmBizTool> selectFmBizToolList(FmBizTool fmBizTool);

    /**
     * 新增工具使用情况
     * 
     * @param fmBizTool 工具使用情况
     * @return 结果
     */
    public int insertFmBizTool(FmBizTool fmBizTool);

    /**
     * 修改工具使用情况
     * 
     * @param fmBizTool 工具使用情况
     * @return 结果
     */
    public int updateFmBizTool(FmBizTool fmBizTool);

    /**
     * 批量删除工具使用情况
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizToolByIds(String ids);

    /**
     * 删除工具使用情况信息
     * 
     * @param fbtId 工具使用情况ID
     * @return 结果
     */
    public int deleteFmBizToolById(String fbtId);
    /**
     * 查看工具使用情况
     */
    public List<FmBizTool> selectFmBizToolAppr(FmBizTool fmBizTool);
    /**
     * 查看工具使用情况
     */
    public List<FmBizTool> selectFmBizToolAppr2(FmBizTool fmBizTool);
}
