package com.ruoyi.activiti.controller.itsm.lxbg;

import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 填报例行变更作业
 */
@Controller
@RequestMapping("/lxbg/filllxbg")
@Transactional(rollbackFor = Exception.class)
public class FillLxbgController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FillLxbgController.class);


    //填报例行变更路径
    private String prefix = "lxbg/filllxbg";

    //查询资源变更单路径
    private String prefix_public = "cmbiz";



    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    /**
     * 转到填报例行变更页面
     * @param map
     * @return
     */
    @GetMapping()
    public String fillLxbg(ModelMap map)
    {
        //获取当前登陆账号的关联的人员Id
        String personId = ShiroUtils.getOgUser().getpId();
        map.addAttribute("personId", personId);

        return prefix + "/filllxbg";
    }

    /**
     * 列表展示
     * @param smBizTaskinfo
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizTaskinfo smBizTaskinfo)
    {

        if (!StringUtils.isEmpty(smBizTaskinfo.getPlanStartDate())){
            smBizTaskinfo.setPlanStartDate(smBizTaskinfo.getPlanStartDate().replaceAll("-", ""));
        }

        if (!StringUtils.isEmpty(smBizTaskinfo.getPlanEndDate())) {
            smBizTaskinfo.setPlanEndDate(smBizTaskinfo.getPlanEndDate().replaceAll("-", ""));
        }

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //当前登录人的工作组信息
        List<OgGroup> ogGroup =  workService.selectGroupByUserId(pId);

        List<String>  groupIds= new ArrayList<>();
        for (OgGroup ogGroup2: ogGroup){
            groupIds.add(ogGroup2.getGroupId());
        }


        List<SmBizTaskinfo> list = smBizTaskinfoService.selectFillLxbgList(smBizTaskinfo);

        //过滤条件
        List<SmBizTaskinfo> list1 = list.stream().filter((item) -> {
            return groupIds.contains( item.getPerformGroupId()) || ogOrg.getOrgId().equals(item.getPerformDeptId());
        }).collect(Collectors.toList());


        return getDataTable_ideal(list1);

    }


    /**
     * 转到填报例行变更页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/lxbg/filllxbg/{id}")
    public String edit(@PathVariable("id") String  id, ModelMap map)
    {
        //当前登录人
        String pId = ShiroUtils.getOgUser().getpId();
        //处理人回显
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        String Pid = ogPerson.getpId();
        String pName = ogPerson.getpName();
        map.put("Pid",Pid);
        map.put("pName",pName);



        //获取当前登录人所在的机构
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        OgOrg o= iSysDeptService.selectDeptById(person.getOrgId());
        String orgName= o.getOrgName();
        map.addAttribute("orgName", orgName);

        //获取当前登录人所在的工作组
        List<OgGroup> ogGroup =  workService.selectGroupByUserId(person.getpId());
        map.put("ogGroup",ogGroup);



        //获取当前页面的信息
        SmBizTaskinfo bizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoById(id);
        map.put("bizTaskinfo",bizTaskinfo);

        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(bizTaskinfo.getSmBizScheduling().getFolder());
        map.put("sf",folder.getName_());

        //接收范围
        bizTaskinfo.getSmBizScheduling().setSendRange(bizTaskinfo.getSmBizTask().getSendRange());

        //填写时间
        String date = DateUtils.getTime();
        map.put("writeTime",date);
        map.put("schedulingId",bizTaskinfo.getSmBizScheduling().getSchedulingId());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);



        //任务开始时间
        String data1 = DateUtils.dateTimeNow();
        bizTaskinfo.setWorkBeginTime(data1);

        //创建时间进行日期回显
        String createTime = bizTaskinfo.getSmBizScheduling().getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            bizTaskinfo.getSmBizScheduling().setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        //填写时间进行日期回显
        String writeTime = date;
        if(StringUtils.isNotEmpty(writeTime)){
            DateUtils.formatDateStr(writeTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS);
        }


        //计划发布时间（上）进行日期回显
        String startTime = bizTaskinfo.getSmBizTask().getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            bizTaskinfo.getSmBizTask().setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        //计划发布时间(下)进行日期回显
        String performDate = bizTaskinfo.getPerformDate();
        if(StringUtils.isNotEmpty(performDate)){
            bizTaskinfo.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }





        return prefix + "/disposelxbg";
    }


    /**
     * 通过
     *
     * @param
     * @return
     */
    @PostMapping("/tianbao")
    @ResponseBody
    public AjaxResult auditPass(SmBizTaskinfo smBizTaskinfo) {

        SmBizTaskinfo bizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoById(smBizTaskinfo.getTaskFormId());

        if (!"02".equals(bizTaskinfo.getTaskFormStatus())) {
            return AjaxResult.error("该事件单已被填报，请刷新列表检查后继续操作!");
        }else {

            //任务结束时间
            String data1 = DateUtils.dateTimeNow();
            smBizTaskinfo.setWorkEndTime(data1);

            //计划任务日期-实际
            String data2 = DateUtils.dateTime();
            smBizTaskinfo.setExcuteDate(data2);

            //计划任务时间-实际
            String data3 = DateUtils.dateTimeStr();
            smBizTaskinfo.setExcuteTime(data3);

            //有效标志
            smBizTaskinfo.setInvalidationMark("1");

            //存储处理人姓名
            OgPerson ogPerson = ogPersonService.selectOgPersonById(smBizTaskinfo.getPerformUserId());
            String Pid = ogPerson.getpId();
            String pName = ogPerson.getpName();
            smBizTaskinfo.setPerformUserName(pName);

            smBizTaskinfo.setTaskFormStatus("03");
            String comment = (String) smBizTaskinfo.getParams().get("comment");
            smBizTaskinfo.setExcuteDescription(comment);
            smBizTaskinfoService.updateSmBizTaskinfo(smBizTaskinfo);
            //计划状态改变
            SmBizScheduling smBizScheduling = new SmBizScheduling();
            String scid = smBizTaskinfo.getSmBizScheduling().getSchedulingId();
            smBizScheduling.setSchedulingId(scid);
            smBizScheduling.setPlanStatus("05");

            addlxbgService.updatelxbg(smBizScheduling);

            SmBizTaskinfo smInfo = new SmBizTaskinfo();
            smInfo.setTaskFormStatus("03");
            smInfo.setTaskId(bizTaskinfo.getTaskId());
            List<SmBizTaskinfo> smList = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smInfo);
            SmBizScheduling sh = smBizTaskinfo.getSmBizScheduling();
            String process = addlxbgService.selectSchedulingById(sh.getSchedulingId()).getProcess();
            int index = process.indexOf("/");
            process = process.substring(index, process.length());
            String newProcess = smList.size() + process;
            sh.setProcess(newProcess);
            addlxbgService.updatelxbg(sh);
            return toAjax(1);
        }
    }



    /**
     * 关联变更单页面打开
     * @return
     */
    @GetMapping("/glbg")
    public String search() {
        return prefix + "/glbgsearch";

    }




}
