package com.ruoyi.es.serviceImpl;

import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.es.controller.KnowledgeCacheController;
import com.ruoyi.es.domain.*;
import com.ruoyi.es.service.*;
import com.ruoyi.es.thread.InsertCacheThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类别 Service业务层处理
 * @date 2021-08-18
 */
//@Service
public class KnowledgeCacheServiceImpl implements KnowledgeCacheService {

	//private static final Logger logger = LoggerFactory.getLogger(KnowledgeCacheController.class);

	@Value("${knowledge.export.num}")
	private int exportNum;

	@Value("${knowledge.export.nucleus}")
	private int nucleus;

	ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(nucleus==0?30:nucleus, 50, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
			new ThreadPoolExecutor.CallerRunsPolicy());

     /**
     * 查询列表
     */
    @Override
    public String alarmExportList(KnowledgeContent knowledgeContent) {
		try {
			//long s1=System.currentTimeMillis();
			//logger.debug("---------------------------KnowledgeCacheThread开始---------------------------");
			int sum = SpringUtils.getBean(SearchService.class).selectAlarmCount();
			//logger.debug("---------------------------sum,exportNum---------------------------"+sum+","+exportNum);
			if(exportNum==0){
				exportNum = 1000;
			}
			//long s2=System.currentTimeMillis();
			//logger.debug("---------------------------s2-s1---------------------------"+(s2-s1));
			int kcount = sum/exportNum + 1;
			Map<Integer, List> map = new HashMap();
			CountDownLatch threadSignal = new CountDownLatch(kcount);
			//logger.debug("---------------------------kcount---------------------------"+kcount);
			for(int i=0;i<kcount;i++){
				InsertCacheThread thread = new InsertCacheThread(threadSignal);
				if(i==kcount-1){
					exportNum = sum - i * exportNum;
				}
				thread.setKnowledgeCount(i,exportNum,map,i);
				poolExecutor.execute(thread);
				//logger.debug("---------------------------thread.getName()---------------------------"+thread.getName());
			}
			long se=System.currentTimeMillis();
			//logger.debug("---------------------------se-s1---------------------------"+(se-s1));
			threadSignal.await();
			List resultlist = new ArrayList();
			if(!map.isEmpty()) {
				for (int i = 0; i < kcount; i++) {
					resultlist.addAll(map.get(i));
				}
				CacheUtils.put("alarmAnalizeList",resultlist);
			}
			//logger.debug("---------------------------resultlist大小---------------------------"+resultlist.size());
			//logger.debug("---------------------------KnowledgeCacheThread线程完成---------------------------");
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("---------------------------KnowledgeCacheThread线程失败---------------------------", e);
		}

		return "1";
	}

}
