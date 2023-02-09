package com.ruoyi.activiti.service.forward;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;
import org.activiti.engine.task.IdentityLink;

import java.util.List;
import java.util.Map;

/**
 * 软研对接接口
 *
 * @author 14735
 */
public interface IForwardService {

    /**
     * 610002单号生成（公共）
     *
     * @param type
     * @return
     */
    public Map getNo(String type) throws BusException;

    /**
     * 610003查询流程记录（公共）
     *
     * @param bizId
     * @return
     */
    PagerRecords getPagerFlowLogs(Pager pager, String bizId);

    /**
     * 610005查询所属/所涉应用系统（发布管理）
     *
     * @param pager
     * @return
     */
    PagerRecords getPagerOgSyss(Pager pager,Map<String, Object> params);

    /**
     * 610006查询下发分行（发布管理）
     *
     * @return
     */
    List getProvence();

    /**
     * 610007查询下发技术局（发布管理）
     *
     * @return
     */
    List getProvenceAgencies();

    /**
     * 610008 查询业务审核人（发布管理）
     *
     * @param params
     * @return
     */
    List getbbss(Map<String, Object> params);

    /**
     * 610009 查询业务主管（发布管理）
     *
     * @param params
     * @return
     */
    List getbbssd(Map<String, Object> params);

    /**
     * 610010 查询技术审核人（发布管理）
     *
     * @param pname
     * @param orgName
     * @return
     */
    PagerRecords getJssh(String pname, String orgName, Pager pager);

    /**
     * 610011-待技术/技术主管审核版本查询
     *
     * @return
     */
    PagerRecords getUserPagerTasks(Pager pager, String userId, String roleIds, String name, String caption,
                                   String versionInfoNo, String versionInfoName, String versionProducerId) throws BusException;

    /**
     * 610012-技术审核通过
     *
     * @param initiator
     * @param variables
     */
    void saveFlowJs(String initiator, Map<String, Object> variables) throws BusException;

    /**
     * 610013-技术/技术主管审核不通过
     *
     * @param initiator
     * @param versionInfoId
     * @param technologyOpinion
     */
    void saveFlowDown(String initiator, String versionInfoId, String name, String technologyOpinion);

    /**
     * 610014-版本单历史查询
     *
     * @param versionInfoNo
     * @param versionInfoName
     * @param versionProducerId
     * @param sys
     * @param versionCreateTimeStart
     * @param versionCreateTimeEnd
     * @param startUpgraedTime
     * @param endUpgraedTime
     * @param useApproval
     * @param useDivisionChiefApproval
     * @param uploaderApproval
     * @param versionStatuss
     * @param ifjj
     * @return
     */
    PagerRecords getVmBizInfoAll(Pager pager, String versionInfoNo,
                                 String versionInfoName,
                                 String versionProducerId, String sys,
                                 String versionCreateTimeStart,
                                 String versionCreateTimeEnd,
                                 String startUpgraedTime,
                                 String endUpgraedTime,
                                 String useApproval,
                                 String useDivisionChiefApproval,
                                 String uploaderApproval,
                                 String versionStatuss, String ifjj);

    /**
     * 610015-版本单详情信息查询
     *
     * @param id
     * @return
     */
    Map getVmBizInfo(String id);

    /**
     * 610016-版本发布申请
     *
     * @param initiator
     * @param fileCiphertext
     * @param variables
     */
    void saveFlowData(String initiator, String fileCiphertext, Map<String, Object> variables);

    /**
     * 610018查询安全审核人（发布管理）
     *
     * @return
     */
    PagerRecords getAqsh(String initiator);

    /**
     * 610017-技术主管审核通过
     *
     * @param initiator
     * @param versionInfoId
     * @param technologyLeadOpinion
     * @return
     */
    Map saveFlowJsLd(String initiator, String versionInfoId, String technologyLeadOpinion);

    /**
     * 610019查询应用审批人（发布管理）
     *
     * @return
     */
    PagerRecords getYysp(String initiator);

    /**
     * 610020查询技术主管审核人（发布管理）
     *
     * @return
     */
    PagerRecords getJszg(String initiator);

    /**
     * 610021查询下发中心
     *
     * @return
     */
    List<Map> getCenter();

    /**
     * 610022任务执行情况
     *
     * @param versionInfoId
     * @return
     */
    PagerRecords getPagerVmBizAll(Pager pager, String versionInfoId);

    /**
     * 610023-查看流程图片
     *
     * @param businessKey
     * @param flowType
     * @return
     */
    Map getFlowImage(String businessKey, String flowType) throws Exception;

    /**
     * 610024查询自动化实施人（发布管理）
     *
     * @return
     */
    PagerRecords getAutomate(Pager pager, String initiator,String pName,String mobilePhone);

    /**
     * 610025业务审核｜业务主管审核通过｜否决
     * @param initiator
     * @param versionInfoId
     * @param name
     * @param passFlag
     * @param comment
     */
    void businessApproval(String initiator,String versionInfoId,String name,String passFlag,String comment);

