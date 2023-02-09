package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmBizTool;
import java.util.List;

/**
 * 工具使用情况Mapper接口
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
public interface FmBizToolMapper 
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
     * 删除工具使用情况
     * 
     * @param fbtId 工具使用情况ID
     * @return 结果
     */
    public int deleteFmBizToolById(String fbtId);

    /**
     * 批量删除工具使用情况
     * 
     * @param fbtIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizToolByIds(String[] fbtIds);
    /**
     * 查看工具使用情况统计
     */
    public List<FmBizTool> selectFmBizToolAppr(FmBizTool fmBizTool);
    /**
     * 查看工具使用情况统计
     */
    public List<FmBizTool> selectFmBizToolAppr2(FmBizTool fmBizTool);
}
