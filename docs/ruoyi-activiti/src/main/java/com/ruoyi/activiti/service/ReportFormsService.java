package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FormsInspection;

import java.util.List;

/**
 * @ProjectName: 报表管理接口
 * @Package: com.ruoyi.activiti.service
 * @Description: 作用类描述
 * @Author: xu_li
 * @CreatDate: 2022/3/8 16:19
 * Copyright: Copyright (c) 2022
 */
public interface ReportFormsService {

    /**
     * 查询日常巡检记录单
     * @param formsInspection
     * @return
     */
    List<FormsInspection> selectInspectionList(FormsInspection formsInspection);

    /**
     * 根据id删除日常巡检记录单
     * @param id
     * @return
     */
    int deleteInspectionById(String id);

    /**
     * 新增日常巡检记录单
     * @param formsInspection
     * @return
     */
    int insertInspection(FormsInspection formsInspection);

    /**
     * 修改日常巡检记录单
     * @param formsInspection
     * @return
     */
    int updateInspection(FormsInspection formsInspection);

    /**
     * 根据id查询巡检记录
     * @param id
     * @return
     */
    FormsInspection selectFormsById(String id);
}
