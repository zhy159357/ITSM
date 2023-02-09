package com.ruoyi.activiti.service.impl;

import java.util.*;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.IOgPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.CmBizQingqiuMapper;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 变更请求单Service业务层处理
 * 
 * @author zhangxu
 * @date 2020-12-21
 */
@Service
public class CmBizQingqiuServiceImpl implements ICmBizQingqiuService 
{
    @Autowired
    private CmBizQingqiuMapper cmBizQingqiuMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询变更请求单
     * 
     * @param changeId 变更请求单ID
     * @return 变更请求单
     */
    @Override
    public CmBizQingqiu selectCmBizQingqiuById(String changeId)
    {
        return cmBizQingqiuMapper.selectCmBizQingqiuById(changeId);
    }

    /**
     * 查询变更请求单 //待受理
     *
     * @param changeId 变更请求单ID
     * @return 变更请求单
     */
    @Override
    public CmBizQingqiu selectCmBizQingqiuAcceptanceById(String changeId)
    {
        return cmBizQingqiuMapper.selectCmBizQingqiuAcceptanceById(changeId);
    }

    /**
     * 查询变更请求单//待审批//待受理
     *
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单
     */
    public CmBizQingqiu selectBGQQVO(CmBizQingqiu cmBizQingqiu){
        return cmBizQingqiuMapper.selectBGQQVO(cmBizQingqiu);
    }


    /**
     * 变更请求单列表
     * 
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单
     */
    @Override
    public List<CmBizQingqiu> selectCmBizQingqiuList(CmBizQingqiu cmBizQingqiu)
    {
        return cmBizQingqiuMapper.selectCmBizQingqiuList(cmBizQingqiu);
    }

    /**
     * 查询变更请求单列表
     *
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单
     */
    @Override
    public List<CmBizQingqiu> selectQingqiuList(CmBizQingqiu cmBizQingqiu)
    {
        return cmBizQingqiuMapper.selectQingqiuList(cmBizQingqiu);
    }

    /**
     * 查询变更请求单列表(排除待提交的)
     *
     * @param cmBizQingqiu 变更请求单
     * @return 变更请求单集合
     */
    public List<CmBizQingqiu> selectCmBizQingqiuListNotNew(CmBizQingqiu cmBizQingqiu){

        return cmBizQingqiuMapper.selectCmBizQingqiuListNotNew(cmBizQingqiu);
    }

    /**
     * 新增变更请求单
     * 
     * @param cmBizQingqiu 变更请求单
     * @return 结果
     */
    @Override
    public int insertCmBizQingqiu(CmBizQingqiu cmBizQingqiu)
    {
        return cmBizQingqiuMapper.insertCmBizQingqiu(cmBizQingqiu);
    }

    /**
     * 修改变更请求单
     * 
     * @param cmBizQingqiu 变更请求单
     * @return 结果
     */
    @Override
    public int updateCmBizQingqiu(CmBizQingqiu cmBizQingqiu)
    {
        return cmBizQingqiuMapper.updateCmBizQingqiu(cmBizQingqiu);
    }

    /**
     * 删除变更请求单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCmBizQingqiuByIds(String ids)
    {
        return cmBizQingqiuMapper.deleteCmBizQingqiuByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除变更请求单信息
     * 
     * @param changeId 变更请求单ID
     * @return 结果
     */
    @Override
    public int deleteCmBizQingqiuById(String changeId)
    {
        return cmBizQingqiuMapper.deleteCmBizQingqiuById(changeId);
    }

    /**
     * 作废变更请求单
     *
     * @param id 需要作废的数据ID
     * @return 结果
     */
    @Override
    public int cmBizQingQiuToCancle(String id)
    {
        return cmBizQingqiuMapper.cmBizQingQiuToCancle(id);
    }

    /**
     * 710012查询分管领导方法
     * @param cmBizQingqiu
     * @return
     */
    @Override
    public List<OgPerson> secondaryDeptLeader(CmBizQingqiu cmBizQingqiu){
        return cmBizQingqiuMapper.secondaryDeptLeader(cmBizQingqiu);
    }

    /**
     * 查询协同受理人
     *
     * @param cmBizQingqiu 变更请求单
     * @return 协同受理人集合
     */
    public List<OgPerson> togetherAcceptancePerson(CmBizQingqiu cmBizQingqiu){
        return cmBizQingqiuMapper.togetherAcceptancePerson(cmBizQingqiu);
    }

