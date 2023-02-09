package com.ruoyi.activiti.controller.itsm.lxbg;

import java.util.List;

import com.ruoyi.activiti.constants.FmDdConstants;
import com.ruoyi.activiti.constants.FmSwConstants;
import com.ruoyi.activiti.service.ISmBizTaskinfoService;
import com.ruoyi.activiti.service.TaskService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 【任务】Controller
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
@Controller
@RequestMapping("/system/taskinfo")
public class SmBizTaskinfoController extends BaseController
{
    private String prefix = "system/taskinfo";

    //任务查看详情页面
    private String add_prefix = "lxbg/addlxbg";


    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    @Autowired
    private IOgPersonService ogPersonService;


    @Autowired
    private TaskService taskService;

    @GetMapping()
    public String taskinfo()
    {
        return prefix + "/taskinfo";
    }

    /**
     * 查询任务列表
     * @param smBizTaskinfo
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizTaskinfo smBizTaskinfo)
    {
        String schedulingId=smBizTaskinfo.getParams().get("schedulingId")==null?"":smBizTaskinfo.getParams().get("schedulingId").toString();
        SmBizTask smBizTask = taskService.selectSchedulingId(schedulingId);
        if(smBizTask!=null){
            smBizTaskinfo.setTaskId(smBizTask.getTaskId());
        }else {
            smBizTaskinfo.setTaskId("1");
        }

        smBizTaskinfo.setGenerateTime(handleTimeYYYYMMDDHHMMSS(smBizTaskinfo.getGenerateTime()));
        smBizTaskinfo.setEndgenerateTime(handleTimeYYYYMMDDHHMMSS(smBizTaskinfo.getEndgenerateTime()));


        List<SmBizTaskinfo> list = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smBizTaskinfo);
        return getDataTable_ideal(list);
    }



    /**
     * 查询任务列表
     * @param
     * @return
     */
    @PostMapping("/listByRwId")
    @ResponseBody
    public TableDataInfo listByRwId(String schedulingId)
    {
        startPage();
        SmBizTask smBizTask = taskService.selectSchedulingId(schedulingId);
        SmBizTaskinfo smBizTaskinfo1 = new SmBizTaskinfo();
        smBizTaskinfo1.setTaskId(smBizTask.getTaskId());
        List<SmBizTaskinfo> smBizTaskinfos = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smBizTaskinfo1);
        return getDataTable_ideal(smBizTaskinfos);
    }


    /**
     * 导出例行变更计划任务页面
     * @param
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SmBizTaskinfo smBizTaskinfo)
    {

        String schedulingId=smBizTaskinfo.getParams().get("schedulingId")==null?"":smBizTaskinfo.getParams().get("schedulingId").toString();
        String isCurrentPage = (String)smBizTaskinfo.getParams().get("currentPage");
        SmBizTask smBizTask = taskService.selectSchedulingId(schedulingId);
        if(smBizTask!=null){
            smBizTaskinfo.setTaskId(smBizTask.getTaskId());
        }else {
            smBizTaskinfo.setTaskId("1");
        }
        if("currentPage".equals(isCurrentPage)){
            startPage();
            List<SmBizTaskinfo> list = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smBizTaskinfo);
            ExcelUtil<SmBizTaskinfo> util = new ExcelUtil<SmBizTaskinfo>(SmBizTaskinfo.class);
            return util.exportExcel(list, "例行变更计划");
        }else {
            smBizTaskinfo.setTaskId(smBizTask.getTaskId());
            List<SmBizTaskinfo> list = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smBizTaskinfo);
            ExcelUtil<SmBizTaskinfo> util = new ExcelUtil<SmBizTaskinfo>(SmBizTaskinfo.class);
            return util.exportExcel(list, "例行变更计划");
        }
    }





    /**
     * 新增
     * @return
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存
     * @param smBizTaskinfo
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SmBizTaskinfo smBizTaskinfo)
    {
        return toAjax(smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo));
    }

    /**
     * 修改
     * @param taskFormId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{taskFormId}")
    public String edit(@PathVariable("taskFormId") String taskFormId, ModelMap mmap)
    {
        SmBizTaskinfo smBizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoById(taskFormId);
        mmap.put("smBizTaskinfo", smBizTaskinfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param smBizTaskinfo
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SmBizTaskinfo smBizTaskinfo)
    {
        return toAjax(smBizTaskinfoService.updateSmBizTaskinfo(smBizTaskinfo));
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(smBizTaskinfoService.deleteSmBizTaskinfoByIds(ids));
    }

    /**
     * 查看详情
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap map)
    {
        //获取当前页面的信息
        SmBizTaskinfo smBizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoById(id);
        map.put("smBizTaskinfo",smBizTaskinfo);

        //获取当前处理人所在的机构
        String pId = smBizTaskinfo.getPerformUserId();
        if(StringUtils.isNotEmpty(pId)){
            OgPerson person= iOgPersonService.selectOgPersonById(pId);
            if(person !=null ){
                OgOrg o= iSysDeptService.selectDeptById(person.getOrgId());
                String orgName= o.getOrgName();
                map.addAttribute("orgName", orgName);
            }

        }



        if( StringUtils.isNotEmpty(smBizTaskinfo.getPerformUserId())){
            OgPerson ogPerson = ogPersonService.selectOgPersonById(smBizTaskinfo.getPerformUserId());
            if(ogPerson != null){
                String Pid = ogPerson.getpId();
                String pName = ogPerson.getpName();
                //处理人回显
                map.put("Pid",Pid);
                map.put("pName",pName);
            }

    }

        //创建时间进行日期回显
        String generateTime = smBizTaskinfo.getGenerateTime();
        if(StringUtils.isNotEmpty(generateTime)){
            smBizTaskinfo.setGenerateTime(DateUtils.formatDateStr(generateTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //计划发布时间进行日期回显
        String performDate = smBizTaskinfo.getPerformDate();
        if(StringUtils.isNotEmpty(performDate)){
            smBizTaskinfo.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


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

        return add_prefix + "/taskinfodetail";
    }

    /**
     * 任务下载
     */
    @GetMapping("/download/{taskFormId}")
    public void download(@PathVariable("taskFormId") String taskFormId,
                         HttpServletResponse response, HttpServletRequest request) throws Exception {
        SmBizTaskinfo smBizTaskinfo = new SmBizTaskinfo();
        smBizTaskinfo.setTaskFormId(taskFormId);
        List<SmBizTaskinfo> list = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smBizTaskinfo);
        for (SmBizTaskinfo bizTaskinfo : list) {
            String filePath = bizTaskinfo.getFilePath();
            String fileName = bizTaskinfo.getFileName();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "bizTaskinfo;fileName=" + FileUtils.setFileDownloadHeader(request, fileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
        }
    }

}
