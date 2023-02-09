package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("endTaskAdapterImplFmBiz")
public class EndTaskAdapterImplFmBiz extends AbstractEndTaskAdapter  {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IFmBizService fmBizService;



    @Override
    public TableDataInfo getEndPagerTasks(String processKey,String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<FmBiz> fmBizList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            FmBiz fmBiz=new FmBiz();
            fmBiz.setFaultNo(number);
            fmBizList=fmBizService.selectFmBizList(fmBiz);
        }
        if(fmBizList.size()>0){
            for(FmBiz fmBiz:fmBizList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(fmBiz.getFmId());
                if(StringUtils.isNotEmpty(maps)){
                    for (Map<String, Object> map : maps) {
                        endTaskVo  = new EndTaskVo();
                        endTaskVo.setBusinesskey(fmBiz.getFmId());
                        endTaskVo.setTaskType(ProcessKeyConstants.fmbiz.getInfo());
                        endTaskVo.setTaskName(map.get("taskName").toString());
                        endTaskVo.setTaskNo(fmBiz.getFaultNo());
                        endTaskVo.setTaskTitle(fmBiz.getFaultDecriptSummary());
                        Date createTime =(Date) map.get("createTime");
                        String geneterTime = DateUtils.formatDate(createTime,DateUtils.YYYY_MM_DD_HH_MM_SS);
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
            fmBizService.updateFmBizByInvalidationMark(id);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删业务事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            FmBiz fmBiz = new FmBiz();
            fmBiz.setFmId(id);
            fmBiz.setCurrentState("09");
            fmBizService.updateFmBiz(fmBiz);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭业务事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

}
