package com.ruoyi.quartz.task;

import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.system.service.impl.ShDutyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author 14735
 */
@Component("shDutyTask")
public class DutyShTask {

	@Autowired
	private TaskLockManager taskLockManager;

	@Autowired
	private ShDutyServiceImpl shDutyServiceImpl;

	private static final Logger log = LoggerFactory.getLogger(DutyShTask.class);

	private final String taskName = "dutyShTask";

	public void dutyShTask() {
		if (taskLockManager.lock(taskName)) {
			long start = System.currentTimeMillis();
			try {
				for (int i = 0; i < 2; i++) {
					try {
						log.debug("------------syncShDuty定时任务执行开始-----------------");
						shDutyServiceImpl.sync();
						log.debug("------------syncShDuty定时任务执行结束-----------------");
						break;
					} catch (Throwable e) {
						if (i == 0) {
							log.error("dutyShTask.Throwable." + i);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						} else {
							e.printStackTrace();
							log.error("dutyShTask", e);
						}
					}
				}
			} finally {
				taskLockManager.unlock(taskName);
			}
			long end = System.currentTimeMillis();
			log.debug("--------定时任务【DutyShTask】执行总时长【" + (end - start) + "】毫秒");
		} else {
			log.debug(taskName + " - 任务已有其他服务执行...");
		}
	}
}
