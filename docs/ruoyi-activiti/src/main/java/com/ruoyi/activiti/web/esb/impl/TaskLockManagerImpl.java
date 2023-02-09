package com.ruoyi.activiti.web.esb.impl;

import com.ruoyi.activiti.web.esb.TaskLockManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务锁定服务
 *
 * @author 14735
 */
@Component
@Transactional
public class TaskLockManagerImpl implements TaskLockManager, InitializingBean {

    @Autowired
    private SqlSessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(TaskLockManagerImpl.class);

    private String tableName = "YOUI_TASK_LOCK";

    private String taskColumnName = "TASK_NAME";

    private String lockColumnName = "LOCK_VALUE";

    private String selectQuery;
    private String updateQuery;
    private String insertQuery;

    private String TYPE_LOCK = "1";
    private String TYPE_UNLOCK = "0";

    private String taskName = "";

    private List<String> allTaskNameList;

    @Override
    public synchronized boolean lock(String taskName) {
        // 加锁时如果YOUI_TASK_LOCK表不包含传入的taskName直接返回false
        if (!allTaskNameList.contains(taskName)) {
            logger.debug("--------定时任务锁类型taskName = " + taskName + "未在表【 " + tableName + " 】中维护，暂不执行加锁操作！-------");
            return false;
        }
        this.taskName = taskName;
        return doWorkInNewTransaction(sessionFactory.openSession(), TYPE_LOCK);
    }

    @Override
    public synchronized void unlock(String taskName) {
        // 解锁时如果YOUI_TASK_LOCK表不包含传入的taskName直接返回不做释放锁处理
        if (!allTaskNameList.contains(taskName)) {
            logger.debug("--------定时任务锁类型taskName = " + taskName + "未在表【 " + tableName + " 】中维护，暂不执行解锁操作！-------");
            return;
        }
        this.taskName = taskName;
        doWorkInNewTransaction(sessionFactory.openSession(), TYPE_UNLOCK);
    }

    protected boolean doWorkInCurrentTransaction(Connection conn, String type) throws SQLException {
        if (TYPE_LOCK.equals(type)) {
            String result;
            int rows;
            logger.debug("-------定时任务锁类型【" + taskName + "】执行加锁开始--------");
            PreparedStatement selectPS = conn.prepareStatement(selectQuery);
            try {
                selectPS.setString(1, taskName);
                ResultSet selectRS = selectPS.executeQuery();
                if (!selectRS.next()) {
                    PreparedStatement insertPS = null;
                    try {
                        insertPS = conn.prepareStatement(insertQuery);
                        insertPS.setString(1, taskName);
                        insertPS.setString(2, TYPE_UNLOCK);
                        insertPS.execute();
                    } finally {
                        if (insertPS != null) {
                            insertPS.close();
                        }
                    }
                } else {
                    result = selectRS.getString(1);
                    if (TYPE_LOCK.equals(result)) {
                        logger.debug("-------定时任务锁类型【" + taskName + "】执行加锁结束，加锁结果为:【false】，该类型锁已由其他服务锁定--------");
                        return false;
                    }
                }
                selectRS.close();
            } catch (SQLException sqle) {
                logger.error("could not read or init a hi value", sqle);
                throw sqle;
            } finally {
                selectPS.close();
            }

            PreparedStatement updatePS = conn.prepareStatement(updateQuery);
            try {
                updatePS.setString(1, TYPE_LOCK);
                updatePS.setString(2, TYPE_UNLOCK);
                updatePS.setString(3, taskName);
                rows = updatePS.executeUpdate();
            } catch (SQLException sqle) {
                logger.error("could not updateQuery hi value in: " + tableName, sqle);
                throw sqle;
            } finally {
                updatePS.close();
            }
            logger.debug("-------定时任务锁类型【" + taskName + "】执行加锁结束，加锁结果为:【" + (rows != 0) + "】--------");
            return rows != 0;
        } else if (TYPE_UNLOCK.equals(type)) {
            PreparedStatement updatePS = conn.prepareStatement(updateQuery);
            try {
                updatePS.setString(1, TYPE_UNLOCK);
                updatePS.setString(2, TYPE_LOCK);
                updatePS.setString(3, taskName);
                updatePS.executeUpdate();
                logger.debug("-------定时任务锁类型【" + taskName + "】执行解锁成功--------");
            } catch (SQLException sqle) {
                logger.error("could not updateQuery hi value in: " + tableName, sqle);
                throw sqle;
            } finally {
                updatePS.close();
            }
        }
        return false;
    }

    /**
     * 项目启动加载YOUI_TASK_LOCK表所有的task_name字段放入集合
     *
     * @return
     */
    public List<String> selectAllTaskName() {
        Connection conn = sessionFactory.openSession().getConnection();
        List<String> list = new ArrayList<>();
        String sql = "select " + taskColumnName + " from " + tableName;
        PreparedStatement selectPS = null;
        ResultSet selectRS = null;
        try {
            selectPS = conn.prepareStatement(sql);
            selectRS = selectPS.executeQuery();
            while (selectRS.next()) {
                list.add(selectRS.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("----------数据库定时任务锁表加载失败------------", ex);
        } finally {
            try {
                if (null != selectRS) {
                    selectRS.close();
                }
                if (null != selectPS) {
                    selectPS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public void afterPropertiesSet() {
        if (sessionFactory != null) {
            this.selectQuery = buildSelectQuery();
            this.updateQuery = buildUpdateQuery();
            this.insertQuery = buildInsertQuery();

            logger.debug("------------加载定时任务锁【YOUI_TASK_LOCK】表所有的task_name字段放入集合开始--------------");
            this.allTaskNameList = selectAllTaskName();
            logger.debug("------------定时任务锁【YOUI_TASK_LOCK】表所有的taskName字段集合为:" + allTaskNameList + "--------------");
            logger.debug("------------加载定时任务锁【YOUI_TASK_LOCK】表所有的task_name字段放入集合结束--------------");
        }
    }

    public boolean doWorkInNewTransaction(SqlSession session, final String type) {
        boolean value = false;
        try {
            value = doWorkInCurrentTransaction(session.getConnection(), type);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return value;
    }

    protected String buildSelectQuery() {
        return "select " + lockColumnName +
                " from " + tableName +
                " where " + taskColumnName + "=?" + " for update";
    }

    protected String buildUpdateQuery() {
        return "update " + tableName +
                " set " + lockColumnName + "=? " +
                " where " + lockColumnName + "=? and " + taskColumnName + "=?";
    }

    protected String buildInsertQuery() {
        return "insert into " + tableName + " (" + taskColumnName + ", " + lockColumnName + ") " + " values (?,?)";
    }

}
