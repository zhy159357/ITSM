package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.core.domain.entity.SysInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.DeleteRea;
import com.ruoyi.common.enums.InfoState;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.ISysInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信息管理信息
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/addlist")
public class SysAddListController extends BaseController {
    private String prefix = "system/addlist";

    @Autowired
    private ISysInfoService infoService;

    @GetMapping()
    public String info(ModelMap mmap)
    {
        List stateList = InfoState.getList();
        List deleList = DeleteRea.getList();
        mmap.addAttribute("stateList",stateList);
        mmap.addAttribute("deleList",deleList);
        return prefix + "/addlist";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysInfo info) {
        startPage();
        List<SysInfo> list = infoService.selectInfoList(info);
        return getDataTable(list);
    }
    /**
     * 信息制度管理审核
     */
    @GetMapping("/audit")
    public String audit( ModelMap mmap) {
        List deleList = DeleteRea.getList();
        mmap.addAttribute("deleList",deleList);
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        return prefix + "/audit";
    }

    /**
     * 审核按钮点击跳转
     */
    @GetMapping("/auditing/{regime_info_id}")
    public String audit(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap) {
        mmap.put("info", infoService.selectInfoById(regime_info_id));
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        return prefix + "/auditing";
    }

    /**
     * 信息制度管理新增
     */
    @GetMapping("/add")
    public String add( ModelMap mmap) {
        List<OgPerson> checkerList = infoService.selectCheckerList(new OgPerson());
        mmap.addAttribute("checkerList",checkerList);
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        return prefix + "/add";
    }
    /**
     * 新增保存
     */
    @Log(title = "信息制度管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysInfo info) {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(infoService.checkInfoNameUnique(info))) {
            return error("信息制度'" + info + "'失败，名称已存在");
        }
        info.setCommit_id(ShiroUtils.getOgUser().getUserId());
        return toAjax(infoService.insertInfo(info));

    }
    /**
     * 修改
     */
    @GetMapping("/edit/{regime_info_id}")
    public String edit(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap) {
        List<OgPerson> checkerList = infoService.selectCheckerList(new OgPerson());
        mmap.put("info", infoService.selectInfoById(regime_info_id));
        mmap.addAttribute("checkerList",checkerList);
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param info
     * @return
     */
    @Log(title = "信息制度管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysInfo info)
    {
        info.setCommit_id(ShiroUtils.getOgUser().getUserId());
        return toAjax(infoService.updateInfo(info));
    }

//    /**
//     * 删除
//     * @param ids
//     * @return
//     */
//    @Log(title = "信息制度管理", businessType = BusType.DELETE)
//    @PostMapping("/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids) {
//        try {
//            return toAjax(infoService.deleteInfoByIds(ids));
//        } catch (Exception e) {
//            return error(e.getMessage());
//        }
//    }
    //删除
    @GetMapping("/remove/{regime_info_id}")
    public String remove(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap) {
        List<OgPerson> checkerList = infoService.selectCheckerList(new OgPerson());
        mmap.put("info", infoService.selectInfoById(regime_info_id));
        mmap.addAttribute("checkerList",checkerList);
        mmap.addAttribute("regime_info_id",regime_info_id);
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        //infoService.deleteInfoByIds(ids);
        return prefix + "/remove";
    }

    /**
     *  删除保存
     */
    @Log(title = "信息制度管理", businessType = BusinessType.UPDATE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult deleteSave(@Validated SysInfo info)
    {
        return toAjax(infoService.deleteInfo(info));
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = infoService.selectFolderTree(new SysFolder());
        return ztrees;
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkInfoNameUnique")
    @ResponseBody
    public String checkInfoNameUnique(SysInfo info) {
        return infoService.checkInfoNameUnique(info);
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{regime_info_id}")
    public String detail(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap)
    {
        List<OgPerson> checkerList = infoService.selectCheckerList(new OgPerson());
        mmap.put("info", infoService.selectInfoById(regime_info_id));
        mmap.addAttribute("checkerList",checkerList);
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        return prefix + "/detail";
    }
    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }



}