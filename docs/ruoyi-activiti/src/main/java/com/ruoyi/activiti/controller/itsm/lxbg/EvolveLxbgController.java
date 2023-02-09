package com.ruoyi.activiti.controller.itsm.lxbg;

import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *例行变更进展情况
 */
@Controller
@RequestMapping("/lxbg/evolvelxbg")
public class EvolveLxbgController extends BaseController {


    //审批例行变更路径
    private String prefix = "lxbg/evolvelxbg";

    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;


    /**
     * 转到例行变更进展情况页面
     * @param map
     * @return
     */
    @GetMapping()
    public String evolbelxbg(ModelMap map)
    {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        map.put("pId",ogUser.getpId());

        return prefix + "/evolvelxbg";
    }


    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizScheduling scheduling)
    {
        //根据目录树ID回显计划表信息
        SmBizFolder smBizFolder = folderService.selectFolderTreeById(scheduling.getFolder());
        if(smBizFolder!=null){
            scheduling.setFolder(smBizFolder.getId_());
        }else {
            scheduling.setFolder("1");
        }
        List<SmBizScheduling> list = addlxbgService.selectEvoScheduling(scheduling);
        return getDataTable_ideal(list);

    }


    /**
     * 查看详情(任务)
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/evodetail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);
        SmBizTaskinfo smBizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoById(id);

        //处理人回显
        if( StringUtils.isNotEmpty(smBizTaskinfo.getPerformUserId())) {

            OgPerson ogPerson = ogPersonService.selectOgPersonById(smBizTaskinfo.getPerformUserId());
            if(ogPerson !=null){
                String Pid = ogPerson.getpId();
                String pName = ogPerson.getpName();
                map.put("Pid", Pid);
                map.put("pName", pName);
            }

        }

        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("smBizTaskinfo",smBizTaskinfo);

        //处理开始时间进行日期回显
        String workBeginTime = smBizTaskinfo.getWorkBeginTime();
        if(StringUtils.isNotEmpty(workBeginTime)){
            smBizTaskinfo.setWorkBeginTime(DateUtils.formatDateStr(workBeginTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        //处理结束时间进行日期回显
        String workEndTime = smBizTaskinfo.getWorkEndTime();
        if(StringUtils.isNotEmpty(workEndTime)){
            smBizTaskinfo.setWorkEndTime(DateUtils.formatDateStr(workEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //生成时间进行日期回显
        String generateTime = smBizTaskinfo.getGenerateTime();
        if(StringUtils.isNotEmpty(generateTime)){
            smBizTaskinfo.setGenerateTime(DateUtils.formatDateStr(generateTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //计划时间进行日期回显
        String performDate = smBizTaskinfo.getPerformDate();
        if(StringUtils.isNotEmpty(performDate)){
            smBizTaskinfo.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        return prefix + "/evodetail";
    }



    /**
     * 查询任务列表
     * @param smBizTaskinfoThree
     * @return
     */
    @PostMapping("/evolist")
    @ResponseBody
    public TableDataInfo list(SmBizTaskinfoThree smBizTaskinfoThree)
    {
        startPage();
        String schedulingId=smBizTaskinfoThree.getParams().get("schedulingId")==null?"":smBizTaskinfoThree.getParams().get("schedulingId").toString();
        SmBizTask smBizTask = taskService.selectSchedulingId(schedulingId);
        if(smBizTask!=null){
            smBizTaskinfoThree.setTaskId(smBizTask.getTaskId());
        }else {
            smBizTaskinfoThree.setTaskId("1");
        }



        List<SmBizTaskinfoThree> list = smBizTaskinfoService.selectSmBizTaskinfoListEvo(smBizTaskinfoThree);

        return getDataTable_ideal(list);
    }


    /**
     * 查看详情(计划)
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/detail/{id}")
    public String scdetail(@PathVariable("id") String id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);
        SmBizTaskinfo smBizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoById(id);

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());

        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("smBizTaskinfo",smBizTaskinfo);

        //计划发布时间(下)进行日期回显
        String performDate = scheduling.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }



        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg!=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList",checkList);
        }


        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());
        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);


        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        return prefix + "/evoschdetail";
    }



    /**
     * 导出例行变更计划
     * @param
     * @return
     */
    @Log(title = "任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SmBizTaskinfoThree smBizTaskinfoThree)
    {

        String schedulingId=smBizTaskinfoThree.getParams().get("schedulingId")==null?"":smBizTaskinfoThree.getParams().get("schedulingId").toString();
        SmBizTask smBizTask = taskService.selectSchedulingId(schedulingId);
        if(smBizTask!=null){
            smBizTaskinfoThree.setTaskId(smBizTask.getTaskId());
        }else {
            smBizTaskinfoThree.setTaskId("1");
        }

        String isCurrentPage = (String)smBizTaskinfoThree.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            startPage();
            List<SmBizTaskinfoThree> list = smBizTaskinfoService.selectSmBizTaskinfoListEvo(smBizTaskinfoThree);
            ExcelUtil<SmBizTaskinfoThree> util = new ExcelUtil<SmBizTaskinfoThree>(SmBizTaskinfoThree.class);
            return util.exportExcel(list, "任务");
        }else{
            smBizTaskinfoThree.setTaskId(smBizTask.getTaskId());
            List<SmBizTaskinfoThree> list = smBizTaskinfoService.selectSmBizTaskinfoListEvo(smBizTaskinfoThree);
            ExcelUtil<SmBizTaskinfoThree> util = new ExcelUtil<SmBizTaskinfoThree>(SmBizTaskinfoThree.class);
            return util.exportExcel(list, "任务");
        }
    }



    /**
     * 导出例行变更计划
     * @param scheduling
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.EXPORT)
    @PostMapping("/jhexport")
    @ResponseBody
    public AjaxResult jhexport(SmBizScheduling scheduling)
    {

        String isCurrentPage = (String)scheduling.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            //根据目录树ID回显计划表信息
            SmBizFolder smBizFolder = folderService.selectFolderTreeById(scheduling.getFolder());
            if(smBizFolder!=null){
                scheduling.setFolder(smBizFolder.getId_());
            }else {
                scheduling.setFolder("1");
            }
            startPage();
        }else if("all".equals(isCurrentPage)){
            //根据目录树ID回显计划表信息
            SmBizFolder smBizFolder = folderService.selectFolderTreeById(scheduling.getFolder());
            if(smBizFolder!=null){
                scheduling.setFolder(smBizFolder.getId_());
            }else {
                scheduling.setFolder("1");
            }
        }

        List<SmBizScheduling> list = addlxbgService.selectEvoScheduling(scheduling);
        ExcelUtil<SmBizScheduling> util = new ExcelUtil<SmBizScheduling>(SmBizScheduling.class);
        return util.exportExcel(list, "例行变更计划");
    }








}
