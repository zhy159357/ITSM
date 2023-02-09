package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUpdService;
import com.ruoyi.system.service.ISysUpdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 信息管理信息
 *
 * @author Mr.Xy
 */
@Controller
public class SysUpDtreeController extends BaseController {
    private String prefix = "system/upDtree";

    @Autowired
    private ISysUpdService iSysUpdService;
    @Autowired
    private ISysUpdownService updownService;

    //新增
    @RequestMapping("/system/updAdd/{id}")
    public String add(@PathVariable("id") String id_, ModelMap mmap) {
        //根据传来的上级id查询出所选的上级信息
        if (!"-1".equals(id_)) {
            mmap.put("updAdd", iSysUpdService.selectUpdById(id_));
        } else {
            mmap.put("updAdd", new SysPubFolder());
        }
        mmap.put("id_", id_);
        return prefix + "/updAdd";
    }

    /**
     * 新增保存
     */
    @Log(title = "机构树", businessType = BusinessType.INSERT)
    @PostMapping("/system/upDtree/updAdd")
    @ResponseBody
    public AjaxResult addSave(@Validated SysPubFolder sysPubFolder) {
        if ("-1".equals(sysPubFolder.getId_()) || "null".equals(sysPubFolder.getId_()) || null == sysPubFolder.getId_()) {
            sysPubFolder.setPath_(sysPubFolder.getName_());
        }
        OgUser ogUser = ShiroUtils.getOgUser();
        sysPubFolder.setCreate_user_(ogUser.getUserId());
        sysPubFolder.setCreate_time_(DateUtils.dateTimeNow());

        return AjaxResult.success("操作成功", iSysUpdService.insertTree(sysPubFolder));

    }

    /**
     * 修改
     * @param id_
     * @param mmap
     * @return
     */
    @RequestMapping("/system/upd/{id}")
    public String edit(@PathVariable("id") String id_, ModelMap mmap) {

        StringBuffer sb = new StringBuffer();
        SysPubFolder spf = iSysUpdService.selectUpdById(id_);
        String parent=null;
        if (spf.getParent_()==null||spf.getParent_()==""){
            parent="";
        }else{
            parent=iSysUpdService.selectParentName(spf.getParent_());
        }
        String orgs_ = iSysUpdService.selectUpdById(id_).getOrgs_();
        orgs_ = (orgs_==null) ? "" :orgs_;
        String [] result =  orgs_.split(",");
        for (String orgId:result) {
            String org = iSysUpdService.selectOrgById(orgId);
            if(org!=null){
                if (StringUtils.isNotEmpty(org)){
                    sb.append(org);
                    sb.append(",");
                }
            }
        }
        mmap.put("orgs_", sb.toString());
        mmap.put("parent_", parent);
        mmap.put("id_", id_);
        mmap.put("upd", spf);
        return prefix + "/upd";
    }



    /**
     * 查看详情
     * @param id_
     * @param mmap
     * @return
     */
    @RequestMapping("/system/upd/detail/{id}")
    public String detail(@PathVariable("id") String id_, ModelMap mmap) {

        StringBuffer sb = new StringBuffer();
        SysPubFolder spf = iSysUpdService.selectUpdById(id_);
        String parent=null;
        if (spf.getParent_()==null||spf.getParent_()==""){
            parent="";
        }else{
            parent=iSysUpdService.selectParentName(spf.getParent_());
        }
        String orgs_ = iSysUpdService.selectUpdById(id_).getOrgs_();
        orgs_ = (orgs_==null) ? "" :orgs_;
        String [] result =  orgs_.split(",");
        for (String orgId:result) {
            String org = iSysUpdService.selectOrgById(orgId);
            if(org!=null){
                if (StringUtils.isNotEmpty(org)){
                    sb.append(org);
                    sb.append(",");
                }
            }
        }
        mmap.put("orgs_", sb.toString());
        mmap.put("parent_", parent);
        mmap.put("id_", id_);
        mmap.put("upd", spf);
        return prefix + "/detail";
    }

    /**
     * 修改保存
     *
     * @param sysPubFolder
     * @return
     */
    @Log(title = "机构树", businessType = BusinessType.UPDATE)
    @PostMapping("/system/upDtree/upd")
    @ResponseBody
    public AjaxResult editSave(@Validated SysPubFolder sysPubFolder) {
        sysPubFolder.setUpdate_time_(DateUtils.dateTimeNow());
        return toAjax(iSysUpdService.updateUpd(sysPubFolder));
    }

    /**
     * 删除树节点
     */
    @PostMapping("/system/upDtree/treeDelete")
    @ResponseBody
    public AjaxResult deleteTree(String id_,SysUpdown updown) {

        //根据这个updown信息查出与上传下载对应的单子
        List<SysUpdown> list = iSysUpdService.selectUpdownById(updown);
        //查询有没有子节点
        int leaf = iSysUpdService.selectUpdLeafById(id_);
        if (leaf > 0) {
            return AjaxResult.error("该节点下存在子任务，请先删除子任务", null);
        }else if(list.size()>0){
            return AjaxResult.error("请先删除该文件夹下的数据！", null);
        }
        else {
            return toAjax(iSysUpdService.deleteUpd(id_));
        }

    }

    /**
     * 选择机构页面
     */
    @GetMapping("/system/upDtree/deptym")
    public String selectGroup()
    {
        return prefix + "/deptym";
    }
}