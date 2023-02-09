package com.ruoyi.activiti.service.impl;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Token;
import com.ruoyi.activiti.domain.BgJJEfficiency;
import com.ruoyi.activiti.domain.BgJSEfficiency;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.mapper.CmBizSingleMapper;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgTypeinfoService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-13
 */
@Service("CmBizSing")
public class CmBizSingleServiceImpl implements ICmBizSingleService {
    @Autowired
    private CmBizSingleMapper cmBizSingleMapper;
    @Autowired
    ISysDeptService iSysDeptService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private ItsmToDevopsService itsmToDevopsService;
    @Autowired
    IOgPersonService iOgPersonService;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private IOgTypeinfoService ogTypeinfoService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Value("${pagehelper.helperDialect}")
    private String dbType;
    /**
     * 查询【请填写功能名称】
     *
     * @param changeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CmBizSingle selectCmBizSingleById(String changeId) {
        return cmBizSingleMapper.selectCmBizSingleById(changeId);
    }

    public String convertParaValueDetail(String paraName, String value) {
        String valueDetail = iPubParaValueService.selectPubParaValueByParaNameApp(paraName, value);
        return valueDetail;
    }

    @Override
    public CmBizSingle selectCmBizSingleByIdApp(String changeId) {
        CmBizSingle cbs = cmBizSingleMapper.selectCmBizSingleByIdApp(changeId);
        if(cbs == null)
            return null;
        String changeType = convertParaValueDetail("change_type", cbs.getChangeType());
        cbs.setChangeType(changeType);//获取变更类型名返回
        String lev = convertParaValueDetail("important_lev", cbs.getImportantLev());
        cbs.setImportantLev(lev);//获取变更风险等级
        String isNotice = convertParaValueDetail("isNotice", cbs.getIfAuto());
        cbs.setIfAuto(isNotice);//是否自动化变更
        String ifMastart = convertParaValueDetail("isNotice", cbs.getIfMastart());
        cbs.setIfMastart(ifMastart);//是否手工启动
        String isJjbg = convertParaValueDetail("isNotice", cbs.getIsJjbg());
        cbs.setIsJjbg(isJjbg);//是否紧急变更
        String changeStage = convertParaValueDetail("change_stage", cbs.getChangeStage());
        cbs.setChangeStage(changeStage);//变更所处阶段
        String changeResource = convertParaValueDetail("change_resource", cbs.getChangeResource());
        cbs.setChangeResource(changeResource);//变更来源
        String ifOasis = convertParaValueDetail("isNotice", cbs.getIfOasis());
        cbs.setIfOasis(ifOasis);//是否通过技术委员会
        String changeReason = convertParaValueDetail("changReason_type", cbs.getChangeReason());
        cbs.setChangeReason(changeReason);//变更原因
        return cbs;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param cmBizSingle 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CmBizSingle> selectCmBizSingleList(CmBizSingle cmBizSingle) {
        if("mysql".equals(dbType)){
            return cmBizSingleMapper.selectCmBizSingleListMysql(cmBizSingle);
        }else{
            return cmBizSingleMapper.selectCmBizSingleList(cmBizSingle);
        }
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param cmBizSingle 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCmBizSingle(CmBizSingle cmBizSingle) {
        cmBizSingle.setCreatetime(DateUtils.dateTimeNow());//创建时间
        cmBizSingle.setInvalidationMark("1");//有效标志
        return cmBizSingleMapper.insertCmBizSingle(cmBizSingle);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param cmBizSingle 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCmBizSingle(CmBizSingle cmBizSingle) {
        return cmBizSingleMapper.updateCmBizSingle(cmBizSingle);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCmBizSingleByIds(String ids) {
        return cmBizSingleMapper.deleteCmBizSingleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param changeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCmBizSingleById(String changeId) {
        return cmBizSingleMapper.deleteCmBizSingleById(changeId);
    }

    @Override
    public List<CmBizSingle> selectMyCmBizSingleList(CmBizSingle cmBizSingle) {
        List<CmBizSingle> list = new ArrayList<CmBizSingle>();
        if("mysql".equals(dbType)){
            list =  cmBizSingleMapper.selectMyCmBizSingleListMysql(cmBizSingle);
        }else{
            list =  cmBizSingleMapper.selectMyCmBizSingleList(cmBizSingle);
        }
        return list;
    }

    /**
     * 查询分管领导
     */
    public List<OgPerson> getFucheck() {
        OgOrg org = iSysDeptService.selectDeptById("40288a14276f110301276f7dc7a60015");
        OgPerson person = new OgPerson();
        String levelCode = org.getLevelCode();
        OgOrg org1 = new OgOrg();
        org1.setLevelCode(levelCode);
        person.setOrg(org1);
        person.setrId("2109");
        List<OgPerson> list = commonService.selectPersonByOrgAndRole(person);
        return list;
    }

