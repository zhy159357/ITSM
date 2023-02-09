package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.*;
import com.ruoyi.activiti.mapper.PurchaseProjectDemandMapper;
import com.ruoyi.activiti.service.IPurchaseProjectDemandService;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购计划Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-11-17
 */
@Service
public class PurchaseProjectDemandServiceImpl implements IPurchaseProjectDemandService
{
    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private PurchaseProjectDemandMapper purchaseProjectDemandMapper;

    /**
     * 查询采购计划
     * 
     * @param changeId 采购计划ID
     * @return 采购计划
     */
    @Override
    public PurchaseProjectDemandChange selectPurchaseProjectDemandChangeById(String changeId)
    {
        return purchaseProjectDemandMapper.selectPurchaseProjectDemandChangeById(changeId);
    }

    /**
     * 查询采购计划列表
     * 
     * @param purchaseProjectDemandChange 采购计划
     * @return 采购计划
     */
    @Override
    public List<PurchaseProjectDemandChange> selectPurchaseProjectDemandChangeList(PurchaseProjectDemandChange purchaseProjectDemandChange)
    {
        return purchaseProjectDemandMapper.selectPurchaseProjectDemandChangeList(purchaseProjectDemandChange);
    }

    /**
     * 新增采购计划
     * 
     * @param purchaseProjectDemandChange 采购计划
     * @return 结果
     */
    @Override
    public int insertPurchaseProjectDemandChange(PurchaseProjectDemandChange purchaseProjectDemandChange)
    {
        purchaseProjectDemandChange.setCreateTime(DateUtils.getNowDate());
        purchaseProjectDemandChange.setCreateBy(ShiroUtils.getUserId());
        purchaseProjectDemandChange.setChangeId(UUID.getUUIDStr());
        // 新增时remark字段 0-表示暂存（草稿），1-表示提交（待审批）
        if("1".equals(purchaseProjectDemandChange.getRemark())) {
            purchaseProjectDemandChange.setPurchaseStatus("02");
        } else {
            purchaseProjectDemandChange.setPurchaseStatus("01");
        }
        return purchaseProjectDemandMapper.insertPurchaseProjectDemandChange(purchaseProjectDemandChange);
    }

    /**
     * 修改采购计划
     * 
     * @param purchaseProjectDemandChange 采购计划
     * @return 结果
     */
    @Override
    public int updatePurchaseProjectDemandChange(PurchaseProjectDemandChange purchaseProjectDemandChange)
    {
        purchaseProjectDemandChange.setUpdateTime(DateUtils.getNowDate());
        purchaseProjectDemandChange.setUpdateBy(ShiroUtils.getUserId());
        if("1".equals(purchaseProjectDemandChange.getRemark()) && "submit".equals(purchaseProjectDemandChange.getParams().get("submitFlag"))) {
            // 提交时将审批状态变更为 "待审批"
            purchaseProjectDemandChange.setPurchaseStatus("02");
        }
        return purchaseProjectDemandMapper.updatePurchaseProjectDemandChange(purchaseProjectDemandChange);
    }

    /**
     * 删除采购计划对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePurchaseProjectDemandChangeByIds(String ids)
    {
        return purchaseProjectDemandMapper.deletePurchaseProjectDemandChangeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除采购计划信息
     * 
     * @param purchaseId 采购计划ID
     * @return 结果
     */
    @Override
    public int deletePurchaseProjectDemandChangeById(String purchaseId)
    {
        return purchaseProjectDemandMapper.deletePurchaseProjectDemandChangeById(purchaseId);
    }

    @Override
    public List<PurchaseProjectDemand> selectPurchaseProjectDemandList(PurchaseProjectDemand purchaseProjectDemand) {
        return purchaseProjectDemandMapper.selectPurchaseProjectDemandList(purchaseProjectDemand);
    }

    @Override
    public int insertPurchaseProjectDemand(PurchaseProjectDemand purchaseProjectDemand) {
        purchaseProjectDemand.setCreateTime(DateUtils.getNowDate());
        return purchaseProjectDemandMapper.insertPurchaseProjectDemand(purchaseProjectDemand);
    }

    @Override
    public int updatePurchaseProjectDemand(PurchaseProjectDemand purchaseProjectDemand) {
        return purchaseProjectDemandMapper.updatePurchaseProjectDemand(purchaseProjectDemand);
    }

    @Override
    public PurchaseProjectDemand selectPurchaseProjectDemandById(String purchaseId) {
        return purchaseProjectDemandMapper.selectPurchaseProjectDemandById(purchaseId);
    }

