package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.OutSystemSmsCodeMapper;
import com.ruoyi.activiti.service.IOutSystemSmsCodeService;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OutSystemSmsCode;
import com.ruoyi.system.domain.OutSystemSmsLog;
import com.ruoyi.system.service.IPubParaValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对外系统短信验证码Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-25
 */
@Service
public class OutSystemSmsCodeServiceImpl implements IOutSystemSmsCodeService, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(OutSystemSmsCodeServiceImpl.class);
    /**
     * 发送短信验证码的间隔时间默认一分钟
     */
    private Long intervalTime = 1L;
    /**
     * 短信验证码的有效时间默认五分钟
     */
    private Long effectiveTime = 5L;
    /**
     * 短信验证码的有效时间最大重试次数默认为3次
     */
    private Long maxTryNum = 3L;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private OutSystemSmsCodeMapper outSystemSmsCodeMapper;

    @Autowired
    private PubBizSmsServiceImpl pubBizSmsService;

    /**
     * 查询对外系统短信验证码
     *
     * @param outId 对外系统短信验证码ID
     * @return 对外系统短信验证码
     */
    @Override
    public OutSystemSmsCode selectOutSystemSmsCodeById(String outId) {
        return outSystemSmsCodeMapper.selectOutSystemSmsCodeById(outId);
    }

    /**
     * 查询对外系统短信验证码列表
     *
     * @param outSystemSmsCode 对外系统短信验证码
     * @return 对外系统短信验证码
     */
    @Override
    public List<OutSystemSmsCode> selectOutSystemSmsCodeList(OutSystemSmsCode outSystemSmsCode) {
        return outSystemSmsCodeMapper.selectOutSystemSmsCodeList(outSystemSmsCode);
    }

    /**
     * 新增对外系统短信验证码
     *
     * @param outSystemSmsCode 对外系统短信验证码
     * @return 结果
     */
    @Override
    public int insertOutSystemSmsCode(OutSystemSmsCode outSystemSmsCode) {
        return outSystemSmsCodeMapper.insertOutSystemSmsCode(outSystemSmsCode);
    }

    /**
     * 修改对外系统短信验证码
     *
     * @param outSystemSmsCode 对外系统短信验证码
     * @return 结果
     */
    @Override
    public int updateOutSystemSmsCode(OutSystemSmsCode outSystemSmsCode) {
        return outSystemSmsCodeMapper.updateOutSystemSmsCode(outSystemSmsCode);
    }

    /**
     * 删除对外系统短信验证码对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOutSystemSmsCodeByIds(String[] ids) {
        return outSystemSmsCodeMapper.deleteOutSystemSmsCodeByIds(ids);
    }

    /**
     * 删除对外系统短信验证码信息
     *
     * @param outId 对外系统短信验证码ID
     * @return 结果
     */
    @Override
    public int deleteOutSystemSmsCodeById(String outId) {
        return outSystemSmsCodeMapper.deleteOutSystemSmsCodeById(outId);
    }

    /**
     * 获取短信验证码时先去查询判断是否存在，如存在判断时候最后一次修改时间与当前间隔时间为60秒
     * 间隔在60秒以内直接返回错误信息，如超过60秒时间则重新发送验证码并覆盖之前的数据
     * 如果没有查询到数据则直接新建一条数据，成功返回成功标识、验证码信息，失败返回失败信息描述
     *
     * @param outSystemSmsCode
     * @return
     */
    @Override
    public Map sendSmsCode(OutSystemSmsCode outSystemSmsCode) {
        Map<Object, Object> map = new HashMap<>();
        // 是否插入log标识
        boolean isInsertLog = true;
        boolean flag;
        String message = "";
        String smsCode = "";
        List<OutSystemSmsCode> outSystemSmsCodes = outSystemSmsCodeMapper.selectOutSystemSmsCodeList(outSystemSmsCode);
        // 判断数据库中是否存在该条短信信息，若不存在则新插入，若存在则更新记录
        if (!CollectionUtils.isEmpty(outSystemSmsCodes)) {
            OutSystemSmsCode outSystemSmsCode1 = outSystemSmsCodes.get(0);
            Date updateTime = outSystemSmsCode1.getUpdateTime();
            long time = updateTime.getTime();
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - time <= intervalTime * 60 * 1000) {
                flag = false;
                //isInsertLog = false;
                message = "在间隔时间" + intervalTime * 60 + "秒内不能重复频繁获取短信验证码！";
            } else {
                long effectiveTime = StringUtils.isNotEmpty(outSystemSmsCode.getMobileAppEffectiveTime()) ? Long.valueOf(outSystemSmsCode.getMobileAppEffectiveTime()) : this.effectiveTime * 60 * 1000;
                smsCode = sendSms(outSystemSmsCode1, effectiveTime);
                if (StringUtils.isEmpty(smsCode)) {
                    flag = false;
                    message = "发送短信验证码失败！";
                } else {
                    outSystemSmsCode1.setSmsCode(smsCode);
                    outSystemSmsCode1.setUpdateTime(new Date());
                    outSystemSmsCode1.setTryNum(0L);
                    outSystemSmsCodeMapper.updateOutSystemSmsCode(outSystemSmsCode1);
                    flag = true;
                    message = "获取短信验证码成功！";
                }
            }
        } else {
            long effectiveTime = StringUtils.isNotEmpty(outSystemSmsCode.getMobileAppEffectiveTime()) ? Long.valueOf(outSystemSmsCode.getMobileAppEffectiveTime()) : this.effectiveTime * 60 * 1000;
            smsCode = sendSms(outSystemSmsCode, effectiveTime);
            if (StringUtils.isEmpty(smsCode)) {
                flag = false;
                message = "获取短信验证码失败！";
            } else {
                outSystemSmsCode.setSmsCode(smsCode);
                outSystemSmsCode.setOutId(UUID.getUUIDStr());
                outSystemSmsCode.setCreateTime(new Date());
                outSystemSmsCode.setUpdateTime(new Date());
                outSystemSmsCode.setTryNum(0L);
                outSystemSmsCodeMapper.insertOutSystemSmsCode(outSystemSmsCode);
                flag = true;
                message = "获取短信验证码成功！";
            }
        }

        // 插入短信日志表
        if (isInsertLog) {
            int rows = insertOutSystemSmsLog(outSystemSmsCode, flag ? "1" : "0");
            if (rows > 0) {
                log.debug("##########对接外系统访问运维管理平台获取短信验证码,插入日志成功,系统名称|系统编码|手机号信息为:"
                        + outSystemSmsCode.getSysName() + "|" + outSystemSmsCode.getSysCode() + "|" + outSystemSmsCode.getPhoneNumber());
            }
        }

        map.put("flag", flag);
        map.put("message", message);
        map.put("smsCode", smsCode);
        return map;
    }

    /**
     * 发送短信验证码方法
     *
     * @param outSystemSmsCode
     * @return
     */
    public String sendSms(OutSystemSmsCode outSystemSmsCode, long effectiveTime) {
        return pubBizSmsService.sendSmsOutSystem(outSystemSmsCode, effectiveTime);
    }

    @Override
    public Map volatileSmsCode(OutSystemSmsCode outSystemSmsCode) {
        boolean flag;
        String message = "";
        Map<Object, Object> map = new HashMap<>();
        OutSystemSmsCode code = new OutSystemSmsCode();
        code.setPhoneNumber(outSystemSmsCode.getPhoneNumber());
        code.setSysCode(outSystemSmsCode.getSysCode());
        List<OutSystemSmsCode> list = outSystemSmsCodeMapper.selectOutSystemSmsCodeList(code);
        if (!CollectionUtils.isEmpty(list) && list.size() == 1) {
            OutSystemSmsCode code1 = list.get(0);
            if (StringUtils.isNotNull(code1.getTryNum()) && code1.getTryNum() >= maxTryNum) {
                flag = false;
                message = "超过最大重试验证次数请重新获取短信验证码,最大重试次数为" + maxTryNum + "次！";
            } else {
                long time = code1.getUpdateTime().getTime();
                long volatileTime = System.currentTimeMillis() - time;
                // 移动运维短信有效时间以传递的为准
                long validTime = StringUtils.isNotEmpty(outSystemSmsCode.getMobileAppEffectiveTime()) ? Long.valueOf(outSystemSmsCode.getMobileAppEffectiveTime()) : effectiveTime * 60 * 1000;
                if (volatileTime <= validTime) {
                    if (outSystemSmsCode.getSmsCode().equals(code1.getSmsCode())) {
                        flag = true;
                        message = "短信验证码验证成功！";
                        // 验证成功重置重试次数为0
                        code1.setTryNum(0L);
                    } else {
                        flag = false;
                        message = "短信验证码验证错误！";
                        // 验证失败更新重试次数
                        code1.setTryNum(code1.getTryNum() == null ? 0L : code1.getTryNum() + 1);
                    }
                    outSystemSmsCodeMapper.updateOutSystemSmsCode(code1);
                } else {
                    message = "短信验证码超时，超时时间为：";
                    flag = false;
                    if(validTime > 60 * 60 * 1000){
                        validTime = validTime / 60 / 60 / 1000;
                        message += validTime + "小时，请重新获取短信验证码！";
                    } else {
                        message += effectiveTime + "分钟，请重新获取短信验证码！";
                    }
                }
            }
        } else {
            flag = false;
            message = "短信验证码验证错误！";
        }
        map.put("flag", flag);
        map.put("message", message);
        return map;
    }

    /**
     * 插入短信验证码日志表
     *
     * @param smsCode
     * @param sendStatus 发送状态0-失败 1-成功
     * @return
     */
    public int insertOutSystemSmsLog(OutSystemSmsCode smsCode, String sendStatus) {
        OutSystemSmsLog smsLog = new OutSystemSmsLog();
        smsLog.setLogId(UUID.getUUIDStr());
        smsLog.setSysName(smsCode.getSysName());
        smsLog.setSysCode(smsCode.getSysCode());
        smsLog.setPhoneNumber(smsCode.getPhoneNumber());
        smsLog.setMsg(smsCode.getMsg());
        smsLog.setSendStatus(sendStatus);
        smsLog.setCreateTime(new Date());
        return outSystemSmsCodeMapper.insertOutSystemSmsLog(smsLog);
    }

    @Override
    public Map sendMsg(OutSystemSmsCode smsCode){
        Map<String, Object> map = new HashMap();
        OutSystemSmsCode selectSmsCode = new OutSystemSmsCode();
        selectSmsCode.setSysCode(smsCode.getSysCode());
        selectSmsCode.setPhoneNumber(smsCode.getPhoneNumber());
        // 是否插入log标识
        boolean isInsertLog = true;
        boolean flag;
        String message = "";
        List<OutSystemSmsCode> outSystemSmsCodes = outSystemSmsCodeMapper.selectOutSystemSmsCodeList(selectSmsCode);
        // 判断数据库中是否存在该条短信信息，若不存在则新插入，若存在则更新记录，均需要发送短信
        if (!CollectionUtils.isEmpty(outSystemSmsCodes)) {
            OutSystemSmsCode outSystemSmsCode1 = outSystemSmsCodes.get(0);
            Date updateTime = outSystemSmsCode1.getUpdateTime();
            long time = updateTime.getTime();
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - time <= intervalTime * 60 * 1000 && smsCode.getMsg().equals(outSystemSmsCode1.getMsg())) {
                flag = false;
                isInsertLog = false;
                message = "在间隔时间" + intervalTime * 60 + "秒内不能重复频繁发送短信！";
            } else {
                pubBizSmsService.sendMsgOutSystem(smsCode.getPhoneNumber(), smsCode.getMsg());
                outSystemSmsCode1.setUpdateTime(new Date());
                outSystemSmsCode1.setMsg(smsCode.getMsg());
                outSystemSmsCodeMapper.updateOutSystemSmsCode(outSystemSmsCode1);
                flag = true;
                message = "发送短信成功！";
            }
        } else {
            pubBizSmsService.sendMsgOutSystem(smsCode.getPhoneNumber(), smsCode.getMsg());
            smsCode.setOutId(UUID.getUUIDStr());
            smsCode.setCreateTime(new Date());
            smsCode.setUpdateTime(new Date());
            outSystemSmsCodeMapper.insertOutSystemSmsCode(smsCode);
            flag = true;
            message = "发送短信成功！";
        }

        // 插入短信日志表
        if (isInsertLog) {
            int rows = insertOutSystemSmsLog(smsCode, flag ? "1" : "0");
            if (rows > 0) {
                log.debug("##########对接外系统访问运维管理平台发送短信,插入日志成功,系统名称|系统编码|手机号信息为:"
                        + smsCode.getSysName() + "|" + smsCode.getSysCode() + "|" + smsCode.getPhoneNumber());
            }
        }

        map.put("flag", flag);
        map.put("message", message);
        return map;
    }

    private final String smsCodeValidTimeKey = "sms_code_valid_time";

    @Override
    public void afterPropertiesSet() {
        log.debug("-----加载外接短信验证码有效时间开始------");
        // 验证码有效时间先查询数据字典，如为空则使用默认时间为五分钟，如不为空则使用字典配置时间
        List<PubParaValue> values = pubParaValueService.selectPubParaValueByParaName(smsCodeValidTimeKey);
        this.effectiveTime = CollectionUtils.isEmpty(values) ? effectiveTime : Long.valueOf(values.get(0).getValue());
        log.debug("-----加载外接短信验证码有效时间结束，短信验证码有效时间为" + effectiveTime + "分钟------");
    }
}
