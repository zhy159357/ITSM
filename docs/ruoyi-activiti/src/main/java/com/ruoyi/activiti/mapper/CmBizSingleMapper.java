package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.BgJJEfficiency;
import com.ruoyi.activiti.domain.BgJSEfficiency;
import com.ruoyi.common.core.domain.entity.CmBizSingle;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2021-01-13
 */
public interface CmBizSingleMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param changeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CmBizSingle selectCmBizSingleById(String changeId);

    public CmBizSingle selectCmBizSingleByIdApp(String changeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param cmBizSingle 【资源变更单】
     * @return 【查询我的变更单】集合
     */
    public List<CmBizSingle> selectCmBizSingleList(CmBizSingle cmBizSingle);

    /**
     * 查询【请填写功能名称】列表mysql数据库
     *
     * @param cmBizSingle 【资源变更单】
     * @return 【查询我的变更单】集合
     */
    public List<CmBizSingle> selectCmBizSingleListMysql(CmBizSingle cmBizSingle);

    /**
     * 新增【请填写功能名称】
     *
     * @param cmBizSingle 【请填写功能名称】
     * @return 结果
     */
    public int insertCmBizSingle(CmBizSingle cmBizSingle);

    /**
     * 修改【请填写功能名称】
     *
     * @param cmBizSingle 【请填写功能名称】
     * @return 结果
     */
    public int updateCmBizSingle(CmBizSingle cmBizSingle);

    /**
     * 删除【请填写功能名称】
     *
     * @param changeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCmBizSingleById(String changeId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param changeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmBizSingleByIds(String[] changeIds);

    /**
     * 查询我的变更单
     */
    public List<CmBizSingle> selectMyCmBizSingleList(CmBizSingle cmBizSingle);

    public List<CmBizSingle> selectMyCmBizSingleListMysql(CmBizSingle cmBizSingle);

    public CmBizSingle selectCmBizSingleByIdById(String changeId);

    public CmBizSingle selectCmBizSingleByIdByIdMysql(String changeId);

    public List<BgJSEfficiency> selectCmBizJs(BgJSEfficiency bgJSEfficiency);

    public List<BgJJEfficiency> selectCmBizJJ(BgJJEfficiency bgJJEfficiency);

    public List<BgJJEfficiency> selectCmBizJJMysql(BgJJEfficiency bgJJEfficiency);

    /**
     * 查询代办专用
     */
    public CmBizSingle getFlowCmBiz(CmBizSingle cmBizSingle);
    public CmBizSingle getFlowCmBizMysql(CmBizSingle cmBizSingle);

    /**
     * 根据单号查询变更单
     */
    public CmBizSingle getCmBizBycChangeCode(String changeCode);

    public List<CmBizSingle> selectCmBizByIdList(CmBizSingle cmBizSingle);
}
