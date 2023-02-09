package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应用问题单
 */
@Service("endTaskAdapterImplPMYY")
public class EndTaskAdapterImplPMYY extends AbstractEndTaskAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IImBizIssuefxService issueService;

    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<ImBizIssuefx> imBizIssuefxList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            ImBizIssuefx imBizIssuefx=new ImBizIssuefx();
            imBizIssuefx.setIssuefxNo(number);
            imBizIssuefxList=issueService.selectIssueList(imBizIssuefx);
        }
        if(imBizIssuefxList.size()>0){
            for(ImBizIssuefx imBizIssuefx:imBizIssuefxList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(imBizIssuefx.getIssuefxId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        endTaskVo = new EndTaskVo();
                        endTaskVo.setBusinesskey(imBizIssuefx.getIssuefxId());
                        endTaskVo.setTaskType(ProcessKeyConstants.pmyy.getInfo());
                        endTaskVo.setTaskName(map.get("taskName").toString());
                        endTaskVo.setTaskNo(imBizIssuefx.getIssuefxNo());
                        endTaskVo.setTaskTitle(imBizIssuefx.getIssuefxName());
                        Date createTime = (Date)map.get("createTime");
                        String geneterTime = DateUtils.formatDate(createTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
                        endTaskVo.setTaskGeneterTime(geneterTime);
                        endTaskVos.add(endTaskVo);
                    }
                }
            }
            return getDataTable_ideal(endTaskVos);
        }
        return getDataTable_ideal(new ArrayList<>());
    }

    @Override
    public int remove(String id) {
        try{
            ImBizIssuefx imBizIssuefx = new ImBizIssuefx();
            imBizIssuefx.setIssuefxId(id);
            imBizIssuefx.setCurrentState("11");
            issueService.updateIssue(imBizIssuefx);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除应用问题单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            ImBizIssuefx imBizIssuefx = new ImBizIssuefx();
            imBizIssuefx.setIssuefxId(id);
            imBizIssuefx.setCurrentState("11");
            issueService.updateIssue(imBizIssuefx);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭应用问题单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }
}
