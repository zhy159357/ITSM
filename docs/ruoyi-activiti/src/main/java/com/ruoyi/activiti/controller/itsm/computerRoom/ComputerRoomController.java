package com.ruoyi.activiti.controller.itsm.computerRoom;

import com.ruoyi.activiti.domain.ComputerModule;
import com.ruoyi.activiti.domain.ComputerRoom;
import com.ruoyi.activiti.service.IComputerRoomService;
import com.ruoyi.activiti.service.IDutySchedulingService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ruoyi.activiti.constants.VersionStatusConstants.QGZX_ORGID;


@Controller
@RequestMapping(value = "computerRoom/apply")
public class ComputerRoomController extends BaseController {

    private String prefix = "computerRoom";

    private String prefixClass = "computerRoom/classModule";

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private IComputerRoomService iComputerRoomService;

    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IDutySchedulingService dutySchedulingService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    /**
     * 公共页面路径前缀
     */
    private String prefix_common = "common";


    /**
     * 机房出入-申请页面列表页
     * @param mmap
     * @return
     */
    @GetMapping()
    public String apply(ModelMap mmap) {


        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);
        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        mmap.put("moduleList", moduleList);
        mmap.put("userlist", userlist);
        mmap.addAttribute("user", ShiroUtils.getOgUser().getpId());
        return prefix + "/apply";
    }




    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo ComputerRoomApplylist(ComputerRoom computerRoom) {
        //TODO  根据岗位权限查看所以还是个人，进行查询集合
        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //如果岗位不是管理员和处长，则查询对应登录用户的自己的数据
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }

        startPage();
        List<ComputerRoom> computerRoomList = iComputerRoomService.computerRoomApplylist(computerRoom);

        return getDataTable(computerRoomList);
    }

    /**
     * 创建机构选择页面
     *
     * @return
     */
    @GetMapping("/selectogOrg")
    public String selectogOrg() {
        return prefix + "/subpage/selectOgorg";
    }

    /**
     * 查询机构
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org) {
        startPage();
        List<OgOrg> list = iSysDeptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * 创建陪同人员选择页面
     *
     * @return
     */
    @GetMapping("/selectAccompanyUser")
    public String selectAccompanyUser() {
        return prefix + "/subpage/selectAccompanyUser";
    }


    /**
     * 查询人员
     */
    @PostMapping("/selectUserList")
    @ResponseBody
    public TableDataInfo selectUserList(OgPerson person) {
        startPage();

        //查询丰台-动力一处和合肥的动力二处人员
        List<OgPerson> list = iComputerRoomService.selectUserList(person);

        return getDataTable(list);
    }


    /**
     * 创建工作内容选择页面
     *
     * @return
     */
    @GetMapping("/selectWorkContent")
    public String selectWorkContent() {
        return prefix + "/subpage/selectWorkContent";
    }

    /**
     * 创建机房模块页面
     *
     * @return
     */
    @GetMapping("/selectComputerRoomModule/{computerCenter}")
    public String selectWorkContent(@PathVariable("computerCenter") String computerCenter) {
        ModelMap mmap = new ModelMap();
        mmap.put("computerCenter",computerCenter);

        return prefix + "/subpage/selectComputerRoomModule";
    }

    /**
     * 查询工作内容
     */
    @PostMapping("/workContent")
    @ResponseBody
    public TableDataInfo workContent() {
        startPage();

        //查询工作内容
        List<ComputerRoom> list = iComputerRoomService.workContent();

        return getDataTable(list);
    }

    /**
     * 应用系统选择页面
     *
     * @return
     */
    @GetMapping("/selectApplication")
    public String selectApplication() {
        return prefix_common + "/selectApplication";
    }


    /**
     * 根据中心查询机房模块列表
     * @param moduleId
     * @return
     */
    @PostMapping("/selectComputerModuleForCenter/{computerCenter}")
    @ResponseBody
    public TableDataInfo selectComputerModuleForCenter(@PathVariable("computerCenter") String moduleId){
        startPage();
        ComputerModule computerModule = new ComputerModule();
        computerModule.setParentId(moduleId);
        computerModule.setStatus("0");
        List<ComputerModule> computerModuleList = iComputerRoomService.selectComputerModuleList(computerModule);

        return getDataTable(computerModuleList);
    }

    /**
     * 点击新建打开页面
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {

        //申请人以及其处室
        //打开新建页面回显创建机构填报人及创建机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonEvoById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());

        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        if(!"1".equals(status)){
            // OgPerson auditors = getAuditors();
            // mmap.put("auditorsId",auditors.getpId());
            // mmap.put("auditorsName",auditors.getpName());
            List<OgPerson> auditorsList = getAuditorsList();
            mmap.put("auditorsList",auditorsList);
        }


        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        //获取编码
        String bizType = "JF";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);

        //打开新建页面回显
        mmap.addAttribute("computerApplyNo", bizType + nowDateStr + numStr);
        mmap.put("userlist", userlist);
        mmap.put("occurrenceOrgName", org.getOrgName());
        mmap.put("occurrenceOrgId", org.getOrgId());
        mmap.put("applyUserId", person.getpId());
        mmap.put("loginName", person.getpName());
        mmap.put("status", status);
        mmap.put("moduleList",moduleList);

        return prefix + "/start";
    }

    /**
     * 新增暂存
     */
    @PostMapping("/saveTS")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveTS(@Validated ComputerRoom computerRoom) {

        if(computerRoom == null){
            return AjaxResult.error("传参为空");
        }

        String id = computerRoom.getId();
        if (StringUtils.isEmpty(id)) {
            computerRoom.setId(UUID.getUUIDStr());
            int resultRow = iComputerRoomService.insertTsComputerRoomApply(computerRoom);

        }

        return AjaxResult.success("操作成功", computerRoom.getId());
    }

    /**
     * 新增保存
     */
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(@Validated ComputerRoom computerRoom) {

        if(computerRoom == null){
            return AjaxResult.error("传参为空");
        }

        String id = computerRoom.getId();
        if (StringUtils.isEmpty(id)) {
            computerRoom.setId(UUID.getUUIDStr());
            int resultRow = iComputerRoomService.insertComputerRoomApply(computerRoom);

        }

        return AjaxResult.success("操作成功", computerRoom.getId());
    }


    /**
     * 修改-页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        List<Map> intoList = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }


        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());
        if(!"1".equals(status) && !"2".equals(status)){
            List<OgPerson> auditorsList = getAuditorsList();
            mmap.put("auditorsList",auditorsList);
        }

        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);
        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        //打开新建页面回显
        mmap.put("state", status);
        mmap.put("userlist", userlist);
        mmap.put("intoList", intoList);
        mmap.put("belongingsList", belongingsList);
        mmap.put("computerRoom", computerRoom);
        mmap.put("status", computerRoom.getApplyState());
        mmap.put("moduleList",moduleList);
        return prefix + "/edit";
    }

    /**
     * 保存-修改
     * @param computerRoom
     * @return
     */
    @PostMapping("/saveEdit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveEdit(ComputerRoom computerRoom){

        if(computerRoom == null){
            return AjaxResult.error("传参为空");
        }
        int resultRow =  iComputerRoomService.upadateComputerRoomApply(computerRoom);

        return AjaxResult.success("修改机房出入申请操作成功");
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        List<OgPerson> auditorsList = getAuditorsList();
        mmap.put("auditorsList",auditorsList);


        //打开新建页面回显
        mmap.put("userlist", userlist);
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("computerRoom", computerRoom);
        mmap.put("moduleList",moduleList);
        return prefix + "/detail";
    }


    //获取处长审核人
    private OgPerson getAuditors(){
        //获取当前登录人机构下所有人
        List<OgPerson> list = iComputerRoomService.getOgPersonList(ShiroUtils.getOgUser().getUserId());
        OgUser ogUser = new OgUser();
        ogUser.setPostId(Long.valueOf(PostConstants.SJZX_CZ));
        //获取处长岗位下所以人
        List<OgUser> userList = userService.selectAllocatedListPost(ogUser);

        for(int i=0; i<userList.size(); i++){
            for (int j=0; j<list.size(); j++){
                if(list.get(j).getpId().equals(userList.get(i).getpId())){
                    return list.get(j);
                }
            }
        }
        return null;
    }

    private List<OgPerson> getAuditorsList(){

        List<OgPerson> auditorsList = new ArrayList<>();
        //获取当前登录人机构下所有人
        List<OgPerson> list = iComputerRoomService.getOgPersonList(ShiroUtils.getOgUser().getUserId());
        OgUser ogUser = new OgUser();
        ogUser.setPostId(Long.valueOf(PostConstants.SJZX_CZ));
        //获取处长岗位下所以人
        List<OgUser> userList = userService.selectAllocatedListPost(ogUser);

        for(int i=0; i<userList.size(); i++){
            for (int j=0; j<list.size(); j++){
                if(list.get(j).getpId().equals(userList.get(i).getpId())){
                    auditorsList.add(list.get(j));
                }
            }
        }


        return auditorsList;
    }


    /**
     * 申请页面-导出
     * @param computerRoom
     * @return
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ComputerRoom computerRoom) {

        String isCurrentPage = (String) computerRoom.getParams().get("currentPage");

        //TODO  根据岗位权限查看所以还是个人，进行查询集合
        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //如果岗位不是管理员和处长，则查询对应登录用户的自己的数据
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<ComputerRoom> list = iComputerRoomService.computerRoomApplylist(computerRoom);
        ExcelUtil<ComputerRoom> util = new ExcelUtil<>(ComputerRoom.class);
        return util.exportExcel(list, "机房出入申请查看");
    }

    /**
     * 登记页面-导出
     * @param computerRoom
     * @return
     */
    @PostMapping("/exportRegisterList")
    @ResponseBody
    public AjaxResult exportRegisterList(ComputerRoom computerRoom) {

        String isCurrentPage = (String) computerRoom.getParams().get("currentPage");
        //TODO  根据岗位权限查看所以还是个人，进行查询集合
        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //如果岗位不是管理员和处长，则查询对应登录用户的自己的数据
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<ComputerRoom> list = iComputerRoomService.selectComputerRoomRegisterList(computerRoom);
        ExcelUtil<ComputerRoom> util = new ExcelUtil<>(ComputerRoom.class);
        return util.exportExcel(list, "机房出入登记查看");
    }


    /*
     * 审核-列表页面
     */
    @GetMapping("/audit")
    public String audit(ModelMap mmap) {

        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);
        mmap.put("userlist", userlist);
        mmap.addAttribute("user", ShiroUtils.getOgUser().getpId());
        return prefix + "/audit";
    }


    /**
     * 审核列表集合信息
     * @param computerRoom
     * @return
     */
    @PostMapping("/auditlist")
    @ResponseBody
    public TableDataInfo ComputerRoomAuditlist(ComputerRoom computerRoom) {

        computerRoom.setAuditorId(ShiroUtils.getOgUser().getpId());
        startPage();
        List<ComputerRoom> computerRoomList = iComputerRoomService.selectComputerRoomAuditlist(computerRoom);

        return getDataTable(computerRoomList);
    }

    /**
     * 修改-页面
     */
    @GetMapping("/auditEdit/{id}")
    public String auditEdit(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        //打开新建页面回显
        mmap.put("userlist", userlist);
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("computerRoom", computerRoom);
        mmap.put("moduleList",moduleList);
        return prefix + "/auditEdit";
    }

    /**
     *审核驳回-直接作废
     * @return
     */
    @PostMapping("/reject")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult reject(@Validated ComputerRoom computerRoom)
    {
        computerRoom.setApplyState("-2");
        int row =  iComputerRoomService.updateComputerRoomApplyState(computerRoom);
        return AjaxResult.success("已作废");
    }

    /**
     *审核不通过
     * @return
     */
    @PostMapping("/noPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult noPass(@Validated ComputerRoom computerRoom)
    {
        computerRoom.setApplyState("2");
        int row =  iComputerRoomService.updateComputerRoomApplyState(computerRoom);
        return AjaxResult.success("审批不通过");
    }

    /**
     *审核通过
     * @return
     */
    @PostMapping("/pass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult pass(@Validated ComputerRoom computerRoom)
    {
        computerRoom.setApplyState("1");
        int row =  iComputerRoomService.updateComputerRoomApplyState(computerRoom);
        return AjaxResult.success("审批通过");
    }

    /**
     * 机房出入-登记页面列表页
     * @param mmap
     * @return
     */
    @GetMapping("/register")
    public String register(ModelMap mmap) {
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);
        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        mmap.put("moduleList", moduleList);
        mmap.put("userlist", userlist);
        mmap.addAttribute("user", ShiroUtils.getOgUser().getpId());
        return prefix + "/register";
    }

    /**
     * 审核列表集合信息
     * @param computerRoom
     * @return
     */
    @PostMapping("/registerList")
    @ResponseBody
    public TableDataInfo ComputerRoomRegisterList(ComputerRoom computerRoom) {

        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //如果岗位不是管理员和处长，则查询对应登录用户的自己的数据
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }

        startPage();
        List<ComputerRoom> computerRoomList = iComputerRoomService.selectComputerRoomRegisterList(computerRoom);

        return getDataTable(computerRoomList);
    }


    /**
     * 登记-页面
     */
    @GetMapping("/registerEdit/{id}")
    public String registerEdit(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowDate = df.format(new Date());
        computerRoom.setRegisterTime(nowDate);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //申请信息与登记信息联动
        //陪同人员
        Map<String,List> map = iComputerRoomService.getApplyForRegister(computerRoom);
        List intoNameList = map.get("intoNameList");

        if(intoNameList.size() == 0){
            mmap.put("intoNameState", "0");
        }else {
            mmap.put("intoNameState", "1");
            mmap.put("intoNameList", map.get("intoNameList"));
        }

        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        //回显联动实际进入
        mmap.put("accompanyList", map.get("accompanyList"));
        mmap.put("realModuleList", map.get("moduleList"));
        //打开新建页面回显
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("userlist", userlist);
        mmap.put("computerRoom", computerRoom);
        mmap.put("status", computerRoom.getApplyState());
        mmap.put("moduleList",moduleList);
        return prefix + "/registerPage";
    }

    /**
     * 保存登记
     * @param computerRoom
     * @return
     */
    @PostMapping("/saveRegisterEdit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveRegisterEdit(ComputerRoom computerRoom){

        if(computerRoom == null){
            return AjaxResult.error("传参为空");
        }
        computerRoom.setApplyState("3");
        int resultRow =  iComputerRoomService.updateComputerRoomRegister(computerRoom);

        return AjaxResult.success("修改机房出入申请操作成功");
    }


    /**
     * 查看登记详情页面
     */
    @GetMapping("/registerDetail/{id}")
    public String registerDetail(@PathVariable("id") String id, ModelMap mmap) {

        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowDate = df.format(new Date());
        computerRoom.setRegisterTime(nowDate);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);

        List<Map> realitybelongingsList = iComputerRoomService.getRealityBelongingsList(computerRoom);

        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //申请信息与登记信息联动
        //陪同人员
        Map<String,List> map = iComputerRoomService.getApplyForRegister(computerRoom);
        List intoNameList = map.get("intoNameList");

        if(intoNameList.size() == 0){
            mmap.put("intoNameState", "0");
        }else {
            mmap.put("intoNameState", "1");
            mmap.put("intoNameList", map.get("intoNameList"));
        }


        //查询机房所属中心
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        //回显联动实际进入
        mmap.put("accompanyList", map.get("accompanyList"));
        mmap.put("realModuleList", map.get("moduleList"));
        //打开新建页面回显
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("realitybelongingsList",realitybelongingsList);
        mmap.put("userlist", userlist);
        mmap.put("computerRoom", computerRoom);
        mmap.put("status", computerRoom.getApplyState());
        mmap.put("moduleList",moduleList);
        return prefix + "/registerPageDetail";
    }

    /* 以下为分类  */

    /**
     * 机房模块分类维护页面
     * @return
     */
    @GetMapping("/moduleCategory")
    public String classIteoator() {
        return prefixClass + "/classIteoatorForModule";
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(ComputerModule computerModule) {
        List<Ztree> ztrees = iComputerRoomService.selectTree(computerModule);
        return ztrees;
    }

    /**
     * 查询分类列表
     */
    @PostMapping("/classList")
    @ResponseBody
    public TableDataInfo classList(ComputerModule computerModule) {
        if (StringUtils.isNotEmpty(computerModule.getParentId())) {
            ComputerModule cm = new ComputerModule();
            cm.setParentId(computerModule.getParentId());
            cm.setStatus(computerModule.getStatus());
            cm.setName(computerModule.getName());
            startPage();
            List<ComputerModule> list = iComputerRoomService.selectComputerModuleList(cm);
            return getDataTable(list);
        } else {
            throw new BusinessException("请选则左侧系统后再点击查询按钮。");
        }
    }

    /**
     * 新增分类
     */
    @GetMapping("/addClass/{id}")
    public String addClass(@PathVariable("id") String id, ModelMap mmap) {

        ComputerModule computerModule = iComputerRoomService.selectComputerModuleById(id);

        mmap.put("parentName", computerModule.getName());
        mmap.put("parentId", computerModule.getId());
        mmap.put("knowledgeTitle", computerModule);
        return prefixClass + "/addClass";
    }

    /**
     * 新增保存分类
     */
    @Log(title = "标题", businessType = BusinessType.INSERT)
    @PostMapping("/addClassData")
    @ResponseBody
    public AjaxResult addClassData(ComputerModule computerModule) {
        computerModule.setId(UUID.getUUIDStr());
        computerModule.setCreateId(ShiroUtils.getOgUser().getpId());
        computerModule.setCreateTime(DateUtils.getTime());
        computerModule.setStatus("0");
        return toAjax(iComputerRoomService.insertComputerModule(computerModule));
    }

    /**
     * 修改分类
     */
    @GetMapping("/editClass/{id}")
    public String editClass(@PathVariable("id") String id, ModelMap mmap) {
        ComputerModule computerModule = iComputerRoomService.selectComputerModuleById(id);
        mmap.put("parentName", computerModule.getParentName());
        mmap.put("parentId", computerModule.getParentId());
        mmap.put("knowledgeTitle", computerModule);
        return prefixClass + "/editClass";
    }

    /**
     * 修改保存分类
     */
    @Log(title = "类别", businessType = BusinessType.UPDATE)
    @PostMapping("/editClassData")
    @ResponseBody
    public AjaxResult editSave(ComputerModule computerModule) {
        ComputerModule cm = new ComputerModule();
        cm.setId(computerModule.getId());
        cm.setName(computerModule.getName());
        cm.setStatus(computerModule.getStatus());
        return toAjax(iComputerRoomService.updateComputerModule(cm));
    }

    /**
     * 新增标题
     */
    @GetMapping("/addZeroType")
    public String addSys(ModelMap mmap) {
        return prefixClass + "/addZeroType";
    }



}
