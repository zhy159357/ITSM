package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.http.entegorserver.entity.UserSyncMsg;
import com.ruoyi.system.service.*;
import com.ruoyi.system.service.server.SynchronizeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员 信息操作处理
 */

@Controller
@RequestMapping("/system/ogperson")
public class OgPersonController extends BaseController {

    private String prefix = "system/people";


    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgUserPostService ogUserPostService;

    @Autowired
    private SynchronizeUserService synchronizeUserserviceImp;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private IOgGroupPersonService groupPersonService;

    @Autowired
    private ISysDeptService deptService;

    /*
      自定义调接口工作组
     */
   @Autowired
   private ISysWorkService iSysWorkService;


    /**同步用户开关*/
    private boolean synchronizeUser = RuoYiConfig.getSynchronizeUser();

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgPerson ogPerson)
    {
        authControlPerson(ogPerson);
        startPage();
        List<OgPerson> list = ogPersonService.selectOgPersonList(ogPerson);
        return getDataTable(list);

    }

    @PostMapping("/listPerson")
    @ResponseBody
    public TableDataInfo listPerson(OgPerson ogPerson)
    {
        if(StringUtils.isNotEmpty(ogPerson.getOrgId())){
            authControl(ogPerson);
            startPage();
            List<OgPerson> list = ogPersonService.selectOgPersonJqList(ogPerson);
            return getDataTable(list);
        }else {
            authControl(ogPerson);
            startPage();
            List<OgPerson> list = ogPersonService.selectOgPersonList(ogPerson);
            return getDataTable(list);
        }
    }

    @PostMapping("/listPersonListByOrgId")
    @ResponseBody
    public AjaxResult listPersonListByOrgId(String orgId)
    {
        List<OgPerson> ogPeopleList = ogPersonService.selectOgPersonListByOrgId(orgId);
        return AjaxResult.success(ogPeopleList);
    }


    /**
     * 人员管理导出
     * @param ogPerson
     * @return
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgPerson ogPerson) {
        Map<String, Object> params = ogPerson.getParams();
        String currentPage = (String) params.get("currentPage");
        List<OgPerson> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(ogPerson.getOrgId())){
            authControl(ogPerson);
            if ("currentPage".equals(currentPage)) {
                startPage();
            }
            list = ogPersonService.selectOgPersonJqList(ogPerson);
        }else {
            authControl(ogPerson);
            if ("currentPage".equals(currentPage)) {
                startPage();
            }
            list = ogPersonService.selectOgPersonList(ogPerson);
        }
        ExcelUtil<OgPerson> util = new ExcelUtil<>(OgPerson.class);
        return util.exportExcel(list, "人员管理");
    }

    /**
     * 人员管理列表查询｜导出增加机构权限控制，全国中心看所有，省中心看本省
     * @param ogPerson
     */
    public void authControlPerson(OgPerson ogPerson) {
        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        String orgLevCode = ogOrg.getLevelCode();
        //获取二级机构code
        if (ogOrg.getLevelCode().length() > 30) {
            orgLevCode = ogOrg.getLevelCode().substring(0, 30);
        }
        ogPerson.setLevelCode(orgLevCode);
        ////2.全国中心查看所有
        //OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        //if(oneLvOrTwoLv.getLevelCode().contains("/000000000/010000888/") || "/000000000/".equals(oneLvOrTwoLv.getLevelCode())){
        //}else{
        //    //3.省中心只看到本省的
        //    ogPerson.setLevelCode(oneLvOrTwoLv.getLevelCode());
        //}
        //
        //if(StringUtils.isNotEmpty(ogPerson.getOrgId())){
        //    OgOrg org = deptService.selectDeptById(ogPerson.getOrgId());
        //    ogPerson.setOrgId("");
        //    ogPerson.setLevelCode(org.getLevelCode());
        //}
    }

    /**
     * 人员管理列表查询｜导出增加机构权限控制，查看当前机构及下级机构
     * @param ogPerson
     */
    public void authControl(OgPerson ogPerson) {
        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //2.全国中心查看所有
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        if("/000000000/".equals(oneLvOrTwoLv.getLevelCode())){
        }else{
            //3.省中心只看到本省的
            ogPerson.setLevelCode(ogOrg.getLevelCode());
        }

        if(StringUtils.isNotEmpty(ogPerson.getOrgId())){
            OgOrg org = deptService.selectDeptById(ogPerson.getOrgId());
            ogPerson.setOrgId("");
            ogPerson.setLevelCode(org.getLevelCode());
        }
    }

    @PostMapping("/accountList")
    @ResponseBody
    public TableDataInfo accountList(OgPerson ogPerson)
    {
        //获取当前登陆人的机构信息
        //1.获取当前登陆人的二级机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //2.全国中心查看所有
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        if(oneLvOrTwoLv.getLevelCode().contains("/000000000/010000888/") || "/000000000/".equals(oneLvOrTwoLv.getLevelCode())){
        }else{
            //3.省中心只看到本省的
            ogPerson.setLevelCode(oneLvOrTwoLv.getLevelCode());
        }
        startPage();
        List<OgPerson> list = ogPersonService.selectBatchPersonList(ogPerson);
        return getDataTable(list);


    }

    /**
     * 账号管理加载人员信息展示    查询当前登录人本级及下辖机构下的所有人员
     * @param ogPerson
     * @return
     */
    @PostMapping("/accountUserList")
    @ResponseBody
    public TableDataInfo accountUserList(OgPerson ogPerson)
    {
        //获取当前登陆人的机构信息
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        String orgId = person.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        ogPerson.setLevelCode(ogOrg.getLevelCode());
        startPage();
        List<OgPerson> list = ogPersonService.selectBatchPersonList(ogPerson);
        return getDataTable(list);


    }


    @PostMapping("/addlist")
    @ResponseBody
    public TableDataInfo addlist(OgPerson ogPerson)
    {
        startPage();
        List<OgPerson> list = ogPersonService.selectOgPersonAddList(ogPerson);
        return getDataTable(list);
    }

    @RequestMapping()
    public String show(ModelMap modelMap){

        String userName = ShiroUtils.getOgUser().getUsername();
        String tongbuButtonFlag = "0";
        if ("admin".equals(userName)) {
            tongbuButtonFlag = "1";
        }
        modelMap.put("tongbuButtonFlag", tongbuButtonFlag);
        return prefix+"/show";
    }

    /**
     * 新增人员
     */
    @RequestMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    /**
     * 新人人员保存
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgPerson ogPerson){

        ogPerson.setpId(UUID.getUUIDStr());
        ogPerson.setAdder(ShiroUtils.getLoginName());
        ogPerson.setAddtime(DateUtils.dateTimeNow());
        ogPerson.setUpdatetime(DateUtils.dateTimeNow());
        String orgId = ogPerson.getOrgId();
        //设置参与机构
        if (StringUtils.isNotEmpty(orgId)) {

            OgOrg org = deptService.getPartakeOrg(orgId);
            if (null != org) {

                ogPerson.setPartakeOrgId(org.getOrgId());
            }
        }
        return toAjax(ogPersonService.insertOgPerson(ogPerson));
    }

    /**
     * 人员修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, ModelMap modelMap) {
        OgPerson ogPerson =  ogPersonService.selectOgPersonById(id);
        if(StringUtils.isNotNull(ogPerson)){
            modelMap.addAttribute("ogPerson",ogPerson);
        }
        return prefix + "/edit";
    }

    /**
     * 人员修改提交
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSubmit(OgPerson ogPerson){
        OgPerson oldOgPerson =  ogPersonService.selectOgPersonById(ogPerson.getpId());
        String oldPhone=oldOgPerson.getMobilPhone();
        Map<String, Object> map = new HashMap<String, Object>();
        UserSyncMsg msg  =new UserSyncMsg();
        ogPerson.setUpdatetime(DateUtils.dateTimeNow());

        if (StringUtils.isNotEmpty(oldOgPerson.getAgencyPid()) && "0".equals(ogPerson.getAgencySwitch())) {
            ogPersonService.updateAgencyIsNull(ogPerson);
        }

        String orgId = ogPerson.getOrgId();
        //设置参与机构
        if (StringUtils.isNotEmpty(orgId)) {

            OgOrg org = deptService.getPartakeOrg(orgId);
            if (null != org) {

                ogPerson.setPartakeOrgId(org.getOrgId());
            }
        }
        int num=ogPersonService.updateOgPerson(ogPerson);
        if(num==1 && synchronizeUser) {
            OgUser ogUser = new OgUser();
            ogUser.setpId(ogPerson.getpId());
            List<OgUser> ogUsers = ogUserService.selectAccountList(ogUser);
            if (StringUtils.isNotEmpty(ogUsers)) {
                OgUser userPerson = ogUserService.selectUserPersonList(ogUser);
                map.put("userKey", userPerson.getUserId());
                map.put("userName", userPerson.getUsername());
                map.put("state", userPerson.getStatus());
                map.put("department", userPerson.getOrgname());
                map.put("password", "Bosc@1234");
                map.put("email", userPerson.getEmail());
                map.put("telphone", userPerson.getMobilPhone());
                map.put("ideCode", userPerson.getSmsCode());
                map.put("longinName", oldPhone);
                msg.setParams(map);
                synchronizeUser(userPerson, 0, oldPhone,msg);
            }
        }
        return toAjax(num);
    }

    private void synchronizeUser(OgUser user, int type, String loginNames,UserSyncMsg msg) {
        synchronizeUserserviceImp.synchronizeUser(user, type,loginNames,msg);
    }

    /**
     * 启用禁用人员
     */
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(String pId,String status){
        Map<String, Object> map = new HashMap<String, Object>();
        OgPerson ogPerson = null;
        UserSyncMsg msg  =new UserSyncMsg();
        if("0".equals(status)){
            String userId = ogUserService.selectUserIdByPId(pId);
            OgUser ogUser = new OgUser();
            ogUser.setUserId(userId);
            ogUser.setInvalidationMark("0");
            ogUserService.updateOgUser(ogUser);
            //清空账号和岗位的关联信息
            ogUserPostService.deletePostByUserId(userId);
            //清空对应的工作组信息
            groupPersonService.deleteGroupByPersonId(pId);

        }else{
            //当前状态为启用状态则查询当前人员关联的手机号是否有启用的状态
            ogPerson = ogPersonService.selectOgPersonByPidForUpdateStatus(pId);
            OgPerson person = ogPersonService.checkPhone(ogPerson.getMobilPhone());
            if(StringUtils.isNotNull(person)){
                //已存在
                return error("该手机号已有启用状态的数据，无法启用!");
            }

        }
        ogPerson  = new OgPerson();
        ogPerson.setpId(pId);
        ogPerson.setInvalidationMark(status);
        int num = ogPersonService.updateOgPersonStatus(ogPerson);
        if(num==1 && synchronizeUser){
                OgUser ogUser = new OgUser();
                ogUser.setpId(ogPerson.getpId());
                List<OgUser> ogUsers = ogUserService.selectAccountList(ogUser);
                if (StringUtils.isNotEmpty(ogUsers)) {
                    OgUser userPerson = ogUserService.selectUserPersonList(ogUser);
                    map.put("userKey", userPerson.getUserId());
                    map.put("userName", userPerson.getUsername());
                    map.put("state", userPerson.getStatus());
                    map.put("department", userPerson.getOrgname());
                    map.put("password", "Bosc@1234");
                    map.put("email", userPerson.getEmail());
                    map.put("telphone", userPerson.getMobilPhone());
                    map.put("ideCode", userPerson.getSmsCode());
                    map.put("longinName", userPerson.getMobilPhone());
                    msg.setParams(map);
                    //调用同步方法
                    synchronizeUser(userPerson, 0, userPerson.getMobilPhone(),msg);
        }
        }
        return toAjax(num);
    }

    /**
     * 响应请求分页数据
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 设置请求分页数据
     */
    @Override
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 人员管理查询工作组列表页面
     */
    @GetMapping("/selectGroup/{userId}")
    public String selectGroup(@PathVariable("userId") String userId, ModelMap mmap)
    {
        mmap.put("userId",userId);
        return prefix + "/selectGroup";
    }

    /**
     * 根据userId查询工作组列表
     */
    @PostMapping("/selectGroupByUserId")
    @ResponseBody
    public TableDataInfo selectGroupByUserId(String userId)
    {
        startPage();
        List<OgGroup> list = workService.selectGroupByUserId(userId);
        return getDataTable(list);
    }

    /**
     * 根据工作组和角色Id查询人员
     * @param ogPerson
     * @return
     */
    @PostMapping("/selectPersonByGroupIdAndRoleId")
    @ResponseBody
    public AjaxResult selectPersonByGroupIdAndRoleId(OgPerson ogPerson){
        AjaxResult ajaxResult = new AjaxResult();
        List<OgPerson> ogPeople = ogPersonService.selectPersonByGroupIdAndRoleId(ogPerson);
        ajaxResult.put("data",ogPeople);
        return ajaxResult;
    }

    @PostMapping("/selectPersonByPostId")
    @ResponseBody
    public AjaxResult selectPersonByPostId(String postId){
        AjaxResult ajaxResult = new AjaxResult();
        Map<String,String> map = new HashMap<>();
        map.put("postId",postId);
        List<OgPerson> ogPeople = ogPersonService.selectPersonByPostId(map);
        ajaxResult.put("data",ogPeople);
        return ajaxResult;
    }

    @PostMapping("/selectListByGroupIdAndRoleId")
    @ResponseBody
    public TableDataInfo selectListByGroupIdAndRoleId(OgPerson ogPerson)
    {
        startPage();
        ogPerson.getParams().put("levelCode", "/000000000/010000888/");
        List<OgPerson> list = ogPersonService.selectPersonByGroupIdAndRoleId(ogPerson);
        return getDataTable(list);
    }

    @PostMapping("/selectList")
    @ResponseBody
    public TableDataInfo selectList(OgPerson ogPerson)
    {
        if("null".equals(ogPerson.getOrgId())){
            ogPerson.setOrgId(null);
        }
        startPage();
        List<OgPerson> list = ogPersonService.selectList(ogPerson);
        return getDataTable(list);
    }


    /**
     * 给当前账号指定人员信息
     * @return
     */
    @GetMapping("/selectUser")
    public String selectUser()
    {
        return prefix + "/selectUser";
    }

    /**
     * 添加时检验手机号是否唯一
     * @param mobilPhone
     * @return
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(String mobilPhone){
        OgPerson ogPerson = ogPersonService.checkPhoneUnique(mobilPhone);
        //如果当前手机号已经存在 判断是启用还是禁用1.如果启用不可添加 2.如果禁用可以添加
        if(StringUtils.isNotNull(ogPerson)){
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }else{
            return UserConstants.USER_PHONE_UNIQUE;
        }
    }

    /**
     * 修改时如果当前手机号不变
     * @param pId
     * @param mobilPhone
     * @return
     */
    @PostMapping("/checkEditPhoneUnique")
    @ResponseBody
    public String checkEditPhoneUnique(String pId,String mobilPhone){
        OgPerson person = ogPersonService.selectOgPersonById(pId);
        if(null!=person.getMobilPhone() && person.getMobilPhone().equals(mobilPhone)){
            return UserConstants.USER_PHONE_UNIQUE;
        }
        OgPerson ogPerson = ogPersonService.checkPhoneUnique(mobilPhone);
        //如果当前手机号已经存在 判断是启用还是禁用1.如果启用不可添加 2.如果禁用可以添加
        if(StringUtils.isNotNull(ogPerson)){
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }else{
            return UserConstants.USER_PHONE_UNIQUE;
        }
    }

    /**
     * 检验邮箱唯一
     * @param mobilPhone
     * @return
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public Boolean checkEmailUnique(String mobilPhone){
        OgPerson ogPerson = ogPersonService.checkPhoneUnique(mobilPhone);
        if(ogPerson==null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 检验身份证唯一
     * @param mobilPhone
     * @return
     */
    @PostMapping("/checkIdCardUnique")
    @ResponseBody
    public Boolean checkIdCardUnique(String mobilPhone){
        OgPerson ogPerson = ogPersonService.checkPhoneUnique(mobilPhone);
        if(ogPerson==null){
            return true;
        }else{
            return false;
        }
    }



    @PostMapping("/selectOgPersonByOrgLevAndPostIds")
    @ResponseBody
    public TableDataInfo selectOgPersonByOrgLevAndPostIds(OgPerson ogPerson)
    {
        //获取机构信息
        String levelCode = sysDeptService.selectDeptById(ogPerson.getOrgId()).getLevelCode();
        List<String> postIds = new ArrayList();
        // 岗位id为数据中心人员｜数据中心领导｜科技部人员｜科技部领导
        postIds.add("0015");
        postIds.add("0016");
        postIds.add("0010");
        postIds.add("0011");
        ogPerson.getParams().put("postIds",postIds);
        ogPerson.getParams().put("levelCode",levelCode);
        startPage();
        List<OgPerson> list = ogPersonService.selectOgPersonByOrgLevAndPostIds(ogPerson);
        return getDataTable(list);
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
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        if(oneLvOrTwoLv.getLevelCode().contains("/000000000/010000888/") || ("/000000000/".equals(oneLvOrTwoLv.getLevelCode()))) {
        }else{
            org.setLevelCode(oneLvOrTwoLv.getLevelCode());
        }

        List<Ztree> ztrees = deptService.selectDeptTree(org);
        return ztrees;
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeDataPerson")
    @ResponseBody
    public List<Ztree> treeDataPerson() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        if(("/000000000/".equals(oneLvOrTwoLv.getLevelCode()))) {
        }else{
            org.setLevelCode(ogOrg.getLevelCode());
        }
        List<Ztree> ztrees = deptService.selectDeptTree(org);
        return ztrees;
    }

    /**
     * CMDB同步用户
     */
    @PostMapping("/tongbuUser")
    @ResponseBody
    public AjaxResult tongbuUser() {

        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> map = ogPersonService.syncPortalData();
        ajaxResult.put("msg", map.get("msg"));
        ajaxResult.put("num", map.get("num"));
        return ajaxResult;
    }

    /**
     * 查询全部人员信息 页面跳转
     * @return
     */
    @GetMapping("/selectAgencyPerson")
    public String selectAgencyPersong()
    {
        return prefix + "/selectAgencyPerson";
    }

    @PostMapping("/selectAgencyPersonList")
    @ResponseBody
    public TableDataInfo selectAgencyPersonList(OgPerson ogPerson) {

        startPage();
        List<OgPerson> list = ogPersonService.selectOgPersonList(ogPerson);
        return getDataTable(list);
    }

    /**
     * 同步参与机构
     */
    @PostMapping("/tongbupartakeID")
    @ResponseBody
    public AjaxResult tongbupartakeID() {

        AjaxResult ajaxResult = new AjaxResult();
        OgPerson ogPerson = new OgPerson();
        Map<String, Object> map = ogPersonService.setPersonJoinOrgId(ogPerson);
        ajaxResult.put("msg", map.get("msg"));
        ajaxResult.put("num", map.get("num"));
        return ajaxResult;
    }
}
