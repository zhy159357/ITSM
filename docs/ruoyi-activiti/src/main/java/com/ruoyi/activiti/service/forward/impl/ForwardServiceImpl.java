package com.ruoyi.activiti.service.forward.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.listener.VersionPublicProcessListener;
import com.ruoyi.activiti.mapper.VmBizInfoMapper;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.service.forward.IForwardService;
import com.ruoyi.activiti.service.impl.EndTaskAdapterImplBGQQ;
import com.ruoyi.activiti.service.impl.VmBizInfoServiceImpl;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.netty.utils.Base64;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;
import com.ruoyi.common.utils.ConverterUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.*;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 软研对接接口实现类
 *
 * @author 14735
 */
@Service("ogForwardManager")
@Transactional(rollbackFor = Exception.class)
public class ForwardServiceImpl implements IForwardService {

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private IIdGeneratorService generatorService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private OgPostService ogPostService;
    @Autowired
    private ICommonService commonService;

    @Autowired
    private VmBizInfoServiceImpl vmBizInfoService;

    @Autowired
    private VmBizInfoMapper vmBizInfoMapper;

    @Autowired
    private IVmBizTaskinfoService vmBizTaskinfoService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private VersionPublicProcessListener listener;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IPubFlowLogService pubFlowLogService;

    @Autowired
    private IForwardFileService forwardFileService;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    @Autowired
    private ICmBizQingqiuService cmBizQingqiuService;

    @Autowired
    private IAutomateMiddleService automateMiddleService;

    @Autowired
    private IImBizIssuefxService issueService;

    @Autowired
    private IPubParaValueService iPubParaValueService;

    @Autowired
    private EndTaskAdapterImplBGQQ endTaskAdapterImplBGQQ;

    private static final Logger log = LoggerFactory.getLogger(ForwardServiceImpl.class);

