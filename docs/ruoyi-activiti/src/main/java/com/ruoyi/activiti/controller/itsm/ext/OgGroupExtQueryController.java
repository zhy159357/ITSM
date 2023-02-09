package com.ruoyi.activiti.controller.itsm.ext;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.activiti.domain.TelSupportPeropleExtension;
import com.ruoyi.activiti.domain.TelSystemSupportgroupNumber;
import com.ruoyi.activiti.service.IOgGroupExtQueryService;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
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
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-05-19
 */
@Controller
@RequestMapping("/ext/group")
public class OgGroupExtQueryController extends BaseController
{
    private String prefix = "ext/group";

    @Autowired
    private IOgGroupExtQueryService ogGroupExtQueryService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @GetMapping()
    public String query(ModelMap mmap)
    {
        //判断当前登陆人是不是管理员
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        OgPerson person = iOgPersonService.selectRoleByIdTwo(userId);
        //List<TelSystemSupportgroupNumber> list=new ArrayList<TelSystemSupportgroupNumber>();
        if(person==null){// 非管理员
            mmap.put("showFlag","0");
        }else {
            mmap.put("showFlag","1");
        }
        return prefix + "/group";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        startPage();
        List<TelSystemSupportgroupNumber> list = ogGroupExtQueryService.selectOgGroupExtQueryList(telSystemSupportgroupNumber);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        List<TelSystemSupportgroupNumber> list = ogGroupExtQueryService.selectOgGroupExtQueryList(telSystemSupportgroupNumber);
        ExcelUtil<TelSystemSupportgroupNumber> util = new ExcelUtil<TelSystemSupportgroupNumber>(TelSystemSupportgroupNumber.class);
        return util.exportExcel(list, "query");
    }

    //选择公司
    @PostMapping("/listTwo")
    @ResponseBody
    public TableDataInfo listTwo(OgSys sys) {
        startPage();
        List<OgSys> list = applicationManagerService.selectOgSysList(sys);
        return getDataTable(list);
    }

    /**
     * 公司名称和系统名称页面
     * @param orgId
     * @param modelMap
     * @return
     */
    @GetMapping("/selectOneApplication/{orgId}")
    public String selectApplication(@PathVariable("orgId") String orgId,ModelMap modelMap){
        modelMap.put("orgId",orgId);
        return prefix + "/selectOneApplication";
    }

    //创建机构选择页面
    @GetMapping("/selectogOrg")
    public String selectogOrg() {
        return prefix + "/selectOgorg";
    }
    /*@GetMapping("/selectogOrg")
    public String selectogOrg(ModelMap mmap) {
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String pId = ogUser.getpId();
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        mmap.put("orgId",ogPerson.getOrgId());
        return prefix + "/selectOgorg";
    }*/

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        telSystemSupportgroupNumber.setId(UUID.getUUIDStr());
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telSystemSupportgroupNumber.setCreateTime(nowTime);
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String id = ogUser.getpId();
        //当前登录人id
        telSystemSupportgroupNumber.setCreatePerson(id);
        return toAjax(ogGroupExtQueryService.insertOgGroupExtQuery(telSystemSupportgroupNumber));
    }

    /**
     * 多选系统
     */
    @RequestMapping("/getAllSys")
    @ResponseBody
    public AjaxResult getAllSys(){
        AjaxResult ajaxResult = new AjaxResult();
        List<OgSys> ogSys = applicationManagerService.selectOgSysList(null);
        List<OgSys> list = ogSys.stream().filter(sys -> "1".equals(sys.getInvalidationMark())).collect(Collectors.toList());
        ajaxResult.put("data",list);
        return ajaxResult;
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        TelSystemSupportgroupNumber telSystemSupportgroupNumber = ogGroupExtQueryService.selectOgGroupExtQueryById(id);
        mmap.put("ogGroupExtQuery", telSystemSupportgroupNumber);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        //修改时间回显
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telSystemSupportgroupNumber.setUpdateTime(nowTime);
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String id = ogUser.getpId();
        //当前登录人id
        telSystemSupportgroupNumber.setUpdatePerson(id);
        return toAjax(ogGroupExtQueryService.updateOgGroupExtQuery(telSystemSupportgroupNumber));
    }

    //机构
    @Autowired
    private ISysDeptService deptService;

    /**
     * 查询机构里的公司
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org)
    {
        startPage();
        List<OgOrg> list = deptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(ogGroupExtQueryService.deleteOgGroupExtQueryByIds(ids));
    }
}
