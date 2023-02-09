package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.CommonTree;
import com.ruoyi.form.service.ICommonTreeService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;
import com.ruoyi.system.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作组管理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work")
public class SysWorkController extends BaseController {
    private String prefix = "system/work";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;


    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ICommonTreeService commonTreeService;

    public static   Map<String,List<String>> ADPMMap=new HashMap<>();
    ////////////////////////
    /**
     * 获取所有配置应用系统的工作组信息
     */
    @PostMapping("/getadpmid")
    @ResponseBody
    public AjaxResult getadpmid() {
        AjaxResult ajaxResult = new AjaxResult();
        String returnStr="";
        try {
            ADPMMap=new HashMap<>();
            List<OgGroup> list = workService.selectOgGroupListAll(new OgGroup());
            Map<String, List<String> > groupMap=new HashMap<>();

            for(int i=0;i<list.size();i++) {
                List<String> pasoList=new  ArrayList<String>();
                String cpdestr = "";
                OgGroup entity = list.get(i);
                if (entity.getSysId() != null) {
                    String[] reList = entity.getSysId().split(",");
                    // 根据sysid查找paso号,
                    for (int j = 0; j < reList.length; j++) {
                        System.out.println("sysId-------------->"+reList[j]);
                        OgSys ogSys = applicationManagerService.selectOgSysBySysId(reList[j]);
                        if (ogSys !=null  && ogSys.getCode() != null) {
                            pasoList.add(ogSys.getCode());
                        }
                    }
                }
                ADPMMap.put(entity.getGroupId(), pasoList);
                returnStr = returnStr + entity.getGrpName() + ",";
            }
        } catch (Exception e) {
            System.out.println("===============================");
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("=================================");
        }
        ajaxResult.put("msg", returnStr);

        return ajaxResult;

    }

    /**
     * 根据paso号,重构sysid
     */
    @PostMapping("/resetsydid")
    @ResponseBody
    public AjaxResult resetsydid() {
        AjaxResult ajaxResult = new AjaxResult();
        String returnStr="";
        try {
            for(Map.Entry<String,List<String>>  entity :ADPMMap.entrySet()){
                String grpid=entity.getKey();
                System.out.println("grpid------->"+grpid);
                List<String> pasolist=entity.getValue();
                String  sysidStr="";
                if(pasolist !=null ){
                    for(int i=0;i<pasolist.size();i++){
                        String str=pasolist.get(i);
                        // 根据paso号(code)查询
                        OgSys sys = applicationManagerService.selectOgSysBySysCode(str);
                        if (null != sys) {
                            sysidStr=sysidStr+sys.getSysId()+",";
                        }
                    }
                }
                if(!sysidStr.equals("")){
                    sysidStr=sysidStr.substring(0,sysidStr.length()-1);
                }
                // 根据grpid根据sysid字段
                OgGroup group = workService.selectOgGroupById(grpid);
                if (group != null) {
                    group.setSysId(sysidStr);
                    workService.updateOgGroup(group);

                }else{
                    System.out.println("===========没有找到数据 ===================="+grpid);
                }

            }
        } catch (Exception e) {
            System.out.println("===============================");
            e.printStackTrace();
            ajaxResult.put("msg", "同步失败");
            System.out.println("=================================");
        }
        ajaxResult.put("msg", "同步成功");

        return ajaxResult;

    }
    //////////////////////////