    @Override
    public int purchasePractice(PurchaseProjectDemand purchaseProjectDemand) {
        boolean passFlag = "1".equals(purchaseProjectDemand.getParams().get("passFlag"));
        boolean managerFlag = "manager".equals(purchaseProjectDemand.getRemark());
        boolean practiceFlag = "practice".equals(purchaseProjectDemand.getRemark());
        PurchaseApproveLog approveLog = null;
        if(managerFlag) {
            String comment = (String)purchaseProjectDemand.getParams().get("comment");
            approveLog = new PurchaseApproveLog();
            approveLog.setPurchaseId(purchaseProjectDemand.getPurchaseId());
            approveLog.setMemo(comment);

            PurchaseProjectDemand projectDemand = new PurchaseProjectDemand();
            projectDemand.setPurchaseId(purchaseProjectDemand.getPurchaseId());
            if(passFlag) {
                // 管理员提交并且是通过修改状态码值为"03"-标识已审批
                projectDemand.setPurchaseStatus("03");
            } else {
                // 管理员退回修改状态码值为"01"-标识退回到起始状态"01"草稿，实施操作员可再次查看到该条数据
                projectDemand.setPurchaseStatus("01");
            }
            purchaseProjectDemand = projectDemand;
        } else {
            // 采购实施操作员提交修改状态码值为"02"-标识待管理员确认
            purchaseProjectDemand.setPurchaseStatus("02");
        }
        int rows = purchaseProjectDemandMapper.updatePurchaseProjectDemand(purchaseProjectDemand);
        if(rows > 0 && practiceFlag) {
            // 采购实施员提交时需要保存当前进度数据
            PurchaseCurrentProgress currentProgress = new PurchaseCurrentProgress();
            currentProgress.setCurrentProgress(purchaseProjectDemand.getCurrentProgress());
            currentProgress.setRiskWarning(purchaseProjectDemand.getRiskWarning());
            currentProgress.setMemo(purchaseProjectDemand.getMemo());
            currentProgress.setPurchaseId(purchaseProjectDemand.getPurchaseId());
            rows = insertPurchaseCurrentProgress(currentProgress);
        }
        // 采购实施管理员通过或者退回记录当前审批意见
        if(managerFlag) {
            if(approveLog != null)
                insertPurchaseApproveLog(approveLog);
        }
        return rows;
    }

    public int insertPurchaseCurrentProgress(PurchaseCurrentProgress currentProgress) {
        currentProgress.setCurrentProgressId(UUID.getUUIDStr());
        currentProgress.setCreateTime(DateUtils.getNowDate());
        currentProgress.setCreateBy(ShiroUtils.getUserId());
        return purchaseProjectDemandMapper.insertPurchaseCurrentProgress(currentProgress);
    }

    public List<PurchaseCurrentProgress> selectPurchaseCurrentProgressListByPurchaseId(String purchaseId) {
        return purchaseProjectDemandMapper.selectPurchaseCurrentProgressListByPurchaseId(purchaseId);
    }

    public String importPurchaseProjectDemand(List<PurchaseProjectDemandVo> purchaseList) {
        Map<String, Object> map = checkPurchaseProjectDemandData(purchaseList);
        String message = map.get("message").toString();
        if((Boolean) map.get("flag")) {
            List<PurchaseProjectDemandVo> dataList = (List)map.get("dataList");
            insertPurchaseProjectDemandList(dataList);
        }
        return message;
    }

    /**
     * 获取单号
     */
    public String getPurchaseNumStr(String bizType) {
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        return bizType + nowDateStr + numStr;
    }

    /**
     * 具体校验判断逻辑
     */
    private Map<String,Object> checkPurchaseProjectDemandData(List<PurchaseProjectDemandVo> purchaseList) {
        Map<String, Object> map = new HashMap<>();
        if(CollectionUtils.isEmpty(purchaseList)) {
            map.put("flag", false);
            map.put("message", "上传excel数据信息不可为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        int rowNum = 1;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        List<PurchaseProjectDemandVo> dataList = new ArrayList();
        for (PurchaseProjectDemandVo demand : purchaseList) {
            StringBuilder errorMsg = validateDemandField(demand, rowNum);
            if(errorMsg.length() > 1) {
                failureNum ++;
                failureMsg.append(errorMsg);
            } else {
                successNum ++;
                demand.setChangeId(UUID.getUUIDStr());
                demand.setCreateBy(ShiroUtils.getUserId());
                demand.setCreateTime(DateUtils.getNowDate());
                demand.setPurchaseNo(this.getPurchaseNumStr("CG"));
                demand.setPurchaseStatus("01");
                dataList.add(demand);
            }
            rowNum ++;
        }
        if(failureNum >= 1) {
            failureMsg.insert(0, "数据导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            map.put("flag", false);
            map.put("message", failureMsg.toString());
        } else {
            successMsg.insert(0, "数据导入成功！共 " + successNum + " 条");
            map.put("flag", true);
            map.put("message", successMsg.toString());
            map.put("dataList", dataList);
        }
        return map;
    }

    /**
     *  根据注解做空值校验
     */
    private StringBuilder validateDemandField (PurchaseProjectDemandVo demandVo, int rowNum) {
        StringBuilder failureMsg = new StringBuilder();
        Field[] fields = demandVo.getClass().getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(demandVo);
            } catch (Exception e) {

            }
            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            if(notBlank != null) {
                if(StringUtils.isEmpty(value)) {
                    failureMsg.append("<br/>第" + rowNum + "行，" + notBlank.message());
                }
            }
        }
        return failureMsg;
    }

    private void insertPurchaseProjectDemandList(List<PurchaseProjectDemandVo> dataList) {
        purchaseProjectDemandMapper.insertPurchaseProjectDemandList(dataList);
    }

    @Override
    public void insertPurchaseApproveLog(PurchaseApproveLog purchaseApproveLog) {
        purchaseApproveLog.setApproveLogId(UUID.getUUIDStr());
        purchaseApproveLog.setCreateTime(DateUtils.getNowDate());
        purchaseApproveLog.setCreateBy(ShiroUtils.getUserId());
        purchaseProjectDemandMapper.insertPurchaseApproveLog(purchaseApproveLog);
    }

    @Override
    public List<PurchaseApproveLog> selectPurchaseApproveLogById(PurchaseApproveLog approveLog) {
        return purchaseProjectDemandMapper.selectPurchaseApproveLogById(approveLog);
    }
}
