package com.ruoyi.activiti.controller.itsm.dispatch;

import com.ruoyi.activiti.constants.FmDdConstants;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.FmDispatchService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反馈调度请求
 */

@Controller
@RequestMapping("/dispatch/feedback")
@Transactional(rollbackFor = Exception.class)
public class FeedBackDispatchController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FeedBackDispatchController.class);


    @Autowired
    private TaskService taskService;

    @Autowired
    private FmDispatchService dispatchService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private ActivitiCommService activitiCommService;



    @Autowired
    private ISysDeptService deptService;

    private String prefix = "dispatch/feedback";



    /**
     * 转到 反馈调度请求 页面
     * @return
     */
    @GetMapping()
    public String mydispatch()
    {
        return prefix + "/feedback";
    }

    /**
     * 列表展示
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmDd fmDd)
    {
        startPage();

        List<Map<String,Object>> reList =activitiCommService.userTask("FmDd",null);
        List<Map<String,Object>> regList=new ArrayList<>();
            regList=activitiCommService.groupTasks("FmDd","xietongchuli");
            reList.addAll(regList);

        List<String> fmDdIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("xietongchuli".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    fmDdIds.add(business_key);
                }
            }
        }
        List<FmDd> resultlist = null;
        if(fmDdIds!=null && fmDdIds.size()>0){
            fmDd.setIds(fmDdIds);
            resultlist =  dispatchService.selectFmDdListByTask(fmDd);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());



    }

    /**
     *处理按钮对应的页面
     */
    @GetMapping("/feedback/feedbackAuthor/{id}")
    public String auditAuthor(@PathVariable("id") String id, ModelMap map)
    {
        //获取页面的信息
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd !=null){
            map.put("fmdd",fmDd);

            //获取审核人信息
            OgPerson person = ogPersonService.selectOgPersonById(fmDd.getCheckerId());
            if(person != null){
                map.put("pname",person.getpName());

            }

            //创建时间进行日期回显
            String createTime = fmDd.getCreateTime();
            if(StringUtils.isNotEmpty(createTime)){
                fmDd.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            }

            //计划操作时间进行日期回显
            String planTime = fmDd.getPlanTime();
            if(StringUtils.isNotEmpty(planTime)){
                fmDd.setPlanTime(DateUtils.formatDateStr(planTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            }
        }

        return prefix + "/feedbackAuthor";
    }



    /**
     *协同处理
     * @param ccAuthorText
     * @param userId
     * @param businessKey
     * @return
     */
    @PostMapping("/dealzhuanfa")
    @ResponseBody
    public AjaxResult zhuanfa(String ccAuthorText,String userId,String businessKey,FmDd fmDd){
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("dealId", userId);
        reMap.put("comment", ccAuthorText);
        reMap.put("businessKey", businessKey);
        reMap.put("reCode", "2");
        reMap.put("userId", ShiroUtils.getUserId());

        activitiCommService.usersComplete(reMap);
        fmDd.setFmddId(businessKey);
        fmDd.setCurrentState("6");
        fmDd.setXtIds(userId);
        dispatchService.updateDispatch(fmDd);

        try {
            //给选择的人发送短信
            FmDd fd = dispatchService.selectFmddById(fmDd.getFmddId());
            //根据账号id查询人员
            String masterPerId=userId==null?"":userId.toString();
            String[] masterPerIds=masterPerId.split(",");

            if(masterPerIds.length>0){
                for(String mId:masterPerIds){
                    if(StringUtils.isNotEmpty(mId)){
                        OgUser ogUser = ogUserService.selectOgUserByUserId(mId);
                        OgPerson person = iOgPersonService.selectOgPersonById(ogUser.getpId());// 获取短信接收人
                        PubBizPresms p = new PubBizPresms();
                        p.setTelephone(person.getMobilPhone());// 手机号
                        p.setName(person.getpName());// 姓名
                        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
                        p.setSmsText("调度请求单号："+fd.getFaultNo()+"需要您帮忙协助处理，请登录运维管理平台查看");
                        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
                        p.setInspectObject("050300");// 检查对象
                        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
                        p.setInvalidationMark("1");
                        p.setSmsState("0");
                        p.setPubBizPresmsId(UUID.getUUIDStr());
                        pubBizPresmsService.insertPubBizPresms(p);
                        pubBizSmsService.findPreSmsAndSend(p);//发送短信
                    }
                }
            }
            return AjaxResult.success("操作成功");
        } catch (Exception e) {
            log.error("协同处理调度请求单短信发送发送失败: "+e.getMessage());
            throw  new BusinessException("协同处理失败","请刷新页面进行重试");
        }


    }


    /**
     * 根据角色查询账号
     * @return
     */
    @PostMapping("/syslist")
    @ResponseBody
    public TableDataInfo syslist(OgPerson ogPerson) {
        startPage();
        //审核人
        List<OgPerson> checkList = ogPersonService.selectListByRoleId(ogPerson);
        return getDataTable(checkList);
    }

    /**
     * 协同处理页面
     *
     * @return
     */
    @GetMapping("/synergy/{fmddId}")
    public String synergy(@PathVariable("fmddId")String fmddId, ModelMap mp) {
        mp.put("fmddId",fmddId);
        return prefix + "/synergy";
    }



    /**
     * 协同处理添加页面
     *
     * @return
     */
    @GetMapping("/selectAccount")
    public String selectAccount() {
        return prefix + "/selectAccount";
    }


    /**
     * 处理是否通过
     */
    @PostMapping("/feedPass")
    @ResponseBody
    public AjaxResult auditPass(FmDd fmDd) {

        FmDd fd = dispatchService.selectFmddById(fmDd.getFmddId());

        if (!"6".equals(fd.getCurrentState())) {
            return AjaxResult.error("该请求已被处理，请刷新列表检查后继续操作!");
        } else {
            OgUser u = ShiroUtils.getOgUser();
            String comment = (String) fmDd.getParams().get("comment");
            Map<String, Object> map = new HashMap<>();
            map.put("userId", ShiroUtils.getUserId());

            if ("1".equals(fmDd.getPassFlag())) {
                // 此时流程走向为调度处理
                map.put("reCode", "0");
                //根据登录人查询角色
                map.put("groupId", "0502");
            }
            try{
            String businessKey = fmDd.getFmddId();
            map.put("businessKey", businessKey);
            map.put("comment", comment);
            String processDefinitionKey = "FmDd";
            map.put("processDefinitionKey", processDefinitionKey);
            //处理的时候添上当前登录人的id
            if (StringUtils.isEmpty(fd.getParticipatorIds())) {
                fd.setParticipatorIds(u.getpId() + ",");
            } else {
                fd.setParticipatorIds(fd.getParticipatorIds() + u.getpId() + ",");
            }
            dispatchService.updateDispatch(fd);

            activitiCommService.usersComplete(map);

            dispatchService.updateDispatch(fmDd);

                //给选择的人发送短信
                //根据创建人id查询人员
                OgPerson person = iOgPersonService.selectOgPersonById(fd.getCreateId().toString());// 获取短信接收人
                PubBizPresms p = new PubBizPresms();
                p.setTelephone(person.getMobilPhone());// 手机号
                p.setName(person.getpName());// 姓名
                p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
                p.setSmsText("调度请求单号："+fd.getFaultNo()+"已经处理完成，请登录运维管理平台查看");
                p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
                p.setInspectObject("050300");// 检查对象
                p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
                p.setInvalidationMark("1");
                p.setSmsState("0");
                p.setPubBizPresmsId(UUID.getUUIDStr());
                pubBizPresmsService.insertPubBizPresms(p);
                pubBizSmsService.findPreSmsAndSend(p);//发送短信

                return AjaxResult.success("操作成功");
            }catch (Exception e){
                log.error("反馈调度请求单短信发送 发送失败: "+e.getMessage());
                throw  new BusinessException("协同处理失败","请刷新页面进行重试");
            }
            }

    }


    /**
     * 删除附件
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }

    /**
     * 点击添加反馈人按钮打开页面回显所选中的账号信息
     *
     * @param fmddId
     * @return
     */
    @PostMapping("/getAccountList/{fmddId}")
    @ResponseBody
    public TableDataInfo getAccountList(@PathVariable("fmddId") String fmddId) {
        List<OgPerson> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(fmddId)) {
            FmDd fmDd = dispatchService.selectFmddById(fmddId);
            if (fmDd != null) {
                if (StringUtils.isNotEmpty(fmDd.getXtIds())) {
                    String s[] = fmDd.getXtIds().split(",");
                    for (int i = 0; i < s.length; i++) {
                        OgUser user =ogUserService.selectOgUserByUserId(s[i]);
                        OgPerson person = ogPersonService.selectOgPersonById(user.getpId());
                        if (person != null) {
                            list.add(person);
                        }
                    }
                }
            }
        }
        return getDataTable(list);
    }




}
