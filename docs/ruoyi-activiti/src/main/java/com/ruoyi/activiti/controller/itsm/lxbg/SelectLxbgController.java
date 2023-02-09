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
import java.util.List;

/**
 * 查询例行变更作业
 */
@Controller
@RequestMapping("/lxbg/selectlxbg")
public class SelectLxbgController extends BaseController {

    //新增例行变更路径
    private String prefix = "lxbg/selectlxbg";


    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    @Autowired
    private IOgPersonService ogPersonService;


    /**
     * 转到查询例行变更页面
     * @param map
     * @return
     */
    @GetMapping()
    public String fillLxbg(ModelMap map)
    {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        map.put("pId",ogUser.getpId());

        return prefix + "/selectlxbg";
    }

    /**
     * 列表展示
     * @param
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizTaskinfoTwo smBizTaskinfoTwo)
    {

        startPage();

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getPublishTime())){
            smBizTaskinfoTwo.setPublishTime(smBizTaskinfoTwo.getPublishTime().replaceAll("-", "")+"000000");
        }

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getEndPublishTime())) {
            smBizTaskinfoTwo.setEndPublishTime(smBizTaskinfoTwo.getEndPublishTime().replaceAll("-", "")+"240000");
        }

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getWorkBeginTime())){
            smBizTaskinfoTwo.setWorkBeginTime(smBizTaskinfoTwo.getWorkBeginTime().replaceAll("-", "")+"000000");
        }

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getEndworkBeginTime())) {
            smBizTaskinfoTwo.setEndworkBeginTime(smBizTaskinfoTwo.getEndworkBeginTime().replaceAll("-", "")+"240000");
        }

        //日期格式转换
        List<SmBizTaskinfoTwo> list = smBizTaskinfoService.selectSmBizTaskinfoSList(smBizTaskinfoTwo);
        return getDataTable(list);

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
        SmBizTaskinfo smBizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoSelById(id);
        map.put("smBizTaskinfo",smBizTaskinfo);


        //计划类别
        smBizTaskinfo.getSmBizScheduling().setTaskTypeId(smBizTaskinfo.getSmBizTask().getTaskTypeId());
        //是否短信通知
        smBizTaskinfo.getSmBizScheduling().setMsgDoor(smBizTaskinfo.getSmBizTask().getMsgDoor());
        //接收范围
        smBizTaskinfo.getSmBizScheduling().setSendRange(smBizTaskinfo.getSmBizTask().getSendRange());

        //处理人回显
        if( StringUtils.isNotEmpty(smBizTaskinfo.getPerformUserId())) {

            OgPerson ogPerson = ogPersonService.selectOgPersonById(smBizTaskinfo.getPerformUserId());
            if(ogPerson != null){
                String Pid = ogPerson.getpId();
                String pName = ogPerson.getpName();
                map.put("Pid", Pid);
                map.put("pName", pName);
            }

        }



        //计划属性创建时间进行日期回显
        String createTime = smBizTaskinfo.getSmBizScheduling().getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            smBizTaskinfo.getSmBizScheduling().setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //任务基本信息生成时间进行日期回显
        String generateTime = smBizTaskinfo.getGenerateTime();
        if(StringUtils.isNotEmpty(generateTime)){
            smBizTaskinfo.setGenerateTime(DateUtils.formatDateStr(generateTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //任务基本信息生成计划发布时间进行日期回显
        String performDate = smBizTaskinfo.getPerformDate();
        if(StringUtils.isNotEmpty(performDate)){
            smBizTaskinfo.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        //任务处理信息处理开始时间进行日期回显
        String workBeginTime = smBizTaskinfo.getWorkBeginTime();
        if(StringUtils.isNotEmpty(workBeginTime)){
            smBizTaskinfo.setWorkBeginTime(DateUtils.formatDateStr(workBeginTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        //任务处理信息处理结束时间进行日期回显
        String workEndTime = smBizTaskinfo.getWorkEndTime();
        if(StringUtils.isNotEmpty(workEndTime)){
            smBizTaskinfo.setWorkEndTime(DateUtils.formatDateStr(workEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }


        return prefix + "/detail";
    }


    /**
     * 导出例行变更计划
     * @param
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SmBizTaskinfoTwo smBizTaskinfoTwo)
    {
        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getPublishTime())){
            smBizTaskinfoTwo.setPublishTime(smBizTaskinfoTwo.getPublishTime().replaceAll("-", "")+"000000");
        }

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getEndPublishTime())) {
            smBizTaskinfoTwo.setEndPublishTime(smBizTaskinfoTwo.getEndPublishTime().replaceAll("-", "")+"240000");
        }

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getWorkBeginTime())){
            smBizTaskinfoTwo.setWorkBeginTime(smBizTaskinfoTwo.getWorkBeginTime().replaceAll("-", "")+"000000");
        }

        if (!StringUtils.isEmpty(smBizTaskinfoTwo.getEndworkBeginTime())) {
            smBizTaskinfoTwo.setEndworkBeginTime(smBizTaskinfoTwo.getEndworkBeginTime().replaceAll("-", "")+"240000");
        }

        String isCurrentPage = (String)smBizTaskinfoTwo.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            startPage();
            List<SmBizTaskinfoTwo> list = smBizTaskinfoService.selectSmBizTaskinfoSList(smBizTaskinfoTwo);
            ExcelUtil<SmBizTaskinfoTwo> util = new ExcelUtil<SmBizTaskinfoTwo>(SmBizTaskinfoTwo.class);
            return util.exportExcel(list, "例行变更计划");
        }else {
            List<SmBizTaskinfoTwo> list = smBizTaskinfoService.selectSmBizTaskinfoSList(smBizTaskinfoTwo);
            ExcelUtil<SmBizTaskinfoTwo> util = new ExcelUtil<SmBizTaskinfoTwo>(SmBizTaskinfoTwo.class);
            return util.exportExcel(list, "例行变更计划");
        }

    }






}
