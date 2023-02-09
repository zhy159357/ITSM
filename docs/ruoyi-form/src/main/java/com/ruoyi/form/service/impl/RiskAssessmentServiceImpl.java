package com.ruoyi.form.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.RiskAccessRecord;
import com.ruoyi.form.enums.ChangeFieldEnum;
import com.ruoyi.form.enums.ChangeTableNameEnum;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.IRiskAccessRecordService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.RiskAssessmentMapper;
import com.ruoyi.form.domain.RiskAssessment;
import com.ruoyi.form.service.IRiskAssessmentService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-05
 */
@Service
public class RiskAssessmentServiceImpl implements IRiskAssessmentService {
    @Autowired
    private RiskAssessmentMapper riskAssessmentMapper;
    @Autowired
    CustomerFormMapper customerFormMapper;
    @Autowired
    IPubParaValueService pubParaValueService;
    @Autowired
    IRiskAccessRecordService riskAccessRecordService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    Base base;
    @Autowired
    ISysApplicationManagerService sysApplicationManagerService;


    /**
     * 查询【请填写功能名称】
     *
     * @param formId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RiskAssessment selectRiskAssessmentById(String formId) {
        return riskAssessmentMapper.selectRiskAssessmentById(formId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param riskAssessment 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RiskAssessment> selectRiskAssessmentList(RiskAssessment riskAssessment) {
        return riskAssessmentMapper.selectRiskAssessmentList(riskAssessment);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param riskAssessment 【请填写功能名称】
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int insertRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessment.setCurrentLevel(riskAssessment.getRiskLevel().toString());
        int infCount = riskAssessmentMapper.insertRiskAssessment(riskAssessment);
        addRecord(riskAssessment, "新建");
        return infCount;
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param riskAssessment 【请填写功能名称】
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int updateRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessment.setCurrentLevel(riskAssessment.getRiskLevel().toString());
        int infCount = riskAssessmentMapper.updateRiskAssessment(riskAssessment);
        addRecord(riskAssessment, "更新");
        return infCount;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int updateCurrentLevel(String currentLevel,String formId) {
        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setCurrentLevel(currentLevel);
        riskAssessment.setFormId(formId);
        return riskAssessmentMapper.updateRiskAssessment(riskAssessment);
    }

    public void addRecord(RiskAssessment riskAssessment, String reason) {
        updateChangeRiskLevel(riskAssessment);
        //添加操作记录
        RiskAccessRecord riskAccessRecord = new RiskAccessRecord();
        riskAccessRecord.setChangeNo(riskAssessment.getFormId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateTimeFormatter.format(LocalDateTime.now());
        riskAccessRecord.setOperateTime(now);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId());
        riskAccessRecord.setOperator(ogPerson.getpName());
        String initLevel = riskAssessment.getRiskLevel().toString();
        String currentLevel = riskAssessment.getCurrentLevel();
        PubParaValue initLevelValue = pubParaValueService.selectPubParaValue("change_risk_level", initLevel);
        PubParaValue currentLevelValue = pubParaValueService.selectPubParaValue("change_risk_level", currentLevel);
        riskAccessRecord.setInitLevel(initLevelValue.getValueDetail());
        riskAccessRecord.setCurrentLevel(currentLevelValue.getValueDetail());
        riskAccessRecord.setReason(reason);
        riskAccessRecordService.insertRiskAccessRecord(riskAccessRecord);
    }

    public void updateChangeRiskLevel(RiskAssessment riskAssessment) {
        String changeNo = riskAssessment.getFormId();
        String riskLevel = String.valueOf(riskAssessment.getRiskLevel());
        PubParaValue pubParaValue = pubParaValueService.selectPubParaValue("change_risk_level", riskLevel);
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("riskLevel", pubParaValue.getValueDetail());
        param.put("currentRiskLevel", riskLevel);
        String referSys = riskAssessment.getReferSys();
        String[] referSysArray = referSys.split(",");
        StringJoiner stringJoiner = new StringJoiner(";");
        for(String appId:referSysArray){
            OgSys sys = sysApplicationManagerService.selectOgSysBySysCode(appId);
            String sysStr = appId;
            if(sys!=null){
                sysStr = sys.getCaption()+"("+sys.getCode()+")";
            }
            stringJoiner.add(sysStr);
        }
        param.put("referApp", stringJoiner.toString());
        query.put(ChangeFieldEnum.EXTRA1.getName(), changeNo);
        base.update(ChangeTableNameEnum.CHANGE, param, query);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRiskAssessmentByIds(String ids) {
        return riskAssessmentMapper.deleteRiskAssessmentByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param formId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRiskAssessmentById(String formId) {
        return riskAssessmentMapper.deleteRiskAssessmentById(formId);
    }

    @Override
    public String getAssessManIdByChangeNo(String changeNo) {
        return base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.EXTRA1, changeNo);
    }

    //风险评估规则方法
           /*
                                                         值   名称      分值
变更风险评估项——变更类别                 ============================================
                                                         5	项目上线类     4
	                                                     4	系统下线类     3
	                                                     3	维护需求类     2
	                                                     2	故障解决类     2
	                                                     1	服务请求类     1

变更风险评估项——实施所涉及的部室个数	   ===========================================
                                                         1	1个部室        1
	                                                     2	2个部室        2
	                                                     3	3个部室        3
	                                                     4	4个部室以上     4
变更风险评估项——可能影响的系统数量        =============================================
	                                                     1	1个               1
	                                                     2	2个或3个           2
	                                                     3	4个（含）以上~10    3
	                                                     4	10个（含）以上系统   4
变更风险评估项——变更规划耗时             ==============================================
	                                                     1	小于1天          1
	                                                     2	1-2天            2
	                                                     3	3-6天            3
	                                                     4	7天或更长         4
变更风险评估项——有无应急或退回方案        ===============================================
	                                                     1	有              1
	                                                     0	无              4
变更风险评估项——回退耗时                 ================================================
	                                                     1	易于回退（30分钟或更短）       1
	                                                     2	回退难度适中（1小时或更短）    2
	                                                     3	回退难度中等以上（1-2小时）    3
	                                                     4	回退时间超过2小时             4
变更风险评估项——应用所属变更模块         ==================================================
	                                                     5	调整账务功能模块的      4
	                                                     4	调整报表功能模块的      3
	                                                     3	调整批量功能模块的      2
	                                                     2	调整查询功能模块的      1
	                                                     1	调整页面功能模块的      1
变更风险评估项——影响在用业务中断的情况    ====================================================
	                                                     1	无应用重启、无业务中断        1
	                                                     2	重启应用但不中断业务          2
	                                                     3	1个（含）以上业务系统中断     3
	                                                     4	10个（含）以上业务系统中断    4
变更风险评估项——变更实施耗时            =======================================================
	                                                     1	不到30分钟               1
	                                                     2	不到1小时                2
	                                                     3	1-2小时                 3
	                                                     4	超过2小时                4
变更风险评估项——软件所属变更模块        ========================================================
	                                                     2	公共模块               3
	                                                     1	独立模块               1

变更风险级别                          ========================================================
                                                          1	低
	                                                      2	中
	                                                      3	高
	                                                      4	重大
*/
    @Override
    public void access(RiskAssessment riskAssessment) {
        Integer backPlanFlag = Integer.valueOf(riskAssessment.getBackPlanFlag());
        if (backPlanFlag == 0) {
            backPlanFlag = 4;
        }
        Integer backTime = Integer.valueOf(riskAssessment.getBackTime());
        Integer changeImplTime = Integer.valueOf(riskAssessment.getChangeImplTime());
        Integer changePlanTime = Integer.valueOf(riskAssessment.getChangePlanTime());
        Integer changeType = Integer.valueOf(riskAssessment.getChangeType());
        if (changeType == 3) {
            changeType = 2;
        }
        Integer infFuncInterruptType = Integer.valueOf(riskAssessment.getInfFuncInterruptType());
        Integer infSysCount = Integer.valueOf(riskAssessment.getInfSysCount());
        Integer referDeptCount = Integer.valueOf(riskAssessment.getReferDeptCount());
        int total = backPlanFlag + backTime + changeImplTime + changePlanTime + changeType + infFuncInterruptType + infSysCount + referDeptCount;
        double count = 8;
        String softwareChangeTypeStr = riskAssessment.getSoftwareChangeType();
        String appChangeTypeStr = riskAssessment.getAppChangeType();
        if (softwareChangeTypeStr != null && !"".equals(softwareChangeTypeStr.trim())) {
            Integer softwareChangeType = Integer.valueOf(softwareChangeTypeStr.trim());
            count++;
            if (softwareChangeType == 2) {
                softwareChangeType = 3;
            }
            total += softwareChangeType;
        }
        if (appChangeTypeStr != null && !"".equals(appChangeTypeStr.trim())) {
            Integer appChangeType = Integer.valueOf(appChangeTypeStr.trim());
            count++;
            if (appChangeType > 1) {
                appChangeType = appChangeType - 1;
            }
            total += appChangeType;
        }
        double levelValue = total / count;
        levelValue = Math.round(levelValue);
        int riskLevel = 0;
        if (levelValue >= 1 && levelValue < 2) {
            //低
            riskLevel = 1;
        } else if (levelValue >= 2 && levelValue < 2.5) {
            //中
            riskLevel = 2;
        } else if (levelValue >= 2.5 && levelValue < 3) {
            //高
            riskLevel = 3;
        } else if (levelValue >= 3 && levelValue <= 4) {
            //重大
            riskLevel = 4;
        }
        riskAssessment.setRiskLevel(riskLevel);
    }
}