    /**
     * 610002单号生成
     * @param type
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public Map getNo(@ServiceParam(name = "type") String type) throws BusException {
        if (StringUtils.isEmpty(type)) {
            throw new BusException("100004", "所传类型不能为空");
        }
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        String number = "";
        if ("BB".equals(type)) {
            generator.setBizType(type);
            number = generatorService.selectIdGeneratorByType(generator);
        } else if ("BG".equals(type)) {
            type = "BGQQ";
            generator.setBizType(type);
            number = generatorService.selectIdGeneratorByType(generator);
        } else {
            throw new BusException("100004", "所传类型不存在");
        }
        Record record = new Record();
        record.put("faultNo", type + nowDateStr + number);
        return record;
    }

    /**
     * 610003查询流程记录（公共）
     * @param pager
     * @param bizId
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getPagerFlowLogs(Pager pager, @ServiceParam(name = "bizId") String bizId) {
        if (StringUtils.isEmpty(bizId)) {
            throw new BusException("100004", "所传参数不能为空");
        }
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setBizId(bizId);
        Page<Object> page = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<PubFlowLog> pubFlowLogs = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        for (PubFlowLog log : pubFlowLogs) {
            log.setNextTaskNames(log.getNextTaskName());
            log.setNextPreformerDesc(convertNextPreformerDesc(log));
        }
        PagerRecords pagerRecords = new PagerRecords(pubFlowLogs, (int) page.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 翻译下一步操作人，若有中文不翻译，如果是人员id查询得到名称
     * @param log
     * @return
     */
    public String convertNextPreformerDesc(PubFlowLog log){
        String nextPerson = log.getNextPerformerDesc();
        String nextPerformerDesc = "";
        if(StringUtils.isNotEmpty(nextPerson)){
            if(StringUtils.hasChinese(nextPerson)){
                return nextPerson;
            }
            String[] nextPersonL = nextPerson.split(":");
            for (String nextPersonm : nextPersonL) {
                String[] nextP = nextPersonm.split(",");
                for (String pId : nextP) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
                    if(null != ogPerson){
                        nextPerformerDesc += ogPerson.getpName() + ",";
                    }
                }
                if (StringUtils.isNotEmpty(nextPerformerDesc)) {
                    nextPerformerDesc = nextPerformerDesc.substring(0, nextPerformerDesc.length() - 1);
                    nextPerformerDesc += ":";
                }
            }
            if (StringUtils.isNotEmpty(nextPerformerDesc)) {
                nextPerformerDesc = nextPerformerDesc.substring(0, nextPerformerDesc.length() - 1);
            }
        }
        return nextPerformerDesc;
    }

    /**
     * 610005查询所属/所涉应用系统（发布管理）
     * @param pager
     * @param params
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getPagerOgSyss(Pager pager, Map<String, Object> params) {
        List<Map> mapList = new ArrayList<Map>();
        OgSys ogSys = new OgSys();
        ogSys.setInvalidationMark("1");
        if (params != null) {
            ogSys.setCaption((String) params.get("caption"));
            ogSys.setOrgName((String) params.get("agency.orgname"));
        }
        Page<OgSys> ogSysPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<OgSys> ogSysList = applicationManagerService.selectOgSysList(ogSys);
        for (OgSys sys : ogSysList) {
            Record map = new Record();
            map.put("sysid", sys.getSysId());
            map.put("code", sys.getCode());
            map.put("caption", sys.getCaption());
            map.put("memo", sys.getMemo());
            map.put("isks", sys.getIsKeySys());
            map.put("invalidationMark", sys.getInvalidationMark());
            // 是否外联渠道标识
            map.put("isOutChannel", sys.getIsOutChannel());
            map.put("orgname", sys.getOgOrg() == null ? "" : sys.getOgOrg().getOrgName());
            mapList.add(map);
        }
        PagerRecords pagerRecords = new PagerRecords(mapList, (int) ogSysPage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 610006查询下发分行（发布管理）
     * @return
     */
    @Override
    @EsbServiceMapping
    public List getProvence() {
        List<Map> ogList = new ArrayList<>();
        OgOrg org = new OgOrg();
        org.setOrgCode("%00088801");
        List<OgOrg> ogOrgs = deptService.selectDeptByOrgCodeLike(org);
        for (OgOrg o : ogOrgs) {
            Record map = new Record();
            map.put("orgid", o.getOrgId());
            map.put("orgname", o.getOrgName());
            ogList.add(map);
        }
        return ogList;
    }

    /**
     * 610007查询下发技术局（发布管理）
     * @return
     */
    @Override
    @EsbServiceMapping
    public List getProvenceAgencies() {
        List<Map> ogList = new ArrayList<>();
        OgOrg org = new OgOrg();
        org.setOrgCode("%00088802");
        List<OgOrg> ogOrgs = deptService.selectDeptByOrgCodeLike(org);
        for (OgOrg o : ogOrgs) {
            Record map = new Record();
            map.put("orgid", o.getOrgId());
            map.put("orgname", o.getOrgName());
            ogList.add(map);
        }
        return ogList;
    }

    /**
     * 610008 查询业务审核人（发布管理）
     * @param params
     * @return
     */
    @Override
    @EsbServiceMapping
    public List getbbss(Map<String, Object> params) {
        List<Record> recordList = new ArrayList<Record>();
        List<OgPerson> business = getOgPerson(params, "business");
        for (OgPerson ogPerson : business) {
            Record record = new Record();
            record.put("pid", ogPerson.getpId());
            record.put("pname", ogPerson.getpName());
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * 610009 查询业务主管（发布管理）
     * @param params
     * @return
     */
    @Override
    @EsbServiceMapping
    public List getbbssd(Map<String, Object> params) {
        List<Record> recordList = new ArrayList<Record>();
        List<OgPerson> leader = getOgPerson(params, "leader");
        for (OgPerson ogPerson : leader) {
            Record record = new Record();
            record.put("pid", ogPerson.getpId());
            record.put("pname", ogPerson.getpName());
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * 610010 查询技术审核人（发布管理）
     * @param pname
     * @param orgname
     * @param pager
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getJssh(@ServiceParam(name = "pname") String pname,
                                @ServiceParam(name = "agency.orgname") String orgname, Pager pager) {
        OgPerson person = new OgPerson();
        person.setpName(pname);
        person.setOrgname(orgname);
        person.setrId("2304");
        List<OgPerson> personList = commonService.selectPersonByOrgAndRole(person);
        List<Record> recordList = new ArrayList<Record>();
        for (OgPerson ogPerson : personList) {
            Record record = new Record();
            record.put("pid", ogPerson.getpId());
            record.put("pname", ogPerson.getpName());
            recordList.add(record);
        }
        PagerRecords pagerRecords = new PagerRecords(recordList, 0);
        return pagerRecords;
    }

    /**
     * 待技术/技术主管审核版本查询
     * @param pager
     * @param userId
     * @param roleIds
     * @param name
     * @param caption
     * @param versionInfoNo
     * @param versionInfoName
     * @param versionProducerId
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getUserPagerTasks(Pager pager,
                                          @ServiceParam(name = "userId", pubProperty = "userId") String userId,
                                          @ServiceParam(name = "roleIds", pubProperty = "roleIds") String roleIds,
                                          @ServiceParam(name = "name") String name, @ServiceParam(name = "sys.caption") String caption,
                                          @ServiceParam(name = "params.versionInfoNo") String versionInfoNo,
                                          @ServiceParam(name = "params.versionInfoName") String versionInfoName,
                                          @ServiceParam(name = "params.versionProducerId") String versionProducerId) throws BusException {
        if (StringUtils.isEmpty(name)) {
            throw new BusException("100004", "审核标识不允许为空");
        }
        if ("JS".equals(name)) {
            name = "技术审核";
        } else if ("JSZG".equals(name)) {
            name = "技术主管审核";
        } else if("YW".equals(name)) {
            name = "业务审核";
        } else if("YWZG".equals(name)) {
            name = "业务主管审核";
        } else {
            throw new BusException("100004", "审核标识参数传入错误");
        }
        List<Map<String, Object>> reList = activitiCommService.userTaskUserId(userId, "VmBn", name);
        List<VmBizInfo> resultList = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            String business_key = (String) map.get("businesskey");
            String taskId = (String) map.get("taskId");
            if (business_key != null) {
                VmBizInfo vmBizInfo = new VmBizInfo();
                vmBizInfo.setVersionInfoId(business_key);
                vmBizInfo.setVersionInfoNo(versionInfoNo);
                vmBizInfo.setVersionInfoName(versionInfoName);
                vmBizInfo.setVersionProducerName(versionProducerId);
                OgSys ogSys = new OgSys();
                ogSys.setCaption(caption);
                vmBizInfo.setOgSys(ogSys);
                VmBizInfo vm = vmBizInfoService.selectVmBizInfoByCondition(vmBizInfo);
                if (vm != null) {
                    vm.getParams().put("taskId", taskId);
                    resultList.add(vm);
                }
            }
        }
        // 代办列表根据生成时间倒序排序
        if (resultList.size() > 1) {
            resultList = resultList.stream().sorted(Comparator.comparing(VmBizInfo::getVersionCreateTime).reversed()).collect(Collectors.toList());
        }

        List<Map> mapList = new ArrayList<>();
        for (VmBizInfo vmBizInfo : resultList) {
            Record map = new Record();
            map.put("versionInfoId", vmBizInfo.getVersionInfoId());
            map.put("versionInfoNo", vmBizInfo.getVersionInfoNo());
            map.put("versionCreateTime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizInfo.getVersionCreateTime()));
            map.put("versionInfoName", vmBizInfo.getVersionInfoName());
            map.put("pname", vmBizInfo.getVersionProducerName());
            map.put("versionStatus", vmBizInfo.getVersionStatus());
            map.put("technologyState", vmBizInfo.getTechnologyState());
            map.put("caption", vmBizInfo.getOgSys() == null ? "" : vmBizInfo.getOgSys().getCaption());
            mapList.add(map);
        }
        List subList = getSubList(mapList, pager.getPageIndex(), pager.getPageSize());
        PagerRecords pagerRecords = new PagerRecords(subList, mapList.size());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 610012-技术审核通过
     * @param initiator
     * @param variables
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public void saveFlowJs(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                           Map<String, Object> variables) throws BusException {
        if (!variables.containsKey("versionInfoId") && StringUtils.isNull(variables.get("versionInfoId")))
            throw new BusException("100004", "版本ID不能为空");
        System.out.println("技术审核版本ID是：" + variables.get("versionInfoId").toString());
        if (!variables.containsKey("technologyApproval.pid")
                && StringUtils.isNull(variables.get("technologyApproval.pid")))
            throw new BusException("100004", "技术主管审核人不能为空");

        if (!variables.containsKey("technologyOpinion") && StringUtils.isNull(variables.get("technologyOpinion")))
            throw new BusException("100004", "审核意见不允许为空");

        Task task = activitiCommService.getTask(variables.get("versionInfoId").toString(), "技术审核");

        if (task == null)
            throw new BusException("100004", "版本ID错误，无法找到流程 versiInfoId=" + variables.get("versionInfoId"));

        //this.userFlowLogAuthJudge(task.getId(),initiator);
        Map map = new HashMap<>();
        map.put("businessKey", variables.get("versionInfoId"));
        map.put("technologyApproval", variables.get("technologyApproval.pid"));
        map.put("comment", variables.get("technologyOpinion"));
        map.put("userId", initiator);
        String processDefinitionKey = "VmBn";
        map.put("processDefinitionKey", processDefinitionKey);
        map.put("taskId", task.getId());
        map.put("reCode", "0");

        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByIdNoConvert(variables.get("versionInfoId").toString());
        if("1".equals(vmBizInfo.getVersionType())){
            VmBizInfo vm = new VmBizInfo();
            // 纯技术修改业务审核状态为已通过
            vm.setBusinessState(VersionStatusConstants.BUSINESS_STATUS_PASS);
            vm.setVersionInfoId(variables.get("versionInfoId").toString());
            vmBizInfoService.updateVmBizInfo(vm);
        }

        activitiCommService.complete(map);
    }

    /**
     * 610013-技术/技术主管审核不通过
     * @param initiator
     * @param name
     * @param versionInfoId
     * @param technologyOpinion
     */
    @Override
    @EsbServiceMapping
    public void saveFlowDown(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                             @ServiceParam(name = "name") String name, @ServiceParam(name = "versionInfoId") String versionInfoId,
                             @ServiceParam(name = "technologyOpinion") String technologyOpinion) {
        if (StringUtils.isEmpty(name))
            throw new BusException("100004", "技术审核参数不允许为空");
        if (StringUtils.isEmpty(versionInfoId))
            throw new BusException("100004", "版本ID不允许为空");
        if (StringUtils.isEmpty(technologyOpinion))
            throw new BusException("100004", "审核意见不允许为空");

        if ("JS".equals(name))
            name = "技术审核";
        else if ("JSZG".equals(name))
            name = "技术主管审核";
        else
            throw new BusException("100004", "技术审核参数错误");

        Task task = activitiCommService.getTask(versionInfoId, name);

        if (task == null)
            throw new BusException("100004", "版本ID错误，无法找到流程 versionInfoId=" + versionInfoId);

        //this.userFlowLogAuthJudge(task.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("businessKey", versionInfoId);
        map.put("userId", initiator);
        String processDefinitionKey = "VmBn";
        map.put("processDefinitionKey", processDefinitionKey);
        map.put("comment", technologyOpinion);
        map.put("taskId", task.getId());
        map.put("reCode", "1"); // 流程走向作废
        map.put("versionStatus", "14");// 作废状态
        activitiCommService.complete(map);
    }

    /**
     * 610014-版本单历史查询
     * @param pager
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
    @Override
    @EsbServiceMapping
    public PagerRecords getVmBizInfoAll(Pager pager, @ServiceParam(name = "versionInfoNo") String versionInfoNo,
                                        @ServiceParam(name = "versionInfoName") String versionInfoName,
                                        @ServiceParam(name = "versionProducerId") String versionProducerId, @ServiceParam(name = "sys") String sys,
                                        @ServiceParam(name = "versionCreateTimeStart") String versionCreateTimeStart,
                                        @ServiceParam(name = "versionCreateTimeEnd") String versionCreateTimeEnd,
                                        @ServiceParam(name = "startUpgraedTime") String startUpgraedTime,
                                        @ServiceParam(name = "endUpgraedTime") String endUpgraedTime,
                                        @ServiceParam(name = "useApproval") String useApproval,
                                        @ServiceParam(name = "useDivisionChiefApproval") String useDivisionChiefApproval,
                                        @ServiceParam(name = "uploaderApproval") String uploaderApproval,
                                        @ServiceParam(name = "versionStatuss") String versionStatuss, @ServiceParam(name = "ifjj") String ifjj) {
        VmBizInfo vmBizInfo = new VmBizInfo();
        vmBizInfo.setVersionInfoNo(versionInfoNo);
        vmBizInfo.setVersionInfoName(versionInfoName);
        vmBizInfo.setVersionProducerName(versionProducerId);
        OgSys ogSys = new OgSys();
        ogSys.setCaption(sys);
        vmBizInfo.setOgSys(ogSys);
        Map params = vmBizInfo.getParams();
        if(StringUtils.isNotEmpty(versionCreateTimeStart)){
            params.put("versionCreateTimeStart", versionCreateTimeStart.substring(0,8) + "000000");
        }
        if(StringUtils.isNotEmpty(versionCreateTimeEnd)){
            params.put("versionCreateTimeEnd", versionCreateTimeEnd.substring(0,8) + "235959");
        }
        vmBizInfo.setStartUpgradeTime(startUpgraedTime);
        vmBizInfo.setEndUpgradeTime(endUpgraedTime);
        vmBizInfo.setUseApprovalName(useApproval);
        vmBizInfo.setUseDivisionChiefApprovalName(useDivisionChiefApproval);
        vmBizInfo.setUploaderApprovalName(uploaderApproval);
        vmBizInfo.setVersionStatus(versionStatuss);
        if (StringUtils.isNotEmpty(versionStatuss)) {
            params.put("versionStatusArray", Convert.toStrArray(versionStatuss));
        }
        vmBizInfo.setAcceptRole(ifjj);
        Page<VmBizInfo> vmBizInfoPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<VmBizInfo> vmBizInfoList = vmBizInfoService.selectVmBizInfoList(vmBizInfo);
        List<Record> recordList = new ArrayList<>();
        for (VmBizInfo vmBizInfo1 : vmBizInfoList) {
            Record record = new Record();
            record.put("versionInfoId", vmBizInfo1.getVersionInfoId());// 版本ID
            record.put("versionInfoNo", vmBizInfo1.getVersionInfoNo());// 版本编号
            record.put("versionInfoName", vmBizInfo1.getVersionInfoName());// 版本简称
            record.put("startUpgraedTime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizInfo1.getStartUpgradeTime()));// 升级时间
            record.put("versionAttr", vmBizInfo1.getVersionAttr());// 版本属性
            record.put("versionStatus", vmBizInfo1.getVersionStatus());// 状态
            record.put("versionName", vmBizInfo1.getVersionName());// 版本摘要
            record.put("versionProducerId", vmBizInfo1.getVersionProducerName());// 版本发起人
            record.put("createTime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizInfo1.getVersionCreateTime()));// 版本创建时间
            record.put("useApproval", vmBizInfo1.getUseApprovalName());// 应用审批人
            record.put("useDivisionChiefApproval", vmBizInfo1.getUseDivisionChiefApprovalName());// 应用处长审批人
            // 所属系统
            if (vmBizInfo1.getOgSys() != null) {
                record.put("caption", vmBizInfo1.getOgSys().getCaption());
            }
            recordList.add(record);
        }
        PagerRecords pagerRecords = new PagerRecords(recordList, (int) vmBizInfoPage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 610015-版本单详情信息查询
     * @param id
     * @return
     */
    @Override
    @EsbServiceMapping
    public Map getVmBizInfo(@ServiceParam(name = "versionInfoId") String id) {
        Record map = new Record();
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        if (vmBizInfo == null) {
            return map;
        }
        // 版本ID
        map.put("versionInfoId", vmBizInfo.getVersionInfoId());
        // 版本编号
        map.put("versionInfoNo", vmBizInfo.getVersionInfoNo());
        // 下发中心
        map.put("agencyZx", vmBizInfo.getAgencyCenterName());
        // 涉及外围系统
        map.put("sysname", StringUtils.isNotEmpty(vmBizInfo.getSysName()) ? vmBizInfo.getSysName() : "");
        // 版本发布标题
        map.put("versionInfoName", vmBizInfo.getVersionInfoName());
        // 版本摘要
        map.put("versionName", vmBizInfo.getVersionName());
        // 版本属性
        map.put("versionAttr", vmBizInfo.getVersionAttr());
        // 更新环境
        map.put("upgradeEnviponment", vmBizInfo.getUpgradeEnvironment());
        // 业务需求内容
        map.put("businessRequirementText", vmBizInfo.getBusinessRequirementText());
        // 功能描述
        map.put("versionDescription", vmBizInfo.getVersionDescription());
        // 涉及模块服务
        map.put("issuedNo", vmBizInfo.getIssuedNo());
        // 测试要点
        map.put("businessValidationText", vmBizInfo.getBusinessValidationText());
        // 业务测试人员
        map.put("questionId", vmBizInfo.getQuestionId());
        // 技术测试人员
        map.put("drafterId", vmBizInfo.getDrafterId());
        // 涉及更新的库
        map.put("acceptOrganization", vmBizInfo.getAcceptOrganization());
        // 涉及更新的服务
        map.put("creaTteunits", vmBizInfo.getCreateunits());
        // 涉及重启的服务
        map.put("copyRequiteUnit", vmBizInfo.getCopyRequiteUnit());
        // 涉及变更的内容
        map.put("noticeText", vmBizInfo.getNoticeText());
        // 是否通报办公室
        map.put("ifReportOffice", vmBizInfo.getIfReportOffice());
        // 是否报备银保监会
        map.put("ifReportCBRC", vmBizInfo.getIfReportCbrc());
        // 是否通报客服中心
        map.put("ifReportCallCenter", vmBizInfo.getIfReportCallCenter());
        // 是否需停业升级
        map.put("ifStopBusiness", vmBizInfo.getIfStopBusiness());
        // 开始升级时间
        map.put("startUpgraedTime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizInfo.getStartUpgradeTime()));
        // 完成升级时间
        map.put("endUpgraedTime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizInfo.getEndUpgradeTime()));
        // 升级支持人员
        map.put("technicalSupportId", vmBizInfo.getTechnicalSupportId());
        // 升级支持电话
        map.put("technicalSupportPhone", vmBizInfo.getTechnicalSupportPhone());
        // 是否发布公告
        map.put("ifIssueNotice", vmBizInfo.getIfIssueNotice());
        // 安全审核人
        map.put("safetyAuditId", StringUtils.isNotEmpty(vmBizInfo.getSafetyAuditName()) ? vmBizInfo.getSafetyAuditName() : "");
        // 是否自动化
        map.put("isAutomate", vmBizInfo.getIsAutomate());
        // 自动化实施人
        map.put("automateAuditId", StringUtils.isNotEmpty(vmBizInfo.getAutomateAuditName()) ? vmBizInfo.getAutomateAuditName() : "");
        // 业务部门
        String orgName = this.convertBusinessOrgName(vmBizInfo.getBusinessOrg());
        map.put("businessOrg", StringUtils.isNotEmpty(orgName) ? orgName : "");
        // 业务审核
        String businessAudit = this.convertBusinessApproval(vmBizInfo.getBusinessAuditId());
        map.put("businessAuditId", StringUtils.isNotEmpty(businessAudit) ? businessAudit : "");
        // 业务主管
        String businessApproval = this.convertBusinessApproval(vmBizInfo.getBusinessApprovalId());
        map.put("businessApprovalId", StringUtils.isNotEmpty(businessApproval) ? businessApproval : "");
        // 所属系统
        if (vmBizInfo.getOgSys() != null) {
            map.put("caption", vmBizInfo.getOgSys().getCaption());
        }

        vmBizInfoService.convertVmBizInfoParams(vmBizInfo);
        // 下发分行
        map.put("issuedName", vmBizInfo.getIssuedBranchName());
        // 下发技术局
        map.put("ogAgencyName", vmBizInfo.getIssuedUnitName());
        // 版本类型
        map.put("versionType", vmBizInfo.getVersionTypeName());
        return map;
    }

    /**
     * 610016-版本发布申请
     * @param initiator
     * @param fileCiphertext
     * @param variables
     */
    @Override
    @EsbServiceMapping
    public void saveFlowData(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                             @ServiceParam(name = "fileCiphertext") String fileCiphertext, Map<String, Object> variables) {
        // 版本发起控制同一版本单号不能重复提交
        int count = vmBizInfoMapper.selectCountByVersionInfoNo(variables.get("versionInfoNo").toString());
        if (count > 0) {
            throw new BusException("100004", "版本单号:" + variables.get("versionInfoNo") + "已存在，不能重复提交！");
        }
        if (StringUtils.isEmpty(fileCiphertext))
            throw new BusException("100004", "附件不允许为空");
        log.debug("软研版本发起传递参数为:" + JSON.toJSONString(variables));
        // 获取当前登陆人所有岗位
        List<OgPost> list = ogPostService.selectLoginUserOgPosts(initiator);
        int b = 0;
        if (list.size() > 0) {
            for (OgPost op : list) {
                if (op.getPostId().contains("0015")) {
                    b++;
                }
            }
            if (b == 0) {
                throw new BusException("100003", "创建人没有权限发起版本单");
            }
        }
        // 所属系统外联渠道标识为【是】则安全审核必须为【是】，为【否】或为空则安全审核标识必须为【否】
        if("1".equals(variables.get("isOutChannel"))){
            if (!"1".equals(variables.get("ifSafetyAudit"))) {
                throw new BusException("100004", "所属系统外联渠道标识为【是】，则安全审核标识必须为【是】");
            }
        } else {
            if (!"2".equals(variables.get("ifSafetyAudit"))) {
                throw new BusException("100004", "所属系统外联渠道标识为【否】或为空，则安全审核标识必须为【否】");
            }
        }
        if ("1".equals(variables.get("ifSafetyAudit"))) {// 安全审核
            if (variables.containsKey("safetyAuditId")) {
                variables.put("versionStatus", VersionStatusConstants.VERSION_STATUS_2);
                variables.put("reCode", "0");
                variables.put("safetyAudit", variables.get("safetyAuditId"));
            } else {
                throw new BusException("100004", "安全审核人不能为空");
            }
        } else if ("1".equals(variables.get("versionType"))) {// 纯技术改造
            variables.put("reCode", "1");
            // 待技术审核
            variables.put("versionStatus", VersionStatusConstants.VERSION_STATUS_6);
        } else {// 包含业务审核｜技术审核
            variables.put("versionStatus", VersionStatusConstants.VERSION_STATUS_3);
            variables.put("reCode", "2");
            variables.put("businessState", "1");
            variables.put("technologyState", "1");
        }
        VmBizInfo vmBizInfo1 = new VmBizInfo();
        // 纯技术改造不校验业务审核，其他情况校验业务审核人
        if (!"1".equals(variables.get("versionType"))) {
            if (variables.containsKey("businessAuditId")) {
                variables.put("businessAudit", variables.get("businessAuditId"));
                // 此时需要保存业务审核人
                vmBizInfo1.getParams().put("cleanBusinessFlag",0);
            } else {
                throw new BusException("100004", "业务审核人不能为空");
            }
        }else{
            // 纯技术此时需要保存业务审核人
            vmBizInfo1.getParams().put("cleanBusinessFlag",1);
        }
        if (variables.containsKey("technologyAuditId")) {
            variables.put("technologyAudit", variables.get("technologyAuditId"));
        } else {
            throw new BusException("100004", "技术审核人不能为空");
        }
        if (variables.containsKey("useApproval")) {
            variables.put("useApproval", variables.get("useApproval"));
        } else {
            throw new BusException("100004", "应用审核人不能为空");
        }

        String bizId = UUID.getUUIDStr();
        variables.put("businessKey", bizId);
        variables.put("versionProducerId", initiator);
        variables.put("userId", initiator);
        String processDefinitionKey = "VmBn";

        // 此处需要启动流程并保存版本单业务数据信息
        try {
            this.insertVmBizInfo(variables,vmBizInfo1);
            if ("1".equals(variables.get("isAutomate"))) {
                // 如果是自动化则先保存中间表，待定时任务轮通过自动化校验之后再提交流程
                this.insertAutoMiddle(bizId, variables);
                // 此时将版本状态置为中间状态待提交
                variables.put("versionStatus", VersionStatusConstants.VERSION_STATUS_1);
            } else {
                // 如果不是自动化则直接提交，不做发送自动化校验，直接提交流程
                activitiCommService.startProcess(bizId, processDefinitionKey, variables);
                vmBizInfoService.sendMsg(vmBizInfo1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("999999", "流程启动失败！");
        }
        // 流程执行包含关联附件(附件多个)
        String[] str = Convert.toStrArray(fileCiphertext);
        OgPerson person = ogPersonService.selectOgPersonById(initiator);
        boolean checkFlag = false;
        for (int i = 0; i < str.length; i++) {
            Attachment attachment = forwardFileService.saveForwardFile(str[i], person, bizId, "vmBizInfo");
            if("1".equals(vmBizInfo1.getIsAutomate())){
                // 循环判断附件是否包含附件类型为自动化步骤
                if("3".equals(attachment.getFileType())){
                    checkFlag = true;
                }
            } else {
                // 不是自动化赋值为true跳过校验
                checkFlag = true;
            }
        }
        // 校验自动化标识为1必须有自动化附件，否则抛出异常
        if(!checkFlag){
            throw new BusException("100004", "自动化标识为【是】必须包含附件类型为【自动化步骤】的附件！");
        }
    }

    /**
     * 保存自动化校验中间表数据
     *
     * @param bizId
     * @param variables
     */
    public void insertAutoMiddle(String bizId, Map variables) {
        AutomateMiddle autoMiddle = new AutomateMiddle();
        autoMiddle.setAutoId(UUID.getUUIDStr());
        autoMiddle.setBusinessNo((String) variables.get("versionInfoNo"));
        autoMiddle.setBusinessId(bizId);
        autoMiddle.setStatus(AutomateMiddle.AUTO_STATUS_LOADING);
        autoMiddle.setCreateTime(DateUtils.getNowDate());
        autoMiddle.setCheckCount(0);
        automateMiddleService.insertAutomateMiddle(autoMiddle);
    }

    /**
     * @param variables
     * @throws Exception
     */
    private void insertVmBizInfo(Map<String, Object> variables,VmBizInfo vmBizInfo) {
        ConverterUtils.mapToObject(vmBizInfo, variables);
        vmBizInfo.setVersionInfoId(variables.get("businessKey").toString());
        vmBizInfo.setUseApprovalId(variables.get("useApproval").toString());
        // 业务审核人id去除重复
        if(StringUtils.isNotNull(variables.get("businessAuditId"))){
            vmBizInfo.setBusinessAuditId(StringUtils.removeDuplicationString(variables.get("businessAuditId").toString()));
            variables.put("businessAudit", vmBizInfo.getBusinessAuditId());
        }
        // 业务主管人id去除重复
        if(StringUtils.isNotNull(variables.get("businessApproval"))){
            vmBizInfo.setBusinessApprovalId(StringUtils.removeDuplicationString(variables.get("businessApproval").toString()));
            variables.put("businessApproval", vmBizInfo.getBusinessApprovalId());
        }
        vmBizInfo.setSysId(variables.get("sys.sysid").toString());
        vmBizInfo.setSysName(StringUtils.isNotNull(variables.get("sysname")) ? variables.get("sysname").toString() : "");
        vmBizInfo.setCreateunits(variables.get("creaTteunits").toString());
        vmBizInfo.setStartUpgradeTime(variables.get("startUpgraedTime").toString());
        vmBizInfo.setEndUpgradeTime(variables.get("endUpgraedTime").toString());
        vmBizInfo.setIfReportCbrc(variables.get("ifReportCBRC").toString());
        vmBizInfo.setUpgradeEnvironment(StringUtils.isNotNull(variables.get("upgradeEnviponment")) ? variables.get("upgradeEnviponment").toString() : "");
        vmBizInfo.getParams().put("insertFlag", "forward");
        // 如果是自动化暂时将版本状态置为待提交
        if ("1".equals(variables.get("isAutomate"))){
            vmBizInfo.setVersionStatus(VersionStatusConstants.VERSION_STATUS_1);
        }
        // 翻译软研传递过来的businessOrg参数为机构id
        vmBizInfo.setBusinessOrg(convertBusinessOrg(vmBizInfo.getBusinessOrg()));
        vmBizInfoService.insertVmBizInfo(vmBizInfo);
    }

    /**
     * 转换软研传递过来的部门机构码值参数为机构id
     * @param businessOrg
     * @return
     */
    public String convertBusinessOrg(String businessOrg){
        String businessOrgs = null;
        List<String> orgIds = new ArrayList<>();
        List<String> deptNames = new ArrayList<>();
        if(StringUtils.isNotEmpty(businessOrg)){
            log.debug("-----------软研传递过来的业务部门参数码值为:" + businessOrg + "---------");
            String[] orgs = Convert.toStrArray(businessOrg);
            if(StringUtils.isNotEmpty(orgs)){
                for(String org : orgs){
                    String deptName = pubParaValueService.selectPubParaValueByNameValue("vm_dept", org);
                    if(StringUtils.isNotEmpty(deptName)){
                        deptNames.add(deptName);
                        OgOrg o = deptService.selectDeptByOrgName(deptName);
                        if(o != null){
                            orgIds.add(o.getOrgId());
                        }
                    }
                }
            }
            businessOrgs = String.join(",", orgIds);
            log.debug("-----------软研传递过来的业务部门参数码值转换成业务部门名称:【" + String.join(",", deptNames) + "】-----------");
        }
        return StringUtils.isEmpty(businessOrgs) ? businessOrg : businessOrgs;
    }

    /**
     * 转换业务部门为中文名称
     * @param businessOrg
     * @return
     */
    public String convertBusinessOrgName(String businessOrg){
        String businessOrgTemp = null;
        List<String> orgNames = new ArrayList<>();
        if(StringUtils.isNotEmpty(businessOrg)){
            String[] orgArr = Convert.toStrArray(businessOrg);
            for(String org : orgArr){
                if(StringUtils.isNumeric(org)){
                    // 如果是数字需要使用字典翻译
                    String orgName = pubParaValueService.selectPubParaValueByNameValue("vm_dept", org);
                    if(StringUtils.isNotEmpty(orgName)){
                        orgNames.add(orgName);
                    }
                } else if(StringUtils.hasChinese(org)) {
                    // 如果是中文则不做处理
                } else {
                    // 如果是id则查询转换成中文
                    OgOrg ogOrg = deptService.selectDeptById(org);
                    if(StringUtils.isNotNull(ogOrg)){
                        orgNames.add(ogOrg.getOrgName());
                    }
                }
            }
            businessOrgTemp = String.join(",", orgNames);
        }
        return StringUtils.isEmpty(businessOrgTemp) ? businessOrg : businessOrgTemp;
    }

    /**
     * 转换业务审核｜领导审核人，若没有业务审核则返回空串
     * @param approvalId
     * @return
     */
    public String convertBusinessApproval(String approvalId){
        String businessApprovalName = null;
        List<String> businessApprovalNames = new ArrayList<>();
        if(StringUtils.isNotEmpty(approvalId)){
            String[] approvalArr = Convert.toStrArray(approvalId);
            for(String approval : approvalArr){
                OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
                if(StringUtils.isNotNull(ogPerson)){
                    businessApprovalNames.add(ogPerson.getpName());
                }
            }
            businessApprovalName = String.join(",", businessApprovalNames);
        }
        return StringUtils.isEmpty(businessApprovalName) ? StringUtils.EMPTY : businessApprovalName;
    }

    /**
     * 610017-技术主管审核通过
     * @param initiator
     * @param versionInfoId
     * @param technologyLeadOpinion
     * @return
     */
    @Override
    @EsbServiceMapping
    public Map saveFlowJsLd(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                            @ServiceParam(name = "versionInfoId") String versionInfoId,
                            @ServiceParam(name = "technologyLeadOpinion") String technologyLeadOpinion) {
        if (StringUtils.isEmpty(versionInfoId))
            throw new BusException("100004", "版本ID不允许为空");

        if (StringUtils.isEmpty(technologyLeadOpinion))
            throw new BusException("100004", "审核意见不允许为空");

        Task task = activitiCommService.getTask(versionInfoId, "技术主管审核");

        if (task == null)
            throw new BusException("100004", "无法找到流程 versiInfoId=" + versionInfoId);

        //this.userFlowLogAuthJudge(task.getId());
        Record map = new Record();
        Map<String, Object> variables = new HashMap<>();
        variables.put("businessKey", versionInfoId);
        variables.put("userId", initiator);
        String processDefinitionKey = "VmBn";
        variables.put("processDefinitionKey", processDefinitionKey);
        variables.put("taskId", task.getId());
        variables.put("comment", technologyLeadOpinion);
        variables.put("reCode", "0");// 通过

        activitiCommService.complete(variables);
        return map;
    }

    /**
     * 610018查询安全审核人
     * @param initiator
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getAqsh(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator) {
        OgPerson person = ogPersonService.selectOgPersonById(initiator);
        List<Map> mapList = new ArrayList<Map>();
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        OgPerson person2 = new OgPerson();

        String orgId = "";
        while (true) {
            String level = org.getOrgLv();
            // 当前登录人为一级机构，邮政金融运维，返回一级机构id
            if ("2".equals(level) || "1".equals(level)) {
                orgId = org.getOrgId();
                break;
            } else {
                if (StringUtils.isNotEmpty(org.getParentId())) {
                    org = deptService.selectDeptById(org.getParentId());
                }
            }
        }
        OgOrg org2 = deptService.selectDeptById(orgId);
        String levelCode = org2.getLevelCode();
        OgOrg org1 = new OgOrg();
        org1.setLevelCode(levelCode);
        person2.setOrg(org1);
        person2.setrId("2301");

        List<OgPerson> aqs = commonService.selectPersonByOrgAndRole(person2);
        for (OgPerson ogPerson : aqs) {
            Record map = new Record();
            map.put("pid", ogPerson.getpId());
            map.put("pname", ogPerson.getpName());
            mapList.add(map);
        }
        PagerRecords pagerRecords = new PagerRecords(mapList, mapList.size());
        Pager pager = new Pager(10, 1, 0);
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 610019查询应用审批人
     * @param initiator
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getYysp(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator) {
        OgPerson person = ogPersonService.selectOgPersonById(initiator);
        List<Map> mapList = new ArrayList<Map>();
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        OgPerson person2 = new OgPerson();

        String orgId = "";
        while (true) {
            String level = org.getOrgLv();
            // 当前登录人为一级机构，邮政金融运维，返回一级机构id
            if ("2".equals(level) || "1".equals(level)) {
                orgId = org.getOrgId();
                break;
            } else {
                if (StringUtils.isNotEmpty(org.getParentId())) {
                    org = deptService.selectDeptById(org.getParentId());
                }
            }
        }
        OgOrg org2 = deptService.selectDeptById(orgId);
        String levelCode = org2.getLevelCode();
        OgOrg org1 = new OgOrg();
        org1.setLevelCode(levelCode);
        person2.setOrg(org1);
        person2.setrId("2306");

        List<OgPerson> aqs = commonService.selectPersonByOrgAndRole(person2);
        for (OgPerson ogPerson : aqs) {
            Record map = new Record();
            map.put("pid", ogPerson.getpId());
            map.put("pname", ogPerson.getpName());
            mapList.add(map);
        }
        PagerRecords pagerRecords = new PagerRecords(mapList, mapList.size());
        Pager pager = new Pager(10, 1, 0);
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 610020查询技术主管审核人（发布管理）
     * @param initiator
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getJszg(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator) {
        OgPerson person = ogPersonService.selectOgPersonById(initiator);
        List<Map> mapList = new ArrayList<Map>();
        OgPerson person2 = new OgPerson();
        person2.setOrgId(person.getOrgId());
        person2.setrId("2305");
        List<OgPerson> jss = commonService.selectPersonByOrgAndRole(person2);
        for (OgPerson ogPerson : jss) {
            Record map = new Record();
            map.put("pid", ogPerson.getpId());
            map.put("pname", ogPerson.getpName());
            mapList.add(map);
        }
        PagerRecords pagerRecords = new PagerRecords(mapList, mapList.size());
        Pager pager = new Pager(10, 1, 0);
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 610021查询下发中心
     * @return
     */
    @Override
    @EsbServiceMapping
    public List<Map> getCenter() {
        List<Map> ogList = new ArrayList<Map>();
        OgOrg org = new OgOrg();
        org.setOrgCode("010%0888");
        // 内部标记
        org.setInoutsideMark("I");
        List<OgOrg> list = deptService.selectDeptByOrgCodeLike(org);
        for (OgOrg ogOrg : list) {
            Record map = new Record();
            map.put("orgid", ogOrg.getOrgId());
            map.put("orgname", ogOrg.getOrgName());
            ogList.add(map);
        }
        return ogList;
    }

    /**
     * 610022任务执行情况
     * @param pager
     * @param versionInfoId
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getPagerVmBizAll(Pager pager, @ServiceParam(name = "versionInfoId") String versionInfoId) {
        if (StringUtils.isEmpty(versionInfoId)) {
            throw new BusException("100004", "版本ID不允许为空");
        }
        List<Map> mapList = new ArrayList<Map>();
        VmBizTaskinfo taskInfo = new VmBizTaskinfo();
        taskInfo.setVersionInfoId(versionInfoId);
        Page<VmBizTaskinfo> taskInfoPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<VmBizTaskinfo> vmBizTaskinfos = vmBizTaskinfoService.selectVmBizTaskinfoList(taskInfo);
        for (VmBizTaskinfo vmBizTaskinfo : vmBizTaskinfos) {
            Map map = new HashMap();
            map.put("taskNo", vmBizTaskinfo.getTaskNo());// 任务单号
            map.put("taskproducetime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizTaskinfo.getTaskproducetime()));// 生成时间
            map.put("orgname", vmBizTaskinfo.getOgOrg().getOrgName());// 所属单位
            map.put("taskDealUserId", vmBizTaskinfo.getTaskDealUserName());// 附件下载人
            map.put("completeUserId", vmBizTaskinfo.getCompleteUserName());// 确认完成人
            map.put("updateTime", DateUtils.handleTimeYYYYMMDDHHMMSS(vmBizTaskinfo.getUpdate_time()));// 确认操作时间
            map.put("taskStatus", vmBizTaskinfo.getTaskStatus());// 任务状态
            mapList.add(map);
        }
        PagerRecords pagerRecords = new PagerRecords(mapList, (int) taskInfoPage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * 查看流程
     * @param businessKey
     * @param flowType
     * @return
     */
    @Override
    @EsbServiceMapping
    public Map getFlowImage(@ServiceParam(name = "versionInfoId") String businessKey,
                            @ServiceParam(name = "flowType") String flowType) {
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusException("100004", "版本ID不允许为空");
        }
        Record map = new Record();
        InputStream inputStream = activitiCommService.readResource(businessKey);
        map.put("image", Base64.encode(inputStream));
        return map;
    }

    /**
     * 610024查询自动化实施人
     * @param pager
     * @param initiator
     * @param pName
     * @param mobilePhone
     * @return
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getAutomate(Pager pager, @ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                                    @ServiceParam(name = "pName") String pName,
                                    @ServiceParam(name = "mobilePhone") String mobilePhone) {
        OgPerson person = new OgPerson();
        List<Map> mapList = new ArrayList<Map>();
        List<String> postIds = new ArrayList();
        // 岗位id为数据中心人员｜数据中心领导｜科技部人员｜科技部灵领导
        postIds.add("0015");
        postIds.add("0016");
        postIds.add("0010");
        postIds.add("0011");
        person.getParams().put("postIds", postIds);
        person.setpName(pName);
        person.setMobilPhone(mobilePhone);
        Page<OgPerson> automatePage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<OgPerson> personList = ogPersonService.selectOgPersonByPostIds(person);
        for (OgPerson ogPerson : personList) {
            Record map = new Record();
            map.put("pid", ogPerson.getpId());
            map.put("pname", ogPerson.getpName());
            map.put("mobilePhone", ogPerson.getMobilPhone());
            // 籍贯
            map.put("birthPlace", ogPerson.getBirthPlace());
            map.put("sex", "1".equals(ogPerson.getSex()) ? "男" : "女");
            map.put("orgname", ogPerson.getOrgname());
            mapList.add(map);
        }
        PagerRecords pagerRecords = new PagerRecords(mapList, (int) automatePage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    /**
     * @date 2021-10-25 新增接口
     * 业务审核｜业务主管审核  通过｜否决
     * @param initiator         userId
     * @param versionInfoId     版本id
     * @param name              YW-业务审核    YWZG-业务主管审核
     * @param passFlag          1-通过    0-否决
     * @param comment           审批意见
     */
    @Override
    @EsbServiceMapping
    public void businessApproval(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                                 @ServiceParam(name = "versionInfoId") String versionInfoId,
                                 @ServiceParam(name = "name") String name,
                                 @ServiceParam(name = "passFlag") String passFlag,
                                 @ServiceParam(name = "comment") String comment) {
        if(StringUtils.isEmpty(versionInfoId)){
            throw new BusException("100004", "业务审核参数错误！");
        }
        if(StringUtils.isEmpty(passFlag)){
            throw new BusException("100004", "业务审核参数错误！");
        }
        if(StringUtils.isEmpty(comment)){
            throw new BusException("100004", "业务审核意见不可为空！");
        }
        String description = "";
        if("YW".equals(name)){
            description = "业务审核";
        } else if("YWZG".equals(name)){
            description = "业务主管审核";
        } else {
            throw new BusException("100004", "业务审核参数错误！");
        }

        Task task = activitiCommService.getTask(versionInfoId, description);
        if(null == task){
            throw new BusException("100004", "版本ID错误，无法找到流程 versiInfoId=" + versionInfoId);
        }

        Map<String, Object> map = new HashMap<>();
        String status = "";
        if (VersionStatusConstants.PASS_FLAG_1.equals(passFlag)) {
            // 此时流程走向为通过到下一节点业务主管审核
            map.put("reCode", "0");
        } else {
            // 此时流程走向为作废
            map.put("reCode", "1");
            status = VersionStatusConstants.VERSION_STATUS_14;
        }
        map.put("businessKey", versionInfoId);
        map.put("comment", comment);

        map.put("userId", initiator);
        String processDefinitionKey = "VmBn";
        map.put("processDefinitionKey", processDefinitionKey);
        // 业务审核通过标识
        boolean isBusinessPass = false;
        // 是否取消标识
        boolean isCancel = false;
        if("YWZG".equals(name)){
            isBusinessPass = false;
        } else {
            if(VersionStatusConstants.VERSION_STATUS_14.equals(status)){
                // 否决
                isCancel = true;
            } else {
                // 通过
                isBusinessPass = true;
            }
        }
        VmBizInfo vmBizInfo = new VmBizInfo();
        vmBizInfo.setVersionStatus(status);
        vmBizInfo.setVersionInfoId(versionInfoId);
        activitiCommService.usersComplete(map);
        // 只有status不为空的时候才更新
        int rows = StringUtils.isNotEmpty(status) ? vmBizInfoService.updateVmBizInfo(vmBizInfo) : 1;
        // 业务审核通过｜否决需要发送短信，业务主管发短信部分在流程处理公共类
        if("YW".equals(name) && rows > 0) {
            VmBizInfo vm = vmBizInfoService.selectVmBizInfoByIdNoConvert(versionInfoId);
            OgPerson producePerson = ogPersonService.selectOgPersonById(vm.getVersionProducerId());
            // 通过的情况
            if(isBusinessPass){
                // 每个业务审核向项目经理发送短信
                OgPerson person = ogPersonService.selectOgPersonById(initiator);
                String message = "版本单号：" + vm.getVersionInfoNo() + ",标题：" + vm.getVersionInfoName() + "的版本业务审核：" + person.getpName() + ",已审批通过。";
                vmBizInfoService.sendSms(message, producePerson);
            }
            // 否决的情况
            if(isCancel){
                // 应用负责人
                OgPerson useApproval = ogPersonService.selectOgPersonById(vm.getUseApprovalId());
                listener.sendCancelMsg(producePerson, useApproval, vm);
            }
        }
    }

    /**
     * 判断用户是否有流程执行权限
     *
     * @param taskId
     * @throws BusException
     */
    private void userFlowLogAuthJudge(String taskId, String userId) throws BusException {
        if (StringUtils.isEmpty(taskId))
            throw new BusException("100004", "流程ID为空");

        OgPerson loginPerson = ogPersonService.selectOgPersonById(userId);

        String phone;
        if (loginPerson.getMobilPhone() != null)
            phone = loginPerson.getMobilPhone();
        else if (loginPerson.getPhone() != null)
            phone = loginPerson.getPhone();
        else
            throw new BusException("100004", "登录用户电话号为空");

        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        if (CollectionUtils.isEmpty(identityLinkList)) {
            throw new BusException("100001", "任务已执行完成");
        }
        for (IdentityLink id : identityLinkList) {
            OgPerson person = ogPersonService.selectOgPersonById(id.getUserId());
            if (phone.equals(person.getMobilPhone())) {
                return;
            }
        }
        throw new BusException("100003", "登录用户无权操作流程");
    }

    public List<OgPerson> getOgPerson(Map<String, Object> params, String flag) {
        String value = "";
        boolean temp = false;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().startsWith("businessOrg")) {
                temp = true;
                value = (String) entry.getValue();
                break;
            }
        }

        if (!temp) {
            throw new BusException("100004", "业务部门参数字段不允许为空");
        }
        List<OgPerson> list = null;
        List<PubParaValue> values = getBusinessOrg(value);
        if (!CollectionUtils.isEmpty(values)) {
            String orgName = values.get(0).getValueDetail();
            OgOrg org = deptService.selectDeptByOrgName(orgName);
            if (StringUtils.isNotNull(org)) {
                OgPerson person = new OgPerson();
                person.setOrgId(org.getOrgId());
                if ("leader".equals(flag)) {
                    person.setrId("2303");
                } else if ("business".equals(flag)) {
                    person.setrId("2302");
                }
                list = commonService.selectPersonByOrgAndRole(person);
                if (CollectionUtils.isEmpty(list)) {
                    throw new BusException("该部门下没有业务审核人!");
                }
            } else {
                throw new BusException("所选业务部门参数异常请联系运维管理平台运维，联系方式010-63700357!");
            }
        }
        return list;
    }

    public List<PubParaValue> getBusinessOrg(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("paraName", "vm_dept");
        map.put("paraValue", value);
        return pubParaValueService.selectOrgNameByParaValue(map);
    }

    public List getSubList(List<?> list, Integer pageNum, Integer pageSize) {
        List<?> newList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            if (pageNum * pageSize > list.size()) {
                if (pageSize > list.size()) {
                    newList = list;
                } else {
                    if((pageNum - 1) * pageSize>list.size()){
                        int freeV = list.size() % pageSize;
                        newList = list.subList(list.size() - freeV, list.size());
                    }else{
                        newList = list.subList((pageNum - 1) * pageSize, list.size());
                    }
                }
            } else {
                newList = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
            }
        }
        return newList;
    }

    public static void main(String[] args) throws Exception {
        VmBizInfo vmBizInfo = new VmBizInfo();
        Map variables = getVersionMap();
        ConverterUtils.mapToObject(vmBizInfo, variables);
        vmBizInfo.setVersionInfoId(variables.get("businessKey").toString());
        vmBizInfo.setUseApprovalId(variables.get("useApproval").toString());
        vmBizInfo.setSysId(variables.get("sys.sysid").toString());
        vmBizInfo.setSysName(variables.get("sysname").toString());
        vmBizInfo.getParams().put("insertFlag", "forward");
        System.out.println(vmBizInfo);

    }

    public static Map getVersionMap() {
        Map map = new HashMap();
        map.put("businessKey", "78f3df8b186f471485aac2a911kjba3b");
        map.put("versionInfoNo", "BB20210310000012");
        map.put("sys.sysid", "4028ab525828e42f0158292b7b78002b");
        map.put("ifSafetyAudit", "1");
        map.put("versionAttr", "1");
        map.put("versionType", "2");
        map.put("upgradeEnviponment", "111111111111111");
        map.put("sysname", "代理理财类业务系统,第三方支付系统");
        map.put("versionName", "测试升级内容摘要");
        map.put("businessRequirementText", "111111111111111");
        map.put("versionDescription", "11111111111111111");
        map.put("issuedNo", "11111111111111111");
        map.put("businessValidationText", "1111111111111");
        map.put("questionId", "张三");
        map.put("drafterId", "李四");
        map.put("acceptOrganization", "1111111111111111");
        map.put("creaTteunits", "111111111111111");
        map.put("copyRequiteUnit", "1111111111111111111");
        map.put("noticeText", "111111111111111");
        map.put("ifReportOffice", "2");
        map.put("ifReportCBRC", "2");
        map.put("ifReportCallCenter", "2");
        map.put("ifStopBusiness", "2");
        map.put("issuedBranch", "4028c4852a1ea93b012a21474f86005a,4028c4852a1ea93b012a214b5dbd0061");
        map.put("issuedUnit", "4028c4852a1ea93b012a2147cad2005b");
        map.put("agencyCenter", "40288a14276f110301276f7e5bc80016");
        map.put("startUpgraedTime", "20210304115046");
        map.put("endUpgraedTime", "20210305115046");
        map.put("technicalSupportId", "王五");
        map.put("technicalSupportPhone", "13838384438");
        map.put("ifIssueNotice", "2");
        map.put("businessOrg", "8b8080f34b5a43ed014b7751638259bd,8b8080f34b5a43ed014b77527e5759d1");
        map.put("businessAuditId", "8b8080f34b78982d014b8b008277317c,4028aba3365822fe013686a7635e137a");
        map.put("businessApproval", "8a9a5add57e4f8f40157e511780e0018,8a9a5add57e4f8f40157e50c264d0009");
        map.put("technologyAuditId", "8a9a5add57e4f8f40157e51212ff001a");
        map.put("useApproval", "8a9a5add57e4f8f40157e50fae550014");
        map.put("safetyAuditId", "8a9a5add57e4f8f40157e50a9da40004");
        map.put("fileCiphertext", "111111111111111111111111111111111111111");
        return map;
    }

    // 710003-查询变更请求受理人
    @Override
    @EsbServiceMapping
    public List<Record> getEffectPersonsById(@ServiceParam(name = "implementorOrg.orgid") String orgid)
            throws BusException {
        if (StringUtils.isEmpty(orgid)) {
            throw new BusException("100004", "受理机构ID不能为空");
        }
        OgPerson person = new OgPerson();
        person.setPflag("1");
        person.setOrgId(orgid);
        person.setrId("2503");
        List<OgPerson> ogPersons = commonService.selectPersonByOrgAndRole(person);
        List<Record> ogList = new ArrayList<Record>();
        for (OgPerson ogPerson : ogPersons) {
            Record map = new Record();
            map.put("pid", ogPerson.getpId());
            map.put("pname", ogPerson.getpName());
            ogList.add(map);
        }
        return ogList;
    }

    // 710005查询审批人
    @Override
    @EsbServiceMapping
    public List<Record> getCheckPersonsByAgencyAndRole(@ServiceParam(name = "checkOrg.orgid") String orgid)
            throws BusException {
        if (StringUtils.isEmpty(orgid)) {
            throw new BusException("100004", "审批机构ID不能为空");
        }
        OgPerson person = new OgPerson();
        person.setPflag("1");
        person.setOrgId(orgid);
        person.setrId("2502");
        List<OgPerson> ogPersons = commonService.selectPersonByOrgAndRole(person);
        List<Record> list = new ArrayList<>();
        if (ogPersons.size() > 0) {
            for (OgPerson p : ogPersons) {
                Record map = new Record();
                map.put("pid",p.getpId());
                map.put("pname",p.getpName());
                map.put("agency.orgid",p.getOrgId());
                map.put("birthplace",p.getBirthday());
                map.put("birthday",p.getBirthday());
                map.put("sex",p.getSex());
                map.put("phone",p.getPhone());
                map.put("mobilPhone",p.getMobilPhone());
                map.put("position",p.getPosition());
                map.put("email",p.getEmail());
                map.put("address",p.getAddress());
                map.put("leader",p.getLeader());
                map.put("invalidationMark",p.getInvalidationMark());
                map.put("pFlag",p.getPflag());
                map.put("memo",p.getMemo());
                map.put("order",p.getpOrder());
                map.put("adder",p.getAdder());
                map.put("edu",p.getEdu());
                map.put("updateTime",p.getUpdatetime());
                list.add(map);
            }
        }
        return list;
    }

    // 710006变更单信息提交/重新提交
    @Override
    @EsbServiceMapping
    public void startBGQQ(@ServiceParam(name = "faultNo") String faultNo,
                          @ServiceParam(name = "createOrg.orgid") String createOrgId,
                          @ServiceParam(name = "implementorOrg.orgid") String implementorOrgId,
                          @ServiceParam(name = "implementor.pid") String implementorId,
                          @ServiceParam(name = "changeCategoryId.typeinfoId") String changeCategoryTypeinfoId,
                          @ServiceParam(name = "importantLev") String importantLev, @ServiceParam(name = "sysname") String sysname,
                          @ServiceParam(name = "sysid") String sysid, @ServiceParam(name = "changeStage") String changeStage,
                          @ServiceParam(name = "changeResource") String changeResource,
                          @ServiceParam(name = "isNotice") String isNotice, @ServiceParam(name = "isStop") String isStop,
                          @ServiceParam(name = "changeSingleName") String changeSingleName,
                          @ServiceParam(name = "changeDetails") String changeDetails,
                          @ServiceParam(name = "checkOrg.orgid") String checkOrg, @ServiceParam(name = "checker.pid") String checker,
                          @ServiceParam(name = "fileCiphertext") String fileCiphertext,
                          @ServiceParam(name = "initiator", pubProperty = "userId") String initiator) throws BusException {

        if (StringUtils.isEmpty(faultNo))
            throw new BusException("100004", "变更请求单号不允许为空");
        if (StringUtils.isEmpty(createOrgId))
            throw new BusException("100004", "申请单位不允许为空");
        if (StringUtils.isEmpty(implementorOrgId))
            throw new BusException("100004", "受理单位不允许为空");
        if (StringUtils.isEmpty(implementorId))
            throw new BusException("100004", "受理人不允许为空");
        if (StringUtils.isEmpty(changeCategoryTypeinfoId))
            throw new BusException("100004", "变更分类不允许为空");
        if (StringUtils.isEmpty(importantLev))
            throw new BusException("100004", "风险等级不允许为空");
        if (StringUtils.isEmpty(changeStage))
            throw new BusException("100004", "变更所处阶段不允许为空");
        if (StringUtils.isEmpty(changeResource))
            throw new BusException("100004", "变更请求来源不允许为空");
        if (StringUtils.isEmpty(isNotice))
            throw new BusException("100004", "是否通知业务不允许为空");
        if (StringUtils.isEmpty(isStop))
            throw new BusException("100004", "是否影响业务连续性不允许为空");
        if (StringUtils.isEmpty(changeSingleName))
            throw new BusException("100004", "变更标题不允许为空");
        if (StringUtils.isEmpty(changeDetails))
            throw new BusException("100004", "变更内容不允许为空");
        if (StringUtils.isEmpty(checkOrg))
            throw new BusException("100004", "审批机构不允许为空");
        if (StringUtils.isEmpty(checker))
            throw new BusException("100004", "审批人不允许为空");

        // 受理机构与审批机构不可一致校验
        if (implementorOrgId.equals(checkOrg))
            throw new BusException("100004", "审批机构和受理机构不能一致，如需一致，请直接提交综合变更单");

        if (changeDetails.indexOf("一、变更原因") == -1 || changeDetails.indexOf("二、变更内容") == -1)
            throw new BusException("100004", "不能删除【一、变更原因】【二、变更内容】模块，请补充");

        // 变更分类校验
        OgTypeinfo ogTypeinfo = ogTypeinfoService.selectOgTypeinfoById(changeCategoryTypeinfoId);
        if (ogTypeinfo == null)
            throw new BusException("100004", "变更分类ID传输有误");

        // 应用系统是否可为空校验
        if (!("省中心".equals(ogTypeinfo.getTypeName()) || "基础设施".equals(ogTypeinfo.getTypeName()))
                && (StringUtils.isEmpty(sysname) || StringUtils.isEmpty(sysid)))
            throw new BusException("100004", "所涉应用系统不允许为空");

        // 受理单位与受理人校验
        List<Record> records = this.getEffectPersonsById(implementorOrgId);
        if (CollectionUtils.isEmpty(records))
            throw new BusException("100004", "受理单位数据错误");

        boolean pidBoolIm = false;
        for (Record record : records) {
            if (implementorId.equals(record.get("pid")))
                pidBoolIm = true;
        }

        if (!pidBoolIm)
            throw new BusException("100004", "受理人数据错误");

        // 审批机构与审批人校验
        List<Record> ogPersonsCh = this.getCheckPersonsByAgencyAndRole(checkOrg);
        if (CollectionUtils.isEmpty(ogPersonsCh))
            throw new BusException("100004", "审批机构数据错误");

        boolean pidBoolCh = false;
        for (Record ogPerson : ogPersonsCh) {
            if (checker.equals(ogPerson.get("pid")))
                pidBoolCh = true;
        }

        if (!pidBoolCh)
            throw new BusException("100004", "审批人数据错误");

        String changeId = UUID.getUUIDStr();
        CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
        cmBizQingqiu.setChangeId(changeId);
        cmBizQingqiu.setChangeCode(faultNo);
        cmBizQingqiu.setApplicantId(initiator);
        cmBizQingqiu.setCreaterOrgId(createOrgId);
        cmBizQingqiu.setCheckOrg(implementorOrgId);
        cmBizQingqiu.setCheckerId(implementorId);
        cmBizQingqiu.setChangeCategoryId(changeCategoryTypeinfoId);
        cmBizQingqiu.setImportantLev(importantLev);
        cmBizQingqiu.setSysname(sysname);
        cmBizQingqiu.setSysid(sysid);
        cmBizQingqiu.setChangeStage(changeStage);
        cmBizQingqiu.setChangeResource(changeResource);
        cmBizQingqiu.setIsNotice(isNotice);
        cmBizQingqiu.setIsStop(isStop);
        cmBizQingqiu.setChangeSingleName(changeSingleName);
        cmBizQingqiu.setChangeDetails(changeDetails);
        cmBizQingqiu.setImplementorOrgid(checkOrg);
        cmBizQingqiu.setImplementorId(checker);
        cmBizQingqiu.setInvalidationMark("1");
        cmBizQingqiu.setStauts("0300");
        cmBizQingqiu.setAddTime(DateUtils.handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        cmBizQingqiu.setSubmitTime(DateUtils.handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        cmBizQingqiuService.insertCmBizQingqiu(cmBizQingqiu);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buinesskey", cmBizQingqiu.getChangeId());
        map.put("processDefinitionKey", "BGQQ");
        map.put("CHECKER_ID", checker);
        map.put("dealId", implementorId);
        map.put("creatId", initiator);
        map.put("userId", initiator);
        activitiCommService.startProcess(changeId, "BGQQ", map);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());
        String text = "变更请求单号："+ cmBizQingqiu.getChangeCode() +"，标题："+ cmBizQingqiu.getChangeSingleName() +"需要审批，请登录运维管理平台处理。";
        vmBizInfoService.sendSms(text, ogPerson);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext))
            return;
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), changeId,
                    "cmBizSingleQingQiu");
        }
    }


    // 710007-分管领导审批变更请求
    @Override
    @EsbServiceMapping
    public void getPagerFZApproval(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                                   @ServiceParam(name = "changeId") String changeId,
                                   @ServiceParam(name = "performDescription") String performDescription,
                                   @ServiceParam(name = "name") String name, @ServiceParam(name = "fileCiphertext") String fileCiphertext)
            throws BusException {
        if (StringUtils.isEmpty(name))
            throw new BusException("100004", "审批参数不允许为空");
        if (StringUtils.isEmpty(changeId))
            throw new BusException("100004", "变更请求单ID不允许为空");
        if (StringUtils.isEmpty(performDescription))
            throw new BusException("100004", "审批意见不允许为空");

        String status = "";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("processDefinitionKey", "BGQQ");
        map.put("businessKey", changeId);
        map.put("comment", performDescription);
        map.put("userId", initiator);
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(changeId);
        if ("TG".equals(name)) {// 分管领导审批通过
            map.put("reCode", "0");
            status = "0400";
            if ("1".equals(cmBizQingqiu.getIsNotice())) {
                OgPerson ogPerson1 = ogPersonService.selectOgPersonById(cmBizQingqiu.getCheckerId());
                String text1 = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台受理。";
                vmBizInfoService.sendSms(text1, ogPerson1);
            }
        } else if ("HT".equals(name)) {// 分管领导审批不通过，退回修改
            map.put("reCode", "1");
            status = "0200";
            OgPerson ogPerson = ogPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());
            OgPerson ogPerson1 = ogPersonService.selectOgPersonById(initiator);
            String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"，审批人："+ ogPerson1.getpName() +"，请登录运维管理平台操作。";
            vmBizInfoService.sendSms(text, ogPerson);
        } else
            throw new BusException("100004", "审批参数错误");

        Task task = activitiCommService.getTask(changeId, "分管领导审批变更请求");
        if (task == null)
            throw new BusException("100004", "无法找到流程changeId =" + changeId);
