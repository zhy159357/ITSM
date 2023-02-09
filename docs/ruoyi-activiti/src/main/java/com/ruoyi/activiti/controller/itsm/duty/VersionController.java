package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.service.IDutyTypeinfoService;
import com.ruoyi.activiti.service.IDutyVersionService;
import com.ruoyi.activiti.service.IDutyViewService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TypeinfoNumber;
import com.ruoyi.framework.shiro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 版本信息
 * @author dayong_sun
 */
@Controller
@RequestMapping("duty/version")
public class VersionController extends BaseController
{
    private String prefix = "duty/version";

    @Autowired
    private IDutyVersionService dutyVersionService;
    @Autowired
    private IDutyTypeinfoService dutyTypeinfoService;
    @Autowired
    private IDutyViewService dutyViewService;
    @Value("${cntxtag.enabled}")
    private String cntxtag;

    @GetMapping()
    public String version()
    {
        return prefix + "/version";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DutyVersion dutyVersion)
    {
        startPage();
        List<DutyVersion> list = dutyVersionService.selectVersionList(dutyVersion);
        return getDataTable(list);
    }

    /**
     * 新增版本
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存版本
     */
    @Log(title = "版本管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated DutyVersion dutyVersion)
    {
        if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(dutyVersionService.checkVersionNoUnique(dutyVersion)))
        {
            return error("新增版本编号" + dutyVersion.getVersionNo() + "已存在");
        }
        return toAjax(dutyVersionService.insertVersion(dutyVersion));

    }

    /**
     * 修改版本
     */
    @GetMapping("/edit/{versionId}")
    public String edit(@PathVariable("versionId") String versionId, ModelMap mmap)
    {
        DutyVersion dutyVersion = dutyVersionService.selectVersionById(versionId);
        mmap.put("version", dutyVersion);
        return prefix + "/edit";
    }

    /**
     * 修改保存版本
     */
    @Log(title = "版本管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated DutyVersion dutyVersion)
    {
        return toAjax(dutyVersionService.updateVersion(dutyVersion));
    }

    @Log(title = "版本管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return dutyVersionService.deleteVersionByIds(ids);
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 版本代码
     */
    @PostMapping("/checkVersionNoUnique")
    @ResponseBody
    public String checkVersionNoUnique(DutyVersion dutyVersion)
    {
        return dutyVersionService.checkVersionNoUnique(dutyVersion);
    }

    /**
     * 验证月份
     */
    @PostMapping("/checkDutyDateUnique")
    @ResponseBody
    public String checkDutyDateUnique(DutyVersion dutyVersion)
    {
        return dutyVersionService.checkDutyDateUnique(dutyVersion);
    }

    /**
     * 通过版本ID查询类别
     */
    @GetMapping("/typeinfo/{versionId}")
    public String authTypeinfo(@PathVariable("versionId") String versionId, ModelMap mmap)
    {
        mmap.put("cntxtag", cntxtag);
        mmap.put("versionId", versionId);
        return prefix + "/typeinfo";
    }

    /**
     * 查询类别数据
     */
    @PostMapping("/typeinfo")
    @ResponseBody
    public List<DutyVersion> typeinfo(DutyVersion dutyVersion) {
        List<DutyVersion> list = dutyVersionService.selectTypeinfoByVersionId(dutyVersion);
        return list;
    }

    /**
     * 修改版本下的类别
     */
    @GetMapping("/typeEdit/{versionTypeinfoId}")
    public String typeEdit(@PathVariable("versionTypeinfoId") String versionTypeinfoId, ModelMap mmap)
    {
        DutyVersion dutyVersion = dutyVersionService.selectVersionTypeinfoById(versionTypeinfoId);
        List<OgPerson> userlist = dutyTypeinfoService.selectUserList(VersionStatusConstants.ZBJS_NO);
        List numberList = TypeinfoNumber.getList();

        mmap.put("userlist", userlist);
        mmap.put("version", dutyVersion);
        mmap.put("numberList", numberList);
        return prefix + "/typeEdit";
    }

    /**
     * 修改保存类别
     */
    @Log(title = "版本管理", businessType = BusinessType.UPDATE)
    @PostMapping("/typeEdit")
    @ResponseBody
    public AjaxResult typeEditSave(@Validated DutyVersion dutyVersion)
    {
        if (dutyVersion.getParentId().equals(dutyVersion.getTypeinfoId()))
        {
            return error("修改类别'" + dutyVersion.getTypeName() + "'失败，上级类别不能是自己");
        }
        return toAjax(dutyVersionService.updateVersionTypeinfo(dutyVersion));
    }

    /**
     * 版本克隆
     */
    @Log(title = "版本克隆", businessType = BusinessType.DELETE)
    @PostMapping("/clone")
    @ResponseBody
    public AjaxResult clone(String versionId)
    {
        try
        {
            return toAjax(dutyVersionService.versionClone(versionId));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    @Log(title = "版本管理", businessType = BusinessType.DELETE)
    @GetMapping("/deleteTypeinfo/{versionTypeinfoId}")
    @ResponseBody
    public AjaxResult deleteTypeinfo(@PathVariable("versionTypeinfoId") String versionTypeinfoId)
    {
        try
        {
            return dutyVersionService.deleteVersionTypeinfos(versionTypeinfoId);
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 选择类别树
     */
    @GetMapping(value = {"/selectTypeinfoTree/{versionTypeinfoId}"})
    public String selectTypeinfoTree(@Validated DutyVersion dutyVersion, ModelMap mmap)
    {
        mmap.put("version", dutyVersionService.selectTypeinfoById(dutyVersion));
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping(value = {"/treeData/{versionId}"})
    @ResponseBody
    public List<Ztree> treeData(@Validated DutyVersion dutyVersion)
    {
        List<Ztree> ztrees = dutyVersionService.selectTypeinfoTree(dutyVersion);
        return ztrees;
    }

    @GetMapping("/preview/{versionId}")
    public String scheduling(@PathVariable("versionId") String versionId,ModelMap mmap) {
        mmap.put("versionId", versionId);
        return prefix + "/preview";
    }

    /**
     * 查询值班视图列表
     */
    @PostMapping("/exclehead")
    @ResponseBody
    public Map<String,Object> exclehead(DutyVersion dutyVersion) {
        return dutyVersionService.selectPreview(dutyVersion);
    }

}