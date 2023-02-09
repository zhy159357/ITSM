package com.ruoyi.activiti.web.esb;

public interface TaskLockManager {
    /**
     * 任务表锁定
     * @param taskName
     * @return
     */
    public boolean lock(String taskName);

    /**
     * 任务表解锁
     * @param taskName
     */
    public void unlock(String taskName);

}
