package com.ruoyi.form.util;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.form.domain.ChmBasedata;
import com.ruoyi.form.domain.ChmParavalue;
import com.ruoyi.form.domain.DesignBizChm;
import com.ruoyi.form.service.impl.ChmBasedataServiceImpl;
import com.ruoyi.form.service.impl.ChmParavalueServiceImpl;
import com.ruoyi.system.service.impl.OgPersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
@Component
public class BizChmExportUtil {
   public void multithreadedListSegmentation(List<DesignBizChm> list) {

        // 开始时间
        long start = System.currentTimeMillis();

        int listSize = list.size();
        //跑批分页大小
        int limitNum = 100;
        //线程数
        int threadNum = listSize % limitNum == 0 ? listSize / limitNum : listSize / limitNum + 1;
        //最大线程数控制
        int maxthreadNum = 20;
        // ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadNum);
        ExecutorService executor = new ThreadPoolExecutor(maxthreadNum, maxthreadNum, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(threadNum), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        //最大并发线程数控制
        final Semaphore semaphore = new Semaphore(maxthreadNum);
        List handleList = null;
        for (int i = 0; i < threadNum; i++) {
            if ((i + 1) == threadNum) {
                int startIndex = i * limitNum;
                int endIndex = list.size();
                handleList = list.subList(startIndex, endIndex);
            } else {
                int startIndex = i * limitNum;
                int endIndex = (i + 1) * limitNum;
                handleList = list.subList(startIndex, endIndex);
            }
            SyncTask task = new SyncTask(handleList, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            System.out.println("线程任务执行结束");
            System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        }
    }
}


@Slf4j
class SyncTask implements Runnable {
    private List<DesignBizChm> list;
    private CountDownLatch countDownLatch;
    private Semaphore semaphore;
    private ChmParavalueServiceImpl iChmParavalueService= SpringUtils.getBean(ChmParavalueServiceImpl.class);

    private ChmBasedataServiceImpl iChmBasedataService= SpringUtils.getBean(ChmBasedataServiceImpl.class);
    private OgPersonServiceImpl ogPersonService= SpringUtils.getBean(OgPersonServiceImpl.class);
    public SyncTask(List<DesignBizChm> list, CountDownLatch countDownLatch, Semaphore semaphore) {
        this.list = list;
        this.countDownLatch = countDownLatch;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        if (!CollectionUtils.isEmpty(list)) {
            try {
                semaphore.acquire();
                list.stream().forEach(cell -> {
                    //业务处理
                    //上报部门
                    String reportDepartment=cell.getReportDepartment();
                    ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(reportDepartment));
                    cell.setReportDepartment(chmBasedata.getOrgName());
                    //123 级
                    ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(cell.getEquipmentType()));
                    cell.setEquipmentType(chmParavalue.getName());

                    ChmParavalue chmParavalue1=iChmParavalueService.selectChmParavalueById(Long.valueOf(cell.getEquipmentName()));
                    cell.setEquipmentName(chmParavalue1.getName());

                    ChmParavalue chmParavalue2=iChmParavalueService.selectChmParavalueById(Long.valueOf(cell.getEquipmentModel()));
                    cell.setEquipmentModel(chmParavalue2.getName());
                    String createBy=cell.getCreatedBy();
                    OgPerson ogPerson=ogPersonService.selectOgPersonById(createBy);
                    if(ogPerson!=null){
                        cell.setCreatedBy(ogPerson.getpName());
                    }else {
                        cell.setCreatedBy("未知");
                    }
                });
                System.out.println(Thread.currentThread().getName() + "线程结束" );
                //  log.debug(String.format("%s", Thread.currentThread().getName() + "线程：" + list));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
        //线程任务完成
        countDownLatch.countDown();
    }
}