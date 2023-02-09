package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.mapper.ConsumerMapper;
import com.ruoyi.system.service.IConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsumerServiceImpl implements IConsumerService, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public int insertOgPerson(OgPerson ogPerson) {
        return consumerMapper.insertOgPerson(ogPerson);
    }

    @Override
    public List<OgPerson> selectOgPersonList(OgPerson ogPerson) {
        return consumerMapper.selectOgPersonList(ogPerson);
    }

    @Override
    public List<OgPerson> selectOgPersonAddList(OgPerson ogPerson) {
        return consumerMapper.selectOgPersonAddList(ogPerson);
    }


    @Override
    public int updateOgPersonStatus(OgPerson ogPerson) {
        return consumerMapper.updateOgPersonStatus(ogPerson);
    }

    @Override
    public OgPerson selectOgPersonById(String pId) {
        return consumerMapper.selectOgPersonById(pId);
    }

    @Override
    public OgPerson selectOgPersonByPid(String pId) {
        return consumerMapper.selectOgPersonByPid(pId);
    }

    @Override
    public int updateOgPerson(OgPerson ogPerson) {
        return consumerMapper.updateOgPerson(ogPerson);
    }

    @Override
    public List<OgPerson> selectListByLevelCode(String levelCode, String rId) {
        return consumerMapper.selectListByLevelCode(levelCode, rId);
    }

    @Override
    public List<OgPerson> selectPersonListByPIds(String[] ids) {
        return consumerMapper.selectPersonListByPIds(ids);
    }

    @Override
    public List<OgPerson> selectListByRoleId(OgPerson ogPerson) {

        return consumerMapper.selectListByRoleId(ogPerson);
    }

    @Override
    public List<OgPerson> selectCmbizPerson(OgPerson ogPerson) {

        return consumerMapper.selectCmBizPerson(ogPerson);
    }

    @Override
    public List<OgPerson> selectOgPersonByOrgAndPostId(OgPerson ogPerson) {
        return consumerMapper.selectOgPersonByOrgAndPostId(ogPerson);
    }

    @Override
    public OgPerson selectOgPersonByPhone(String phone) {
        OgPerson person = new OgPerson();
        person.setMobilPhone(phone);
        person.setInvalidationMark("1");
        List<OgPerson> personList = this.selectOgPersonList(person);
        return !CollectionUtils.isEmpty(personList) ? personList.get(0) : null;
    }

    @Override
    public OgPerson selectOgPersonEvoById(String performUserId) {
        return consumerMapper.selectOgPersonEvoById(performUserId);
    }

    @Override
    public List<OgPerson> selectOgPersonOrgById(String orgId) {
        return consumerMapper.selectOgPersonOrgById(orgId);
    }

    @Override
    public OgPerson checkPhoneUnique(String phone) {
        return consumerMapper.checkPhoneUnique(phone);
    }

    @Override
    public OgPerson bigDataPersonByPhone(String phone) {
        return consumerMapper.bigDataPersonByPhone(phone);
    }


    @Override
    public List<OgPerson> selectPersonByGroupIdAndRoleId(OgPerson ogPerson) {
        return consumerMapper.selectPersonByGroupIdAndRoleId(ogPerson);
    }

    @Override
    public List<OgPerson> selectPersonByPostId(Map<String, String> map) {
        return consumerMapper.selectPersonByPostId(map);
    }

	@Override
	public int deleteOgPersonByPIds(String pIds) {
		return consumerMapper.deleteOgPersonByPIds(Convert.toStrArray(pIds));
	}

	@Override
    public List<OgPerson> selectOgPersonByPostIds(OgPerson ogPerson){
        return consumerMapper.selectOgPersonByPostIds(ogPerson);
    }

    @Override
    public List<OgPerson> selectBatchPersonList(OgPerson ogPerson) {
        Map<String,Object> map = new HashMap<>();
        map.put("ogPerson",ogPerson);
        return consumerMapper.selectBatchPersonList(map);
    }

    @Override
    public List<OgPerson> selectList(OgPerson ogPerson) {
        return consumerMapper.selectList(ogPerson);
    }

    @Override
    public List<OgPerson> selectPersonListByPostAndRole(String postId, String roleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("postId",postId);
        map.put("roleId",roleId);
        return consumerMapper.selectPersonListByPostAndRole(map);
    }

    @Override
    public List<OgPerson> selectOgPersonByOrgLevAndPostIds(OgPerson ogPerson) {
        return consumerMapper.selectOgPersonByOrgLevAndPostIds(ogPerson);
    }

    @Override
    public List<OgPerson> selectLxbgOgPersonOrgById(String orgId) {
        return consumerMapper.selectLxbgOgPersonOrgById(orgId);
    }

    @Override
    public List<OgPerson> selectListByOrgIdAndRoleId(String nodeId, String rId) {
        return consumerMapper.selectListByOrgIdAndRoleId(nodeId,rId);
    }

    @Override
    public OgPerson selectRoleByIdTwo(String userId) {
        return consumerMapper.selectRoleByIdTwo(userId);
    }

    @Override
    public List<OgPerson> selectOgPersonJqList(OgPerson ogPerson) {
        return consumerMapper.selectOgPersonJqList(ogPerson);
    }

    @Override
    public List<OgPerson> selectAppAssessPersonByPostId(OgPerson ogPerson) {
        ogPerson.getParams().put("postIds", this.appAssessPostIdList);
        return consumerMapper.selectOgPersonByPostIds(ogPerson);
    }

    @Override
    public List<OgPerson> selectAssessList(OgPerson ogPerson) {
        // union标识需要机构和岗位id联合查询，noUnion标识只需要岗位id查询
        ogPerson.getParams().put("orgIds", "union".equals(ogPerson.getRemark()) ? this.gzAnalyOrgIdList : new ArrayList<>());
        ogPerson.getParams().put("postIds", "union".equals(ogPerson.getRemark()) ? this.gzAnalyPostIdList : this.gzLeaderConfirmPostIdList);
        return consumerMapper.selectAssessList(ogPerson);
    }

    @Override
    public List<OgPerson> selectPostByuser(OgPerson person) {
        return consumerMapper.selectPostByuser(person);
    }

    @Override
    public List<OgPerson> selectAppCheck(OgPerson ogPerson) {
        final String centerDataDirector = "0011";
        final String businessDirector = "0014";
        final String technologyDataDirector = "0016";
        Map<String, Object> params = ogPerson.getParams();
        //  数据中心处长｜总行业务处长｜总行科技处长
        String[] postIds = {centerDataDirector, businessDirector, technologyDataDirector};
        List<String> postIdsList = Arrays.stream(postIds).collect(Collectors.toList());
        params.put("postIds", postIdsList);
        params.put("postId", centerDataDirector);
        return consumerMapper.selectAppCheck(ogPerson);
    }

    /**应急事件单-应用评估人根据机构查询  应用管理一处+应用管理二处+应用管理三处+应用管理四组*/
    private List<String> appAssessPostIdList = new ArrayList<>();

    /**应急事件单-故障分析｜整改人根据机构查询  网络管理一处+系统管理一处+系统管理二处+资源管理处*/
    private List<String> gzAnalyOrgIdList = new ArrayList<>();

    /**应急事件单-故障分析｜整改人根据机构查询 开发：数据中心人员+总行科技人员（岗位）*/
    private List<String> gzAnalyPostIdList = new ArrayList<>();

    /**故障确认提交审核人查询 开发：数据中心处长  + 数据中心领导（岗位）*/
    private List<String> gzLeaderConfirmPostIdList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /*log.debug("----------根据参数key[paraName=app_assess_org]加载应急事件单-应用评估人-机构开始------------");
        List<PubParaValue> assessList = pubParaValueMapper.selectPubParaValueByParaName("app_assess_org");
        for(PubParaValue value : assessList){
            this.appAssessOrgIdList.add(value.getValue());
        }
        log.debug("----------根据参数key[paraName=app_assess_org]加载应急事件单-应用评估人-机构字典项: " + assessList + "------------");
        log.debug("----------根据参数key[paraName=app_assess_org]加载应急事件单-应用评估人-机构结束------------");*/
        // 数据中心人员
        appAssessPostIdList.add("0010");

        /*log.debug("----------根据参数key[paraName=gz_analy_org]加载应急事件单-故障分析｜整改人-机构开始------------");
        List<PubParaValue> analyList = pubParaValueMapper.selectPubParaValueByParaName("gz_analy_org");
        for(PubParaValue value : analyList){
            this.gzAnalyOrgIdList.add(value.getValue());
        }
        log.debug("----------根据参数key[paraName=gz_analy_org]加载应急事件单-故障分析｜整改人-机构字典项: " + analyList + "------------");
        log.debug("----------根据参数key[paraName=gz_analy_org]加载应急事件单-故障分析｜整改人-机构结束------------");*/

        // 数据中心人员  +  总行科技人员
        String[] analyPostIds = {"0010", "0015"};
        this.gzAnalyPostIdList = Arrays.stream(analyPostIds).collect(Collectors.toList());
        String[] confirmPostIds = {"0012"};
        // 数据中心处长  + 数据中心领导
        this.gzLeaderConfirmPostIdList = Arrays.stream(confirmPostIds).collect(Collectors.toList());
    }

    @Override
    public List<OgPerson> getOgPersonListByPostIdAndGroupId(Map map) {
        return consumerMapper.getOgPersonListByPostIdAndGroupId(map);
    }

    @Override
    public List<OgPerson> selectListByRoleId(String rId){
        return consumerMapper.selectListByRoleId(rId);
    }
}
