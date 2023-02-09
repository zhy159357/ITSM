package com.ruoyi.activiti.listener;
import com.ruoyi.activiti.constants.ImBizIssueConstants;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.impl.ImBizIssuefxServiceImpl;
import com.ruoyi.activiti.service.impl.PubBizPresmsServiceImpl;
import com.ruoyi.activiti.service.impl.PubBizSmsServiceImpl;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtilsAct;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.impl.OgPersonServiceImpl;
import com.ruoyi.system.service.impl.SysDeptServiceImpl;
import com.ruoyi.system.service.impl.SysRoleServiceImpl;
import org.activiti.engine.delegate.*;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *应用问题单流程监听类
 *
 *
 * @author zx
 */
@Service("ReportBackEndProcessor")
@Transactional
public class ReportBackEndProcessor implements JavaDelegate,ExecutionListener{

    private static final long serialVersionUID = 1L;

    private ImBizIssuefxServiceImpl issueService=SpringUtils.getBean(ImBizIssuefxServiceImpl.class);
    private PubBizPresmsServiceImpl pubBizPresmsService=SpringUtils.getBean(PubBizPresmsServiceImpl.class);
    private PubBizSmsServiceImpl PubBizSmsService=SpringUtils.getBean(PubBizSmsServiceImpl.class);
    private OgPersonServiceImpl ogPersonService=SpringUtils.getBean(OgPersonServiceImpl.class);
    private SysRoleServiceImpl sysRoleService=SpringUtils.getBean(SysRoleServiceImpl.class);
    private SysDeptServiceImpl deptService=SpringUtils.getBean(SysDeptServiceImpl.class);
    @Override
    public void execute(DelegateExecution execution) {
    }

    @Override
    public void notify(DelegateExecution execution) {
    }

