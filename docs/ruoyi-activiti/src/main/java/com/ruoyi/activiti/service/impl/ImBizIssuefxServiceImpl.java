package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.constants.ImBizIssueConstants;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.mapper.ImBizIssuefxMapper;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.common.constant.DictConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.mapper.KnowledgeTitleMapper;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问题信息 服务层处理
 *
 * @author dayong_sun
 */
@Service
public class ImBizIssuefxServiceImpl implements IImBizIssuefxService {
    private static final Logger log = LoggerFactory.getLogger(ImBizIssuefxServiceImpl.class);
    @Autowired
    private ImBizIssuefxMapper issueListMapper;
    @Autowired
    ISysDeptService iSysDeptService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IOgUserService ogUserService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;
    @Autowired(required=false)
    private KnowledgeTitleMapper knowledgeTitleMapper;
    @Autowired
    private ISysDeptService deptService;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * a
     * 查询数据集合
     *
     * @return 问题数据集合
     */
    @Override
    public List<ImBizIssuefx> selectIssueList(ImBizIssuefx issue) {
        List<ImBizIssuefx> list;
        if ("mysql".equals(dbType)) {
            list = issueListMapper.selectImBizIssuefxListMysql(issue);
        } else {
            list = issueListMapper.selectImBizIssuefxList(issue);
        }
        return list;
    }

    /**
     * 查询关联事件单List
     *
     * @return 问题数据集合
     */
    @Override
    public List<ImBizIssuefx> selectRelationFmbizList(ImBizIssuefx issue) {
        List<ImBizIssuefx> list;
        if ("mysql".equals(dbType)) {
            list = issueListMapper.selectRelationFmbizListMysql(issue);
        } else {
            list = issueListMapper.selectRelationFmbizList(issue);
        }
        return list;
    }

    /**
     * 通过问题ID查询问题信息
     *
     * @param issueId 账号ID
     * @return 问题信息
     */
    @Override
    public ImBizIssuefx selectImBizIssuefxById(String issueId) {
        ImBizIssuefx iif;
        if ("mysql".equals(dbType)) {
            iif = issueListMapper.selectImBizIssuefxByIdMysql(issueId);
        } else {
            iif = issueListMapper.selectImBizIssuefxById(issueId);
        }
        return iif;
    }

    /**
     * 批量删除问题信息
     *
     * @param issueIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIssueByIds(String issueIds) {
        Long[] Ids = Convert.toLongArray(issueIds);
        return issueListMapper.deleteIssueByIds(Ids);
    }

    /**
     * 批量作废问题信息
     *
     * @param issueIds 需要作废的数据ID
     * @return 结果
     */
    @Override
    public int updateInBatchById(String issueIds) {
        Long[] Ids = Convert.toLongArray(issueIds);
        return issueListMapper.updateInBatchById(Ids);
    }

    /**
     * 修改问题信息
     *
     * @param issue 问题信息
     * @return 结果
     */
    @Override
    public int updateIssue(ImBizIssuefx issue) {
        if (StringUtils.isNotEmpty(issue.getSolutionTime())) {
            issue.setSolutionTime(DateUtils.handleTimeYYYYMMDDHHMMSS(issue.getSolutionTime()));
        }
        if (StringUtils.isNotEmpty(issue.getCreatTime())) {
            issue.setCreatTime(DateUtils.handleTimeYYYYMMDDHHMMSS(issue.getCreatTime()));
        }
        if (StringUtils.isNotEmpty(issue.getReporttime())) {
            issue.setReporttime(DateUtils.handleTimeYYYYMMDDHHMMSS(issue.getReporttime()));
        }
        if (StringUtils.isNotEmpty(issue.getExpectTime())) {
            issue.setExpectTime(DateUtils.handleTimeYYYYMMDDHHMMSS(issue.getExpectTime()));
        }
        return issueListMapper.updateImBizIssuefx(issue);
    }

    /**
     * 新增问题信息
     *
     * @param issue 问题信息
     * @return 结果
     */
    @Override
    public int insertIssue(ImBizIssuefx issue) {
        issue.setCreatTime(DateUtils.dateTimeNow());
        return issueListMapper.insertImBizIssuefx(issue);
    }

    /**
     * 修改问题信息
     *
     * @param issue 问题信息
     * @return 结果
     */
    @Override
    public int changeStatus(ImBizIssuefx issue) {
        return issueListMapper.changeStatus(issue);
    }

