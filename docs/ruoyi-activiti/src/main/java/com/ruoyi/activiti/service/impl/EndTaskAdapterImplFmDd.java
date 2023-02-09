package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.FmDispatchService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmDd;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("endTaskAdapterImplFmDd")
public class EndTaskAdapterImplFmDd extends AbstractEndTaskAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private FmDispatchService dispatchService;

    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<FmDd> fmDdList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            FmDd fmDd=new FmDd();
            fmDd.setFaultNo(number);
            fmDdList=dispatchService.selectDispatchList(fmDd);
        }
        if(fmDdList.size()>0){
            for(FmDd fmDd:fmDdList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(fmDd.getFmddId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        endTaskVo = new EndTaskVo();
                        endTaskVo.setBusinesskey(fmDd.getFmddId());
                        endTaskVo.setTaskType(ProcessKeyConstants.fmdd.getInfo());
                        endTaskVo.setTaskName(map.get("taskName").toString());
                        endTaskVo.setTaskNo(fmDd.getFaultNo());
                        endTaskVo.setTaskTitle(fmDd.getFaultTitle());
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
            dispatchService.updateFmDdByInvalidationMark(id);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除调度事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            FmDd fmDd = new FmDd();
            fmDd.setFmddId(id);
            fmDd.setCurrentState("9");
            dispatchService.updateDispatch(fmDd);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭调度事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

}
