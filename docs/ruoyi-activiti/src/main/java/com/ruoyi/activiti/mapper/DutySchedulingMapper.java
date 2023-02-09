package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参数类表Mapper接口
 *
 * @author ruoyi
 * @date 2020-12-06
 */
public interface DutySchedulingMapper
{
    /**
     * 修改值班日志
     */
    public int editCheckLog(@Param("id") String id, @Param("content") String content, @Param("updateTime") String updateTime);

    /**
     * 删除值班日志
     */
    public int deleteDutyLogByIds(String[] ids);

    /**
     * 查询值班日志
     */
    public List<DutyLog> searchLogForUserId(DutyLog dutyLog);

    /**
     * 查询参数列表列表
     * @return 参数类表集合
     */
    public int addLog(DutyLog dutyLog);

    /**
     * 查询参数列表列表
     * @return 参数类表集合
     */
    public int selectDutyPersonList(@Param("mobilePhone") String mobilePhone, @Param("dutyDate") String dutyDate);

    /**
     * 查询参数列表列表
     * @param dutyScheduling 参数类表
     * @return 参数类表集合
     */
    public List<DutyScheduling> selectDutySchedulingList(DutyScheduling dutyScheduling);

    /**
     * 查询参数列表
     * @param schedulingId 参数类表ID
     * @return 参数类表
     */
    public DutyScheduling selectDutySchedulingById(String schedulingId);


    /**
     * 新增参数列表
     * @param dutyScheduling 参数列表
     * @return 结果
     */
    public String addCheckSave(DutyScheduling dutyScheduling);

    /**
     * 新增参数列表
     * @param dutyScheduling 参数列表
     * @return 结果
     */
    public int insertDutyScheduling(DutyScheduling dutyScheduling);

    /**
     * 修改参数列表
     * @param dutyScheduling 参数列表
     * @return 结果
     */
    public int updateDutyScheduling(DutyScheduling dutyScheduling);

    /**
     * 删除参数列表
     * @param dutyScheduling 三个参数
     * @return 结果
     */
    public int deleteDutyScheduling(DutyScheduling dutyScheduling);

    /**
     * 删除参数列表
     * @param schedulingId 参数列表ID
     * @return 结果
     */
    public int deleteDutySchedulingById(String schedulingId);

    /**
     * 删除参数列表
     * @param schedulingId 参数列表ID
     * @return 结果
     */
    public int deleteDutyAccountById(String schedulingId);

    /**
     * 删除参数列表
     * @param schedulingId 参数列表ID
     * @return 结果
     */
    public List<DutyScheduling> selectDutySchedulingByIds(String[] schedulingId);

    /**
     * 批量删除参数列表
     * @param schedulingIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDutySchedulingByIds(String[] schedulingIds);

    /**
     * 根据pid查询联系方式
     * @param pids 需要数据ID
     * @return 结果
     */
    public DutyScheduling selectPhoneByPid(String[] pids);
    public DutyScheduling selectPhoneByPidMysql(String[] pids);

    /**
     * 校验类别编码是否唯一
     * @param typeNo 角色信息
     * @return 结果
     */
    public DutyTypeinfo checkTypeNoUnique(String typeNo);

    /**
     * 查询值班人员
     * @param ogPerson 参数列别ID
     * @return 人员
     */
    public  List<OgPerson> selectDutyUserList(OgPerson ogPerson);

    /**
     * 查询值班人员
     * @param orgId 机构ID
     * @return 人员
     */
    public  List<DutyPerson> selectOgPersonList(String orgId);

    /**
     * 导出岗位为数据中心人员，数据中心处长，厂商人员('0002','0010','0011')人员
     * @return 人员信息
     */
    public List<DutyPerson> exportOgPersonList();

    /**
     * 查询值班人员
     * @param scheduling 联系电话
     * @return 人员
     */
    public DutyPerson selectPersonByMobilephone(DutyScheduling scheduling);

    /**
     * 查询类别
     * @param dutyVersion 版本信息
     * @return 类别
     */
    public DutyScheduling selectTypeinfoByTypeNo(DutyVersion dutyVersion);

    /**
     * 查询值班人数
     * @param dutyVersion 版本信息
     * @return 类别
     */
    public DutyScheduling selectDutyNumber(DutyVersion dutyVersion);
    public DutyScheduling selectDutyNumberMysql(DutyVersion dutyVersion);

    /**
     * 查询版本
     * @param dutyDate 值班时间
     * @return 版本
     */
    public DutyVersion selectVersionByDate(String dutyDate);

    /**
     * 查询排版记录
     * @param scheduling 值班时间
     * @return 排版信息
     */
    public DutyScheduling selectScheduling(DutyScheduling scheduling);
    public DutyScheduling selectSchedulingMysql(DutyScheduling scheduling);

    /**
     * 插入排班信息
     * @param list
     */
    public int insertSchedulings(List<DutyScheduling> list);

    /**
     * 查询人员账号信息
     * @param pids 人员ID
     * @return 结果
     */
    public List<DutyScheduling> selectDutyAccountList(String[] pids);

    /**
     * 查询人员账号信息
     * @param schedulingId 排版信息ID
     * @return 结果
     */
    public List<DutyScheduling> selectDutyAccountById(String schedulingId);

    /**
     * 插入人员账号信息
     * @param dutyScheduling
     */
    public int insertDutyAccount(DutyScheduling dutyScheduling);

    /**
     * 更新人员账号信息
     * @param dutyScheduling
     */
    public int updateDutyAccount(DutyScheduling dutyScheduling);

    /**
     * 校验月份是否唯一
     * @return 版本信息
     */
    public DutyScheduling checkDutyDateUnique(String dutyDate);

    /**
     * 校验月份
     * @return 版本信息
     */
    public List<DutyScheduling> checkDutyDate(String dutyDate);

    /**
     * 查询参数列表
     * @param dutyVersion 参数类别
     * @return 结果
     */
    public List<DutyVersion> selectTypeinfoTree(DutyVersion dutyVersion);

    /**
     * 查询值班人
     * @param dutyScheduling
     * @return
     */
    public List<DutyScheduling> selectDutySchedulingByTypeNo(DutyScheduling dutyScheduling);

    /**
     * 统一监控调用接口
     * @param dutyScheduling
     * @return
     */
    public List<DutyScheduling> selectDutyInfoList(DutyScheduling dutyScheduling);

    /**
     * 统一监控调用接口
     * @param typeinfoId
     * @return
     */
    public DutyScheduling selectTypeNameByTypeNo(String typeinfoId);

    List<DutyVersion> selectDutyCalendar(DutyVersion dutyVersion);

    OgUser selectOgUserByCustNo(String custNo);
}
