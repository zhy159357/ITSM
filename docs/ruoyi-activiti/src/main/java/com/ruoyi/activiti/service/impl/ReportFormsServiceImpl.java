package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.ReportFormsMapper;
import com.ruoyi.activiti.service.ReportFormsService;
import com.ruoyi.common.core.domain.entity.FormsInspection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: ruoyi
 * @Package: com.ruoyi.activiti.service.impl
 * @Description: 作用类描述
 * @Author: xu_li
 * @CreatDate: 2022/3/8 16:20
 * Copyright: Copyright (c) 2022
 */
@Service
public class ReportFormsServiceImpl implements ReportFormsService {

    @Autowired
    private ReportFormsMapper reportFormsMapper;

    /**
     * 日常巡检记录单列表
     * @param formsInspection
     * @return
     */
    @Override
    public List<FormsInspection> selectInspectionList(FormsInspection formsInspection) {
        return reportFormsMapper.selectInspectionList(formsInspection);
    }

    /**
     * 根据id删除日常巡检记录
     * @param id
     * @return
     */
    @Override
    public int deleteInspectionById(String id) {
        return reportFormsMapper.deleteInspectionById(id);
    }

    /**
     * 新增记录
     * @param formsInspection
     * @return
     */
    @Override
    public int insertInspection(FormsInspection formsInspection) {
        return reportFormsMapper.insertInspection(formsInspection);
    }

    /**
     * 修改记录
     * @param formsInspection
     * @return
     */
    @Override
    public int updateInspection(FormsInspection formsInspection) {
        return reportFormsMapper.updateInspection(formsInspection);
    }

    /**
     * 根据id查询巡检记录
     * @param id
     * @return
     */
    @Override
    public FormsInspection selectFormsById(String id){
        return reportFormsMapper.selectFormsById(id);
    }
}
