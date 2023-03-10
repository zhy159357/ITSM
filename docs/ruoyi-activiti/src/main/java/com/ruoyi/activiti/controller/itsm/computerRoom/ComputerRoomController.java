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
     * ????????????????????????
     */
    private String prefix_common = "common";


    /**
     * ????????????-?????????????????????
     * @param mmap
     * @return
     */
    @GetMapping()
    public String apply(ModelMap mmap) {


        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);
        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        mmap.put("moduleList", moduleList);
        mmap.put("userlist", userlist);
        mmap.addAttribute("user", ShiroUtils.getOgUser().getpId());
        return prefix + "/apply";
    }




    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo ComputerRoomApplylist(ComputerRoom computerRoom) {
        //TODO  ???????????????????????????????????????????????????????????????
        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //????????????????????????????????????????????????????????????????????????????????????
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }

        startPage();
        List<ComputerRoom> computerRoomList = iComputerRoomService.computerRoomApplylist(computerRoom);

        return getDataTable(computerRoomList);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/selectogOrg")
    public String selectogOrg() {
        return prefix + "/subpage/selectOgorg";
    }

    /**
     * ????????????
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org) {
        startPage();
        List<OgOrg> list = iSysDeptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GetMapping("/selectAccompanyUser")
    public String selectAccompanyUser() {
        return prefix + "/subpage/selectAccompanyUser";
    }


    /**
     * ????????????
     */
    @PostMapping("/selectUserList")
    @ResponseBody
    public TableDataInfo selectUserList(OgPerson person) {
        startPage();

        //????????????-??????????????????????????????????????????
        List<OgPerson> list = iComputerRoomService.selectUserList(person);

        return getDataTable(list);
    }


    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GetMapping("/selectWorkContent")
    public String selectWorkContent() {
        return prefix + "/subpage/selectWorkContent";
    }

    /**
     * ????????????????????????
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
     * ??????????????????
     */
    @PostMapping("/workContent")
    @ResponseBody
    public TableDataInfo workContent() {
        startPage();

        //??????????????????
        List<ComputerRoom> list = iComputerRoomService.workContent();

        return getDataTable(list);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/selectApplication")
    public String selectApplication() {
        return prefix_common + "/selectApplication";
    }


    /**
     * ????????????????????????????????????
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
     * ????????????????????????
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {

        //????????????????????????
        //????????????????????????????????????????????????????????????
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

        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        //????????????
        String bizType = "JF";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);

        //????????????????????????
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
     * ????????????
     */
    @PostMapping("/saveTS")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveTS(@Validated ComputerRoom computerRoom) {

        if(computerRoom == null){
            return AjaxResult.error("????????????");
        }

        String id = computerRoom.getId();
        if (StringUtils.isEmpty(id)) {
            computerRoom.setId(UUID.getUUIDStr());
            int resultRow = iComputerRoomService.insertTsComputerRoomApply(computerRoom);

        }

        return AjaxResult.success("????????????", computerRoom.getId());
    }

    /**
     * ????????????
     */
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(@Validated ComputerRoom computerRoom) {

        if(computerRoom == null){
            return AjaxResult.error("????????????");
        }

        String id = computerRoom.getId();
        if (StringUtils.isEmpty(id)) {
            computerRoom.setId(UUID.getUUIDStr());
            int resultRow = iComputerRoomService.insertComputerRoomApply(computerRoom);

        }

        return AjaxResult.success("????????????", computerRoom.getId());
    }


    /**
     * ??????-??????
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
        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        //????????????????????????
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
     * ??????-??????
     * @param computerRoom
     * @return
     */
    @PostMapping("/saveEdit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveEdit(ComputerRoom computerRoom){

        if(computerRoom == null){
            return AjaxResult.error("????????????");
        }
        int resultRow =  iComputerRoomService.upadateComputerRoomApply(computerRoom);

        return AjaxResult.success("????????????????????????????????????");
    }

    /**
     * ??????????????????
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        List<OgPerson> auditorsList = getAuditorsList();
        mmap.put("auditorsList",auditorsList);


        //????????????????????????
        mmap.put("userlist", userlist);
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("computerRoom", computerRoom);
        mmap.put("moduleList",moduleList);
        return prefix + "/detail";
    }


    //?????????????????????
    private OgPerson getAuditors(){
        //???????????????????????????????????????
        List<OgPerson> list = iComputerRoomService.getOgPersonList(ShiroUtils.getOgUser().getUserId());
        OgUser ogUser = new OgUser();
        ogUser.setPostId(Long.valueOf(PostConstants.SJZX_CZ));
        //??????????????????????????????
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
        //???????????????????????????????????????
        List<OgPerson> list = iComputerRoomService.getOgPersonList(ShiroUtils.getOgUser().getUserId());
        OgUser ogUser = new OgUser();
        ogUser.setPostId(Long.valueOf(PostConstants.SJZX_CZ));
        //??????????????????????????????
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
     * ????????????-??????
     * @param computerRoom
     * @return
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ComputerRoom computerRoom) {

        String isCurrentPage = (String) computerRoom.getParams().get("currentPage");

        //TODO  ???????????????????????????????????????????????????????????????
        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //????????????????????????????????????????????????????????????????????????????????????
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<ComputerRoom> list = iComputerRoomService.computerRoomApplylist(computerRoom);
        ExcelUtil<ComputerRoom> util = new ExcelUtil<>(ComputerRoom.class);
        return util.exportExcel(list, "????????????????????????");
    }

    /**
     * ????????????-??????
     * @param computerRoom
     * @return
     */
    @PostMapping("/exportRegisterList")
    @ResponseBody
    public AjaxResult exportRegisterList(ComputerRoom computerRoom) {

        String isCurrentPage = (String) computerRoom.getParams().get("currentPage");
        //TODO  ???????????????????????????????????????????????????????????????
        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //????????????????????????????????????????????????????????????????????????????????????
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<ComputerRoom> list = iComputerRoomService.selectComputerRoomRegisterList(computerRoom);
        ExcelUtil<ComputerRoom> util = new ExcelUtil<>(ComputerRoom.class);
        return util.exportExcel(list, "????????????????????????");
    }


    /*
     * ??????-????????????
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
     * ????????????????????????
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
     * ??????-??????
     */
    @GetMapping("/auditEdit/{id}")
    public String auditEdit(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        //????????????????????????
        mmap.put("userlist", userlist);
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("computerRoom", computerRoom);
        mmap.put("moduleList",moduleList);
        return prefix + "/auditEdit";
    }

    /**
     *????????????-????????????
     * @return
     */
    @PostMapping("/reject")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult reject(@Validated ComputerRoom computerRoom)
    {
        computerRoom.setApplyState("-2");
        int row =  iComputerRoomService.updateComputerRoomApplyState(computerRoom);
        return AjaxResult.success("?????????");
    }

    /**
     *???????????????
     * @return
     */
    @PostMapping("/noPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult noPass(@Validated ComputerRoom computerRoom)
    {
        computerRoom.setApplyState("2");
        int row =  iComputerRoomService.updateComputerRoomApplyState(computerRoom);
        return AjaxResult.success("???????????????");
    }

    /**
     *????????????
     * @return
     */
    @PostMapping("/pass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult pass(@Validated ComputerRoom computerRoom)
    {
        computerRoom.setApplyState("1");
        int row =  iComputerRoomService.updateComputerRoomApplyState(computerRoom);
        return AjaxResult.success("????????????");
    }

    /**
     * ????????????-?????????????????????
     * @param mmap
     * @return
     */
    @GetMapping("/register")
    public String register(ModelMap mmap) {
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);
        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        mmap.put("moduleList", moduleList);
        mmap.put("userlist", userlist);
        mmap.addAttribute("user", ShiroUtils.getOgUser().getpId());
        return prefix + "/register";
    }

    /**
     * ????????????????????????
     * @param computerRoom
     * @return
     */
    @PostMapping("/registerList")
    @ResponseBody
    public TableDataInfo ComputerRoomRegisterList(ComputerRoom computerRoom) {

        String status = iComputerRoomService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        //????????????????????????????????????????????????????????????????????????????????????
        if("0".equals(status)){
            computerRoom.setApplyUserId(ShiroUtils.getOgUser().getpId());
        }

        startPage();
        List<ComputerRoom> computerRoomList = iComputerRoomService.selectComputerRoomRegisterList(computerRoom);

        return getDataTable(computerRoomList);
    }


    /**
     * ??????-??????
     */
    @GetMapping("/registerEdit/{id}")
    public String registerEdit(@PathVariable("id") String id, ModelMap mmap) {
        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        String nowDate = df.format(new Date());
        computerRoom.setRegisterTime(nowDate);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //?????????????????????????????????
        //????????????
        Map<String,List> map = iComputerRoomService.getApplyForRegister(computerRoom);
        List intoNameList = map.get("intoNameList");

        if(intoNameList.size() == 0){
            mmap.put("intoNameState", "0");
        }else {
            mmap.put("intoNameState", "1");
            mmap.put("intoNameList", map.get("intoNameList"));
        }

        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();
        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        //????????????????????????
        mmap.put("accompanyList", map.get("accompanyList"));
        mmap.put("realModuleList", map.get("moduleList"));
        //????????????????????????
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("userlist", userlist);
        mmap.put("computerRoom", computerRoom);
        mmap.put("status", computerRoom.getApplyState());
        mmap.put("moduleList",moduleList);
        return prefix + "/registerPage";
    }

    /**
     * ????????????
     * @param computerRoom
     * @return
     */
    @PostMapping("/saveRegisterEdit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveRegisterEdit(ComputerRoom computerRoom){

        if(computerRoom == null){
            return AjaxResult.error("????????????");
        }
        computerRoom.setApplyState("3");
        int resultRow =  iComputerRoomService.updateComputerRoomRegister(computerRoom);

        return AjaxResult.success("????????????????????????????????????");
    }


    /**
     * ????????????????????????
     */
    @GetMapping("/registerDetail/{id}")
    public String registerDetail(@PathVariable("id") String id, ModelMap mmap) {

        ComputerRoom computerRoom = iComputerRoomService.selectComputerRoomApplyById(id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        String nowDate = df.format(new Date());
        computerRoom.setRegisterTime(nowDate);
        List<Map> list = iComputerRoomService.getIntoList(computerRoom);
        List<Map> belongingsList = iComputerRoomService.getBelongingsList(computerRoom);

        List<Map> realitybelongingsList = iComputerRoomService.getRealityBelongingsList(computerRoom);

        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(PostConstants.JF_CRRY));
        List<OgUser> userlist = iComputerRoomService.selectAllocatedListPost(user);

        //?????????????????????????????????
        //????????????
        Map<String,List> map = iComputerRoomService.getApplyForRegister(computerRoom);
        List intoNameList = map.get("intoNameList");

        if(intoNameList.size() == 0){
            mmap.put("intoNameState", "0");
        }else {
            mmap.put("intoNameState", "1");
            mmap.put("intoNameList", map.get("intoNameList"));
        }


        //????????????????????????
        List<ComputerModule>  moduleList = iComputerRoomService.selectComputerModuleCenter();

        if (StringUtils.isNotEmpty(computerRoom.getInOutType())) {
            String inOutType = computerRoom.getInOutType();
            String[] orgs = Convert.toStrArray(inOutType);
            computerRoom.setInOutType(orgs[0]);
        }

        //????????????????????????
        mmap.put("accompanyList", map.get("accompanyList"));
        mmap.put("realModuleList", map.get("moduleList"));
        //????????????????????????
        mmap.put("intoList", list);
        mmap.put("belongingsList", belongingsList);
        mmap.put("realitybelongingsList",realitybelongingsList);
        mmap.put("userlist", userlist);
        mmap.put("computerRoom", computerRoom);
        mmap.put("status", computerRoom.getApplyState());
        mmap.put("moduleList",moduleList);
        return prefix + "/registerPageDetail";
    }

    /* ???????????????  */

    /**
     * ??????????????????????????????
     * @return
     */
    @GetMapping("/moduleCategory")
    public String classIteoator() {
        return prefixClass + "/classIteoatorForModule";
    }

    /**
     * ???????????????
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(ComputerModule computerModule) {
        List<Ztree> ztrees = iComputerRoomService.selectTree(computerModule);
        return ztrees;
    }

    /**
     * ??????????????????
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
            throw new BusinessException("????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????
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
     * ??????????????????
     */
    @Log(title = "??????", businessType = BusinessType.INSERT)
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
     * ????????????
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
     * ??????????????????
     */
    @Log(title = "??????", businessType = BusinessType.UPDATE)
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
     * ????????????
     */
    @GetMapping("/addZeroType")
    public String addSys(ModelMap mmap) {
        return prefixClass + "/addZeroType";
    }



}
