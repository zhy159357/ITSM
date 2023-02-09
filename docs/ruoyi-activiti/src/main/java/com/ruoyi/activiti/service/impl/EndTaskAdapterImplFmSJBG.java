package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.CmBizSingleSJ;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.CmBizSingleSJService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据变更单
 */
@Service("endTaskAdapterImplFmSJBG")
public class EndTaskAdapterImplFmSJBG extends AbstractEndTaskAdapter {


    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;
    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<CmBizSingleSJVo> cmBizSingleSJVoList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            CmBizSingleSJVo cmBizSingleSJVo=new CmBizSingleSJVo();
            cmBizSingleSJVo.setChangeCode(number);
            cmBizSingleSJVoList=cmBizSingleSJService.list(cmBizSingleSJVo);
        }
        if(cmBizSingleSJVoList.size()>0){
            for(CmBizSingleSJVo cmBizSingleSJVo:cmBizSingleSJVoList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(cmBizSingleSJVo.getChangeSingleId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                            endTaskVo = new EndTaskVo();
                            endTaskVo.setBusinesskey(cmBizSingleSJVo.getChangeSingleId());
                            endTaskVo.setTaskType(ProcessKeyConstants.fmsjbg.getInfo());
                            endTaskVo.setTaskName(map.get("taskName").toString());
                            endTaskVo.setTaskNo(cmBizSingleSJVo.getChangeCode());
                            endTaskVo.setTaskTitle(cmBizSingleSJVo.getFmTitle());
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

            CmBizSingleSJ cmBizSingleSJ = new CmBizSingleSJ();
            cmBizSingleSJ.setChangeSingleId(id);
            cmBizSingleSJ.setChangeSingleStatus("20");
            cmBizSingleSJ.setInvalidationMark("0");
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除数据变更单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            CmBizSingleSJ cmBizSingleSJ = new CmBizSingleSJ();
            cmBizSingleSJ.setChangeSingleId(id);
            cmBizSingleSJ.setChangeSingleStatus("10");
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭数据变更单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

}
