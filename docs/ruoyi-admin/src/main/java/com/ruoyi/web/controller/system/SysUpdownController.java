package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.service.forward.IAgencyService;
import com.ruoyi.activiti.web.esb.utils.URLCodeUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 信息管理信息
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/updown")
public class SysUpdownController extends BaseController {
    private String prefix = "system/updown";

    @Autowired
    private ISysUpdService iSysUpdService;
    @Autowired
    private ISysUpdownService updownService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IAgencyService iAgencyService;
    @GetMapping()
    public String updown(ModelMap mmap)
    {
        String uId = ShiroUtils.getOgUser().getUserId();
        List<OgRole> ogRoles = iSysRoleService.selectRolesByUserId(uId);
        mmap.addAttribute("folder",0);
        mmap.addAttribute("fold",0);
        mmap.addAttribute("folder_",0);
        for (OgRole role : ogRoles)
        {
            if("9001".equals(role.getRid())){ //type
                mmap.addAttribute("folder",1);
            }
            if("9002".equals(role.getRid())){//type2
                mmap.addAttribute("fold",1);
            }
            if("9000".equals(role.getRid())){//type1
                mmap.addAttribute("folder_",1);
            }
        }
        return prefix + "/updown";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUpdown updown) {
        startPage();
        List<SysUpdown> list = updownService.selectUpdownList(updown);
        for (SysUpdown su:list) {
            String su1 = su.getSize_();
            Long lon = Long.parseLong(su1);
            su.setSize_(Convert.convertSize(lon));
            String path = su.getFolder_();
            if(path==null){
                path="";
            }
            su.setFile_path_(path+"/");
        }
        return getDataTable(list);
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getUserId());
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //String orgId = "4028aba334fa8d0e01357622aa6a1a16";
        //当前登陆人的机构信息
        List<OgOrg> ogOrg = deptService.selectAllChildOrg(orgId);
        List<Ztree> ztrees = updownService.selectFolderTree(new SysPubFolder());
        List<Ztree>  zz= new ArrayList<Ztree>();
        for (Ztree tree : ztrees){
            if(StringUtils.isNotEmpty(tree.getOrgs())){
                for (OgOrg og : ogOrg)
                {
                    if(tree.getOrgs().indexOf(og.getOrgId())>-1 || ogPerson.getpId().equals(tree.getCreateUser())){
                        zz.add(tree);
                        break;
                    }
                }
            }
        }
        return zz;
    }

    /**
     * 获取当前登陆人的二级或者是一级机构
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.当前登陆用户的机构就是一级或者是二级机构
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            return ogOrg;
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            String twoLevelCode = split[2];
            return deptService.selectDeptByCode(twoLevelCode);
        }

    }
    @GetMapping("/upload/{folder_}")
    public String upload(@PathVariable("folder_") String folder_, ModelMap mmap) {
        mmap.put("folder_", folder_);
        return prefix + "/upload";
    }

    @PostMapping("/selectParent")
    @ResponseBody
    public AjaxResult selectParent(HttpServletRequest request, String selecttreeId) {
        String selectId = iSysUpdService.selectParent(selecttreeId);
        if(selectId==null){
            return AjaxResult.success("您无权进行此记录的删除操作", 1);
        }else{
            return AjaxResult.success("null", 2);
        }
    }

    @PostMapping("/saveUpdown")
    @ResponseBody
    public AjaxResult saveUpdown(String folder_, String fileType,
                                 @RequestParam("file_data") MultipartFile file) {
        SysUpdown updown = new SysUpdown();
        try {
            String[] arr = FastDFSHelper.getInstance().uploadBreakpointFile(file);
            updown.setId_(UUID.getUUIDStr());
            OgUser ogUser = ShiroUtils.getOgUser();
            updown.setCreate_user_(ogUser.getUserId());
            updown.setFolder_(folder_);
            updown.setSize_(file.getSize() + "");
            updown.setCreate_time_(DateUtils.dateTimeNow());
            updown.setFile_name_(file.getOriginalFilename());
            updown.setFile_path_(arr[1]);
            updown.setInvalidation_mark(fileType);
        } catch (Exception  e) {
            return error(e.getMessage());
        }
        return toAjax(updownService.insertUpdown(updown));
    }

    //修改附件
    @GetMapping("/edit/{id_}")
    public String edit(@PathVariable("id_") String id_, ModelMap mmap) {
        mmap.put("updown", updownService.selectUpdownById(id_));
        mmap.addAttribute("id_",id_);
        return prefix + "/edit";
    }

    /**
     *  修改附件保存
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysUpdown updown)
    {
        return toAjax(updownService.updateUpdown(updown));
    }


    /**
     * 附件下载
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") String id,
                         HttpServletResponse response, HttpServletRequest request) throws Exception {
        SysUpdown att = new SysUpdown();
        att.setId_(id);
        List<SysUpdown> updowns = updownService.selectUpdownList(att);
        for (SysUpdown updown : updowns) {
            String filePath = updown.getFile_path_();
            String fileName = updown.getFile_name_();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, URLCodeUtils.encode(fileName)));
            byte[] bytes = FastDFSHelper.getInstance().downloadFile("group1", filePath);
            FileUtils.writeBytes(bytes, response.getOutputStream());
        }

    }
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(updownService.deleteUpdownByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }





}