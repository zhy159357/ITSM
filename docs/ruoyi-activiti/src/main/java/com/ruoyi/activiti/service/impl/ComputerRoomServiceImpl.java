package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.ComputerModule;
import com.ruoyi.activiti.domain.ComputerRoom;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.mapper.ComputerRoomMapper;
import com.ruoyi.activiti.service.IComputerRoomService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.OgPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class ComputerRoomServiceImpl implements IComputerRoomService {

    @Autowired
    private ComputerRoomMapper computerRoomMapper;

    @Autowired
    private PubParaValueMapper pubParaValueMapper;

    @Autowired
    private OgPostService ogPostService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private ISysUserService userService;

    @Override
    public List<OgPerson> selectUserList(OgPerson person) {
        List<OgPerson> personList =  computerRoomMapper.selectUserList(person);

        return personList;
    }

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public String getContentMap() {
        //Map<String, Object> map = new HashMap<>();
        StringBuilder contentTypeHtml = new StringBuilder();
        StringBuilder branchHtml = new StringBuilder();
        List<PubParaValue> values = pubParaValueMapper.selectPubParaValueByParaName("content_type");
        for (int i = 0; i < values.size(); i++) {
            PubParaValue value = values.get(i);
            contentTypeHtml.append("<label class=\"checkbox-inline check-box\">");
            contentTypeHtml.append("<input id=\"versionType" + i + "\" type=\"checkbox\" name=\"versionType\" value=\"" + value.getValue() + "\" checked>" + value.getValueDetail());
            contentTypeHtml.append("</label>");
        }
        //map.put("contentTypeHtml", contentTypeHtml.toString());
        return contentTypeHtml.toString();
    }

    @Override
    public List<ComputerRoom> workContent() {

        List<ComputerRoom> computerRoomList = computerRoomMapper.selectPubParaValueByParaName("content_type");
        return computerRoomList;
    }

    @Override
    public String getStatusByUserId(String userId) {
        String status = "0";//??????
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId, null);
        for (OgPost op : list) {
            if (PostConstants.SUPERADMIN.equals(op.getPostId()) || PostConstants.SJZXLD.equals(op.getPostId()) || PostConstants.SJZX_CZ.equals(op.getPostId()) ) {
                status = "1";//????????????????????????????????????
                break;
            }
        }
        if("1".equals(status)){
            return status;
        }

        for (OgPost op : list) {
            if (PostConstants.SJZX_CZ.equals(op.getPostId())) {
                status = "2";//??????????????????
                break;
            }
        }
        if("2".equals(status)){
            return status;
        }

        for (OgPost op : list) {
            if (PostConstants.JF_DLG.equals(op.getPostId())) {
                status = "3";//??????????????????
                break;
            }
        }
        if("3".equals(status)){
            return status;
        }
        return status;
    }

    @Override
    public List<OgPerson> getOgPersonList(String userId) {
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String orgId = userPerson.getOrgId();
        //????????????????????????????????????
        List<OgPerson> personList = iOgPersonService.selectOgPersonOrgById(orgId);

        return personList;
    }

    @Override
    @Transactional
    public int insertComputerRoomApply(ComputerRoom computerRoom){
        int rows = 0;
        String smsText = "";
        try{
            //??????status????????????????????????????????????????????????????????????????????????????????????
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
            String nowDate = df.format(new Date());
            computerRoom.setCreateTimes(nowDate);
            computerRoom.setUpdateTimes(nowDate);
            if("0".equals(computerRoom.getStatus()) || "3".equals(computerRoom.getStatus())){
                //?????????????????????????????????????????? 0 ?????????????????????
                computerRoom.setApplyState("0");
                rows = computerRoomMapper.insertComputerRoomApply(computerRoom);
                //????????????????????????
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(computerRoom.getAuditorId());

                OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(computerRoom.getApplyUserId());
                smsText = "???????????????????????????????????????????????????????????????" +computerRoom.getComputerApplyNo()+ "???????????????"+ogPerson1.getpName() + ",????????????????????????????????????";
                sendSms(ogPerson,smsText);
            }else {
                //??????????????????????????????????????????????????????1 ????????????
                computerRoom.setApplyState("1");
                computerRoom.setAuditorId(ShiroUtils.getOgUser().getUserId());
                computerRoom.setReason("??????");
                rows = computerRoomMapper.insertComputerRoomApply(computerRoom);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return rows;
    }

    @Override
    public List<ComputerRoom> computerRoomApplylist(ComputerRoom computerRoom) {

        if("mysql".equals(dbType)){
            return computerRoomMapper.computerRoomApplylistMysql(computerRoom);
        }else{
            return computerRoomMapper.computerRoomApplylist(computerRoom);
        }
    }

    @Override
    public ComputerRoom selectComputerRoomApplyById(String id) {
        return computerRoomMapper.selectComputerRoomApplyById(id);
    }

    @Override
    public int upadateComputerRoomApply(ComputerRoom computerRoom) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        String nowDate = df.format(new Date());
        computerRoom.setUpdateTimes(nowDate);
        String smsText = "";
        int row = 0;

        if(StringUtils.isEmpty(computerRoom.getBelongings())){
            computerRoom.setInOutType("");
        }

        String status = getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        if("0".equals(status) || "3".equals(status)){
            computerRoom.setApplyState("0");
            row = computerRoomMapper.upadateComputerRoomApply(computerRoom);

            //????????????????????????
            OgPerson ogPerson = iOgPersonService.selectOgPersonById(computerRoom.getAuditorId());

            OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(computerRoom.getApplyUserId());
            smsText = "???????????????????????????????????????????????????????????????"+computerRoom.getComputerApplyNo()+"???????????????"+ogPerson1.getpName() + ",????????????????????????????????????";
            sendSms(ogPerson,smsText);
        }else {
            computerRoom.setApplyState("1");
            computerRoom.setAuditorId(ShiroUtils.getOgUser().getUserId());
            computerRoom.setReason("??????");
            row = computerRoomMapper.upadateComputerRoomApply(computerRoom);
        }

        return row;
    }

    @Override
    public List<ComputerRoom> selectComputerRoomAuditlist(ComputerRoom computerRoom) {

        if("mysql".equals(dbType)){
            return computerRoomMapper.selectComputerRoomAuditlistMysql(computerRoom);
        }else{
            return computerRoomMapper.selectComputerRoomAuditlist(computerRoom);
        }

    }

    @Override
    public int updateComputerRoomApplyState(ComputerRoom computerRoom) {

        String smsText = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        String nowDate = df.format(new Date());
        computerRoom.setUpdateTimes(nowDate);

        //????????????????????????
        List<String> accompanyList = new ArrayList<>();
        accompanyList.add(computerRoom.getAccompanyUserId());
        if(!StringUtils.isEmpty(computerRoom.getAccompanyUserIdTwo())){
            accompanyList.add(computerRoom.getAccompanyUserIdTwo());
        }

        int row = computerRoomMapper.updateComputerRoomApplyState(computerRoom);

        //????????????????????????
        if("1".equals(computerRoom.getApplyState())){
            for (String id : accompanyList) {
                //?????????
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(computerRoom.getApplyUserId());
                //?????????
                OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(id);
                smsText = "????????????????????????????????????????????????????????????"+computerRoom.getComputerApplyNo()+ "???????????????"+ogPerson.getpName() + ",????????????????????????????????????";
                sendSms(ogPerson1,smsText);
            }

        }

        return row;
    }

    @Override
    public List<ComputerRoom> selectComputerRoomRegisterList(ComputerRoom computerRoom) {
        if("mysql".equals(dbType)){
            return computerRoomMapper.selectComputerRoomRegisterListMysql(computerRoom);
        }else{
            return computerRoomMapper.selectComputerRoomRegisterList(computerRoom);
        }

    }

    @Override
    public int updateComputerRoomRegister(ComputerRoom computerRoom) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        String nowDate = df.format(new Date());
        computerRoom.setUpdateTimes(nowDate);
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getOgUser().getUserId());
        computerRoom.setRegisterName(ogPerson.getpName());

        return computerRoomMapper.updateComputerRoomRegister(computerRoom);
    }

    @Override
    public List<Map> getIntoList(ComputerRoom computerRoom) {

        String intoName = computerRoom.getIntoName();
        List<Map> list = new ArrayList<>();
        if (StringUtils.isEmpty(intoName)) {
            return list;
        }

        if (StringUtils.isNotEmpty(intoName)) {
            String[] intoNames = computerRoom.getIntoName().split(",");
            String[] intoIdNumbers = computerRoom.getIntoIdNumber().split(",");
            String[] intoPhones = computerRoom.getIntoPhone().split(",");
            String[] intoUnits = computerRoom.getIntoUnit().split(",");

            for (int i = 0; i < intoNames.length; i++) {
                Map map = new HashMap<String, String>();
                String name = intoNames[i];
                String idNumber = intoIdNumbers[i];
                String phone = intoPhones[i];
                String unit = intoUnits[i];

                map.put("intoName", name);
                map.put("intoIdNumber", idNumber);
                map.put("intoPhone", phone);
                map.put("intoUnit", unit);
                list.add(map);
            }

        }

        return list;
    }

    //getBelongingsList
    @Override
    public List<Map> getBelongingsList(ComputerRoom computerRoom) {

        String belongings = computerRoom.getBelongings();
        List<Map> list = new ArrayList<>();
        if (StringUtils.isEmpty(belongings)) {
            return list;
        }

        if (StringUtils.isNotEmpty(belongings)) {
            String[] belongingArr = computerRoom.getBelongings().split(",");
            String[] belongingsNumArr = computerRoom.getBelongingsNum().split(",");
            String[] inOutTypeArr = computerRoom.getInOutType().split(",");
            String[] installSiteArr = computerRoom.getInstallSite().split(",");

            for (int i = 0; i < belongingArr.length; i++) {
                Map map = new HashMap<String, String>();
                String belonging = belongingArr[i];
                String belongingsNum = belongingsNumArr[i];
                String inOutType = inOutTypeArr[i];
                String installSite = installSiteArr[i];

                map.put("belongings", belonging);
                map.put("belongingsNum", belongingsNum);
                map.put("inOutType", inOutType);
                map.put("installSite", installSite);
                list.add(map);
            }

        }

        return list;
    }

    @Override
    public List<Ztree> selectTree(ComputerModule computerModule) {
        List<Ztree> ztrees = initZtree(computerRoomMapper.selectTree(computerModule));
        return ztrees;
    }

    @Override
    public List<ComputerModule> selectComputerModuleList(ComputerModule cm) {

        if("mysql".equals(dbType)){
            return computerRoomMapper.selectComputerModuleMysqlList(cm);
        }else{
            return computerRoomMapper.selectComputerModuleList(cm);
        }
    }

    @Override
    public ComputerModule selectComputerModuleById(String id) {
        return computerRoomMapper.selectComputerModuleById(id);
    }

    @Override
    public int insertComputerModule(ComputerModule computerModule) {
        return computerRoomMapper.insertComputerModule(computerModule);
    }

    @Override
    public int updateComputerModule(ComputerModule cm) {
        return computerRoomMapper.updateComputerModule(cm);
    }

    @Override
    public List<ComputerModule> selectComputerModuleCenter() {
        return computerRoomMapper.selectComputerModuleCenter();
    }

    @Override
    public List<ComputerModule> selectComputerModuleForCenter(ComputerModule computerModule) {
        return computerRoomMapper.selectComputerModuleForCenter(computerModule);
    }

    @Override
    public int insertTsComputerRoomApply(ComputerRoom computerRoom) {
        //??????status????????????????????????????????????????????????????????????????????????????????????
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        String nowDate = df.format(new Date());
        computerRoom.setCreateTimes(nowDate);
        computerRoom.setUpdateTimes(nowDate);
        int rows = 0;
        computerRoom.setApplyState("-1");
        rows = computerRoomMapper.insertComputerRoomApply(computerRoom);

        return rows;
    }

    @Override
    public Map<String, List> getApplyForRegister(ComputerRoom computerRoom) {

        Map<String, List> map = new HashMap<>();

        //????????????????????????
        List<String> accompanyList = new ArrayList<>();
        accompanyList.add(computerRoom.getAccompanyUserName());
        if(!StringUtils.isEmpty(computerRoom.getAccompanyUserNameTwo())){
            accompanyList.add(computerRoom.getAccompanyUserNameTwo());
        }
        map.put("accompanyList",accompanyList);

        List<String> intoNameList = new ArrayList<>();
        //??????????????????
        if(!StringUtils.isEmpty(computerRoom.getIntoName())){
            String[] intoNames = computerRoom.getIntoName().split(",");
            for (int i = 0; i < intoNames.length; i++) {
                intoNameList.add(intoNames[i]);
            }
        }
        map.put("intoNameList",intoNameList);

        //??????????????????
        List<String> moduleList = new ArrayList<>();
        if(!StringUtils.isEmpty(computerRoom.getComputerRoomModuleName())){
            String[] moduleNames = computerRoom.getComputerRoomModuleName().split(",");
            for (int i = 0; i < moduleNames.length; i++) {
                moduleList.add(moduleNames[i]);
            }
        }
        map.put("moduleList",moduleList);

        return map;
    }

    @Override
    public List<Map> getRealityBelongingsList(ComputerRoom computerRoom) {
        String belongings = computerRoom.getRealityBelongings();
        List<Map> list = new ArrayList<>();
        if (StringUtils.isEmpty(belongings)) {
            return list;
        }

        if (StringUtils.isNotEmpty(belongings)) {
            String[] belongingArr = computerRoom.getRealityBelongings().split(",");
            String[] belongingsNumArr = computerRoom.getRealityBelongingsNum().split(",");

            for (int i = 0; i < belongingArr.length; i++) {
                Map map = new HashMap<String, String>();
                String belonging = belongingArr[i];
                String belongingsNum = belongingsNumArr[i];

                map.put("realityBelongings", belonging);
                map.put("realityBelongingsNum", belongingsNum);

                list.add(map);
            }

        }

        return list;
    }

    @Override
    public List<OgUser> selectAllocatedListPost(OgUser user) {
        return computerRoomMapper.selectAllocatedListPost(user);
    }

    public List<Ztree> initZtree(List<ComputerModule> categoryList) {
        List<Ztree> ztrees = initZtree(categoryList, null);
        return ztrees;
    }

    public List<Ztree> initZtree(List<ComputerModule> categoryList, List<String> roleDeptList) {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (ComputerModule category : categoryList) {
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

    public void sendSms(OgPerson person, String smsText) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// ?????????
        p.setName(person.getpName());// ??????
        p.setSmsType("4");// ???????????????3???4????????????,2????????????
        p.setSmsText(smsText);// ????????????
        p.setInspectTime(DateUtils.dateTimeNow());// ????????????
        p.setInspectObject("111103");// ????????????
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//????????????
    }

}
