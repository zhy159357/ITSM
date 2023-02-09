package com.ruoyi.activiti.controller.itsm.lxbg;

import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *新增例行变更计划
 */
@Controller
@RequestMapping("/lxbg/addlxbg")
@Transactional(rollbackFor = Exception.class)
public class AddlxbgController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AddlxbgController.class);


    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    //新增例行变更路径
    private String prefix = "lxbg/addlxbg";


    /**
     * 转到新增例行变更页面
     * @param map
     * @return
     */
    @GetMapping()
    public String addlxbg(ModelMap map)
    {
        map.addAttribute("userId",ShiroUtils.getUserId());

        return prefix + "/lxbg";
    }


    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizScheduling scheduling)
    {

        //根据目录树ID回显计划表信息
        SmBizFolder smBizFolder = folderService.selectFolderTreeById(scheduling.getFolder());
        if(smBizFolder!=null){
            scheduling.setFolder(smBizFolder.getId_());
        }else {
            scheduling.setFolder("1");
        }
        //列表信息
        List<SmBizScheduling> list = addlxbgService.selectScheduling(scheduling);
        return getDataTable_ideal(list);

    }

    /**
     * 转到添加页面
     * @param treeId
     * @param modelMap
     * @return
     */
    @GetMapping("/add")
    public String add(String treeId, ModelMap modelMap) {
        String bizType = "ZDJH";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        modelMap.addAttribute("num", bizType + nowDateStr + numStr);

        //打开新建页面回显创建机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        OgOrg o= iSysDeptService.selectDeptById(person.getOrgId());
        //获取路径
        SmBizFolder sf =folderService.selectFolderTreeById(treeId);
        if(sf!=null){
            String folderId = sf.getId_();
            String path = sf.getName_();
            modelMap.addAttribute("path",path);
            modelMap.addAttribute("folderId",folderId);
        }


        //获取机构信息
        String orgName= o.getOrgName();

        String createTime = DateUtils.getTime();

        //获取机构ID
        String orgId = person.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        if(ogOrg!=null){
            String levelCode = ogOrg.getLevelCode();
            //审核人
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, "8200");
            modelMap.put("checkList", checkList);

        }

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }

        modelMap.addAttribute("orgName", orgName);
        modelMap.addAttribute("creatorId", pId);
        modelMap.addAttribute("loginName", person.getpName());
        modelMap.addAttribute("createTime",createTime);
        //生成计划表主键id
        modelMap.put("schedulingId",UUID.getUUIDStr());
        modelMap.addAttribute("ogOrg", ogOrg);
        modelMap.put("sendRangeList" ,reList);
        return prefix + "/add";
    }


    /**
     * 保存
     * @param scheduling
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult lxbgsave( SmBizScheduling scheduling)
    {

        SmBizTask smBizTask = new SmBizTask();

        OgPerson ogPerson1  = ogPersonService.selectOgPersonById(scheduling.getCheckPersonId());
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        scheduling.setCreatorId(ShiroUtils.getOgUser().getpId());
        scheduling.setCreatorDeptId(ogPerson.getOrgId());
        scheduling.setInvalidationMark("1");
        if(StringUtils.isNotEmpty(scheduling.getCheckPersonId())){
            scheduling.setCheckPersonName(ogPerson1.getpName());
        }
        scheduling.setSchedulingId(scheduling.getSchedulingId());

        //根据机构生成执行机构id
        String masterOrgId=scheduling.getParams().get("masterOrgId")==null?"":scheduling.getParams().get("masterOrgId").toString();
        String[] masterOrgIds=masterOrgId.split(",");

        //根据工作组生成工作组id
        String workGroup=scheduling.getParams().get("workGroup")==null?"":scheduling.getParams().get("workGroup").toString();

        //根据机构生成
        if(StringUtils.isNotEmpty(scheduling.getSendRange())&&"1".equals(scheduling.getSendRange())){
            scheduling.setPlanType("1");
            smBizTask.setPerformDeptId(scheduling.getParams().get("masterOrgId").toString());
            smBizTask.setPerformDeptName(scheduling.getParams().get("masterOrgIdName").toString());
            smBizTask.setPerformGroupId("");
            smBizTask.setPerformGroupName("");
            smBizTask.setSendRange("1");
            //根据工作组生成
        }else if(StringUtils.isNotEmpty(scheduling.getSendRange()) &&"2".equals(scheduling.getSendRange())){
            scheduling.setPlanType("1");
            smBizTask.setPerformGroupId(scheduling.getParams().get("workGroup").toString());
            smBizTask.setPerformGroupName(scheduling.getParams().get("workGroupName").toString());
            smBizTask.setPerformDeptId("");
            smBizTask.setPerformDeptName("");
            smBizTask.setSendRange("2");
            //其他
        }else {
            scheduling.setPlanType("1");
            String deptId = "";
            String groupId = "";
            String deptName ="";
            String groupName ="";

            //判断传进来的id是否带有 orgId和grpId
            if(masterOrgId.contains("orgId") || masterOrgId.contains("grpId") ){

                //遍历前段传来的Id
                for (String kj : masterOrgIds) {
                    if (kj.indexOf("orgId") > -1) {
                        String orgId = kj.substring(kj.indexOf(":") + 1, kj.length());
                        deptId += orgId + ",";
                    }
                    if (kj.indexOf("grpId") > -1) {
                        String grpId = kj.substring(kj.indexOf(":") + 1, kj.length());
                        groupId += grpId + ",";
                    }
                }

                //分割
                if(StringUtils.isNotEmpty(deptId)){
                    deptId = deptId.substring(0, deptId.length() - 1);
                    smBizTask.setPerformDeptId(deptId);
                }

                if(StringUtils.isNotEmpty(groupId)){
                    groupId = groupId.substring(0, groupId.length() - 1);
                    smBizTask.setPerformGroupId(groupId);
                }

                String performDnameId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
                String[] performDnameIds=performDnameId.split(",");

                String performGroupNameId=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
                String[] performGroupNameIds=performGroupNameId.split(",");

                //根据机构id添加名字
                if(performDnameIds.length>0){
                    for(String nId : performDnameIds){
                        if(StringUtils.isNotEmpty(nId)){
                            //根据遍历出来的机构id查询到机构名字
                            OgOrg ogOrg = iSysDeptService.selectDeptById(nId);
                            if(ogOrg!=null){
                                deptName += ogOrg.getOrgName() + ",";
                            }
                        }
                    }
                }

                //根据工作组id添加名字
                if(performGroupNameIds.length>0){
                    for(String nId : performGroupNameIds){
                        if(StringUtils.isNotEmpty(nId)){
                            //根据遍历出来的工作组id查询到机构名字
                            OgGroup ogGroup = workService.selectOgGroupById(nId);
                            if(ogGroup!=null){
                                groupName += ogGroup.getGrpName() + ",";
                            }
                        }
                    }
                }

                if(StringUtils.isNotEmpty(deptName)){
                    deptName = deptName.substring(0, deptName.length() - 1);
                    smBizTask.setPerformDeptName(deptName);
                }

                if(StringUtils.isNotEmpty(groupName)){
                    groupName = groupName.substring(0, groupName.length() - 1);
                    smBizTask.setPerformGroupName(groupName);
                }

            }else {
                //分割机构
                if(StringUtils.isNotEmpty(masterOrgId) ){
                    //分割
                    String[] deptId1 = masterOrgId.split(",");
                    //判断
                    if(deptId1.length>0){
                        for(String dId:deptId1){
                            if(StringUtils.isNotEmpty(dId)){
                                //根据遍历出来的机构id查询到机构名字
                                OgOrg ogOrg = iSysDeptService.selectDeptById(dId);
                                if(ogOrg!=null){
                                    deptId+= dId+",";
                                    deptName += ogOrg.getOrgName() + ",";
                                }
                            }
                        }
                    }
                }

                //分割工作组
                if(StringUtils.isNotEmpty(masterOrgId) ){
                    String[] groupId1 = masterOrgId.split(",");
                    if(groupId1.length>0){
                        for(String gId:groupId1){
                            if(StringUtils.isNotEmpty(gId)){
                                //根据遍历出来的工作组id查询到工作组名字
                                OgGroup ogGroup = workService.selectOgGroupById(gId);
                                if(ogGroup!=null){
                                    groupId+=gId +",";
                                    groupName += ogGroup.getGrpName() + ",";
                                }
                            }
                        }
                    }
                }
                if(StringUtils.isNotEmpty(deptId)){
                    deptId = deptId.substring(0, deptId.length() - 1);
                    smBizTask.setPerformDeptId(deptId);
                }
                if(StringUtils.isNotEmpty(groupId)){
                    groupId = groupId.substring(0, groupId.length() - 1);
                    smBizTask.setPerformGroupId(groupId);
                }
                if(StringUtils.isNotEmpty(deptName)){
                    deptName = deptName.substring(0, deptName.length() - 1);
                    smBizTask.setPerformDeptName(deptName);
                }
                if(StringUtils.isNotEmpty(groupName)){
                    groupName = groupName.substring(0, groupName.length() - 1);
                    smBizTask.setPerformGroupName(groupName);
                }

            }

        }

        //生成作业表id
        smBizTask.setTaskId(UUID.getUUIDStr());
        smBizTask.setSchedulingId(scheduling.getSchedulingId());

        //作业表单号生成
        String bizType1 = "ZY";
        IdGenerator generator1 = new IdGenerator();
        String nowDateStr1 = DateUtils.dateTimeNow("yyyyMMdd");
        generator1.setCurrentDate(nowDateStr1);
        generator1.setBizType(bizType1);
        String numStr1 = idGeneratorService.selectIdGeneratorByType(generator1);
        //修改时间回显
        String updateTime = DateUtils.dateTimeNow();
        //自动生成的数据
        smBizTask.setTaskNo(bizType1+nowDateStr1+numStr1);
        smBizTask.setTaskTypeId(scheduling.getTaskTypeId());
        smBizTask.setMsgDoor(scheduling.getMsgDoor());
        smBizTask.setStartTime(scheduling.getStartTime());
        smBizTask.setSendRange(scheduling.getSendRange());
        smBizTask.setTaskTitle(scheduling.getSchedulingName());
        smBizTask.setTaskDescription(scheduling.getSchedulingDescription());
        smBizTask.setCharacter("0");
        smBizTask.setUpdateTime(updateTime);
        smBizTask.setInvalidationMark("1");
        smBizTask.setWorkStatus("01");
        smBizTask.setCreateTime(updateTime);
        smBizTask.setCreateOrgId(scheduling.getCreatorDeptId());
        smBizTask.setReceiveRoleName("例行变更计划执行人,");
        smBizTask.setReceiveRoleId("8300");

        //添加作业表
        taskService.insertTask(smBizTask);

        String performDeptId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
        String[] performDeptIds=performDeptId.split(",");

        //根据机构任务表生成
        if(performDeptIds.length>0){

            for(String fId:performDeptIds){
                if(StringUtils.isNotEmpty(fId)){
                    SmBizTaskinfo smBizTaskinfo=new SmBizTaskinfo();
                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    smBizTaskinfo.setPerformDeptId(fId);
                    smBizTaskinfo.setTaskFromNo(bizType+nowDateStr+numStr);
                    smBizTaskinfo.setTaskFormId(UUID.getUUIDStr());
                    smBizTaskinfo.setGenerateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setUpdateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setPerformDate(smBizTask.getStartTime());
                    smBizTaskinfo.setTaskId(smBizTask.getTaskId());
                    smBizTaskinfo.setTaskFormStatus("01");
                    smBizTaskinfo.setInvalidationMark("1");
                    smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo);
                    scheduling.setMasterOrgId("");
                }

            }
        }

        String performGroupId=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
        String[] performGroupIds=performGroupId.split(",");

        //根据工作组任务表生成
        if(performGroupIds.length>0){
            for(String gId:performGroupIds){
                if(StringUtils.isNotEmpty(gId)){
                    SmBizTaskinfo smBizTaskinfo=new SmBizTaskinfo();
                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    smBizTaskinfo.setTaskFromNo(bizType+nowDateStr+numStr);
                    smBizTaskinfo.setTaskFormId(UUID.getUUIDStr());
                    smBizTaskinfo.setGenerateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setUpdateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setPerformDate(smBizTask.getStartTime());
                    smBizTaskinfo.setTaskId(smBizTask.getTaskId());
                    smBizTaskinfo.setPerformGroupId(gId);
                    smBizTaskinfo.setTaskFormStatus("01");
                    smBizTaskinfo.setInvalidationMark("1");
                    smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo);
                    scheduling.setWorkGroup("");
                }
            }
        }
        SmBizTaskinfo sf=new SmBizTaskinfo();
        sf.setTaskId(smBizTask.getTaskId());
        int taskNum=smBizTaskinfoService.selectSmBizTaskinfoListtwo(sf).size();
        scheduling.setProcess("0/"+taskNum);
        smBizTask.setProcess(scheduling.getProcess());

        //判断当前是否为暂存还是提交状态
        if("commit".equals(scheduling.getLabel())){
            scheduling.setPlanStatus("02");
            try{
                addlxbgService.insertLxbg(scheduling);
            }catch (Exception e){
                logger.error("例行变更计划单新增失败 "+e.getMessage());
                throw  new BusinessException("例行变更计划单新增失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }
        try{
            scheduling.setPlanStatus("01");
            addlxbgService.insertLxbg(scheduling);
        }catch (Exception e){
            log.error("例行变更计划单单暂存失败: "+e.getMessage());
            return AjaxResult.error("例行变更计划单单暂存失败");
        }

        return AjaxResult.success("操作成功");

    }


    /**
     * 转到修改页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);
        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //发布时间进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        return prefix + "/edit";
    }

    /**
     * 修改保存例行变更计划
     * @param scheduling
     * @return
     */
    @Log(title = "例行变更计划修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave( SmBizScheduling scheduling)
    {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        OgPerson ogPerson1  = ogPersonService.selectOgPersonById(scheduling.getCheckPersonId());

        SmBizTask smBizTask = new SmBizTask();

        if(StringUtils.isNotEmpty(scheduling.getCheckPersonId())){
            scheduling.setCheckPersonName(ogPerson1.getpName());
        }

        smBizTask.setTaskTypeId(scheduling.getTaskTypeId());
        smBizTask.setTaskId(taskService.selectSchedulingId(scheduling.getSchedulingId()).getTaskId());
        smBizTask.setStartTime(scheduling.getStartTime());

        //根据机构生成执行机构id
        String masterOrgId=scheduling.getParams().get("masterOrgId")==null?"":scheduling.getParams().get("masterOrgId").toString();
        String[] masterOrgIds=masterOrgId.split(",");
        //根据工作组生成工作组id
        String workGroup=scheduling.getParams().get("workGroup")==null?"":scheduling.getParams().get("workGroup").toString();

        //根据机构生成
        if(StringUtils.isNotEmpty(scheduling.getSendRange())&&"1".equals(scheduling.getSendRange())){
            scheduling.setPlanType("1");
            String deptId = "";
            String deptName ="";
            //分割机构
            if(StringUtils.isNotEmpty(masterOrgId) ){
                //分割
                String[] deptId1 = masterOrgId.split(",");
                //判断
                if(deptId1.length>0){
                    for(String dId:deptId1){
                        if(StringUtils.isNotEmpty(dId)){
                            //根据遍历出来的机构id查询到机构名字
                            OgOrg ogOrg = iSysDeptService.selectDeptById(dId);
                            if(ogOrg!=null){
                                deptId+= dId+",";
                                deptName += ogOrg.getOrgName() + ",";
                            }
                        }
                    }
                }
            }
            if(StringUtils.isNotEmpty(deptId)){
                deptId = deptId.substring(0, deptId.length() - 1);
                smBizTask.setPerformDeptId(deptId);
            }
            if(StringUtils.isNotEmpty(deptName)){
                deptName = deptName.substring(0, deptName.length() - 1);
                smBizTask.setPerformDeptName(deptName);
            }

            smBizTask.setPerformGroupId("");
            smBizTask.setPerformGroupName("");
            smBizTask.setSendRange("1");
        }else if(StringUtils.isNotEmpty(scheduling.getSendRange())&&"2".equals(scheduling.getSendRange())){         //根据工作组生成
            scheduling.setPlanType("1");
            smBizTask.setPerformGroupId(scheduling.getParams().get("workGroup").toString());
            smBizTask.setPerformGroupName(scheduling.getParams().get("workGroupName").toString());
            smBizTask.setPerformDeptId("");
            smBizTask.setPerformDeptName("");
            smBizTask.setSendRange("2");
        }else {
            scheduling.setPlanType("1");

            smBizTask.setPerformGroupId("");
            smBizTask.setPerformGroupName("");

            String deptId = "";
            String groupId = "";
            String deptName ="";
            String groupName ="";

            if(masterOrgId.contains("orgId") || masterOrgId.contains("grpId") ){

                for (String kj : masterOrgIds) {
                    if (kj.indexOf("orgId") > -1) {
                        String orgId = kj.substring(kj.indexOf(":") + 1, kj.length());
                        deptId += orgId + ",";

                    }
                    if (kj.indexOf("grpId") > -1) {
                        String grpId = kj.substring(kj.indexOf(":") + 1, kj.length());
                        groupId += grpId + ",";
                    }
                }
                if(StringUtils.isNotEmpty(deptId)){
                    deptId = deptId.substring(0, deptId.length() - 1);
                    smBizTask.setPerformDeptId(deptId);
                }
                if(StringUtils.isNotEmpty(groupId)){
                    groupId = groupId.substring(0, groupId.length() - 1);
                    smBizTask.setPerformGroupId(groupId);
                }

                String performDnameId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
                String[] performDnameIds=performDnameId.split(",");

                String performGroupNameId=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
                String[] performGroupNameIds=performGroupNameId.split(",");

                //根据机构id添加名字
                if(performDnameIds.length>0){
                    for(String nId : performDnameIds){
                        if(StringUtils.isNotEmpty(nId)){
                            //根据遍历出来的机构id查询到机构名字
                            OgOrg ogOrg = iSysDeptService.selectDeptById(nId);
                            if(ogOrg!=null){
                                deptName += ogOrg.getOrgName() + ",";
                            }
                        }
                    }
                }

                //根据工作组id添加名字
                if(performGroupNameIds.length>0){
                    for(String nId : performGroupNameIds){
                        if(StringUtils.isNotEmpty(nId)){
                            //根据遍历出来的工作组id查询到机构名字
                            OgGroup ogGroup = workService.selectOgGroupById(nId);
                            if(ogGroup!=null){
                                groupName += ogGroup.getGrpName() + ",";
                            }
                        }
                    }
                }

                if(StringUtils.isNotEmpty(deptName)){
                    deptName = deptName.substring(0, deptName.length() - 1);
                    smBizTask.setPerformDeptName(deptName);
                }
                if(StringUtils.isNotEmpty(groupName)){
                    groupName = groupName.substring(0, groupName.length() - 1);
                    smBizTask.setPerformGroupName(groupName);
                }

            }else {
                //分割机构
                if(StringUtils.isNotEmpty(masterOrgId) ){
                    //分割
                    String[] deptId1 = masterOrgId.split(",");
                    //判断
                    if(deptId1.length>0){
                        for(String dId:deptId1){
                            if(StringUtils.isNotEmpty(dId)){

                                //根据遍历出来的机构id查询到机构名字
                                OgOrg ogOrg = iSysDeptService.selectDeptById(dId);
                                if(ogOrg!=null){
                                    deptId+= dId+",";
                                    deptName += ogOrg.getOrgName() + ",";
                                }
                            }
                        }
                    }
                }

                //分割工作组
                if(StringUtils.isNotEmpty(masterOrgId)){
                    String[] groupId1 = masterOrgId.split(",");
                    if(groupId1.length>0){
                        for(String gId:groupId1){
                            if(StringUtils.isNotEmpty(gId)){
                                //根据遍历出来的工作组id查询到工作组名字
                                OgGroup ogGroup = workService.selectOgGroupById(gId);
                                if(ogGroup!=null){
                                    groupId+=gId +",";
                                    groupName += ogGroup.getGrpName() + ",";
                                }
                            }
                        }
                    }
                }
                if(StringUtils.isNotEmpty(deptId)){
                    deptId = deptId.substring(0, deptId.length() - 1);
                    smBizTask.setPerformDeptId(deptId);
                }
                if(StringUtils.isNotEmpty(groupId)){
                    groupId = groupId.substring(0, groupId.length() - 1);
                    smBizTask.setPerformGroupId(groupId);
                }
                if(StringUtils.isNotEmpty(deptName)){
                    deptName = deptName.substring(0, deptName.length() - 1);
                    smBizTask.setPerformDeptName(deptName);
                }
                if(StringUtils.isNotEmpty(groupName)){
                    groupName = groupName.substring(0, groupName.length() - 1);
                    smBizTask.setPerformGroupName(groupName);
                }

            }

        }

        //短信
        if("1".equals(scheduling.getMsgDoor())){
            smBizTask.setMsgDoor("1");
        }
        if("2".equals(scheduling.getMsgDoor())){
            smBizTask.setMsgDoor("2");
        }

        taskService.updateTask(smBizTask);

        //删除原有的机构或工作组
        smBizTaskinfoService.deleteSmBizTaskinfoZyById(smBizTask.getTaskId());

        String performDeptId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
        String[] performDeptIds=performDeptId.split(",");

        //根据机构任务表生成
        if(performDeptIds.length>0){

            for(String fId:performDeptIds){
                if(StringUtils.isNotEmpty(fId)){
                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    SmBizTaskinfo smBizTaskinfo = new SmBizTaskinfo();
                    smBizTaskinfo.setTaskFromNo(bizType+nowDateStr+numStr);
                    smBizTaskinfo.setPerformDeptId(fId);
                    smBizTaskinfo.setTaskFormId(UUID.getUUIDStr());
                    smBizTaskinfo.setGenerateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setUpdateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setPerformDate(smBizTask.getStartTime());
                    smBizTaskinfo.setTaskId(smBizTask.getTaskId());
                    smBizTaskinfo.setTaskFormStatus("01");
                    smBizTaskinfo.setInvalidationMark("1");
                    smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo);
                    scheduling.setMasterOrgId("");
                }
            }
        }

        String performGroupId=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
        String[] performGroupIds=performGroupId.split(",");

        //根据工作组任务表生成
        if(performGroupIds.length>0){

            for(String gId:performGroupIds){
                if(StringUtils.isNotEmpty(gId)){
                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    SmBizTaskinfo smBizTaskinfo1 =new SmBizTaskinfo();

                    smBizTaskinfo1.setTaskFromNo(bizType+nowDateStr+numStr);
                    smBizTaskinfo1.setTaskFormId(UUID.getUUIDStr());
                    smBizTaskinfo1.setGenerateTime(scheduling.getCreateTime());
                    smBizTaskinfo1.setUpdateTime(scheduling.getCreateTime());
                    smBizTaskinfo1.setPerformDate(smBizTask.getStartTime());
                    smBizTaskinfo1.setTaskId(smBizTask.getTaskId());
                    smBizTaskinfo1.setPerformGroupId(gId);
                    smBizTaskinfo1.setTaskFormStatus("01");
                    smBizTaskinfo1.setInvalidationMark("1");
                    smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo1);
                    scheduling.setWorkGroup("");
                }
            }

        }
        SmBizTaskinfo sf=new SmBizTaskinfo();
        sf.setTaskId(smBizTask.getTaskId());
        int taskNum=smBizTaskinfoService.selectSmBizTaskinfoListtwo(sf).size();
        scheduling.setProcess("0/"+taskNum);
        smBizTask.setProcess(scheduling.getProcess());

        //判断当前是否为暂存还是提交状态
        if("commit".equals(scheduling.getLabel())){
            scheduling.setPlanStatus("02");
            scheduling.setCreatorId(ShiroUtils.getOgUser().getpId());
            scheduling.setCreatorDeptId(ogPerson.getOrgId());
            try{
                addlxbgService.updatelxbg(scheduling);
            }catch (Exception e){
                logger.error("例行变更计划单修改失败 "+e.getMessage());
                throw  new BusinessException("例行变更计划单修改失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }
        scheduling.setPlanStatus("01");
        scheduling.setCreatorId(ShiroUtils.getOgUser().getpId());
        scheduling.setCreatorDeptId(ogPerson.getOrgId());
        try{
            addlxbgService.updatelxbg(scheduling);
        }catch (Exception e){
            log.error("例行变更计划单暂存失败: "+e.getMessage());
            return AjaxResult.error("例行变更计划单暂存失败");
        }
        return AjaxResult.success("例行变更计划单暂存成功");

    }


    /**
     * 查看详情
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap map)
    {
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        SmBizTaskinfo smBizTaskinfo = smBizTaskinfoService.selectSmBizTaskinfoByTaskId(id);

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("smBizTaskinfo",smBizTaskinfo);
        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //执行时间进行日期回显
        String performDate = scheduling.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }


        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg!=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList",checkList);
        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());


        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        return prefix + "/detail";
    }


    /**
     * 删除前查询id
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        SmBizScheduling smBizScheduling = addlxbgService.selectSchedulingById(id);
        ajaxResult.put("data",smBizScheduling);
        return  ajaxResult;
    }


    /**
     * 根据id单个删除
     * @param id
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String id) {
        //根据传过来的id查询出作业表
        SmBizTask smBizTask =  taskService.selectSchedulingId(id);
        //根据查找的任务id批量删除任务表
        smBizTaskinfoService.deleteSmBizTaskinfoZyById(smBizTask.getTaskId());
        //根据查找的作业id删除作业表
        taskService.deleteById(smBizTask.getTaskId());
        //单个删除计划表
        int del = addlxbgService.deleteById(id);
        return toAjax(del);
    }


    /**
     * 导出例行变更计划
     * @param scheduling
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SmBizScheduling scheduling)
    {
        SmBizFolder smBizFolder = folderService.selectFolderTreeById(scheduling.getFolder());

        //获取当前登录人的id
        String pid =ShiroUtils.getOgUser().getpId();

        String isCurrentPage = (String)scheduling.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            //根据目录树ID回显计划表信息
            if(smBizFolder!=null){
                scheduling.setFolder(smBizFolder.getId_());
                scheduling.setCreatorId(pid);
            }else {
                scheduling.setFolder("1");
                scheduling.setCreatorId(pid);

            }
            startPage();
        }else if("all".equals(isCurrentPage)){
            //根据目录树ID回显计划表信息
            if(smBizFolder!=null){
                scheduling.setFolder(smBizFolder.getId_());
                scheduling.setCreatorId(pid);

            }else {
                scheduling.setFolder("1");
                scheduling.setCreatorId(pid);

            }
        }

        List<SmBizScheduling> list = addlxbgService.selectScheduling(scheduling);
        ExcelUtil<SmBizScheduling> util = new ExcelUtil<SmBizScheduling>(SmBizScheduling.class);
        return util.exportExcel(list, "例行变更计划");
    }


    /**
     * 转到快捷标签页面
     */
    @GetMapping("/label")
    public String label(ModelMap map)
    {

        return prefix + "/label";
    }


    /**
     * 跳转到执行机构页面
     */
    @GetMapping("/multiusers")
    public String multiusers() {
        return prefix + "/multiusers";
    }


    /**
     * 查询协同评估人
     */
    @PostMapping("/selectMultiusers/{orgIds}")
    @ResponseBody
    public TableDataInfo selectMultiusers(@PathVariable("orgIds") String orgIds, OgOrg ogOrg) {
        startPage();
        List<OgOrg> list = deptService.selectDeptLxbgList(orgIds,ogOrg);
        return getDataTable(list);
    }

    /**
     * 工作组选择页面
     *
     * @return
     */
    @GetMapping("/working/{groupIds}")
    public String working(@PathVariable("groupIds") String groupIds, ModelMap mmap) {
        mmap.addAttribute("groupIds", groupIds);
        return prefix + "/working";
    }

    /**
     * 查询接收工作组
     */
    @PostMapping("/selectWorking/{groupIds}")
    @ResponseBody
    public TableDataInfo selectMultiusers(@PathVariable("groupIds") String groupIds, OgGroup group) {
        startPage();
        List<OgGroup> list = workService.selectOgGroupLxbgList(groupIds,group);
        return getDataTable(list);
    }




    /**
     * 转到克隆页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/clonelxbg/{id}")
    public String clonelxbg(@PathVariable("id") String  id, ModelMap map)
    {
        String bizType = "ZDJH";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        map.addAttribute("num", bizType + nowDateStr + numStr);

        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        String createTime = DateUtils.getTime();
        map.addAttribute("createTime",createTime);
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);


        //生成计划表主键id
        map.put("schedulingId",UUID.getUUIDStr());

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);

        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //发布时间进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        return prefix + "/clonelxbg";
    }


    /**
     * 克隆保存
     * @param scheduling
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.INSERT)
    @PostMapping("/cloneAdd")
    @ResponseBody
    public AjaxResult clonelxbgsave( SmBizScheduling scheduling)
    {

        SmBizTask smBizTask = new SmBizTask();

        OgPerson ogPerson1  = ogPersonService.selectOgPersonById(scheduling.getCheckPersonId());
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        scheduling.setCreatorId(ShiroUtils.getOgUser().getpId());
        scheduling.setCreatorDeptId(ogPerson.getOrgId());
        scheduling.setInvalidationMark("1");

        if(StringUtils.isNotEmpty(scheduling.getCheckPersonId())){
            scheduling.setCheckPersonName(ogPerson1.getpName());
        }

        scheduling.setSchedulingId(scheduling.getSchedulingId());
        scheduling.setFolder(scheduling.getFolder());



        //根据机构生成执行机构id
        String masterOrgId=scheduling.getParams().get("masterOrgId")==null?"":scheduling.getParams().get("masterOrgId").toString();
        String[] masterOrgIds=masterOrgId.split(",");
        //根据工作组生成工作组id
        String workGroup=scheduling.getParams().get("workGroup")==null?"":scheduling.getParams().get("workGroup").toString();

        //根据机构生成
        if(StringUtils.isNotEmpty(scheduling.getSendRange()) && "1".equals(scheduling.getSendRange())){
            scheduling.setPlanType("1");
            String deptId = "";
            String deptName ="";
            //分割机构
            if(StringUtils.isNotEmpty(masterOrgId) ){
                //分割
                String[] deptId1 = masterOrgId.split(",");
                //判断
                if(deptId1.length>0){
                    for(String dId:deptId1){
                        if(StringUtils.isNotEmpty(dId)){
                            //根据遍历出来的机构id查询到机构名字
                            OgOrg ogOrg = iSysDeptService.selectDeptById(dId);
                            if(ogOrg!=null){
                                deptId+= dId+",";
                                deptName += ogOrg.getOrgName() + ",";
                            }
                        }
                    }
                }
            }
            if(StringUtils.isNotEmpty(deptId)){
                deptId = deptId.substring(0, deptId.length() - 1);
                smBizTask.setPerformDeptId(deptId);
            }
            if(StringUtils.isNotEmpty(deptName)){
                deptName = deptName.substring(0, deptName.length() - 1);
                smBizTask.setPerformDeptName(deptName);
            }

            smBizTask.setPerformGroupId("");
            smBizTask.setPerformGroupName("");
            smBizTask.setSendRange("1");

        }else if(StringUtils.isNotEmpty(scheduling.getSendRange())&&"2".equals(scheduling.getSendRange())){
            scheduling.setPlanType("1");
            smBizTask.setPerformGroupId(scheduling.getParams().get("workGroup").toString());
            smBizTask.setPerformGroupName(scheduling.getParams().get("workGroupName").toString());
            smBizTask.setPerformDeptId("");
            smBizTask.setPerformDeptName("");
            smBizTask.setSendRange("2");
        }else {
            scheduling.setPlanType("1");
            String deptId = "";
            String groupId = "";
            String deptName ="";
            String groupName ="";

            if(masterOrgId.contains("orgId") || masterOrgId.contains("grpId") ){

                for (String kj : masterOrgIds) {
                    if (kj.indexOf("orgId") > -1) {
                        String orgId = kj.substring(kj.indexOf(":") + 1, kj.length());
                        deptId += orgId + ",";
                    }
                    if (kj.indexOf("grpId") > -1) {
                        String grpId = kj.substring(kj.indexOf(":") + 1, kj.length());
                        groupId += grpId + ",";
                    }
                }
                if(StringUtils.isNotEmpty(deptId)){
                    deptId = deptId.substring(0, deptId.length() - 1);
                    smBizTask.setPerformDeptId(deptId);
                }
                if(StringUtils.isNotEmpty(groupId)){
                    groupId = groupId.substring(0, groupId.length() - 1);
                    smBizTask.setPerformGroupId(groupId);
                }

                String performDnameId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
                String[] performDnameIds=performDnameId.split(",");

                String performGroupNameId=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
                String[] performGroupNameIds=performGroupNameId.split(",");

                //根据机构id添加名字
                if(performDnameIds.length>0){
                    for(String nId : performDnameIds){
                        if(StringUtils.isNotEmpty(nId)){
                            //根据遍历出来的机构id查询到机构名字
                            OgOrg ogOrg = iSysDeptService.selectDeptById(nId);
                            if(ogOrg!=null){
                                deptName += ogOrg.getOrgName() + ",";
                            }
                        }
                    }
                }

                //根据工作组id添加名字
                if(performGroupNameIds.length>0){
                    for(String nId : performGroupNameIds){
                        if(StringUtils.isNotEmpty(nId)){
                            //根据遍历出来的工作组id查询到机构名字
                            OgGroup ogGroup = workService.selectOgGroupById(nId);
                            if(ogGroup!=null){
                                groupName += ogGroup.getGrpName() + ",";
                            }
                        }
                    }
                }

                if(StringUtils.isNotEmpty(deptName)){
                    deptName = deptName.substring(0, deptName.length() - 1);
                    smBizTask.setPerformDeptName(deptName);
                }
                if(StringUtils.isNotEmpty(groupName)){
                    groupName = groupName.substring(0, groupName.length() - 1);
                    smBizTask.setPerformGroupName(groupName);
                }

            }else {
                //分割机构
                if(StringUtils.isNotEmpty(masterOrgId) ){
                    //分割
                    String[] deptId1 = masterOrgId.split(",");
                    //判断
                    if(deptId1.length>0){
                        for(String dId:deptId1){
                            if(StringUtils.isNotEmpty(dId)){
                                //根据遍历出来的机构id查询到机构名字
                                OgOrg ogOrg = iSysDeptService.selectDeptById(dId);
                                if(ogOrg!=null){
                                    deptId+= dId+",";
                                    deptName += ogOrg.getOrgName() + ",";
                                }
                            }
                        }
                    }
                }


                //分割工作组
                if(StringUtils.isNotEmpty(masterOrgId) ){
                    String[] groupId1 = masterOrgId.split(",");
                    if(groupId1.length>0){
                        for(String gId:groupId1){
                            if(StringUtils.isNotEmpty(gId)){
                                //根据遍历出来的工作组id查询到工作组名字
                                OgGroup ogGroup = workService.selectOgGroupById(gId);
                                if(ogGroup!=null){
                                    groupId+=gId +",";
                                    groupName += ogGroup.getGrpName() + ",";
                                }
                            }
                        }
                    }
                }
                if(StringUtils.isNotEmpty(deptId)){
                    deptId = deptId.substring(0, deptId.length() - 1);
                    smBizTask.setPerformDeptId(deptId);
                }
                if(StringUtils.isNotEmpty(deptName)){
                    deptName = deptName.substring(0, deptName.length() - 1);
                    smBizTask.setPerformDeptName(deptName);
                }
                if(StringUtils.isNotEmpty(groupId)){
                    groupId = groupId.substring(0, groupId.length() - 1);
                    smBizTask.setPerformGroupId(groupId);
                }
                if(StringUtils.isNotEmpty(groupName)){
                    groupName = groupName.substring(0, groupName.length() - 1);
                    smBizTask.setPerformGroupName(groupName);
                }

            }

        }

        //生成作业表id
        smBizTask.setTaskId(UUID.getUUIDStr());
        smBizTask.setSchedulingId(scheduling.getSchedulingId());

        //作业表单号生成
        String bizType1 = "ZY";
        IdGenerator generator1 = new IdGenerator();
        String nowDateStr1 = DateUtils.dateTimeNow("yyyyMMdd");
        generator1.setCurrentDate(nowDateStr1);
        generator1.setBizType(bizType1);
        String numStr1 = idGeneratorService.selectIdGeneratorByType(generator1);
        //修改时间回显
        String updateTime = DateUtils.dateTimeNow();
        //自动生成的数据
        smBizTask.setTaskNo(bizType1+nowDateStr1+numStr1);
        smBizTask.setTaskTypeId(scheduling.getTaskTypeId());
        smBizTask.setMsgDoor(scheduling.getMsgDoor());
        smBizTask.setStartTime(scheduling.getStartTime());
        smBizTask.setSendRange(scheduling.getSendRange());
        smBizTask.setTaskTitle(scheduling.getSchedulingName());
        smBizTask.setTaskDescription(scheduling.getSchedulingDescription());
        smBizTask.setCharacter("0");
        smBizTask.setUpdateTime(updateTime);
        smBizTask.setInvalidationMark("1");
        smBizTask.setWorkStatus("01");
        smBizTask.setCreateTime(updateTime);
        smBizTask.setCreateOrgId(scheduling.getCreatorDeptId());
        smBizTask.setReceiveRoleName("例行变更计划执行人,");
        smBizTask.setReceiveRoleId("8300");

        //添加作业表
        taskService.insertTask(smBizTask);

        String performDeptId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
        String[] performDeptIds=performDeptId.split(",");

        //根据机构任务表生成
        if(performDeptIds.length>0){

            for(String fId:performDeptIds){
                if(StringUtils.isNotEmpty(fId)){
                    SmBizTaskinfo smBizTaskinfo=new SmBizTaskinfo();
                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    smBizTaskinfo.setPerformDeptId(fId);
                    smBizTaskinfo.setTaskFromNo(bizType+nowDateStr+numStr);
                    smBizTaskinfo.setTaskFormId(UUID.getUUIDStr());
                    smBizTaskinfo.setGenerateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setUpdateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setPerformDate(smBizTask.getStartTime());
                    smBizTaskinfo.setTaskId(smBizTask.getTaskId());
                    smBizTaskinfo.setTaskFormStatus("01");
                    smBizTaskinfo.setInvalidationMark("1");
                    smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo);
                    scheduling.setMasterOrgId("");
                }
            }
        }

        String performGroupId=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
        String[] performGroupIds=performGroupId.split(",");

        //根据工作组任务表生成
        if(performGroupIds.length>0){

            for(String gId:performGroupIds){
                if(StringUtils.isNotEmpty(gId)){
                    SmBizTaskinfo smBizTaskinfo=new SmBizTaskinfo();
                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    smBizTaskinfo.setTaskFromNo(bizType+nowDateStr+numStr);
                    smBizTaskinfo.setTaskFormId(UUID.getUUIDStr());
                    smBizTaskinfo.setGenerateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setUpdateTime(scheduling.getCreateTime());
                    smBizTaskinfo.setPerformDate(smBizTask.getStartTime());
                    smBizTaskinfo.setTaskId(smBizTask.getTaskId());
                    smBizTaskinfo.setPerformGroupId(gId);
                    smBizTaskinfo.setTaskFormStatus("01");
                    smBizTaskinfo.setInvalidationMark("1");
                    smBizTaskinfoService.insertSmBizTaskinfo(smBizTaskinfo);
                    scheduling.setWorkGroup("");
                }
            }
        }
        SmBizTaskinfo sf=new SmBizTaskinfo();
        sf.setTaskId(smBizTask.getTaskId());
        int taskNum=smBizTaskinfoService.selectSmBizTaskinfoListtwo(sf).size();
        scheduling.setProcess("0/"+taskNum);
        smBizTask.setProcess(scheduling.getProcess());


        //判断当前是否为暂存还是提交状态
        if("commit".equals(scheduling.getLabel())){
            scheduling.setPlanStatus("02");
            try{
                addlxbgService.insertLxbg(scheduling);
            }catch (Exception e){
                logger.error("克隆例行变更计划单新增失败 "+e.getMessage());
                throw  new BusinessException("克隆例行变更计划单新增失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");

        }
        try{
            scheduling.setPlanStatus("01");
            addlxbgService.insertLxbg(scheduling);
        }catch (Exception e){
            log.error("克隆例行变更计划单单暂存失败: "+e.getMessage());
            return AjaxResult.error("克隆例行变更计划单单暂存失败");
        }

        return AjaxResult.success("操作成功");

    }


    /**
     * 删除附件
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }






}
