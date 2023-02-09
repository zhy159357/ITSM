package com.ruoyi.activiti.controller.itsm.lxbg;

import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *审批例行变更计划
 */
@Controller
@RequestMapping("/lxbg/auditlxbg")
@Transactional(rollbackFor = Exception.class)
public class AuditLxbgController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AddlxbgController.class);



    //审批例行变更路径
    private String prefix = "lxbg/auditlxbg";

    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    /**
     * 转到审核例行变更页面
     * @param map
     * @return
     */
    @GetMapping()
    public String auditlxbg(ModelMap map)
    {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        map.put("pId",ogUser.getpId());

        return prefix + "/auditlxbg";
    }

    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo listauditlxbg(SmBizScheduling scheduling)
    {
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        startPage();
        List<SmBizScheduling> list = addlxbgService.selectScheduling(scheduling);
        //过滤条件
        List<SmBizScheduling> list1 = list.stream().filter((item) -> {
            return item.getCheckPersonId().equals(pId);
        }).collect(Collectors.toList());



        return getDataTable(list1);

    }

    /**
     * 转到待审核页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/lxbg/auditlxbg/{id}")
    public String edit(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //获取审核人
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPersonName = ogPersonService.selectOgPersonById(pid);

        String pname = ogPersonName.getpName();


        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("pname",pname);


        //执行日期进行日期回显
        String performDate = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }

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

        //执行日期进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        String checkTime = DateUtils.getTime();
        map.put("checkTime",checkTime);


        return prefix + "/authorlxbg";
    }


    /**
     * 审核是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/auditPass")
    @ResponseBody
    public AjaxResult auditPass(SmBizScheduling scheduling) {

        SmBizScheduling smBizScheduling = addlxbgService.selectSchedulingById(scheduling.getSchedulingId());

        if (!"02".equals(smBizScheduling.getPlanStatus())) {
            return AjaxResult.error("该事件单已被审核，请刷新列表检查后继续操作!");
        }else {

            String comment = (String) scheduling.getParams().get("comment");
            OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
            Map<String, Object> map = new HashMap<>();
            String status = "";
            scheduling.setCreatorDeptId(ogPerson.getOrgId());
            scheduling.setCheckTime(scheduling.getCheckTime());
            scheduling.setCheckDescription(comment);
            map.put("comment", comment);

            if ("01".equals(scheduling.getPlanStatus())) {
                // 此时流程走向已发布
                status = "04";
                scheduling.setPlanStatus(status);

                scheduling.setPlanCheckpass("审核通过");
                try{
                    addlxbgService.updatelxbg(scheduling);
                }catch (Exception e){
                    log.error("例行变更计划单审核失败: "+e.getMessage());
                    throw  new BusinessException("例行变更计划单审核失败,请刷新页面进行重试");
                }
                return AjaxResult.success("操作成功");

            } else {
                // 此时流程走向为退回
                status = "03";
                scheduling.setPlanStatus(status);
                try{
                    addlxbgService.updatelxbg(scheduling);
                }catch (Exception e){
                    log.error("例行变更计划单审核退回失败: "+e.getMessage());
                    throw  new BusinessException("例行变更计划单审核退回失败,请刷新页面进行重试");
                }
                return AjaxResult.success("操作成功");

            }

        }
    }







}
