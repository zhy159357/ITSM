package com.ruoyi.form.controller;

import java.util.List;

import com.ruoyi.form.util.BizChmExportUtil;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.ChmBasedata;
import com.ruoyi.form.domain.ChmParavalue;
import com.ruoyi.form.enums.ChmPJEnum;
import com.ruoyi.form.enums.ChmSFnum;
import com.ruoyi.form.enums.ChmfaultTypenum;
import com.ruoyi.form.service.IChmBasedataService;
import com.ruoyi.form.service.IChmParavalueService;
import com.ruoyi.system.service.IOgPersonService;
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
import com.ruoyi.form.domain.DesignBizChm;
import com.ruoyi.form.service.IDesignBizChmService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 硬件保障Controller
 * 
 * @author zx
 * @date 2022-11-26
 */
@Controller
@RequestMapping("/form/chm")
public class DesignBizChmController extends BaseController
{
    private String prefix = "form/chm";
    @Autowired
    private IDesignBizChmService designBizChmService;
    @Autowired
    private IChmBasedataService iChmBasedataService;
    @Autowired
    private IChmParavalueService iChmParavalueService;
    @Autowired
    private  IOgPersonService ogPersonService;
    @GetMapping()
    public String chm()
    {
        return prefix + "/chm";
    }

    /**
     * 查询硬件保障列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DesignBizChm designBizChm)
    {
        startPage();
        List<DesignBizChm> list = designBizChmService.selectDesignBizChmList(designBizChm);
        //解析特殊字段
        list.forEach(cell->{
            //上报部门
            String reportDepartment=cell.getReportDepartment();
            ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(reportDepartment));
            cell.setReportDepartment(chmBasedata.getOrgName());
            //123 级
            ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(cell.getEquipmentType()));
            cell.setEquipmentType(chmParavalue.getName());

            ChmParavalue chmParavalue1=iChmParavalueService.selectChmParavalueById(Long.valueOf(cell.getEquipmentName()));
            cell.setEquipmentName(chmParavalue1.getName());

            ChmParavalue chmParavalue2=iChmParavalueService.selectChmParavalueById(Long.valueOf(cell.getEquipmentModel()));
            cell.setEquipmentModel(chmParavalue2.getName());
            String createBy=cell.getCreatedBy();
            OgPerson ogPerson=ogPersonService.selectOgPersonById(createBy);
            if(ogPerson!=null){
                cell.setCreatedBy(ogPerson.getpName());
            }else {
                cell.setCreatedBy("未知");
            }
            String chmType=cell.getChmType();
            cell.setChmType("1".equalsIgnoreCase(chmType)?"硬件":"软件");
        });
        return getDataTable(list);
    }

    /**
     * 导出硬件保障列表
     */
    @Log(title = "硬件保障", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DesignBizChm designBizChm)
    {
        String flag=designBizChm.getParams().get("currentPage").toString();
        if("currentPage".equals(flag)){
            startPage();
        }
        List<DesignBizChm> list = designBizChmService.selectDesignBizChmList(designBizChm);
        BizChmExportUtil exportUtil=new BizChmExportUtil();
        exportUtil.multithreadedListSegmentation(list);
        ExcelUtil<DesignBizChm> util = new ExcelUtil<DesignBizChm>(DesignBizChm.class);
        return util.exportExcel(list, "chm");
    }

    /**
     * 新增硬件保障
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存硬件保障
     */
    @Log(title = "硬件保障", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DesignBizChm designBizChm)
    {
        return toAjax(designBizChmService.insertDesignBizChm(designBizChm));
    }

    /**
     * 硬件报障详情
     */
    @GetMapping("/view/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        DesignBizChm designBizChm = designBizChmService.selectDesignBizChmById(id);
        //上报部门
        String reportDepartment=designBizChm.getReportDepartment();
        ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(reportDepartment));
        designBizChm.setReportDepartment(chmBasedata.getOrgName());
        //123 级
        ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(designBizChm.getEquipmentType()));
        if(chmParavalue!=null){
            designBizChm.setEquipmentType(chmParavalue.getName());
        }

        ChmParavalue chmParavalue11=iChmParavalueService.selectChmParavalueById(Long.valueOf(StringUtils.isEmpty(designBizChm.getEquipmentTypeB())?"0":designBizChm.getEquipmentTypeB()));
        if(chmParavalue11!=null){
            designBizChm.setEquipmentTypeB(chmParavalue11.getName());
        }

        ChmParavalue chmParavalue1=iChmParavalueService.selectChmParavalueById(Long.valueOf(designBizChm.getEquipmentName()));
        if(chmParavalue1!=null){
            designBizChm.setEquipmentName(chmParavalue1.getName());
        }
        ChmParavalue chmParavalue12=iChmParavalueService.selectChmParavalueById(Long.valueOf(StringUtils.isEmpty(designBizChm.getEquipmentNameB())?"0":designBizChm.getEquipmentNameB()));
        if(chmParavalue12!=null){
            designBizChm.setEquipmentNameB(chmParavalue12.getName());
        }

        ChmParavalue chmParavalue2=iChmParavalueService.selectChmParavalueById(Long.valueOf(designBizChm.getEquipmentModel()));
        if(chmParavalue2!=null){
            designBizChm.setEquipmentModel(chmParavalue2.getName());
        }
        ChmParavalue chmParavalue21=iChmParavalueService.selectChmParavalueById(Long.valueOf(StringUtils.isEmpty(designBizChm.getEquipmentModelB())?"0":designBizChm.getEquipmentModelB()));
        if(chmParavalue21!=null){
            designBizChm.setEquipmentModelB(chmParavalue21.getName());
        }
        String createBy=designBizChm.getCreatedBy();
        OgPerson ogPerson=ogPersonService.selectOgPersonById(createBy);
        if(ogPerson!=null){
            designBizChm.setCreatedBy(ogPerson.getpName());
        }else {
            designBizChm.setCreatedBy("未知");
        }
        String scope=designBizChm.getScopeInfluence();
        designBizChm.setScopeInfluence(ChmSFnum.getInfo(scope));
        String faultType=designBizChm.getFaultType();
        designBizChm.setFaultType(ChmfaultTypenum.getInfo(faultType));
        String chmType=designBizChm.getChmType();
        designBizChm.setChmType("1".equalsIgnoreCase(chmType)?"硬件":"软件");
        String serviceEvaluation=designBizChm.getServiceEvaluation();
        designBizChm.setServiceEvaluation(ChmPJEnum.getInfo(serviceEvaluation));
        String telephoneFollow=designBizChm.getTelephoneFollow();
        if("0".equals(telephoneFollow)){
            designBizChm.setTelephoneFollow("否");
        }
        if("1".equals(telephoneFollow)){
            designBizChm.setTelephoneFollow("是");
        }
        String solution =designBizChm.getSolution();
        if("0".equals(solution)){
            designBizChm.setSolution("现场");
        }
        if("1".equals(solution)){
            designBizChm.setSolution("远程");
        }
        mmap.put("designBizChm", designBizChm);
        return prefix + "/edit";
    }

    /**
     * 修改保存硬件保障
     */
    @Log(title = "硬件保障", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DesignBizChm designBizChm)
    {
        return toAjax(designBizChmService.updateDesignBizChm(designBizChm));
    }

    /**
     * 删除硬件保障
     */
    @Log(title = "硬件保障", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(designBizChmService.deleteDesignBizChmByIds(ids));
    }
}
