package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.*;

import java.util.List;

/**
 * 采购计划Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-17
 */
public interface PurchaseProjectDemandMapper 
{
    /**
     * 查询采购计划
     * 
     * @param changeId 采购计划ID
     * @return 采购计划
     */
    public PurchaseProjectDemandChange selectPurchaseProjectDemandChangeById(String changeId);

    /**
     * 查询采购计划列表
     * 
     * @param purchaseProjectDemandChange 采购计划
     * @return 采购计划集合
     */
    public List<PurchaseProjectDemandChange> selectPurchaseProjectDemandChangeList(PurchaseProjectDemandChange purchaseProjectDemandChange);

    /**
     * 新增采购计划
     * 
     * @param purchaseProjectDemandChange 采购计划
     * @return 结果
     */
    public int insertPurchaseProjectDemandChange(PurchaseProjectDemandChange purchaseProjectDemandChange);

    /**
     * 修改采购计划
     * 
     * @param purchaseProjectDemandChange 采购计划
     * @return 结果
     */
    public int updatePurchaseProjectDemandChange(PurchaseProjectDemandChange purchaseProjectDemandChange);

    /**
     * 删除采购计划
     * 
     * @param changeId 采购计划ID
     * @return 结果
     */
    public int deletePurchaseProjectDemandChangeById(String changeId);

    /**
     * 批量删除采购计划
     * 
     * @param changeIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePurchaseProjectDemandChangeByIds(String[] changeIds);

    public List<PurchaseProjectDemand> selectPurchaseProjectDemandList(PurchaseProjectDemand purchaseProjectDemand);

    public int insertPurchaseProjectDemand(PurchaseProjectDemand purchaseProjectDemand);

    public int updatePurchaseProjectDemand(PurchaseProjectDemand purchaseProjectDemand);

    public PurchaseProjectDemand selectPurchaseProjectDemandById(String purchaseId);

    public int insertPurchaseCurrentProgress(PurchaseCurrentProgress currentProgress);

    public List<PurchaseCurrentProgress> selectPurchaseCurrentProgressListByPurchaseId(String purchaseId);

    public void insertPurchaseProjectDemandList(List<PurchaseProjectDemandVo> dataList);

    public void insertPurchaseApproveLog(PurchaseApproveLog purchaseApproveLog);

    public List<PurchaseApproveLog> selectPurchaseApproveLogById(PurchaseApproveLog purchaseApproveLog);
}
