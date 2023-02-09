package com.ruoyi.activiti.mapper;

import com.ruoyi.system.domain.OutSystemSmsCode;
import com.ruoyi.system.domain.OutSystemSmsLog;

import java.util.List;

/**
 * 对外系统短信验证码Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-25
 */
public interface OutSystemSmsCodeMapper 
{
    /**
     * 查询对外系统短信验证码
     * 
     * @param outId 对外系统短信验证码ID
     * @return 对外系统短信验证码
     */
    public OutSystemSmsCode selectOutSystemSmsCodeById(String outId);

    /**
     * 查询对外系统短信验证码列表
     * 
     * @param outSystemSmsCode 对外系统短信验证码
     * @return 对外系统短信验证码集合
     */
    public List<OutSystemSmsCode> selectOutSystemSmsCodeList(OutSystemSmsCode outSystemSmsCode);

    /**
     * 新增对外系统短信验证码
     * 
     * @param outSystemSmsCode 对外系统短信验证码
     * @return 结果
     */
    public int insertOutSystemSmsCode(OutSystemSmsCode outSystemSmsCode);

    /**
     * 修改对外系统短信验证码
     * 
     * @param outSystemSmsCode 对外系统短信验证码
     * @return 结果
     */
    public int updateOutSystemSmsCode(OutSystemSmsCode outSystemSmsCode);

    /**
     * 删除对外系统短信验证码
     * 
     * @param outId 对外系统短信验证码ID
     * @return 结果
     */
    public int deleteOutSystemSmsCodeById(String outId);

    /**
     * 批量删除对外系统短信验证码
     * 
     * @param outIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOutSystemSmsCodeByIds(String[] outIds);

    /**
     * 新增对外系统短信验证码日志信息
     * @param outSystemSmsLog
     * @return
     */
    public int insertOutSystemSmsLog(OutSystemSmsLog outSystemSmsLog);
}
