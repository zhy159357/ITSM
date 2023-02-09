package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资源变更单
 */
@Service("endTaskAdapterImplCmZy")
public class EndTaskAdapterImplCmZy extends AbstractEndTaskAdapter {
    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ICmBizSingleService cmBizSingleService;
    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<CmBizSingle> cmBizSingleList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            CmBizSingle cmBizSingle=new CmBizSingle();
            cmBizSingle.setChangeCode(number);
            cmBizSingleList=cmBizSingleService.selectCmBizSingleList(cmBizSingle);
        }
        if(cmBizSingleList.size()>0){
            for(CmBizSingle cmBizSingle:cmBizSingleList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(cmBizSingle.getChangeId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        endTaskVo = new EndTaskVo();
                        endTaskVo.setBusinesskey(cmBizSingle.getChangeId());
                        endTaskVo.setTaskType(ProcessKeyConstants.cmzy.getInfo());
                        endTaskVo.setTaskName(map.get("taskName").toString());
                        endTaskVo.setTaskNo(cmBizSingle.getChangeCode());
                        endTaskVo.setTaskTitle(cmBizSingle.getChangeSingleName());
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

            CmBizSingle cmBizSingle = new CmBizSingle();
            cmBizSingle.setChangeId(id);
            cmBizSingle.setInvalidationMark("0");
            cmBizSingleService.updateCmBizSingle(cmBizSingle);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除资源变更单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            CmBizSingle cmBizSingle = new CmBizSingle();
            cmBizSingle.setChangeId(id);
            cmBizSingle.setChangeSingleStauts("0801");
            cmBizSingleService.updateCmBizSingle(cmBizSingle);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭资源变更单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

}