    /**
     * 问题单 任务状态
     * @param execution
     */
    public void test(DelegateExecution execution){
        DelegateExecution parent=execution.getParent();
        String issueId= (String) parent.getProcessInstanceBusinessKey();
        String desption=execution.getCurrentFlowElement().getDocumentation();
        ImBizIssuefx imBizIssuefx= issueService.selectImBizIssuefxById(issueId);
        Map<String, VariableInstance> variableMap=parent.getVariableInstances();
        String issueNo=variableMap.get("issueNo")==null?"":variableMap.get("issueNo").getTextValue();
        String issueTitle=variableMap.get("issueTitle")==null?"":variableMap.get("issueTitle").getTextValue();
        String smsText="问题单号："+issueNo+",标题为："+issueTitle;
        //评估人 数组
        String multiusers=variableMap.get("multiusers")==null?"":variableMap.get("multiusers").getTextValue();
        //审核人
        String reviewerId=variableMap.get("reviewerId")==null?"":variableMap.get("reviewerId").getTextValue();
        //分发角色
        String roleId=variableMap.get("groupId")==null?"":variableMap.get("groupId").getTextValue();
        //复核 数组
        String businessId=variableMap.get("businessId")==null?"":variableMap.get("businessId").getTextValue();
        //处理人
        String auditId=variableMap.get("auditId")==null?"":variableMap.get("auditId").getTextValue();
        //业务受理人
        String busId=variableMap.get("busId")==null?"":variableMap.get("busId").getTextValue();
        //待关闭
        String creatId=variableMap.get("creatId")==null?"":variableMap.get("creatId").getTextValue();
        String isInside=imBizIssuefx.getIsInside();
        String currentState="";
        String subDept="";
        switch (desption){
            case "pinggu":
                currentState="1";
                smsText+="的问题单已提交,请登录运维管理平台处理。";
                sendUser(smsText,multiusers);
                String[] multiusersIds=multiusers.split(",");
                for(String Id:multiusersIds){
                    subDept=deptSelect(Id,isInside);
                    if(!"0".equals(subDept)){
                        break;
                    }
                }

            break;
            case "shenhe":
                currentState="2";
                //待审核
                smsText+="的问题单已提交请审核。";
                sendMsgNoUp(reviewerId,smsText);
                subDept=deptSelect(reviewerId,isInside);
            break;
            case "fenfa":
                currentState="3";
                //待分发
                smsText+="的问题单请处理。";
                sendRole(smsText,roleId);
                subDept=ImBizIssueConstants.ORG_ZHXXKJB_ID;
                break;
            case "fuhe":
                currentState="401";
                //待复核
                smsText+="的问题单已分发,请登录运维管理平台查看复核。";
                sendUser(smsText,businessId);
                String[] busIds=businessId.split(",");
                for(String id:busIds){
                    subDept=deptSelect(id,isInside);
                    if(!"0".equals(subDept)){
                        break;
                    }
                }
            break;
            case "chuli":
                currentState="5";
                //修改后重新提交
                if(imBizIssuefx!=null&& StringUtils.isNotEmpty(imBizIssuefx.getCurrentState())&&"10".equals(imBizIssuefx.getCurrentState())){
                    smsText+="的问题单已重新提交请处理。";
                }else{
                    //待处理
                    smsText+="的问题单已分发,请登录运维管理平台处理。";
                }
                sendMsgNoUp(auditId,smsText);
                subDept=deptSelect(auditId,isInside);
                break;
            case "yujiejue":
                currentState="6";
            break;
            case "ywshouli":
                currentState="7";
                smsText+="的问题单已分发,请登录运维管理平台处理。";
                sendMsgNoUp(busId, smsText);
                subDept=deptSelect(busId,isInside);
                break;
            case "jiejue":
                currentState="8";
            //待解决
            break;
            case "close":
                currentState="9";
            //待关闭
                smsText+="的问题单已处理完成。";
                sendMsgNoUp(creatId, smsText);
                subDept=deptSelect(creatId,isInside);
            break;
            case "edit":
                currentState="10";
            //退回待修改
                smsText+="的问题单已被退回,请处理。";
                sendMsgNoUp(creatId, smsText);
                subDept=deptSelect(creatId,isInside);
            break;
           case "over":
               //recode 等于1 作废 其他是关闭
                String reCode=variableMap.get("reCode")==null?"":variableMap.get("reCode").getTextValue();
                currentState="1".equals(reCode)?"15":"11";
                subDept=deptSelect(creatId,isInside);

            break;

        }
        //业务复核
        if("401".equals(currentState)){
            imBizIssuefx.setHanguptask(currentState);
        }else{
            imBizIssuefx.setCurrentState(currentState);
        }
        if("11".equals(currentState)){
            imBizIssuefx.setHanguptask("0");
        }
        imBizIssuefx.setSubDept(subDept);
        issueService.changeStatus(imBizIssuefx);

    }

