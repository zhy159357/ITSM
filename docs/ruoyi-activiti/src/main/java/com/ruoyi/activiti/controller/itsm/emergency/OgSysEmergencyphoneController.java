package com.ruoyi.activiti.controller.itsm.emergency;

import com.ruoyi.activiti.domain.OgSysEmergencyphone;
import com.ruoyi.activiti.service.IOgSysEmergencyphoneService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 应急通讯录Controller
 *
 * @author ruoyi
 * @date 2021-07-28
 */
@Controller
@RequestMapping("/system/emergencyphone")
public class OgSysEmergencyphoneController extends BaseController
{
    private String prefix = "emergency";

    @Autowired
    private IOgSysEmergencyphoneService ogSysEmergencyphoneService;

    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IOgUserService ogUserService;

    @GetMapping()
    public String emergencyphone(ModelMap mmap)
    {
        String ifAdmin = "0";
        OgUser ogUser = ogUserService.selectOgUserByUserId(ShiroUtils.getUserId());
        if ("admin".equals(ogUser.getUsername())) {
            ifAdmin = "1";
        }
        mmap.put("ifAdmin",ifAdmin);
        return prefix + "/emList";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgSysEmergencyphone ogSysEmergencyphone)
    {
        if (StringUtils.isEmpty(ogSysEmergencyphone.getSysId())) {
            ogSysEmergencyphone.setSysId("1");
        }
        startPage();
        List<OgSysEmergencyphone> list = ogSysEmergencyphoneService.selectOgSysEmergencyphoneList(ogSysEmergencyphone);
        return getDataTable(list);
    }

    /**
     * id查询
     */
    @PostMapping( "/ifGroupLeader")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        OgSysEmergencyphone ogSysEmergencyphone = new OgSysEmergencyphone();
        ogSysEmergencyphone.getParams().put("pId",ShiroUtils.getUserId());
        ogSysEmergencyphone.getParams().put("sysId", id);
        List<String> gpositionList = ogSysEmergencyphoneService.ifGroupLeader(ogSysEmergencyphone);
        ajaxResult.put("ifGroupLeader", "0");
        if (gpositionList != null && gpositionList.contains("1")) {
            ajaxResult.put("ifGroupLeader", "1");
        }
        OgUser ogUser = ogUserService.selectOgUserByUserId(ShiroUtils.getUserId());
        String username = ogUser.getUsername();
        if ("admin".equals(username)) {
            ajaxResult.put("ifGroupLeader", "1");
        }
        return  ajaxResult;
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap mmap) {

        mmap.put("sysId", id);
        return prefix + "/emAdd";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgSysEmergencyphone ogSysEmergencyphone)
    {
        OgSys sys = sysApplicationManagerService.selectOgSysBySysId(ogSysEmergencyphone.getSysId());
        ogSysEmergencyphone.setSysName(sys.getCaption());
        ogSysEmergencyphone.setEmId(UUID.getUUIDStr());
        ogSysEmergencyphone.setAddTime(DateUtils.getTime());
        ogSysEmergencyphone.setUpdatePerson(ShiroUtils.getUserId());
        ogSysEmergencyphone.setInvalipationMark("1");
        return toAjax(ogSysEmergencyphoneService.insertOgSysEmergencyphone(ogSysEmergencyphone));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{emId}")
    public String edit(@PathVariable("emId") String emId, ModelMap mmap)
    {
        OgSysEmergencyphone ogSysEmergencyphone = ogSysEmergencyphoneService.selectOgSysEmergencyphoneById(emId);
        mmap.put("ogSysEmergencyphone", ogSysEmergencyphone);
        return prefix + "/emEdit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgSysEmergencyphone ogSysEmergencyphone)
    {
        ogSysEmergencyphone.setEditTime(DateUtils.getTime());
        ogSysEmergencyphone.setUpdatePerson(ShiroUtils.getUserId());
        return toAjax(ogSysEmergencyphoneService.updateOgSysEmergencyphone(ogSysEmergencyphone));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(ogSysEmergencyphoneService.deleteOgSysEmergencyphoneByIds(ids));
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = new ArrayList<>();
        ztrees = initZtree(ogSysEmergencyphoneService.selectOgSysTreeForEmergencyPhone());
        return ztrees;
    }

    public List<Ztree> initZtree(List<OgSys> sysList) {
        return initZtree(sysList, null);
    }

    public List<Ztree> initZtree(List<OgSys> sysList, List<String> roleDeptList) {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (OgSys ogSys : sysList) {
            Ztree ztree = new Ztree();
            ztree.setId(ogSys.getSysId());
            ztree.setpId("");
            ztree.setName(ogSys.getCaption());
            ztree.setTitle(ogSys.getCaption());
            if (isCheck) {
                ztree.setChecked(roleDeptList.contains(ogSys.getSysId() + ogSys.getCaption()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<OgSysEmergencyphone> util = new ExcelUtil<OgSysEmergencyphone>(OgSysEmergencyphone.class);
        List<OgSysEmergencyphone> userList = null;
        try{
            userList = util.importExcel(file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.warn("导入数据有误！");
        }
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = ogSysEmergencyphoneService.importEmergencyphone(userList, operName);
        return AjaxResult.success(message);

    }

}
