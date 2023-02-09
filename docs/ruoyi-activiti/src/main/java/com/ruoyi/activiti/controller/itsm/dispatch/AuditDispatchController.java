package com.ruoyi.activiti.controller.itsm.dispatch;

import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.FmDispatchService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.*;
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
 * 审核调度事请求
 */

@Controller
@RequestMapping("/dispatch/audit")
@Transactional(rollbackFor = Exception.class)
public class AuditDispatchController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuditDispatchController.class);


    @Autowired
    private FmDispatchService dispatchService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysWorkService sysWorkService;

    private String prefix = "dispatch/audit";





    /**
     * 转到 审核调度请求 页面
     * @return
     */
    @GetMapping()
    public String auditDD(ModelMap map)
    {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        map.put("pId",ogUser.getpId());
        return prefix + "/audit";
    }


    /**
     * 审核状态的信息展示
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmDd fmDd)
    {
        startPage();
        List<Map<String,Object>> reList =activitiCommService.userTask("FmDd",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmDd","shenghe");
            reList.addAll(regList);
        }
        List<String> fmDdIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("shenghe".equals(map.get("description"))){
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
     *审核按钮对应的页面
     */
    @GetMapping("/audit/auditAuthor/{id}")
    public String auditAuthor(@PathVariable("id") String id, ModelMap map)
    {
        //获取页面的信息
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd != null){
            map.put("fmdd",fmDd);

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


        return prefix + "/auditAuthor";
    }



    /**
     * 审核是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/auditPass")
    @ResponseBody
    public AjaxResult auditPass(FmDd fmDd) {

        FmDd fd = dispatchService.selectFmddById(fmDd.getFmddId());

        if (!"4".equals(fd.getCurrentState())) {
            return AjaxResult.error("该请求已被审核，请刷新列表检查后继续操作!");
        }else{
            String comment = (String) fmDd.getParams().get("comment");
            Map<String, Object> map = new HashMap<>();
            map.put("userId", ShiroUtils.getUserId());

            String status = "";
            if ("1".equals(fmDd.getPassFlag())) {
                // 此时流程走向为调度处理
                map.put("reCode", "0");
                status = "5";
                map.put("groupId", "0502");

                fmDd.setCurrentState(status);
                String businessKey = fmDd.getFmddId();
                map.put("businessKey", businessKey);
                map.put("comment", comment);
                String processDefinitionKey = "FmDd";
                map.put("processDefinitionKey", processDefinitionKey);

                try{
                    dispatchService.updateDispatch(fmDd);
                    activitiCommService.complete(map);
                }catch (Exception e){
                    log.error("调度请求单审核失败: "+e.getMessage());
                    throw  new BusinessException("调度请求单审核失败,请刷新页面进行重试");
                }
                return AjaxResult.success("操作成功");
            } else {
                // 此时流程走向为退回
                map.put("reCode", "1");
                status = "2";
                map.put("createId", fmDd.getCreateId());

                fmDd.setCurrentState(status);
                String businessKey = fmDd.getFmddId();
                map.put("businessKey", businessKey);
                map.put("comment", comment);
                String processDefinitionKey = "FmDd";
                map.put("processDefinitionKey", processDefinitionKey);

                try{
                    dispatchService.updateDispatch(fmDd);
                    activitiCommService.complete(map);
                }catch (Exception e){
                    log.error("调度请求单审核退回失败: "+e.getMessage());
                    throw  new BusinessException("调度请求单审核退回失败,请刷新页面进行重试");
                }
                return AjaxResult.success("操作成功");
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
        if(StringUtils.isNotEmpty(userId)){
            ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        }
        return ajaxResult;
    }









}
