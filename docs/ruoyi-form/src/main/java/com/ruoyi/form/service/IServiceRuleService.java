package com.ruoyi.form.service;

import java.util.List;
import java.util.Map;

public interface IServiceRuleService {
    List<Map<String, Object>> getTicketTypes();
    List<Map<String, Object>> getRuleTypes();
    List<Map<String, Object>> recheckStatus();
    List<Map<String, Object>> ruleStatus();
    List<Map<String, Object>> commitUsers();
    List<Map<String, Object>> allUsers(Integer f);
}