    /**
     * 查询分管领导方法
     * @param person
     * @return
     */
    @Override
    public List<OgPerson> selectLeader(OgPerson person){
        return cmBizQingqiuMapper.selectLeader(person);
    }

    /**
     * 根据机构id获取下级机构（包括父级机构）
     * @param orgId
     * @return
     */
    @Override
    public List<OgOrg> selectAllChildOrg(String orgId){
        List<OgOrg> ogOrgs = deptMapper.selectAllChildOrg(orgId);
        if (ogOrgs == null || ogOrgs.size() == 0) {
            ogOrgs.add(deptMapper.selectDeptById(orgId));
        }
        return ogOrgs;
    }

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    /**
     * 移动APP分管领导审批变更请求单
     * @param cmBizQingqiu  待审批的变更单对象
     * @param userId        审批人
     * @param trend         审批标志
     * @return
     */
    @Override
    @Transactional
    public AjaxResult checkCmBizQingQiu(CmBizQingqiu cmBizQingqiu, String userId, String trend){
        String comment = (String) cmBizQingqiu.getParams().get("comment");
        cmBizQingqiu = cmBizQingqiuMapper.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        CmBizQingqiu qingqiu = new CmBizQingqiu();
        if ("0600".equals(cmBizQingqiu.getStauts())) {
            Map<String, Object> map = new HashMap<>();
            String businessKey = cmBizQingqiu.getChangeId();
            map.put("businessKey",businessKey);
            map.put("comment", comment);
            map.put("processDefinitionKey", "BGQQ");
            map.put("userId", userId);
            String status = "";
            if ("0".equals(trend)) {
                // 此时流程走向为调度处理
                map.put("reCode", "0");
                status = "0400";
                try {
                    activitiCommService.complete(map);
                    if ("1".equals(cmBizQingqiu.getIsNotice())) {
                        //给受理人发送短信
                        OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(cmBizQingqiu.getCheckerId());
                        String text1 = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台受理。";
                        sendSms(text1, ogPerson1, userId);
                    }
                    qingqiu.setChangeId(cmBizQingqiu.getChangeId());
                    qingqiu.setStauts(status);
                    String dealIdList = cmBizQingqiu.getDealIdList();
                    qingqiu.setDealIdList(dealIdList + cmBizQingqiu.getFucheckerId() + ",");
                    cmBizQingqiuMapper.updateCmBizQingqiu(qingqiu);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
                return AjaxResult.success("变更请求单：" + cmBizQingqiu.getChangeCode()  +"审批成功");
            } else if ("1".equals(trend)) {
                // 此时流程走向为退回
                map.put("reCode", "1");
                status = "0200";
                try {
                    activitiCommService.complete(map);
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());//给审批人发短信
                    OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(userId);
                    String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"，审批人："+ ogPerson1.getpName() +"，请登录运维管理平台操作。";
                    sendSms(text, ogPerson, userId);
                    qingqiu.setChangeId(cmBizQingqiu.getChangeId());
                    qingqiu.setStauts(status);
                    String dealIdList = cmBizQingqiu.getDealIdList();
                    qingqiu.setDealIdList(dealIdList + cmBizQingqiu.getFucheckerId() + ",");
                    cmBizQingqiuMapper.updateCmBizQingqiu(qingqiu);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
                return AjaxResult.success("变更请求单：" + cmBizQingqiu.getChangeCode()  +"回退操作成功");
            }
        }
        return AjaxResult.success("变更请求单：" + cmBizQingqiu.getChangeCode()  +"操作失败,请刷新列表重新操作");
    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     * @param userId     短信创建人
     */
    public void sendSms(String setSmsText, OgPerson person, String userId) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(userId);// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);
        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 移动APP分管领导查看近一月已办
     * @param bizIds
     * @return
     */
    @Override
    public List<CmBizQingqiu> selectQingQiuListApp(List<String> bizIds, String search){
        CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
        cmBizQingqiu.getParams().put("changeIds", bizIds);
        cmBizQingqiu.getParams().put("search", search);
        return cmBizQingqiuMapper.selectQingqiuList(cmBizQingqiu);
    }
}
