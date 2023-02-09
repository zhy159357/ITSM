package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.core.domain.entity.SysInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.ISysInfoService;
import com.ruoyi.system.service.ISysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 信息管理信息
 *
 * @author Mr.Xy
 */
@Controller
public class SysZtreeController extends BaseController {
    private String prefix = "system/ztree";

    @Autowired
    private ISysTreeService iSysTreeService;
    @Autowired
    private ISysInfoService infoService;

    //新增
    @RequestMapping("/system/ztreeAdd")
    public String add(HttpServletRequest req, ModelMap mmap) {
        String parent_ = req.getParameter("treeName");
        String id_ = req.getParameter("id");
        if (!"-1".equals(id_)) {
            mmap.put("ztreeAdd", iSysTreeService.selectTreeById(id_));
        } else {
            mmap.put("ztreeAdd", new SysFolder());
        }
        mmap.put("parent_", parent_);
        mmap.put("id_", id_);
        return prefix + "/ztreeAdd";
    }

    /**
     * 新增保存
     */
    @Log(title = "机构树", businessType = BusinessType.INSERT)
    @PostMapping("/system/ztree/ztreeAdd")
    @ResponseBody
    public AjaxResult addSave(@Validated SysFolder sysFolder) {
        if ("-1".equals(sysFolder.getId_()) || "null".equals(sysFolder.getId_()) || null == sysFolder.getId_()) {
            sysFolder.setPath_(sysFolder.getName_());
        }
        OgUser ogUser = ShiroUtils.getOgUser();
        sysFolder.setCreate_user_(ogUser.getUserId());
        return AjaxResult.success("操作成功", iSysTreeService.insertTree(sysFolder));

    }

    //  修改
    @RequestMapping("/system/ztree")
    public String edit(HttpServletRequest req, ModelMap mmap) {
        String parent_ = req.getParameter("treeName");
        String id_ = req.getParameter("id");
        String parent=null;
        SysFolder spf =iSysTreeService.selectTreeById(id_);
        if (spf.getParent_()==null||spf.getParent_()==""){
            parent="";
        }else{
            parent=iSysTreeService.selectParentName(spf.getParent_());
        }
        mmap.put("ztree", iSysTreeService.selectTreeById(id_));
        mmap.put("parent_", parent);
        mmap.put("id_", id_);
        return prefix + "/ztree";
    }
    /**
     * 修改保存
     *
     * @param sysFolder
     * @return
     */
    @Log(title = "机构树", businessType = BusinessType.UPDATE)
    @PostMapping("/system/ztree/ztree")
    @ResponseBody
    public AjaxResult editSave(@Validated SysFolder sysFolder) {
        return toAjax(iSysTreeService.updateTree(sysFolder));
    }

    /**
     * 删除树节点
     */
    @PostMapping("/system/ztree/treeDelete")
    @ResponseBody
    public AjaxResult deleteTree(String id_, SysInfo info) {
        //查询是否自己个人所创建
        SysFolder sysFolder = new SysFolder();
        info.setFolder_(id_);
        OgUser ogUser = ShiroUtils.getOgUser();
        sysFolder.setCreate_user_(ogUser.getUserId());
        sysFolder.setId_(id_);
        int person = iSysTreeService.selectSelfTreeById(sysFolder);
        //查询当前节点有没有数据
        if (person > 0) {
            //查询有没有子节点
            int leaf = iSysTreeService.selectTreeLeafById(id_);
            //查询当前节点有没有数据
            List<SysInfo> sy = infoService.selectIFList(info);
            if (sy.size()>0) {
                return AjaxResult.success("该节点下存在子任务，请先删除子任务", 1);
            } else if(leaf > 0) {
                return AjaxResult.success("当前节点存在数据，不能删除", 2);
            }else{
                return toAjax(iSysTreeService.deleteTree(id_));
            }
        } else {
            return AjaxResult.success("您无权进行此记录的删除操作", 0);
        }
    }
}