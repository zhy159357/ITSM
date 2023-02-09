package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.activiti.service.PatrolService;
import com.ruoyi.activiti.domain.PatrolInspection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 舆情监测系统巡检Controller
 */
@Controller
@RequestMapping("/patrol/monitoring")
public class PatrolController extends BaseController {
    private String prefix = "system/patrol";

    @Autowired
    private PatrolService patrolService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    /**
     * 舆情监测巡检页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping()
    public String pl(ModelMap modelMap) {
        modelMap.put("user", ShiroUtils.getOgUser());
        return prefix + "/pl";
    }

    /**
     * 查询监测系统巡检列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PatrolInspection patrolInspection) {
        startPage();
        List<PatrolInspection> list = patrolService.selectPatrolList(patrolInspection);
        return getDataTable(list);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Log(title = "舆情监测系统检测单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(patrolService.deletePatrolByIds(ids));
    }

    /**
     * 新增页面
     *
     * @param userId
     * @param modelMap
     * @return
     */
    @GetMapping("/add/{userId}")
    public String add(@PathVariable("userId") String userId, ModelMap modelMap) {
        //查询当前用户所在的机构信息
        OgUser ogUser = new OgUser();
        ogUser.setUserId(userId);
        List<OgUser> ogUsers = ogUserService.selectAccountList(ogUser);
        modelMap.put("org", ogUsers.get(0));

        String uid = ogUser.getUserId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(uid);
        String orgId = ogPerson.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        if (!StringUtils.isEmpty(ogOrg)) {
            String orgLv = ogOrg.getOrgLv();
            if (Integer.valueOf(orgLv) > 3) {
                String s = deptService.getpCode(uid);
                if (!s.equals("010000888")) {
                    String orgName = ogOrg.getOrgName();
                    modelMap.put("treeName", orgName);
                    modelMap.put("createOrgId", ogOrg.getOrgId());
                }
            }
        }

        modelMap.put("pId", ShiroUtils.getUserId());
        modelMap.put("patrolId", UUID.getUUIDStr());
        return prefix + "/add";
    }
    /**
     * 详情页面
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") String id, ModelMap mmap) {
        PatrolInspection patrolInspection = new PatrolInspection();
        patrolInspection.setPatrolId(id);
        List<PatrolInspection> patrolInspections = patrolService.selectPatrolList(patrolInspection);
        PatrolInspection inspection = patrolInspections.get(0);
        mmap.put("patrolInspection", inspection);
        mmap.put("patrolId", id);
        return prefix + "/details";
    }
    /**
     * 修改单页面
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        PatrolInspection patrolInspection = new PatrolInspection();
        patrolInspection.setPatrolId(id);
        PatrolInspection patrolIn = patrolService.selectPatrolListById(id);
        mmap.put("patrolId", id);
        mmap.put("patrolInspection", patrolIn);
        mmap.put("pId", ShiroUtils.getUserId());

        return prefix + "/edit";
    }

    /**
     * 新增保存 暂存
     *
     * @param patrolInspection
     * @return
     */
    @Log(title = "舆情监测系统检测单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(PatrolInspection patrolInspection) {

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        patrolInspection.setCreateId(ShiroUtils.getUserId());
        patrolInspection.setCreateTime(DateUtils.dateTimeNow());
        try {
            patrolService.insertPatrolInspection(patrolInspection);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("舆情监测系统检测单新增失败: {} ", e);
            return AjaxResult.error("舆情监测系统检测单暂存失败");
        }

        return AjaxResult.success("舆情监测系统检测单保存成功");

    }

    /**
     * 修改 暂存
     *
     * @param patrolInspection
     * @return
     */
    @Log(title = "舆情监测系统检测单", businessType = BusinessType.INSERT)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult edit(PatrolInspection patrolInspection) {

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        try {
            patrolService.updatePatrolInspection(patrolInspection);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("舆情监测系统检测单修改失败: {} ", e);
            return AjaxResult.error("舆情监测系统检测单修改失败");
        }

        return AjaxResult.success("舆情监测系统检测单修改成功");

    }

    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix + "/selectOneApplication";
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @PostMapping("/selectById")
    @ResponseBody
    public AjaxResult selectById(String id) {
        AjaxResult ajaxResult = new AjaxResult();
        PatrolInspection patrolInspection = patrolService.selectPatrolListById(id);
        ajaxResult.put("data", patrolInspection);
        return ajaxResult;
    }
}