    /**
     * 原隐患排查合并后 支线流程--任务状态及短信
     * @param execution
     */
    public void sheet(DelegateExecution execution){
        DelegateExecution parent=execution.getParent();
        String issueId= (String) parent.getProcessInstanceBusinessKey();
        String desption=execution.getCurrentFlowElement().getDocumentation();
        ImBizIssuefx imBizIssuefx= issueService.selectImBizIssuefxById(issueId);
        Map<String, VariableInstance> variableMap=parent.getVariableInstances();
        String issueNo=variableMap.get("issueNo")==null?"":variableMap.get("issueNo").getTextValue();
        String issueTitle=variableMap.get("issueTitle")==null?"":variableMap.get("issueTitle").getTextValue();
        String insideUserId=variableMap.get("insideUserId")==null?"":variableMap.get("insideUserId").getTextValue();
        String creatId=variableMap.get("creatId")==null?"":variableMap.get("creatId").getTextValue();
        String isInside=imBizIssuefx.getIsInside();
        String smsText="问题单号："+issueNo+",标题为："+issueTitle;
        String currentState="";
        String subDept="";
        //数据中心处理
        if("inside_deal".equals(desption)){
            currentState="12";
            smsText+="的问题单已审核，请登录运维管理平台处理。";
            if(StringUtils.isNotEmpty(insideUserId)){
                sendMsgNoUp(insideUserId,smsText);
                subDept=deptSelect(insideUserId,isInside);
            }
        }
        //预解决
        if("inside_yujiejue".equals(desption)){
            currentState="13";
            smsText+="的问题单已处理，请登录运维管理平台处理。";
            if(StringUtils.isNotEmpty(insideUserId)){
                sendMsgNoUp(insideUserId,smsText);
                subDept=deptSelect(insideUserId,isInside);
            }
        }
        //解决
        if("inside_jiejue".equals(desption)){
            currentState="14";
            smsText+="的问题单已处理，请登录运维管理平台处理。";
            if(StringUtils.isNotEmpty(insideUserId)){
                sendMsgNoUp(insideUserId,smsText);
                subDept=deptSelect(insideUserId,isInside);
            }
        }
        //关闭
        if("inside_close".equals(desption)){
            currentState="9";
            //待关闭
            smsText+="的问题单已处理完成。";
            if(StringUtils.isNotEmpty(insideUserId)){
                sendMsgNoUp(creatId,smsText);
                subDept=deptSelect(insideUserId,isInside);
            }
        }
        //结束
        if("over".equals(desption)){
            String reCode=variableMap.get("reCode")==null?"":variableMap.get("reCode").getTextValue();
            currentState="1".equals(reCode)?"15":"11";
            subDept=deptSelect(insideUserId,isInside);
            //流程结束
            /*smsText+="的问题单关闭！";
            sendMsgNoUp(creatId, smsText);*/
        }
        imBizIssuefx.setCurrentState(currentState);
        imBizIssuefx.setSubDept(subDept);
        issueService.changeStatus(imBizIssuefx);

    }

    /**
     * 问题单 并行任务完成状态修改预解决完成，业务评估未完成
     * @param execution
     */
    public void bing(DelegateExecution execution) {
        DelegateExecution parent = execution.getParent();
        String businessId = (String) parent.getProcessInstanceBusinessKey();
        ImBizIssuefxServiceImpl issueService = BeanUtilsAct.getObject(ImBizIssuefxServiceImpl.class);
        ImBizIssuefx imBizIssuefx = issueService.selectImBizIssuefxById(businessId);
        //402 未定义码值
        if("401".equals(imBizIssuefx.getHanguptask())){
            imBizIssuefx.setCurrentState("401");
            issueService.changeStatus(imBizIssuefx);
        }

    }
    /*
     问题单 业务复核结束
     */
    public void fuheOver(DelegateExecution execution){
        DelegateExecution parent=execution.getParent();
        String businessId= (String) parent.getProcessInstanceBusinessKey();
        //注入为null 重新获取
        ImBizIssuefxServiceImpl issueService= BeanUtilsAct.getObject(ImBizIssuefxServiceImpl.class);
        ImBizIssuefx imBizIssuefx= issueService.selectImBizIssuefxById(businessId);
        imBizIssuefx.setHanguptask("0");
        issueService.changeStatus(imBizIssuefx);
    }

