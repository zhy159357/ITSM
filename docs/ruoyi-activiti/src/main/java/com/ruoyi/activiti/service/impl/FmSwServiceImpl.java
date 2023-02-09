package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.FmSw;
import com.ruoyi.activiti.mapper.FmSwMapper;
import com.ruoyi.activiti.service.IFmSwService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 事务事件单Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-12-16
 */
@Service
public class FmSwServiceImpl implements IFmSwService
{
    @Autowired
    private FmSwMapper fmSwMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;
    /**
     * 查询事务事件单
     * 
     * @param fmSwId 事务事件单ID
     * @return 事务事件单ss
     */
    @Override
    public FmSw selectFmSwById(String fmSwId)
    {
        if("mysql".equals(dbType)){
            return fmSwMapper.selectFmSwByIdMysql(fmSwId);
        }else{
            return fmSwMapper.selectFmSwById(fmSwId);
        }
    }

    /**
     * 查询事务事件单列表
     * 
     * @param fmSw 事务事件单
     * @return 事务事件单
     */
    @Override
    public List<FmSw> selectFmSwList(FmSw fmSw)
    {

        if("mysql".equals(dbType)){
            return fmSwMapper.selectFmSwListMysql(fmSw);
        }else{
            return fmSwMapper.selectFmSwList(fmSw);
        }
    }

    @Override
    public List<FmSw> selectFmSwListByTask(FmSw fmSw) {
        return fmSwMapper.selectFmSwListByTask(fmSw);
    }

    /**
     * 新增事务事件单
     * 
     * @param fmSw 事务事件单
     * @return 结果
     */
    @Override
    public int insertFmSw(FmSw fmSw)
    {
        return fmSwMapper.insertFmSw(fmSw);
    }

    /**
     * 修改事务事件单
     * 
     * @param fmSw 事务事件单
     * @return 结果
     */
    @Override
    public int updateFmSw(FmSw fmSw)
    {
        return fmSwMapper.updateFmSw(fmSw);
    }

    @Override
    public int checkUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.checkUpdateFmSw(fmSw);
    }

    /**
     * 删除事务事件单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmSwByIds(String ids)
    {
        return fmSwMapper.deleteFmSwByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<FmSw> selectList(FmSw fmSw) {
        if("mysql".equals(dbType)){
            return fmSwMapper.selectListMysql(fmSw);
        }else{
            return fmSwMapper.selectList(fmSw);
        }
    }

    @Override
    public int checkReturnUpdateFmSw(FmSw fmsw) {
        return fmSwMapper.checkReturnUpdateFmSw(fmsw);
    }

    @Override
    public int returnEditUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.returnEditUpdateFmSw(fmSw);
    }

    @Override
    public int invalidateUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.invalidateUpdateFmSw(fmSw);
    }

    @Override
    public int dealUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.dealUpdateFmSw(fmSw);
    }

    @Override
    public int dealPassUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.dealPassUpdateFmSw(fmSw);
    }

    @Override
    public int auditUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.auditUpdateFmSw(fmSw);
    }

    @Override
    public List<FmSw> fmListByStatus(FmSw fmSw) {
        return fmSwMapper.fmListByStatus(fmSw);
    }

    @Override
    public int checkNoAuthUpdateFmSw(FmSw fmSw) {
        return fmSwMapper.checkNoAuthUpdateFmSw(fmSw);
    }

    @Override
    public int updateFmSwByInvalidationMark(String fmSwId) {
        return fmSwMapper.updateFmSwByInvalidationMark(fmSwId);
    }

    /**
     * 查询当前的事务事件单列表
     *
     * @param fmSw 事务事件单
     * @return 事务事件单
     */
    @Override
    public List<FmSw> selectFmSwListTwo(FmSw fmSw)
    {
        return fmSwMapper.selectFmSwListTwo(fmSw);
    }

    /**
     * 删除事务事件单信息
     * 
     * @param ${pkColumn.javaField} 事务事件单ID
     * @return 结果
     */
  /*  @Override
    public int deleteFmSwById(${pkColumn.javaType} ${pkColumn.javaField})
    {
        return fmSwMapper.deleteFmSwById(${pkColumn.javaField});
    }*/


    /**
     * 通过id查询绑定流程
     *
     * @param fmSwId fmSwId
     * @return 结果
     */
    @Override
    public FmSw getProcessStatus(String fmSwId) {
        return fmSwMapper.getProcessStatus(fmSwId);
    }

}
