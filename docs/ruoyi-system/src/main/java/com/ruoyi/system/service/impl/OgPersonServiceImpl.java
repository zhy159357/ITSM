package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.security.Md5OriginalUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.mapper.OgUserMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgPersonsService;
import com.ruoyi.system.service.IOgUsersService;
import com.ruoyi.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Service("iOgPersonService")
public class OgPersonServiceImpl implements IOgPersonService, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(OgPersonServiceImpl.class);

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Autowired
    private PubParaValueMapper pubParaValueMapper;

    @Autowired
    private OgUserMapper ogUserMapper;

    @Autowired
    private IOgUsersService iOgUsersService;
    @Autowired
    private IOgPersonsService iOgPersonsService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Value("${foreign.user.url}")
    private String portalUrl;
    @Value("${foreign.user.serviceCode}")
    private String serviceCode;

    @Override
    public int insertOgPerson(OgPerson ogPerson) {
    	ogPerson.setPid_crc(this.cRc32(ogPerson.getpId()));
        return ogPersonMapper.insertOgPerson(ogPerson);
    }

    @Override
    public List<OgPerson> selectOgPersonList(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersonList(ogPerson);
    }
    @Override
    public List<Map<String, String>> selectOgPersons(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersons(ogPerson);
    }
    @Override
    public List<OgPerson> selectOgPersonAddList(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersonAddList(ogPerson);
    }


    @Override
    public int updateOgPersonStatus(OgPerson ogPerson) {
        return ogPersonMapper.updateOgPersonStatus(ogPerson);
    }

    @Override
    public OgPerson selectOgPersonById(String pId) {
        return ogPersonMapper.selectOgPersonById(pId);
    }

    @Override
    public OgPerson selectOgPersonByPid(String pId) {
        return ogPersonMapper.selectOgPersonByPid(pId);
    }

    @Override
    public int updateOgPerson(OgPerson ogPerson) {
    	ogPerson.setPid_crc(this.cRc32(ogPerson.getpId()));
        return ogPersonMapper.updateOgPerson(ogPerson);
    }

    @Override
    public List<OgPerson> selectListByLevelCode(String levelCode, String rId) {
        return ogPersonMapper.selectListByLevelCode(levelCode, rId);
    }

    @Override
    public List<OgPerson> selectPersonListByPIds(String[] ids) {
        List<OgPerson> list = new ArrayList<OgPerson>();
        if ("mysql".equals(dbType)) {
            list = ogPersonMapper.selectPersonListByPIdsMysql(ids);
        } else {
            list = ogPersonMapper.selectPersonListByPIds(ids);
        }
        return ogPersonMapper.selectPersonListByPIds(ids);
    }

    @Override
    public List<OgPerson> selectListByRoleId(OgPerson ogPerson) {
        List<OgPerson> list = new ArrayList<OgPerson>();
        if ("mysql".equals(dbType)) {
            list = ogPersonMapper.selectListByRoleIdMysql(ogPerson);
        } else {
            list = ogPersonMapper.selectListByRoleId(ogPerson);
        }
        return list;
    }

    @Override
    public List<OgPerson> selectCmbizPerson(OgPerson ogPerson) {

        return ogPersonMapper.selectCmBizPerson(ogPerson);
    }

    @Override
    public List<OgPerson> selectOgPersonByOrgAndPostId(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersonByOrgAndPostId(ogPerson);
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
        return ogPersonMapper.selectOgPersonEvoById(performUserId);
    }

    @Override
    public List<OgPerson> selectOgPersonOrgById(String orgId) {
        return ogPersonMapper.selectOgPersonOrgById(orgId);
    }

    /**
     * ????????????????????????????????????
     *
     * @param orgId
     * @return
     */
    @Override
    public List<OgPerson> selectOgPersonListByOrgId(String orgId) {
        return ogPersonMapper.selectOgPersonListByOrgId(orgId);
    }

    @Override
    public OgPerson checkPhoneUnique(String phone) {
        return ogPersonMapper.checkPhoneUnique(phone);
    }
    @Override
    public OgPerson checkPhone(String phone) {
        return ogPersonMapper.checkPhone(phone);
    }

    @Override
    public OgPerson bigDataPersonByPhone(String phone) {
        return ogPersonMapper.bigDataPersonByPhone(phone);
    }


    @Override
    public List<OgPerson> selectPersonByGroupIdAndRoleId(OgPerson ogPerson) {
        return ogPersonMapper.selectPersonByGroupIdAndRoleId(ogPerson);
    }

    @Override
    public List<OgPerson> selectPersonByPostId(Map<String, String> map) {
        return ogPersonMapper.selectPersonByPostId(map);
    }

    @Override
    public int deleteOgPersonByPIds(String pIds) {
        return ogPersonMapper.deleteOgPersonByPIds(Convert.toStrArray(pIds));
    }

    @Override
    public List<OgPerson> selectOgPersonByPostIds(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersonByPostIds(ogPerson);
    }

    @Override
    public List<OgPerson> selectBatchPersonList(OgPerson ogPerson) {
        Map<String, Object> map = new HashMap<>();
        map.put("ogPerson", ogPerson);
        return ogPersonMapper.selectBatchPersonList(map);
    }

    @Override
    public List<OgPerson> selectPersonListByLevelCode(OgPerson ogPerson) {
        return ogPersonMapper.selectPersonListByLevelCode(ogPerson);
    }

    @Override
    public List<OgPerson> selectList(OgPerson ogPerson) {
        return ogPersonMapper.selectList(ogPerson);
    }

    @Override
    public List<OgPerson> selectPersonListByPostAndRole(String postId, String roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("postId", postId);
        map.put("roleId", roleId);
        return ogPersonMapper.selectPersonListByPostAndRole(map);
    }

    @Override
    public List<OgPerson> selectOgPersonByOrgLevAndPostIds(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersonByOrgLevAndPostIds(ogPerson);
    }

    @Override
    public List<OgPerson> selectLxbgOgPersonOrgById(String orgId) {
        return ogPersonMapper.selectLxbgOgPersonOrgById(orgId);
    }

    @Override
    public List<OgPerson> selectListByOrgIdAndRoleId(String nodeId, String rId) {
        return ogPersonMapper.selectListByOrgIdAndRoleId(nodeId, rId);
    }

    @Override
    public List<OgPerson> selectListByOrgIdAllAndPostId(String orgId, String postId) {
        return ogPersonMapper.selectListByOrgIdAllAndPostId(orgId, postId);
    }
    @Override
    public List<OgPerson> selectListByOrgIdAllAndPostIdBosc(String orgId, String postId) {
        return ogPersonMapper.selectListByOrgIdAllAndPostIdBosc(orgId, postId);
    }

    @Override
    public OgPerson selectRoleByIdTwo(String userId) {
        return ogPersonMapper.selectRoleByIdTwo(userId);
    }

    @Override
    public List<OgPerson> selectOgPersonJqList(OgPerson ogPerson) {
        return ogPersonMapper.selectOgPersonJqList(ogPerson);
    }

    @Override
    public List<OgPerson> selectOgPersonByDeptId(String deptId) {
        OgPerson ogPerson = new OgPerson();
        ogPerson.setOrgId(deptId);
        ogPerson.setInvalidationMark("1");
        return ogPersonMapper.selectOgPersonJqList(ogPerson);
    }

    @Override
    public List<OgPerson> selectAppAssessPersonByPostId(OgPerson ogPerson) {
        ogPerson.getParams().put("postIds", this.appAssessPostIdList);
        return ogPersonMapper.selectOgPersonByPostIds(ogPerson);
    }

    @Override
    public List<OgPerson> selectAssessList(OgPerson ogPerson) {
        // union???????????????????????????id???????????????noUnion?????????????????????id??????
        ogPerson.getParams().put("orgIds", "union".equals(ogPerson.getRemark()) ? this.gzAnalyOrgIdList : new ArrayList<>());
        ogPerson.getParams().put("postIds", "union".equals(ogPerson.getRemark()) ? this.gzAnalyPostIdList : this.gzLeaderConfirmPostIdList);
        return ogPersonMapper.selectAssessList(ogPerson);
    }

    @Override
    public List<OgPerson> selectPostByuser(OgPerson person) {
        return ogPersonMapper.selectPostByuser(person);
    }

    @Override
    public List<OgPerson> selectAppCheck(OgPerson ogPerson) {
        final String centerDataDirector = "0011";
        final String businessDirector = "0014";
        final String technologyDataDirector = "0016";
        Map<String, Object> params = ogPerson.getParams();
        //  ????????????????????????????????????????????????????????????
        String[] postIds = {centerDataDirector, businessDirector, technologyDataDirector};
        List<String> postIdsList = Arrays.stream(postIds).collect(Collectors.toList());
        params.put("postIds", postIdsList);
        params.put("postId", centerDataDirector);
        return ogPersonMapper.selectAppCheck(ogPerson);
    }

    /**
     * ???????????????-?????????????????????????????????  ??????????????????+??????????????????+??????????????????+??????????????????
     */
    private List<String> appAssessPostIdList = new ArrayList<>();

    /**
     * ???????????????-??????????????????????????????????????????  ??????????????????+??????????????????+??????????????????+???????????????
     */
    private List<String> gzAnalyOrgIdList = new ArrayList<>();

    /**
     * ???????????????-?????????????????????????????????????????? ???????????????????????????+??????????????????????????????
     */
    private List<String> gzAnalyPostIdList = new ArrayList<>();

    /**
     * ????????????????????????????????? ???????????????????????????  + ??????????????????????????????
     */
    private List<String> gzLeaderConfirmPostIdList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /*log.debug("----------????????????key[paraName=app_assess_org]?????????????????????-???????????????-????????????------------");
        List<PubParaValue> assessList = pubParaValueMapper.selectPubParaValueByParaName("app_assess_org");
        for(PubParaValue value : assessList){
            this.appAssessOrgIdList.add(value.getValue());
        }
        log.debug("----------????????????key[paraName=app_assess_org]?????????????????????-???????????????-???????????????: " + assessList + "------------");
        log.debug("----------????????????key[paraName=app_assess_org]?????????????????????-???????????????-????????????------------");*/
        // ??????????????????
        appAssessPostIdList.add("0010");

        /*log.debug("----------????????????key[paraName=gz_analy_org]?????????????????????-????????????????????????-????????????------------");
        List<PubParaValue> analyList = pubParaValueMapper.selectPubParaValueByParaName("gz_analy_org");
        for(PubParaValue value : analyList){
            this.gzAnalyOrgIdList.add(value.getValue());
        }
        log.debug("----------????????????key[paraName=gz_analy_org]?????????????????????-????????????????????????-???????????????: " + analyList + "------------");
        log.debug("----------????????????key[paraName=gz_analy_org]?????????????????????-????????????????????????-????????????------------");*/

        // ??????????????????  +  ??????????????????
        String[] analyPostIds = {"0010", "0015"};
        this.gzAnalyPostIdList = Arrays.stream(analyPostIds).collect(Collectors.toList());
        String[] confirmPostIds = {"0012"};
        // ??????????????????  + ??????????????????
        this.gzLeaderConfirmPostIdList = Arrays.stream(confirmPostIds).collect(Collectors.toList());
    }


    @Override
    public Map<String, String> getBizTypeMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("???????????????", "ZH");
        map.put("???????????????", "KH");
        map.put("??????????????????", "DMT");
        map.put("???????????????", "YY");
        map.put("????????????????????????", "NET");
        map.put("???????????????", "AQ");
        map.put("???????????????", "CS");
        map.put("???????????????", "DL");
        return map;
    }

    /**
     * CMDB????????????
     *
     * @return
     */
