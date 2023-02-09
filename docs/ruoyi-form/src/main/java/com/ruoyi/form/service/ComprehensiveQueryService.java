package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.vo.ComprehensiveQuery;
import com.ruoyi.form.vo.ExportExcelParams;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName ComprehensiveQueryService
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/9/20 13:44
 */
public interface ComprehensiveQueryService {
    AjaxResult comprehensiveQuery(Page page,ComprehensiveQuery comprehensiveQuery);


    AjaxResult getBizCode();

    void exportExcel(ExportExcelParams params, HttpServletResponse response);
}