    /**
     * 根据条件查询用户列表
     *
     * @param ogPerson 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<OgPerson> selectOgPersonList(OgPerson ogPerson) {
        return issueListMapper.selectOgPersonList(ogPerson);
    }

    /**
     * 查询应用系统
     */
    @Override
    public List<OgSys> selectOgSysList(OgSys sys) {
        List<OgSys> ogSysList = issueListMapper.selectOgSysList(sys);
        if (!"0".equals(sys.getSysId())) {
            String[] orgIds = Convert.toStrArray(sys.getSysId());
            for (OgSys ogSys : ogSysList) {
                for (String orgId : orgIds) {
                    if (ogSys.getSysId().equals(orgId)) {
                        ogSys.setFlag(true);
                        break;
                    }
                }
            }
        }
        return ogSysList;
    }

    /**
     * 查询协同评估人/技术经理
     */
    @Override
    public List<OgPerson> selectMultiusers(String userIds, OgPerson ogPerson) {
        if (!"8b8080f457fffe39015800015ce60006".equals(ShiroUtils.getOgUser().getpId())) {
            ogPerson.setpId(ShiroUtils.getOgUser().getpId());
        }
        List<OgPerson> ogPersonList = selectMultiusers(ogPerson);
        if (!"0".equals(userIds)) {
            String[] uIds = Convert.toStrArray(userIds);
            for (OgPerson op : ogPersonList) {
                for (String uId : uIds) {
                    if (op.getpId().equals(uId)) {
                        op.setFlag(true);
                        break;
                    }
                }
            }
        }
        return ogPersonList;
    }

    /**
     * 查询数据中心人员
     * -兼容mysql：列转行、树状查询 -- modify by sunxf on 2022.3.4
     * OG_ORG.orgid：组织id
     * OG_ORG.t_o_orgid：上级组织id
     * -原oracle树查询sql
     * * 		<if test="pId != null and pId!=''">
     * * 			START WITH a.orgid=(select orgid from OG_PERSON where pid = #{pId} and INVALIDATION_MARK = '1')
     * * 			CONNECT BY PRIOR a.orgid=a.t_o_orgid
     * * 		</if>
     */
    @Override
    public List<OgPerson> selectMultiusers(OgPerson ogPerson) {
        if ("mysql".equals(dbType)) {
            log.debug("selectMultiusers-入参-->{}", ogPerson);
            //获取人员（参数太多了，导致查不出数据，只取id即可）
            OgPerson ogPerson2 = new OgPerson();
            if (ogPerson.getUserid() != null) {
                ogPerson2.setUserid(ogPerson.getUserid());
            } else {
                ogPerson2 = ogPerson;
            }
            List<OgPerson> ogPersonList = issueListMapper.selectMultiusersMysql(ogPerson2);
            //递归获取下级组织
            selectChildOrg(ogPersonList, ogPerson.getOrgId());
            log.debug("selectMultiusers-最终获取人员数量-->【{}】", ogPersonList.size());
            for (OgPerson ogPerson1 : ogPersonList) {
                log.debug("selectMultiusers-最终获取人员-->【{}】", ogPerson1);
            }
            return ogPersonList;
        }
        return issueListMapper.selectMultiusers(ogPerson);
    }

    //递归获取下一级组织人员
    private void selectChildOrg(List<OgPerson> ogPersonList, String orgId) {
        log.debug("selectMultiusers-递归获取人员数量【{}】，组织id【{}】", ogPersonList.size(), orgId);
        //获取下一级组织信息--selectDeptListByOrgId
        //List<OgOrg> orgList = iSysDeptService.selectAllChildOrg(orgId);
        OgOrg org1 = new OgOrg();
        org1.setParentId(orgId);
        List<OgOrg> orgList = deptService.selectDeptList(org1);
        if (orgList == null || orgList.size() == 0) {
            return;//结束递归
        }
        //根据下级组织查询人员
        for (OgOrg ogOrg : orgList) {
            log.debug("[{}]下级组织包括-->{}", orgId, ogOrg.getOrgName());
            //根据组织查询人员
            OgPerson ogPerson1 = new OgPerson();
            ogPerson1.setOrgId(ogOrg.getOrgId());
            List<OgPerson> ogPersonList1 = issueListMapper.selectMultiusersMysql(ogPerson1);
            ogPersonList.addAll(ogPersonList1);
            //继续递归
            selectChildOrg(ogPersonList, ogOrg.getOrgId());
        }
    }


    @Override
    public String selectOgPersonById(String pId) {
        return issueListMapper.selectOgPersonById(pId);
    }

