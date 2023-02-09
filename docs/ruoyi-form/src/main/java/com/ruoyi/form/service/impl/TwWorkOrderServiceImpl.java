package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.activiti.bmp.service.IBmpService;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.service.forward.OgSysService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.entity.AutomationPlatFormsParams;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkOrder;
import com.ruoyi.form.entity.automation.JsonParamArr;
import com.ruoyi.form.enums.TwWorkEnum;
import com.ruoyi.form.mapper.TwWorkNodeMapper;
import com.ruoyi.form.mapper.TwWorkOrderMapper;
import com.ruoyi.form.service.AutomationPlatformsService;
import com.ruoyi.form.service.ITwWorkOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.mapper.OgSysMapper;
import com.ruoyi.system.mapper.OgUserMapper;
import com.ruoyi.system.service.IOgPersonService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Service
public class TwWorkOrderServiceImpl extends ServiceImpl<TwWorkOrderMapper, TwWorkOrder> implements ITwWorkOrderService {

    @Resource
    private TwWorkOrderMapper twWorkOrderMapper;
    @Resource
    private TwWorkNodeMapper twWorkNodeMapper;
    @Autowired
    private AutomationPlatformsService automationPlatformsService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Resource
    private OgSysMapper ogSysMapper;

    @Resource
    private OgUserMapper ogUserMapper;
    @Autowired
    private IBmpService bmpService;
    @Autowired
    private IOgPersonService personService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;


    @Override
    public void addSave(TwWorkOrder twWorkOrder) {
        twWorkOrder.setId(UUID.getUUIDStr());
        twWorkOrder.setCreateBy(ShiroUtils.getUserId());
        twWorkOrder.setCreateByName(ShiroUtils.getSysUser().getLoginName());
        twWorkOrder.setCreateTime(DateUtils.getNowDate());
        twWorkOrder.setStatus(TwWorkEnum.TO_BE_SUBMITTED.getInfo());
        twWorkOrderMapper.insert(twWorkOrder);
    }

    @Override
    public List<TwWorkOrder> getTwWorkOrder(TwWorkOrder twWorkOrder) {
        String userId = ShiroUtils.getUserId();
        String[] statusList = {"执行"};
        twWorkOrder.setStatusList(Arrays.asList(statusList));
        twWorkOrder.setImplementorId(userId);
        List<TwWorkOrder> twWorkOrderList = twWorkOrderMapper.getTwWorkOrderList(twWorkOrder);
        return twWorkOrderList;
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    @Override
    public List<TwWorkOrder> backlogList(TwWorkOrder twWorkOrder) {
        String[] statusList = {"初审", "复核", "待提交", "执行", "复审","确认关闭"};
        twWorkOrder.setStatusList(Arrays.asList(statusList));
        String userId = ShiroUtils.getUserId();
        twWorkOrder.setDealPersonId(userId);
        List<TwWorkOrder> twWorkOrderList = twWorkOrderMapper.getTwWorkOrderList(twWorkOrder);
        return twWorkOrderList;
    }

    //@Override
    public List<TwWorkOrder> backlogLists(TwWorkOrder twWorkOrder) {
//        twWorkOrder.setCreateBy(ShiroUtils.getUserId());
        //List<TwWorkOrder> list = new ArrayList<>();
        //获取个人待办任务
        List<Map<String, Object>> userTask = activitiCommService.userTask(TwWorkEnum.PROCESS_KEY.getInfo(), "");
        List<Map<String, Object>> groupTask = activitiCommService.groupTasks(TwWorkEnum.PROCESS_KEY.getInfo(), "");
        userTask.addAll(groupTask);

//        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey("TW20220818002")
//                .list();
        //activitiCommService.startProcess(twWorkOrder.getWorkNum(), "FWSQ", reMap);
        //businessKey：workNum processDefinitionKey FWSQ
        //String userId = ShiroUtils.getUserId();
        //List<Task> test = taskService.createTaskQuery().processDefinitionKey(TwWorkEnum.PROCESS_KEY.getInfo()).processInstanceBusinessKey(twWorkOrder.getWorkNum()).taskAssignee(userId).list();
//        List<Task> test2 = taskService.createTaskQuery().processInstanceBusinessKey(twWorkOrder.getWorkNum()).processDefinitionKey("FWSQ").taskAssignee(userId).list();
//        List<Task> test3 = taskService.createTaskQuery().processInstanceBusinessKey(twWorkOrder.getWorkNum()).processDefinitionKey("FWSQ").taskCandidateUser(userId).list();

//        QueryWrapper<TwWorkOrder> wrapper = new QueryWrapper<>();
//        wrapper.eq("create_by",ShiroUtils.getUserId()).eq("status",TwWorkEnum.TO_BE_SUBMITTED.getInfo());
//        List<TwWorkOrder> twWorkOrders = twWorkOrderMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(userTask)) {
            //所有待办workNum集合
            List<String> businesskeyList = userTask.stream().map(map -> map.get("businesskey") == null ? "" : map.get("businesskey").toString()).collect(Collectors.toList());
            startPage();
            return twWorkOrderMapper.getTwWorkOrderByWorkNumAndTwWorkOrder(twWorkOrder, businesskeyList);
        }

//        for(Map<String, Object> mp:userTask){
//            String id=mp.get("businesskey")==null?"":mp.get("businesskey").toString();
//            String taskName=mp.get("taskName").toString();
//            CmBizQingqiu vo = new CmBizQingqiu();
//            vo.setChangeId(id);
//            vo.setChangeCode(cmBizQingqiu.getChangeCode());//变更单号
//            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("createTime").toString())) {
//                vo.getParams().put("createTime",handleTimeYYYYMMDDHHMMSS(cmBizQingqiu.getParams().get("createTime").toString()));
//            }
//            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("endCreateTime").toString())) {
//                vo.getParams().put("endCreateTime",handleTimeYYYYMMDD(cmBizQingqiu.getParams().get("endCreateTime").toString()) + "240000");
//            }
//            vo.setChangeCategoryId(cmBizQingqiu.getChangeCategoryId());//变更分类ID
//            vo.setApplyName(cmBizQingqiu.getApplyName());//申请人
//            vo.setChangeSingleName(cmBizQingqiu.getChangeSingleName());//标题
//            if("shenpi".equals(type)) {
//                vo.setStauts("0000");//状态
//                if ("审批".equals(taskName)) {
//                    vo.setStauts("0300");//状态
//                } else if ("分管领导审批".equals(taskName)) {
//                    vo.setStauts("0600");//状态
//                }
//            } else if ("shouli".equals(type)) {
//                if ("受理".equals(taskName)) {}
//                vo.setStauts("0400");//状态
//            }
//            CmBizQingqiu cu = cmBizQingqiuService.selectBGQQVO(vo);
//            if (cu != null) {
//                OgPerson ogPerson = iOgPersonService.selectOgPersonById(cu.getApplicantId());
//                cu.setApplicantId(ogPerson.getpName());
//                cu.getParams().put("taskName", taskName);
//                list.add(cu);
//            }
//        }
//        return getDataTable_ideal(list);
        return null;
    }

