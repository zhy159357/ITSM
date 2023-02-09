package com.ruoyi.es.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.EventType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.es.domain.KnowledgeOperationHistory;
import com.ruoyi.es.service.KnowledgeOperationHistoryService;

/**
 * 操作记录Controller
 */
@Controller
@RequestMapping("knowledge/operationhistory")
public class OperationHistoryController extends BaseController {
    private String prefix = "knowledge/operationhistory";

    @Autowired(required=false)
    private KnowledgeOperationHistoryService operationHistoryService;

    @GetMapping()
    public String operationhistory() {
        return prefix + "/operationhistory";
    }

    /**
     * 查询列表
     * 业务事件单
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeOperationHistory knowledgeOperationHistory) {
        startPage();
        knowledgeOperationHistory.setEventType(EventType.BUSINESS.getCode());
        List<KnowledgeOperationHistory> list = operationHistoryService.selectKnowledgeOperationHistoryList(knowledgeOperationHistory);
        return getDataTable(list);
    }
    
    
    @GetMapping("/monitoring")
    public String history() {
        return prefix + "/monitoring";
    }

    /**
     * 查询列表
     * 监控事件单
     */
    @PostMapping("/listMonitoring")
    @ResponseBody
    public TableDataInfo listMonitoring(KnowledgeOperationHistory knowledgeOperationHistory) {
        startPage();
        knowledgeOperationHistory.setEventType(EventType.MONITORING.getCode());
        List<KnowledgeOperationHistory> list = operationHistoryService.selectKnowledgeOperationHistoryList(knowledgeOperationHistory);
        return getDataTable(list);
    }
    
    @GetMapping("/single/{id}")
    public String single(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.addAttribute("id", id);
        return prefix + "/single";
    }
    /**
     * 查询列表
     * 监控事件单
     */
    @PostMapping("/listSingle/{id}")
    @ResponseBody
    public TableDataInfo listSingle(@PathVariable("id") String id) {
        startPage();
        KnowledgeOperationHistory knowledgeOperationHistory = new KnowledgeOperationHistory();
        knowledgeOperationHistory.setContentId(id);
        List<KnowledgeOperationHistory> list = operationHistoryService.selectKnowledgeOperationHistoryList(knowledgeOperationHistory);
        return getDataTable(list);
    }
}
