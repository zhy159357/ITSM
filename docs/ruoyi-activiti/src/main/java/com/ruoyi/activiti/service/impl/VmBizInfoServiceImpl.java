package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.mapper.VmBizInfoMapper;
import com.ruoyi.activiti.mapper.VmBizTaskinfoMapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.VmDept;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.http.entegorserver.entity.UploadApproval;
import com.ruoyi.system.http.entegorserver.entity.UploadMsg;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 【版本发布】Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-15
 */
@Service
public class VmBizInfoServiceImpl implements IVmBizInfoService {

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private VmBizInfoMapper vmBizInfoMapper;

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Autowired
    private PubParaValueMapper pubParaValueMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private VmBizTaskinfoMapper taskinfoMapper;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private PubBizPresmsServiceImpl pubBizPresmsService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private IPubAttachmentService attachmentService;

    @Autowired
    private IEntegorServer entegorServer;

    @Autowired
    private ISysNoticeService noticeService;

    private static final Logger log = LoggerFactory.getLogger(VmBizInfoServiceImpl.class);

    /**
     * 查询列表
     *
     * @param vmBizInfo
     * @return
     */
    @Override
    public List<VmBizInfo> selectVmBizInfoList(VmBizInfo vmBizInfo) {
        return vmBizInfoMapper.selectVmBizInfoList(vmBizInfo);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public VmBizInfo selectVmBizInfoById(String id) {
        VmBizInfo vmBizInfo = vmBizInfoMapper.selectVmBizInfoById(id);
        if (vmBizInfo != null) {
            convertVmBizInfoParams(vmBizInfo);
        }
        return vmBizInfo;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public List<VmBizInfo> selectVmBizInfoByIdList(List<String> ids) {
        return vmBizInfoMapper.selectVmBizInfoByIdList(ids);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public VmBizInfo selectVmBizInfoByIdNoConvert(String id) {
        return vmBizInfoMapper.selectVmBizInfoById(id);
    }

    /**
     * 根据版本号查询
     *
     * @param versionInfoNo
     * @return
     */
    @Override
    public VmBizInfo selectVmBizInfoByNo(String versionInfoNo) {
        VmBizInfo vmBizInfo = vmBizInfoMapper.selectVmBizInfoByNo(versionInfoNo);
        if (vmBizInfo == null) {
            return null;
        }
        convertVmBizInfoParams(vmBizInfo);
        return vmBizInfo;
    }

    /**
     * 根据条件查询
     *
     * @param vmBizInfo
     * @return
     */
    @Override
    public VmBizInfo selectVmBizInfoByCondition(VmBizInfo vmBizInfo) {
        return vmBizInfoMapper.selectVmBizInfoByCondition(vmBizInfo);
    }

    /**
     * 定时任务查询用
     *
     * @param id
     * @return
     */
    @Override
    public VmBizInfo selectVmBizInfoByQuartz(String id) {
        return vmBizInfoMapper.selectVmBizInfoByQuartz(id);
    }

    /**
     * 根据版本单号查询总数
     *
     * @param versionInfoNo
     * @return
     */
    @Override
    public int selectCountByVersionInfoNo(String versionInfoNo) {
        return vmBizInfoMapper.selectCountByVersionInfoNo(versionInfoNo);
    }

    /**
     * 新增
     *
     * @param vmBizInfo
     * @return 结果
     */
    @Override
    public int insertVmBizInfo(VmBizInfo vmBizInfo) {
        // forward表示软研接口调用类型
        if (!"forward".equals(vmBizInfo.getParams().get("insertFlag"))) {
            // 新增时校验版本信息表是否存在单号
            int count = this.selectCountByVersionInfoNo(vmBizInfo.getVersionInfoNo());
            if (count > 0) {
                throw new BusinessException("版本单号:" + vmBizInfo.getVersionInfoNo() + "已存在，同一版本单号不可重复保存!");
            }

            vmBizInfo.setVersionInfoId(UUID.getUUIDStr());
            vmBizInfo.setVersionStatus(VersionStatusConstants.VERSION_STATUS_1);
            vmBizInfo.setSendMsgFlag("product");
            this.removeBusinessRepeat(vmBizInfo);
        } else {
            vmBizInfo.setSendMsgFlag("test");
            getVersionInfoName(vmBizInfo);
        }
        vmBizInfo.setVersionCreateTime(DateUtils.dateTimeNow());
        return vmBizInfoMapper.insertVmBizInfo(vmBizInfo);
    }

    /**
     * 业务审核数据去重
     *
     * @param vmBizInfo
     */
    public void removeBusinessRepeat(VmBizInfo vmBizInfo) {
        Map<String, Object> params = vmBizInfo.getParams();
        // 新增｜编辑页面保存|更新|提交数据需要去重业务部门数据
        if ("saveOrSubmit".equals(params.get("repeatFlag")) || "editOrSubmit".equals(params.get("repeatFlag"))) {
            List<Map> list = getBusinessList(vmBizInfo);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            String businessOrg = "";
            String businessAuditId = "";
            String businessApprovalId = "";
            for (Map map : list) {
                businessOrg += map.get("businessOrgName") + ",";
                businessAuditId += map.get("businessAuditId") + ",";
                businessApprovalId += map.get("businessApprovalId") + ",";
            }
            vmBizInfo.setBusinessOrg(StringUtils.removeLastChar(businessOrg));
            vmBizInfo.setBusinessAuditId(StringUtils.removeLastChar(businessAuditId));
            vmBizInfo.setBusinessApprovalId(StringUtils.removeLastChar(businessApprovalId));
        }
    }

    /**
     * 修改
     *
     * @param vmBizInfo
     * @return
     */
    @Override
    public int updateVmBizInfo(VmBizInfo vmBizInfo) {
        vmBizInfo.setUpdate_time(DateUtils.dateTimeNow());
        removeBusinessRepeat(vmBizInfo);
        if (StringUtils.isNotEmpty(vmBizInfo.getStartUpgradeTime())) {
            vmBizInfo.setStartUpgradeTime(handleTimeYYYYMMDDHHMMSS(vmBizInfo.getStartUpgradeTime()));
        }
        if (StringUtils.isNotEmpty(vmBizInfo.getEndUpgradeTime())) {
            vmBizInfo.setEndUpgradeTime(handleTimeYYYYMMDDHHMMSS(vmBizInfo.getEndUpgradeTime()));
        }
        return vmBizInfoMapper.updateVmBizInfo(vmBizInfo);
    }

    /**
     * 删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVmBizInfoByIds(String ids) {
        // 删除版本单的同时删除上传的所有附件
        List<Attachment> attachments = attachmentService.selectAttachmentByOwnerIds(ids);
        attachmentService.deleteAttachment(attachments, false);
        return vmBizInfoMapper.deleteVmBizInfoByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<Map> getBusinessList(VmBizInfo vmBizInfo) {
        convertBusinessOrg(vmBizInfo);
        String businessOrg = vmBizInfo.getBusinessOrg();
        List<Map> list = new ArrayList<>();
        if (StringUtils.isEmpty(businessOrg)) {
            return list;
        }
        // 构造所属业务部门集合数据，纯技术改造不显示业务审核数据
        if (!"1".equals(vmBizInfo.getVersionType())) {
            if (StringUtils.isNotEmpty(businessOrg)) {
                String[] businessOrgs = businessOrg.split(",");
                String[] aduitIds = vmBizInfo.getBusinessAuditId().split(",");
                String[] approvalIds = vmBizInfo.getBusinessApprovalId().split(",");
                for (int i = 0; i < businessOrgs.length; i++) {
                    Map map = new HashMap<String, String>();
                    String busOrg = businessOrgs[i];
                    //vmBizInfo.setBusinessOrg(busOrg);
                    String auditId = aduitIds[i];
                    String approvalId = approvalIds[i];
                    OgPerson p1 = ogPersonMapper.selectOgPersonById(auditId);
                    if (null == p1) {
                        return list;
                    }
                    OgPerson p2 = ogPersonMapper.selectOgPersonById(approvalId);
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
     * 版本名称生成
     *
     * @param vmBizInfo
     * @return
     */
    @Override
    public void getVersionInfoName(VmBizInfo vmBizInfo) {
        String versionInfoName = "";
        if (StringUtils.isNotEmpty(vmBizInfo.getVersionType()) && StringUtils.isNotEmpty(vmBizInfo.getSysId())
                && StringUtils.isNotEmpty(vmBizInfo.getStartUpgradeTime())) {
            String versionType = StringUtils.removeLastChar(vmBizInfo.getVersionType());
            vmBizInfo.setVersionType(versionType);
            List<PubParaValue> valueList = pubParaValueService.selectPubParaValueByVersionType(Convert.toStrArray(versionType));
            String sarr = "";
            for (PubParaValue value : valueList) {
                if (valueList.size() == 1) {
                    sarr = value.getValueDetail();
                } else {
                    sarr += value.getValueDetail() + ",";
                }
            }
            sarr = StringUtils.removeLastChar(sarr);
            OgSys sys = applicationManagerService.selectOgSysBySysId(vmBizInfo.getSysId());
            versionInfoName = "关于" + vmBizInfo.getStartUpgradeTime().replace("-", "").substring(0, 8)
                    + sys.getCaption() + sarr + "的升级";
        }
        if (StringUtils.isNotEmpty(versionInfoName)) {
            vmBizInfo.setVersionInfoName(versionInfoName);
            log.debug("------------软研提交过来的版本单号:[" + vmBizInfo.getVersionInfoNo() + "],版本简称:[" + versionInfoName + "]-----------");
        }
        handleTime(vmBizInfo);
    }

    /**
     * 处理时间统一为不带-
     *
     * @param vmBizInfo
     */
    public void handleTime(VmBizInfo vmBizInfo) {
        if (StringUtils.isNotEmpty(vmBizInfo.getStartUpgradeTime())) {
            vmBizInfo.setStartUpgradeTime(handleTimeYYYYMMDDHHMMSS(vmBizInfo.getStartUpgradeTime()));
        }
        if (StringUtils.isNotEmpty(vmBizInfo.getEndUpgradeTime())) {
            vmBizInfo.setEndUpgradeTime(handleTimeYYYYMMDDHHMMSS(vmBizInfo.getEndUpgradeTime()));
        }
    }

    /**
     * 设置下发分行、技术局、数据中心、版本类型
     *
     * @param vmBizInfo
     * @return
     */
    public void convertVmBizInfoParams(VmBizInfo vmBizInfo) {
        // 下发分行
        String issuedBranch = vmBizInfo.getIssuedBranch();
        if (StringUtils.isNotEmpty(issuedBranch)) {
            String issuedBranchName = "";
            List<OgOrg> ogOrgs = deptMapper.selectDeptByIds(Convert.toStrArray(issuedBranch));
            for (OgOrg branch : ogOrgs) {
                issuedBranchName += branch.getOrgName() + ",";
            }
            vmBizInfo.setIssuedBranchName(issuedBranchName.substring(0, issuedBranchName.length() - 1));
        }

        // 下发技术局
        String issuedUnit = vmBizInfo.getIssuedUnit();
        if (StringUtils.isNotEmpty(issuedUnit)) {
            String issuedUnitName = "";
            List<OgOrg> ogOrgs = deptMapper.selectDeptByIds(Convert.toStrArray(issuedUnit));
            for (OgOrg unit : ogOrgs) {
                issuedUnitName += unit.getOrgName() + ",";
            }
            vmBizInfo.setIssuedUnitName(issuedUnitName.substring(0, issuedUnitName.length() - 1));
        }

        // 总行数据中心
        String agencyCenter = vmBizInfo.getAgencyCenter();
        if (StringUtils.isNotEmpty(agencyCenter)) {
            if (StringUtils.isNotEmpty(vmBizInfo.getAgencyZx())) {
                vmBizInfo.setAgencyCenterName(vmBizInfo.getAgencyZx());
            } else {
                String agencyCenterName = "";
                List<OgOrg> ogOrgs = deptMapper.selectDeptByIds(Convert.toStrArray(agencyCenter));
                for (OgOrg center : ogOrgs) {
                    agencyCenterName += center.getOrgName() + ",";
                }
                vmBizInfo.setAgencyCenterName(agencyCenterName.substring(0, agencyCenterName.length() - 1));
            }
        }

        // 如果自动化标识为空默认赋值为2-否
        if (StringUtils.isEmpty(vmBizInfo.getIsAutomate())) {
            vmBizInfo.setIsAutomate("2");
        }

        convertVersionType(vmBizInfo);
    }

    /**
     * 转换版本类型
     *
     * @param vmBizInfo
     */
    public void convertVersionType(VmBizInfo vmBizInfo) {
        if (StringUtils.isNotEmpty(vmBizInfo.getVersionType())) {
            String versionType = StringUtils.removeLastChar(vmBizInfo.getVersionType());
            List<PubParaValue> valueList = pubParaValueMapper.selectPubParaValueByVersionType(Convert.toStrArray(versionType));
            String versionTypeName = "";
            for (PubParaValue value : valueList) {
                versionTypeName += value.getValueDetail() + ",";
            }
            vmBizInfo.setVersionTypeName(StringUtils.removeLastChar(versionTypeName));
        }
    }

    /**
     * 业务部门转换  存储时名称转id，显示id转名称
     *
     * @param vmBizInfo
     */
    public void convertBusinessOrg(VmBizInfo vmBizInfo) {
        String businessOrgStr = "";
        if (StringUtils.isNotEmpty(vmBizInfo.getBusinessOrg())) {
            String[] businessOrgs = Convert.toStrArray(vmBizInfo.getBusinessOrg());
            for (String businessOrg : businessOrgs) {
                OgOrg org = null;
                if (StringUtils.hasChinese(businessOrg)) {
                    org = deptMapper.selectDeptByOrgName(businessOrg);
                    businessOrgStr += org.getOrgId() + ",";
                } else {
                    org = deptMapper.selectDeptById(businessOrg);
                    if (null != org) {
                        businessOrgStr += org.getOrgName() + ",";
                    } else {
                        businessOrgStr += VmDept.getOrgName(businessOrg) + ",";
                    }
                }
            }
            vmBizInfo.setBusinessOrg(StringUtils.removeLastChar(businessOrgStr));
        }
    }

    @Override
    public Map getOrgMap(VmBizInfo vmBizInfo) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder branchHtml = new StringBuilder();
        StringBuilder unitHtml = new StringBuilder();
        StringBuilder centerHtml = new StringBuilder();
        OgOrg org = new OgOrg();

        /***********************下发分行复选框html start***********************/
        org.setOrgCode("%00088801");
        List<OgOrg> allBranchList = deptMapper.selectDeptByOrgCodeLike(org);
        List<OgOrg> selectBranchList = new ArrayList<>();
        String issuedBranch = vmBizInfo.getIssuedBranch();
        if (StringUtils.isNotEmpty(issuedBranch)) {
            selectBranchList = deptMapper.selectDeptByIds(Convert.toStrArray(issuedBranch));
        }
        // 如果两个集合长度相等则默认全部选中增加checked属性，否则不是选中
        if (allBranchList.size() == selectBranchList.size()) {
            branchHtml.append("<label class=\"checkbox-inline check-box\"><input id=\"branchAll\" type=\"checkbox\" name=\"issuedBranchAll\" onClick=\"selectAllBranch()\" checked>全选</label><br/>");
        } else {
            branchHtml.append("<label class=\"checkbox-inline check-box\"><input id=\"branchAll\" type=\"checkbox\" name=\"issuedBranchAll\" onClick=\"selectAllBranch()\">全选</label><br/>");
        }
        for (int i = 0; i < allBranchList.size(); i++) {
            OgOrg allBranchOrg = allBranchList.get(i);
            boolean flag = false;
            for (int k = 0; k < selectBranchList.size(); k++) {
                if (allBranchOrg.getOrgId().equals(selectBranchList.get(k).getOrgId())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                branchHtml.append("<label class=\"checkbox-inline check-box\">");
                branchHtml.append("<input id=\"issuedBranch" + i + "\" type=\"checkbox\" name=\"issuedBranch\" value=\"" + allBranchOrg.getOrgId() + "\" checked>" + allBranchOrg.getOrgName());
                branchHtml.append("</label>");
            } else {
                branchHtml.append("<label class=\"checkbox-inline check-box\">");
                branchHtml.append("<input id=\"issuedBranch" + i + "\" type=\"checkbox\" name=\"issuedBranch\" value=\"" + allBranchOrg.getOrgId() + "\">" + allBranchOrg.getOrgName());
                branchHtml.append("</label>");
            }

        }
        /***********************下发分行复选框html end***********************/

        /***********************下发分局复选框html start***********************/
        org.setOrgCode("%00088802");
        List<OgOrg> allUnitList = deptMapper.selectDeptByOrgCodeLike(org);
        List<OgOrg> selectUnitList = new ArrayList<>();
        String issuedUnit = vmBizInfo.getIssuedUnit();
        if (StringUtils.isNotEmpty(issuedUnit)) {
            selectUnitList = deptMapper.selectDeptByIds(Convert.toStrArray(issuedUnit));
        }
        // 如果两个集合长度相等则默认全部选中增加checked属性，否则不是选中
        if (allUnitList.size() == selectUnitList.size()) {
            unitHtml.append("<label class=\"checkbox-inline check-box\"><input id=\"unitAll\" type=\"checkbox\" name=\"issuedUnitAll\" onClick=\"selectAllUnit()\" checked>全选</label><br/>");
        } else {
            unitHtml.append("<label class=\"checkbox-inline check-box\"><input id=\"unitAll\" type=\"checkbox\" name=\"issuedUnitAll\" onClick=\"selectAllUnit()\">全选</label><br/>");
        }
        for (int i = 0; i < allUnitList.size(); i++) {
            OgOrg allUnitOrg = allUnitList.get(i);
            boolean flag = false;
            for (int k = 0; k < selectUnitList.size(); k++) {
                if (allUnitOrg.getOrgId().equals(selectUnitList.get(k).getOrgId())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                unitHtml.append("<label class=\"checkbox-inline check-box\">");
                unitHtml.append("<input id=\"issuedUnit" + i + "\" type=\"checkbox\" name=\"issuedUnit\" value=\"" + allUnitOrg.getOrgId() + "\" checked>" + allUnitOrg.getOrgName());
                unitHtml.append("</label>");
            } else {
                unitHtml.append("<label class=\"checkbox-inline check-box\">");
                unitHtml.append("<input id=\"issuedUnit" + i + "\" type=\"checkbox\" name=\"issuedUnit\" value=\"" + allUnitOrg.getOrgId() + "\">" + allUnitOrg.getOrgName());
                unitHtml.append("</label>");
            }
        }
        /***********************下发分局复选框html end***********************/

        /***********************总行数据中心复选框html start***********************/
        org.setOrgCode("010%0888");
        // 内部标记
        org.setInoutsideMark("I");

        List<OgOrg> allCenterList = deptMapper.selectDeptByOrgCodeLike(org);
        List<OgOrg> selectCenterList = new ArrayList<>();
        String agencyCenter = vmBizInfo.getAgencyCenter();
        if (StringUtils.isNotEmpty(agencyCenter)) {
            selectCenterList = deptMapper.selectDeptByIds(Convert.toStrArray(agencyCenter));
        }
        // 如果两个集合长度相等则默认全部选中增加checked属性，否则不是选中
        if (allCenterList.size() == selectCenterList.size()) {
            centerHtml.append("<label class=\"checkbox-inline check-box\"><input id=\"centerAll\" type=\"checkbox\" name=\"agencyCenterAll\" onClick=\"selectAllCenter()\" checked>全选</label><br/>");
        } else {
            centerHtml.append("<label class=\"checkbox-inline check-box\"><input id=\"centerAll\" type=\"checkbox\" name=\"agencyCenterAll\" onClick=\"selectAllCenter()\">全选</label><br/>");
        }
        for (int i = 0; i < allCenterList.size(); i++) {
            OgOrg allCenterOrg = allCenterList.get(i);
            boolean flag = false;
            for (int k = 0; k < selectCenterList.size(); k++) {
                if (allCenterOrg.getOrgId().equals(selectCenterList.get(k).getOrgId())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                centerHtml.append("<label class=\"checkbox-inline check-box\">");
                centerHtml.append("<input id=\"agencyCenter" + i + "\" type=\"checkbox\" name=\"agencyCenter\" value=\"" + allCenterOrg.getOrgId() + "\" checked>" + allCenterOrg.getOrgName());
                centerHtml.append("</label>");
            } else {
                centerHtml.append("<label class=\"checkbox-inline check-box\">");
                centerHtml.append("<input id=\"agencyCenter" + i + "\" type=\"checkbox\" name=\"agencyCenter\" value=\"" + allCenterOrg.getOrgId() + "\">" + allCenterOrg.getOrgName());
                centerHtml.append("</label>");
            }
        }
        /***********************总行数据中心复选框html end***********************/

        map.put("issuedBranchHtml", branchHtml.toString());
        map.put("issuedUnitHtml", unitHtml.toString());
        map.put("agencyCenterHtml", centerHtml.toString());
        map.putAll(getVersionTypeMap(vmBizInfo));
        return map;
    }

    /**
     * 版本类型复选框页面html组装
     *
     * @param vmBizInfo
     * @return
     */
    public Map getVersionTypeMap(VmBizInfo vmBizInfo) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder versionTypeHtml = new StringBuilder();
        List<String> versionTypeList = new ArrayList<>();
        String versionType = vmBizInfo.getVersionType();
        if (StringUtils.isNotEmpty(versionType)) {
            versionTypeList.addAll(Arrays.asList(Convert.toStrArray(versionType)));
        }
        List<PubParaValue> values = pubParaValueMapper.selectPubParaValueByParaName("version_type");
        for (int i = 0; i < values.size(); i++) {
            PubParaValue value = values.get(i);
            boolean flag = false;
            for (int k = 0; k < versionTypeList.size(); k++) {
                if (value.getValue().equals(versionTypeList.get(k))) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                versionTypeHtml.append("<label class=\"checkbox-inline check-box\">");
                versionTypeHtml.append("<input id=\"versionType" + i + "\" type=\"checkbox\" name=\"versionType\" value=\"" + value.getValue() + "\" onClick=\"hideBusinessByVersionType()\" checked>" + value.getValueDetail());
                versionTypeHtml.append("</label>");
            } else {
                versionTypeHtml.append("<label class=\"checkbox-inline check-box\">");
                versionTypeHtml.append("<input id=\"versionType" + i + "\" type=\"checkbox\" name=\"versionType\" value=\"" + value.getValue() + "\" onClick=\"hideBusinessByVersionType()\">" + value.getValueDetail());
                versionTypeHtml.append("</label>");
            }
        }
        map.put("versionTypeHtml", versionTypeHtml.toString());
        return map;
    }

    @Override
    @Transactional
    public void startProcess(VmBizInfo vmBizInfo) {
        Map map = new HashMap<String, Object>();
        String userId = "";
        // 定时任务启动流程没有当前登录人id，使用版本创建人id
        if ("1".equals(vmBizInfo.getParams().get("automateCheckFlag"))) {
            userId = vmBizInfo.getVersionProducerId();
        } else {
            // 正常页面提交从对象中获取当前登录人
            userId = (String) vmBizInfo.getParams().get("currentUserId");
        }
        // 版本状态
        String status = "";
        // 业务审核状态
        String businessStatus = "";
        // 技术审核状态
        String technologyStatus = "";
        // 安全审核
        if ("1".equals(vmBizInfo.getIfSafetyAudit())) {
            map.put("reCode", "0");
            // 安全审核人
            map.put("safetyAudit", vmBizInfo.getSafetyAuditId());
            // 业务审核人需要去除相同的id
            map.put("businessAudit", StringUtils.removeDuplicationString(vmBizInfo.getBusinessAuditId()));
            // 业务领导需要去除相同的id
            map.put("businessApproval", StringUtils.removeDuplicationString(vmBizInfo.getBusinessApprovalId()));
            // 待安全审核
            status = VersionStatusConstants.VERSION_STATUS_2;
        }
        // 纯技术改造
        else if ("1".equals(vmBizInfo.getVersionType())) {
            map.put("reCode", "1");
            // 待技术审核
            status = VersionStatusConstants.VERSION_STATUS_6;
        }
        // 包含业务审核｜技术审核
        else {
            map.put("reCode", "2");
            map.put("businessAudit", StringUtils.removeDuplicationString(vmBizInfo.getBusinessAuditId()));
            map.put("businessApproval", StringUtils.removeDuplicationString(vmBizInfo.getBusinessApprovalId()));
            // 待业务/技术审核
            status = VersionStatusConstants.VERSION_STATUS_3;
            // 业务审核字段为待业务审核
            businessStatus = VersionStatusConstants.BUSINESS_STATUS_1;
            // 技术审核字段为待业务审核
            technologyStatus = VersionStatusConstants.TECHNOLOGY_STATUS_1;
        }

        handleTime(vmBizInfo);
        // 版本名称生成
        getVersionInfoName(vmBizInfo);

        vmBizInfo.setVersionStatus(status);
        vmBizInfo.setBusinessState(businessStatus);
        vmBizInfo.setTechnologyState(technologyStatus);
        Map<String, Object> params = vmBizInfo.getParams();
        // 纯技术改造去除业务审核数据
        if ("1".equals(vmBizInfo.getVersionType())) {
            params.put("cleanBusinessFlag", 1);
        } else {
            params.put("cleanBusinessFlag", 0);
        }
        // 技术审核
        map.put("technologyAudit", vmBizInfo.getTechnologyAuditId());
        // 应用审核人
        map.put("useApproval", vmBizInfo.getUseApprovalId());
        String businessKey = vmBizInfo.getVersionInfoId();
        String processDefinitionKey = "VmBn";
        map.put("createId", userId);
        map.put("userId", userId);
        // 纯技术审核时版本状态为6
        map.put("status", status);

        List<ProcessInstance> processInstances = activitiCommService.processInfo(businessKey);
        if (!CollectionUtils.isEmpty(processInstances)) {
            log.debug("------------流程businessKey=" + businessKey + ",versionInfoNo=" + vmBizInfo.getVersionInfoNo() + "的版本单流程已启动，不可重复启动-------------");
            return;
        }
        activitiCommService.startProcess(businessKey, processDefinitionKey, map);

        int rows = this.updateVmBizInfo(vmBizInfo);
        if (rows > 0) {
            this.sendMsg(vmBizInfo);
        }
    }

    /**
     * 版本提交时发送短信
     *
     * @param vmBizInfo
     */
    public void sendMsg(VmBizInfo vmBizInfo) {
        String setSmsText = "";
        VmBizInfo info = selectVmBizInfoById(vmBizInfo.getVersionInfoId());
        OgPerson producerPerson = ogPersonService.selectOgPersonById(info.getVersionProducerId());
        if ("1".equals(info.getIfSafetyAudit())) {
            OgPerson safetyPerson = ogPersonService.selectOgPersonById(info.getSafetyAuditId());
            // 向项目经理发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经提交," +
                    "下一步安全审核人：" + safetyPerson.getpName() + "。";
            this.sendSms(setSmsText, producerPerson);
            // 向安全审核发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经创建，请登录运维管理平台查看审核。";
            this.sendSms(setSmsText, safetyPerson);
        } else if ("1".equals(info.getVersionType())) {
            OgPerson technologyAuditPerson = ogPersonService.selectOgPersonById(info.getTechnologyAuditId());
            // 向项目经理发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经提交," +
                    "下一步技术审核人：" + technologyAuditPerson.getpName() + "。";
            this.sendSms(setSmsText, producerPerson);
            // 向技术审核发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经创建，请登录运维管理平台查看审核。";
            this.sendSms(setSmsText, technologyAuditPerson);
        } else {
            List<OgPerson> businessPersons = ogPersonService.selectPersonListByPIds(Convert.toStrArray(info.getBusinessAuditId()));
            String businessPersonName = "";
            for (OgPerson business : businessPersons) {
                businessPersonName += business.getpName() + ",";
            }
            businessPersonName = StringUtils.removeLastChar(businessPersonName);
            OgPerson technologyPerson = ogPersonService.selectOgPersonById(info.getTechnologyAuditId());

            // 向项目经理发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经提交," +
                    "下一步业务审核.技术审核,业务审核人：(" + businessPersonName + "),技术审核人：" + technologyPerson.getpName() + "。";
            this.sendSms(setSmsText, producerPerson);

            // 向业务审核发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经创建，请登录运维管理平台查看审核。";
            for (OgPerson business : businessPersons) {
                this.sendSms(setSmsText, business);
            }

            // 向技术审核发送短信
            setSmsText = "版本单号：" + info.getVersionInfoNo() + ",标题：" + info.getVersionInfoName() + "的版本已经创建，请登录运维管理平台查看审核。";
            this.sendSms(setSmsText, technologyPerson);
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
        p.setCreaterId(person.getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        //p.setDealStatus("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 生成版本任务
     *
     * @param vmBizInfo
     */
    @Override
    public void saveVmBizTaskInfo(VmBizInfo vmBizInfo) {
        VmBizInfo vm = null;
        // 短信审批时已经查询到实体对象这里不重复查询
        if (StringUtils.isNotNull(vmBizInfo.getParams().get("isMsgAppoval"))) {
            vm = vmBizInfo;
        } else {
            vm = selectVmBizInfoById(vmBizInfo.getVersionInfoId());
        }
        // 版本状态
        String versionStatus = vmBizInfo.getVersionStatus();
        String orgsString = "";
        OgOrg org = new OgOrg();
        if (VersionStatusConstants.VERSION_STATUS_12.equals(versionStatus)) {
            String versionInfoName = vm.getVersionInfoName();
            String ifIssueNotice = vm.getIfIssueNotice();
            // 下发分行
            String issued = vm.getIssuedBranch();
            if (StringUtils.isNotEmpty(issued)) {
                orgsString += issued + ",";
            }
            // 下发技术局
            String unit = vm.getIssuedUnit();
            if (StringUtils.isNotEmpty(unit)) {
                orgsString += unit + ",";
            }

            if (StringUtils.isNotEmpty(orgsString)) {
                String[] strs = orgsString.split("\\,");
                for (int i = 0; i < strs.length; i++) {
                    if (StringUtils.isNotEmpty(strs[i])) {
                        org = deptMapper.selectDeptById(strs[i]);
                    }

                    String bizType = "RW";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);

                    // 下发任务时 生成任务信息
                    VmBizTaskinfo vmBizTaskinfo = new VmBizTaskinfo();
                    vmBizTaskinfo.setTaskproducetime(DateUtils.dateTimeNow());
                    vmBizTaskinfo.setTaskNo(bizType + nowDateStr + numStr);// 任务单序号
                    vmBizTaskinfo.setOrg(org.getOrgId());// 所属机构
                    vmBizTaskinfo.setVersionInfoId(vm.getVersionInfoId());// 版本申请号
                    vmBizTaskinfo.setTaskName(versionInfoName);// 任务名称
                    vmBizTaskinfo.setActualStartTime(DateUtils.handleTimeYYYYMMDDHHMMSS(vm.getStartUpgradeTime()));// 升级时间
                    vmBizTaskinfo.setActualFinishTime(DateUtils.handleTimeYYYYMMDDHHMMSS(vm.getEndUpgradeTime()));// 完成时间
                    vmBizTaskinfo.setTaskStatus("1");// 设置任务状态为待下载
                    vmBizTaskinfo.setTaskId(UUID.getUUIDStr());
                    taskinfoMapper.insertVmBizTaskinfo(vmBizTaskinfo);
                }
            }
            if (StringUtils.isNotEmpty(vm.getAgencyCenter())) {

                String bizType = "RW";
                IdGenerator generator = new IdGenerator();
                String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                generator.setCurrentDate(nowDateStr);
                generator.setBizType(bizType);
                String numStr = idGeneratorService.selectIdGeneratorByType(generator);

                VmBizTaskinfo vmBizTaskinfo = new VmBizTaskinfo();
                vmBizTaskinfo.setTaskproducetime(DateUtils.dateTimeNow());
                // 任务单序号
                vmBizTaskinfo.setTaskNo(bizType + nowDateStr + numStr);
                // vmBizTaskinfo.setTaskDealGroupId(groupId);//所属工作组
                // 所属机构
                OgPerson person = ogPersonMapper.selectOgPersonById(vm.getUseDivisionChiefApprovalId());
                vmBizTaskinfo.setOrg(person.getOrgId());
                vmBizTaskinfo.setVersionInfoId(vm.getVersionInfoId());// 版本申请号
                vmBizTaskinfo.setTaskName(versionInfoName);// 任务名称
                vmBizTaskinfo.setActualStartTime(DateUtils.handleTimeYYYYMMDDHHMMSS(vm.getStartUpgradeTime()));// 升级时间
                vmBizTaskinfo.setActualFinishTime(DateUtils.handleTimeYYYYMMDDHHMMSS(vm.getEndUpgradeTime()));// 完成时间
                vmBizTaskinfo.setTaskStatus("1");// 设置任务状态为待下载
                vmBizTaskinfo.setTaskId(UUID.getUUIDStr());
                taskinfoMapper.insertVmBizTaskinfo(vmBizTaskinfo);
            }

            // 自动生成公告
            if ("1".equals(ifIssueNotice)) {
                SysNotice sysNotice = new SysNotice();
                sysNotice.setAmTitle(versionInfoName);// 公告标题
                //sysNotice.setAddTime(DateUtils.dateTimeNow());// 创建日期
                sysNotice.setCreateId(vmBizInfo.getVersionProducerId());// 创建人
                sysNotice.setDescription("");// 描述
                sysNotice.setCurrentStatus("01");// 当前状态
                sysNotice.setAmBizId(UUID.getUUIDStr());
                noticeService.insertNotice(sysNotice);// 暂存公告
            }

            // 向分行|科技短信
            if (StringUtils.isNotEmpty(orgsString)) {
                VmBizTaskinfo vmBizTaskinfo = new VmBizTaskinfo();
                vmBizTaskinfo.setVersionInfoId(vm.getVersionInfoId());
                vmBizTaskinfo.setTaskStatus("1");// 待下载
                List<VmBizTaskinfo> list = taskinfoMapper.selectVmBizTaskinfoList(vmBizTaskinfo);
                if (!CollectionUtils.isEmpty(list)) {
                    List<String> orgIds = new ArrayList<>();
                    for (VmBizTaskinfo taskInfo : list) {
                        orgIds.add(taskInfo.getOrg());
                    }
                    OgPerson ogPerson = new OgPerson();
                    Map<String, Object> params = new HashMap<>();
                    params.put("orgIds", orgIds);
                    // 岗位id为 0017
                    params.put("postId", "0017");
                    ogPerson.setParams(params);
                    List<OgPerson> personList = ogPersonService.selectOgPersonByOrgAndPostId(ogPerson);
                    String msg = "版本变更单号：" + vm.getVersionInfoNo() + ";标题："
                            + vm.getVersionInfoName() + "的版本已下发，请登录运维管理平台/发布管理/版本实施页面,进行版本下载和确认升级！";
                    for (OgPerson person : personList) {
                        this.sendSms(msg, person);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelVmBizInfoByVersionStatus() {
        String[] status = {VersionStatusConstants.VERSION_STATUS_2, VersionStatusConstants.VERSION_STATUS_3,
                VersionStatusConstants.VERSION_STATUS_4, VersionStatusConstants.VERSION_STATUS_6,
                VersionStatusConstants.VERSION_STATUS_7, VersionStatusConstants.VERSION_STATUS_8};
        List<VmBizInfo> vmBizInfoList = vmBizInfoMapper.selectVmBizInfoByVersionStatus(Arrays.asList(status));
        if (!CollectionUtils.isEmpty(vmBizInfoList)) {
            for (VmBizInfo vmBizInfo : vmBizInfoList) {
                String startUpgradeTime = vmBizInfo.getStartUpgradeTime();
                if (StringUtils.isNotEmpty(startUpgradeTime)) {
                    Date startTimeDate = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, startUpgradeTime);
                    Date nowDate = DateUtils.getNowDate();
                    // 如果 升级开始时间 + 1天 < 当前时间，则执行关闭版本单流程相关逻辑
                    if (DateUtils.addDay(startTimeDate).before(nowDate)) {
                        Map<String, Object> map = new HashMap<>();
                        String businessKey = vmBizInfo.getVersionInfoId();
                        String comment = "定时任务结束流程";
                        map.put("comment", comment);
                        map.put("userId", vmBizInfo.getVersionProducerId());
                        map.put("businessKey", businessKey);
                        String processDefinitionKey = "VmBn";
                        map.put("processDefinitionKey", processDefinitionKey);
                        // 此时流程走向为作废
                        map.put("reCode", "1");

                        VmBizInfo vm = new VmBizInfo();
                        // 状态为已作废
                        vm.setVersionStatus(VersionStatusConstants.VERSION_STATUS_14);
                        // 复用invalidationMark="TK"标识定时任务作废的流程
                        vm.setInvalidationMark("TK");
                        vm.setVersionInfoId(businessKey);

                        try {
                            int row = vmBizInfoMapper.updateVmBizInfo(vm);
                            if (row > 0) {
                                activitiCommService.complete(map);

                                // 先执行流程在发送短信，防止短信发送成功，流程报错的情况
                                String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本，已错过预定升级时间窗口，系统按照逾期作废进行处理！";
                                OgPerson person = ogPersonService.selectOgPersonById(vmBizInfo.getVersionProducerId());
                                // 向版本创建人发送短信
                                this.sendSms(msg, person);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.debug("版本单号：" + vmBizInfo.getVersionInfoNo() + "流程自动关闭失败，失败原因：" + e.getMessage());
                        }

                        // 调用定时任务作废流程的同时如果是自动化类型需要调用接口发送自动化禁止版本单的接口
                        this.forbiddenVersion(vmBizInfo);
                    }
                }
            }
        } else {
            log.debug("############没有符合状态的版本单，定时任务执行结束#############");
        }
    }

    @Override
    public Map checkVersionAttachmentExcel(VmBizInfo vmBizInfo) {
        Map map = new HashMap<>();
        // 是自动化才需要校验excel模板，否则不做校验
        if ("1".equals(vmBizInfo.getIsAutomate())) {
            List<Attachment> attachments = (List) vmBizInfo.getParams().get("attachmentList");
            if (CollectionUtils.isEmpty(attachments)) {
                Attachment attachment = new Attachment();
                attachment.setOwnerId(vmBizInfo.getVersionInfoId());
                attachment.setFileType("3");
                attachments = attachmentService.selectAttachmentList(attachment);
            }

            if (CollectionUtils.isEmpty(attachments)) {
                map.put("flag", false);
                map.put("message", "版本发布是自动化必须上传附件类型为自动化步骤的附件！");
                return map;
            }
            for (Attachment att : attachments) {
                // 附件类型为自动化步骤、变更原因不为空说明发送自动化已经成功不再重复发送
                if (StringUtils.isEmpty(att.getChangeReason())) {
                    UploadMsg msg = new UploadMsg();
                    List<UploadApproval> list = new ArrayList<>();
                    UploadApproval uploadApproval = new UploadApproval();
                    uploadApproval.setExcelname(att.getFileName());
                    uploadApproval.setExcelpath(att.getFilePath());
                    uploadApproval.setTaskid(vmBizInfo.getVersionInfoNo());
                    uploadApproval.setTasktype("1");
                    uploadApproval.setChangedes("");
                    list.add(uploadApproval);
                    Map<String, Object> params = new HashMap<>();
                    params.put("excelname", att.getFileName());
                    params.put("realexcelname", att.getFilePath());
                    params.put("replaceno", vmBizInfo.getVersionInfoNo());
                    params.put("groupName", att.getGroupName());
                    params.put("mobilePhone", vmBizInfo.getParams().get("mobilePhone"));
                    msg.setParams(params);
                    msg.setAction("upload");
                    msg.setDemand(list);
                    // 发送自动化校验如果失败直接返回
                    Map resultMap = entegorServer.sendMessageUpload(msg);
                    if (!"200".equals(resultMap.get("status"))) {
                        map.put("flag", false);
                        map.put("message", "自动化步骤名称为:" + att.getFileName() + "的附件发送自动化校验失败，请检查该附件内容是否正确，失败原因为:" + resultMap.get("message"));
                        return map;
                    }
                    // 如果变更原因不为空则更新到附件表
                    String changeDesc = (String) resultMap.get("changedesc");
                    String sysName = (String) resultMap.get("sysname");
                    if (StringUtils.isNotEmpty(changeDesc) && StringUtils.isNotEmpty(sysName)) {
                        Attachment attachment1 = new Attachment();
                        attachment1.setAtId(att.getAtId());
                        attachment1.setChangeReason(changeDesc);
                        attachment1.setSysName(sysName);
                        attachmentService.updateAttachment(attachment1);
                    }
                }
            }
        }
        map.put("flag", true);
        map.put("message", "校验自动化步骤成功！");
        return map;
    }

    /**
     * 调用自动化接口实现撤回流程后通知自动化
     * 此时只发送不不等返回结果信息，catch到异常以后输出日志便于查找问题
     *
     * @param vmBizInfo
     * @return
     */
    @Override
    public void forbiddenVersion(VmBizInfo vmBizInfo) {
        if ("1".equals(vmBizInfo.getIsAutomate())) {
            try {
                Map map = new HashMap<>();
                map.put("versionInfoNo", vmBizInfo.getVersionInfoNo());
                map.put("mobilePhone", vmBizInfo.getParams().get("mobilePhone"));
                entegorServer.forbiddenVersion(map);
                Attachment attachment = new Attachment();
                attachment.setOwnerId(vmBizInfo.getVersionInfoId());
                attachment.setFileType("3");
                List<Attachment> attachments = attachmentService.selectAttachmentList(attachment);
                if (!CollectionUtils.isEmpty(attachments)) {
                    for (Attachment att : attachments) {
                        String changeReason = att.getChangeReason();
                        if (StringUtils.isNotEmpty(changeReason)) {
                            Attachment attachment1 = new Attachment();
                            attachment1.getParams().put("isCleanChangeReason", "1");
                            attachment1.setAtId(att.getAtId());
                            attachmentService.updateAttachment(attachment1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("版本单号为:" + vmBizInfo.getVersionInfoNo() + "的版本单调用自动化禁止接口失败.", e);
            }

        }
    }

    @Override
    public void closeVmBizVersion(String id) {
        VmBizInfo vmBizInfo = this.selectVmBizInfoByIdNoConvert(id);
        vmBizInfo.getParams().put("mobilePhone", ShiroUtils.getOgUser().getMobilPhone());
        this.forbiddenVersion(vmBizInfo);
        VmBizInfo vmBizInfo1 = new VmBizInfo();
        vmBizInfo1.setVersionInfoId(id);
        vmBizInfo1.setVersionStatus(VersionStatusConstants.VERSION_STATUS_14);
        this.updateVmBizInfo(vmBizInfo1);
    }

    /**
     * 版本待办列表
     * vmBnApprovalFlag=1 技术主管审核
     * vmBnApprovalFlag=2 业务主管审核
     * vmBnApprovalFlag=3 运维审核
     * vmBnApprovalFlag=4 发布审核
     * vmBnApprovalFlag=5 发布审核
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public List<Map<String, Object>> getVmBnUserTask(String userId, String type) {
        List<Map<String, Object>> allList = new ArrayList<>();
        List<Map<String, Object>> techList = activitiCommService.userTaskUserId(userId, type, "技术主管审核");
        if (!CollectionUtils.isEmpty(techList)) {
            for (Map<String, Object> map : techList) {
                map.put("vmBnApprovalFlag", "1");
            }
            allList.addAll(techList);
        }
        List<Map<String, Object>> busList = activitiCommService.userTaskUserId(userId, type, "业务主管审核");
        if (!CollectionUtils.isEmpty(busList)) {
            for (Map<String, Object> map : busList) {
                map.put("vmBnApprovalFlag", "2");
            }
            allList.addAll(busList);
        }
        List<Map<String, Object>> operatList = activitiCommService.userTaskUserId(userId, type, "运维审核");
        if (!CollectionUtils.isEmpty(operatList)) {
            for (Map<String, Object> map : operatList) {
                map.put("vmBnApprovalFlag", "3");
            }
            allList.addAll(operatList);
        }
        List<Map<String, Object>> pubList = activitiCommService.userTaskUserId(userId, type, "发布审核");
        if (!CollectionUtils.isEmpty(pubList)) {
            for (Map<String, Object> map : pubList) {
                map.put("vmBnApprovalFlag", "4");
            }
            allList.addAll(pubList);
        }
        List<Map<String, Object>> jjList = activitiCommService.userTaskUserId(userId, type, "紧急审核");
        if (!CollectionUtils.isEmpty(jjList)) {
            for (Map<String, Object> map : jjList) {
                map.put("vmBnApprovalFlag", "5");
            }
            allList.addAll(jjList);
        }
        return allList;
    }

    @Override
    public AjaxResult vmBnAppApproval(String userId, String bizId, String comment, String trend, String vmBnApprovalFlag, String jjspApprovalId) {
        VmBizInfo vmBizInfo = this.selectVmBizInfoByIdNoConvert(bizId);
        String status = "";
        String businessState = "";
        String technologyStatus = "";
        Map<String, Object> map = new HashMap<>();
        map.put("businessKey", bizId);
        map.put("comment", comment);
        map.put("userId", userId);
        String processDefinitionKey = "VmBn";
        map.put("processDefinitionKey", processDefinitionKey);
        VmBizInfo bizInfo = new VmBizInfo();
        boolean userComplete = false;
        switch (vmBnApprovalFlag) {
            // 技术主管审核
            case "1":
                if ("1".equals(vmBizInfo.getVersionType())) {
                    // 如果是纯技术，技术主管审核完成后将状态修改为待版本审核
                    status = VersionStatusConstants.VERSION_STATUS_9;
                    technologyStatus = VersionStatusConstants.TECHNOLOGY_STATUS_PASS;
                }
                break;
            // 业务主管审核
            case "2":
                userComplete = true;
                break;
            // 运维审核
            case "3":
                status = VersionStatusConstants.VERSION_STATUS_11;
                break;
            // 发布审核
            case "4":
                if ("0".equals(trend)) {
                    status = VersionStatusConstants.VERSION_STATUS_12;
                }
                if (StringUtils.isNotEmpty(jjspApprovalId)) {
                    status = VersionStatusConstants.VERSION_STATUS_13;
                    bizInfo.setAcceptRole("1");
                    bizInfo.setJjspApprovalId(jjspApprovalId);
                } else {
                    bizInfo.setAcceptRole("2");
                }
                break;
            // 紧急审核
            case "5":
                status = VersionStatusConstants.VERSION_STATUS_12;
                break;
        }
        if ("0".equals(trend)) {
            map.put("reCode", "0");
        } else {
            map.put("reCode", "1");
            status = VersionStatusConstants.VERSION_STATUS_14;
        }
        bizInfo.setVersionStatus(status);
        bizInfo.setVersionInfoId(bizId);
        bizInfo.setBusinessState(businessState);
        bizInfo.setTechnologyState(technologyStatus);
        String msg = "";
        try {
            this.updateVmBizInfo(bizInfo);
            if (userComplete) {
                activitiCommService.usersComplete(map);
            } else {
                activitiCommService.complete(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "版本单审批失败，失败原因:" + e.getMessage();
        }
        return StringUtils.isEmpty(msg) ? AjaxResult.success() : AjaxResult.error(msg);
    }

    @Override
    public String selectStatusByVersionInfoNo(String versionInfoNo) {
        return vmBizInfoMapper.selectStatusByVersionInfoNo(versionInfoNo);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss类型的时间格式化为yyyyMMddHHmmss
     *
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDDHHMMSS(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, date);
        return formatDateStr;
    }

    /**
     * 将yyyy-MM-dd类型的时间格式化为yyyyMMdd
     *
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDD(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDD, date);
        return formatDateStr;
    }

}
