package com.ruoyi.web.controller.system;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.ruoyi.system.domain.OgDeputyCfg;
import com.ruoyi.system.service.IOgDeputyCfgService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 代理人配置Controller
 *
 * @author admin
 * @date 2022-12-12
 */
@Controller
@RequestMapping("/system/putycfg")
public class OgDeputyCfgController extends BaseController {
    private String prefix = "system/putycfg";

    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IOgDeputyCfgService ogDeputyCfgService;
    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("system:putycfg:view")
    @GetMapping()
    public String putycfg() {
        return prefix + "/putycfg";
    }

    /**
     * 查询代理人配置列表
     */
    @RequiresPermissions("system:putycfg:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgDeputyCfg ogDeputyCfg) {
        startPage();
        List<OgDeputyCfg> list = ogDeputyCfgService.selectOgDeputyCfgList(ogDeputyCfg);
        return getDataTable(list);
    }

    /**
     * 导出代理人配置列表
     */
    @RequiresPermissions("system:putycfg:export")
    @Log(title = "代理人配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgDeputyCfg ogDeputyCfg) {
        List<OgDeputyCfg> list = ogDeputyCfgService.selectOgDeputyCfgList(ogDeputyCfg);
        ExcelUtil<OgDeputyCfg> util = new ExcelUtil<OgDeputyCfg>(OgDeputyCfg.class);
        return util.exportExcel(list, "putycfg");
    }

    /**
     * 新增代理人配置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存代理人配置
     */
    @RequiresPermissions("system:putycfg:add")
    @Log(title = "代理人配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgDeputyCfg ogDeputyCfg) {
        String pId = ShiroUtils.getOgUser().getpId();
        ogDeputyCfg.setCreateBy(pId);
        OgDeputyCfg match = new OgDeputyCfg();
        match.setDirector(ogDeputyCfg.getDirector());
        match.setCfgType(ogDeputyCfg.getCfgType());
        List<OgDeputyCfg> list = ogDeputyCfgService.isOgDeputyCfgList(match);
        if (!list.isEmpty()) {
            return AjaxResult.error("配置重复！");
        } else {
            return toAjax(ogDeputyCfgService.insertOgDeputyCfg(ogDeputyCfg));
        }
    }

    /**
     * 修改代理人配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        OgDeputyCfg ogDeputyCfg = ogDeputyCfgService.selectOgDeputyCfgById(id);
        mmap.put("ogDeputyCfg", ogDeputyCfg);
        return prefix + "/edit";
    }

    /**
     * 修改保存代理人配置
     */
    @RequiresPermissions("system:putycfg:edit")
    @Log(title = "代理人配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgDeputyCfg ogDeputyCfg) {
        String pId = ShiroUtils.getOgUser().getpId();
        ogDeputyCfg.setUpdateBy(pId);
        OgDeputyCfg match = new OgDeputyCfg();
        match.setId(ogDeputyCfg.getId());
        List<OgDeputyCfg> list = ogDeputyCfgService.isOgDeputyCfgList(match);
        if (list.isEmpty()) {
            return AjaxResult.error("id不存在！");
        }
        OgDeputyCfg old = list.get(0);
        String oldDir = old.getDirectorId();
        Long oldType = old.getCfgType();
        String director = ogDeputyCfg.getDirector();
        Long type = ogDeputyCfg.getCfgType();
        if (director != null && !"".equals(director) && !oldDir.equals(director)) {
            return AjaxResult.warn("负责人不可修改");
        }
        if (type != null && !oldType.equals(type)) {
            return AjaxResult.warn("类型不可修改");
        }
        return toAjax(ogDeputyCfgService.updateOgDeputyCfg(ogDeputyCfg));
    }

    /**
     * 删除代理人配置
     */
    @RequiresPermissions("system:putycfg:remove")
    @Log(title = "代理人配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(ogDeputyCfgService.deleteOgDeputyCfgByIds(ids));
    }

    @GetMapping("/selectUser")
    public String selectUser() {
        return prefix + "/selectUser";
    }

    /**
     * 账号管理加载人员信息展示    查询当前登录人本级及下辖机构下的所有人员
     *
     * @param ogPerson
     * @return
     */
    @PostMapping("/accountUserList")
    @ResponseBody
    public TableDataInfo accountUserList(OgPerson ogPerson) {
        //获取当前登陆人的机构信息
        startPage();
        List<OgPerson> list = ogPersonService.selectList(ogPerson);
        return getDataTable(list);


    }
}
