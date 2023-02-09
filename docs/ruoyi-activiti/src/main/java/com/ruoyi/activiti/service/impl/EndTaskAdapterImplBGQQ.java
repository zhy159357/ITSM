package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICmBizQingqiuService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("endTaskAdapterImplBGQQ")
public class EndTaskAdapterImplBGQQ extends AbstractEndTaskAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ICmBizQingqiuService cmBizQingqiuService;
    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<CmBizQingqiu> cmBizQingqiuList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            CmBizQingqiu cmBizQingqiu=new CmBizQingqiu();
            cmBizQingqiu.setChangeCode(number);
            cmBizQingqiuList=cmBizQingqiuService.selectCmBizQingqiuList(cmBizQingqiu);
        }
        if(cmBizQingqiuList.size()>0){
            for(CmBizQingqiu cmBizQingqiu:cmBizQingqiuList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(cmBizQingqiu.getChangeId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                        endTaskVo = new EndTaskVo();
                        endTaskVo.setBusinesskey(cmBizQingqiu.getChangeId());
                        endTaskVo.setTaskType(ProcessKeyConstants.BGQQ.getInfo());
                        endTaskVo.setTaskName(map.get("taskName").toString());
                        endTaskVo.setTaskNo(cmBizQingqiu.getChangeCode());
                        endTaskVo.setTaskTitle(cmBizQingqiu.getChangeSingleName());
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

            CmBizQingqiu cmBizQingqiu  = new CmBizQingqiu();
            cmBizQingqiu.setChangeId(id);
            cmBizQingqiu.setInvalidationMark("0");
            cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除变更请求单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            CmBizQingqiu cmBizQingqiu  = new CmBizQingqiu();
            cmBizQingqiu.setChangeId(id);
            cmBizQingqiu.setStauts("5");
            cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭变更请求单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }
}
