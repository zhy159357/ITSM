package com.ruoyi.activiti.controller.itsm.skill;

import com.ruoyi.activiti.domain.SjFetchSingle;
import com.ruoyi.activiti.domain.TelSkillsOrg;
import com.ruoyi.activiti.service.ITelSkillService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 【技能组管理维护】Controller
 *
 * @author ruoyi
 * @date 2021-05-20
 */
@Controller
@RequestMapping("/skill/one")
public class TelSkillController extends BaseController {

    private String prefix = "skill/one";

    @Autowired
    private ITelSkillService telSkillService;

    @GetMapping()
    public String skill() {
        return prefix + "/skill";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TelSkillsOrg telSkillsOrg) {

        String ogOrgId = telSkillsOrg.getOgOrgId();
        if (StringUtils.isEmpty(ogOrgId)) {
            telSkillsOrg.setOgOrgId("");
        } else {
            OgOrg ogOrg = deptService.selectDeptById(telSkillsOrg.getOgOrgId());
            telSkillsOrg.setOgOrgId(ogOrg.getOrgId());
        }
        startPage();

        List<TelSkillsOrg> list = telSkillService.selectTelSkillList(telSkillsOrg);
        return getDataTable(list);
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/listtwo/{pId}")
    @ResponseBody
    public TableDataInfo listtwo(@PathVariable("pId") String pId) {
        OgPerson person=ogPersonService.selectOgPersonById(pId);
        TelSkillsOrg tso=new TelSkillsOrg();
        tso.setOgOrgId(person.getOrgId());
        startPage();
        List<TelSkillsOrg> list = telSkillService.selectTelSkillListtwo(tso);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelSkillsOrg telSkillsOrg) {
        List<TelSkillsOrg> list = telSkillService.selectTelSkillList(telSkillsOrg);
        ExcelUtil<TelSkillsOrg> util = new ExcelUtil<TelSkillsOrg>(TelSkillsOrg.class);
        return util.exportExcel(list, "skill");
    }

    @ApiOperation("调用技能组接口")
    @RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public AjaxResult bigDataAddSave(@RequestBody SjFetchSingle sjFetchSingle) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("code", "1");
        ajaxResult.put("message", "接收成功");
        return ajaxResult;
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add/{orOrgId}")
    public String add(@PathVariable("orOrgId") String orOrgId, ModelMap mmap) {
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime", nowTime);
        if ("1".equals(orOrgId)) {
            mmap.put("ogOrgId", "");
        } else {
            mmap.put("ogOrgId", orOrgId);
        }
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TelSkillsOrg telSkillsOrg) {

        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telSkillsOrg.setCreateTime(nowTime);
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String id = ogUser.getpId();
        //当前登录人id
        telSkillsOrg.setCreatePerson(id);
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(id);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //把orgId赋值给orOrgId
        String ogOrgId = telSkillsOrg.getOgOrgId();
        if (StringUtils.isEmpty(ogOrgId)) {
            telSkillsOrg.setOgOrgId("");
        } else {
            OgOrg ogOrg = deptService.selectDeptById(telSkillsOrg.getOgOrgId());
            telSkillsOrg.setOgOrgId(ogOrg.getOrgId());
        }

        telSkillsOrg.setId(UUID.getUUIDStr());

        List<TelSkillsOrg> telSkillsOrgsList = telSkillService.selectTelSkillListByName(telSkillsOrg);
        for (TelSkillsOrg telSkillsOrg1 : telSkillsOrgsList){
            String skillsGroupName = telSkillsOrg1.getSkillsGroupName();
            if(telSkillsOrg.getSkillsGroupName().equals(skillsGroupName)){
                return AjaxResult.error("技能组别名已存在");
            }
        }

        List<TelSkillsOrg> telSkillsOrgsListName = telSkillService.selectTelSkillListByTelName(telSkillsOrg);
        for (TelSkillsOrg telSkillsOrg1 : telSkillsOrgsListName){
            String skillsGroupTelname = telSkillsOrg1.getSkillsGroupTelname();
            if(telSkillsOrg.getSkillsGroupTelname().equals(skillsGroupTelname)){
                return AjaxResult.error("技能组名称已存在");
            }
        }

        return toAjax(telSkillService.insertTelSkill(telSkillsOrg));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        TelSkillsOrg telSkillsOrg = telSkillService.selectTelSkillById(id);
        mmap.put("telSkill", telSkillsOrg);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TelSkillsOrg telSkillsOrg) {
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telSkillsOrg.setUpdateTime(nowTime);
        return toAjax(telSkillService.updateTelSkill(telSkillsOrg));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(telSkillService.deleteTelSkillByIds(ids));
    }


    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService ogPersonService;

    /**
     * 选择部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
                                 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }


    /**
     * 加载部门列表树（排除下级）
     */
    @GetMapping("/treeData/{excludeId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) String excludeId) {
        OgOrg dept = new OgOrg();
        dept.setOrgId(excludeId);
        List<Ztree> ztrees = deptService.selectDeptTreeExcludeChild(dept);
        return ztrees;
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

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData(SysRole role) {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }

    @PostMapping("/selDeptByDeptId/{deptId}")
    @ResponseBody()
    public AjaxResult selDeptByDeptId(@PathVariable String deptId) {
        OgOrg sysDept = deptService.selectDeptById(deptId);
        return AjaxResult.success("部门查询", sysDept);
    }


    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //String orgId = "4028aba334fa8d0e01357622aa6a1a16";
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());

        List<Ztree> ztrees = deptService.selectDeptTree(org);
        return ztrees;
    }

    /**
     * 选择所有部门树
     */
    @GetMapping("selectDeptTreeAll")
    public String selectAllTree(ModelMap mmap) {
        return prefix + "/treeAll";
    }

    /**
     * 加载所有部门列表树
     */
    @GetMapping("/selectAllTree")
    @ResponseBody
    public List<Ztree> selectAllTree() {
        OgOrg org = new OgOrg();
        org.setLevelCode("/000000000/");
        return deptService.selectDeptTree(org);
    }

}
