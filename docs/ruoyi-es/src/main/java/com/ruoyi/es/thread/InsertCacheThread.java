package com.ruoyi.es.thread;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.es.domain.KnowledgeAlarmExample;
import com.ruoyi.es.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 查询告警数据放到缓存中
 * @author sdy
 * @date 2021年8月2日
 */
public class InsertCacheThread extends Thread {

   // private static final Logger logger = LoggerFactory.getLogger(InsertCacheThread.class);

    private Integer pageNum = 0;
    private Integer pageSize = 1000;
    private Map map;
    private int i;
    private CountDownLatch latch;

    public void setKnowledgeCount(int pageNum, int pageSize, Map map, int i) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.map = map;
        this.i = i;
    }

    public InsertCacheThread(CountDownLatch latch){
        this.latch = latch;
    }

    /**
     * @see Thread#run()
     */
    @SuppressWarnings("resource")
    @Override
    public void run() {
      //  logger.debug("---------------------------InsertCacheThread线程执行开始---------------------------"+Thread.currentThread().getId());
        try {
            SearchService contentService = SpringUtils.getBean(SearchService.class);
            KnowledgeAlarmExample knowledgeAlarmExample = new KnowledgeAlarmExample();
            PageHelper.startPage(pageNum, pageSize, null);
           // long s1=System.currentTimeMillis();
            List<KnowledgeAlarmExample> list = contentService.alarmExportList(knowledgeAlarmExample);
           // long s2=System.currentTimeMillis();
           // logger.debug("============================s2-s1====================================="+(s2-s1));
            map.put(i,list);
           // logger.debug("---------------------------InsertCacheThread线程完成---------------------------"+Thread.currentThread().getId());
        } catch (Exception e) {
           // logger.error("---------------------------InsertCacheThread线程失败---------------------------", e);
        	e.printStackTrace();
        } finally {
            // 线程执行完成，线程等待变量减少
            latch.countDown();
        }

    }
}