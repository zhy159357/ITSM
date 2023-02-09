package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.DesignBizProblemTaskNew;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.vo.ProblemTaskRetrievalVo;

import javax.servlet.http.HttpServletResponse;

public interface DesignBizProblemTaskNewService extends IService<DesignBizProblemTaskNew>{


    AjaxResult problemTaskRetrieval(Page page, ProblemTaskRetrievalVo problemTaskRetrievalVo);

    void problemTaskRetrieval(ProblemTaskRetrievalVo problemTaskRetrievalVo, HttpServletResponse response);
}