    @Override
    public Object execute(Integer id) {
        TwWorkOrder twWorkOrder = twWorkOrderMapper.selectById(id);
        QueryWrapper<TwWorkNode> wrapper = new QueryWrapper<>();
        wrapper.eq("work_order_id", id);
        List<TwWorkNode> twWorkNodes = twWorkNodeMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(twWorkNodes)) {
            StringBuilder hostName = new StringBuilder();
            StringBuilder osImage = new StringBuilder();
            StringBuilder osFlavor = new StringBuilder();
            StringBuilder osVolumeName = new StringBuilder();
            StringBuilder osVolumeSize = new StringBuilder();
            twWorkNodes.forEach(twWorkNode -> {
                hostName.append(",").append(twWorkNode.getHostName());
                osImage.append(",").append(twWorkNode.getOsVersion());
                //cpu 内存 磁盘
                osFlavor.append(",").append(twWorkNode.getApplyCpu()).append(twWorkNode.getApplyMemory()).append(twWorkNode.getDiskSize());
                //分卷名称
                osVolumeName.append(",").append(twWorkNode.getOsVolumeName());
                //分卷大小
                osVolumeSize.append(",").append(twWorkNode.getOsVolumeSize());
            });
            ArrayList<JsonParamArr> list = new ArrayList<>();
            //value：zncx-jkdspoc01,zncx-jkdspoc02 主机名
            JsonParamArr hostNameJsonParamArr = new JsonParamArr("hostName_list", "String", hostName.substring(1), "主机名（多台逗号隔开）");
            //镜像名 CentOS7.8_v5,CentOS7.8_v5
            JsonParamArr osImageJsonParamArr = new JsonParamArr("os_Image_list", "String", osImage.substring(1), "镜像名（多台主机以逗号隔开分隔）");
            //OS配置 4C8G35G,4C8G35G
            JsonParamArr osFlavorJsonParamArr = new JsonParamArr("os_Flavor_list", "String", osFlavor.substring(1), "OS配置（多台以逗号分隔）");
            //主机对应的osVolumeName名称
            JsonParamArr osVolumeNameJsonParamArr = new JsonParamArr("os_Volume_Name_list", "String", osVolumeName.substring(1), "主机对应的分卷名（多台以逗号分隔）");
            //主机对应的osVolume大小
            JsonParamArr osVolumeSizeJsonParamArr = new JsonParamArr("os_Volume_Size_list", "String", osVolumeSize.substring(1), "主机对应的Volumn大小，填写数字，默认多少G（多台主机以逗号隔开）");
            list.add(hostNameJsonParamArr);
            list.add(osImageJsonParamArr);
            list.add(osFlavorJsonParamArr);
            list.add(osVolumeNameJsonParamArr);
            list.add(osVolumeSizeJsonParamArr);
            AutomationPlatFormsParams automationPlatFormsParams = new AutomationPlatFormsParams("ITSM_OpenStack虚拟机创建_参数来源输入_副本", twWorkNodes.get(0).getEquipmentIds(), twWorkOrder.getWorkNum(), list);
            automationPlatformsService.commitTask(automationPlatFormsParams);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object submit(Integer state, TwWorkOrder twWorkOrder) {
        //state=0 暂存 state=1 提交
        if (state == 1) {
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("businesskey", twWorkOrder.getWorkNum());
            //审批人
            reMap.put("CHECKER_ID", twWorkOrder.getImplementorId());
            //reMap.put("dealId",cmBizQingqiu.getCheckerId());
            reMap.put("creatId", ShiroUtils.getUserId());
            reMap.put("userId", ShiroUtils.getUserId());
            // activitiCommService.startProcess(businessKey, "FWSQ", map);
            // activitiCommService.startProcess(twWorkOrder.getWorkNum(), "FWSQ", reMap);
            if ("1".equals(twWorkOrder.getParams().get("flag"))) {
                //执行流程
                reMap.put("businessKey", twWorkOrder.getWorkNum());
                activitiCommService.complete(reMap);
            } else {
                BmpEntity bmpEntity = startProcess(twWorkOrder.getWorkNum(), reMap);
                twWorkOrder.setInstanceId(bmpEntity.getProcessInstanceId());
            }
            twWorkOrder.setStatus(TwWorkEnum.FIRST_TRIAL.getInfo());
            twWorkOrder.setDealPersonId(twWorkOrder.getImplementorId());
            twWorkOrder.setDealPersonName(twWorkOrder.getImplementorPeopleName());
            twWorkOrderMapper.updateById(twWorkOrder);
        }else{
            twWorkOrder.setStatus(TwWorkEnum.TO_BE_SUBMITTED.getInfo());
            twWorkOrderMapper.updateById(twWorkOrder);
        }
        return null;
    }

    @Override
    @Transactional
    public BmpEntity startProcess(String workNum, Map<String, Object> variables) {
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setBusinessKey(workNum);
        bmpEntity.setProcessDefinitionKey(TwWorkEnum.PROCESS_KEY.getInfo());
        bmpEntity.setProcessVariables(variables);
        bmpService.startProcess(bmpEntity);
        return bmpEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPass(TwWorkOrder twWorkOrder) {

        String state = (String) twWorkOrder.getParams().get("type");
        String acceptorId = (String) twWorkOrder.getParams().get("checkerId");
        String comment = (String) twWorkOrder.getParams().get("comment");
        TwWorkOrder twOrder = twWorkOrderMapper.selectTwWorkOrderById(String.valueOf(twWorkOrder.getId()));
        String status = twOrder.getStatus();

        Map<String, Object> map = new HashMap<>(8);

        map.put("businessKey", twOrder.getWorkNum());
        //map.put("processInstancedId", twWorkOrder.getInstanceId());
        //这是审批页面的备注
        map.put("comment", comment);
        // map.put("processDefinitionKey", TwWorkEnum.PROCESS_KEY.getInfo());
        map.put("userId", ShiroUtils.getUserId());

        if(status.equals("复核")) {

            map.put("recode", "0");
            twOrder.setStatus("确认关闭");
            map.put("create_id",twOrder.getCreateBy());
        } else if (status.equals("确认关闭")){
            map.put("recode", "0");
            twOrder.setStatus("已关闭");
        }else if (("初审").equals(status)) {

            map.put("recode", state);
            if ("1".equals(state)) {//审核转派
                map.put("review_id", acceptorId);
                twOrder.setStatus("复审");
            } else if ("2".equals(state)) {//终审通过到执行
                map.put("execute_id", acceptorId);
                twOrder.setStatus("执行");
            } else {
                twOrder.setStatus("待提交");
                acceptorId = twWorkOrder.getCreateBy();
            }

        } else if(("复审").equals(status)) {

            map.put("recode", state);
            if ("1".equals(state)) {//审核转派
                map.put("CHECKER_ID", acceptorId);
                twOrder.setStatus("初审");
            } else if ("2".equals(state)) {//终审通过到执行
                map.put("execute_id", acceptorId);
                twOrder.setStatus("执行");
            } else {
                twOrder.setStatus("待提交");
                acceptorId = twWorkOrder.getCreateBy();
            }
        } else if(("执行").equals(status)) {
            if ("3".equals(state)) {
                map.put("recode", "2");
                map.put("execute_id", acceptorId);

            } else if ("1".equals(state)) {
                map.put("recode", state);
                map.put("re_check", twWorkOrder.getCreateBy());
                twOrder.setStatus("复核");
                acceptorId = twWorkOrder.getCreateBy();
            } else {
                map.put("recode", state);
                twOrder.setStatus("待提交");
                acceptorId = twWorkOrder.getCreateBy();
            }
        } else if (("待提交").equals(status)) {

            map.put("CHECKER_ID", acceptorId);
            twOrder.setStatus("初审");
        }
        map.put("re_update", twOrder.getCreateBy());
        if (StringUtils.isNotEmpty(acceptorId)) {

            twOrder.setDealPersonId(acceptorId);
            OgPerson ogPerson = personService.selectOgPersonEvoById(acceptorId);
            twOrder.setDealPersonName(ogPerson.getpName());
        }
        //if (!status.equals("复核")) {
        //    //复审人
        //    map.put("review_id", twWorkOrder.getImplementorId());
        //    //执行人
        //    map.put("execute_id", twWorkOrder.getImplementorId());
        //    //复核人（创建人）
        //    map.put("re_check", twWorkOrder.getCreateBy());
        //    //重新修改人（创建人）
        //    map.put("re_update", twWorkOrder.getCreateBy());
        //}
        //执行流程
        activitiCommService.complete(map);

        twWorkOrderMapper.updateTwWorkOrderById(twOrder);

    }

//    @Override
//    public List<OgSys> syslist(OgSys sys) {
//        String userId = ShiroUtils.getUserId();
//        String sysIds = ogSysMapper.getSysId(userId);
//        String[] split = sysIds.split(",");
//        List<String> list = Arrays.asList(split);
//        // String collect = list.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
//        //sys.setSysids(collect);
//        sys.setSysids(list);
//        return ogSysMapper.selectOgSysListByLoginUser(sys);
//
//    }

    @Override
    public List<OgSys> syslist(OgSys sys) {
        OgSys ogSys = new OgSys();
        String userId = ShiroUtils.getUserId();
//        List<String> list = new ArrayList<>();
//        List<String> sysIds = ogSysMapper.getSysId(userId);
//        if (!CollectionUtils.isEmpty(sysIds)) {
//            for (String sysId : sysIds) {
//                String[] split = sysId.split(",");
//                list.addAll(Arrays.asList(split));
//            }
//        }
//        OgSys ogSys = new OgSys();
//        ogSys.setPid(userId);
//
//        // String collect = list.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
//        //sys.setSysids(collect);
//        sys.setSysids(list);
        OgUser ogUser = ogUserMapper.selectOgUserByUserIdMysql(userId);
        if (!StringUtils.isEmpty(ogUser)) {
            ogSys.setPid(ogUser.getUsername());
        }
        List<OgSys> ogSysList = ogSysMapper.selectOgSysList(ogSys);
        return ogSysList;

    }

//    @Override
//    public List<OgSys> syslist(OgSys sys) {
//        String userId = ShiroUtils.getUserId();
//        String sysIds = ogSysMapper.getSysId(userId);
//        OgSys ogSys = new OgSys();
//        ogSys.setPid(userId);
//        String[] split = sysIds.split(",");
//        List<String> list = Arrays.asList(split);
//        // String collect = list.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
//        //sys.setSysids(collect);
//        sys.setSysids(list);
//        return ogSysMapper.selectOgSysListByLoginUser(sys);
//
//    }


    @Override
    public List<TwWorkOrder> getTwWorkOrderAll(TwWorkOrder twWorkOrder) {
        List<TwWorkOrder> twWorkOrderList = twWorkOrderMapper.getTwWorkOrder(twWorkOrder);
        return twWorkOrderList;
    }

    @Override
    public List<TwWorkOrder> getTwWorkMyOrder(TwWorkOrder twWorkOrder) {
        String userId = ShiroUtils.getUserId();
        String[] statusList = {"初审", "复核", "待提交", "执行", "复审"};
        twWorkOrder.setStatusList(Arrays.asList(statusList));
        twWorkOrder.setCreateBy(userId);
        List<TwWorkOrder> twWorkOrderList = twWorkOrderMapper.getTwWorkOrderList(twWorkOrder);
        return twWorkOrderList;
    }

    @Override
    public TwWorkOrder selectTwWorkOrderById(String id) {
        return twWorkOrderMapper.selectTwWorkOrderById(id);
    }

    @Override
    public int toCancle(String id) {
        return twWorkOrderMapper.toCancle(id);
    }

}
