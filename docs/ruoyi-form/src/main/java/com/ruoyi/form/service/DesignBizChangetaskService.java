package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.DesignBizChangetask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.vo.ChangeTaskRetrievalVo;

import javax.servlet.http.HttpServletResponse;

public interface DesignBizChangetaskService extends IService<DesignBizChangetask>{


    AjaxResult changeTaskRetrieval(Page page, ChangeTaskRetrievalVo changeRetrievalVo);

    void changeTaskRetrieval(ChangeTaskRetrievalVo changeRetrievalVo, HttpServletResponse response);
}
