package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.FmBizKh;

import java.util.List;

/**
 * 2021业务事件考核Service接口
 * 
 * @author ruoyi
 * @date 2021-03-15
 */
public interface IFmBizKhService 
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
     * 批量删除2021业务事件考核
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizKhByIds(String ids);

    /**
     * 删除2021业务事件考核信息
     * 
     * @param kid 2021业务事件考核ID
     * @return 结果
     */
    public int deleteFmBizKhById(String kid);

    /**
     * 根据传入年月转换格式
     * @param dateTime
     * @return
     */
    public String getDateTime(String dateTime);

    /**
     * 根据系统ID和处理时间查询已关闭事件单满意率
     * @param fmBizKh
     * @return
     */
    public FmBizKh getFmBizSatisfaction(FmBizKh fmBizKh);
}
