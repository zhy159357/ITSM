package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.FmBizKh;

import java.util.List;

/**
 * 2021业务事件考核Mapper接口
 * 
 * @author liul
 * @date 2021-03-15
 */
public interface FmBizKhMapper 
{
    /**
     * 查询2021业务事件考核
     * 
     * @param kid 2021业务事件考核ID
     * @return 2021业务事件考核
     */
    public FmBizKh selectFmBizKhById(String kid);

    /**
     * 查询2021业务事件考核列表
     * 
     * @param fmBizKh 2021业务事件考核
     * @return 2021业务事件考核集合
     */
    public List<FmBizKh> selectFmBizKhList(FmBizKh fmBizKh);

    /**
     * 新增2021业务事件考核
     * 
     * @param fmBizKh 2021业务事件考核
     * @return 结果
     */
    public int insertFmBizKh(FmBizKh fmBizKh);

    /**
     * 修改2021业务事件考核
     * 
     * @param fmBizKh 2021业务事件考核
     * @return 结果
     */
    public int updateFmBizKh(FmBizKh fmBizKh);

    /**
     * 删除2021业务事件考核
     * 
     * @param kid 2021业务事件考核ID
     * @return 结果
     */
    public int deleteFmBizKhById(String kid);

    /**
     * 批量删除2021业务事件考核
     * 
     * @param kids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizKhByIds(String[] kids);

    public FmBizKh getFmBizSatisfaction(FmBizKh fmBizKh);
}
