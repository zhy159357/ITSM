package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.form.domain.IncidentBatchTemp;
import com.ruoyi.form.mapper.IncidentBatchMapper;
import com.ruoyi.form.service.IncidentBatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentBatchServiceImpl extends ServiceImpl<IncidentBatchMapper, IncidentBatchTemp> implements IncidentBatchService {
    @Override
    public List<IncidentBatchTemp> selectIncidentBatchTempList(IncidentBatchTemp batchTemp) {
        QueryWrapper<IncidentBatchTemp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("execute_status", batchTemp.getExecuteStatus());
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void insertIncidentBatch(List<IncidentBatchTemp> batchTempList) {
        this.saveBatch(batchTempList);
    }

    @Override
    public void updateIncidentBatchTempByEventNo(String executeStatus, String eventNo) {
        IncidentBatchTemp incidentBatchTemp = new IncidentBatchTemp();
        incidentBatchTemp.setEventNo(eventNo);
        incidentBatchTemp.setExecuteStatus(executeStatus);
        Wrapper<IncidentBatchTemp> updateWrapper = new UpdateWrapper<IncidentBatchTemp>().eq("event_no", eventNo);
        this.baseMapper.update(incidentBatchTemp, updateWrapper);
    }

    @Override
    public void deleteIncidentBatchTempBatch(String executeStatus) {
        Wrapper<IncidentBatchTemp> deleteWrapper = new QueryWrapper<IncidentBatchTemp>().eq("execute_status", executeStatus);
        this.baseMapper.delete(deleteWrapper);
    }
}
