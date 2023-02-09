package com.ruoyi.activiti.controller.itsm.personDuty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.OgPersonDuty;
import com.ruoyi.activiti.service.IOgPersonDutyService;
import com.ruoyi.activiti.service.ItsmWorkStatusService;
import com.ruoyi.activiti.service.impl.ActivitiCommServiceImpl;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.system.service.*;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.enums.BusinessType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 监控值班Controller
 * 
 * @author ruoyi
 * @date 2021-06-25
 */
@Controller
@RequestMapping("/monitor/dutyRole")
public class OgPersonDutyController extends BaseController
{
    private String prefix = "monitor/dutyRole";

    @Autowired
    private IOgPersonDutyService ogPersonDutyService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private ItsmWorkStatusService workStatusService;

    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ActivitiCommServiceImpl activitiCommService;
    @Autowired
    private IOgUserPostService iOgUserPostService;
    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;
    @GetMapping()
    public String duty()
    {
        return prefix + "/dutyRole";
    }

    /**
     * 查询所有监控值班列表数据
     */
    @PostMapping("/selectDutyList/{userId}")
    @ResponseBody
    public TableDataInfo selectDutyList(@PathVariable("userId") String userId)
    {
        List<OgPersonDuty> list = new ArrayList<>();
        if("0".equals(userId)){
            userId=ShiroUtils.getUserId();
        }
        boolean dataCenter = workStatusService.isDataCenter(userId);
        List<OgGroup> groupList = workService.selectGroupByUserId(userId);
        for(OgGroup group : groupList){
            if("1".equals(String.valueOf(group.getInvalidationMark()))){
                OgPersonDuty ogPersonDuty = new OgPersonDuty();
                ogPersonDuty.setGroupId(group.getGroupId());
                ogPersonDuty.setDutyName(group.getGrpName());
                ogPersonDuty.setDutyType(group.getGroupType());
                list.add(ogPersonDuty);
            }
        }
        // 数据中心查询工作组和监控类型的数据
        if(dataCenter){
            // 此处需要查询监控类型信息(现阶段考虑放到字典表中)
            List<PubParaValue> typeList = pubParaValueService.selectPubParaValueByParaName("duty_type");
            for(PubParaValue value : typeList){
                OgPersonDuty ogPersonDuty = new OgPersonDuty();
                ogPersonDuty.setMonitorTypeAccountId(value.getParaValueId());
                ogPersonDuty.setDutyName(value.getValueDetail());
                if("7".equals(value.getValue())){
                    ogPersonDuty.setDutyType("6");
                }else{
                    ogPersonDuty.setDutyType("5");
                }
                list.add(ogPersonDuty);
            }
        }
        return getDataTable_ideal(list);
    }

    /**
     * 查看自己所在的值班列表信息
     * @param ogPersonDuty
     * @return
     */
    @PostMapping("/selectMyDutyList")
    @ResponseBody
    public TableDataInfo selectMyDutyList(OgPersonDuty ogPersonDuty){
        startPage();
        ogPersonDuty.setUserId(ShiroUtils.getUserId());
        List<OgPersonDuty> dutyList = ogPersonDutyService.selectOgPersonDutyList(ogPersonDuty);
        return getDataTable(dutyList);
    }

    /**
     * 新增监控值班
     */
    @GetMapping("/addGain")
    public String add()
    {
        return prefix + "/gain";
    }

