//package com.ruoyi.form.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.ruoyi.common.core.domain.entity.OgOrg;
//import com.ruoyi.common.core.domain.entity.OgPerson;
//import com.ruoyi.common.core.domain.entity.OgUser;
//import com.ruoyi.common.utils.DateUtils;
//import com.ruoyi.common.utils.StringUtils;
//import com.ruoyi.common.utils.security.Md5OriginalUtils;
//import com.ruoyi.common.utils.uuid.UUID;
//import com.ruoyi.form.domain.PortalVo;
//import com.ruoyi.form.service.PortalService;
//import com.ruoyi.system.mapper.SysDeptMapper;
//import com.ruoyi.system.service.IOgPersonService;
//import com.ruoyi.system.service.IOgUserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 统一门户同步人员同步新增、禁用。等功能
// * @author jiang
// * @Date 2022-08-20
// * @param {"serviceCode":"SyncAwebBankUserToCMDB"}
// */
//
//
//
//
//@Service
//public class PortalServiceImpl implements PortalService {
//
//    private final Logger log = LoggerFactory.getLogger(PortalServiceImpl.class);
//
//    @Value("${portal.url}")
//    private String portalUrl ;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private IOgUserService ogUserService;
//
//    @Autowired
//    private IOgPersonService ogPersonService;
//
//    @Autowired
//    private SysDeptMapper sysDeptMapper;
//
//    @Transactional
//    @Override
//    public void syncPortalData() {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        MediaType type = MediaType.APPLICATION_FORM_URLENCODED;
//        httpHeaders.setContentType(type);
//        Map<String, Object> params = new HashMap<>();
//        Map<String, Object> service = new HashMap<>();
//        service.put("serviceCode", "SyncAwebBankUserToCMDB");
//        params.put("reqData", service);
//        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, httpHeaders);
//
//
//              ResponseEntity<String> resp = restTemplate.postForEntity(portalUrl, httpEntity, String.class);
//        if(resp != null && resp.getStatusCodeValue() == 200) {
//            String body = resp.getBody();
//            log.info("运维管理平台收到门户用户数据:" + body);
//            JSONObject jsonObject = JSON.parseObject(body);
//            Map<String, Object> content = (Map<String, Object>) jsonObject.get("content");
//            Map<String, Object> data = (Map<String, Object>) content.get("data");
//
//
//            List<PortalVo> dataList = JSONObject.parseArray(data.get("data").toString(), PortalVo.class);
//            if(!CollectionUtils.isEmpty(dataList)) {
//                dataList.forEach(portal -> {
//                    if(portal != null) {
//                        this.saveOgUser(portal);
//                    }
//                });
//
//
//            }
//        }
//    }
//
//    private void saveOgUser(PortalVo portal) {
//        //  首先判断  og_user 有用户名 如果没有 新增用户
//        String username = ogUserService.selectUserByUsername(portal.getId());
//
//        OgUser user = new OgUser();
//        OgPerson person = new OgPerson();
//        String uuid = UUID.getUUIDStr();
//        String portalStatus = portal.getStatus();
//        String[] split = portalStatus.split("@");
//
//
//        if(StringUtils.isEmpty(username)) {
//
//           // String uuid = UUID.getUUIDStr();
//          //  OgUser user = new OgUser();
//            user.setUsername(portal.getId());
//            user.setPassword(Md5OriginalUtils.md5Password("Bosc@1234"));
//            user.setUserId(uuid);
//            user.setpId(uuid);
//
//
//            if (Integer.valueOf(split[0])==1|| portal.getStatus() == null){
//                user.setInvalidationMark("1");
//            }else {
//                user.setInvalidationMark("0");
//            }
//
//            user.setUpdateTime(DateUtils.dateTimeNow());
//
//            int rows = ogUserService.insertOgUser(user);
//           //  通过查询  portal对象 portal.getDepCode()  og_org对象 获取 字段 orgid
//            OgOrg ogOrg = sysDeptMapper.selectDeptByCode(portal.getDepCode());
//
//
//                if (rows > 0) {
//                   // OgPerson person = new OgPerson();
//                    person.setpId(uuid);
//
//                    person.setOrgId(ogOrg.getOrgId());
//
//                    if (Integer.valueOf(split[0])==1|| portal.getStatus() == null){
//                        person.setInvalidationMark("1");
//                    }else {
//                        person.setInvalidationMark("0");
//                    }
//
//
//                    person.setpName(portal.getName());
//                    person.setSex(portal.getGender());
//                    person.setEdu(portal.getHighLevel());
//                    //同步职务
//                    person.setPosition(portal.getDuty());
//
//                    person.setAddtime(DateUtils.dateTimeNow());
//
//                    ogPersonService.insertOgPerson(person);
//                }
//
//
//
//            }else {
//
//            OgUser ogUser = ogUserService.selectTimeByUsername(username);
//
//            if (Integer.valueOf(split[0])==1 || portal.getStatus() == null ){
//                ogUser.setInvalidationMark("1");
//            }else {
//                ogUser.setInvalidationMark("0");
//            }
//
//
//            // where userid = #{userId}
//            ogUserService.updateOgUser(ogUser);
//
//
//            OgPerson ogPerson = ogPersonService.selectOgPersonById(ogUser.getpId());
//
//            if (Integer.valueOf(split[0])==1 || portal.getStatus() == null ){
//                ogPerson.setInvalidationMark("1");
//            }else {
//               ogPerson.setInvalidationMark("0");
//            }
//
//                   ogPerson.setpId(ogUser.getpId());
//                   ogPerson.setpName(portal.getName());
//                    ogPerson.setSex(portal.getGender());
//                    ogPerson.setEdu(portal.getHighLevel());
//                    person.setPosition(portal.getDuty());
//
//
//            // person.setOrgId(ogOrg.getOrgId());
//
//            ogPersonService.updateOgPerson(ogPerson);
//        }
//
//    }
//}
