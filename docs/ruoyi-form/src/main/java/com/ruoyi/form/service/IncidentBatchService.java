package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.domain.IncidentBatchTemp;

import java.util.List;

public interface IncidentBatchService extends IService<IncidentBatchTemp> {

    List<IncidentBatchTemp> selectIncidentBatchTempList(IncidentBatchTemp batchTemp);

    void insertIncidentBatch(List<IncidentBatchTemp> batchTempList);

    void updateIncidentBatchTempByEventNo(String executeStatus, String eventNo);

    void deleteIncidentBatchTempBatch(String executeStatus);
}
