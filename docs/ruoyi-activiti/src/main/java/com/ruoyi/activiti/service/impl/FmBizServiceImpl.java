package com.ruoyi.activiti.service.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.domain.*;
import com.ruoyi.activiti.mapper.CmBizSingleSJMapper;
import com.ruoyi.activiti.mapper.FmBizMapper;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.data.JSONException;
import com.ruoyi.common.data.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 业务事件单Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-23
 */
@Service
public class FmBizServiceImpl implements IFmBizService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FmBizServiceImpl.class);
    @Value("${pagehelper.helperDialect}")
    private String dataSourceType;

    @Autowired
    private FmBizMapper fmBizMapper;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private IFmBizDhyhService iFmBizDhyhService;
    @Autowired
    private IPubAttachmentService iPubAttachmentService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private IFmSysDTimeService iFmSysDTimeService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IOgGroupPersonService iOgGroupPersonService;
    @Autowired
    private IFmbizAndIssueService iFmbizAndIssueService;
    @Autowired(required=false)
    private KnowledgeBusinessContentService knowledgeBusinessContentService;
    @Autowired
    private IImBizIssuefxService iImBizIssuefxService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IPubParaService iPubParaService;
    @Autowired
    private IDifficultEventsService iDifficultEventsService;
    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;
    @Autowired
    private IOgUserPostService iOgUserPostService;
    @Autowired
    private CmBizSingleSJMapper cmBizSingleSJMapper;
    private String oneLineOS = "3c1ad0d140934d719302f1bdab2f8a40";
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    private final String SOURCE = "1";
    //电话银行ip端口
    @Value("${PhoneBank.url}")
    private String dhyhUrl;

    //信用卡ip端口
    @Value("${Card.url}")
    private String xykUrl;

    /**
     * 查询业务事件单
     *
     * @param fmId 业务事件单ID
     * @return 业务事件单
     */
    @Override
    public FmBiz selectFmBizById(String fmId) {
        return fmBizMapper.selectFmBizById(fmId);
    }

    @Override
    public FmBiz selectFmBizByFaultNo(String faultNo) {
        return fmBizMapper.selectFmBizByFaultNo(faultNo);
    }

    /**
     * 查询业务事件单列表
     *
     * @param fmBiz 业务事件单
     * @return 业务事件单
     */
    @Override
    public List<FmBiz> selectFmBizList(FmBiz fmBiz) {
        return fmBizMapper.selectFmBizList(fmBiz);
    }

    /**
     * 新增业务事件单
     *
     * @param fmBiz 业务事件单
     * @return 结果
     */
    @Override
    public int insertFmBiz(FmBiz fmBiz) {
        fmBiz.setCreatTime(DateUtils.dateTimeNow());
        OgUser u = ShiroUtils.getOgUser();
        if (u != null) {
            fmBiz.setCreateId(u.getpId());
            fmBiz.setCreateOrgId(iOgPersonService.selectOgPersonById(u.getpId()).getOrgId());

        } else {
            fmBiz.setCreateId(fmBiz.getCreateId());
            fmBiz.setCreateOrgId(iOgPersonService.selectOgPersonById(fmBiz.getCreateId()).getOrgId());
        }

        return fmBizMapper.insertFmBiz(fmBiz);
    }

    /**
     * 修改业务事件单
     *
     * @param fmBiz 业务事件单
     * @return 结果
     */
    @Override
    public int updateFmBiz(FmBiz fmBiz) {
        return fmBizMapper.updateFmBiz(fmBiz);
    }

    /**
     * 删除业务事件单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizByIds(String ids) {
        return fmBizMapper.deleteFmBizByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务事件单信息
     *
     * @param fmId 业务事件单ID
     * @return 结果
     */
    @Override
    public int deleteFmBizById(String fmId) {
        return fmBizMapper.deleteFmBizById(fmId);
    }

    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @Override
    public List<FmBiz> selectMylist(FmBiz fmBiz) {
        return fmBizMapper.selectMylist(fmBiz);
    }

    /**
     * 查询事件单
     */
    public List<FmBiz> selectFmBizListPager(FmBiz fmBiz) {
        return fmBizMapper.selectFmBizListPager(fmBiz);
    }

    @Override
    public List<FmSysDTime> selectGroupAnalysisList(FmBiz fmBiz) {
        List<FmSysDTime> list = fmBizMapper.selectGroupAnalysisList(fmBiz);
        return list;
    }

    @Override
    public List<FmBiz> selectGroupHandleList(FmBiz fmBiz) {
        List<FmBiz> list = fmBizMapper.selectGroupHandleList(fmBiz);
        return list;
    }

    @Override
    public List<FmBizJJAppr> selectFmBizCount(FmBiz fmBiz) {
        List<FmBizJJAppr> list;
        if("mysql".equals(dbType)){
            list = fmBizMapper.selectFmBizCountMysql(fmBiz);
        }else{
            list = fmBizMapper.selectFmBizCount(fmBiz);
        }
        return list;
    }

    @Override
    public FmBiz getFlowFmBiz(FmBiz fmBiz) {
        return fmBizMapper.getFlowFmBiz(fmBiz);
    }

    @Override
    public List<FmBizCDealerD> getDealerDCount1(FmBiz fmBiz) {
        List<FmBizCDealerD> list;
        if("mysql".equals(dbType)){
            list = fmBizMapper.getDealerDCount1Mysql(fmBiz);
        }else{
            list = fmBizMapper.getDealerDCount1(fmBiz);
        }
        return list;
    }

    @Override
    public List<FmBizCDealerD> getDealerDCount2(FmBiz fmBiz) {
        List<FmBizCDealerD> list;
        if("mysql".equals(dbType)){
            list = fmBizMapper.getDealerDCount2Mysql(fmBiz);
        }else{
            list = fmBizMapper.getDealerDCount2(fmBiz);
        }
        return list;
    }

    @Override
    public List<FmBiz> getPowerOffFmYw(int day) {
        String time = DateUtils.dateTimeNow();
        String day5 = DateUtils.getday5(time, day);
        return fmBizMapper.getPowerOffFmYw(day5);
    }

    @Override
    public List<FmBiz> getSysNDealCount(String sysid) {
        return fmBizMapper.getSysNDealCount(sysid);
    }

    @Override

    public List<FmBiz> getFmBizByRequestFail() {
        List<FmBiz> list = fmBizMapper.getFmBizByRequestFail();
        return list;
    }

    public List<FmBiz> getCompletedFmBizByDate(String endTime) {
        List<FmBiz> list = fmBizMapper.getCompletedFmBizByDate(endTime);
        return list;
    }

    public List<FmBiz> getCompletedFmBizByDate2(String endTime) {

        List<FmBiz> list = fmBizMapper.getCompletedFmBizByDate2(endTime);
        return list;
    }

    @Override
    public int updateFmBizByInvalidationMark(String id) {
        return fmBizMapper.updateFmBizByInvalidationMark(id);
    }

    @Override
    public List<FmBizCDealerD> getGroupCount() {
        List<FmBizCDealerD> list = new ArrayList<>();
        List<FmBizCDealerD> list2 = fmBizMapper.getGroupCount();
        if (!list2.isEmpty()) {
            list = list2;
        }
        return list;
    }

    @Override
    public FmBiz formatFmbiz(FmBiz fmBiz) {
        String startCreatTime = (String) fmBiz.getParams().get("startCreatTime");
        String endCreatTime = (String) fmBiz.getParams().get("endCreatTime");
        String startDealTime = (String) fmBiz.getParams().get("startDealTime");
        String endDealTime = (String) fmBiz.getParams().get("endDealTime");
        String startToQgzxTime = (String) fmBiz.getParams().get("startToQgzxTime");
        String endToQgzxTime = (String) fmBiz.getParams().get("endToQgzxTime");
        if (StringUtils.isNotEmpty(startCreatTime)) {
            String d1 = DateUtils.handleTimeYYYYMMDD(startCreatTime);
            fmBiz.getParams().put("startCreatTime", d1 + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            String d2 = DateUtils.handleTimeYYYYMMDD(endCreatTime);
            fmBiz.getParams().put("endCreatTime", d2 + "240000");
        }
        if (StringUtils.isNotEmpty(startDealTime)) {
            String d3 = DateUtils.handleTimeYYYYMMDD(startDealTime);
            fmBiz.getParams().put("startDealTime", d3 + "000000");
        }
        if (StringUtils.isNotEmpty(endDealTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(endDealTime);
            fmBiz.getParams().put("endDealTime", d4 + "240000");
        }
        if (StringUtils.isNotEmpty(startToQgzxTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(startToQgzxTime);
            fmBiz.getParams().put("startToQgzxTime", d4 + "000000");
        }
        if (StringUtils.isNotEmpty(endToQgzxTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(endToQgzxTime);
            fmBiz.getParams().put("endToQgzxTime", d4 + "240000");
        }
        if (StringUtils.isNotEmpty(fmBiz.getCurrentState())) {
            String[] CurrentState = fmBiz.getCurrentState().split(",");
            fmBiz.getParams().put("state", CurrentState);
        }
        if (StringUtils.isNotEmpty(fmBiz.getFmCause())) {
            String[] fmCause = fmBiz.getFmCause().split(",");
            fmBiz.getParams().put("cause", fmCause);
        }
        if (StringUtils.isNotEmpty(fmBiz.getDealMode())) {
            String[] dealModes = fmBiz.getDealMode().split(",");
            fmBiz.getParams().put("dealMode", dealModes);
        }
        if (StringUtils.isNotEmpty(fmBiz.getOccurrenceOrgId())) {
            OgOrg org = iSysDeptService.selectDeptById(fmBiz.getOccurrenceOrgId());
            fmBiz.getParams().put("levelCode", org.getLevelCode());
        }
        if (StringUtils.isNotEmpty(fmBiz.getParticipatorIds())) {
            OgPerson p = new OgPerson();
            p.setpName(fmBiz.getParticipatorIds());
            List<OgPerson> pList = iOgPersonService.selectOgPersonList(p);
            if (!pList.isEmpty()) {
                List<String> list = new ArrayList<>();
                for (OgPerson gp : pList) {
                    list.add(gp.getpId());
                }
                fmBiz.getParams().put("pList", list);
            }
        }
        //默认查询规则1、全国中心除厂商外其他人看所有2、厂商看自己工作组参与的事件单3、省内看自己省创建的
        OgUser u = ShiroUtils.getOgUser();
        OgOrg o = iSysDeptService.selectDeptById(iOgPersonService.selectOgPersonEvoById(u.getpId()).getOrgId());
        String lvCode = o.getLevelCode();
        if ("/000000000/".equals(lvCode)) {
            // 邮政金融运维人员，查看全部
        } else if (lvCode.startsWith("/000000000/010000888/")) {
            // 全国中心，查看全部
            if (lvCode.startsWith("/000000000/010000888/010900888/")) {
                List<OgGroup> group = iSysWorkService.selectGroupByUserId(u.getUserId());
                if (!group.isEmpty()) {
                    List<String> list = new ArrayList<>();
                    for (OgGroup gp : group) {
                        list.add(gp.getGroupId());
                    }
                    fmBiz.getParams().put("sGroupid", list);
                }
            }
        } else {
            fmBiz.getParams().put("sorgId", o.getOrgId());
        }
        return fmBiz;
    }

    @Override
    public List<FmBiz> selectFmBizListByMonitor(FmBiz fmBiz) {
        return fmBizMapper.selectFmBizListByMonitor(fmBiz);
    }

    @Override
    @Transactional
    public void savebacktoYnSj(FmBiz f, String pId) {
        List<CmBizSingleSJ> cbsList;
        if("mysql".equals(dataSourceType)){
            cbsList = cmBizSingleSJMapper.selectListByFmNo2Mysql(f.getFaultNo());
        }else{
            cbsList = cmBizSingleSJMapper.selectListByFmNo2(f.getFaultNo());
        }
        if (!cbsList.isEmpty()) {
            throw new BusinessException("该事件单已转过数据变更无需转疑难处理。");
        }
        DifficultEvents difficultEvents = new DifficultEvents();
        difficultEvents.setFmId(f.getFmId());
        List<DifficultEvents> diffList = iDifficultEventsService.selectDifficultEventsList(difficultEvents);
        if (!diffList.isEmpty()) {
            throw new BusinessException("该事件单已转过疑难事件单。");
        }
        iDifficultEventsService.insertDifficultEventsByFmBiz(f, SOURCE);
        //添加流程记录展示标记为疑难
        /*List<PubFlowLog> logList = pubFlowLogService.selectPubFlowLogDesc(f.getFmId());
        if (logList.isEmpty()) {
            return;
        }
        PubFlowLog plog = logList.get(0);
        PubFlowLog pfl = new PubFlowLog();
        pfl.setLogId(UUID.getUUIDStr());
        pfl.setBizId(f.getFmId());
        pfl.setLogType("FmBiz");
        pfl.setTaskName("标记疑难事件");
        pfl.setPerformerId(pId);
        pfl.setPerformerGroupId(plog.getPerformerGroupId());
        OgPerson person = iOgPersonService.selectOgPersonById(pId);
        pfl.setPerformerTel(person.getPhone() != null ? person.getPhone() : person.getMobilPhone());
        pfl.setPerformerOrgId(plog.getPerformerOrgId());
        pfl.setPerformerName(person.getpName());
        pfl.setPerformerGroupName(plog.getPerformerGroupName());
        pfl.setPerformerOrgName(plog.getPerformerOrgName());
        pfl.setPerformerTime(DateUtils.dateTimeNow());
        pfl.setPerformName("标记疑难");
        pfl.setNextTaskName("事件单处理");
        pfl.setSysResidenceTime("0");
        pfl.setSerialNum((Integer.parseInt(plog.getSerialNum()) + 1) + "");
        pfl.setNextPerformerDesc(plog.getNextPerformerDesc());
        pfl.setNextTaskId(plog.getNextTaskId());
        pfl.setNextPerformerTel(plog.getNextPerformerTel());
        pubFlowLogService.insertPubFlowLog(pfl);*/
    }

    @Override
    public void sendPhoneBankOrCard(FmBiz fmBiz, String dealResult, Object obj) {
        String faultSource = fmBiz.getFaultSource();
        if ("03".equals(faultSource) || "04".equals(faultSource)) {
            String telid = null;
            String urlt = "";
            String status = "0";
            if ("03".equals(faultSource)) {// 电话银行
                telid = fmBiz.getN1();
                urlt = dhyhUrl + "/agent/business/recviceYGManager.do";// 电话银行URL
            } else if ("04".equals(faultSource)) {// 信用卡
                telid = fmBiz.getN2();
                urlt = xykUrl + "/agent/business/recviceYGManager.do";// 信用卡URL
            }
            JSONObject parameters = new JSONObject();

            // 是否新增了附件
            Attachment att = new Attachment();
            att.setCreateId(fmBiz.getDealerId());
            att.setOwnerId(fmBiz.getFmId());
            List<Attachment> list = iPubAttachmentService.selectAttachmentList(att);
            if (list.size() > 0) {
                obj += "，有附件";
            }
            OgPerson person = iOgPersonService.selectOgPersonEvoById(fmBiz.getDealerId());// 获取事件单处理人
            String dealDept = iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getCaption();
            try {
                parameters.put("faultNo", telid); // 电话银行id
                parameters.put("dealId", person.getpName()); // 处理人
                parameters.put("mobilPhone", person.getMobilPhone()); // 处理人联系方式
                parameters.put("fmId", fmBiz.getFaultNo()); // 事件编号
                parameters.put("dealDept", dealDept); // 处理部门
                parameters.put("dealContent", obj); // 处理内容
                parameters.put("deal_result", dealResult); // 运管处理结果 ，0退回修改
            } catch (Exception e) {
                throw new RuntimeException("JSON异常", e);
            }

            // String
            String data = "fm_ID:" + telid + ";deal_ID:"
                    + person.getpName() + ";fault_NO:"
                    + fmBiz.getFaultNo() + ";deal_Dept:";
            System.out.println(data);

            HttpURLConnection con = null;

            try {
                URL url = new URL(urlt);
                con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/json;charset=GBK");
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "GBK"));
                writer.print(parameters.toString());
                writer.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "GBK"));
                String line = reader.readLine();
                if (line != null && !"".equals(line)) {
                    net.sf.json.JSONObject aa = net.sf.json.JSONObject.fromObject(line);
                    String a = aa.getString("response_code");
                    if ("-1".equals(a)) {
                        status = "1";
                    }
                }
                reader.close();
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                status = "1";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            //添加到转发电话银行消息的对象里start
            FmBizDhyh dhyh = new FmBizDhyh();
            dhyh.setFdId(UUID.getUUIDStr());
            dhyh.setTelId(telid);
            dhyh.setDealId(person.getpName());
            dhyh.setDealTel(person.getMobilPhone());
            dhyh.setFmId(fmBiz.getFaultNo());
            dhyh.setDealDept(dealDept);
            dhyh.setDealContent(obj + "");
            dhyh.setDealResult(dealResult);
            dhyh.setSendNum("1");
            dhyh.setFdStatus(status);
            dhyh.setFdType(faultSource);
            dhyh.setSendTime(DateUtils.getToday("yyyyMMddHHmmss"));
            iFmBizDhyhService.insertFmBizDhyh(dhyh);
            //添加到转发电话银行消息的对象里end
        }
    }

    @Override
    public void sendAntifraud(FmBiz f, String url) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("faultNo", f.getFaultNo());//单号
            parameters.put("createName", iOgPersonService.selectOgPersonById(f.getCreateId()).getpName());//创建人
            parameters.put("createOrgName", iSysDeptService.selectDeptById(f.getCreateOrgId()).getOrgName());//创建机构
            parameters.put("createTime", f.getCreatTime());//创建时间
            parameters.put("dealTime", f.getDealTime());//处理时间
            parameters.put("captipn", iSysApplicationManagerService.selectOgSysBySysId(f.getSysid()).getCaption());//所属系统
            parameters.put("faultText", f.getFaultDecriptDetail());//事件描述
            parameters.put("dealPersonName", iOgPersonService.selectOgPersonById(f.getDealerId()).getpName());//处理人
            parameters.put("DealText", f.getDealDescription());//处理描述
            parameters.put("involveAmount", f.getTransactionAmount());//交易金额
            parameters.put("orderNumber", f.getOrderNumber());//订单号
            parameters.put("ifjj", f.getIfJj());//是否紧急
        } catch (Exception e) {
            System.out.println("发送反欺诈接口JSON数据异常");
        }

        HttpURLConnection con = null;
        try {
            URL realUrl = new URL(url);
            con = (HttpURLConnection) realUrl.openConnection();
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json;charset=utf-8");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
            writer.print(parameters.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = reader.readLine();
            if (line != null && !"".equals(line)) {
                net.sf.json.JSONObject aa = net.sf.json.JSONObject.fromObject(line);
                String a = aa.getString("state");
                if ("0".equals(a)) {
                    f.setIsSend("1");
                } else {
                    f.setIsSend("0");
                    System.out.println("发送反欺诈失败：" + aa.getString("remark"));
                }
            }
            reader.close();
            con.disconnect();
        } catch (MalformedURLException e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e1) {
                System.out.println("发送发欺诈系统失败，单号是：" + f.getFaultNo());
                f.setIsSend("0");
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        updateFmBiz(f);
    }

    @Override
    public void calculationDealTime(FmBiz f) {
//关闭事件单时计算系统处理用时为事件单考核做数据预处理
        List<PubFlowLog> list = pubFlowLogService.selectPubFlowLogAsc(f.getFmId());
        String prePerformGroupId = "";// 上一步操作组id
        String performGroupId = "";// 当前操作组id
        String performName = "";// 操作名称
        String perforTime = "";// 操作时间
        String perforpName = "";// 操作人
        long sysUseTime = 0;//处理用时
        FmSysDTime fsd = new FmSysDTime();
        if (list.size() > 0) {
            for (PubFlowLog flowlog : list) {
                performGroupId = flowlog.getPerformerGroupId();//拿到当前组
                if (StringUtils.isNotEmpty(performGroupId)) {//当前组不为空
                    if (StringUtils.isEmpty(prePerformGroupId)) {//上一步组为空
                        sysUseTime += Long.parseLong(flowlog
                                .getSysResidenceTime());//拿到系统处理用时
                        performName = flowlog.getPerformName();
                        perforpName = flowlog.getPerformerId();
                        perforTime = flowlog.getPerformerTime();
                        prePerformGroupId = performGroupId;
                    } else {//上一步不为空
                        if (performGroupId.equals(prePerformGroupId)) {//判断当前组和上一步组是否相同如果相同则叠加时间
                            sysUseTime += Long.parseLong(flowlog
                                    .getSysResidenceTime());
                            performName = flowlog.getPerformName();
                            perforpName = flowlog.getPerformerId();
                            perforTime = flowlog.getPerformerTime();
                            prePerformGroupId = performGroupId;
                        } else {//不相同则是换工作组了
                            if (sysUseTime != 0) {
                                fsd.setBizId(f.getFmId());//保存业务事件单ID
                                fsd.setGroupId(prePerformGroupId);//保存工作组ID
                                fsd.setPerformName(performName);//保存操作名称
                                fsd.setDealUseTime(String.valueOf(sysUseTime));//保存处理用时
                                fsd.setDealId(perforpName);//保存处理人
                                fsd.setDealTime(perforTime);//保存处理时间
                                fsd.setFmSysDTimeId(UUID.getUUIDStr());//生成ID
                                iFmSysDTimeService.insertFmSysDTime(fsd);
                                //为下一个循环准备上一步骤初始数据
                                sysUseTime = Long.parseLong(flowlog.getSysResidenceTime());//重置系统处理用时计算下一个阶段
                                performName = flowlog.getPerformName();
                                prePerformGroupId = performGroupId;
                            }
                        }
                    }
                } else {//当前组为空
                    if (StringUtils.isNotEmpty(prePerformGroupId)) {//上一步组不为空
                        //保存上一阶段的处理用时
                        fsd.setBizId(f.getFmId());//保存业务事件单ID
                        fsd.setGroupId(prePerformGroupId);//保存工作组ID
                        fsd.setPerformName(performName);//保存操作名称
                        fsd.setDealId(perforpName);//保存处理人
                        fsd.setDealTime(perforTime);//保存处理时间
                        fsd.setDealUseTime(String.valueOf(sysUseTime));//保存处理用时
                        fsd.setFmSysDTimeId(UUID.getUUIDStr());//生成ID
                        iFmSysDTimeService.insertFmSysDTime(fsd);
                        //为下一阶段循环准备初始数据
                        sysUseTime = 0;//重置系统处理用时计算下一个阶段
                    }
                }
            }
        }
    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    @Override
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 给组长发短信
     *
     * @param flag    1-退回 2-转发
     * @param groupId 工作组
     * @param sysId   系统ID
     * @param faultNo 单号
     */
    @Override
    public void ifJJToSendMessage(String flag, String groupId, String sysId, String faultNo) {
        List<OgGroupPerson> gps = iSysWorkService.selectGroupIdByPersonList(groupId);
        String sysname = iSysApplicationManagerService.selectOgSysBySysId(sysId).getCaption();
        List<OgPerson> ops = new ArrayList<OgPerson>();
        if (!gps.isEmpty()) {
            for (OgGroupPerson gp : gps) {
                String gposition = gp.getGpOsition();//1组长 2数据中心负责人 3组员
                if ("1".equals(gposition)) {
                    ops.add(gp.getPerson());
                }
            }

            for (OgPerson op : ops) {
                //发送短信
                OgPerson person = iOgPersonService.selectOgPersonEvoById(op.getpId());// 获取短信接收人
                String setSmstext = "";
                if ("1".equals(flag)) {
                    setSmstext = "{" + sysname + "}收到退回紧急事件单，单号是：" + faultNo + "，请及时处理。";// 短信内容
                } else {
                    setSmstext = "{" + sysname + "}收到转发事件单，单号是：" + faultNo + "，请及时处理。";// 短信内容
                }
                sendSms(setSmstext, person);
            }
        }
    }

    /**
     * 给二线值班发送短信
     *
     * @param faultNo 单号
     * @param sysname 应用系统名
     */
    @Override
    public void sendMessageToApplication(String faultNo, String sysname) {
        PubBizPresms p1 = new PubBizPresms();
        p1.setTelephone("13621150163");// 手机号
        p1.setName("应用二线值班");// 姓名
        p1.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p1.setSmsText("{" + sysname + "}收到新的紧急事件单，单号是：" + faultNo + "，请及时处理。");// 短信内容
        p1.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p1.setInspectObject("050100");// 检查对象
        p1.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p1.setInvalidationMark("1");
        p1.setSmsState("0");
        p1.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p1);

        pubBizSmsService.findPreSmsAndSend(p1);//发送短信
    }

    /**
     * 如果是紧急事件单发送短信
     *
     * @param groupId 工作组ID
     * @param faultNo 事件单号
     */
    @Override
    public void ifJJSendMessage(String groupId, String sysId, String faultNo) {
        List<OgGroupPerson> gps = iSysWorkService.selectGroupIdByPersonList(groupId);
        String sysname = iSysApplicationManagerService.selectOgSysBySysId(sysId).getCaption();
        List<OgPerson> ops = new ArrayList();
        if (!gps.isEmpty()) {
            for (OgGroupPerson gp : gps) {
                String gposition = gp.getGpOsition();//1组长 2数据中心负责人 3组员
                if ("1".equals(gposition)) {
                    ops.add(gp.getPerson());
                }
            }
            if (!ops.isEmpty()) {
                for (OgPerson op : ops) {
                    //发送短信
                    String setSmstext = "{" + sysname + "}收到新的紧急事件单，单号是：" + faultNo + "，请及时处理。";// 短信内容
                    sendSms(setSmstext, op);
                }
            }
            sendMessageToApplication(faultNo, sysname);
        }
    }

    @Override
    public List<FmBiz> getFmBizByAll(List<String> list) {
        return fmBizMapper.getFmBizByAll(list);
    }

    @Override
    public ModelMap getTypeList(FmBiz fmbiz, ModelMap mmap) {
        if (StringUtils.isNotEmpty(fmbiz.getIniOneType())) {//一级分类名称
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setCategory("1");
            kdt.setEventType("1");
            kdt.setStatus("0");
            kdt.setSysId(fmbiz.getSysid());
            mmap.put("oneTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(fmbiz.getIniTwoType())) {//二级分类
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setParentId(fmbiz.getOneType());
            kdt.setSysId(fmbiz.getSysid());
            kdt.setStatus("0");
            mmap.put("twoTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(fmbiz.getIniThreeType())) {//三级分类
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setSysId(fmbiz.getSysid());
            kdt.setStatus("0");
            kdt.setParentId(fmbiz.getTwoType());
            mmap.put("threeTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(fmbiz.getIniKeywords())) {//关键字
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setSysId(fmbiz.getSysid());
            kdt.setParentId(fmbiz.getThreeType());
            mmap.put("keyWorksList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        return mmap;
    }

    @Override
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowremoveRecord(FmBiz fmBiz) {
        FmBiz f = selectFmBizById(fmBiz.getFmId());
        if (!"06".equals(f.getCurrentState())) {
            throw new BusinessException("该事件单已被处理，请刷新列表检查后继续操作。");
        } else {
            OgOrg org = iSysDeptService.selectDeptById(iOgPersonService.selectOgPersonEvoById(f.getDealerId()).getOrgId());
            if ("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/")) {
                fmBiz.setCurrentState("04");//处理中状态
                fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//添加转全国中心时间
            } else {
                fmBiz.setCurrentState("11");
            }
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", fmBiz.getFmId());
        String catory = iPubParaValueService.selectPubParaValueByParaNameApp("chargeback_dissatisfaction_category", (String) fmBiz.getParams().get("catory"));
        if (fmBiz.getParams().containsKey("comment")) {
            reMap.put("comment", catory + "|" + fmBiz.getParams().get("comment"));
        }
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("reCode", "1");

        String pId = ShiroUtils.getOgUser().getpId();
        reMap.put("userId", pId);
        //添加参与人
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(pId + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        try {
            updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("业务事件评价退回操作失败，单号：" + f.getFaultNo());
        }
        if ("04".equals(fmBiz.getCurrentState())) {//是否全国中心工作组处理
            iFmbizAndIssueService.deleteFmbizAndIssueByFmId(fmBiz.getFmId());//清除关联问题单绑定关系
        }
        String smsText = "业务事件单号：" + f.getFaultNo()
                + "的单子已经已经被退回，请登录运维管理平台查看。";//短信内容
        OgPerson person = iOgPersonService.selectOgPersonEvoById(f.getDealerId());// 获取短信接收人
        sendSms(smsText, person);
        if ("1".equals(f.getIfJj()) && "04".equals(fmBiz.getCurrentState())) {
            ifJJToSendMessage("1", f.getGroupId(), f.getSysid(), f.getFaultNo());
            sendMessageToApplication(f.getFaultNo(), iSysApplicationManagerService.selectOgSysBySysId(f.getSysid()).getCaption());
        }
        return AjaxResult.success("业务事件评价退回操作成功", f.getFaultNo());
    }

    public boolean ifGroup(String groupId) {//判断处理人是否为二线全国中心工作组
        boolean b = false;
        OgGroup group = iSysWorkService.selectOgGroupById(groupId);
        if (!ObjectUtils.isEmpty(group)) {
            if ("2".equals(group.getGroupType())) {
                b = true;
            }
        }
        return b;
    }

    @Override
    public List<FmBiz> getFmBizAndIssueList(FmBiz fmBiz) {
        List<FmBiz> fmBizs = new ArrayList<>();
        OgGroupPerson ogGroupPerson = new OgGroupPerson();
        ogGroupPerson.setPid(ShiroUtils.getUserId());
        List<OgGroupPerson> list = iOgGroupPersonService.selectOgGroupPersonList(ogGroupPerson);//拿到登陆人所有的工作组
        if (!list.isEmpty()) {
            List<String> groupId = new ArrayList<>();
            for (OgGroupPerson groupPerson : list) {
                groupId.add(groupPerson.getGroupId());
            }
            fmBiz.getParams().put("sGroupid", groupId);
            if (StringUtils.isNotEmpty(fmBiz.getOccurrenceOrgId())) {
                OgOrg org = iSysDeptService.selectDeptById(fmBiz.getOccurrenceOrgId());
                fmBiz.getParams().put("levelCode", org.getLevelCode());
            }
            String startCreatTime = (String) fmBiz.getParams().get("startCreatTime");
            String endCreatTime = (String) fmBiz.getParams().get("endCreatTime");
            String startDealTime = (String) fmBiz.getParams().get("startDealTime");
            String endDealTime = (String) fmBiz.getParams().get("endDealTime");
            if (StringUtils.isNotEmpty(startCreatTime)) {
                String d1 = DateUtils.handleTimeYYYYMMDD(startCreatTime);
                fmBiz.getParams().put("startCreatTime", d1 + "000000");
            }
            if (StringUtils.isNotEmpty(endCreatTime)) {
                String d2 = DateUtils.handleTimeYYYYMMDD(endCreatTime);
                fmBiz.getParams().put("endCreatTime", d2 + "240000");
            }
            if (StringUtils.isNotEmpty(startDealTime)) {
                String d3 = DateUtils.handleTimeYYYYMMDD(startDealTime);
                fmBiz.getParams().put("startDealTime", d3 + "000000");
            }
            if (StringUtils.isNotEmpty(endDealTime)) {
                String d4 = DateUtils.handleTimeYYYYMMDD(endDealTime);
                fmBiz.getParams().put("endDealTime", d4 + "240000");
            }
            startPage();
            fmBizs = fmBizMapper.getFmBizAndIssueList(fmBiz);
        }
        return fmBizs;
    }

    @Override
    public String ifKnowledgeRelationProblem(FmBiz fmBiz) {
        String str = "1";
        boolean b = ifGroup(fmBiz.getGroupId());
        if (!b) {
            KnowledgeContent kc = knowledgeBusinessContentService.selectIssuefxIdForKnowledgeId(fmBiz.getKnowledgeId());
            if (!ObjectUtils.isEmpty(kc)) {
                String issuefxId = kc.getIssuefxId();
                if (StringUtils.isNotEmpty(issuefxId)) {
                    ImBizIssuefx ii = iImBizIssuefxService.selectImBizIssuefxById(issuefxId);
                    if (!ObjectUtils.isEmpty(ii)) {
                        str = "0";
                    }
                }
            }
        } else {
            str = "0";
        }
        return str;
    }

    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    public AjaxResult getFmBizOneLineApi(Map<String, String> map) {
        String fmNo = map.get("fmNo");
        if (StringUtils.isEmpty(fmNo)) {
            return AjaxResult.error("单号不能为空。");
        }
        Map<String, String> fmBizMap = new HashedMap();
        FmBiz fmBiz = selectFmBizByFaultNo(fmNo);
        if (!ObjectUtils.isEmpty(fmBiz)) {
            fmBizMap.put("date", DateUtils.timeMillis(fmBiz.getOccurTime()));//发生日期
            fmBizMap.put("transferTime", DateUtils.timeToTimeMillis(fmBiz.getToQgzxTime()));//转全国中心时间
            fmBizMap.put("onelineName", fmBiz.getCreateGroupName());//工作组
            fmBizMap.put("sysName", fmBiz.getOgSys().getCaption());//系统名
            fmBizMap.put("no", fmBiz.getFaultNo());//单号
            fmBizMap.put("dealName", fmBiz.getDealName());//处理人
            String status = iPubParaValueService.selectPubParaValueByParaNameApp("fmStatus", fmBiz.getCurrentState());
            fmBizMap.put("status", status);//状态
        }
        return AjaxResult.success(fmBizMap);
    }

    /**
     * 一线工作组领取事件单后发送信息给一线管理系统
     *
     * @param f
     */
    @Override
    public void pushOneLine(FmBiz f) {
        String state = iPubParaService.selectPubParaById(oneLineOS).getState();
        if (!"1".equals(state)) {
            logger.error("发送一线管理系统开关关闭。");
            return;
        }
        JSONObject parameters = new JSONObject();
        try {
            //日期
            parameters.put("date", DateUtils.getDate());//发生日期
            //转全国中心时间
            parameters.put("transferTime", DateUtils.timeToTimeMillis(f.getToQgzxTime()));//转全国中心时间
            //系统名
            parameters.put("sysName", f.getOgSys().getCaption());
            //业务事件单编号
            parameters.put("no", f.getFaultNo());
            //是否接手
            parameters.put("ifRogger", "true");
            //接手时间
            parameters.put("roggerTime", DateUtils.getTime());
            //接手人姓名
            //parameters.put("fixPerson", iOgPersonService.selectOgPersonById(f.getDealerId()).getpName());
            //（北京1组，北京2组，合肥1组，合肥2组，合肥3组，合肥4组，合肥5组，合肥6组，合肥7组，合肥8组）
            parameters.put("onelineName", f.getCreateGroupName());

        } catch (JSONException e) {
            e.printStackTrace();
            logger.error("接口JSON数据异常");
        }

        HttpURLConnection con = null;
        try {
            PubParaValue ppv = iPubParaValueService.selectPubParaValue("oneLineUrl", "fmBizUrl");
            URL realUrl = new URL(ppv.getValueDetail());
            con = (HttpURLConnection) realUrl.openConnection();
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json;charset=utf-8");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
            System.out.println("发送一线管理系统数据为：" + parameters.toString());
            writer.print(parameters.toString());
            writer.close();
            int responseCode = con.getResponseCode();
            if (200 != responseCode) {
                logger.error("发送一线管理系统失败！-------------->" + responseCode);
            }
            con.disconnect();
        } catch (MalformedURLException e) {
            logger.error("url路径异常！-------------->" + e.getMessage());
        } catch (IOException e) {
            logger.error("发送异常！-------------->" + e.getMessage());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    @Override
    public List<FmBiz> getFmBizAndIssueListExport(FmBiz fmBiz, String flag) {
        List<FmBiz> fmBizs = new ArrayList<>();
        OgGroupPerson ogGroupPerson = new OgGroupPerson();
        ogGroupPerson.setPid(ShiroUtils.getUserId());
        List<OgGroupPerson> list = iOgGroupPersonService.selectOgGroupPersonList(ogGroupPerson);//拿到登陆人所有的工作组
        if (!list.isEmpty()) {
            List<String> groupId = new ArrayList<>();
            for (OgGroupPerson groupPerson : list) {
                groupId.add(groupPerson.getGroupId());
            }
            fmBiz.getParams().put("sGroupid", groupId);
            if (StringUtils.isNotEmpty(fmBiz.getOccurrenceOrgId())) {
                OgOrg org = iSysDeptService.selectDeptById(fmBiz.getOccurrenceOrgId());
                fmBiz.getParams().put("levelCode", org.getLevelCode());
            }
            String startCreatTime = fmBiz.getParams().get("startCreatTime").toString();
            String endCreatTime = fmBiz.getParams().get("endCreatTime").toString();
            String startDealTime = fmBiz.getParams().get("startDealTime").toString();
            String endDealTime = fmBiz.getParams().get("endDealTime").toString();
            if (StringUtils.isNotEmpty(startCreatTime)) {
                String d1 = DateUtils.handleTimeYYYYMMDD(startCreatTime);
                fmBiz.getParams().put("startCreatTime", d1 + "000000");
            }
            if (StringUtils.isNotEmpty(endCreatTime)) {
                String d2 = DateUtils.handleTimeYYYYMMDD(endCreatTime);
                fmBiz.getParams().put("endCreatTime", d2 + "240000");
            }
            if (StringUtils.isNotEmpty(startDealTime)) {
                String d3 = DateUtils.handleTimeYYYYMMDD(startDealTime);
                fmBiz.getParams().put("startDealTime", d3 + "000000");
            }
            if (StringUtils.isNotEmpty(endDealTime)) {
                String d4 = DateUtils.handleTimeYYYYMMDD(endDealTime);
                fmBiz.getParams().put("endDealTime", d4 + "240000");
            }
            if ("1".equals(flag)) {
                startPage();
            }
            fmBizs = fmBizMapper.getFmBizAndIssueList(fmBiz);
        }
        return fmBizs;
    }

    @Override
    public List<FmBiz> getFmBizByFlowLogCount(int order) {
        return fmBizMapper.getFmBizByFlowLogCount(order);
    }

    @Override
    public List<FmBiz> getFmBizByToQgzxTime(String toQgzxTime) {
        return fmBizMapper.getFmBizByToQgzxTime(toQgzxTime);
    }

    @Override
    public AjaxResult saveflowrepeatIn(FmBiz fmBiz) {
        FmBiz f = selectFmBizById(fmBiz.getFmId());
        OgUser u = ShiroUtils.getOgUser();
        ifIdToDealId(u.getpId(), f.getDealerId());
        if (!"04".equals(f.getCurrentState()) && !"11".equals(f.getCurrentState())) {
            throw new BusinessException("该事件单已被处理，请刷新列表检查后继续操作。");
        }
        List<CmBizSingleSJ> list = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
        if (!list.isEmpty()) {
            throw new BusinessException("该事件单存在数据变更单在途，请刷新列表检查后继续操作。");
        }
        //添加参与人
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(u.getpId() + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + u.getpId() + ",");
        }
        Map<String, Object> reMap = new HashMap<>();//流程所需参数回填
        reMap.put("businessKey", f.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("reCode", "3");
        reMap.put("dealGroupId", f.getDealGroupId());
        reMap.put("comment", fmBiz.getParams().get("comment"));
        reMap.put("dealId", fmBiz.getDealerId());//更换处理人
        reMap.put("userId", u.getUserId());
        try {
            updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("业务事件单组内转发操作失败，单号：" + f.getFaultNo());
        }
        //判断组内转发的是否为总行业务人员如果是需要发送短信通知业务处理
        List<OgUserPost> orpList = iOgUserPostService.selectPostByUserIdTwo(fmBiz.getDealerId());
        boolean b = false;
        for (OgUserPost orp : orpList) {
            if ("0013".equals(orp.getPostId())) {//判断当前登陆人是否包含总行业务人员岗位
                b = true;
                break;
            }
        }
        if (b) {
            String smsText = "业务事件单号：" + f.getFaultNo()
                    + "需您进行处理，请登录运维管理平台查看并处理。";//短信内容
            OgPerson person = iOgPersonService.selectOgPersonEvoById(fmBiz.getDealerId());// 获取短信接收人
            sendSms(smsText, person);
        }
        return AjaxResult.success("业务事件单组内转发操作成功", f.getFaultNo());
    }

    @Override
    public void ifIdToDealId(String id1, String id2) {
        if (!id1.equals(id2)) {
            throw new BusinessException("该事件单处理人已更改，请刷新后检查列表重新操作。");
        }
    }

    /**
     * 待处理、待领取事件单map排序根据上一步操作时间排序
     *
     * @param resultlist
     */
    @Override
    public List<FmBiz> compareList2(List<FmBiz> resultlist) {
        List<FmBiz> list1 = new ArrayList<>();
        List<FmBiz> list2 = new ArrayList<>();
        List<FmBiz> cmList = new ArrayList<>();
        List list3 = new ArrayList<>();
        if (!resultlist.isEmpty()) {
            for (FmBiz fb : resultlist) {
                if ("1".equals(fb.getParams().get("ifcmsj"))) {
                    cmList.add(fb);
                } else {
                    if ("1".equals(fb.getIfJj()) || "3".equals(fb.getSeriousLev())) {
                        list1.add(fb);//添加高级紧急的进第一个list中
                    } else {
                        list2.add(fb);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(list1)) {
            Collections.sort(list1, new Comparator<FmBiz>() {
                @Override
                public int compare(FmBiz o1, FmBiz o2) {
                    Date createTime1 = (Date) o1.getParams().get("createTime");
                    Date createTime2 = (Date) o2.getParams().get("createTime");
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        if (!CollectionUtils.isEmpty(list2)) {
            Collections.sort(list2, new Comparator<FmBiz>() {
                @Override
                public int compare(FmBiz o1, FmBiz o2) {
                    Date createTime1 = (Date) o1.getParams().get("createTime");
                    Date createTime2 = (Date) o2.getParams().get("createTime");
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        list3.addAll(list1);
        list3.addAll(list2);
        list3.addAll(cmList);
        return list3;
    }

    /**
     * 待处理、待领取事件单map排序根据全国中心时间
     *
     * @param resultlist
     */
    @Override
    public List<FmBiz> compareList(List<FmBiz> resultlist) {
        List<FmBiz> list1 = new ArrayList<>();
        List<FmBiz> list2 = new ArrayList<>();
        List<FmBiz> cmList = new ArrayList<>();
        List list3 = new ArrayList<>();
        if (!resultlist.isEmpty()) {
            for (FmBiz fb : resultlist) {
                if ("1".equals(fb.getParams().get("ifcmsj"))) {
                    cmList.add(fb);
                } else {
                    if ("1".equals(fb.getIfJj()) || "3".equals(fb.getSeriousLev())) {
                        list1.add(fb);//添加高级紧急的进第一个list中
                    } else {
                        list2.add(fb);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(list1)) {
            Collections.sort(list1, new Comparator<FmBiz>() {
                @Override
                public int compare(FmBiz o1, FmBiz o2) {
                    Date createTime1 = DateUtils.parseDate(o1.getToQgzxTime(), "yyyyMMddHHmmss");
                    Date createTime2 = DateUtils.parseDate(o2.getToQgzxTime(), "yyyyMMddHHmmss");
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        if (!CollectionUtils.isEmpty(list2)) {
            Collections.sort(list2, new Comparator<FmBiz>() {
                @Override
                public int compare(FmBiz o1, FmBiz o2) {
                    Date createTime1 = DateUtils.parseDate(o1.getToQgzxTime(), "yyyyMMddHHmmss");
                    Date createTime2 = DateUtils.parseDate(o2.getToQgzxTime(), "yyyyMMddHHmmss");
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        list3.addAll(list1);
        list3.addAll(list2);
        list3.addAll(cmList);
        return list3;
    }

    @Override
    public List<FmBiz> getFmBizAndIssueEdit(FmBiz fmBiz) {
        if (StringUtils.isNotEmpty(fmBiz.getOccurrenceOrgId())) {
            OgOrg org = iSysDeptService.selectDeptById(fmBiz.getOccurrenceOrgId());
            fmBiz.getParams().put("levelCode", org.getLevelCode());
        }
        String startCreatTime = (String) fmBiz.getParams().get("startCreatTime");
        String endCreatTime = (String) fmBiz.getParams().get("endCreatTime");
        String startDealTime = (String) fmBiz.getParams().get("startDealTime");
        String endDealTime = (String) fmBiz.getParams().get("endDealTime");
        if (StringUtils.isNotEmpty(startCreatTime)) {
            String d1 = DateUtils.handleTimeYYYYMMDD(startCreatTime);
            fmBiz.getParams().put("startCreatTime", d1 + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            String d2 = DateUtils.handleTimeYYYYMMDD(endCreatTime);
            fmBiz.getParams().put("endCreatTime", d2 + "240000");
        }
        if (StringUtils.isNotEmpty(startDealTime)) {
            String d3 = DateUtils.handleTimeYYYYMMDD(startDealTime);
            fmBiz.getParams().put("startDealTime", d3 + "000000");
        }
        if (StringUtils.isNotEmpty(endDealTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(endDealTime);
            fmBiz.getParams().put("endDealTime", d4 + "240000");
        }
        fmBiz.setDealerId(ShiroUtils.getUserId());
        startPage();
        return fmBizMapper.getFmBizAndIssueEdit(fmBiz);
    }

    @Override
    public List<OgPerson> getPersonByTransfer(FmBiz fmBiz) {
        String groupId = fmBiz.getGroupId();
        OgGroupPerson ogGroupPerson = new OgGroupPerson();
        ogGroupPerson.setPid(ShiroUtils.getUserId());
        ogGroupPerson.setGpOsition("1");
        List<OgGroupPerson> list = iOgGroupPersonService.selectOgGroupPersonList(ogGroupPerson);//拿到登陆人是组长职位的所有工作组
        for (OgGroupPerson gp : list) {
            if (gp.getGroupId().equals(groupId)) {
                return iSysWorkService.selectGroupByPerson(groupId);
            }
        }
        throw new BusinessException("该事件单已处理请刷新列表后操作。");
    }

    @Override
    public AjaxResult saveflowTransfer(FmBiz fmBiz) {
        FmBiz f = selectFmBizById(fmBiz.getFmId());
        getPersonByTransfer(f);//判断是否为自己所在为组长的工作组所属事件单
        fmBiz.setCurrentState("04");
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        if ("03".equals(f.getCurrentState())) {//待领取为处理人做领取
            //添加参与组
            if (StringUtils.isEmpty(f.getParticipateGroupids())) {
                fmBiz.setParticipateGroupids(f.getGroupId()
                        + ",");
            } else {
                fmBiz.setParticipateGroupids(f
                        .getParticipateGroupids()
                        + f.getGroupId()
                        + ",");
            }
            updateFmBiz(fmBiz);
            reMap.put("dealId", fmBiz.getDealerId());
            reMap.put("dealGroupId", f.getDealGroupId());
            reMap.put("businessKey", f.getFmId());
            reMap.put("processDefinitionKey", "FmBiz");
            activitiCommService.complete(reMap);
        } else if ("04".equals(f.getCurrentState())) {//待处理给处理人做转发
            fmBiz.setDealerId(fmBiz.getDealerId());//重新填写处理人
            updateFmBiz(fmBiz);
            reMap.put("dealId", fmBiz.getDealerId());
            reMap.put("dealGroupId", f.getDealGroupId());
            reMap.put("reCode", "3");
            reMap.put("businessKey", f.getFmId());
            reMap.put("processDefinitionKey", "FmBiz");
            activitiCommService.complete(reMap);
        } else {
            throw new BusinessException("该事件单已被处理，请刷新列表检查后继续操作。");
        }
        return AjaxResult.success("事件单转办完成。");
    }

}