    @GetMapping()
    public String work() {
        return prefix + "/work";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgGroup group) {

        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //2.全国中心查看所有
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        if (oneLvOrTwoLv.getLevelCode().contains("/000000000/010000888/") || "/000000000/".equals(oneLvOrTwoLv.getLevelCode())) {

        } else {
            //3.省中心只看到本省的
            group.setLevelCode(oneLvOrTwoLv.getLevelCode());
        }
        group.setOrgId(resetOrgId(group.getOrgId(), group.getOrgName()));
        List<OgGroup> list = workService.selectOgGroupList(group);
        List<OgGroup> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (OgGroup gp : list) {
                if (StringUtils.isNotEmpty(gp.getSysId())) {
                    if (gp.getSysId().contains(",")) {
                        gp.setSysId("多系统");
                    } else {
                        OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(gp.getSysId());
                        if (ogSys != null) {
                            String caption = ogSys.getCaption();
                            if (StringUtils.isNotEmpty(caption)) {
                                gp.setSysId(caption);
                            }
                        }

                    }
                }
                list2.add(gp);
            }
        }
        return getDataTable_ideal(list2);
    }

    @PostMapping("/listWork")
    @ResponseBody
    public TableDataInfo listWork(OgGroup group) {

        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        /*OgOrg ogOrg = deptService.selectDeptById(orgId);
        //2.全国中心查看所有
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        //if(oneLvOrTwoLv.getLevelCode().contains("/000000000/010000888/") || "/000000000/".equals(oneLvOrTwoLv.getLevelCode())) {
        if ("/000000000/".equals(oneLvOrTwoLv.getLevelCode())) {

        } else {
            //3.省中心只看到本省的
            group.setLevelCode(ogOrg.getLevelCode());
        }*/
        group.setOrgId(resetOrgId(group.getOrgId(), group.getOrgName()));
        List<OgGroup> list = workService.selectOgGroupList(group);
        List<OgGroup> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (OgGroup gp : list) {
                if (StringUtils.isNotEmpty(gp.getSysId())) {
                    if (gp.getSysId().contains(",")) {
                        gp.setSysId("多系统");
                    } else {
                        OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(gp.getSysId());
                        if (ogSys != null) {
                            String caption = ogSys.getCaption();
                            if (StringUtils.isNotEmpty(caption)) {
                                gp.setSysId(caption);
                            }
                        }

                    }
                }
                list2.add(gp);
            }
        }
        return getDataTable_ideal(list2);
    }

    @Log(title = "工作组管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "工作组数据");
    }

    @Log(title = "工作组管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("工作组数据");
    }

    /**
     * 新增工作组
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return prefix + "/add";
    }

    /**
     * 新增保存工作组
     */
    @Log(title = "工作组管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgGroup group) {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(workService.checkGroupNameUnique(group.getGrpName()))) {
            return error("新增工作组'" + group.getGrpName() + "'失败，工作组已存在");
        }
        String sysId = group.getSysId();
        if (StringUtils.isNotEmpty(sysId) && 1 == group.getInvalidationMark()) {//判断该工作组对应的系统是否已经存在对应的分类组中
            OgGroup gp = new OgGroup();
            gp.setGroupType(group.getGroupType());
            String s[] = sysId.split(",");
            for (int i = 0; i < s.length; i++) {
                gp.setSysId(s[i]);
                gp.setInvalidationMark(1L);
                List<OgGroup> list = workService.selectOgGroupList(gp);
                if (list.size() > 0) {
                    return error("选择的系统已存在其他工作组内。");
                }
            }
        }
        group.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(workService.insertOgGroup(group));
    }

    /**
     * 修改工作组
     */
    @GetMapping("/edit/{groupId}")
    public String edit(@PathVariable("groupId") String groupId, ModelMap mmap) {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/edit";
    }

    @GetMapping("/editG/{groupId}")
    public String editG(@PathVariable("groupId") String groupId, ModelMap mmap) {
        OgGroup ogGroup = workService.selectOgGroupById(groupId);
        if (ogGroup != null) {
            mmap.put("group", ogGroup);
        }
        return prefix + "/editleader";
    }

    /**
     * 修改保存工作组
     */
    @Log(title = "工作组管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgGroup group) {
        String sysId = group.getSysId();
        if (StringUtils.isNotEmpty(sysId) && 1 == group.getInvalidationMark()) {//判断该工作组对应的系统是否已经存在对应的分类组中
            OgGroup gp = new OgGroup();
            gp.setGroupType(group.getGroupType());
            String s[] = sysId.split(",");
            for (int i = 0; i < s.length; i++) {
                gp.setSysId(s[i]);
                gp.setInvalidationMark(1L);
                List<OgGroup> list = workService.selectOgGroupList(gp);
                if (!list.isEmpty()) {
                    for (OgGroup gps : list) {
                        if (!gps.getGroupId().equals(group.getGroupId())) {//存在不等于当前修改的工作组
                            return error("选择的系统已存在其他工作组内。");
                        }
                    }
                }
            }
        }
        group.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(workService.updateOgGroup(group));
    }

    @Log(title = "工作组管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(workService.deleteOgGroupByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验工作组名称
     */
    @PostMapping("/checkGrpNameUnique")
    @ResponseBody
    public String checkPhoneUnique(OgGroup group) {
        return workService.checkGroupNameUnique(group.getGrpName());
    }

    /**
     * 工作组状态修改
     */
    @Log(title = "工作组管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user) {
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }

    /**
     * 增加工作组成员
     */
    @GetMapping("/addGroupPerson/{groupId}")
    public String addGroupPerson(@PathVariable("groupId") String groupId, ModelMap mmap) {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/groupPerson";
    }

    /**
     * 保存工作组成员
     */
    @PostMapping("/groupPerson/add/{groupId}")
    public String addGroupPersonSave(@PathVariable("groupId") Long groupId) {
        return prefix + "/addGroupPerson";
    }

    @PostMapping("/groupPerson/groupPersonList")
    @ResponseBody
    public TableDataInfo groupPersonList(OgGroupPerson person) {
        startPage();
        person.setOrgId(resetOrgId(person.getOrgId(), person.getOrgName()));
        List<OgGroupPerson> list = workService.selectOgGroupPersonList(person);
        return getDataTable(list);
    }

    /**
     * 用户选择页面
     */
    @GetMapping("/groupPerson/selectUser/{groupId}")
    public String selectUser(@PathVariable("groupId") String groupId, ModelMap mmap) {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/selectUser";
    }

    /**
     * 查询用户列表
     */
    @PostMapping("/groupPerson/userList")
    @ResponseBody
    public TableDataInfo userList(OgPerson user) {
        startPage();
        List<OgPerson> list = workService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 保存用户信息到工作成员
     */
    @PostMapping("/groupPerson/selectAll")
    @ResponseBody
    public AjaxResult saveGroupPerson(String groupId, String pIds) {
        return toAjax(workService.insertOgGroupPerson(groupId, pIds));
    }

    /**
     * 修改工作组成员信息（工作组职位、组内顺序）
     */
    @GetMapping("/groupPerson/editGroupPerson/{pid}")
    public String editGroupPerson(@PathVariable("pid") String pid, ModelMap mmap) {
        mmap.put("person", workService.selectOgGroupPersonById(pid));
        return prefix + "/editGroupPerson";
    }

    /**
     * 修改保存工作组管理成员信息
     */
    @Log(title = "工作组管理成员信息", businessType = BusinessType.UPDATE)
    @PostMapping("/groupPerson/editGroupPerson")
    @ResponseBody
    public AjaxResult editSaveGroupPerson(@Validated OgGroupPerson person) {
        person.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(workService.updateOgGroupPerson(person));
    }

    @Log(title = "工作组管理成员信息", businessType = BusinessType.DELETE)
    @PostMapping("/groupPerson/removeGroupPerson")
    @ResponseBody
    public AjaxResult removeGroupPerson(String ids) {
        try {
            return toAjax(workService.deleteOgGroupPersonByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PostMapping("/selectGroupByUserId")
    @ResponseBody
    public TableDataInfo selectGroupByUserId(String grpName) {
        String userId = ShiroUtils.getUserId();
        OgUser ogUser = ogUserService.selectOgUserByUserId(userId);
        String pId = ogUser.getpId();
        startPage();
        Map map = new HashMap<>();
        map.put("userId", pId);
        map.put("grpName", grpName);
        map.put("gpOsition", "1");
        map.put("invalidationMark", "1");
        List<OgGroup> list = workService.selectGroupByGposition(map);
        return getDataTable(list);
    }


    //我的运行事件单新增方法获取人员信息
    @PostMapping("/groupPerson/personListByGroupId")
    @ResponseBody
    public AjaxResult personListByGroupId(OgGroupPerson person) {
        AjaxResult ajaxResult = new AjaxResult();
        List<OgGroupPerson> list = workService.selectOgGroupPersonList(person);
        ajaxResult.put("data", list);
        return ajaxResult;
    }


    @PostMapping("/selectGroupBySysId")
    @ResponseBody
    public AjaxResult selectGroupBySysId(String sysId) {
        AjaxResult ajaxResult = new AjaxResult();
        List<OgGroup> ogGroups = workService.selectGroupBySysId(sysId);
        ajaxResult.put("data", ogGroups);
        return ajaxResult;
    }

    /**
     * 修改工作组信息打开页面根据工作组ID加载对应系统信息
     *
     * @param groupId
     * @return
     */
    @PostMapping("/getSysList/{groupId}")
    @ResponseBody
    public TableDataInfo getSysList(@PathVariable("groupId") String groupId) {
        List<OgSys> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(groupId)) {
            OgGroup group = workService.selectOgGroupById(groupId);
            if (group != null) {
                if (StringUtils.isNotEmpty(group.getSysId())) {
                    String s[] = group.getSysId().split(",");
                    for (int i = 0; i < s.length; i++) {
                        OgSys sys = sysApplicationManagerService.selectOgSysBySysId(s[i]);
                        if (sys != null) {
                            list.add(sys);
                        }
                    }
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 点击添加按钮打开系统列表页面
     *
     * @param mmap
     * @return
     */
    @GetMapping("/viewSys")
    public String viewSys(ModelMap mmap) {
        return prefix + "/viewSys";
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


    @PostMapping("/syslist")
    @ResponseBody
    public TableDataInfo list(OgSys sys) {
        // 查询外围系统时需要全部展示有多选isPage为2不分页
        if (!"2".equals(sys.getIsPage())) {
            // 只有应用系统管理列表查询时才需要控制权限
            if ("applicationList".equals(sys.getRemark())) {
                OgPerson person = personService.selectOgPersonById(ShiroUtils.getUserId());
                OgOrg org = deptService.selectDeptById(person.getOrgId());
                // 判断当前登录人是否全国中心，是全国中心默认查询所有，不是全国中心使用层级码like查询
                if (!("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/"))) {
                    OgOrg ogOrg = new OgOrg();
                    ogOrg.setLevelCode(org.getLevelCode());
                    sys.setOgOrg(ogOrg);
                }
            }
            startPage();
        }
        sys.setOrgId(resetOrgId(sys.getOrgId(), sys.getOrgName()));
        List<OgSys> list = applicationManagerService.selectOgSysListWork(sys);
        return getDataTable(list);
    }

    /**
     * 所属应用系统选择页面
     *
     * @return
     */
    @GetMapping("/sysGrouplist")
    public String selectOneApplication() {
        return prefix + "/selectApplication";
    }



    @GetMapping("/test")
    @ResponseBody
    public  List<LabelValue> test(String groupName) {
        List<LabelValue> labelValues = workService.selectOgGroupPersonListByGroupName(groupName);
        return labelValues;
    }

    /**
     * 事件单受派组
     * @param orgId
     * @return
     */
    @PostMapping("/incident/selectAllGroup")
    @ResponseBody
    @ApiOperation(value = "加载受派组")
    public AjaxResult listOgPerson(@RequestBody Map<String, String> params) {
        /*OgGroup ogGroup=new OgGroup();
        ogGroup.setOrgId(orgId);
        ogGroup.setGroupType("1");// 查询一线工作组
        List<OgGroup> list = workService.selectOgGroupList(ogGroup);
        List<Map<String, String>> objects = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(OgGroup og:list){
                Map<String, String> map = new HashMap<>();
                map.put("value", og.getGroupId());
                map.put("label", og.getGrpName());
                objects.add(map);
            }
        }
        return AjaxResult.success(objects);*/
        List<Map<String, String>> collect = new ArrayList<>();
        List<CommonTree> commonTrees = commonTreeService.selectCommonTreeByOrgFlag(params.get("orgFlag"));
        if(!CollectionUtils.isEmpty(commonTrees)) {
            collect = commonTrees.stream().map(tree -> {
                Map<String, String> map = new HashMap<>();
                map.put("value", tree.getOgId());
                map.put("label", tree.getName());
                return map;
            }).filter(tree -> {return StringUtils.isNotEmpty(tree.get("value"));}).collect(Collectors.toList());
        }
        return AjaxResult.success(collect);
    }
    /**
     * 事件单受派组受派人查询
     * @param groupId
     * @return
     */
    @PostMapping("/incident/selectGroupUsers")
    @ResponseBody
    @ApiOperation(value = "加载受派组人员")
    public AjaxResult selectGroupUsers(@RequestBody JSONObject jsonObject ) {
        logger.info("=====groupId :"+jsonObject.get("groupId"));
        if(StringUtils.isEmpty(jsonObject.get("groupId"))) {
            return AjaxResult.error("请先选择受派组！");
        }
        /*OgGroupPerson ogGroupPerson=new OgGroupPerson();
        ogGroupPerson.setGroupId(jsonObject.get("groupId").toString());
        List<OgGroupPerson> list = workService.selectOgGroupPersonList(ogGroupPerson);
        List<Map<String, String>> objects = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(OgGroupPerson og:list){
                Map<String, String> map = new HashMap<>();
                map.put("value", og.getPerson().getpId());
                map.put("label", og.getPerson().getpName());
                objects.add(map);
            }
        }*/
        String groupId = (String) jsonObject.get("groupId");
        OgOrg ogOrg = deptService.selectDeptById(groupId);
        List<OgPerson> personList = new ArrayList<>();
        if(ogOrg != null) {
            /*String orgId = ogOrg.getOrgId();
            if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_BEIJING)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_BEIJING;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_TIANJING)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_TIANJIN;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_HANGZHOU)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_HANGZHOU;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_NANJING)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_NANJING;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_TIANJING)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_TIANJIN;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_SUZHOU)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_SUZHOU;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_CHENGDU)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_CHENGDU;
            } else if(ogOrg.getLevelCode().contains(CommonDeptCodeConstants.BRANCH_LEVEL_CODE_SHENZHEN)) {
                orgId = CommonDeptCodeConstants.BRANCH_DEPT_CODE_SHENZHEN;
            }*/
            OgPerson ogPerson = new OgPerson();
            ogPerson.setLevelCode(ogOrg.getLevelCode());
            personList = ogPersonService.selectPersonListByLevelCode(ogPerson);
        } else {
            OgGroup ogGroup = workService.selectOgGroupById(groupId);
            if(ogGroup != null) {
                OgGroupPerson ogGroupPerson=new OgGroupPerson();
                ogGroupPerson.setGroupId(groupId);
                List<OgGroupPerson> list = workService.selectOgGroupPersonList(ogGroupPerson);
                if(!CollectionUtils.isEmpty(list)) {
                    personList = list.stream().map(p -> {
                        OgPerson person = new OgPerson();
                        person.setpId(p.getPerson().getpId());
                        person.setpName(p.getPerson().getpName());
                        person.setUsername(p.getPerson().getUsername());
                        return person;
                    }).collect(Collectors.toList());
                }
            } else {
                OgPerson person = new OgPerson();
                person.setrId(groupId);
                personList = ogPersonService.selectListByRoleId(person);
            }
        }
        if(CollectionUtils.isEmpty(personList)) {
            return AjaxResult.error("该受派组下尚未配置人员信息！");
        }
        /*String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();*/
        List<Map<String, String>> collect = personList.stream().map(person -> {
            Map<String, String> map = new HashMap<>();
            map.put("value", person.getpId());
            if(StringUtils.isNotEmpty(person.getpName()) && StringUtils.isNotEmpty(person.getUsername())) {
                map.put("label", person.getpName() + "@" + person.getUsername());
            }
            return map;
        }).filter(p -> {
            return StringUtils.isNotEmpty(p.get("label"))/* && !userId.equals(p.get("value"))*/;
        }).collect(Collectors.toList());
        return AjaxResult.success(collect);
    }




}