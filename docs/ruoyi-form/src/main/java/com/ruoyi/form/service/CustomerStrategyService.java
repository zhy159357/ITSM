package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.entity.CompleteParamDto;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerStrategyService
 * @Description 自定义抽象接口
 * @Author JiaQi Zhang
 * @Date 2022/10/2 16:12
 */
public interface CustomerStrategyService  {
    AjaxResult customerDetailsPage(String code, String processId, Integer id, String type, String version);

    AjaxResult customerDetailsPage(List<Map<String, Object>> formJsonData,List<Map<String, String>> formJsonAppendInfo,String code, Integer id, String processId);


    AjaxResult processComplete(CompleteParamDto completeParamDto);

    AjaxResult summaryConsoleDesk(Page page, String searchType);

    AjaxResult console(Page page);
}
