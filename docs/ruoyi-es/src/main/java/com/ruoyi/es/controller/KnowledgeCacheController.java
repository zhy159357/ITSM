package com.ruoyi.es.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.service.KnowledgeCacheService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.SearchService;
import com.ruoyi.es.thread.InsertCacheThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 查询告警数据放到缓存中
 * @author sdy
 * @date 2021年8月2日
 */
@Controller
@Configuration
@RequestMapping("knowledge/cacheThread")
public class KnowledgeCacheController extends BaseController {

    @Autowired
    KnowledgeCacheService knowledgeCacheService;

    @GetMapping("/list")
    @ResponseBody
    public String list(KnowledgeContent knowledgeContent) {
        return knowledgeCacheService.alarmExportList(knowledgeContent);
    }

}