    /**
     *角色
     * @param smsText
     * @param roleId
     */
    public void sendRole(String smsText,String roleId){
        List<String> userIds=sysRoleService.selectUserIdByRoleId(roleId);
        if(!CollectionUtils.isEmpty(userIds)){
            for (String userId : userIds) {
                sendMsgNoUp(userId, smsText);
            }
        }

    }
    /**
     *多人
     * @param smsText
     * @param multiusers
     */
    public void sendUser(String smsText,String multiusers){
        String[] mul=multiusers.split(",");
        if(mul.length>0){
            for(String userId :mul){
                sendMsgNoUp(userId,smsText);
            }
        }
    }
    /**
     * 发送短信方法(不包含上行)
     *
     * @param
     * @param msg
     */
    public void sendMsgNoUp(String userId, String msg) {
        OgPerson person = ogPersonService.selectOgPersonById(userId);
        if(person!=null){
            PubBizPresms pubBizPresms = new PubBizPresms();
            pubBizPresms.setPubBizPresmsId(UUID.getUUIDStr());
            pubBizPresms.setTelephone(person.getMobilPhone());
            pubBizPresms.setName(person.getpName());
            pubBizPresms.setSmsText(msg);
            pubBizPresms.setCreaterId(person.getpId());
            pubBizPresms.setInvalidationMark("1");
            pubBizPresms.setSmsState("0");
            pubBizPresms.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
            pubBizPresms.setInspectObject("050100");// 检查对象
            pubBizPresms.setInspectTime(DateUtils.dateTimeNow());// 检查时间
            //pubBizPresms.setDealStatus("0");
            pubBizPresmsService.insertPubBizPresms(pubBizPresms);
            PubBizSmsService.findPreSmsAndSend(pubBizPresms);
        }

    }
    /**
     * 查询用户所属机构  问题单统计使用 只查询
     * 信息科技部1，数据中心2，金融科技创新部3，信用卡中心4，管理信息部5，软件研发中心6
     * @param userId
     * @return
     */
    public String deptSelect(String userId,String isInside){
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //获取机构
        OgOrg sysDept = deptService.selectDeptById(orgId);
        //信用卡中心4 4级机构
        if(ImBizIssueConstants.CARD_CENTER_ID.equals(orgId)||
                ImBizIssueConstants.CARD_CENTER_ID.equals(sysDept.getParentId())){
            return ImBizIssueConstants.CARD_CENTER_ID;
        }
        //信息科技部 1 3级机构
        if(ImBizIssueConstants.ORG_ZHXXKJB_ID.equals(orgId)||
                ImBizIssueConstants.ORG_ZHXXKJB_ID.equals(sysDept.getParentId())){
            return ImBizIssueConstants.ORG_ZHXXKJB_ID;
        }
        //数据中心 2 3级机构
        if(ImBizIssueConstants.ORG_DATACENTER_FENGTAI.equals(orgId)||
                ImBizIssueConstants.ORG_DATACENTER_FENGTAI.equals(sysDept.getParentId())||
                ImBizIssueConstants.ORG_DATACENTER_HEFEI.equals(orgId)||
                ImBizIssueConstants.ORG_DATACENTER_HEFEI.equals(sysDept.getParentId())||
                ImBizIssueConstants.ORG_DATACENTER_YIZHUNAG.equals(orgId)||
                ImBizIssueConstants.ORG_DATACENTER_YIZHUNAG.equals(sysDept.getParentId())){
            if("0".equals(isInside)){
                return ImBizIssueConstants.ORG_DATACENTER;
            }
            return orgId;
        }
        //金融科技创新部3  3级机构
        if(ImBizIssueConstants.FINANCE_SAT_ID.equals(orgId)||
                ImBizIssueConstants.FINANCE_SAT_ID.equals(sysDept.getParentId())){
            return ImBizIssueConstants.FINANCE_SAT_ID;
        }
        //管理信息部5  3级机构
        if(ImBizIssueConstants.MANAGE_INFO_ID.equals(orgId)||
                ImBizIssueConstants.MANAGE_INFO_ID.equals(sysDept.getParentId())){
            return ImBizIssueConstants.MANAGE_INFO_ID;
        }
        //软件研发中心6  3级机构
        if(ImBizIssueConstants.SOFTWARE_CENTER_ID.equals(orgId)||
                ImBizIssueConstants.SOFTWARE_CENTER_ID.equals(sysDept.getParentId())){
            return ImBizIssueConstants.SOFTWARE_CENTER_ID;
        }
        return "0";
    }
}
