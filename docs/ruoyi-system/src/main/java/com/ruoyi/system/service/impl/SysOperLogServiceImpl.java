package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;
    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        String title= StringUtils.substring(operLog.getTitle(),0,16);
        String method= StringUtils.substring(operLog.getMethod(),0,100);
        String deptName=StringUtils.substring(operLog.getDeptName(),0,16);
        String operParam=StringUtils.substring(operLog.getOperParam(),0,2000);
        String errorMsg=StringUtils.substring(operLog.getErrorMsg(),0,2000);
        String JsonResult=StringUtils.substring(operLog.getJsonResult(),0,4000);
        operLog.setTitle(title);
        operLog.setMethod(method);
        operLog.setDeptName(deptName);
        operLog.setOperParam(operParam);
        operLog.setErrorMsg(errorMsg);
        operLog.setJsonResult(JsonResult);
        if("mysql".equals(dbType)){
            operLogMapper.insertOperlogMysql(operLog);
        }else{
            operLogMapper.insertOperlog(operLog);
        }
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int deleteOperLogByIds(String ids)
    {
        return operLogMapper.deleteOperLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
