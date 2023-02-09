package com.ruoyi.activiti.mapper;

import java.util.List;

import com.ruoyi.activiti.domain.FmSw;

/**
 * 事务事件单Mapper接口
 * 
 * @author ruoyi
 * @date 2020-12-16
 */
public interface FmSwMapper 
{
    /**
     * 查询事务事件单
     * 
     * @param FM_SW_ID 事务事件单ID
     * @return 事务事件单
     */
    public FmSw selectFmSwById(String FM_SW_ID);

    public FmSw selectFmSwByIdMysql(String FM_SW_ID);

    /**
     * 查询事务事件单列表
     * 
     * @param fmSw 事务事件单
     * @return 事务事件单集合
     */
    public List<FmSw> selectFmSwList(FmSw fmSw);

    public List<FmSw> selectFmSwListMysql(FmSw fmSw);

    /**
     * 查询事务事件单列表
     *
     * @param fmSw 事务事件单
     * @return 事务事件单集合根据待办任务进行筛选
     */
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
     * @param fmSw
     * @return
     */
    public int checkUpdateFmSw(FmSw fmSw);

    /**
     * 审核没有选择授权人更新
     * @param fmSw
     * @return
     */
    public int checkNoAuthUpdateFmSw(FmSw fmSw);
    /**
     * 删除事务事件单
     * 
     * @param FM_SW_ID 事务事件单ID
     * @return 结果
     */
    public int deleteFmSwById(String FM_SW_ID);

    /**
     * 批量删除事务事件单
     * 
     * @param  ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmSwByIds(String[] ids);

    List<FmSw> selectList(FmSw fmSw);

    List<FmSw> selectListMysql(FmSw fmSw);

    int checkReturnUpdateFmSw(FmSw fmsw);

    int returnEditUpdateFmSw(FmSw fmSw);

    int invalidateUpdateFmSw(FmSw fmSw);

    int dealUpdateFmSw(FmSw fmSw);

    int dealPassUpdateFmSw(FmSw fmSw);

    int auditUpdateFmSw(FmSw fmSw);

    List<FmSw> fmListByStatus(FmSw fmSw);

    /**
     * 结束流程逻辑删除
     * @param fmSwId
     * @return
     */
    int updateFmSwByInvalidationMark(String fmSwId);

    List<FmSw> selectFmSwListTwo(FmSw fmSw);

    /**
     * 通过id查询绑定流程
     *
     * @param fmSwId fmSwId
     * @return 结果
     */
    FmSw getProcessStatus(String fmSwId);

}
