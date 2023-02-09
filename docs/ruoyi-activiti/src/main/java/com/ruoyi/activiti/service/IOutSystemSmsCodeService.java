package com.ruoyi.activiti.service;

import com.ruoyi.system.domain.OutSystemSmsCode;

import java.util.List;
import java.util.Map;

/**
 * 对外系统短信验证码Service接口
 * 
 * @author ruoyi
 * @date 2021-04-25
 */
public interface IOutSystemSmsCodeService 
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
     * 批量删除对外系统短信验证码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOutSystemSmsCodeByIds(String[] ids);

    /**
     * 删除对外系统短信验证码信息
     * 
     * @param outId 对外系统短信验证码ID
     * @return 结果
     */
    public int deleteOutSystemSmsCodeById(String outId);

    /**
     * 获取短信验证码
     * @param outSystemSmsCode
     * @return
     */
    public Map sendSmsCode(OutSystemSmsCode outSystemSmsCode);

    /**
     * 验证短信验证码
     * @param outSystemSmsCode
     * @return
     */
    public Map volatileSmsCode(OutSystemSmsCode outSystemSmsCode);

    /**
     * 发送短信
     * @param outSystemSmsCode
     * @return
     */
    public Map sendMsg(OutSystemSmsCode outSystemSmsCode);
}
