package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.DesignBizIncident;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.vo.IncidentRetrievalVo;

import javax.servlet.http.HttpServletResponse;

public interface DesignBizIncidentService extends IService<DesignBizIncident>{


    AjaxResult incidentRetrieval(Page page, IncidentRetrievalVo retrievalVo);

    void incidentRetrieval(IncidentRetrievalVo retrievalVo, HttpServletResponse response);
}
