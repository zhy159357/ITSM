package com.ruoyi.form.strategy.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.CustomerFormListDTO;
import com.ruoyi.form.entity.CompleteParamDto;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.service.CustomerStrategyService;
import com.ruoyi.form.strategy.CustomerStrategy;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.GeneralQueryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerStrategyContext
 * @Description 自定义具体实现类
 * @Author JiaQi Zhang
 * @Date 2022/10/2 16:04
 */

@Service
@RequiredArgsConstructor
public class CustomerStrategyContext extends CustomerStrategy {

    private final Map<String, CustomerStrategyService> customerStrategyServiceMap;


    @Override
    public AjaxResult list(String code, String type, Page page, CustomerFormListDTO customerFormListDTO) {
        Map<String, Long> currentTableInfo = GeneralQueryUtil.getCurrentTableInfo(code, customerFormListDTO.getVersion());
        List<Map<String, String>> formFieldInfo = GeneralQueryUtil.getFormFieldInfo(currentTableInfo.get("id"));
        String formName = GeneralQueryUtil.getFormName(currentTableInfo.get("id"));
        QueryWrapper queryWrapper=new QueryWrapper();
        CustomerStrategyUtil.extractedSelectSql(queryWrapper,formName,formFieldInfo);
        CustomerStrategyUtil.extractedSqlCondition(queryWrapper,customerFormListDTO.getSearchDTOList());
        //TODO  构造查询对象IDS
        return null;
    }


    @Override
    public AjaxResult insertOrUpdate(String fromCode, String id, String code, CustomerFormContent customerFormContent) {
        Long bizId = ObjectUtil.isEmpty(customerFormContent.getFields().get("id")) ? null : Long.valueOf(String.valueOf(customerFormContent.getFields().get("id")));
        for (String key : customerFormContent.getFields().keySet()) {
            if (customerFormContent.getFields().get(key) instanceof Collection) {
                customerFormContent.getFields().put(key, JSON.toJSONString(customerFormContent.getFields().get(key)));
            }
        }
        if (bizId!=null){
            Map<String, Long> currentTableInfo = GeneralQueryUtil.getCurrentTableInfo(code, null);
            List<Map<String, String>> formFieldInfo = GeneralQueryUtil.getFormFieldInfo(currentTableInfo.get("id"));
            GeneralQueryUtil.getFormName(currentTableInfo.get("id"));
        }
        return null;
    }

    /**
     * 获取代办详情页面
     * @param code 业务编码CODE
     * @param processId  流程实例ID
     * @param id      任务主键ID
     * @param type    类型
     * @param version 版本
     * @return
     */
    @Override
    public AjaxResult customerDetailsPage(String code, String processId, Integer id, String type, String version) {
        AjaxResult result = customerStrategyServiceMap.get(WorkOrderInformation.customerStrategy.getCustomerServiceImpl()).customerDetailsPage(code, processId, id, type, version);
        return result;


    }


    @Override
    public AjaxResult customerDetailsPage(String code, Integer id, String processId) {
        //获取表单内容JSON
        List<Map<String, Object>> formJsonData = GeneralQueryUtil.getFormJsonData(code, processId, id);
        //获取表单追加的数据-----【编号及状态】
        List<Map<String, String>> formJsonAppendInfo = GeneralQueryUtil.getFormJsonAppendInfo(code, id);

        // TODO  只获取了基本信息页面 在基本信息页面如果需要定制化个别按钮  请写具体的serviceImpl类实现CustomerStrategyService此接口 并参考customerStrategyServiceImpl类把结果放进去
        AjaxResult result = customerStrategyServiceMap.get(WorkOrderInformation.getByCode(code)).customerDetailsPage(formJsonData,formJsonAppendInfo,code, id, processId);
        return result;


    }

    /**
     * 审批接口
     * @param completeParamDto
     * @return
     */
    @Override
    public AjaxResult processComplete(CompleteParamDto completeParamDto) {
        AjaxResult result = customerStrategyServiceMap.get(GeneralQueryUtil.buildButtonInfo(completeParamDto.getButtonConfigId(), completeParamDto.getCode()).getProcessCompleteServiceImpl()).processComplete(completeParamDto);
        return result;
    }


    @Override
    public AjaxResult summaryConsoleDesk(Page page,String searchType) {
        return customerStrategyServiceMap.get(WorkOrderInformation.customerStrategy.getCustomerServiceImpl()).summaryConsoleDesk(page,searchType);
    }

    @Override
    public AjaxResult incidentConsole(Page page) {
        return customerStrategyServiceMap.get(WorkOrderInformation.getByCode(WorkOrderInformation.incident.getCode())).console(page);
    }

}
