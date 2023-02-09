package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 发布管理
 */
@Service("endTaskAdapterImplVmBn")
public class EndTaskAdapterImplVmBn extends AbstractEndTaskAdapter {
    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<VmBizInfo> vmBizInfoList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            VmBizInfo vmBizInfo=new VmBizInfo();
            vmBizInfo.setVersionInfoNo(number);
            vmBizInfoList=vmBizInfoService.selectVmBizInfoList(vmBizInfo);
        }
        if (vmBizInfoList.size()>0) {
            for(VmBizInfo vmBizInfo:vmBizInfoList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(vmBizInfo.getVersionInfoId());
                if(StringUtils.isNotEmpty(maps)) {
                    for (Map<String, Object> map : maps) {
                            endTaskVo = new EndTaskVo();
                            endTaskVo.setBusinesskey(vmBizInfo.getVersionInfoId());
                            endTaskVo.setTaskType(ProcessKeyConstants.vmbn.getInfo());
                            endTaskVo.setTaskName(map.get("taskName").toString());
                            endTaskVo.setTaskNo(vmBizInfo.getVersionInfoNo());
                            endTaskVo.setTaskTitle(vmBizInfo.getVersionName());
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
    @Transactional(rollbackFor = Exception.class)
    public int remove(String id) {
        try {
            VmBizInfo vmBizInfo = new VmBizInfo();
            vmBizInfo.setVersionInfoId(id);
            vmBizInfo.setInvalidationMark("0");
            vmBizInfoService.updateVmBizInfo(vmBizInfo);
            //删除任务
            activitiCommService.deleteProcess(id, "强制删除版本发布");
            return AjaxResult.Type.SUCCESS.value();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("强制删除版本发布异常！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int close(String id) {
        try {
            // 关闭版本单
            vmBizInfoService.closeVmBizVersion(id);
            //关闭任务
            activitiCommService.deleteProcess(id, "强制关闭版本发布");
            return AjaxResult.Type.SUCCESS.value();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("强制关闭版本发布单异常！");
        }
    }
}