//    @Transactional
//    @Override
//    public Map<String, Object> syncPortalData() {
//        Map<String, Object> returnMap = new HashMap<>();
//        String msg = "????????????";
//        int num = 0;
//        Map<String, Object> service = new HashMap<>();
//        service.put("serviceCode", "SyncAwebBankUserToCMDB");
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
//
//        httpHeaders.setContentType(type);
//        String json = JSON.toJSONString(service);
//        httpHeaders.add("reqData", json);
//
//        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
//
//        try {
//            ResponseEntity<String> resp = restTemplate.postForEntity(portalUrl, httpEntity, String.class);
//            if (resp != null && resp.getStatusCodeValue() == 200) {
//                String body = resp.getBody();
//                log.info("??????????????????????????????????????????:" + body);
//                JSONObject jsonObject = JSON.parseObject(body);
//                Map<String, Object> content = (Map<String, Object>) jsonObject.get("content");
//                Map<String, Object> data = (Map<String, Object>) content.get("data");
//
//
//                List<PortalVo> dataList = JSONObject.parseArray(data.get("data").toString(), PortalVo.class);
//                if (!CollectionUtils.isEmpty(dataList)) {
//                    for (PortalVo portalVo : dataList) {
//                        if (null != portalVo) {
//                            this.saveOgUser(portalVo);
//                        }
//                        num++;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        returnMap.put("msg", msg);
//        returnMap.put("num", num);
//        return returnMap;
//    }
//
    @Transactional
    @Override
    public Map<String, Object> syncPortalData() {
        Map<String, Object> returnMap = new HashMap<>();
        String msg = "????????????";
        int num = 0;
        Map<String, Object> service = new HashMap<>();
        service.put("serviceCode", serviceCode);//SyncAwebUser
        HttpHeaders httpHeaders = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");

        httpHeaders.setContentType(type);
        String json = JSON.toJSONString(service);
        httpHeaders.add("reqData", json);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(portalUrl, httpEntity, String.class);
            if (resp != null && resp.getStatusCodeValue() == 200) {
                String body = resp.getBody();
                log.info("??????????????????????????????????????????:" + body);
                JSONObject jsonObject = JSON.parseObject(body);
                Map<String, Object> content = (Map<String, Object>) jsonObject.get("content");
                Map<String, Object> data = (Map<String, Object>) content.get("data");
                List<PortalVo> dataList = JSONObject.parseArray(data.get("data").toString(), PortalVo.class);
                List<OgUserDto> ogUserAddList = new ArrayList();
                List<OgUserDto> ogUserUpdateList = new ArrayList();
                List<OgPersonDto> ogPersonAddList = new ArrayList();
                List<OgPersonDto> ogPersonUpdateList = new ArrayList();
                if (!CollectionUtils.isEmpty(dataList)) {
                    for (PortalVo portalVo : dataList) {
                        if (null != portalVo && !"admin".equals(portalVo.getUsername())) {
                            OgUserDto ogUser = this.updateOgUser(portalVo);
                            if (ogUser.getAddAndUpdate().equals("1")) {
                                ogUserAddList.add(ogUser);
                            } else {
                                ogUserUpdateList.add(ogUser);
                            }
                            if (null != ogUser) {
                                OgPersonDto ogPerson = this.updateOgPerson(portalVo, ogUser);
                                if (ogPerson.getAddAndUpdate().equals("1")) {
                                    ogPersonAddList.add(ogPerson);
                                } else {
                                    ogPersonUpdateList.add(ogPerson);
                                }
                            }
                        }
                        num++;
                    }
                }
                //??????????????????(????????????)
                
                boolean flagUserAdd = false;
                boolean flagPersonAdd = false;
                boolean flagUserupdadte = false;
                if (!CollectionUtils.isEmpty(ogUserAddList)) {
                    flagUserAdd = iOgUsersService.saveBatch(ogUserAddList);
                }
                if (!CollectionUtils.isEmpty(ogUserUpdateList)) {
                    flagUserupdadte = iOgUsersService.updateBatchById(ogUserUpdateList);
                }
                
                if (!CollectionUtils.isEmpty(ogPersonAddList)) {
                    flagPersonAdd = iOgPersonsService.saveBatch(ogPersonAddList);
                }
                if (!CollectionUtils.isEmpty(ogPersonUpdateList)) {
                	flagPersonAdd = iOgPersonsService.updateBatchById(ogPersonUpdateList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            msg = "????????????";
        }
        returnMap.put("msg", msg);
        returnMap.put("num", num);
        return returnMap;
    }


    public List<PortalVo> dataLists() {
        List<PortalVo> portalVoList = new ArrayList<>();
        PortalVo portalVo = new PortalVo();
        portalVo.setName("??????2");
        portalVo.setId("mzy");
        portalVo.setStatus("1");
        portalVoList.add(portalVo);
        return portalVoList;
    }
	public Long cRc32(String msg) {
		  Checksum checksum = new CRC32();
		    byte[] b = msg.getBytes();
		    checksum.update(b,0,b.length);
		long content=checksum.getValue();
		return content;
	}
    
    /**
     * ?????????????????????
     *
     * @param portal
     * @return map
     */
    public OgUserDto updateOgUser(PortalVo portal) {
        String username = ogUserMapper.selectUserByUsername(portal.getUsername());
        OgUserDto ogUser = new OgUserDto();
       
        String portalStatus = portal.getStatus();
        String state = portal.getState();
        if(null==portalStatus || "".equals(portalStatus)) {
        	portalStatus=state;
        }
        if(null==portalStatus || "".equals(portalStatus)) {
        	portalStatus="0";
        }
        String[] split = portalStatus.split("@");
        if (StringUtils.isEmpty(username)) {
        	String uuid = UUID.getUUIDStr();
            ogUser.setUsername(portal.getUsername());
            ogUser.setPassword(Md5OriginalUtils.md5Password("Bosc@1234"));
            ogUser.setUserId(uuid);
            ogUser.setPId(uuid);
            if (Integer.valueOf(split[0]) == 1 || portal.getStatus() == null) {
                ogUser.setInvalidationMark("1");
            } else {
                ogUser.setInvalidationMark("0");
            }
            ogUser.setUpdateTime(DateUtils.dateTimeNow());
            ogUser.setAddAndUpdate("1");
            //int rows = ogUserMapper.insertOgUser(user);
        } else {
            OgUser ogUsers = ogUserMapper.selectTimeByUsername(username);
            if (Integer.valueOf(split[0]) == 1 || portal.getStatus() == null) {
                ogUser.setInvalidationMark("1");
            } else {
                ogUser.setInvalidationMark("0");
            }
            ogUser.setPId(ogUsers.getpId());
            ogUser.setUserId(ogUsers.getUserId());
            ogUser.setUsername(portal.getUsername());
            ogUser.setPassword(ogUsers.getPassword());
            ogUser.setAddAndUpdate("2");
            // ogUserMapper.updateOgUser(ogUser);
        }
        return ogUser;
    }

    /**
     * ???????????????
     *
     * @param portal
     * @return map
     */
    public OgPersonDto updateOgPerson(PortalVo portal, OgUserDto user) {
        String userName = ogUserMapper.selectPersonByUsername(portal.getUsername());
        OgPersonDto person = new OgPersonDto();
        if (StringUtils.isEmpty(userName)) {
//        	String uuid = UUID.getUUIDStr();
            OgOrg ogOrg = sysDeptMapper.selectDeptByCode(portal.getDepCode());
            person.setPid(user.getPId());
            person.setPid_crc(this.cRc32(user.getPId()));
            if (null != ogOrg) {
                person.setOrgid(ogOrg.getOrgId());
                //??????????????????
                if (StringUtils.isNotEmpty(ogOrg.getOrgId())) {
                    OgOrg org = sysDeptService.getPartakeOrg(ogOrg.getOrgId());
                    if (null != org) {
                        person.setPartakeOrgId(org.getOrgId());
                    }
                }
            }
            person.setInvalidationMark(user.getInvalidationMark());
            if (StringUtils.isNotEmpty(portal.getNickname())) {
                person.setPname(portal.getNickname());
            }
            if (StringUtils.isNotEmpty(portal.getGender())) {
                person.setSex(portal.getGender());
            }
            if (StringUtils.isNotEmpty(portal.getMail())) {
            	person.setEmail(portal.getMail());
            }
            if (StringUtils.isNotEmpty(portal.getTelephone())) {
            	person.setMobilPhone(portal.getTelephone());
            }
            if (StringUtils.isNotEmpty(portal.getHighLevel())) {
                person.setEdu(portal.getHighLevel());
            }
           
            if (StringUtils.isNotEmpty(portal.getDuty())) {
                //????????????
                person.setPosition(portal.getDuty());
            }
            if (StringUtils.isNotEmpty(portal.getBrno())) {
                //????????????
                person.setOrgid(portal.getBrno());;
            }
            person.setUpdateTime(DateUtils.dateTimeNow());
            person.setAddAndUpdate("1");
            // ogPersonMapper.insertOgPerson(person);
        } else {
//            OgPerson persons = ogPersonMapper.selectOgPersonById(user.getPId());
//            if (null == person) {
//                persons = new OgPerson();
//            }
        	person.setPid_crc(this.cRc32(user.getPId()));
            person.setPid(user.getPId());
            person.setInvalidationMark(user.getInvalidationMark());
            if (StringUtils.isNotEmpty(portal.getNickname())) {
                person.setPname(portal.getNickname());
            }
            if (StringUtils.isNotEmpty(portal.getMail())) {
            	person.setEmail(portal.getMail());
            }
            if (StringUtils.isNotEmpty(portal.getTelephone())) {
            	person.setMobilPhone(portal.getTelephone());
            }
            if (StringUtils.isNotEmpty(portal.getGender())) {
                person.setSex(portal.getGender());
            }
            if (StringUtils.isNotEmpty(portal.getHighLevel())) {
                person.setEdu(portal.getHighLevel());
            }
            if (StringUtils.isNotEmpty(portal.getDuty())) {
                //????????????
                person.setPosition(portal.getDuty());
            }
            if (StringUtils.isNotEmpty(portal.getBrno())) {
                //????????????
                person.setOrgid(portal.getBrno());
            }
            person.setAddAndUpdate("2");
            //ogPersonMapper.updateOgPerson(ogPerson);
        }
        return person;
    }


//    private void saveOgUser(PortalVo portal) {
//        //  ????????????  og_user ???????????? ???????????? ????????????
//        String username = ogUserMapper.selectUserByUsername(portal.getId());
//
//        OgUser user = new OgUser();
//        OgPerson person = new OgPerson();
//        String uuid = UUID.getUUIDStr();
//        String portalStatus = portal.getStatus();
//        String[] split = portalStatus.split("@");
//        if (StringUtils.isEmpty(username)) {
//            user.setUsername(portal.getId());
//            user.setPassword(Md5OriginalUtils.md5Password("Bosc@1234"));
//            user.setUserId(uuid);
//            user.setpId(uuid);
//            if (Integer.valueOf(split[0]) == 1 || portal.getStatus() == null) {
//                user.setInvalidationMark("1");
//            } else {
//                user.setInvalidationMark("0");
//            }
//            user.setUpdateTime(DateUtils.dateTimeNow());
//            int rows = ogUserMapper.insertOgUser(user);
//            //  ????????????  portal?????? portal.getDepCode()  og_org?????? ?????? ?????? orgid
//            OgOrg ogOrg = sysDeptMapper.selectDeptByCode(portal.getDepCode());
//            if (rows > 0) {
//                person.setpId(uuid);
//                if (null != ogOrg) {
//                    person.setOrgId(ogOrg.getOrgId());
//                    //??????????????????
//                    if (StringUtils.isNotEmpty(ogOrg.getOrgId())) {
//                        OgOrg org = sysDeptService.getPartakeOrg(ogOrg.getOrgId());
//                        if (null != org) {
//                            person.setPartakeOrgId(org.getOrgId());
//                        }
//                    }
//                }
//                person.setInvalidationMark(user.getInvalidationMark());
//                if (StringUtils.isNotEmpty(portal.getName())) {
//                    person.setpName(portal.getName());
//                }
//                if (StringUtils.isNotEmpty(portal.getGender())) {
//                    person.setSex(portal.getGender());
//                }
//                if (StringUtils.isNotEmpty(portal.getHighLevel())) {
//                    person.setEdu(portal.getHighLevel());
//                }
//                if (StringUtils.isNotEmpty(portal.getDuty())) {
//                    //????????????
//                    person.setPosition(portal.getDuty());
//                }
//                person.setAddtime(DateUtils.dateTimeNow());
//
//                ogPersonMapper.insertOgPerson(person);
//            }
//        } else {
//            OgUser ogUser = ogUserMapper.selectTimeByUsername(username);
//            if (Integer.valueOf(split[0]) == 1 || portal.getStatus() == null) {
//                ogUser.setInvalidationMark("1");
//            } else {
//                ogUser.setInvalidationMark("0");
//            }
//            ogUserMapper.updateOgUser(ogUser);
//            OgPerson ogPerson = ogPersonMapper.selectOgPersonById(ogUser.getpId());
//            if (null == ogPerson) {
//                ogPerson = new OgPerson();
//            }
//            ogPerson.setInvalidationMark(ogUser.getInvalidationMark());
//            ogPerson.setpId(ogUser.getpId());
//
//            if (StringUtils.isNotEmpty(portal.getName())) {
//                person.setpName(portal.getName());
//            }
//            if (StringUtils.isNotEmpty(portal.getGender())) {
//                person.setSex(portal.getGender());
//            }
//            if (StringUtils.isNotEmpty(portal.getHighLevel())) {
//                person.setEdu(portal.getHighLevel());
//            }
//            if (StringUtils.isNotEmpty(portal.getDuty())) {
//                //????????????
//                person.setPosition(portal.getDuty());
//            }
//            ogPersonMapper.updateOgPerson(ogPerson);
//        }
//    }

    /**
     * ????????????ID????????????ID?????????????????????????????????
     *
     * @param requestMap
     * @return
     */
    @Override
    public List<Map<String, String>> selectOgPersonByRoleIdORByPostId(Map<String, String> requestMap) {

        List<Map<String, String>> objects = new ArrayList<>();

        OgPerson ogPerson = new OgPerson();
        if (StringUtils.isNotEmpty(requestMap.get("roleId"))) {
            ogPerson.setrId(requestMap.get("roleId"));
        } else if (StringUtils.isNotEmpty(requestMap.get("postId"))) {
            ogPerson.getParams().put("postId", requestMap.get("postId"));
        } else {
            return objects;
        }

        try {
            ogPerson.getParams().put("orgIdList", getOrgIds());
            List<OgPerson> list = ogPersonMapper.selectOgPersonByRoleIdORByPostId(ogPerson);
            for (OgPerson person : list) {
                Map<String, String> map = new HashMap<>();
                map.put("value", person.getpId());
                map.put("label", person.getpName());
                objects.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }


    private List<String> getOrgIds() {

        List<String> orgIdList = new ArrayList<>();

        try {
            OgPerson person = ogPersonMapper.selectOgPersonEvoById(ShiroUtils.getUserId());
            OgOrg ogOrg = sysDeptMapper.selectDeptById(person.getOrgId());
            orgIdList.add(ogOrg.getOrgId());

            if (StringUtils.isNotEmpty(ogOrg.getParentId())) {
                while (!"310100001".equals(ogOrg.getParentId())) {
                    ogOrg = sysDeptMapper.selectDeptById(ogOrg.getParentId());
                    orgIdList.add(ogOrg.getOrgId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orgIdList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param roleId
     * @return
     */
    @Override
    public List<LabelValue> selectPersonForRoleId(String roleId, String userId) {

        //String userId = ShiroUtils.getUserId();
        OgPerson person = ogPersonMapper.selectOgPersonById(userId);
        OgOrg ogOrg = sysDeptMapper.selectDeptById(person.getOrgId());

        OgPerson ogPerson = new OgPerson();
        ogPerson.setrId(roleId);
        ogPerson.getParams().put("orgIdList", ogOrg.getOrgId().split(","));
        List<LabelValue> labelValues = getPerson(ogPerson);

        return labelValues;
    }

    private List<LabelValue> getPerson(OgPerson ogPerson) {

        List<LabelValue> objects = new ArrayList<>();

        try {
            List<OgPerson> list = ogPersonMapper.selectOgPersonByRoleIdORByPostId(ogPerson);
            for (OgPerson person1 : list) {

                LabelValue labelValue = new LabelValue();
                labelValue.setValue(person1.getpId());
                labelValue.setLabel(person1.getpName());

                //Map<String, String> map = new HashMap<>();
                //map.put("value", person1.getpId());
                //map.put("label", person1.getpName());

                objects.add(labelValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param roleId
     * @return
     */
    @Override
    public List<LabelValue> selectLenderForRoleId(String roleId) {

        List<LabelValue> objects = new ArrayList<>();
        OgPerson ogPerson = new OgPerson();
        ogPerson.setrId(roleId);
        ogPerson.getParams().put("orgIdList", getOrgIds());
        objects = getPerson(ogPerson);

        return objects;
    }

    public int updateAgencyIsNull(OgPerson ogPerson) {

        return ogPersonMapper.updateAgencyIsNull(ogPerson);
    }

    /**
     * tinyWeb???????????????
     *
     * @return
     */
    @Override
    public List<LabelValue> selectCheckForTinyWeb(String postIds) {

        List<LabelValue> labelValues = new ArrayList<>();
        List<OgPerson> personList = new ArrayList<>();
        if (StringUtils.isNotEmpty(postIds)) {

            try {

                OgPerson person = new OgPerson();
                person.getParams().put("postIds", postIds.split(","));
                personList = ogPersonMapper.selectCheckForTinyWeb(person);
                if (!CollectionUtils.isEmpty(personList)) {

                    for (OgPerson ogPerson : personList) {

                        LabelValue labelValue = new LabelValue();
                        labelValue.setValue(ogPerson.getpId());
                        labelValue.setLabel(ogPerson.getpName());
                        labelValue.setSpare(ogPerson.getOrgId());
                        labelValues.add(labelValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return labelValues;
    }

    /**
     * tinyWeb????????????????????? ????????? ???????????????????????????
     *
     * @return
     */
    @Override
    public List<LabelValue> selectContactPersonForTinyWeb(String orgId) {

        List<LabelValue> labelValues = new ArrayList<>();
        List<OgPerson> personList = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgId)) {

            try {

                OgPerson person = new OgPerson();
                person.setOrgId(orgId);
                personList = ogPersonMapper.selectContactPersonForTinyWeb(person);
                if (!CollectionUtils.isEmpty(personList)) {

                    for (OgPerson ogPerson : personList) {

                        LabelValue labelValue = new LabelValue();
                        labelValue.setValue(ogPerson.getpId());
                        labelValue.setLabel(ogPerson.getpName());

                        labelValues.add(labelValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return labelValues;
    }

    //???????????????ID?????????person??????
    public Map<String, Object> setPersonJoinOrgId(OgPerson ogPerson) {

        Map<String, Object> map = new HashMap<>();
        int num = 0;
        String msg = "????????????";
        List<OgPerson> personList = new ArrayList<>();
        try {

            personList = ogPersonMapper.selectOgPersonList(ogPerson);
            if (!CollectionUtils.isEmpty(personList)) {

                for (OgPerson person : personList) {

                    //????????????ID
                    String partakeId = "";

                    String levelCode = "";
                    String orgId = person.getOrgId();
                    OgOrg ogOrg = sysDeptMapper.selectDeptById(orgId);
                    if (null != ogOrg) {

                        levelCode = ogOrg.getLevelCode();
                        if (ogOrg.getType().equals("0")) {

                            if (levelCode.length() > 40) {
                                if (ogOrg.getOrgId().equals("310200811") || ogOrg.getOrgId().equals("310201107")) {
                                    levelCode = ogOrg.getLevelCode().substring(0, 50);
                                } else {
                                    levelCode = ogOrg.getLevelCode().substring(0, 40);
                                }
                            }
                        } else if (ogOrg.getType().equals("1")) {

                            if (levelCode.length() > 30) {
                                levelCode = ogOrg.getLevelCode().substring(0, 30);
                            }
                        }

                        OgOrg org = new OgOrg();
                        org.getParams().put("levelCode", levelCode);
                        List<OgOrg> orgList = sysDeptMapper.selectDeptList(org);
                        if (!CollectionUtils.isEmpty(orgList)) {
                            partakeId = orgList.get(0).getOrgId();
                        }

                        //??????????????????????????????????????? ??????????????????????????????
                        if ("310200176".equals(partakeId) || "310200958".equals(partakeId)) {
                            partakeId = sysDeptMapper.selectDeptById(orgId).getOrgId();
                            //???????????????????????????????????????????????? ?????????????????????????????????
                            if ("310201107".equals(org.getOrgId())) {
                                partakeId = sysDeptMapper.selectDeptById("310200811").getOrgId();
                            }
                        }

                        OgPerson ogPerson1 = new OgPerson();
                        ogPerson1.setpId(person.getpId());
                        ogPerson1.setPartakeOrgId(partakeId);
                        ogPersonMapper.updateOgPerson(ogPerson1);
                        num++;
                    }
                }
                msg = "????????????";
            } else {
                msg = "????????????";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("msg", msg);
        map.put("num", num);
        return map;
    }

    @Autowired
    private MailUtils mailUtils;
    public String sendComplexMailExec(String taskNo,String userId, String subject, String content, String imgPath, String filePath1, String filePath2) {
        OgPerson ogPerson =  new OgPerson();
        ogPerson.setUserid(userId);
        OgPerson o = ogPersonMapper.selectUserInfo(ogPerson);
        String receivers= o.getEmail();
        return mailUtils.sendComplexMail( taskNo, receivers, null,  subject,  content,  imgPath,  filePath1,  filePath2);
    }
    @Autowired
    private SmsUtils smsUtils;
    public String sendSmsMessageBySocketExec(String taskNo,String userId, String contents) {
        OgPerson ogPerson =  new OgPerson();
        ogPerson.setUserid(userId);
        OgPerson o = ogPersonMapper.selectUserInfo(ogPerson);
        String mobiles=o.getMobilPhone();
        return smsUtils.sendSmsMessageBySocket( taskNo, mobiles,  contents);
    }

    @Override
    public OgPerson selectOgPersonByPidForUpdateStatus(String pId) {
        return ogPersonMapper.selectOgPersonByPidForUpdateStatus(pId);
    }


    public List<OgPerson> selectListByOrgIdAllAndGroupId(String orgId, String groupId){
        return ogPersonMapper.selectListByOrgIdAllAndGroupId(orgId,groupId);
    }
}
