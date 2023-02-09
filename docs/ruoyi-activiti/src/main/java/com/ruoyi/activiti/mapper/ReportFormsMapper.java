package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.FmBizJJAppr;
import com.ruoyi.common.core.domain.entity.*;

import java.util.List;

/**
 * 报表管理Mapper接口
 *
 * @author ruoyi
 * @date 2022-3-8
 */
public interface ReportFormsMapper {

    /**
     * 查询日常巡检记录单
     *
     * @return 日常巡检记录单
     */
    List<FormsInspection> selectInspectionList(FormsInspection formsInspection);

    /**
     * 根据id删除日常巡检记录单
     *
     * @return
     */
    int deleteInspectionById(String id);

    /**
     * 新增记录
     *
     * @return
     */
    int insertInspection(FormsInspection formsInspection);

    /**
     * 根据id删除日常巡检记录单
     *
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
