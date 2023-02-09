package com.ruoyi.activiti.controller.itsm.sion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.ruoyi.activiti.domain.SysDeploymentVersion;
import com.ruoyi.activiti.service.ISysDeploymentVersionService;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.aspectj.weaver.ast.Var;
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
 * 【版本部署记录】Controller
 * 
 * @author ruoyi
 * @date 2021-11-18
 */
@Controller
@RequestMapping("sion/newly")
public class SysDeploymentVersionController extends BaseController
{
    private String prefix = "sion/newly";

    @Autowired
    private ISysDeploymentVersionService sysDeploymentVersionService;

    @GetMapping()
    public String version()
    {
        return prefix + "/sion";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDeploymentVersion sysDeploymentVersion)
    {
        startPage();
        sysDeploymentVersion.setDeploymentTime(handleTimeYYYYMMDDHHMMSS(sysDeploymentVersion.getDeploymentTime()));
        List<SysDeploymentVersion> list = sysDeploymentVersionService.selectSysDeploymentVersionList(sysDeploymentVersion);
        return getDataTable(list);
    }

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
    public AjaxResult addSave(SysDeploymentVersion sysDeploymentVersion)
    {
        String versionNo = sysDeploymentVersion.getVersionNo();
        SysDeploymentVersion sysDeploymentVersionNo = new SysDeploymentVersion();
        sysDeploymentVersionNo.setVersionNo(versionNo);
        List<SysDeploymentVersion> list = sysDeploymentVersionService.selectSysDeploymentVersionListNew(sysDeploymentVersionNo);
        if(list.size() !=0){
            return AjaxResult.error("版本号已经存在，请重新输入版本号！");
        }
        sysDeploymentVersion.setDvId(UUID.getUUIDStr());
        //获取当前登录人id
        OgUser ogUser = ShiroUtils.getOgUser();
        String username = ogUser.getUsername();
        sysDeploymentVersion.setCreaterId(username);
        sysDeploymentVersion.setDeploymentTime(handleTimeYYYYMMDDHHMMSS(sysDeploymentVersion.getDeploymentTime()));
        sysDeploymentVersionService.insertSysDeploymentVersion(sysDeploymentVersion);
//        list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysDeploymentVersion ::getVersionNo))), ArrayList::new))
//        .forEach(p->{
//            sysDeploymentVersionService.insertSysDeploymentVersion(p);
//        });
        return AjaxResult.success("添加成功！");
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{dvId}")
    public String edit(@PathVariable("dvId") String dvId, ModelMap mmap)
    {
        SysDeploymentVersion sysDeploymentVersion = sysDeploymentVersionService.selectSysDeploymentVersionById(dvId);
        mmap.put("sysDeploymentVersion", sysDeploymentVersion);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDeploymentVersion sysDeploymentVersion)
    {
        /*String versionNo = sysDeploymentVersion.getVersionNo();
        SysDeploymentVersion sysDeploymentVersionNo = new SysDeploymentVersion();
        sysDeploymentVersionNo.setVersionNo(versionNo);
        List<SysDeploymentVersion> list = sysDeploymentVersionService.selectSysDeploymentVersionListNew(sysDeploymentVersionNo);
        if(list.size() !=0){
            return AjaxResult.error("版本号已经存在，请重新输入版本号！");
        }*/
        return toAjax(sysDeploymentVersionService.updateSysDeploymentVersion(sysDeploymentVersion));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/detail/{dvId}")
    public String detail(@PathVariable("dvId") String dvId, ModelMap mmap)
    {
        SysDeploymentVersion sysDeploymentVersion = sysDeploymentVersionService.selectSysDeploymentVersionById(dvId);
        mmap.put("sysDeploymentVersion", sysDeploymentVersion);
        return prefix + "/detail";
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        SysDeploymentVersion sysDeploymentVersion = sysDeploymentVersionService.selectSysDeploymentVersionById(ids);
        String versionShow = sysDeploymentVersion.getVersionShow();
        if (versionShow.startsWith("2")){
            return toAjax(sysDeploymentVersionService.deleteSysDeploymentVersionByIds(ids));
        }
        //return toAjax(sysDeploymentVersionService.deleteSysDeploymentVersionByIds(ids));
        return AjaxResult.error("只有展示为否才可以删除！");
    }
}
