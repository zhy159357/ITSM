package com.ruoyi.activiti.controller.itsm.lxbg;


import com.ruoyi.activiti.service.FolderService;
import com.ruoyi.activiti.web.esb.utils.URLCodeUtils;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *左侧目录树
 */
@Controller
@RequestMapping("/lxbg/folder")
public class FolderController extends BaseController {


    @Autowired
    private FolderService folderService;

    @Autowired
    private IOgPersonService ogPersonService;


    private String prefix = "lxbg/folder";

    /**
     * 加载参数设置列表树
     * @return
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZtreeStr> treeData()
    {
        List<ZtreeStr> ztrees = folderService.selectFolderTree(new SmBizFolder());
        return ztrees;
    }


    /**
     * 转到新增页面
     * @param mmap
     * @return
     */
    @RequestMapping("/lxbg/ztreeAdd/{id}")
    public String add(@PathVariable("id") String id_, ModelMap mmap) {
        if (!"-1".equals(id_)) {
            mmap.put("ztreeAdd", folderService.selectFolderTreeById(id_));
        } else {
            mmap.put("ztreeAdd", new SmBizFolder());
        }
        mmap.put("id_", id_);
        return prefix + "/ztreeAdd";
    }

    /**
     * 新增保存
     * @param smBizFolder
     * @return
     */
    @Log(title = "机构树", businessType = BusinessType.INSERT)
    @PostMapping("/ztreeAdd")
    @ResponseBody
    public AjaxResult addSave(@Validated SmBizFolder smBizFolder) {
        //获取选中左侧树的id
        if ("-1".equals(smBizFolder.getId_()) || "null".equals(smBizFolder.getId_()) || null == smBizFolder.getId_()) {
            smBizFolder.setPath_(smBizFolder.getName_());
        }
        OgUser ogUser = ShiroUtils.getOgUser();
        smBizFolder.setCreateUser_(ogUser.getUserId());
        smBizFolder.setCreateTime_(DateUtils.dateTimeNow());
        return AjaxResult.success("操作成功", folderService.insertTree(smBizFolder));

    }



    /**
     * 转到修改页面
     * @param mmap
     * @return
     */
    @RequestMapping("/lxbg/ztree/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        SmBizFolder folder = folderService.selectFolderTreeById(id);
        if(folder !=null){
            String parent=null;
            if (folder.getParent_()==null||folder.getParent_()==""){
                parent="";
            }else{
                parent=folderService.selectParentName(folder.getParent_());
                mmap.put("parent_", parent);
            }
        }
        mmap.put("id_", id);
        mmap.put("folder", folder);
        //查询是否自己创建的目录树
        //查询当前登录人的id
        String pid = ShiroUtils.getOgUser().getpId();
        //根据id_查出当前目录树的信息
        //根据pid查出人员信息
        OgPerson person =ogPersonService.selectOgPersonById(folder.getCreateUser_());

        if(pid.equals(folder.getCreateUser_())) {
            return prefix + "/ztree";
        }else {
            throw new BusinessException("您不能修改此文件夹,请联系创建人："+person.getpName());
        }

    }

    /**
     * 修改保存
     *
     * @param smBizFolder
     * @return
     */
    @Log(title = "机构树", businessType = BusinessType.UPDATE)
    @PostMapping("/ztree")
    @ResponseBody
    public AjaxResult editSave(@Validated SmBizFolder smBizFolder) {
        smBizFolder.setUpdateTime_(DateUtils.dateTimeNow());
        return toAjax(folderService.updateTree(smBizFolder));
    }


    /**
     * 删除树节点
     * @param id_
     * @return
     */
    @PostMapping("/treeDelete")
    @ResponseBody
    public AjaxResult deleteTree(String id_,SmBizFolder bizFolder) {

        SmBizFolder smBizFolder = new SmBizFolder();
        OgUser ogUser = ShiroUtils.getOgUser();
        smBizFolder.setCreateUser_(ogUser.getUserId());
        smBizFolder.setId_(id_);

        //查询是否自己创建的目录树
        //查询当前登录人的id
        String pid = ShiroUtils.getOgUser().getpId();
        //根据id_查出当前目录树的信息
        SmBizFolder folder = folderService.selectFolderTreeById(id_);
        //根据pid查出人员信息
        OgPerson person =ogPersonService.selectOgPersonById(folder.getCreateUser_());

        //根据这个folder信息查出与例行变更计划对应的单子
        List<SmBizFolder> list = folderService.selectFolderList(bizFolder);

        if(pid.equals(folder.getCreateUser_())){
            //查询有没有子节点
            int leaf = folderService.selectUpdLeafById(id_);
            if (leaf > 0) {
                return AjaxResult.error("该节点下存在子任务，请先删除子任务", null);
            }else if(list.size()>0){
                return AjaxResult.error("请先删除该文件夹下的数据！", null);
            }
            else {
                return toAjax(folderService.deleteTree(id_));
             }
        }else {
            return AjaxResult.error("您不能删除此文件夹,请联系创建人："+person.getpName(), 0);
        }

    }






}
