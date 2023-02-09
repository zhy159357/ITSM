package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.domain.FmSw;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSwService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务事件单结束流程
 */
@Service("endTaskAdapterImplFmSw")
public class EndTaskAdapterImplFmSw extends AbstractEndTaskAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IFmSwService fmSwService;

    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<FmSw> fmSwList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            FmSw fmSw=new FmSw();
            fmSw.setFaultNo(number);
            fmSwList=fmSwService.selectFmSwList(fmSw);
        }
        if(fmSwList.size()>0){
            for(FmSw fmSw:fmSwList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(fmSw.getFmSwId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        if ("1".equals(fmSw.getInvalidationMark())) {
                            endTaskVo = new EndTaskVo();
                            endTaskVo.setBusinesskey(fmSw.getFmSwId());
                            endTaskVo.setTaskType(ProcessKeyConstants.fmsw.getInfo());
                            endTaskVo.setTaskName(map.get("taskName").toString());
                            endTaskVo.setTaskNo(fmSw.getFaultNo());
                            endTaskVo.setTaskTitle(fmSw.getFaultTitle());
                            Date createTime = (Date)map.get("createTime");
                            String geneterTime = DateUtils.formatDate(createTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
                            endTaskVo.setTaskGeneterTime(geneterTime);
                            endTaskVos.add(endTaskVo);
                        }
                    }
                }
            }
            return getDataTable_ideal(endTaskVos);
        }
        return getDataTable_ideal(new ArrayList<>());
    }


    @Override
    @Transactional()
    public int remove(String id) {
        try{
            //逻辑删除事务事件单事件单
            fmSwService.updateFmSwByInvalidationMark(id);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除事务事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    @Transactional()
    public int close(String id) {
        try{
            FmSw fmSw = new FmSw();
            fmSw.setFmSwId(id);
            fmSw.setCurrentState("05");
            //修改事务事件单的状态为关闭状态
            fmSwService.invalidateUpdateFmSw(fmSw);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭事务事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }
}
