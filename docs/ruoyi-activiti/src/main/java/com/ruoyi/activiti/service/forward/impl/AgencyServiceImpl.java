package com.ruoyi.activiti.service.forward.impl;

import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.activiti.service.forward.IAgencyService;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 软研对接接口实现类
 *
 * @author 14735
 */
@Service("ogAgencyManager")
@Transactional(rollbackFor = Exception.class)
public class AgencyServiceImpl implements IAgencyService {

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IImBizIssuefxService issueService;
    private static final Logger logger = LoggerFactory.getLogger(ForwardServiceImpl.class);
    /**
     * 710001
     * 获取登录人的二级机构树
     * 如果登录人属于一级机构，返回整个机构树
     * @return
     * @throws BusException
     */
    @EsbServiceMapping
    @Override
    public List<OgAgency> getLvTAgencysTree(@ServiceParam(name="orgid") String id,
                                         @ServiceParam(name = "initiator", pubProperty = "userId") String initiator) throws BusException {
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(initiator);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //String orgId = "4028aba334fa8d0e01357622aa6a1a16";
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());

        List<OgOrg> deptList = deptService.selectDeptList(org);
        List<OgAgency> list = new ArrayList<>();
        for (OgOrg org1 : deptList) {
            //机构parent
            OgAgency ogAgencyParent = new OgAgency();
            OgOrg ogOrgParent = deptService.selectDeptById(org1.getOrgId());
            ogAgencyParent.setOrgid(ogOrgParent.getOrgId());
            ogAgencyParent.setOrgcode(ogOrgParent.getOrgCode());
            ogAgencyParent.setOrgname(ogOrgParent.getOrgName());
            ogAgencyParent.setOrglv(ogOrgParent.getOrgLv());
            ogAgencyParent.setLevelCode(ogOrgParent.getLevelCode());


            //机构
            OgAgency ogAgency = new OgAgency();
            ogAgency.setOrgid(org1.getOrgId());
            ogAgency.setOrgcode(org1.getOrgCode());
            ogAgency.setOrgname(org1.getOrgName());
            ogAgency.setParent(ogAgencyParent);
            ogAgency.setOrglv(org1.getOrgLv());
            ogAgency.setLevelCode(org1.getLevelCode());
            ogAgency.setInvalidationMark(org1.getInvalidationMark());
            list.add(ogAgency);
        }
        return list;
    }

    /**
     * 获取当前登陆人的二级或者是一级机构
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.当前登陆用户的机构就是一级或者是二级机构
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            return ogOrg;
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            String twoLevelCode = split[2];
            return deptService.selectDeptByCode(twoLevelCode);
        }

    }

    /**
     * 710002根据上级查找下一级机构（用于机构树展示）- 无权限认证
     * @param id
     * @return
     * @throws BusException
     */
    @EsbServiceMapping
    @Override
    public List<OgAgency> getChildOgAgencys(@ServiceParam(name="orgid") String id,
                                         @ServiceParam(name = "initiator", pubProperty = "userId") String initiator) throws BusException {

        String orgId = id;
        if (orgId == null || orgId.equals("")) {
            //获取人员信息
            OgPerson ogPerson = ogPersonService.selectOgPersonById(initiator);
            //获取机构ID
            orgId = ogPerson.getOrgId();
        }
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> deptList  = deptService.selectDeptList(org);
        List<OgAgency> list = new ArrayList<>();
        for (OgOrg org1 : deptList) {
            //机构parent
            OgAgency ogAgencyParent = new OgAgency();
            OgOrg ogOrgParent = deptService.selectDeptById(org1.getOrgId());
            ogAgencyParent.setOrgid(ogOrgParent.getOrgId());
            ogAgencyParent.setOrgcode(ogOrgParent.getOrgCode());
            ogAgencyParent.setOrgname(ogOrgParent.getOrgName());
            ogAgencyParent.setOrglv(ogOrgParent.getOrgLv());
            ogAgencyParent.setLevelCode(ogOrgParent.getLevelCode());

            //机构
            OgAgency ogAgency = new OgAgency();
            ogAgency.setOrgid(org1.getOrgId());
            ogAgency.setOrgcode(org1.getOrgCode());
            ogAgency.setOrgname(org1.getOrgName());
            ogAgency.setParent(ogAgencyParent);
            ogAgency.setOrglv(org1.getOrgLv());
            ogAgency.setLevelCode(org1.getLevelCode());
            ogAgency.setInvalidationMark(org1.getInvalidationMark());
            list.add(ogAgency);
        }
        return list;
    }

    /**
     * 810002
     * @param issuefxId
     * @return
     * @throws BusException
     */
    @EsbServiceMapping
    @Override
    public OgOrg getZxAll(@ServiceParam(name = "issuefxId") String issuefxId)throws BusException{
        logger.info("=======软研调用问题接口810002：查询问题单所属机构开始======");
        ImBizIssuefx imBizIssuefx=issueService.selectImBizIssuefxById(issuefxId);
        String ogId=imBizIssuefx.getIssueOrg();
        String ogName=imBizIssuefx.getIssueOrgName();
        OgOrg ogOrg=new OgOrg();
        ogOrg.setOrgId(ogId);
        ogOrg.setOrgName(ogName);
        logger.info(ogOrg.toString());
        logger.info("=======软研调用问题接口810002：查询问题单所属机构结束：======");
        return ogOrg;
    }
}
