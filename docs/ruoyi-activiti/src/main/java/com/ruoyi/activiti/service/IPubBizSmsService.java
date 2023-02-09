package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubBizSms;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;

import java.util.List;

/**
 * 短信发送Service接口
 *
 * @author ruoyi
 * @date 2021-02-09
 */
public interface IPubBizSmsService {
    /**
     * 根据主键查询短信发送
     *
     * @param pubBizSmsId 短信发送ID
     * @return
     */
    public PubBizSms selectPubBizSmsById(String pubBizSmsId);

    /**
     * 查询短信发送列表
     *
     * @param pubBizSms 短信发送
     * @return 短信发送集合
     */
    public List<PubBizSms> selectPubBizSmsList(PubBizSms pubBizSms);

    /**
     * 新增短信发送
     *
     * @param pubBizSms 短信发送
     * @return 结果
     */
    public int insertPubBizSms(PubBizSms pubBizSms);

    /**
     * 修改短信发送
     *
     * @param pubBizSms 短信发送
     * @return 结果
     */
    public int updatePubBizSms(PubBizSms pubBizSms);

    /**
     * 批量删除短信发送
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePubBizSmsByIds(String ids);

    /**
     * 删除短信发送信息
     *
     * @param pubBizSmsId 短信发送ID
     * @return 结果
     */
    public int deletePubBizSmsById(String pubBizSmsId);

    /**
     * 根据输入的账号密校验发送短信验证码
     *
     * @param user
     * @return 短信验证码
     */
    public String send(OgUser user);

    /**
     * 存储短信表信息发送无回复短信
     *
     * @param pubBizPresms
     */
    public void findPreSmsAndSend(PubBizPresms pubBizPresms);

    /**
     * 发送信息并提示回复短信
     *
     * @param taskId               流程Id
     * @param bizId                业务单Id
     * @param person               操作人
     * @param smsPerson            接收短信对象
     * @param processDefinitionKey 流程模板key
     */
    public void sendMessage(String taskId, String bizId, OgPerson person, OgPerson smsPerson, String processDefinitionKey);

    /**
     * 接收短信并处理流程
     *
     * @param vfCode    验证码
     * @param telephone 发送手机号
     * @return 是否版本发布上行短信审批
     */
    public boolean ReceiveFlow(String vfCode, String telephone);

    List<PubBizSms> selectPubBizSmsListTwo(PubBizSms pubBizSms);
}