    /**
     * 710003-查询变更请求受理人
     * @param orgid
     * @return
     * @throws BusException
     */
    List<Record> getEffectPersonsById(String orgid) throws BusException;

    /**
     *
     * 710005查询审批人
     * @param orgid
     * @return
     * @throws BusException
     */
    List<Record> getCheckPersonsByAgencyAndRole(String orgid) throws BusException;

    /**
     * 710006变更单信息提交/重新提交
     * @param faultNo
     * @param createOrgId
     * @param implementorOrgId
     * @param implementorId
     * @param changeCategoryTypeinfoId
     * @param importantLev
     * @param sysname
     * @param sysid
     * @param changeStage
     * @param changeResource
     * @param isNotice
     * @param isStop
     * @param changeSingleName
     * @param changeDetails
     * @param checkOrg
     * @param checker
     * @param fileCiphertext
     * @param initiator
     * @throws BusException
     */
    void startBGQQ(String faultNo, String createOrgId, String implementorOrgId, String implementorId,
                   String changeCategoryTypeinfoId, String importantLev, String sysname,
                   String sysid, String changeStage, String changeResource, String isNotice,
                   String isStop, String changeSingleName, String changeDetails, String checkOrg,
                   String checker, String fileCiphertext, String initiator) throws BusException;

    /**
     * 710007-分管领导审批变更请求
     * @param initiator
     * @param changeId
     * @param performDescription
     * @param name
     * @param fileCiphertext
     * @throws BusException
     */
    void getPagerFZApproval(String initiator,
                            String changeId,
                            String performDescription,
                            String name,
                            String fileCiphertext) throws BusException;

    /**
     * // 710008-审批变更请求
     * @param initiator
     * @param changeId
     * @param flowSelect
     * @param fuchecker
     * @param performDescription
     * @param name
     * @param fileCiphertext
     * @throws BusException
     */
    void getPagerApproval(String initiator,
                          String changeId,
                          String flowSelect,
                          String fuchecker,
                          String stauts,
                          String performDescription,
                          String name,
                          String fileCiphertext) throws BusException;

    /**
     * // 710010-查询用户创建的变更单（过滤掉待提交状态的）
     * @param pager
     * @param changeSingleName
     * @param changeCode
     * @param stauts
     * @param typeinfoId
     * @param changeResource
     * @param isNotice
     * @param createTimeStart
     * @param initiator
     * @return
     * @throws BusException
     */
    PagerRecords getPagerMyself(Pager pager,
                                String changeSingleName,
                                String changeCode,
                                String stauts,
                                String typeinfoId,
                                String changeResource,
                                String isNotice,
                                String createTimeStart,
                                String initiator) throws BusException;

    /**
     * 710011-查询用户需要待审批/待分管领导审批的变更单
     * @param userId
     * @param roleIds
     * @param pager
     * @param faultNo
     * @param changeSingleName
     * @param stauts
     * @param pname
     * @param createTime
     * @param typeinfoId
     * @param name
     * @return
     * @throws BusException
     */
    PagerRecords getUserPagerTasksBGQQ(String userId,
                                       String roleIds,
                                       Pager pager,
                                       String faultNo,
                                       String changeSingleName,
                                       String stauts,
                                       String pname,
                                       String createTime,
                                       String typeinfoId,
                                       String name) throws BusException;

    /**
     * 710012查询分管领导方法
     * @param initiator
     * @return
     * @throws BusException
     */
    List<Map<String, String>> getFuchecker(String initiator) throws BusException;

    /**
     * 710013-查询历史变更单
     * @param pager
     * @param changeSingleName
     * @param changeCode
     * @param stauts
     * @param typeinfoId
     * @param sysname
     * @param createOrg
     * @param effectOrg
     * @param creater
     * @param createTimeStart
     * @param createTimeEnd
     * @param effctTimeStart
     * @param effctTimeEnd
     * @param implementor
     * @return
     * @throws BusException
     */
    PagerRecords getPagers(Pager pager,
                           String changeSingleName,
                           String changeCode,
                           String stauts,
                           String typeinfoId,
                           String sysname,
                           String createOrg,
                           String effectOrg,
                           String creater,
                           String createTimeStart,
                           String createTimeEnd,
                           String effctTimeStart,
                           String effctTimeEnd,
                           String implementor) throws BusException;

    /**
     * 710014-变更单详情信息查询
     * @param id
     * @return
     * @throws BusException
     */
    Record getCmBizSingleQingQiu(String id) throws BusException;


