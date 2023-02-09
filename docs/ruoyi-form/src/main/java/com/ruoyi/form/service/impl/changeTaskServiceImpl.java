package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.entity.CompleteParamDto;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import com.ruoyi.form.enums.ChangeTaskStatusEnum;
import com.ruoyi.form.service.CustomerStrategyService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class changeTaskServiceImpl implements CustomerStrategyService {

    @Autowired
    Base base;
    @Autowired
    IOgPersonService ogPersonService;

    @Override
    public AjaxResult customerDetailsPage(String code, String processId, Integer id, String type, String version) {
        return null;
    }

    @Override
    public AjaxResult customerDetailsPage(List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String code, Integer id, String processId) {
        String taskDept = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.TASKDEPT, ChangeTaskFieldEnum.ID, String.valueOf(id));
        String status = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.ID, String.valueOf(id));
        Map<String, Object> resultMap = new HashMap<>();
        /*当前登录人与任务单的审批人在一个部门，且该单子处于待实施状态，则有抢单按钮*/
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ogUser.getpId());
        if (ogPerson.getOrgId().equals(taskDept) && ChangeTaskStatusEnum.waitImpl.getName().equals(status)) {
            Map<String, Object> button = new HashMap<>();
            button.put("buttonName", "抢单");
            button.put("buttonUrlPath", "/customerForm/change/rob/task");
            List<Map<String, Object>> buttonInfoList = new ArrayList<>();
            buttonInfoList.add(button);
            resultMap.put("actionButtonList", buttonInfoList);
        }
        resultMap.put("jsonData", formJsonData);
        resultMap.put("appenJsondata", formJsonAppendInfo);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult processComplete(CompleteParamDto completeParamDto) {
        return null;
    }


    @Override
    public AjaxResult summaryConsoleDesk(Page page, String searchType) {
        return null;
    }

    @Override
    public AjaxResult console(Page page) {
        return null;
    }
}
