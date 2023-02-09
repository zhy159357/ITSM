package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.DesignBizProblem;
import com.ruoyi.form.vo.ProblemRetrievalVo;

import javax.servlet.http.HttpServletResponse;

public interface DesignBizProblemService extends IService<DesignBizProblem>{


    AjaxResult problemRetrieval(Page page, ProblemRetrievalVo problemRetrievalVo);

    void problemRetrieval(ProblemRetrievalVo problemRetrievalVo, HttpServletResponse httpServletResponse);
}