    @Override
    public CmBizSingle selectCmBizSingleByIdById(String changeId) {
        if("mysql".equals(dbType)){
            return cmBizSingleMapper.selectCmBizSingleByIdByIdMysql(changeId);
        }else{
            return cmBizSingleMapper.selectCmBizSingleByIdById(changeId);
        }
    }

    @Override
    public List<BgJSEfficiency> selectCmBizJs(BgJSEfficiency bgJSEfficiency) {
        return cmBizSingleMapper.selectCmBizJs(bgJSEfficiency);
    }

    @Override
    public List<BgJJEfficiency> selectCmBizJJ(BgJJEfficiency bgJJEfficiency) {
        return cmBizSingleMapper.selectCmBizJJ(bgJJEfficiency);
    }

    @Override
    public CmBizSingle getFlowCmBiz(CmBizSingle cmBizSingle) {
        if("mysql".equals(dbType)){
            return cmBizSingleMapper.getFlowCmBizMysql(cmBizSingle);
        }else{
            return cmBizSingleMapper.getFlowCmBiz(cmBizSingle);
        }
    }

    @Override
    public CmBizSingle getCmBizBycChangeCode(String changeCode) {
        return cmBizSingleMapper.getCmBizBycChangeCode(changeCode);
    }

    @Override
    public void sendCheck(CmBizSingle cbs) {
        if ("1".equals(cbs.getIfAuto()) && "1".equals(selectByTypeId(cbs.getChangeCategoryId()))) {
            String applicantId = iOgPersonService.selectOgPersonById(cbs.getApplicantId()).getMobilPhone();
            String planImplPer = iOgPersonService.selectOgPersonById(cbs.getImplementorId()).getMobilPhone();
            String planReiwPer = iOgPersonService.selectOgPersonById(cbs.getChangeManagerId()).getMobilPhone();
            Map map = itsmToDevopsService.startChangeTask(cbs.getChangeCode(), cbs.getChangeSingleName(), applicantId, planImplPer, planReiwPer, cbs.getIfMastart(), cbs.getExpectStartTime(), cbs.getExpectEndTime());
            if (map.containsKey("state") && "0".equals(map.get("state"))) {
                //发送短信告知变更申请人发送自动化调用预启任务成功
                String smsText = "变更单号：" + cbs.getChangeCode() + ",标题：" + cbs.getChangeSingleName() + "的变更单已经审批完成，自动化预启任务创建成功，详情请登录网络自动化平台查看。";
                OgPerson person = iOgPersonService.selectOgPersonById(cbs.getApplicantId());//获取短信接收人 发起人
                sendSms(smsText, person);
            } else {
                //发送短信告知变更申请人发送自动化调用预启任务失败
                String smsText = "变更单号：" + cbs.getChangeCode() + ",标题：" + cbs.getChangeSingleName() + "的变更单已经审批完成，自动化预启任务创建失败，原因：" + map.get("mes");
                OgPerson person = iOgPersonService.selectOgPersonById(cbs.getApplicantId());//获取短信接收人 发起人
                sendSms(smsText, person);
            }

        } else {
            String smsText3 = "变更单号：" + cbs.getChangeCode() + ",标题：" + cbs.getChangeSingleName() + "的变更单已经审批完成，可以实施，请登录运维管理平台查看。";
            OgPerson person3 = iOgPersonService.selectOgPersonById(cbs.getApplicantId());//获取短信接收人 发起人
            sendSms(smsText3, person3);

            String smsText2 = "变更单号：" + cbs.getChangeCode() + ",标题：" + cbs.getChangeSingleName() + "的变更单已经审批完成，可以实施，请登录运维管理平台查看。";
            OgPerson person2 = iOgPersonService.selectOgPersonById(cbs.getChangeManagerId());//获取短信接收人 评估人
            sendSms(smsText2, person2);
        }
    }

    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("111103");// 检查对象
        p.setCreaterId(person.getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * ID查询变更分类是否为网络或者网络下级
     */
    public String selectByTypeId(String id) {
        OgTypeinfo ogTypeinfo = ogTypeinfoService.selectOgTypeinfoById(id);
        if (ogTypeinfo.getTypeLevel() != null) {
            int level = Integer.parseInt(String.valueOf(ogTypeinfo.getTypeLevel()));
            for (int i = 2; i < level; i++) {
                ogTypeinfo = ogTypeinfoService.selectOgTypeinfoById(ogTypeinfo.getParentId());
            }
        }
        String flag = "0";//不是网络
        if ("网络".equals(ogTypeinfo.getTypeName())) {
            flag = "1";//是网络
        }
        return flag;
    }

    @Override
    public AjaxResult checkCmBizPass(CmBizSingle cmBizSingle, String userId) {
        CmBizSingle cbs = selectCmBizSingleById(cmBizSingle.getChangeId());
        if (!"0401".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        cbs.setChangeSingleStauts("0500");
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("implementorId", cbs.getImplementorId());
        reMap.put("reCode", "0");
        reMap.put("userId", userId);
        if (cmBizSingle.getParams().containsKey("comment")) {
            reMap.put("comment", cmBizSingle.getParams().get("comment").toString());
        }
        reMap.put("processDefinitionKey", "CmZy");
        try {
            updateCmBizSingle(cbs);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("资源变更单操作失败，请刷新列表后重新操作。");
        }
        sendCheck(cbs);
        return AjaxResult.success("变更单：" + cbs.getChangeCode() + "审批通过操作成功。");
    }

    @Override
    public AjaxResult checkCmBizNoPass(CmBizSingle cmBizSingle, String userId) {
        CmBizSingle cbs = selectCmBizSingleById(cmBizSingle.getChangeId());
        if (cbs == null || (!"0300".equals(cbs.getChangeSingleStauts()) &&
                !"0400".equals(cbs.getChangeSingleStauts()) && !"0401".equals(cbs.getChangeSingleStauts()))) {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        cbs.setChangeSingleStauts("0200");//赋值退回待修改状态
        Map<String, Object> reMap = new HashMap<>();
        if (cmBizSingle.getParams().containsKey("comment")) {
            reMap.put("comment", cmBizSingle.getParams().get("comment").toString());
        }
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("processDefinitionKey", "CmZy");
        reMap.put("reCode", "2");
        reMap.put("userId", userId);
        try {
            updateCmBizSingle(cbs);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("资源变更单退回操作失败，请刷新列表后重新尝试。");
        }
        return AjaxResult.success("变更单：" + cbs.getChangeCode() + "回退操作成功。");
    }

    @Override
    public List<CmBizSingle> selectCmBizByIdList(CmBizSingle cmBizSingle) {
        return cmBizSingleMapper.selectCmBizByIdList(cmBizSingle);
    }
}
