package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.BgJJEfficiency;
import com.ruoyi.activiti.domain.BgJSEfficiency;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.OgPerson;

import java.util.List;

/**
 * 【资源变更单】Service接口
 *
 * @author ruoyi
 * @date 2021-01-13
 */
public interface ICmBizSingleService {
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
     * @param cmBizSingle 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CmBizSingle> selectCmBizSingleList(CmBizSingle cmBizSingle);

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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmBizSingleByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param changeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCmBizSingleById(String changeId);

    /**
     * 查询我的变更单
     */
    public List<CmBizSingle> selectMyCmBizSingleList(CmBizSingle cmBizSingle);

    public List<OgPerson> getFucheck();

    public CmBizSingle selectCmBizSingleByIdById(String changeId);

    public List<BgJSEfficiency> selectCmBizJs(BgJSEfficiency bgJSEfficiency);

    public List<BgJJEfficiency> selectCmBizJJ(BgJJEfficiency bgJJEfficiency);

    /**
     * 查询代办专用
     */
    public CmBizSingle getFlowCmBiz(CmBizSingle cmBizSingle);

    /**
     * 根据单号查询变更单
     */
    public CmBizSingle getCmBizBycChangeCode(String changeCode);

    void sendCheck(CmBizSingle cbs);

    /**
     * ID查询变更分类是否为网络或者网络下级
     */
    public String selectByTypeId(String id);

    public AjaxResult checkCmBizPass(CmBizSingle cmBizSingle, String userId);

    public AjaxResult checkCmBizNoPass(CmBizSingle cmBizSingle, String userId);

    public List<CmBizSingle> selectCmBizByIdList(CmBizSingle cmBizSingle);
}
