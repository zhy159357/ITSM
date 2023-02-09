package com.ruoyi.form.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.CustomerFormListDTO;
import com.ruoyi.form.entity.CompleteParamDto;
import org.springframework.stereotype.Service;

/**
 * @ClassName CustomerStrategy
 * @Description 自定义类型抽象类
 * @Author JiaQi Zhang
 * @Date 2022/10/2 15:59
 */
@Service
public abstract class CustomerStrategy {

    public abstract AjaxResult customerDetailsPage(String code,String processId,Integer id,String type,String version);

    public abstract AjaxResult customerDetailsPage(String code, Integer id, String processId);

    public abstract AjaxResult processComplete(CompleteParamDto completeParamDto);

    public abstract AjaxResult list(String code, String type, Page page, CustomerFormListDTO customerFormListDTO);

    public abstract AjaxResult insertOrUpdate(String fromCode, String id, String code, CustomerFormContent customerFormContent);

    public abstract AjaxResult summaryConsoleDesk(Page page,String searchType);

    public abstract AjaxResult incidentConsole(Page page);

}
