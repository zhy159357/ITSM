package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.constants.ImBizIssueConstants;
import com.ruoyi.activiti.mapper.ImBizDataqMapper;
import com.ruoyi.activiti.mapper.ImBizIssuefxMapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.IImBizDataqService;
import com.ruoyi.common.constant.DictConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.VmDept;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import org.springframework.ui.ModelMap;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-09-28
 */
@Service
public class ImBizDataqServiceImpl implements IImBizDataqService {
    @Autowired
    private ImBizDataqMapper imBizDataqMapper;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IImBizDataqService imBizDataqService;

    @Autowired
    private ImBizIssuefxMapper issueListMapper;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private IPubParaValueService iPubParaValueService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysDeptService iSysDeptService;
    private String processDefinitionKey = "IMDATA";

    /**
     * 查询【请填写功能名称】
     *
     * @param imId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ImBizDataq selectImBizDataqById(String imId) {
        return imBizDataqMapper.selectImBizDataqById(imId);
    }


    /**
     * 新增
     *
     * @param imBizDataq
     * @return 结果
     */
    @Override
    public int insertImBizDataq(ImBizDataq imBizDataq) {
        //新增数据变更页面新增数据问题单
        //状态为待提交
        imBizDataq.setCurrentState("01");
        //创建人
        imBizDataq.setCreaterId(ShiroUtils.getUserId());
        //有效标志
        imBizDataq.setInvalidationMark("1");
        //创建时间
        imBizDataq.setCreatTime(DateUtils.dateTimeNow());
        saveFormatTime(imBizDataq);
        return imBizDataqMapper.insertImBizDataq(imBizDataq);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param imBizDataq 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateImBizDataq(ImBizDataq imBizDataq) {
        removeBusinessRepeat(imBizDataq);
        //日期格式转换
        if (StringUtils.isNotEmpty(imBizDataq.getCreatTime())) {
            imBizDataq.setCreatTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getCreatTime()));
        }
        if (StringUtils.isNotEmpty(imBizDataq.getProblemDiscoveryTime())) {
            imBizDataq.setProblemDiscoveryTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getProblemDiscoveryTime()));
        }
        if (StringUtils.isNotEmpty(imBizDataq.getBusinessPlanTime())) {
            imBizDataq.setBusinessPlanTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getBusinessPlanTime()));
        }
        return imBizDataqMapper.updateImBizDataq(imBizDataq);
    }

    /**
     * 业务审核数据去重
     *
     * @param imBizDataq
     */
    public void removeBusinessRepeat(ImBizDataq imBizDataq) {
        Map<String, Object> params = imBizDataq.getParams();
        // 新增｜编辑页面保存|更新|提交数据需要去重业务部门数据
        List<Map> list = getBusinessList(imBizDataq);
        if (org.springframework.util.CollectionUtils.isEmpty(list)) {
            return;
        }
        String businessOrg = "";
        String businessAuditId = "";
        String businessApprovalId = "";
        String chargeLeadership = "";
        for (Map map : list) {
            businessOrg += map.get("businessOrgName") + ",";
            businessAuditId += map.get("businessAuditId") + ",";
            if (!"".equals(imBizDataq.getBusinessAppid())) {
                businessApprovalId += map.get("businessApprovalId") + ",";
            } else {
                businessApprovalId += "" + ",";
            }
            if (!"".equals(imBizDataq.getChargeLeadership())) {
                chargeLeadership += map.get("chargeLeadership") + ",";
            } else {
                chargeLeadership += "" + ",";
            }
        }
        imBizDataq.setBusinessDept(StringUtils.removeLastChar(businessOrg));
        imBizDataq.setBusinessId(StringUtils.removeLastChar(businessAuditId));
        imBizDataq.setBusinessAppid(StringUtils.removeLastChar(businessApprovalId));
        imBizDataq.setChargeLeadership(StringUtils.removeLastChar(chargeLeadership));

    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteImBizDataqByIds(String ids) {
        return imBizDataqMapper.deleteImBizDataqByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param imId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteImBizDataqById(String imId) {
        return imBizDataqMapper.deleteImBizDataqById(imId);
    }

    @Override
    public ModelMap addview(ModelMap modelMap) {
        //数据问题单号
        String bizType = "SJWT";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        modelMap.put("num", bizType + nowDateStr + numStr);
        //创建人
        String userId = ShiroUtils.getUserId();
        OgPerson person = ogPersonService.selectOgPersonById(userId);
        if (!StringUtils.isEmpty(person)) {
            modelMap.put("createName", person.getpName());
        }
        //创建时间
        modelMap.put("createTime", DateUtils.getTime());
        //问题单ID
        modelMap.put("imId", UUID.getUUIDStr());

        return modelMap;
    }

    @Override
    public ModelMap xgsjwt(String imNo, ModelMap modelMap) {

        //根据问题单号查询数据问题单
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqByNo(imNo);
        if (!StringUtils.isEmpty(imBizDataq)) {
            modelMap.put("imBizDataq", imBizDataq);
            //获取创建人名称
            OgPerson person = ogPersonService.selectOgPersonById(imBizDataq.getCreaterId());
            if (!StringUtils.isEmpty(person)) {
                modelMap.put("createName", person.getpName());
            }
            objectFormat(imBizDataq);

        }

        return modelMap;
    }

    /**
     * 根据单号查询
     *
     * @param imNo
     * @return
     */
    @Override
    public ImBizDataq selectImBizDataqByNo(String imNo) {
        return imBizDataqMapper.selectImBizDataqByNo(imNo);
    }

    /**
     * 根据Id查询数据问题单集合
     *
     * @param id
     * @return
     */
    @Override
    public List<ImBizDataq> selectImBizDataqListById(String id) {
        return imBizDataqMapper.selectImBizDataqListById(id);
    }


    public List<PubParaValue> selectIssuesourceList(String source) {
        return issueListMapper.selectIssuesourceList(source);
    }

    public List<OgOrg> selectOgOrgList() {
        return issueListMapper.selectOgOrg();
    }

    /**20211003修改**/

    /**
     * 设置请求分页数据
     */
    protected static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 根据传入对象转换日期格式为YYYYMMDDHHMMSS
     *
     * @param imBizDataq
     * @return
     */
    public static ImBizDataq dataFormatYYYYMMDDHHMMSS(ImBizDataq imBizDataq) {
        try {
            if (StringUtils.isNotEmpty(imBizDataq.getCreatTime())) {
                String parseStart = DateUtils.handleTimeYYYYMMDD(imBizDataq.getCreatTime());
                imBizDataq.setCreatTime(parseStart + "000000");
            }
            if (StringUtils.isNotEmpty(imBizDataq.getEndCreateTime())) {
                String parseEnd = DateUtils.handleTimeYYYYMMDD(imBizDataq.getEndCreateTime());
                imBizDataq.setEndCreateTime(parseEnd + "240000");
            }
            if (StringUtils.isNotEmpty(imBizDataq.getProblemDiscoveryTime())) {
                String parseStart = DateUtils.handleTimeYYYYMMDD(imBizDataq.getProblemDiscoveryTime());
                imBizDataq.setProblemDiscoveryTime(parseStart + "000000");
            }
            if (StringUtils.isNotEmpty(imBizDataq.getEndproblemDiscoveryTime())) {
                String parseEnd = DateUtils.handleTimeYYYYMMDD(imBizDataq.getEndproblemDiscoveryTime());
                imBizDataq.setEndproblemDiscoveryTime(parseEnd + "240000");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("单号为：" + imBizDataq.getImNo() + "的单子日期格式转换失败。");
        }
        return imBizDataq;
    }

    /**
     * 对数据问题单回显中文翻译
     *
     * @param imBizDataq
     * @return
     */
    public static ImBizDataq objectFormat(ImBizDataq imBizDataq) {
        //创建时间进行日期回显
        String creatTime = imBizDataq.getCreatTime();
        if (StringUtils.isNotEmpty(creatTime)) {
            imBizDataq.setCreatTime(DateUtils.formatDateStr(creatTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //问题发生时间进行日期回显
        String problemDiscoveryTime = imBizDataq.getProblemDiscoveryTime();
        if (StringUtils.isNotEmpty(problemDiscoveryTime)) {
            imBizDataq.setProblemDiscoveryTime(DateUtils.formatDateStr(problemDiscoveryTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //问题业务计划日期回显
        String businessPlanTime = imBizDataq.getBusinessPlanTime();
        if (StringUtils.isNotEmpty(businessPlanTime)) {
            imBizDataq.setBusinessPlanTime(DateUtils.formatDateStr(businessPlanTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        return imBizDataq;
    }

    @Override
    public List<ImBizDataq> selectImBizDataqList(ImBizDataq imBizDataq) {
        imBizDataq = dataFormatYYYYMMDDHHMMSS(imBizDataq);
        imBizDataq.setCreaterId(ShiroUtils.getUserId());
        return imBizDataqMapper.selectImBizDataqList(imBizDataq);
    }

    @Override
    public List<ImBizDataq> pageList(ImBizDataq imBizDataq) {
        imBizDataq = dataFormatYYYYMMDDHHMMSS(imBizDataq);
        return imBizDataqMapper.pageList(imBizDataq);
    }

    @Override
    public List<ImBizDataq> exprot(ImBizDataq imBizDataq) {
        imBizDataq = dataFormatYYYYMMDDHHMMSS(imBizDataq);//日期格式化
        List<ImBizDataq> list = new ArrayList<>();
        String isCurrentPage = (String) imBizDataq.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            list = imBizDataqService.selectImBizDataqList(imBizDataq);

        } else {
            list = imBizDataqService.selectImBizDataqList(imBizDataq);

        }
        return list;
    }

    @Override
    public AjaxResult addSave(ImBizDataq imBizDataq) {
        removeBusinessRepeat(imBizDataq);
        try {
            String userId = ShiroUtils.getUserId();//获取当前登陆人
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("userId", userId);
            reMap.put("createId", userId);
            if ("1".equals(imBizDataq.getIsState())) {
                reMap.put("reCode", "0");
                reMap.put("shouliId", userId);
                imBizDataq.setCurrentState("11");//业务受理中
            } else {
                reMap.put("reCode", "1");
                reMap.put("groupId", ImBizIssueConstants.ROLE_WTFENFA);
                imBizDataq.setCurrentState("02");//待分发
            }
            /*if ("1".equals(imBizDataq.getIfJs())) {//判断是否走技术

                reMap.put("groupId", ImBizIssueConstants.ROLE_WTFENFA);
                imBizDataq.setCurrentState("02");
            } else {
                reMap.put("reCode", "0");
                reMap.put("pingguId", imBizDataq.getAssessorId());
                imBizDataq.setCurrentState("05");
            }*/
            updateImBizDataq(imBizDataq);
            activitiCommService.startProcess(imBizDataq.getImId(), processDefinitionKey, reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("数据问题单创建失败。");
        }
        return AjaxResult.success("问题单号为：" + imBizDataq.getImNo() + "的单子创建成功。");
    }

    @Override
    public ModelMap editView(String imId, ModelMap mmap) {
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqById(imId);
        //获取taskID
        String description = "edit";//取出是什么任务类型
        List<Map<String, Object>> reList1 = activitiCommService.userTask(processDefinitionKey, description);
        for (Map<String, Object> map : reList1) {
            if (!StringUtils.isEmpty(map.get("businesskey"))) {
                String business_key = map.get("businesskey").toString();
                if (StringUtils.isNotEmpty(business_key)) {
                    if(imId.equals(business_key)){
                        if (imBizDataq != null) {
                            Map<String, Object> reMap = new HashMap<>();
                            reMap.put("taskId", map.get("taskId").toString());
                            mmap.put("imBizDataq", imBizDataq);
                            mmap.put("taskId", reMap.get("taskId"));
                            imBizDataq.setParams(reMap);
                        }
                    }

                }
            }
        }

        //获取创建人名称
        OgPerson person = ogPersonService.selectOgPersonById(imBizDataq.getCreaterId());
        if (!StringUtils.isEmpty(person)) {
            mmap.put("createName", person.getpName());
        }
        imBizDataq = objectFormat(imBizDataq);

        List<Map> list = imBizDataqService.getBusinessList(imBizDataq);
        if (StringUtils.isNotEmpty(imBizDataq.getBusinessDept())) {
            String businessOrg = imBizDataq.getBusinessDept();
            String[] orgs = Convert.toStrArray(businessOrg);
            imBizDataq.setBusinessDept(orgs[0]);
        }
        mmap.put("businessList", list);


        return mmap;
    }


    @Override
    public ModelMap view(String imId, ModelMap mmap) {
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqById(imId);
        mmap.put("imBizDataq", imBizDataq);
        //获取创建人名称
        OgPerson person = ogPersonService.selectOgPersonById(imBizDataq.getCreaterId());
        if (!StringUtils.isEmpty(person)) {
            mmap.put("createName", person.getpName());
        }
        objectFormat(imBizDataq);
        return mmap;
    }

    @Override
    public ModelMap xgsjwtsh(String imNo, ModelMap modelMap) {

        //根据问题单号查询数据问题单
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqByNo(imNo);

        List<Map> list = imBizDataqService.getBusinessList(imBizDataq);
        if (StringUtils.isNotEmpty(imBizDataq.getBusinessDept())) {
            String businessOrg = imBizDataq.getBusinessDept();
            String[] orgs = Convert.toStrArray(businessOrg);
            imBizDataq.setBusinessDept(orgs[0]);
        }
        modelMap.put("businessList", list);

        if (!StringUtils.isEmpty(imBizDataq)) {
            modelMap.put("imBizDataq", imBizDataq);
            //获取创建人名称
            OgPerson person = ogPersonService.selectOgPersonById(imBizDataq.getCreaterId());
            if (!StringUtils.isEmpty(person)) {
                modelMap.put("createName", person.getpName());
            }
            //创建时间进行日期回显
            String creatTime = imBizDataq.getCreatTime();
            if (StringUtils.isNotEmpty(creatTime)) {
                imBizDataq.setCreatTime(DateUtils.formatDateStr(creatTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM_SS));
            }

            //问题发生时间进行日期回显
            String problemDiscoveryTime = imBizDataq.getProblemDiscoveryTime();
            if (StringUtils.isNotEmpty(problemDiscoveryTime)) {
                imBizDataq.setProblemDiscoveryTime(DateUtils.formatDateStr(problemDiscoveryTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM_SS));
            }

            //业务计划日期回显
            String businessPlanTime = imBizDataq.getBusinessPlanTime();
            if (StringUtils.isNotEmpty(businessPlanTime)) {
                imBizDataq.setBusinessPlanTime(DateUtils.formatDateStr(businessPlanTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM_SS));
            }
        }


        return modelMap;
    }


    @Override
    public List<Map> getBusinessList(ImBizDataq imBizDataq) {
        convertBusinessOrg(imBizDataq);
        //业务部门
        String businessOrg = imBizDataq.getBusinessDept();
        //经办人
        String auditIds = imBizDataq.getBusinessId();
        //部门领导
        String approvaId = imBizDataq.getBusinessAppid();
        //分管行长
        String charship = imBizDataq.getChargeLeadership();
        List<Map> list = new ArrayList<>();
        if (StringUtils.isEmpty(businessOrg)) {
            return list;
        }
        // 业务部门及审核人信息回显
        if (StringUtils.isNotEmpty(businessOrg) && StringUtils.isNotEmpty(auditIds) && StringUtils.isNotEmpty(approvaId) && StringUtils.isNotEmpty(charship)) {
            String[] businessOrgs = businessOrg.split(",");
            String[] aduitIds = auditIds.split(",");
            String[] approvalIds = approvaId.split(",", -1);
            String[] chargeLeaderships = charship.split(",", -1);

            for (int i = 0; i < businessOrgs.length; i++) {
                Map map = new HashMap<String, String>();
                String busOrg = businessOrgs[i];
                String auditId = aduitIds[i];

                if (!"".equals(approvalIds) && approvalIds != null) {
                    String approvalId = approvalIds[i];
                    OgPerson p2 = ogPersonService.selectOgPersonById(approvalId);
                    if (null != p2) {
                        String approvalName = p2.getpName();
                        map.put("businessApprovalName", approvalName);
                    } else {
                        String approvalName = "";
                        map.put("businessApprovalName", approvalName);
                    }
                    map.put("businessApprovalId", approvalId);
                }

                if (!"".equals(chargeLeaderships) && chargeLeaderships != null) {
                    String chargeLeadership = chargeLeaderships[i];
                    map.put("chargeLeadership", chargeLeadership);
                } else {
                    String chargeLeadership = "";
                    map.put("chargeLeadership", chargeLeadership);
                }


                OgPerson p1 = ogPersonService.selectOgPersonById(auditId);
                if (null == p1) {
                    return list;
                }
                String auditName = p1.getpName();
                map.put("businessOrgName", busOrg);
                map.put("businessAuditName", auditName);
                map.put("businessAuditId", auditId);
                list.add(map);
            }
        } else if (StringUtils.isEmpty(approvaId) && StringUtils.isEmpty(charship)) {
            String[] businessOrgs = businessOrg.split(",");
            String[] aduitIds = auditIds.split(",");
            for (int i = 0; i < businessOrgs.length; i++) {
                Map map = new HashMap<String, String>();
                String busOrg = businessOrgs[i];
                String auditId = aduitIds[i];
                OgPerson p1 = ogPersonService.selectOgPersonById(auditId);
                if (null == p1) {
                    return list;
                }
                String auditName = p1.getpName();
                map.put("businessOrgName", busOrg);
                map.put("businessAuditName", auditName);
                map.put("businessAuditId", auditId);
                list.add(map);
            }
        } else if (StringUtils.isEmpty(approvaId)) {
            String[] businessOrgs = businessOrg.split(",");
            String[] aduitIds = auditIds.split(",");
            String[] chargeLeaderships = charship.split(",", -1);

            for (int i = 0; i < businessOrgs.length; i++) {
                Map map = new HashMap<String, String>();
                String busOrg = businessOrgs[i];
                String auditId = aduitIds[i];
                String chargeLeadership = chargeLeaderships[i];
                OgPerson p1 = ogPersonService.selectOgPersonById(auditId);
                if (null == p1) {
                    return list;
                }
                String auditName = p1.getpName();
                map.put("businessOrgName", busOrg);
                map.put("businessAuditName", auditName);
                map.put("businessAuditId", auditId);
                map.put("chargeLeadership", chargeLeadership);
                list.add(map);
            }
        } else if (StringUtils.isEmpty(charship)) {
            String[] businessOrgs = businessOrg.split(",");
            String[] aduitIds = auditIds.split(",");
            String[] approvalIds = approvaId.split(",", -1);
            for (int i = 0; i < businessOrgs.length; i++) {
                Map map = new HashMap<String, String>();
                String busOrg = businessOrgs[i];
                String auditId = aduitIds[i];
                String approvalId = approvalIds[i];
                OgPerson p1 = ogPersonService.selectOgPersonById(auditId);
                if (null == p1) {
                    return list;
                }
                OgPerson p2 = ogPersonService.selectOgPersonById(approvalId);
                if (null == p2) {
                    return list;
                }
                String auditName = p1.getpName();
                String approvalName = p2.getpName();
                map.put("businessOrgName", busOrg);
                map.put("businessAuditName", auditName);
                map.put("businessApprovalName", approvalName);
                map.put("businessAuditId", auditId);
                map.put("businessApprovalId", approvalId);
                list.add(map);
            }
        }


        // 判断集合中的元素，如果业务部门｜审核人｜审核领导全部相同，则过滤掉
        if (!list.isEmpty() && list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (list.get(j).get("businessOrgName").equals(list.get(i).get("businessOrgName"))
                            && list.get(j).get("businessAuditName").equals(list.get(i).get("businessAuditName"))
                            && list.get(j).get("businessApprovalName").equals(list.get(i).get("businessApprovalName"))) {
                        list.remove(j);
                    }
                }
            }
        }
        return list;
    }


    /**
     * 业务部门转换  存储时名称转id，显示id转名称
     *
     * @param imBizDataq
     */
    public void convertBusinessOrg(ImBizDataq imBizDataq) {
        String businessOrgStr = "";
        if (StringUtils.isNotEmpty(imBizDataq.getBusinessDept())) {
            String[] businessOrgs = Convert.toStrArray(imBizDataq.getBusinessDept());
            for (String businessOrg : businessOrgs) {
                OgOrg org = null;
                if (StringUtils.hasChinese(businessOrg)) {
                    org = iSysDeptService.selectDeptByOrgName(businessOrg);
                    businessOrgStr += org.getOrgId() + ",";
                } else {
                    org = iSysDeptService.selectDeptById(businessOrg);
                    if (null != org) {
                        businessOrgStr += org.getOrgName() + ",";
                    } else {
                        businessOrgStr += VmDept.getOrgName(businessOrg) + ",";
                    }
                }
            }
            imBizDataq.setBusinessDept(StringUtils.removeLastChar(businessOrgStr));
        }
    }

    @Override
    public List<ImBizDataq> getBenchTaskList(ImBizDataq imBizDataq) {

        /*if (StringUtils.isNotEmpty(cmBizSingle.getCreatetime())) {
            cmBizSingle.setCreatetime(handleTimeYYYYMMDD(cmBizSingle.getCreatetime()));
        }*/
        List<ImBizDataq> resultlist = new ArrayList<>();
        String description = imBizDataq.getParams().get("description").toString();//取出是什么任务类型
        List<Map<String, Object>> reList1 = activitiCommService.userTask(processDefinitionKey, description);
        if ("fenfa".equals(description)) {//如果是分发有组任务
            reList1 = activitiCommService.groupTasks(processDefinitionKey, description);
        }
        if ("shouli".equals(description)) {
            List<Map<String, Object>> reList2 = activitiCommService.userTask(processDefinitionKey, "edit2");
            reList1.addAll(reList2);
        }
        for (Map<String, Object> map : reList1) {
            if (!StringUtils.isEmpty(map.get("businesskey"))) {
                String business_key = map.get("businesskey").toString();
                if (StringUtils.isNotEmpty(business_key)) {
                    imBizDataq.setImId(business_key);
                    ImBizDataq ibd = imBizDataqService.selectImBizDataqById(business_key);
                    if (ibd != null) {
                        Map<String, Object> reMap = new HashMap<>();
                        reMap.put("taskId", map.get("taskId").toString());
                        ibd.setParams(reMap);
                        resultlist.add(ibd);
                    }
                }
            }
        }
        return resultlist;
    }

    @Override
    public AjaxResult saveflowAssessor(ImBizDataq imBizDataq) {
        if (!"05".equals(imBizDataq.getCurrentState())) {
            throw new BusinessException("问题单已被处理，请刷新列表重新操作。");
        }
        ImBizDataq ibd = new ImBizDataq();
        ibd.setImId(imBizDataq.getImId());

        Map<String, Object> reMap = formatMap(imBizDataq);
        reMap.put("comment", imBizDataq.getParams().get("comment"));
        String reCode = imBizDataq.getParams().get("reCode").toString();//取出流程走向
        reMap.put("reCode", reCode);
        if ("0".equals(reCode)) {
            ibd.setCurrentState("06");
            reMap.put("createId", imBizDataq.getCreaterId());
        } else {
            if ("1".equals(imBizDataq.getIfJs())) {//技术受理的退回给技术
                reMap.put("shouliId", imBizDataq.getTechnicalId());
                ibd.setCurrentState("04");
            } else {//退回给业务
                reMap.put("shouliId", imBizDataq.getCreaterId());
                ibd.setCurrentState("11");
            }
        }
        try {
            updateImBizDataq(ibd);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("问题单评估操作失败，" + e.getMessage());
        }
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子评估成功。");
    }

    @Override
    public AjaxResult saveflowCloseOrFenFa(ImBizDataq imBizDataq) {
        if (!"02".equals(imBizDataq.getCurrentState()) && !"06".equals(imBizDataq.getCurrentState())) {
            throw new BusinessException("问题单操作失败，请刷新列表重新操作或联系系统管理员。");
        }
        Map<String, Object> reMap = formatMap(imBizDataq);
        ImBizDataq ibd = new ImBizDataq();
        ibd.setImId(imBizDataq.getImId());
        if ("02".equals(imBizDataq.getCurrentState())) {
            ibd.setCurrentState("03");
            reMap.put("dealId", imBizDataq.getTechnicalId());
        }
        if ("06".equals(imBizDataq.getCurrentState())) {
            ibd.setCurrentState("07");
        }

        reMap.put("reCode", "0");
        reMap.put("comment", imBizDataq.getParams().get("comment"));
        try {
            updateImBizDataq(ibd);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("问题单评估操作失败，" + e.getMessage());
        }
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子操作成功。");
    }

    public Map<String, Object> formatMap(ImBizDataq imBizDataq) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", imBizDataq.getImId());
        reMap.put("processDefinitionKey", processDefinitionKey);
        reMap.put("taskId", imBizDataq.getParams().get("taskId"));
        reMap.put("userId", ShiroUtils.getUserId());
        return reMap;
    }

    @Override
    public AjaxResult saveAccept(ImBizDataq imBizDataq) {
        if (!"03".equals(imBizDataq.getCurrentState())) {
            throw new BusinessException("问题单操作失败，请刷新列表重新操作或联系系统管理员。");
        }
        ImBizDataq ibd = new ImBizDataq();
        ibd.setImId(imBizDataq.getImId());
        ibd.setTechnicalRectificationPlan(imBizDataq.getTechnicalRectificationPlan());
        ibd.setCurrentState("04");
        updateImBizDataq(ibd);
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子受理成功。");
    }

    @Override
    public AjaxResult saveFlowJj(ImBizDataq imBizDataq) {
        ImBizDataq ibd = new ImBizDataq();
        Map<String, Object> reMap = formatMap(imBizDataq);
        if ("11".equals(imBizDataq.getCurrentState())) {
            ibd.setBusinessSolutions(imBizDataq.getBusinessSolutions());
            String comment = imBizDataq.getBusinessSolutions();
            reMap.put("comment", comment);
            if ("1".equals(imBizDataq.getIfJs())) {
                ibd.setCurrentState("02");
                reMap.put("reCode", "1");
                reMap.put("groupId", ImBizIssueConstants.ROLE_WTFENFA);
            } else {
                reMap.put("reCode", "0");
                ibd.setCurrentState("05");
                reMap.put("pingguId", imBizDataq.getAssessorId());
            }
        } else if ("04".equals(imBizDataq.getCurrentState())) {
            ibd.setTechnicalSolutions(imBizDataq.getTechnicalSolutions());
            ibd.setVersionNo(imBizDataq.getVersionNo());
            reMap.put("reCode", "0");
            ibd.setCurrentState("05");
            reMap.put("pingguId", imBizDataq.getAssessorId());
        } else {
            throw new BusinessException("问题单操作失败，请刷新列表重新操作或联系系统管理员。");
        }
        try {
            ibd.setImId(imBizDataq.getImId());
            updateImBizDataq(ibd);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("数据问题单操作失败，请联系统管理员。");
        }
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子解决成功。");
    }

    @Override
    public AjaxResult saveFlowZf(ImBizDataq imBizDataq) {
        if (!"03".equals(imBizDataq.getCurrentState())) {
            throw new BusinessException("问题单操作失败，请刷新列表重新操作或联系系统管理员。");
        }
        Map<String, Object> reMap = formatMap(imBizDataq);
        try {
            ImBizDataq ibd = new ImBizDataq();
            ibd.setImId(imBizDataq.getImId());
            ibd.setTechnicalId(imBizDataq.getTechnicalId());
            updateImBizDataq(ibd);

            reMap.put("reCode", "1");
            reMap.put("dealId", ibd.getTechnicalId());
            reMap.put("comment", imBizDataq.getParams().get("comment"));
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("数据问题单转发失败，请联系统管理员。");
        }
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子转发成功。");
    }

    @Override
    public AjaxResult saveFlowTuihui(ImBizDataq imBizDataq) {
        if (!"03".equals(imBizDataq.getCurrentState())) {
            throw new BusinessException("问题单操作失败，请刷新列表重新操作或联系系统管理员。");
        }
        Map<String, Object> reMap = formatMap(imBizDataq);
        try {
            ImBizDataq ibd = new ImBizDataq();
            ibd.setImId(imBizDataq.getImId());
            ibd.setCurrentState("09");
            updateImBizDataq(ibd);

            reMap.put("reCode", "2");
            reMap.put("createId", imBizDataq.getCreaterId());
            reMap.put("comment", imBizDataq.getParams().get("comment"));
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("数据问题单退回失败，请联系统管理员。");
        }
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子退回成功。");
    }

    @Override
    public AjaxResult addSjwtSh(ImBizDataq imBizDataq) {
        //总行业务审批页面新增数据问题单
        //状态为待提交
        imBizDataq.setCurrentState("01");
        //创建人
        imBizDataq.setCreaterId(ShiroUtils.getUserId());
        //有效标志
        imBizDataq.setInvalidationMark("1");
        removeBusinessRepeat(imBizDataq);

        //日期格式转换
        saveFormatTime(imBizDataq);
        imBizDataqMapper.insertImBizDataq(imBizDataq);

        return AjaxResult.success("操作成功");
    }


    /**
     * 添加或修改保存时日期格式转换
     *
     * @param imBizDataq
     * @return
     */
    public static ImBizDataq saveFormatTime(ImBizDataq imBizDataq) {
        //日期格式转换
        if (StringUtils.isNotEmpty(imBizDataq.getCreatTime())) {
            imBizDataq.setCreatTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getCreatTime()));
        }
        if (StringUtils.isNotEmpty(imBizDataq.getProblemDiscoveryTime())) {
            imBizDataq.setProblemDiscoveryTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getProblemDiscoveryTime()));
        }
        if (StringUtils.isNotEmpty(imBizDataq.getBusinessPlanTime())) {
            imBizDataq.setBusinessPlanTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getBusinessPlanTime()));
        }
        if (StringUtils.isNotEmpty(imBizDataq.getObservationTime())) {
            imBizDataq.setObservationTime(DateUtils.handleTimeYYYYMMDDHHMMSS(imBizDataq.getObservationTime()));
        }
        return imBizDataq;
    }

    @Override
    public AjaxResult saveFlowPlFenFa(ImBizDataq imBizDataq) {
        String imId = imBizDataq.getImId();
        String[] imIds = Convert.toStrArray(imId);
        String userId = ShiroUtils.getUserId();
        String comment = (String) imBizDataq.getParams().get("comment");
        try {
            for (String id : imIds) {

                ImBizDataq ibd = new ImBizDataq();
                ibd.setImId(id);
                ibd.setCurrentState("03");
                updateImBizDataq(ibd);

                Map<String, Object> reMap = new HashedMap();
                reMap.put("businessKey", id);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("userId", userId);
                reMap.put("comment", comment);
                reMap.put("reCode", "0");
                reMap.put("dealId", imBizDataqService.selectImBizDataqById(id).getTechnicalId());
                activitiCommService.complete(reMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("数据问题单分发失败，请联系统管理员。");
        }
        return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子分发成功。");
    }

    @Override
    public List<ImBizDataq> pageListBg(ImBizDataq imBizDataq) {
        return imBizDataqMapper.pageListBg(imBizDataq);
    }

    /**
     * 我的数据问题单修改保存数据问题单
     * @param imBizDataq
     * @return
     */
    @Override
    public AjaxResult saveOrCancellation(ImBizDataq imBizDataq) {
        if (!"09".equals(imBizDataq.getCurrentState())) {
            throw new BusinessException("问题单操作失败，请刷新列表重新操作或联系系统管理员。");
        }
        Map<String, Object> reMap = formatMap(imBizDataq);
        //判断当前是【重新提交】还是【作废】
        if("commit".equals(imBizDataq.getWtflag())){    //重新提交
            try {
                ImBizDataq ibd = new ImBizDataq();
                ibd.setImId(imBizDataq.getImId());
                ibd.setCurrentState("03");
                updateImBizDataq(ibd);
                reMap.put("dealId", imBizDataq.getTechnicalId());
                reMap.put("reCode", "0");
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("数据问题单提交失败，请联系统管理员。");
            }
            return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子退回成功。");

        }else {     //作废
            try {
                ImBizDataq ibd = new ImBizDataq();
                ibd.setImId(imBizDataq.getImId());
                ibd.setCurrentState("08");
                updateImBizDataq(ibd);
                reMap.put("reCode", "1");
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("数据问题单作废失败，请联系统管理员。");
            }
            return AjaxResult.success("问题单号：" + imBizDataq.getImNo() + "的单子作废成功。");
        }
    }

}