    /**
     * 删除wenti对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteImBizIssuefxByIds(String ids) {
        return issueListMapper.deleteImBizIssuefxByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<PubParaValue> selectIssuesourceList(String source) {
        return issueListMapper.selectIssuesourceList(source);
    }

    @Override
    public List<OgOrg> selectOgOrgList() {
        return issueListMapper.selectOgOrg();
    }

    /**
     * 详情
     *
     * @param issuefxId
     * @param mmap
     * @return zx
     */
    @Override
    public ModelMap view(String issuefxId, ModelMap mmap) {
        ImBizIssuefx issue = selectImBizIssuefxById(issuefxId);
        //获取字典项
        List<PubParaValue> issuesources = selectIssuesourceList(DictConstants.WT_SOUCE);//问题来源
        List<PubParaValue> issueFenleis = selectIssuesourceList(DictConstants.WT_FENLEI);//问题分类
        List<PubParaValue> issueTypes = selectIssuesourceList(DictConstants.ISSUE_TYPE);//问题类型
        List<PubParaValue> seriousLevs = selectIssuesourceList(DictConstants.ISSUE_SERIOUS_LEV);//问题等级
        List<PubParaValue> businessOrgs = selectIssuesourceList(DictConstants.VM_DEPT);//业务部门
        List<OgOrg> issueOrgs = selectOgOrgList();
        List<OgPerson> userlist = new ArrayList<>();
        List<OgPerson> buslist = new ArrayList<>();
        List<Map<String, Object>> depList = new ArrayList<>();
        if (StringUtils.isNotEmpty(issue.getBusinessId())) {
            String business[] = issue.getBusinessId().split(",");

        }
        if (!CollectionUtils.isEmpty(businessOrgs)) {
            String valueDetail = issue.getBusinessOrg();
            if (StringUtils.isNotEmpty(valueDetail)) {
                String detail[] = valueDetail.split(",");
                for (int i = 0; i < detail.length; i++) {
                    Map<String, Object> map = new HashMap();
                    OgPerson ogPerson = new OgPerson();
                    ogPerson.setrId(ImBizIssueConstants.ROLE_YWFUHE);
                    String name = iPubParaValueService.selectPubParaValueByNameValue(ImBizIssueConstants.VM_DEPT, detail[i]);
                    ogPerson.setOrgname(name);
                    buslist = commonService.selectPersonByOrgAndRole(ogPerson);
                    map.put("detail", detail[i]);
                    if (StringUtils.isNotEmpty(issue.getBusinessId())) {
                        String business[] = issue.getBusinessId().split(",");
                        if (i > business.length - 1) {
                            map.put("userId", "");
                        } else {
                            if (!"0".equals(issue.getCurrentState())) {
                                ogPerson.setpId(business[i]);
                                List<OgPerson> bus = commonService.selectPersonByOrgAndRole(ogPerson);
                                //如果人员无效 或角色岗位发生变化
                                if (CollectionUtils.isEmpty(bus)) {
                                    buslist.add(iOgPersonService.selectOgPersonEvoById(business[i]));
                                }
                            }
                            map.put("userId", business[i]);
                        }
                    } else {
                        map.put("userId", "");
                    }
                    map.put("buslist", buslist);
                    depList.add(map);
                }
            }

        }

        if (null != issueOrgs && issueOrgs.size() > 0) {
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgId(issue.getIssueOrg());
            ogPerson.setrId(ImBizIssueConstants.ROLE_SHENGHE);
            userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        //问题发现人
       /* OgUser reportname=ogUserService.selectOgUserByUserId(issue.getReportname());
        mmap.addAttribute("reportnameName", reportname.getPname());*/
        //技术经理
        OgUser auditId = ogUserService.selectOgUserByUserId(issue.getAuditId());
        if (auditId != null) {
            mmap.addAttribute("auditIdName", auditId.getPname());
        }
        //协同评估人
        if (StringUtils.isNotEmpty(issue.getMultiusers())) {
            String[] muls = issue.getMultiusers().split(",");
            String multiuser = "";
            for (String mu : muls) {
                OgUser multiusers = ogUserService.selectOgUserByUserId(mu);
                multiuser += multiusers.getPname() + ",";
            }

            mmap.addAttribute("multiusersName", multiuser.substring(0, multiuser.length() - 1));
        }
        String dealName = "";
        if (StringUtils.isNotEmpty(issue.getDealId())) {
            OgPerson ogPersonDeal = iOgPersonService.selectOgPersonById(issue.getDealId());
            if (ogPersonDeal != null) {
                dealName = ogPersonDeal.getpName();
            }
        }
        mmap.put("dealName", dealName);
        mmap.addAttribute("issuesources", issuesources);
        mmap.addAttribute("issueFenleis", issueFenleis);
        mmap.addAttribute("issueTypes", issueTypes);
        mmap.addAttribute("seriousLevs", seriousLevs);
        mmap.addAttribute("businessOrgs", businessOrgs);
        mmap.addAttribute("buslist", buslist);
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("userlist", userlist);
        mmap.addAttribute("depList", depList);
        mmap.put("issue", issue);

        //
        if (StringUtils.isNotEmpty(issue.getOneType())) {//一级分类名称
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setCategory("1");
            kdt.setEventType("2");
            kdt.setStatus("0");
            String parentId = "WTD0001";
            if (StringUtils.isNotEmpty(issue.getIssueFenlei())) {
                if ("6".equals(issue.getIssueFenlei())) {
                    parentId = "WTD0002";
                }
            }
            kdt.setParentId(parentId);
            mmap.put("oneTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(issue.getTwoType())) {//二级分类
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setParentId(issue.getOneType());
            kdt.setStatus("0");
            kdt.setEventType("2");
            mmap.put("twoTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(issue.getThreeType())) {//三级分类
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setStatus("0");
            kdt.setEventType("2");
            kdt.setParentId(issue.getTwoType());
            mmap.put("threeTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        return mmap;
    }

    /**
     * 查看建设单位报表
     *
     * @return
     */
    @Override
    public List<Map<Object, Object>> selectBulidList(Map<String, Object> map) {
        return issueListMapper.selectBulidList(map);
    }

    /**
     * 查看数据中心报表
     *
     * @return
     */
    @Override
    public List<Map<Object, Object>> statementDataList(Map<String, Object> map) {
        return issueListMapper.selectDataList(map);
    }

    /**
     * 查询我处理过的任务历史
     *
     * @param issue
     * @return
     */
    @Override
    public List<ImBizIssuefx> selectIssueHistroyList(ImBizIssuefx issue) {
        return issueListMapper.selectIssueHistroyList(issue);
    }

    @Override
    public List<ImBizIssuefx> selectTofmbiz(ImBizIssuefx imBizIssuefx) {
        return issueListMapper.selectTofmbiz(imBizIssuefx);
    }

    @Override
    public List<OgPerson> selectIssueByReviewer(OgPerson imBizIssuefx) {
        return issueListMapper.selectIssueByReviewer(imBizIssuefx);
    }

    @Override
    public List<ImBizIssuefx> selectImBizIssuefxByFmNo(String fmNo) {
        return issueListMapper.selectImBizIssuefxByFmNo(fmNo);
    }

    @Override
    public ModelMap getTypeList(ImBizIssuefx imBizIssuefx, ModelMap mmap) {
        if (StringUtils.isNotEmpty(imBizIssuefx.getOneType())) {//一级分类名称
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setCategory("1");
            kdt.setEventType("2");
            kdt.setStatus("0");
            String parentId = "WTD0001";
            if (StringUtils.isNotEmpty(imBizIssuefx.getIssueFenlei())) {
                if ("6".equals(imBizIssuefx.getIssueFenlei())) {
                    parentId = "WTD0002";
                }
            }
            kdt.setParentId(parentId);
            mmap.put("oneTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(imBizIssuefx.getTwoType())) {//二级分类
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setParentId(imBizIssuefx.getOneType());
            kdt.setStatus("0");
            kdt.setEventType("2");
            mmap.put("twoTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        if (StringUtils.isNotEmpty(imBizIssuefx.getThreeType())) {//三级分类
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setStatus("0");
            kdt.setEventType("2");
            kdt.setParentId(imBizIssuefx.getTwoType());
            mmap.put("threeTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
        }
        return mmap;
    }

    /**
     * 查询数据集合 事件单关联问题单时的查询
     *
     * @return 问题数据集合
     */
    @Override
    public List<ImBizIssuefx> listAllForEventSheet(ImBizIssuefx issue) {
        List<ImBizIssuefx> list;
        if ("mysql".equals(dbType)) {
            list = issueListMapper.listAllForEventSheetMysql(issue);
        } else {
            list = issueListMapper.listAllForEventSheet(issue);
        }
        return list;
    }

    public List<Ztree> selectTitleTree(KnowledgeTitle knowledgeTitle) {
        return initZtree(knowledgeTitleMapper.selectTitleTree(knowledgeTitle));
    }

    public List<Ztree> initZtree(List<KnowledgeTitle> categoryList) {
        return initZtree(categoryList, null);
    }

    public List<Ztree> initZtree(List<KnowledgeTitle> categoryList, List<String> roleDeptList) {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (KnowledgeTitle category : categoryList) {
            Ztree ztree = new Ztree();
            ztree.setId(category.getId());
            ztree.setpId(category.getParentId());
            ztree.setName(category.getName());
            ztree.setTitle(category.getName());
            if (isCheck) {
                ztree.setChecked(roleDeptList.contains(category.getId() + category.getName()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 查询问题单关联事件单的个数
     *
     * @param issuefxId 问题单ID
     * @return 结果
     */
    public int selectRelationFmbizCount(String issuefxId) {
        return issueListMapper.selectRelationFmbizCount(issuefxId);
    }
}
