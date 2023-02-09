package com.ruoyi.activiti.service;

import java.util.List;

import com.ruoyi.activiti.domain.FmSw;

/**
 * 事务事件单Service接口
 * 
 * @author ruoyi
 * @date 2020-12-16
 */
public interface IFmSwService 
{
    /**
     * 查询事务事件单
     * 
     * @param ${pkColumn.javaField} 事务事件单ID
     * @return 事务事件单
     */
    public FmSw selectFmSwById(String fmSwId);

    /**
     * 查询事务事件单列表
     * 
     * @param fmSw 事务事件单
     * @return 事务事件单集合
     */
    public List<FmSw> selectFmSwList(FmSw fmSw);

    public List<FmSw> selectFmSwListByTask(FmSw fmSw);

    /**
     * 新增事务事件单
     * 
     * @param fmSw 事务事件单
     * @return 结果
     */
    public int insertFmSw(FmSw fmSw);

    /**
     * 修改事务事件单
     * 
     * @param fmSw 事务事件单
     * @return 结果
     */
    public int updateFmSw(FmSw fmSw);

    /**
     * 审核修改事务事件单
     *
     * @param fmSw 事务事件单
     * @return 结果
     */
    public int checkUpdateFmSw(FmSw fmSw);

    /**
     * 批量删除事务事件单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmSwByIds(String ids);

    List<FmSw> selectList(FmSw fmSw);

    /**
     * 审核退回更新
     * @param fmsw
     * @return
     */
    int checkReturnUpdateFmSw(FmSw fmsw);

    int returnEditUpdateFmSw(FmSw fmSw);

    int invalidateUpdateFmSw(FmSw fmSw);

    int dealUpdateFmSw(FmSw fmSw);

    int dealPassUpdateFmSw(FmSw fmSw);

    int auditUpdateFmSw(FmSw fmSw);

    List<FmSw> fmListByStatus(FmSw fmsw);

    int checkNoAuthUpdateFmSw(FmSw fmSw);


    /**
     * 结束流程逻辑删除
     * @param fmSwId
     * @return
     */
    int updateFmSwByInvalidationMark(String fmSwId);

    List<FmSw> selectFmSwListTwo(FmSw fmSw);

    /**
     * 删除事务事件单信息
     * 
     * @param ${pkColumn.javaField} 事务事件单ID
     * @return 结果
     */
   // public int deleteFmSwById(${pkColumn.javaType} ${pkColumn.javaField});

    /**
     * 通过id查询绑定流程
     *
     * @param fmSwId fmSwId
     * @return 结果
     */
    FmSw getProcessStatus(String fmSwId);

}
