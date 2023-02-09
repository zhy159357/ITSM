package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.form.domain.DesignBizIncident;
import com.ruoyi.form.vo.IncidentRetrievalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DesignBizIncidentMapper extends BaseMapper<DesignBizIncident> {
    List<Map<String,Object>> incidentRetrieval(IncidentRetrievalVo retrievalVo);

    IPage<Map<String,Object>> incidentPageRetrieval(Page page, @Param("queryObject") IncidentRetrievalVo retrievalVo);

    IPage<Map<String,Object>> summaryConsoleDesk(Page page, @Param("createBy")String createBy);

    IPage<Map<String,Object>> summaryTodoConsoleDesk(Page page, @Param("todoPersonal")String todoPersonal);

    IPage<Map<String,Object>> todoGroupConsole(Page page, @Param("userId") String userId);

    List<Map<String,Object>> selectIncidentListByBatchClose(@Param("eventTitle") String eventTitle,
                                                            @Param("startTime") String startTime,
                                                            @Param("endTime") String endTime,
                                                            @Param("userId") String userId,
                                                            @Param("currentBizId") String currentBizId);

    IPage<Map<String, Object>> selectIncidentListByDealClose(Page page, @Param("params") Map<String, Object> map);

    List<Map<String, String>> selectIncidentListTaskEmail(@Param("params") Map<String,Object> params);

    int updateDesignBizIncidentBatchStatusByMonitor(@Param("eventNo") String eventNo, @Param("eventStatus") String eventStatus);

    void clearCurrentDealByStatus();
}