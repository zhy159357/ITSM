package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("benchAdapterImplLxBg")
public class BenchAdapterImplLxBg extends AbstractBenchAdapter {

    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysWorkService workService;


    @Override
    public boolean isMainTask() {
        return true;
    }




    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("8100");
        roles.add("8200");
        roles.add("8300");
        roles.add("8400");
        roles.add("8500");
        roles.add("8600");
        roles.add("8700");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "LXBG";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {
        List<BenchTask> records = new ArrayList<BenchTask>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //计划单
        List<SmBizScheduling> list = new ArrayList();

        //查出所有计划单
        SmBizScheduling smBizScheduling = new SmBizScheduling();
        //查询修改
        smBizScheduling.setPlanStatus("03");
        smBizScheduling.setCreatorId(userId);
        list.addAll(addlxbgService.selectScheduling(smBizScheduling));

        //查询审核
        SmBizScheduling bizScheduling = new SmBizScheduling();
        bizScheduling.setPlanStatus("02");
        bizScheduling.setCheckPersonId(userId);
        list.addAll(addlxbgService.selectScheduling(bizScheduling));

        //查出所有任务单
        SmBizTaskinfo smBizTaskinfo = new SmBizTaskinfo();
        smBizTaskinfo.setTaskFormStatus("02");
        List<SmBizTaskinfo> bizTaskinfos = smBizTaskinfoService.selectSmBizTaskinfoListtwo(smBizTaskinfo);

        //根据当前登录人的机构或工作组过滤
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //当前登录人的工作组信息
        List<OgGroup> ogGroup =  workService.selectGroupByUserId(pId);

        List<String>  groupIds= new ArrayList<>();
        for (OgGroup ogGroup2: ogGroup){
            groupIds.add(ogGroup2.getGroupId());
        }
        List<SmBizTaskinfo> smBizTaskinfoList = bizTaskinfos.stream().filter((item) -> {
            return groupIds.contains( item.getPerformGroupId()) || ogOrg.getOrgId().equals(item.getPerformDeptId());
        }).collect(Collectors.toList());




        if (!list.isEmpty()) {
            BenchTask benchTask = null;
            for (SmBizScheduling ss : list) {
                if (ss != null) {
                    benchTask = new BenchTask();
                    benchTask.setType("LXBG");//任务类型
                    benchTask.setBizId(ss.getSchedulingId());//业务ID
                    benchTask.setNumber(ss.getSchedulingNo());//单号
                    benchTask.setTitle(ss.getSchedulingName());//标题
                    if ("03".equals(ss.getPlanStatus())){
                        benchTask.setTaskName("退回待修改");//任务名称
                    }
                    if("02".equals(ss.getPlanStatus())){
                        benchTask.setTaskName("待审批");//任务名称
                    }
                    benchTask.setPrePerformTime(ss.getCreateTime());//上一步操作时间
                    records.add(benchTask);
                }

                }
            }


        if (!smBizTaskinfoList.isEmpty()) {
            BenchTask benchTask = null;
            for (SmBizTaskinfo taskinfo : smBizTaskinfoList) {
                if (taskinfo != null) {
                    benchTask = new BenchTask();
                    benchTask.setType("LXBG");//任务类型
                    benchTask.setBizId(taskinfo.getTaskFormId());//业务ID
                    benchTask.setNumber(taskinfo.getTaskFromNo());//单号
                    benchTask.setTitle(taskinfo.getSmBizTask().getTaskTitle());//标题
                    if("02".equals(taskinfo.getTaskFormStatus())){
                        benchTask.setTaskName("待填报");//任务名称
                    }
                    benchTask.setPrePerformTime(taskinfo.getGenerateTime());//上一步操作时间
                    records.add(benchTask);
                }

            }
        }

        if (!CollectionUtils.isEmpty(records)) {
            Collections.sort(records, new Comparator<BenchTask>() {
                @Override
                public int compare(BenchTask o1, BenchTask o2) {
                    String createTime1 = o1.getPrePerformTime();
                    String createTime2 = o2.getPrePerformTime();
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return getDataTable_ideal(records);
    }
}
