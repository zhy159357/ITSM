package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.DesignBizChange;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.vo.ChangeRetrievalVo;

import javax.servlet.http.HttpServletResponse;

public interface DesignBizChangeService extends IService<DesignBizChange>{


    AjaxResult changeRetrieval(Page page, ChangeRetrievalVo changeRetrievalVo);

    void changeRetrieval(ChangeRetrievalVo changeRetrievalVo, HttpServletResponse httpServletResponse);
}