    /**
     * 新增保存监控值班组人员关系
     */
    @PostMapping("/addDuty")
    @ResponseBody
    public AjaxResult addDuty(String groups)
    {
        System.out.println(groups);
        JSONArray gropuJs= JSONArray.parseArray(groups);
        for(int i=0;i<gropuJs.size(); i++){
            JSONObject group=gropuJs.getJSONObject(i);
            OgPersonDuty ogPersonDuty=new OgPersonDuty();
            ogPersonDuty.setGroupId(group.getString("groupId"));
            ogPersonDuty.setMonitorTypeAccountId(group.getString("monitorTypeAccountId"));
            ogPersonDuty.setUserId(ShiroUtils.getUserId());
            ogPersonDuty.setDutyName(group.getString("dutyName"));
            ogPersonDuty.setDutyType(group.getString("dutyType"));
            OgPerson ogPerson=ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
            if(StringUtils.isNotEmpty(ogPerson.getPhone())){
                ogPersonDuty.setTel(ogPerson.getPhone());
            }else {
                ogPersonDuty.setTel(ogPerson.getMobilPhone());
            }
            if(StringUtils.isNotEmpty(ogPersonDuty.getGroupId()) || StringUtils.isNotEmpty(ogPersonDuty.getMonitorTypeAccountId())){
                // 根据groupId或者类型id判断值班组表中数据是否重复
                if(ogPersonDutyService.judgeDutyRepeat(ogPersonDuty)){
                    return AjaxResult.error("值班组名称：" + ogPersonDuty.getDutyName() + "已添加，不可重复添加值班组信息！");
                }
            } else {
                return AjaxResult.error("工作组信息或监控类型信息不可为空！");
            }
            ogPersonDuty.setOperateTime(new Date());
            ogPersonDuty.setDutyId(UUID.getUUIDStr());
            ogPersonDuty.setPid(ShiroUtils.getOgUser().getpId());
           ogPersonDutyService.insertOgPersonDuty(ogPersonDuty);
        }
        /*;*/
        return success();
    }

