package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.CheckSheet;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICheckSheetService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 隐患排查单
 */
@Service("endTaskAdapterImplPMYH")
public class EndTaskAdapterImplPMYH extends AbstractEndTaskAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ICheckSheetService checkSheetService;

    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<CheckSheet> checkSheetList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            CheckSheet checkSheet=new CheckSheet();
            checkSheet.setCsNo(number);
            checkSheetList=checkSheetService.selectCheckSheetList(checkSheet);
        }
        if(checkSheetList.size()>0){
            for(CheckSheet checkSheet:checkSheetList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(checkSheet.getCsId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        endTaskVo = new EndTaskVo();
                        endTaskVo.setBusinesskey(checkSheet.getCsId());
                        endTaskVo.setTaskType(ProcessKeyConstants.pmyh.getInfo());
                        endTaskVo.setTaskName(map.get("taskName").toString());
                        endTaskVo.setTaskNo(checkSheet.getCsNo());
                        endTaskVo.setTaskTitle(checkSheet.getCheckName());
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
            CheckSheet checkSheet = new CheckSheet();
            checkSheet.setCsId(id);
            checkSheet.setInvalidationMark("0");
            checkSheetService.updateCheckSheet(checkSheet);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除隐患排查单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            CheckSheet checkSheet = new CheckSheet();
            checkSheet.setCsId(id);
            checkSheet.setStatus("5");
            checkSheetService.updateCheckSheet(checkSheet);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭隐患排查单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }
}
