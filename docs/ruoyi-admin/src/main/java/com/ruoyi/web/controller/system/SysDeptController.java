package com.ruoyi.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.OgDeptDto;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/dept")
@Slf4j
public class SysDeptController extends BaseController {

    @Value("${foreign.dept.url}")
    private String deptUrl;
    @Value("${foreign.dept.serviceCode}")
    private String serviceCode;
    private String prefix = "system/dept";
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private RestTemplate restTemplate;

    @Resource
    ISysWorkService iSysWorkService;

    @Resource
    private IOgGroupPersonService ogGroupPersonService;

    @GetMapping()
    public String dept() {
        return prefix + "/dept";
    }

    @Autowired
    private IOgPersonService iOgPersonService;

    @Resource
    ISysDeptService iSysDeptService;

    @Resource
    IChangePersonService changePersonService;

    @Resource
    IOgGroupPersonService iOgGroupPersonService;

    @Autowired
    private IOgUserService ogUserService;

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgOrg dept) {
        // 如果parentName信息为空则将parentId信息置空
        dept.setParentId(StringUtils.isNotEmpty(dept.getParentName()) ? dept.getParentId() : "");
        OgPerson person = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        // 判断当前登录人是否全国中心，是全国中心默认查询所有，不是全国中心使用层级码like查询
        if (!("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/"))) {
            dept.setLevelCode(org.getLevelCode());
        }
        startPage();
        List<OgOrg> deptList = deptService.selectDeptList(dept);
        return getDataTable(deptList);
    }

    @PostMapping("/listDept")
    @ResponseBody
    public TableDataInfo listDept(OgOrg dept) {
        // 如果parentName信息为空则将parentId信息置空
        dept.setParentId(StringUtils.isNotEmpty(dept.getParentName()) ? dept.getParentId() : "");
        OgPerson person = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        // 判断当前登录人是否全国中心，是全国中心默认查询所有，不是全国中心使用层级码like查询
        if (!("000000000".equals(org.getOrgCode()))) {
            dept.setLevelCode(org.getLevelCode());
        }
        startPage();
        List<OgOrg> deptList = deptService.selectDeptList(dept);
        return getDataTable(deptList);
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") String parentId, ModelMap mmap) {
        OgOrg org = deptService.selectDeptById(parentId);
        if (StringUtils.isNotEmpty(org.getOrgLv())) {
            org.setOrgLv(String.valueOf(Integer.valueOf(org.getOrgLv()) + 1));
        }
        mmap.put("dept", org);
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(OgOrg dept) {
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return error("新增部门'" + dept.getOrgName() + "'失败，部门名称已存在");
        }

        OgOrg isExist = deptService.selectDeptByCode(dept.getOrgCode());
        if (null != isExist) {
            return error("新增部门'" + dept.getOrgName() + "'失败，部门编码已存在");
        }

        dept.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") String deptId, ModelMap mmap) {
        OgOrg dept = deptService.selectDeptById(deptId);
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgOrg dept) {
        if (dept.getParentId().equals(dept.getOrgId())) {
            return error("修改部门'" + dept.getOrgName() + "'失败，上级部门不能是自己");
        }
        dept.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") String deptId) {
        if (deptService.selectDeptCount(deptId) > 0) {
            return AjaxResult.warn("存在下级机构,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.warn("机构存在人员,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(OgOrg dept) {
        return deptService.checkDeptNameUnique(dept);
    }

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
        // 加载机构树增加是否全国中心权限控制
        OgPerson person = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg o = deptService.selectDeptById(person.getOrgId());
        OgOrg org = new OgOrg();
        // 如果是第一级默认查询所有
        if ("000000000".equals(o.getOrgCode())) {
            org.setLevelCode("/000000000/");
        } else {
            // 使用层级码分割然后取出第二级机构码，使用like查询
            String levelCode = o.getLevelCode();
            String[] levelCodeArray = Convert.toStrArray("/", levelCode);
            if (levelCodeArray != null && levelCodeArray.length > 2) {
                org.setLevelCode("/000000000/" + levelCodeArray[2]);
            }
        }
        return deptService.selectDeptTree(org);
    }

    /**
     * 加载所有部门列表树
     */
    @GetMapping("/selectAllTreeDept")
    @ResponseBody
    public List<Ztree> selectAllTreeDept() {
        // 加载机构树增加是否全国中心权限控制
        OgPerson person = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg o = deptService.selectDeptById(person.getOrgId());
        OgOrg org = new OgOrg();
        // 如果是第一级默认查询所有
        if ("000000000".equals(o.getOrgCode())) {
            org.setLevelCode("/000000000/");
        } else {
            // 使用层级码分割然后取出第二级机构码，使用like查询
            String levelCode = o.getLevelCode();
            org.setLevelCode(levelCode);
        }
        return deptService.selectDeptTree(org);
    }

    @GetMapping("/selectAllDept")
    @ResponseBody
    public List<Ztree> selectAllDept(@RequestParam("levelCode") String levelCode) {
        // 加载机构树增加是否全国中心权限控制
        OgOrg org = new OgOrg();
        org.setLevelCode(levelCode);
        return deptService.selectDeptTree(org);
    }

    /**
     * 加载问题牵头部室数据列表
     *
     * @return
     */
    @PostMapping("/treeListSysDept")
    @ResponseBody
    @ApiOperation(value = "问题单牵头部室数据")
    public AjaxResult treeList() {
        OgOrg ogOrg = new OgOrg();
        List<OgOrg> ogOrgList = deptService.selectDeptListByOrgId(ogOrg);
        return AjaxResult.success(getTree(ogOrgList));
    }

    /**
     * 事件单上报部门
     *
     * @return
     */
    @PostMapping("/incident/treeListSysDept")
    @ResponseBody
    @ApiOperation(value = "事件单上报部门")
    public AjaxResult incidentTreeListSysDept() {
        OgOrg ogOrg = new OgOrg();
        // 查询固定的二线部门人员
        ogOrg.setMemo("SecondLine");
        List<OgOrg> ogOrgList = deptService.selectDeptList(ogOrg);
        List<SysDeptVo> sysDeptVoList = new LinkedList();
        for (OgOrg ogOrgl : ogOrgList) {
            SysDeptVo sysDeptVo = new SysDeptVo();
            sysDeptVo.setValue(ogOrgl.getOrgId());
            sysDeptVo.setLabel(ogOrgl.getOrgName());
            sysDeptVo.setChildren(getChild(ogOrgl.getOrgId(), ogOrgList));
            sysDeptVoList.add(sysDeptVo);
        }
        if (!CollectionUtils.isEmpty(ogOrgList)) {
            ogOrgList.forEach(og -> {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(og.getOrgId());
                sysDeptVo.setLabel(og.getOrgName());
                sysDeptVoList.add(sysDeptVo);
            });
        }
        return AjaxResult.success(sysDeptVoList);
    }

    /*** 加载问题牵头人列表和审核人
     *
     * @return
     */
    @PostMapping("/listOgPerson")
    @ResponseBody
    @ApiOperation(value = "加载问题单牵头部室人员列表")
    public AjaxResult listOgPerson(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("orgId"))) {
            return AjaxResult.error("请先选择机构!");
        }
        OgPerson ogPersonQuery = new OgPerson();
        ogPersonQuery.setOrgId(jsonObject.get("orgId").toString());
        List<OgPerson> ogPeopleList = iOgPersonService.selectOgPersonList(ogPersonQuery);
        if (CollectionUtils.isEmpty(ogPeopleList)) {
            return AjaxResult.error("该机构下尚未配置人员！");
        }
        List<Object> ogPeopleMapList = ogPeopleList.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            mapPeople.put("value", op.getpId());
            mapPeople.put("label", op.getpName());
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }

    /**
     * 加载审核人
     *
     * @return
     */
    @PostMapping("/auditOgPerson")
    @ResponseBody
    @ApiOperation(value = "加载问题单审核人")
    public AjaxResult auditPerson(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("orgId"))) {
            throw new BusinessException("请先选择部室!");
        }
        // 分行情况
        List<PubParaValue> bankList = pubParaValueService.selectPubParaValueById("60a02cbc224344749c4d1d0ec65f6d5a");
        // 获取到各分行信息技术部id
        List<String> orgidList = bankList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList());
        if (orgidList.contains(jsonObject.get("orgId").toString())) {
            OgOrg ogOrg = iSysDeptService.selectDeptById(jsonObject.get("orgId").toString());
            String orgId = changePersonService.selectDept(ogOrg.getOrgId());
            if (StringUtils.isBlank(orgId)) {
                throw new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
            }
            OgGroup ogGroup = new OgGroup();
            // 設置當前登錄人所在的機構id todo  后续会根因sysid和orgid查询
            ogGroup.setOrgId(orgId);
            ogGroup.setMemo("problem_admin_not_update");// 问题管理员组
            List<OgGroup> ogGroups = iSysWorkService.selectOgGroupList(ogGroup);
            if (org.springframework.util.CollectionUtils.isEmpty(ogGroups)) {
                throw new BusinessException("该組不存在!");
            }
            String groupid = ogGroups.get(0).getGroupId();
            List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById(groupid);
            if (org.springframework.util.CollectionUtils.isEmpty(ogGroupPeopleList)) {
                throw new BusinessException("该机构没有配置人员!");
            }

            List<Object> ogPeopleMapList = ogGroupPeopleList.stream().map(op -> {
                Map<String, String> mapPeople = new HashMap<>();
                mapPeople.put("value", op.getPid());
                mapPeople.put("label", op.getPname());
                return mapPeople;
            }).collect(Collectors.toList());
            return AjaxResult.success(ogPeopleMapList);
        } else {
            OgPerson ogPersonQuery = new OgPerson();
            ogPersonQuery.setOrgId(jsonObject.get("orgId").toString());
            List<OgPerson> ogPeopleList = iOgPersonService.selectOgPersonList(ogPersonQuery);
            if (org.springframework.util.CollectionUtils.isEmpty(ogPeopleList)) {
                throw new BusinessException("该机构没有配置人员!");
            }
            List<OgPerson> leaderPersonList = ogPeopleList.stream().filter(ogPerson -> com.ruoyi.common.utils.StringUtils.equals("1", ogPerson.getLeader())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(leaderPersonList)) {
                throw new BusinessException("该机构-{%s}下没有指定领导！", jsonObject.get("orgId").toString());
            }
            List<Object> ogPeopleMapList = leaderPersonList.stream().map(op -> {
                Map<String, String> mapPeople = new HashMap<>();
                mapPeople.put("value", op.getpId());
                mapPeople.put("label", op.getpName());
                return mapPeople;
            }).collect(Collectors.toList());
            return AjaxResult.success(ogPeopleMapList);
        }
    }


    /**
     * 组装树形结构
     *
     * @param ogOrgList
     * @return
     */
    public List<SysDeptVo> getTree(List<OgOrg> ogOrgList) {
        if (CollectionUtils.isEmpty(ogOrgList)) {
            throw new BusinessException("机构为空!");
        }
        // 加载金融科技部,数据管理与应用  北京,天津,(市北,市南,浦东,浦西没有信息技术部),南京,苏州,宁波,杭州,深圳,成都等这些分行下面的信息技术部
        List<PubParaValue> pubParaValueList = pubParaValueService.selectPubParaValueById("c1db677eb52342a5937911787ed80b85");
        // 配置的各分行
        List<PubParaValue> bankList = pubParaValueService.selectPubParaValueById("60a02cbc224344749c4d1d0ec65f6d5a");
        return ogOrgList.stream().filter(ogOrg -> pubParaValueList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList()).contains(ogOrg.getOrgId())).map(ogOrg -> {
            // 各分行需要添加分行名称
            if (bankList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList()).contains(ogOrg.getOrgId())) {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(ogOrg.getOrgId());
                sysDeptVo.setLabel(ogOrg.getParentName() + "_" + ogOrg.getOrgName());
                sysDeptVo.setChildren(getChild(ogOrg.getOrgId(), ogOrgList));
                return sysDeptVo;
            }
            SysDeptVo sysDeptVo = new SysDeptVo();
            sysDeptVo.setValue(ogOrg.getOrgId());
            sysDeptVo.setLabel(ogOrg.getOrgName());
            sysDeptVo.setChildren(getChild(ogOrg.getOrgId(), ogOrgList));
            return sysDeptVo;
        }).collect(Collectors.toList());
    }

    /**
     * 递归获取每级的子类
     *
     * @param code
     * @param ogOrgList
     * @return
     */
    private static List<SysDeptVo> getChild(String code, List<OgOrg> ogOrgList) {
        List<SysDeptVo> childrenList = new LinkedList();
        for (OgOrg ogOrg : ogOrgList) {
            if (code.equals(ogOrg.getParentId())) {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(ogOrg.getOrgId());
                sysDeptVo.setLabel(ogOrg.getOrgName());
                sysDeptVo.setChildren(getChild(ogOrg.getOrgId(), ogOrgList));
                childrenList.add(sysDeptVo);
            }
        }
        return childrenList;
    }

    /**
     * 加载部门列表树--用于配置牵头部门
     */
    @GetMapping("/treeData4Configlp")
    @ResponseBody
    public List<Ztree> treeData4Configlp() {
        OgOrg org = new OgOrg();
        List<Ztree> ztrees = deptService.selectAllDeptTree(org);
        return ztrees;
    }

    /**
     * 查询事件单二线部门
     */
    @PostMapping("/selectSecondLineDept")
    @ResponseBody
    public List<OgOrg> selectSecondLineDept() {
        OgOrg org = new OgOrg();
        org.setMemo("SecondLine");
        List<OgOrg> list = deptService.selectDeptList(org);
        return list;
    }

    /**
     * 查询事件单二线部门所属人员
     */
    @PostMapping("/selectSecondLineDealPerson")
    @ResponseBody
    public List<OgPerson> selectSecondLineDealPerson(@RequestBody Map<String, Object> params) {
        String orgId = params.get("orgId").toString();
        OgPerson person = new OgPerson();
        person.setOrgId(orgId);
        List<OgPerson> personList = ogPersonService.selectOgPersonList(person);
        return personList;
    }

    /**
     * 查询参与机构
     */
    @GetMapping("/treeDataForPartakeOrg")
    @ResponseBody
    public AjaxResult treeDataForPartakeOrg() {
        //OgOrg ogOrg = new OgOrg();
        List<SysDeptVo> ogOrgList = deptService.treeDataForPartakeOrg();
        return AjaxResult.success(ogOrgList);
    }

    /**
     * 选择参与机构树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectParTaskTree/{deptId}", "/selectParTaskTree/{deptId}/{excludeId}"})
    public String selectParTaskTree(@PathVariable("deptId") String deptId,
                                    @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/parTaskTree";
    }

    /**
     * 加载参与机构树
     */
    @GetMapping("/parTaskTreeData")
    @ResponseBody
    public List<Ztree> parTaskTreeData() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());

        List<Ztree> ztrees = deptService.selectDeptTree(org);
        return ztrees;
    }

    /*** 加载总经理室组人员
     *
     * @return
     */
    @PostMapping("/selectOgGroupPersons")
    @ResponseBody
    @ApiOperation(value = "加载总经理室组人员")
    public AjaxResult selectOgGroupPersons(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("groupId"))) {
            return AjaxResult.error("组id为空!");
        }
        return AjaxResult.success(iOgGroupPersonService.selectOgGroupPersons(jsonObject.get("groupId").toString()));
    }

    /*** 加载总经理室组人员
     *
     * @return
     */
    @PostMapping("/selectGeneralManagers")
    @ResponseBody
    @ApiOperation(value = "加载总经理室组人员")
    public AjaxResult selectGeneralManagers(String id) {

        return AjaxResult.success(iOgGroupPersonService.selectGeneralManagers(id));
    }

    /**
     * 事件单查询上报人
     *
     * @return
     */
    @PostMapping("/incident/selectReportPerson")
    @ResponseBody
    @ApiOperation(value = "事件单上报人")
    public AjaxResult selectReportPerson(@RequestBody Map<String, String> params) {
        String orgId = params.get("orgId");
        List<OgPerson> personList = ogPersonService.selectOgPersonListByOrgId(orgId);
        List<Map<String, String>> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(personList)) {
            collect = personList.stream().map(person -> {
                Map<String, String> map = new HashMap<>();
                map.put("value", person.getpId());
                String label = person.getpName();
                if (StringUtils.isNotEmpty(person.getEmail())) {
                    label += person.getEmail();
                }
                map.put("label", label);
                return map;
            }).collect(Collectors.toList());
        }
        return AjaxResult.success(collect);
    }

    /**
     * 事件单上报部门和电话号码
     *
     * @return
     */
    @PostMapping("/incident/incidentReportSysDept")
    @ResponseBody
    @ApiOperation(value = "事件单上报部门")
    public AjaxResult incidentReportSysDept(@RequestBody Map<String, Object> params) {
        String pId = (String) params.get("pId");
        if (StringUtils.isEmpty(pId)) {
            return AjaxResult.success(new HashMap<>());
        }
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        OgOrg ogOrg = deptService.selectDeptById(person.getOrgId());
        boolean orgFlag = deptService.judgeCurrentOrgIfAll(person.getOrgId());

        Map<String, Object> map = new HashMap<>();
        map.put("orgId", ogOrg.getOrgId());
        map.put("orgName", ogOrg.getOrgName());
        map.put("mobilePhone", person.getMobilPhone());
        map.put("orgFlag", orgFlag);

        return AjaxResult.success(map);
    }

    /**
     * 同步应用系统数据 调用外部ADMP系统webService接口
     */
    @RequestMapping("/tongbu")
    @ResponseBody
    public AjaxResult tongbu() {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            Map<String, Object> service = new HashMap<>();
            service.put("serviceCode", serviceCode);
            HttpHeaders httpHeaders = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
            httpHeaders.setContentType(type);
            String json = JSON.toJSONString(service);
            httpHeaders.add("reqData", json);

            HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
            ResponseEntity<String> resp = restTemplate.postForEntity(deptUrl, httpEntity, String.class);
            String body = resp.getBody();
            log.info("机构数据:" + body);
            JSONObject jsonObject = JSON.parseObject(body);
            Map<String, Object> content = (Map<String, Object>) jsonObject.get("content");
            Map<String, Object> data = (Map<String, Object>) content.get("data");
//            List<OgDeptDto> ogDeptDtoList = addList();
            List<OgDeptDto> ogDeptDtoList = JSONObject.parseArray(data.get("data").toString(), OgDeptDto.class);
            ogDeptDtoList.stream().map(p -> getChildNode(p, ogDeptDtoList)).collect(Collectors.toList());
            ogDeptDtoList.stream().forEach(p -> {
                OgOrg ogOld = deptService.selectDeptById(p.getOrgcode());
                OgOrg ogOrg = new OgOrg();
                ogOrg.setOrgId(p.getOrgcode());
                ogOrg.setOrgCode(p.getOrgcode());
                ogOrg.setOrgName(p.getOrgname());
                ogOrg.setType(p.getOrgtype());
                ogOrg.setParentId(p.getP_orgcode());
                ogOrg.setParentName(p.getOrgname());
                ogOrg.setInvalidationMark(p.getOrgstatus());
                if (StringUtils.isEmpty(ogOld)) {
                    OgOrg pOg = deptService.selectDeptById(p.getP_orgcode());
                    if (StringUtils.isEmpty(pOg)) {
                        String level = "/" + p.getOrglevel() + "/";
                        ogOrg.setLevelCode(level.replace("//", "/"));
                    } else {
                        String level = pOg.getLevelCode() + "/" + ogOrg.getOrgCode() + "/";
                        ogOrg.setLevelCode(level.replace("//", "/"));
                    }
                    deptMapper.insertDeptMysql(ogOrg);
                } else {
                    OgOrg info = deptMapper.selectDeptById(ogOld.getParentId());
                    if (StringUtils.isEmpty(info)) {
                        String level = "/" + p.getOrglevel() + "/";
                        ogOrg.setLevelCode(level.replace("//", "/"));
                    } else {
                        String level = info.getLevelCode() + "/" + ogOld.getOrgCode() + "/";
                        ogOrg.setLevelCode(level.replace("//", "/"));
                    }
                    ogOrg.setOrgLeader(ogOld.getOrgLeader());
                    deptMapper.updateDeptMysql(ogOrg);
                }
            });
            ajaxResult.put("result", 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("同步失败！");
        }
        return ajaxResult;
    }


    /**
     * 递归获取每级的子类
     *
     * @param code      父节点信息
     * @param ogOrgList
     * @return
     */
    private static List<OgDeptDto> getChildNode(OgDeptDto code, List<OgDeptDto> ogOrgList) {
        for (OgDeptDto ogDeptDto : ogOrgList) {
            if (!code.getOrgcode().equals(ogDeptDto.getOrgcode())) {
                if (StringUtils.isNotEmpty(code.getP_orgcode()) && code.getP_orgcode().equals(ogDeptDto.getOrgcode())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ogDeptDto.getOrgcode()).append("/").append(code.getOrgcode());
                    code.setOrglevel(pLevef(ogDeptDto, ogOrgList) + "/" + sb.toString());
                }
            }
        }
        if (StringUtils.isEmpty(code.getOrglevel())) {
            code.setOrglevel("/" + code.getP_orgcode() + "/" + code.getOrgcode());
        }
        return ogOrgList;
    }

    public static String pLevef(OgDeptDto code, List<OgDeptDto> ogOrgList) {
        for (OgDeptDto ogDeptDto : ogOrgList) {
            if (!code.getOrgcode().equals(ogDeptDto.getOrgcode())) {
                if (StringUtils.isNotEmpty(code.getP_orgcode()) && code.getP_orgcode().equals(ogDeptDto.getOrgcode())) {
                    //fu节点
                    StringBuilder sb = new StringBuilder();
                    String str = pLevef(ogDeptDto, ogOrgList);
                    return sb.append(str).append("/").append(ogDeptDto.getOrgcode()).toString();
                }
            }
        }
        return "";
    }


    public List<OgDeptDto> addList() {
        List<OgDeptDto> ogDeptDtoList = new ArrayList<>();
        OgDeptDto ogDeptDto = new OgDeptDto();
        ogDeptDto.setOrgcode("910100001");
        ogDeptDto.setOrgname("北京bbbb支行");
        ogDeptDto.setP_orgcode("900000001");
        ogDeptDto.setOrgtype("1");
        ogDeptDto.setOrgstatus("1");
        ogDeptDtoList.add(ogDeptDto);

        OgDeptDto ogDeptDto1 = new OgDeptDto();
        ogDeptDto1.setOrgcode("900000001");
        ogDeptDto1.setP_orgcode("1111111111");
        ogDeptDto1.setOrgname("北京支行");
        ogDeptDto1.setOrgtype("1");
        ogDeptDto1.setOrgstatus("1");
        ogDeptDtoList.add(ogDeptDto1);
        return ogDeptDtoList;
    }


    public static void main(String[] args) {

        List<OgDeptDto> ogDeptDtoList = new ArrayList<>();
//        OgDeptDto ogDeptDto3 = new OgDeptDto();
//        ogDeptDto3.setOrgcode("110100002");
//        ogDeptDto3.setOrgname("北京中关村支行1");
//        ogDeptDto3.setP_orgcode("110100001");
//        ogDeptDto3.setOrgtype("1");
//        ogDeptDto3.setOrgstatus("1");
//        ogDeptDtoList.add(ogDeptDto3);
//
//        OgDeptDto ogDeptDto6 = new OgDeptDto();
//        ogDeptDto6.setOrgcode("110100006");
//        ogDeptDto6.setOrgname("北京西二旗支行");
//        ogDeptDto6.setP_orgcode("110100003");
//        ogDeptDtoList.add(ogDeptDto6);
//
//        OgDeptDto ogDeptDto8 = new OgDeptDto();
//        ogDeptDto8.setOrgcode("110100008");
//        ogDeptDto8.setOrgname("北京1-1网点");
//        ogDeptDto8.setP_orgcode("110100007");
//        ogDeptDtoList.add(ogDeptDto8);
//
//        OgDeptDto ogDeptDto1 = new OgDeptDto();
//        ogDeptDto1.setOrgcode("110100001");
//        ogDeptDto1.setOrgname("北京海淀支行");
//        ogDeptDtoList.add(ogDeptDto1);
//
//        OgDeptDto ogDeptDto2 = new OgDeptDto();
//        ogDeptDto2.setOrgcode("110100002");
//        ogDeptDto2.setOrgname("北京中关村支行");
//        ogDeptDto2.setP_orgcode("110100001");
//        ogDeptDtoList.add(ogDeptDto2);
//
//        OgDeptDto ogDeptDto4 = new OgDeptDto();
//        ogDeptDto4.setOrgcode("110100004");
//        ogDeptDto4.setOrgname("北京科贸网点");
//        ogDeptDto4.setP_orgcode("110100002");
//        ogDeptDtoList.add(ogDeptDto4);
//
//        OgDeptDto ogDeptDto5 = new OgDeptDto();
//        ogDeptDto5.setOrgcode("110100005");
//        ogDeptDto5.setOrgname("北京1网点");
//        ogDeptDto5.setP_orgcode("110100004");
//        ogDeptDtoList.add(ogDeptDto5);
//
//        OgDeptDto ogDeptDto7 = new OgDeptDto();
//        ogDeptDto7.setOrgcode("110100007");
//        ogDeptDto7.setOrgname("北京1-1网点");
//        ogDeptDto7.setP_orgcode("110100005");
//        ogDeptDtoList.add(ogDeptDto7);
//
//
//        OgDeptDto ogDeptDto9 = new OgDeptDto();
//        ogDeptDto9.setOrgcode("110100009");
//        ogDeptDto9.setOrgname("北京1-1网点");
//        ogDeptDto9.setP_orgcode("110100008");
//        ogDeptDtoList.add(ogDeptDto9);
//
//        OgDeptDto ogDeptDto10 = new OgDeptDto();
//        ogDeptDto10.setOrgcode("1101000010");
//        ogDeptDto10.setOrgname("北京1-1网点");
//        ogDeptDto10.setP_orgcode("110100009");
//        ogDeptDtoList.add(ogDeptDto10);
//
//
//        OgDeptDto ogDeptDto11 = new OgDeptDto();
//        ogDeptDto11.setOrgcode("1101000011");
//        ogDeptDto11.setOrgname("北京1-1网点");
//        ogDeptDto11.setP_orgcode("1101000010");
//        ogDeptDtoList.add(ogDeptDto11);
        String a = "121233/232322/2342/";

        String vb = a.replace("//", "/");

        System.out.println(vb);
    }


}
