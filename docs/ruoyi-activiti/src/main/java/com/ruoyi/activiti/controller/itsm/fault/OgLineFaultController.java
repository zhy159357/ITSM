package com.ruoyi.activiti.controller.itsm.fault;

import java.util.Date;
import java.util.List;

import com.ruoyi.activiti.domain.OgLine;
import com.ruoyi.activiti.domain.SysLine;
import com.ruoyi.activiti.service.IOgLineFaultService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
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

/**
 * 【登记网络条线故障】Controller
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
@Controller
@RequestMapping("fault/xz")
public class OgLineFaultController extends BaseController
{
    private String prefix = "fault/xz";

    @Autowired
    private IOgLineFaultService ogLineFaultService;

    @GetMapping()
    public String fault()
    {
        return prefix + "/fault";
    }

    /**
     * 查询【网络】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysLine sysLine)
    {
        startPage();
        if (!StringUtils.isEmpty(sysLine.getCreateTimeTwo())){
            sysLine.setCreateTimeTwo(sysLine.getCreateTimeTwo().replaceAll("-", ""));
            sysLine.setCreateTimeTwo(sysLine.getCreateTimeTwo().replaceAll(" ",""));
            sysLine.setCreateTimeTwo(sysLine.getCreateTimeTwo().replaceAll(":",""));
        }
        List<SysLine> list = ogLineFaultService.selectOgLineFaultList(sysLine);
        return getDataTable(list);
    }

    /**
     * 新增【网络】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap, SysLine sysLine)
    {
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime",nowTime);

        mmap.put("ogLineFault",sysLine);
        return prefix + "/add";
    }

    /**
     * 新增保存【网络】
     */
    @Log(title = "【新增保存】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysLine sysLine)
    {
        if(StringUtils.isNotEmpty(sysLine.getCreateTimeTwo())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getCreateTimeTwo());
            sysLine.setCreateTimeTwo(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getHardwareTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getHardwareTime());
            sysLine.setHardwareTime(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getPartPresentTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getPartPresentTime());
            sysLine.setPartPresentTime(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getPersonPresentTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getPersonPresentTime());
            sysLine.setPersonPresentTime(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getDisposeFinishTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getDisposeFinishTime());
            sysLine.setDisposeFinishTime(parseStart);
        }
        //UUID
        sysLine.setHardwareId(UUID.getUUIDStr());
        return toAjax(ogLineFaultService.insertOgLineFault(sysLine));
    }

    /**
     * 修改【网络】
     */
    @GetMapping("/edit/{hardwareId}")
    public String edit(@PathVariable("hardwareId") String hardwareId, ModelMap mmap,SysLine sysLine)
    {
        SysLine sysLineTwo = ogLineFaultService.selectOgLineFaultById(hardwareId);
        mmap.put("sysLine", sysLineTwo);
        return prefix + "/edit";
    }

    /**
     * 修改保存【网络】
     */
    @Log(title = "【修改保存】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysLine sysLine)
    {
        if(StringUtils.isNotEmpty(sysLine.getCreateTimeTwo())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getCreateTimeTwo());
            sysLine.setCreateTimeTwo(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getHardwareTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getHardwareTime());
            sysLine.setHardwareTime(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getPartPresentTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getPartPresentTime());
            sysLine.setPartPresentTime(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getPersonPresentTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getPersonPresentTime());
            sysLine.setPersonPresentTime(parseStart);
        }
        if(StringUtils.isNotEmpty(sysLine.getDisposeFinishTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getDisposeFinishTime());
            sysLine.setDisposeFinishTime(parseStart);
        }
        return toAjax(ogLineFaultService.updateOgLineFault(sysLine));
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String hardwareId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        SysLine sysLine = ogLineFaultService.selectOgLineFaultById(hardwareId);
        ajaxResult.put("data",sysLine);
        return  ajaxResult;
    }

    /**
     * 删除【网络】
     */
    @Log(title = "【删除】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(ogLineFaultService.deleteOgLineFaultByIds(ids));
    }

    /**
     * 详情【网络】列表
     */
    @GetMapping("/detail/{hardwareId}")
    public String detail(@PathVariable("hardwareId") String hardwareId, ModelMap mmap)
    {
        SysLine sysLine = ogLineFaultService.selectOgLineFaultById(hardwareId);
        mmap.put("sysLine",sysLine);
        return prefix + "/detail";
    }

    /**
     * 导出【网络】列表
     */
    @Log(title = "【导出】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysLine sysLine)
    {
        /*List<SysLine> list = ogLineFaultService.selectOgLineFaultList(sysLine);
        ExcelUtil<SysLine> util = new ExcelUtil<SysLine>(SysLine.class);
        return util.exportExcel(list, "fault");*/
        String isCurrentPage = (String) sysLine.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SysLine> list = ogLineFaultService.selectOgLineFaultList(sysLine);
            ExcelUtil<SysLine> util = new ExcelUtil<SysLine>(SysLine.class);
            return util.exportExcel(list, "网络条线故障");
        } else if ("all".equals(isCurrentPage)) {
            List<SysLine> list = ogLineFaultService.selectOgLineFaultList(sysLine);
            ExcelUtil<SysLine> util = new ExcelUtil<SysLine>(SysLine.class);
            return util.exportExcel(list, "网络条线故障");
        }
        return null;
    }
}