    /**
     * 删除监控值班
     */
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        String[] idarry=ids.split(",");
        for(String id:idarry){
            Map map = ogPersonDutyService.deleteOgPersonDutyByIds(id, ShiroUtils.getUserId());
            if((boolean)map.get("flag")){
                return AjaxResult.error(map.get("message").toString());
            }
        }
        return success();
    }

    /**
     * 首页点击获取值班组角色进入我的值班组列表展示界面
     * @return
     */
    @GetMapping("/myDutyRole")
    public String gainRole(){
        return prefix + "/myDutyRole";
    }
    /**
     * 条件查询所有人员及工作组
     */
    @GetMapping("/pageList")
    public String pageList(ModelMap mp){
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(ShiroUtils.getUserId());
        List<OgUserPost> ogUserPosts = iOgUserPostService.selectPostByUserId(ogUserPost);
        //循环
        for(OgUserPost post : ogUserPosts){
            //判断查询出的岗位信息是否为数据中心
            if("0001".equals(post.getPostId())){
                mp.put("admin",true);
            }
        }
        return prefix+"/dutyGroup";
    }
    /**
     * 条件查询所有人员及工作组
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgPersonDuty ogPersonDuty)
    {
        startPage();
        if(StringUtils.isNotEmpty(ogPersonDuty.getGroupId())){
            String[] groupId=ogPersonDuty.getGroupId().split(",");
            List<String> statecollection= Arrays.stream(groupId).collect(Collectors.toList());
            ogPersonDuty.getParams().put("groupIds",statecollection);
        }
        if(!ObjectUtils.isEmpty(ogPersonDuty.getParams().get("dutyType"))){
            String[] dutyType=String.valueOf(ogPersonDuty.getParams().get("dutyType")).split(",");
            List<String> dutyCollection= Arrays.stream(dutyType).collect(Collectors.toList());
            ogPersonDuty.getParams().put("monitorIds",dutyCollection);
        }
        List<OgPersonDuty> list = ogPersonDutyService.selectOgPersonDutyGroupList(ogPersonDuty);
        for(OgPersonDuty dt:list){
            List<Task> tasks = activitiCommService.allTasks(dt.getUserId(), "FmYx");
            dt.getParams().put("task",tasks.size());
        }
        return getDataTable(list);
    }
    /**
     * 新增监控   运行值班人
     */
    @GetMapping("/openAdd")
    public String openAdd()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存监控  运行值班人
     */
    @Log(title = "监控值班", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgPersonDuty ogPersonDuty)
    {
        ogPersonDuty.setOperateTime(new Date());
        ogPersonDuty.setDutyId(UUID.getUUIDStr());
        OgPerson ogPerson=ogPersonService.selectOgPersonById(ogPersonDuty.getUserId());
        if(StringUtils.isNotEmpty(ogPerson.getPhone())){
            ogPersonDuty.setTel(ogPerson.getPhone());
        }else {
            ogPersonDuty.setTel(ogPerson.getMobilPhone());
        }
        String dutyTypeOriginal=ogPersonDuty.getParams().get("dutyTypeOriginal")==null?"":ogPersonDuty.getParams().get("dutyTypeOriginal").toString();
        if("5".equals(dutyTypeOriginal)||"6".equals(dutyTypeOriginal)){
            ogPersonDuty.setMonitorTypeAccountId(ogPersonDuty.getGroupId());
            ogPersonDuty.setGroupId("");
        }
        return toAjax(ogPersonDutyService.insertOgPersonDuty(ogPersonDuty));
    }

    /**
     * 修改监控运行值班人
     */
    @GetMapping("/edit/{dutyId}")
    public String edit(@PathVariable("dutyId") String dutyId, ModelMap mmap)
    {
        OgPersonDuty ogPersonDuty = ogPersonDutyService.selectOgPersonDutyById(dutyId);
        OgPerson ogPerson=ogPersonService.selectOgPersonById(ogPersonDuty.getUserId());
        ogPersonDuty.getParams().put("pName",ogPerson.getpName());
        mmap.put("ogPersonDuty", ogPersonDuty);
        return prefix + "/edit";
    }

    /**
     * 修改保存监控 运行值班
     */
    @Log(title = "监控值班", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgPersonDuty ogPersonDuty)
    {
        ogPersonDuty.setOperateTime(new Date());
        OgPerson ogPerson=ogPersonService.selectOgPersonById(ogPersonDuty.getUserId());
        if(StringUtils.isNotEmpty(ogPerson.getPhone())){
            ogPersonDuty.setTel(ogPerson.getPhone());
        }else {
            ogPersonDuty.setTel(ogPerson.getMobilPhone());
        }
        String dutyTypeOriginal=ogPersonDuty.getParams().get("dutyTypeOriginal")==null?"":ogPersonDuty.getParams().get("dutyTypeOriginal").toString();
        if("5".equals(dutyTypeOriginal)||"6".equals(dutyTypeOriginal)){
            ogPersonDuty.setMonitorTypeAccountId(ogPersonDuty.getGroupId());
            ogPersonDuty.setGroupId("");
        }
        return toAjax(ogPersonDutyService.updateOgPersonDuty(ogPersonDuty));
    }

    /**
     * 删除监控  运行值班
     */
    @Log(title = "监控值班", businessType = BusinessType.DELETE)
    @PostMapping( "/removeDuty")
    @ResponseBody
    public AjaxResult removeDuty(String ids)
    {
        return toAjax(ogPersonDutyService.deleteOgPersonDutyByIds(ids));
    }
    /**
     * 选择工作组
     */
    @GetMapping("/choseGroup/{userId}")
    public String choseGroup(@PathVariable("userId")String userId,ModelMap map){
        map.put("userId",userId);
        return prefix+"/userGroup";
    }
    /**
     * 条件查询工作组
     */
    @GetMapping("/workGroup")
    public String workGroup(){
        return prefix+"/workGroup";
    }
    /**
     *
     * @param
     * @param mmap
     * @return
     */
    @GetMapping("/sysGrouplist")
    public String sysGrouplist(ModelMap mmap){
        return prefix+"/selectOneApplication";
    }
    @PostMapping("/listWork")
    @ResponseBody
    public TableDataInfo listWork(OgGroup group) {
        startPage();
        if(StringUtils.isNotEmpty(group.getSysId())){
            group.getParams().put("sysid",Arrays.stream(group.getSysId().split(",")).collect(Collectors.toList()));
        }
        List<OgGroup> list = workService.selectOgGroupListSysIdIn(group);
        List<OgGroup> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (OgGroup gp : list) {
                if (StringUtils.isNotEmpty(gp.getSysId())) {
                    if (gp.getSysId().contains(",")) {
                        gp.setSysId("多系统");
                    } else {
                        OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(gp.getSysId());
                        if(ogSys!=null){
                            String caption = ogSys.getCaption();
                            if(StringUtils.isNotEmpty(caption)){
                                gp.setSysId(caption);
                            }
                        }

                    }
                }
                list2.add(gp);
            }
        }
        return getDataTable(list2,list);
    }
}
