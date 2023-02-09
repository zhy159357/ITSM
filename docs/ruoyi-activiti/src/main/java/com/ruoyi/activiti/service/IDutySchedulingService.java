package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;

import java.text.ParseException;
import java.util.List;

/**
 * 排班信息Service接口
 * @author dayong_sun
 * @date 2020-01-19
 */
public interface IDutySchedulingService
{
    /**
     * 修改值班日志
     * @return 排班信息
     */
    public int editCheckLog(String id, String content, String updateTime);
    /**
     * 删除值班日志
     * @return 排班信息
     */
    public AjaxResult deleteDutyLogByIds(String ids);

    /**
     * 查询值班日志
     * @return 排班信息
     */
    public List<DutyLog> searchLogForUserId(DutyLog dutyLog);

    /**
     * 增加值班日志
     * @return 排班信息
     */
    public int addLog(DutyLog dutyLog);

    /**
     * 查询排班信息
     * @return 排班信息
     */
    public int selectDutyPersonList(String mobilePhone, String endTime) throws ParseException;

    /**
     * 查询排班信息
     * @param dutyScheduling 排班信息ID
     * @return 排班信息
     */
    public List<DutyScheduling> selectDutySchedulingList(DutyScheduling dutyScheduling);

    /**
     * 查询排班信息列表
     * @param schedulingId 排班信息
     * @return 排班信息集合
     */
    public DutyScheduling selectDutySchedulingById(String schedulingId);

    /**
     * 新增排班信息
     * @param dutyScheduling 排班信息
     * @return 结果
     */
    public String addCheckSave(DutyScheduling dutyScheduling);

    /**
     * 新增排班信息
     * @param dutyScheduling 排班信息
     * @return 结果
     */
    public int insertDutyScheduling(DutyScheduling dutyScheduling);

    /**
     * 修改排班信息
     * @param schedulingId 排班信息
     * @return 结果
     */
    public int editSchedulingCheck(String schedulingId);

    /**
     * 修改排班信息
     * @param dutyScheduling 排班信息
     * @return 结果
     */
    public int updateDutyScheduling(DutyScheduling dutyScheduling);

    /**
     * 批量删除排班信息
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public AjaxResult deleteDutySchedulingByIds(String ids);

    /**
     * 根据pid查询联系方式
     * @param pids 需要数据ID
     * @return 结果
     */
    public String selectPhoneByPid(String pids);

    /**
     * 删除排班信息信息
     * @param schedulingId 排班信息ID
     * @return 结果
     */
    public int deleteDutySchedulingById(String schedulingId);

    /**
     * 校验类别编码是否唯一
     * @param dutyTypeinfo 角色信息
     * @return 结果
     */
    public String checkTypeNoUnique(DutyTypeinfo dutyTypeinfo);

    /**
     * 查询值班人员
     * @param ogPerson 排班信息ID
     * @return 排班信息
     */
    public List<OgPerson> selectDutyUserList(OgPerson ogPerson);

    /**
     * 查询值班人员
     * @param orgId 机构ID
     * @return 排班信息
     */
    public List<DutyPerson> selectOgPersonList(String orgId);

    /**
     * 导出岗位为数据中心人员，数据中心处长，厂商人员('0002','0010','0011')人员
     * @return 人员信息
     */
    public List<DutyPerson> exportOgPersonList();

    /**
     * 校验月份是否唯一
     * @return 版本信息
     */
    public String checkDutyDateUnique(DutyScheduling dutyScheduling);

    /**
     * 导入排班数据
     * @param userList 用户数据列表
     * @param operName 操作用户
     * @return 结果
     */
    public String importScheduling(List<DutyScheduling> userList, String operName);

    /**
     * 查询值班人
     * @param dutyScheduling
     * @return
     */
    public List<DutyScheduling> selectDutySchedulingByTypeNo(DutyScheduling dutyScheduling);

}