    /**
     * #重新提交
     * @param businessKey
     * @param createOrgId
     * @param implementorOrgId
     * @param implementorId
     * @param changeCategoryTypeinfoId
     * @param importantLev
     * @param sysname
     * @param sysid
     * @param changeStage
     * @param changeResource
     * @param isNotice
     * @param isStop
     * @param changeSingleName
     * @param changeDetails
     * @param checkOrg
     * @param checker
     * @param fileCiphertext
     * @param initiator
     * @throws BusException
     */
    void alterBGQQ(String businessKey,
                   String createOrgId,
                   String implementorOrgId,
                   String implementorId,
                   String changeCategoryTypeinfoId,
                   String importantLev,
                   String sysname,
                   String sysid,
                   String changeStage,
                   String changeResource,
                   String isNotice,
                   String isStop,
                   String changeSingleName,
                   String changeDetails,
                   String checkOrg,
                   String checker,
                   String fileCiphertext,
                   String initiator) throws BusException;

    /**
     * 710016-流程记录列表中超链接页面--基本信息
     * @param id
     * @return
     * @throws BusException
     */
    PubFlowLog getFlowLog(String id) throws BusException;


    /**
     * 710017-流程记录列表中超链接页面--下一步操作
     * @param taskId
     * @return
     */
    List<IdentityLink> getFlowAssignees(String taskId);

    /**
     * //变更单作废
     * @param id
     * @return
     * @throws BusException
     */
    CmBizQingqiu removeChange(String id) throws BusException;

    /**
     * 810001 问题单查询
     * @param pager
     * @param issuefxNo
     * @param issuefxName
     * @param issueSource
     * @param issueFenLei
     * @param issueType
     * @param issueOrg
     * @param sysName
     * @param createTimeStart
     * @param createTimeEnd
     * @param state
     * @param create
     * @param createOrg
     * @param reviewer
     * @param lev
     * @return
     * @throws BusException
     */
    PagerRecords getPagerIssueFx(Pager pager,
                                 String issuefxNo,
                                 String issuefxName,
                                 String issueSource,
                                 String issueFenLei,
                                 String issueType,
                                 String issueOrg,
                                 String createTimeStart,
                                 String createTimeEnd,
                                 String state,
                                 String create,
                                 String createOrg,
                                 String reviewer,
                                 String lev,
                                 String sysName,
                                 String technologyAuditId);

    /**
     * 810003 查询问题单详情信息
     * @param issuefxId
     * @return
     * @throws BusException
     */
     Record getIssuefx( String issuefxId) throws BusException;
    /**
     *   810009-查询待技术受理的问题单查询
     * @param userId
     * @param roleIds
     * @param pager
     * @param issuefxNo
     * @param issuefxName
     * @param issueFenLei
     * @param issueType
     * @param sysName
     * @param createTime
     * @param state
     * @param name
     * @return
     * @throws BusException
     */
     PagerRecords getDealIssueFx(String userId,
                                       String roleIds, Pager pager, // 分页条件
                                       String issuefxNo,
                                       String issuefxName,
                                       String issueFenLei,
                                       String issueType,
                                       String sysName,
                                       String createTime,
                                       String state,
                                       String name) throws BusException;
    /**
     * 810010--技术受理通过
     * @param initiator
     * @param issueFxId
     * @param papDesc
     * @param fileCiphertext
     * @throws BusException
     */
    public void saveFlowDeal1(String initiator,String issueFxId, String papDesc, String fileCiphertext) throws BusException;
    /**
     * 810011--技术受理未通过---退回修改
     * @param initiator
     * @param issueFxId
     * @param performDesc
     * @param fileCiphertext
     * @throws BusException
     */
    public void saveFlowEdit2( String initiator, String issueFxId, String performDesc, String fileCiphertext) throws BusException;
    /**
     *  810012技术受理需技术经理协同处理---转发技术
     * @param initiator
     * @param issueFxId
     * @param sysName
     * @param technologyAuditId
     * @param dddesc
     * @param fileCiphertext
     * @throws BusException
     */
    public void saveFlowRepeat( String initiator,
                                String issueFxId,
                                String sysName,
                                String technologyAuditId,
                                String dddesc,
                                String fileCiphertext) throws BusException;
    /**
     *  810013技术受理涉及业务问题---转发业务
     * @param initiator
     * @param issueFxId
     * @param businessOrg0
     * @param bussId
     * @param performDesc
     * @param fileCiphertext
     * @throws BusException
     */
    public void saveFlowRepeatYw( String initiator,
                                  String issueFxId,
                                  String businessOrg0,
                                  String bussId,
                                  String performDesc,
                                  String fileCiphertext) throws BusException;
    /**
     *   810014--业务受理通过
     * @param initiator
     * @param issueFxId
     * @param performDesc
     * @param fileCiphertext
     * @throws BusException
     */
    public void saveFlowBuss(String initiator, String issueFxId, String performDesc, String fileCiphertext) throws BusException;
    /**
     *  810016--查询所属应用系统
     * @param pager
     * @param caption
     * @param orgname
     * @return
     * @throws BusException
     */
    public PagerRecords getPagerOgSyssByRestrict(Pager pager,String caption, String orgname) throws BusException;
    /**
     *  810017--查询技术经理
     * @param pager
     * @param pname
     * @param orgname
     * @return
     * @throws BusException
     */
    public PagerRecords getProvinccePersonsByRoleByPage(Pager pager,String pname, String orgname)throws BusException;
    }
