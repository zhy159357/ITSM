package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.service.IDutySchedulingService;
import com.ruoyi.activiti.service.IDutySubstituteRemarkService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutyPerson;
import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.domain.entity.DutySubstituteRemark;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SubstituteStatus;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.activiti.constants.VersionStatusConstants.QGZX_ORGID;


@Controller
@RequestMapping("duty/substituteRemark")
public class SubstituteRemarkController extends BaseController {

    private String prefix = "duty/substituteRemark";

    @Autowired
    private IDutySubstituteRemarkService dutySubstituteRemarkService;

    @Autowired
    private IDutySchedulingService dutySchedulingService;


    @GetMapping("/remark")
    public String apply(ModelMap mmap) {
        mmap.put("statusList", SubstituteStatus.getList());
        return prefix + "/remark";
    }

    /**
     * 查询替班备注列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo alist(DutySubstituteRemark dutySubstituteRemark) {
        startPage();
        List<DutySubstituteRemark> list = dutySubstituteRemarkService.selectSubstituteRemarkList(dutySubstituteRemark);
        return getDataTable(list);
    }

    /**
     * 新增替班备注信息-页面
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        DutySubstitute dutySubstitute = new DutySubstitute();
        dutySubstitute.setPid(ShiroUtils.getOgUser().getpId());
        List<DutySubstitute> list = dutySubstituteRemarkService.selectSubstituteList(dutySubstitute);
        List<DutyPerson> userlist = dutySchedulingService.selectOgPersonList(QGZX_ORGID);

        mmap.put("remarkList", list);
        mmap.put("userlist", userlist);

        return prefix + "/add";
    }


    /**
     * 根据值班时间查询替班信息
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/selectRemarkByDutyDate")
    @ResponseBody
    public DutySubstitute selectRemarkByDutyDate(String param) {
        String dutyDate = param.substring(0,10);
        String typeNo = ClearBracket(param);

        DutySubstitute substituteRemark = new DutySubstitute();
        substituteRemark.setDutyDate(dutyDate);
        substituteRemark.setTypeNo(typeNo);

        DutySubstitute dutySubstituteRemark  = dutySubstituteRemarkService.selectRemarkByDutyDate(substituteRemark);

        return dutySubstituteRemark;
    }


    /**
     * 加载模板
     * @return
     */
    @PostMapping("/loadtemplate")
    @ResponseBody
    public DutySubstituteRemark loadsavetemplate()
    {
        return dutySubstituteRemarkService.selectTemplateById("TEMPLATE0001");
    }

    private  String ClearBracket(String gSQL){
        String quStr=gSQL.substring(gSQL.indexOf("(")+1,gSQL.indexOf(")"));
        if(StringUtils.isEmpty(quStr)){
            return "";
        }
        return quStr;
    }


    /**
     * 根据值班时间和值班类别检查是否存在
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/addCheckSave")
    @ResponseBody
    public String addCheckSave(DutySubstituteRemark dutySubstituteRemark) {
        String s = dutySubstituteRemarkService.addCheckSave(dutySubstituteRemark);
        return s;
    }

    /**
     * 新增保存
     */
    @Log(title = "参数列别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DutySubstituteRemark dutySubstituteRemark) {
        return toAjax(dutySubstituteRemarkService.insertDutySubstituteRemark(dutySubstituteRemark));
    }

    /**
     * 修改替班备注-页面
     */
    @GetMapping("/edit/{substituteRemarkId}")
    public String edit(@PathVariable("substituteRemarkId") String substituteRemarkId, ModelMap mmap) {
        DutySubstituteRemark dutySubstituteRemark = dutySubstituteRemarkService.selectDutySubstituteRemarkById(substituteRemarkId);
        mmap.put("dutySubstituteRemark", dutySubstituteRemark);

        return prefix + "/edit";
    }

    /**
     * 修改保存参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DutySubstituteRemark dutySubstituteRemark) {
        return toAjax(dutySubstituteRemarkService.updateDutySubstituteRemark(dutySubstituteRemark));
    }

    /**
     * 查询当前登录人是否是创建人
     */
    @PostMapping("/selectCreateIdById")
    @ResponseBody
    public int selectCreateIdById(@Validated String id) {
        return dutySubstituteRemarkService.selectCreateIdById(id);
    }

    /**
     * 删除替班备注
     */
//    @Log(title = "参数替班备注", businessType = BusinessType.DELETE)
//    @PostMapping("/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids) {
//        return dutySubstituteRemarkService.deleteDutySubstituteRemarkByIds(ids);
//    }
}