//        this.userFlowLogAuthJudge(task.getId(), initiator);
        activitiCommService.complete(map);

        cmBizQingqiu.setStauts(status);
        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext))
            return;
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), changeId,
                    "cmBizSingleQingQiu");
        }

    }

    // 710008-审批变更请求
    @Override
    @EsbServiceMapping
    public void getPagerApproval(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                                 @ServiceParam(name = "changeId") String changeId, @ServiceParam(name = "flowSelect") String flowSelect,
                                 @ServiceParam(name = "fuchecker") String fuchecker,
                                 @ServiceParam(name = "status") String status,
                                 @ServiceParam(name = "performDescription") String performDescription,
                                 @ServiceParam(name = "name") String name, @ServiceParam(name = "fileCiphertext") String fileCiphertext)
            throws BusException {
        if (StringUtils.isEmpty(name))
            throw new BusException("100004", "审批参数不允许为空");
        if (StringUtils.isEmpty(changeId))
            throw new BusException("100004", "变更请求单ID不允许为空");
        if (StringUtils.isEmpty(flowSelect))
            throw new BusException("100004", "流程选项不允许为空");
        if (StringUtils.isEmpty(fuchecker))
            throw new BusException("100004", "分管领导不允许为空");
        if (StringUtils.isEmpty(performDescription))
            throw new BusException("100004", "审批意见不允许为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessKey", changeId);
        map.put("fucheckerId", fuchecker);
        map.put("comment", performDescription);
        map.put("userId", initiator);
        map.put("processDefinitionKey", "BGQQ");
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(changeId);
        cmBizQingqiu.setFucheckerId(fuchecker);
        if ("TG".equals(name)) {// 通过
            if ("01".equals(flowSelect)) {// 分管领导审批--进入分管领导审核
                map.put("reCode", "0");
                status = "0600";
            } else {
                map.put("reCode", "2");
                status = "0400";
                OgPerson ogPerson = ogPersonService.selectOgPersonById(cmBizQingqiu.getFucheckerId());
                String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台查看。";
                vmBizInfoService.sendSms(text, ogPerson);
                if ("1".equals(cmBizQingqiu.getIsNotice())) {
                    OgPerson ogPerson1 = ogPersonService.selectOgPersonById(cmBizQingqiu.getCheckerId());
                    String text1 = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台受理。";
                    vmBizInfoService.sendSms(text1, ogPerson1);
                }
            }
        } else if ("HT".equals(name)) {// 退回
            map.put("reCode", "1");
            status = "0200";
            OgPerson ogPerson = ogPersonService.selectOgPersonById(cmBizQingqiu.getApplicantId());
            OgPerson ogPerson1 = ogPersonService.selectOgPersonById(initiator);
            String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"，审批人："+ ogPerson1.getpName() +"，请登录运维管理平台操作。";
            vmBizInfoService.sendSms(text, ogPerson);
        } else {
            throw new BusException("100004", "审批参数错误");
        }

        Task task = activitiCommService.getTask(changeId, "审批变更请求");
        if (task == null)
            throw new BusException("100004", "无法找到流程changeId =" + changeId);

//        this.userFlowLogAuthJudge(task.getId(), initiator);
        activitiCommService.complete(map);
        cmBizQingqiu.setStauts(status);
        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext))
            return;
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), changeId,
                    "cmBizSingleQingQiu");
        }
    }

    // 710010-查询用户创建的变更单（过滤掉待提交状态的）
    @Override
    @EsbServiceMapping
    public PagerRecords getPagerMyself(Pager pager, @ServiceParam(name = "changeSingleName") String changeSingleName,
                                       @ServiceParam(name = "faultNo") String changeCode, @ServiceParam(name = "stauts") String stauts,
                                       @ServiceParam(name = "changeCategoryId.typeinfoId") String typeinfoId,
                                       @ServiceParam(name = "changeResource") String changeResource,
                                       @ServiceParam(name = "isNotice") String isNotice,
                                       @ServiceParam(name = "createTimeStart") String createTimeStart,
                                       @ServiceParam(name = "initiator", pubProperty = "userId") String initiator) throws BusException {
        CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
        cmBizQingqiu.setChangeSingleName(changeSingleName);
        cmBizQingqiu.setChangeCode(changeCode);
        cmBizQingqiu.setStauts(stauts);
        cmBizQingqiu.setChangeCategoryId(typeinfoId);
        cmBizQingqiu.setChangeResource(changeResource);
        cmBizQingqiu.setApplicantId(initiator);
        cmBizQingqiu.setIsNotice(isNotice);
        if(StringUtils.isNotEmpty(createTimeStart)){
            cmBizQingqiu.setAddTime(createTimeStart + "000000");
        }
        Page<CmBizQingqiu> cmBizQingqiuPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        //List<CmBizQingqiu> cmBizQingqiuList = cmBizQingqiuService.selectCmBizQingqiuListNotNew(cmBizQingqiu);
        List<CmBizQingqiu> cmBizQingqiuList = cmBizQingqiuService.selectCmBizQingqiuList(cmBizQingqiu);
        List<Record> list = new ArrayList<>();
        for (CmBizQingqiu cmBizQingqiu1 : cmBizQingqiuList ) {
            Record data = new Record();
            data.put("changeId",cmBizQingqiu1.getChangeId());
            data.put("faultNo",cmBizQingqiu1.getChangeCode());
            data.put("createTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getAddTime()));
            data.put("submitTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getSubmitTime()));
            data.put("practicleTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getPracticleTime()));
            data.put("sysid",cmBizQingqiu1.getSysid());
            data.put("sysname",cmBizQingqiu1.getSysname());
            data.put("importantLev",cmBizQingqiu1.getImportantLev());
            data.put("isNotice",cmBizQingqiu1.getIsNotice());
            data.put("isStop",cmBizQingqiu1.getIsStop());
            data.put("changeResource",cmBizQingqiu1.getChangeResource());
            data.put("changeSingleName",cmBizQingqiu1.getChangeSingleName());
            data.put("changeDetails",cmBizQingqiu1.getChangeDetails());
            data.put("changeStage",cmBizQingqiu1.getChangeStage());
            data.put("invalidationMark",cmBizQingqiu1.getInvalidationMark());
            data.put("createrPname",cmBizQingqiu1.getApplyName());
            data.put("typeinfoName",cmBizQingqiu1.getTypeName());
            data.put("stauts",cmBizQingqiu1.getStauts());
            list.add(data);
        }
        PagerRecords pagerRecords = new PagerRecords(list, (int) cmBizQingqiuPage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    //710011-查询用户需要待审批/待分管领导审批的变更单
    @EsbServiceMapping
    public PagerRecords getUserPagerTasksBGQQ(@ServiceParam(name = "userId", pubProperty = "userId") String userId,
                                              @ServiceParam(name = "roleIds", pubProperty = "roleIds") String roleIds, Pager pager,
                                              @ServiceParam(name = "faultNo") String faultNo,
                                              @ServiceParam(name = "changeSingleName") String changeSingleName,
                                              @ServiceParam(name = "stauts") String stauts,
                                              @ServiceParam(name = "pname") String pname,
                                              @ServiceParam(name = "createTime") String createTime,
                                              @ServiceParam(name = "changeCategoryId.typeinfoId") String typeinfoId,
                                              @ServiceParam(name = "name") String name) throws BusException {
        List<CmBizQingqiu> list = new ArrayList<>();
        List<Map<String, Object>> userTask = activitiCommService.userTaskUserId(userId, "BGQQ", "");
        for (Map<String, Object> mp : userTask) {
            String id = mp.get("businesskey") == null ? "" : mp.get("businesskey").toString();
            String taskName = mp.get("taskName").toString();
            CmBizQingqiu cu = new CmBizQingqiu();
            if ("SP".equals(name) && "审批".equals(taskName)) {
                cu.setChangeId(id);
                cu.setChangeSingleName(changeSingleName);
                cu.setChangeCategoryId(typeinfoId);
                cu.setChangeCode(faultNo);
                cu.setAddTime(createTime);
                if (StringUtils.isEmpty(stauts)) {
                    stauts = "0300";
                }
                cu.setStauts(stauts);
                cu = cmBizQingqiuService.selectBGQQVO(cu);
                if (cu != null) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(cu.getApplicantId());
                    cu.setApplicantId(ogPerson.getpName());
                    cu.getParams().put("taskName", taskName);
                    list.add(cu);
                }
            } else if ("FGSP".equals(name) && "分管领导审批".equals(taskName)) {
                cu.setChangeId(id);
                cu.setChangeSingleName(changeSingleName);
                cu.setChangeCategoryId(typeinfoId);
                cu.setChangeCode(faultNo);
                cu.setAddTime(createTime);
                cu.setStauts("0600");
                cu = cmBizQingqiuService.selectBGQQVO(cu);
                if (cu != null) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(cu.getApplicantId());
                    cu.setApplicantId(ogPerson.getpName());
                    cu.getParams().put("taskName", taskName);
                    list.add(cu);
                }
            }
        }

        // 代办列表根据生成时间倒序排序
        if (list.size() > 1) {
            list = list.stream().sorted(Comparator.comparing(CmBizQingqiu::getAddTime).reversed()).collect(Collectors.toList());
        }

        List<Record> recordList = new ArrayList<>();
        for (CmBizQingqiu cmBizQingqiu1 : list ) {
            Record data = new Record();
            data.put("businessKey",cmBizQingqiu1.getChangeId());
            data.put("faultNo",cmBizQingqiu1.getChangeCode());
            data.put("createTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getAddTime()));
            data.put("submitTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getSubmitTime()));
            data.put("practicleTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getPracticleTime()));
            data.put("sysid",cmBizQingqiu1.getSysid());
            data.put("sysname",cmBizQingqiu1.getSysname());
            data.put("importantLev",cmBizQingqiu1.getImportantLev());
            data.put("isNotice",cmBizQingqiu1.getIsNotice());
            data.put("isStop",cmBizQingqiu1.getIsStop());
            data.put("changeResource",cmBizQingqiu1.getChangeResource());
            data.put("changeSingleName",cmBizQingqiu1.getChangeSingleName());
            data.put("changeDetails",cmBizQingqiu1.getChangeDetails());
            data.put("changeStage",cmBizQingqiu1.getChangeStage());
            data.put("invalidationMark",cmBizQingqiu1.getInvalidationMark());
            data.put("createrPname",cmBizQingqiu1.getApplyName());
            data.put("typeinfoName",cmBizQingqiu1.getTypeName());
            data.put("stauts",cmBizQingqiu1.getStauts());
            recordList.add(data);
        }
        List subList = getSubList(recordList, pager.getPageIndex(), pager.getPageSize());
        PagerRecords pagerRecords = new PagerRecords(subList, recordList.size());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    // 710012查询分管领导方法
    @Override
    @EsbServiceMapping
    public List<Map<String, String>> getFuchecker(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator) throws BusException {
        OgPerson person = ogPersonService.selectOgPersonById(initiator);
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        String levelCode = org.getLevelCode();
        String[] state = levelCode.split("/");
        CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
        cmBizQingqiu.setRole("2505");
        cmBizQingqiu.setState(state);
        List<OgPerson> list = cmBizQingqiuService.secondaryDeptLeader(cmBizQingqiu);

        List<Map<String, String>> ogList = new ArrayList<Map<String, String>>();
        for (OgPerson ogPerson : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("fucheckerId", ogPerson.getpId());
            map.put("fuchecker", ogPerson.getpName());
            ogList.add(map);
        }
        return ogList;
    }

    //710013-查询历史变更单
    @Override
    @EsbServiceMapping
    public PagerRecords getPagers(Pager pager, // 分页条件
                                  @ServiceParam(name = "changeSingleName") String changeSingleName,
                                  @ServiceParam(name = "faultNo") String changeCode,
                                  @ServiceParam(name = "stauts") String stauts,
                                  @ServiceParam(name = "changeCategoryId.typeinfoId") String typeinfoId,
                                  @ServiceParam(name = "sysname") String sysname,
                                  @ServiceParam(name = "createOrg.orgid") String createOrg,
                                  @ServiceParam(name = "implementorOrg.orgid") String effectOrg,
                                  @ServiceParam(name = "creater.pname") String creater,
                                  @ServiceParam(name = "createTimeStart") String createTimeStart,
                                  @ServiceParam(name = "createTimeEnd") String createTimeEnd,
                                  @ServiceParam(name = "effctTimeStart") String effctTimeStart,
                                  @ServiceParam(name = "effctTimeEnd") String effctTimeEnd,
                                  @ServiceParam(name = "implementor") String implementor) throws BusException {
        CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
        cmBizQingqiu.setChangeSingleName(changeSingleName);
        cmBizQingqiu.setChangeCode(changeCode);
        cmBizQingqiu.setStauts(stauts);
        cmBizQingqiu.setChangeCategoryId(typeinfoId);
        cmBizQingqiu.setSysname(sysname);
        cmBizQingqiu.setCreaterOrgId(createOrg);
        cmBizQingqiu.setCheckOrg(effectOrg);
        cmBizQingqiu.setApplyName(creater);
        cmBizQingqiu.setImplementorId(implementor);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("createTime", DateUtils.handleTimeYYYYMMDDHHMMSS(createTimeStart));
        map.put("endCreateTime", DateUtils.handleTimeYYYYMMDDHHMMSS(createTimeEnd));
        map.put("practicleTime", DateUtils.handleTimeYYYYMMDDHHMMSS(effctTimeStart));
        map.put("endpracticleTime", DateUtils.handleTimeYYYYMMDDHHMMSS(effctTimeEnd));
        cmBizQingqiu.setParams(map);

        Page<CmBizQingqiu> cmBizQingqiuPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<CmBizQingqiu> cmBizQingqiuList = cmBizQingqiuService.selectCmBizQingqiuList(cmBizQingqiu);
        List<Record> list = new ArrayList<>();
        for (CmBizQingqiu cmBizQingqiu1 : cmBizQingqiuList ) {
            Record data = new Record();
            data.put("changeId",cmBizQingqiu1.getChangeId());
            data.put("faultNo",cmBizQingqiu1.getChangeCode());
            data.put("createTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getAddTime()));
            data.put("submitTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getSubmitTime()));
            data.put("practicleTime",DateUtils.handleTimeYYYYMMDDHHMMSS(cmBizQingqiu1.getPracticleTime()));
            data.put("sysid",cmBizQingqiu1.getSysid());
            data.put("sysname",cmBizQingqiu1.getSysname());
            data.put("importantLev",cmBizQingqiu1.getImportantLev());
            data.put("isNotice",cmBizQingqiu1.getIsNotice());
            data.put("isStop",cmBizQingqiu1.getIsStop());
            data.put("changeResource",cmBizQingqiu1.getChangeResource());
            data.put("changeSingleName",cmBizQingqiu1.getChangeSingleName());
            data.put("changeDetails",cmBizQingqiu1.getChangeDetails());
            data.put("changeStage",cmBizQingqiu1.getChangeStage());
            data.put("invalidationMark",cmBizQingqiu1.getInvalidationMark());
            data.put("createrPname",cmBizQingqiu1.getApplyName());
            data.put("typeinfoName",cmBizQingqiu1.getTypeName());
            data.put("stauts",cmBizQingqiu1.getStauts());
            list.add(data);
        }
        PagerRecords pagerRecords = new PagerRecords(list, (int) cmBizQingqiuPage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }

    // 710014-变更单详情信息查询
    @Override
    @EsbServiceMapping
    public Record getCmBizSingleQingQiu(@ServiceParam(name = "changeId") String id) throws BusException {

        if (StringUtils.isEmpty(id))
            throw new BusException("100004", "changeId不允许为空");

        CmBizQingqiu cmBizQingqiu = null;
        try {
            cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(id);
        } catch (ObjectRetrievalFailureException e) {
            throw new BusException("100004", "查询无数据，changeId不存在");
        }

        if (cmBizQingqiu == null)
            return null;

        // 返回数据
        Record map = new Record();

        map.put("changeId", id);// 变更请求单id
        map.put("faultNo", cmBizQingqiu.getChangeCode());// 变更请求单号
        if (cmBizQingqiu.getCreaterOrgId() != null) {
            map.put("createOrg", cmBizQingqiu.getApplyOrgName());// 申请单位
            map.put("createOrgId", cmBizQingqiu.getCreaterOrgId());// 申请单位id
        }
        if (cmBizQingqiu.getCheckOrg() != null) {
            map.put("implementorOrg", cmBizQingqiu.getAcceptanceOrgName());// 受理单位
            map.put("implementorOrgId", cmBizQingqiu.getCheckOrg());// 受理单位id
        }
        if (cmBizQingqiu.getCheckerId() != null) {
            map.put("implementor", cmBizQingqiu.getAcceptanceName());// 受理人
            map.put("implementorId", cmBizQingqiu.getCheckerId());// 受理人id
        }
        if (cmBizQingqiu.getChangeCategoryId() != null) {
            map.put("changeCategory", cmBizQingqiu.getTypeName());// 变更分类
            map.put("changeCategoryId", cmBizQingqiu.getChangeCategoryId());// 变更分类id
        }

        map.put("importantLev", cmBizQingqiu.getImportantLev());// 变更风险等级
        map.put("sysid", cmBizQingqiu.getSysid());// 所涉应用系统ID
        map.put("sysname", cmBizQingqiu.getSysname());// 所涉应用系统
        map.put("changeStage", cmBizQingqiu.getChangeStage());// 变更所处阶段
        map.put("changeResource", cmBizQingqiu.getChangeResource());// 变更请求来源
        map.put("isNotice", cmBizQingqiu.getIsNotice());// 是否通知业务
        map.put("isStop", cmBizQingqiu.getIsStop());// 是否影响业务连续性
        map.put("changeSingleName", cmBizQingqiu.getChangeSingleName());// 变更标题
        map.put("changeDetails", cmBizQingqiu.getChangeDetails());// 变更内容
        if (cmBizQingqiu.getImplementorOrgid() != null) {
            map.put("checkOrg", cmBizQingqiu.getCheckOrgName());// 审批机构
            map.put("checkOrgId", cmBizQingqiu.getImplementorOrgid());// 审批机构id
        }
        if (cmBizQingqiu.getImplementorId() != null) {
            map.put("checker", cmBizQingqiu.getCheckName());// 审批人
            map.put("checkerId", cmBizQingqiu.getImplementorId());// 审批人id
        }
        return map;
    }

    //710015重新提交
    @Override
    @EsbServiceMapping
    public void alterBGQQ(@ServiceParam(name = "businessKey") String businessKey,
                          @ServiceParam(name = "createOrg.orgid") String createOrgId,
                          @ServiceParam(name = "implementorOrg.orgid") String implementorOrgId,
                          @ServiceParam(name = "implementor.pid") String implementorId,
                          @ServiceParam(name = "changeCategoryId.typeinfoId") String changeCategoryTypeinfoId,
                          @ServiceParam(name = "importantLev") String importantLev,
                          @ServiceParam(name = "sysname") String sysname,
                          @ServiceParam(name = "sysid") String sysid,
                          @ServiceParam(name = "changeStage") String changeStage,
                          @ServiceParam(name = "changeResource") String changeResource,
                          @ServiceParam(name = "isNotice") String isNotice,
                          @ServiceParam(name = "isStop") String isStop,
                          @ServiceParam(name = "changeSingleName") String changeSingleName,
                          @ServiceParam(name = "changeDetails") String changeDetails,
                          @ServiceParam(name = "checkOrg.orgid") String checkOrg,
                          @ServiceParam(name = "checker.pid") String checker,
                          @ServiceParam(name = "fileCiphertext") String fileCiphertext,
                          @ServiceParam(name = "initiator", pubProperty = "userId") String initiator) throws BusException {

        if (StringUtils.isEmpty(businessKey))
            throw new BusException("100004", "变更单ID不允许为空");
        if (StringUtils.isEmpty(createOrgId))
            throw new BusException("100004", "申请单位不允许为空");
        if (StringUtils.isEmpty(implementorOrgId))
            throw new BusException("100004", "受理单位不允许为空");
        if (StringUtils.isEmpty(implementorId))
            throw new BusException("100004", "受理人不允许为空");
        if (StringUtils.isEmpty(changeCategoryTypeinfoId))
            throw new BusException("100004", "变更分类不允许为空");
        if (StringUtils.isEmpty(importantLev))
            throw new BusException("100004", "风险等级不允许为空");
        if (StringUtils.isEmpty(changeStage))
            throw new BusException("100004", "变更所处阶段不允许为空");
        if (StringUtils.isEmpty(changeResource))
            throw new BusException("100004", "变更请求来源不允许为空");
        if (StringUtils.isEmpty(isNotice))
            throw new BusException("100004", "是否通知业务不允许为空");
        if (StringUtils.isEmpty(isStop))
            throw new BusException("100004", "是否影响业务连续性不允许为空");
        if (StringUtils.isEmpty(changeSingleName))
            throw new BusException("100004", "变更标题不允许为空");
        if (StringUtils.isEmpty(changeDetails))
            throw new BusException("100004", "变更内容不允许为空");
        if (StringUtils.isEmpty(checkOrg))
            throw new BusException("100004", "审批机构不允许为空");
        if (StringUtils.isEmpty(checker))
            throw new BusException("100004", "审批人不允许为空");

        // 受理机构与审批机构不可一致校验
        if (implementorOrgId.equals(checkOrg))
            throw new BusException("100004", "审批机构和受理机构不能一致，如需一致，请直接提交综合变更单");

        // 变更内容校验
        if (changeDetails.indexOf("一、变更原因") == -1 || changeDetails.indexOf("二、变更内容") == -1)
            throw new BusException("100004", "不能删除【一、变更原因】【二、变更内容】模块，请补充");

        // 变更分类校验
        OgTypeinfo ogTypeinfo = ogTypeinfoService.selectOgTypeinfoById(changeCategoryTypeinfoId);
        if (ogTypeinfo == null)
            throw new BusException("100004", "变更分类ID传输有误");

        // 应用系统是否可为空校验
        if (!("省中心".equals(ogTypeinfo.getTypeName()) || "基础设施".equals(ogTypeinfo.getTypeName()))
                && (StringUtils.isEmpty(sysname) || StringUtils.isEmpty(sysid)))
            throw new BusException("100004", "所涉应用系统不允许为空");

        // 受理单位与受理人校验
        List<Record> records = this.getEffectPersonsById(implementorOrgId);
        if (CollectionUtils.isEmpty(records))
            throw new BusException("100004", "受理单位数据错误");

        boolean pidBoolIm = false;
        for (Record record : records) {
            if (implementorId.equals(record.get("pid")))
                pidBoolIm = true;
        }

        if (!pidBoolIm)
            throw new BusException("100004", "受理人数据错误");

        // 审批机构与审批人校验
        List<Record> ogPersonsCh = this.getCheckPersonsByAgencyAndRole(checkOrg);
        if (CollectionUtils.isEmpty(ogPersonsCh))
            throw new BusException("100004", "审批机构数据错误");

        boolean pidBoolCh = false;
        for (Record ogPerson : ogPersonsCh) {
            if (checker.equals(ogPerson.get("pid")))
                pidBoolCh = true;
        }

        if (!pidBoolCh)
            throw new BusException("100004", "审批人数据错误");
        CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
        cmBizQingqiu.setChangeId(businessKey);
        cmBizQingqiu.setCreaterOrgId(createOrgId);
        cmBizQingqiu.setCheckOrg(implementorOrgId);
        cmBizQingqiu.setCheckerId(implementorId);
        cmBizQingqiu.setChangeCategoryId(changeCategoryTypeinfoId);
        cmBizQingqiu.setImportantLev(importantLev);
        cmBizQingqiu.setSysname(sysname);
        cmBizQingqiu.setSysid(sysid);
        cmBizQingqiu.setChangeStage(changeStage);
        cmBizQingqiu.setChangeResource(changeResource);
        cmBizQingqiu.setIsNotice(isNotice);
        cmBizQingqiu.setIsStop(isStop);
        cmBizQingqiu.setChangeSingleName(changeSingleName);
        cmBizQingqiu.setChangeDetails(changeDetails);
        cmBizQingqiu.setImplementorOrgid(checkOrg);
        cmBizQingqiu.setImplementorId(checker);
        cmBizQingqiu.setStauts("0300");
        cmBizQingqiu.setSubmitTime(DateUtils.handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("processDefinitionKey", "BGQQ");
        map.put("businessKey", businessKey);
        map.put("comment", "已重新提交，请等待审批.");
        map.put("CHECKER_ID", checker);
        map.put("dealId", implementorId);
        map.put("userId", initiator);
//        Task task = activitiCommService.getTask(businessKey, "修改变更请求");
//        if (task == null)
//            throw new BusException("100004", "无法找到流程changeId =" + businessKey);
//        this.userFlowLogAuthJudge(task.getId(), initiator);
        activitiCommService.complete(map);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());
        String text = "变更请求单号："+ cmBizQingqiu.getChangeCode() +"，标题："+ cmBizQingqiu.getChangeSingleName() +"需要审批，请登录运维管理平台处理。";
        vmBizInfoService.sendSms(text, ogPerson);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext))
            return;
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), businessKey,
                    "cmBizSingleQingQiu");
        }
    }

    //710016-流程记录列表中超链接页面--基本信息
    @Override
    @EsbServiceMapping
    public PubFlowLog getFlowLog(@ServiceParam(name = "logId") String id) throws BusException {
        PubFlowLog pubFlowLog = pubFlowLogService.selectPubFlowLogById(id);
        pubFlowLog.setNextPreformerDesc(convertNextPreformerDesc(pubFlowLog));
        return pubFlowLog;
    }

    //710017-流程记录列表中超链接页面--下一步操作
    @Override
    @EsbServiceMapping
    public List<IdentityLink> getFlowAssignees(@ServiceParam(name = "taskId") String taskId) {
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        return identityLinkList;
    }

    //710018变更单作废
    @Override
    @EsbServiceMapping
    public CmBizQingqiu removeChange(@ServiceParam(name = "changeId") String id) throws BusException {

        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(id);
        if (cmBizQingqiu == null) {
            throw new BusException("100004", "未找到该变更单，请重新选择");
        }
        // 判断是什么状态的 非待提交 、退回待修改状态的报错
        if ("0100".equals(cmBizQingqiu.getStauts()) || "0200".equals(cmBizQingqiu.getStauts())) {
            if ("0200".equals(cmBizQingqiu.getStauts())) {
                endTaskAdapterImplBGQQ.close(id);
            }
            cmBizQingqiuService.cmBizQingQiuToCancle(id);
            return cmBizQingqiu;
        } else {
            throw new BusException("100004", "只能作废退回待修改状态的请求单，请重新选择");
        }
    }

    /**
     * 查询问题单810001
     * @param pager
     * @param issuefxNo
     * @param issuefxName
     * @param issueSource
     * @param issueFenLei
     * @param issueType
     * @param issueOrg
     * @param createTimeStart
     * @param createTimeEnd
     * @param state
     * @param create
     * @param createOrg
     * @param reviewer
     * @param lev
     * @param sysName
     * @param technologyAuditId
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public PagerRecords  getPagerIssueFx(Pager pager,
                                         @ServiceParam(name = "issuefxNo") String issuefxNo,
                                         @ServiceParam(name = "issuefxName") String issuefxName,
                                         @ServiceParam(name = "issueSource") String issueSource,
                                         @ServiceParam(name = "issueFenLei") String issueFenLei,
                                         @ServiceParam(name = "issueType") String issueType,
                                         @ServiceParam(name = "issueOrg") String issueOrg,
                                         @ServiceParam(name = "createTimeStart") String createTimeStart,
                                         @ServiceParam(name = "createTimeEnd") String createTimeEnd,
                                         @ServiceParam(name = "state") String state,
                                         @ServiceParam(name = "create") String create,
                                         @ServiceParam(name = "createOrg") String createOrg,
                                         @ServiceParam(name = "reviewer") String reviewer,
                                         @ServiceParam(name = "lev") String lev,
                                         @ServiceParam(name = "sysName") String sysName,
                                         @ServiceParam(name = "technologyAuditId") String technologyAuditId
    ) throws BusException{
        log.debug("===========软研问题单列表查询开始-810001========");
        ImBizIssuefx issue=new ImBizIssuefx();
        issue.setIssuefxNo(issuefxNo);
        issue.setIssuefxName(issuefxName);
        issue.setIssuesource(issueSource);
        issue.setIssueFenlei(issueFenLei);
        issue.setIssueType(issueType);
        issue.setIssueOrg(issueOrg);
        issue.getParams().put("startTime",createTimeStart);
        issue.getParams().put("endTime",createTimeEnd);
        ///sate  currentState
        if (StringUtils.isNotEmpty(state)) {
            String[] currentState = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(currentState).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        issue.setCreaterId(create);
        issue.setCreateOrg(createOrg);
        issue.setReviewerId(reviewer);
        issue.setSeriousLev(lev);
        issue.setSysName(sysName);
        issue.setAuditId(technologyAuditId);
        List<ImBizIssuefx> issuefxList= issueService.selectIssueList(issue);
        List<Record> recordList = new ArrayList<Record>();
        for(ImBizIssuefx issueFx:issuefxList){
            Record record = new Record();
            record.put("issueFxId", issueFx.getIssuefxId());//问题单id
            record.put("issuefxNo", issueFx.getIssuefxNo());//问题单号
            record.put("issuefxName", issueFx.getIssuefxName());//问题标题
            record.put("sysName", issueFx.getSysName());//涉及系统
            if (issueFx.getIssueOrg() != null) {
                record.put("issueOrgName", issueFx.getIssueOrgName());//提出机构
            }
            record.put("issueFenLei", issueFx.getIssueFenlei());//分类
            record.put("issueType", issueFx.getIssueType());//类型
            record.put("issueSource", issueFx.getIssuesource());//来源
            if (issueFx.getCreateOrg() != null) {
                record.put("createOrgName", issueFx.getIssueOrgName());//创建机构
            }
            record.put("createTime", issueFx.getCreateTime());//创建日期
            record.put("state", issueFx.getCurrentState());//状态
            record.put("lev", issueFx.getSeriousLev());//等级
            record.put("issuefxText", issueFx.getIssuefxText());//问题描述
            record.put("issuefxScheme", issueFx.getIssuefxScheme());//初步分析
            record.put("planDesc", issueFx.getPlanDesc());//计划方案
            record.put("dealDesc", issueFx.getDealDetail());//解决情况
            if(issueFx.getAuditId() !=null){
                OgPerson person=ogPersonService.selectOgPersonById(issueFx.getAuditId());
                record.put("technologyAuditPname", person.getpName());//技术经理
            }
            record.put("distributeTime", issueFx.getDistributeTime());//分发时间
            record.put("acceptTime", issueFx.getAcceptTime());//受理时间
            record.put("preSolutionTime", issueFx.getPreSolutionTime());//预解决时间
            record.put("solutionTime", issueFx.getSolutionTime());//解决时间
            recordList.add(record);
        }
        Page<ImBizIssuefx> ImBizIssuefxPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        PagerRecords pagerRecords = new PagerRecords(recordList, (int) ImBizIssuefxPage.getTotal());
        pagerRecords.setPager(pager);
        log.debug(pagerRecords.toString());
        log.debug("===========软研问题单列表查询结束-810001========");
        return pagerRecords;
    }


    /**
     * 810003-查询问题单详情信息
     * @param issuefxId
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public Record getIssuefx(@ServiceParam(name = "issuefxId") String issuefxId) throws BusException {
        log.debug("=========软研查询问题单详情信息开始-810003===========");
        if (StringUtils.isEmpty(issuefxId)) {
            throw new BusException("100004", "issuefxId不允许为空");
        }
        ImBizIssuefx issueFx=issueService.selectImBizIssuefxById(issuefxId);
        if(issueFx==null){
            throw new BusException("100004", "查询无数据，issuefxId不存在");
        }
        Record map = new Record();
        map.put("issueFxId", issueFx.getIssuefxId());//问题单id
        map.put("issuefxNo", issueFx.getIssuefxNo());//问题编号
        map.put("issueSource", issueFx.getIssuesource());//来源
        map.put("state", issueFx.getCurrentState());//状态
        OgPerson createperson=ogPersonService.selectOgPersonById(issueFx.getCreaterId());
        map.put("createPname",createperson.getpName());//创建人
        if (issueFx.getIssueOrg() !=null) {
            map.put("issueOrgName", issueFx.getIssueOrgName());//所在机构
        }
        map.put("createTime", issueFx.getCreateTime());//创建时间
        map.put("issueFenLei", issueFx.getIssueFenlei());//问题分类
        map.put("issueType", issueFx.getIssueType());//问题类型
        map.put("sysName", issueFx.getSysName());//涉及系统
        map.put("perortTime", issueFx.getReporttime());//问题发现时间
        map.put("perortName", createperson.getpName());//问题发现人
        map.put("perortPhone",issueFx.getReportphone());//联系方式
        map.put("lev", issueFx.getSeriousLev());//问题等级
        map.put("issuefxName", issueFx.getIssuefxName());//问题标题
        map.put("issuefxText", issueFx.getIssuefxText());//问题描述
        map.put("issuefxImpact", issueFx.getIssuefxImpact());//影响描述
        map.put("issuefxScheme", issueFx.getIssuefxScheme());//处理建议
        map.put("expectTime", issueFx.getExpectTime());//计划处理时间
        map.put("realityTime", issueFx.getRealityTime());//实际处理时间
        map.put("fuNo", issueFx.getFuNo());//关联变更单或者版本单号
        map.put("issueYFenlei", issueFx.getIssueFenlei());//问题原因分类
        map.put("solvStatus", issueFx.getSolvStatus());//验证解决状态
        map.put("papDesc", issueFx.getPapDesc());//初步分析方案
        map.put("planDesc", issueFx.getPlanDesc());//计划解决方案
        map.put("dealDesc", issueFx.getDealDetail());//解决情况
        if (issueFx.getAuditId() !=null) {
            OgPerson auditPeoson=ogPersonService.selectOgPersonById(issueFx.getAuditId());
            map.put("technologyAuditPname", auditPeoson.getpName());//技术经理
        }
        map.put("businessOrg", issueFx.getBusinessOrg());//业务部门
        map.put("businessAudit", issueFx.getBusinessId());//业务经理
        log.debug(map.toString());
        log.debug("=========软研查询问题单详情信息结束-810003===========");
        return map;
    }
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
    @Override
    @EsbServiceMapping
    public PagerRecords getDealIssueFx(@ServiceParam(name = "userId", pubProperty = "userId") String userId,
                                       @ServiceParam(name = "roleIds", pubProperty = "roleIds") String roleIds, Pager pager, // 分页条件
                                       @ServiceParam(name = "issuefxNo") String issuefxNo,
                                       @ServiceParam(name = "issuefxName") String issuefxName,
                                       @ServiceParam(name = "issueFenLei") String issueFenLei,
                                       @ServiceParam(name = "issueType") String issueType,
                                       @ServiceParam(name = "sysName") String sysName,
                                       @ServiceParam(name = "createTime") String createTime,
                                       @ServiceParam(name = "state") String state,
                                       @ServiceParam(name = "name") String name) throws BusException {
        log.debug("================810009-查询待技术受理/待业务受理的问题单查询开始====================");
        ImBizIssuefx imBizIssuefx=new ImBizIssuefx();
        if (StringUtils.isNotEmpty(name)) {
            if ("WTSL".equals(name)) {
                //待技术受理
                name = "5";
                imBizIssuefx.setAuditId(userId);
            }
            else if ("YWSL".equals(name)) {
                //待业务受理
                name = "7";
                imBizIssuefx.setBusinessId(userId);
            }
            else {
                throw new BusException("100004", "受理标识错误");
            }
        }else
            throw new BusException("100004", "受理标识不能为空");

        if (StringUtils.isNotEmpty(issuefxNo))
            imBizIssuefx.setIssuefxNo(issuefxNo);
        if (StringUtils.isNotEmpty(issuefxName))
            imBizIssuefx.setIssuefxName(issuefxName);
        if (StringUtils.isNotEmpty(issueFenLei))
            imBizIssuefx.setIssueFenlei(issueFenLei);
        if (StringUtils.isNotEmpty(issueType))
            imBizIssuefx.setIssueType(issueType);
        if (StringUtils.isNotEmpty(sysName))
            imBizIssuefx.setSysName(sysName);
        if (StringUtils.isNotEmpty(createTime))
            imBizIssuefx.setCreatTime(createTime);
        imBizIssuefx.setCurrentState(name);
        List<ImBizIssuefx> imBizIssuefxList=issueService.selectIssueList(imBizIssuefx);
        List<Record> recordList = new ArrayList<Record>();
        for (ImBizIssuefx imfx : imBizIssuefxList) {
            Record record = new Record();
            record.put("issueFxId", imfx.getIssuefxId());
            record.put("issuefxNo", imfx.getIssuefxNo());
            record.put("issuefxName", imfx.getIssuefxName());
            record.put("sysName", imfx.getSysName());
            record.put("issueFenLei", imfx.getIssueFenlei());
            record.put("issueType", imfx.getIssueType());
            record.put("issueSource",imfx.getIssuesource());
            record.put("createIdPname", imfx.getCreaterId());
            record.put("createTime", imfx.getCreatTime());
            record.put("state", imfx.getCurrentState());
            recordList.add(record);
        }
        Page<ImBizIssuefx> ImBizIssuefxPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        PagerRecords pagerRecords = new PagerRecords(recordList, (int) ImBizIssuefxPage.getTotal());
        pagerRecords.setPager(pager);
        log.debug(pagerRecords.toString());
        log.debug("================810009-查询待技术受理/待业务受理的问题单查询结束====================");
        return pagerRecords;
    }


    /**
     * 810010--技术受理通过
     * @param initiator
     * @param issueFxId
     * @param papDesc
     * @param fileCiphertext
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public void saveFlowDeal1(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                              @ServiceParam(name = "issueFxId") String issueFxId, @ServiceParam(name = "papDesc") String papDesc, @ServiceParam(name = "fileCiphertext") String fileCiphertext) throws BusException {
        log.debug("========================810010--技术受理通过接口开始==========================");
        if (StringUtils.isEmpty(issueFxId))
            throw new BusException("100004", "问题单ID不能为空");
        if (StringUtils.isEmpty(papDesc))
            throw new BusException("100004", "初步分析方案不能为空");

        Task task=activitiCommService.getTask(issueFxId,"shouli");

        if (task == null)
            throw new BusException("100004", "问题单ID错误，无法找到流程 issueFxId=" + issueFxId);

        this.userFlowLogAuthJudge(task.getId(),initiator);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("businessKey",issueFxId);
        variables.put("comment",papDesc);
        variables.put("reCode", "0");
        variables.put("userId", initiator);
        variables.put("taskId", task.getId());
        activitiCommService.complete(variables);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext)){
            log.debug("========================810010--技术受理通过接口结束无附件==========================");
            return;
        }
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i],  ogPersonService.selectOgPersonById(initiator), issueFxId,
                    "issueFx");
        }
        log.debug("========================810010--技术受理通过接口结束附件上传完成==========================");
    }


    /**
     *   810011--技术受理未通过---退回修改
     * @param initiator
     * @param issueFxId
     * @param performDesc
     * @param fileCiphertext
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public void saveFlowEdit2(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                              @ServiceParam(name = "issueFxId") String issueFxId, @ServiceParam(name = "s-LOG_performDesc") String performDesc,
                              @ServiceParam(name = "fileCiphertext") String fileCiphertext) throws BusException {
        log.debug("========================810011--技术受理退回修改接口开始==========================");
        if (StringUtils.isEmpty(issueFxId))
            throw new BusException("100004", "问题单ID不能为空");
        if (StringUtils.isEmpty(performDesc))
            throw new BusException("100004", "初步分析方案不能为空");

        Task task=activitiCommService.getTask(issueFxId,"shouli");

        if (task == null)
            throw new BusException("100004", "问题单ID错误，无法找到流程 issueFxId=" + issueFxId);

        this.userFlowLogAuthJudge(task.getId(),initiator);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("businessKey",issueFxId);
        variables.put("comment",performDesc);
        variables.put("reCode", "3");
        variables.put("userId", initiator);
        variables.put("taskId", task.getId());
        activitiCommService.complete(variables);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext)){
            log.debug("========================810011--技术受理退回修改接口结束无附件==========================");
            return;
        }
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i],  ogPersonService.selectOgPersonById(initiator), issueFxId,
                    "issueFx");
        }
        log.debug("========================810011--技术受理退回修改接口结束附件上传完成==========================");
    }
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
    @Override
    @EsbServiceMapping
    public void saveFlowRepeat(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                               @ServiceParam(name = "issueFxId") String issueFxId,
                               @ServiceParam(name = "sysName") String sysName,
                               @ServiceParam(name = "technologyAuditId") String technologyAuditId,
                               @ServiceParam(name = "dddesc") String dddesc,
                               @ServiceParam(name = "fileCiphertext") String fileCiphertext) throws BusException {
        log.debug("========================810012--技术受理转技术接口开始=========================");
        if (StringUtils.isEmpty(issueFxId))
            throw new BusException("100004", "问题单ID不能为空");
        if (StringUtils.isEmpty(sysName))
            throw new BusException("100004", "所属应用系统不能为空");
        if (StringUtils.isEmpty(technologyAuditId))
            throw new BusException("100004", "技术经理不能为空");
        if (StringUtils.isEmpty(dddesc))
            throw new BusException("100004", "操作意见不能为空");
        ImBizIssuefx imBizIssuefx= issueService.selectImBizIssuefxById(issueFxId);
        List<PubParaValue> pvList= iPubParaValueService.selectPubParaValueByParaName("WT_MULTICOUNT");
        //已转发次数
        long num=imBizIssuefx.getMulticount();
        //最大转发次数
        long maxNum=Long.valueOf(pvList.get(0).getValue());
        if(num>=maxNum){
            throw new BusException("转发次数已达"+num+"次！无法转发！");
        }else {
            num+=1;
        }
        Task task=activitiCommService.getTask(issueFxId,"chuli");

        if (task == null)
            throw new BusException("100004", "问题单ID错误，无法找到流程 issueFxId=" + issueFxId);

        this.userFlowLogAuthJudge(task.getId(),initiator);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("businessKey",issueFxId);
        variables.put("comment",dddesc);
        variables.put("reCode", "1");
        variables.put("userId", initiator);
        variables.put("taskId", task.getId());
        variables.put("auditId",technologyAuditId);
        imBizIssuefx.setAuditId(technologyAuditId);
        imBizIssuefx.setSysName(sysName);
        issueService.updateIssue(imBizIssuefx);
        activitiCommService.complete(variables);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext)) {
            log.debug("========================810012--技术受理转技术接口结束无附件=========================");
            return;
        }
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), issueFxId, "issueFx");
        }
        log.debug("========================810012--技术受理转技术接口结束有附件=========================");
    }
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
    @Override
    @EsbServiceMapping
    public void saveFlowRepeatYw(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                                 @ServiceParam(name = "issueFxId") String issueFxId, @ServiceParam(name = "businessOrg0") String businessOrg0,
                                 @ServiceParam(name = "bussId") String bussId, @ServiceParam(name = "s-LOG_performDesc") String performDesc,@ServiceParam(name = "fileCiphertext") String fileCiphertext) throws BusException {
        log.debug("========================810013--技术受理转业务接口开始=========================");
        if (StringUtils.isEmpty(issueFxId))
            throw new BusException("100004", "问题单ID不能为空");
        if (StringUtils.isEmpty(businessOrg0))
            throw new BusException("100004", "业务部门不能为空");
        if (StringUtils.isEmpty(bussId))
            throw new BusException("100004", "业务经理不能为空");
        if (StringUtils.isEmpty(performDesc))
            throw new BusException("100004", "操作意见不能为空");

        ImBizIssuefx imBizIssuefx= issueService.selectImBizIssuefxById(issueFxId);
        List<PubParaValue> pvList= iPubParaValueService.selectPubParaValueByParaName("WT_MULTICOUNT");
        //已转发次数
        long num=imBizIssuefx.getMulticount();
        //最大转发次数
        long maxNum=Long.valueOf(pvList.get(0).getValue());
        if(num>=maxNum){
            throw new BusException("转发次数已达"+num+"次！无法转发！");
        }else {
            num+=1;
        }
        Task task=activitiCommService.getTask(issueFxId,"chuli");

        if (task == null)
            throw new BusException("100004", "问题单ID错误，无法找到流程 issueFxId=" + issueFxId);

        this.userFlowLogAuthJudge(task.getId(),initiator);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("businessKey",issueFxId);
        variables.put("comment",performDesc);
        variables.put("reCode", "2");
        variables.put("userId", initiator);
        variables.put("taskId", task.getId());
        variables.put("busId",bussId);
        imBizIssuefx.setBusinessId(bussId);
        imBizIssuefx.setBusinessOrg(businessOrg0);
        issueService.updateIssue(imBizIssuefx);
        activitiCommService.complete(variables);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext)) {
            log.debug("========================810012--技术受理转业务接口结束无附件=========================");
            return;
        }
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), issueFxId, "issueFx");
        }
        log.debug("========================810012--技术受理转业务接口结束有附件=========================");
    }
    /**
     *   810014--业务受理通过
     * @param initiator
     * @param issueFxId
     * @param performDesc
     * @param fileCiphertext
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public void saveFlowBuss(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator,
                             @ServiceParam(name = "issueFxId") String issueFxId, @ServiceParam(name = "s-LOG_performDesc") String performDesc,@ServiceParam(name = "fileCiphertext") String fileCiphertext) throws BusException {

        if (StringUtils.isEmpty(issueFxId))
            throw new BusException("100004", "问题单ID不能为空");
        if (StringUtils.isEmpty(performDesc))
            throw new BusException("100004", "处理意见不能为空");
        if (StringUtils.isEmpty(initiator))
            throw new BusException("100004", "处理人Id不能为空");

        Task task=activitiCommService.getTask(issueFxId,"ywshouli");

        if (task == null)
            throw new BusException("100004", "问题单ID错误，无法找到流程 issueFxId=" + issueFxId);

        this.userFlowLogAuthJudge(task.getId(),initiator);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("businessKey",issueFxId);
        variables.put("comment",performDesc);
        variables.put("description","ywshouli");
        variables.put("processDefinitionKey","PMYY");
        variables.put("taskId",task.getId());
        variables.put("userId",initiator);
        activitiCommService.usersComplete(variables);
        // 流程执行包含关联附件(附件多个)
        if (StringUtils.isEmpty(fileCiphertext))
            return;
        String[] str = fileCiphertext.split(",");
        for (int i = 0; i < str.length; i++) {
            forwardFileService.saveForwardFile(str[i], ogPersonService.selectOgPersonById(initiator), issueFxId, "issueFx");
        }
    }
    /**
     *  810016--查询所属应用系统
     * @param pager
     * @param caption
     * @param orgname
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getPagerOgSyssByRestrict(Pager pager,@ServiceParam(name = "caption") String caption,
                                                 @ServiceParam(name = "agency.orgname") String orgname) throws BusException {
        log.debug("======================810016--查询所属应用系统开始==================");
        OgSys ogSys=new OgSys();
        ogSys.setCaption(caption);
        ogSys.setOrgName(orgname);
        List<OgSys> list = applicationManagerService.selectOgSysList(ogSys);
        List<Record> recordList = new ArrayList<Record>();
        for (OgSys oss : list) {
            Record record = new Record();
            record.put("sysid", oss.getSysId());//系统ID
            record.put("code", oss.getCode());// 系统编码
            record.put("caption", oss.getCaption());// 系统名称
            if (oss.getOrgName() !=null) {
                record.put("agencyOrgname", oss.getOrgName());// 所属机构
            }
            record.put("memo", oss.getMemo());// 备注
            record.put("invalidationMark", oss.getInvalidationMark());// 状态
            recordList.add(record);
        }
        Page<OgSys> OgSysPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        PagerRecords pagerRecords = new PagerRecords(recordList, (int) OgSysPage.getTotal());
        pagerRecords.setPager(pager);
        log.debug(pagerRecords.toString());
        log.debug("======================810016--查询所属应用系统结束==================");
        return pagerRecords;
    }
    /**
     *  810017--查询技术经理
     * @param pager
     * @param pname
     * @param orgname
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getProvinccePersonsByRoleByPage(Pager pager,@ServiceParam(name = "pname") String pname,
                                                        @ServiceParam(name = "agency.orgname") String orgname)throws BusException{
        log.debug("======================810017--查询技术经理开始==================");
        OgPerson ogPerson=new OgPerson();
        ogPerson.setpName(pname);
        ogPerson.setOrgname(orgname);
        List<Record> recordList = new ArrayList<Record>();
        List<OgPerson> persons = ogPersonService.selectOgPersonList(ogPerson);
        for(OgPerson opn:persons){
            Record record = new Record();
            record.put("pid",opn.getpId());//人员ID
            record.put("pname",opn.getpName());//人员名称
            if (opn.getOrgname() !=null) {
                record.put("agencyOrgname", opn.getOrgname());//所属机构
            }
            record.put("position", ogPerson.getPosition());//职位
            record.put("mobilPhone", ogPerson.getMobilPhone());//移动电话
            record.put("invalidationMark", ogPerson.getInvalidationMark());//状态
            recordList.add(record);
        }
        Page<OgPerson> OgPersonPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        PagerRecords pagerRecords = new PagerRecords(recordList, (int) OgPersonPage.getTotal());
        pagerRecords.setPager(pager);
        log.debug(pagerRecords.toString());
        log.debug("======================810017--查询技术经理结束==================");
        return pagerRecords;
    }
}
