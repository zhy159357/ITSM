package com.ruoyi.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysNoticeReceive;
import com.ruoyi.system.domain.SysNoticeReleaseLog;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;


/**
 * 公告 服务层实现
 *
 * @author ruoyi
 * @date 2018-06-25
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper noticeMapper;

    @Autowired
    private OgUserMapper ogUserMapper;

    @Autowired
    private SysNoticeReleaseLogMapper sysNoticeReleaseLogMapper;

    @Autowired
    private SysNoticeReceiveMapper sysNoticeReceiveMapper;

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysWorkMapper workMapper;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询公告信息 编辑，详情页面初始化赋值
     *
     * @param ambizid 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(String ambizid)
    {
        SysNotice notice = new SysNotice();
        notice = noticeMapper.selectNoticeById(ambizid);
        String receiveId = "";
        String receiveName = "";
        if (StringUtils.isNotEmpty(notice.getReceiveDeptName())) {
            receiveId += notice.getReceiveDeptId();
            receiveName += notice.getReceiveDeptName();
        }
        if (StringUtils.isNotEmpty(notice.getReceiveGroupName())) {
            receiveId += notice.getReceiveGroupId();
            receiveName += notice.getReceiveGroupName();
        }
        notice.setReceiveDeptGroupId(receiveId);
        notice.setReceiveDeptGroupName(receiveName);
        return notice;
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        OgPerson ogPerson = ogPersonMapper.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        String levelCode = iSysDeptService.selectDeptById(ogPerson.getOrgId()).getLevelCode();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("beginTime", notice.getParams().get("beginTime"));
        String endTime = (String) notice.getParams().get("endTime");
        if (StringUtils.isNotEmpty(endTime)) {
            endTime += " " + "23:59:59";
        }
        map1.put("endTime", endTime);
        if ( StringUtils.isEmpty(notice.getMyIdentity()) || "0".equals(notice.getMyIdentity())) {
            String[] arr = receiveAmBizIds();
            notice.setAmBizIds(arr);
            map1.put("currentStatus", "03");
            notice.setParams(map1);
        } else if ("1".equals(notice.getMyIdentity())) {
            notice.setParams(map1);
            notice.setCreateId(ShiroUtils.getOgUser().getpId());
        } else if ("2".equals(notice.getMyIdentity())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("beginTime", notice.getParams().get("beginTime"));
            String endTime1 = (String) notice.getParams().get("endTime");
            if (StringUtils.isNotEmpty(endTime)) {
                endTime1 += " " + "23:59:59";
            }
            map.put("endTime", endTime1);
            map.put("releaseBeginTime", notice.getParams().get("releaseBeginTime"));
            String releaseEndTime = (String) notice.getParams().get("releaseEndTime");
            if (StringUtils.isNotEmpty(releaseEndTime)) {
                releaseEndTime += " " + "23:59:59";
            }
            map.put("releaseEndTime", releaseEndTime);
            String userId = ShiroUtils.getOgUser().getpId();
            List<OgGroup> ogGroupList =  workMapper.selectGroupByUserId(userId);
            List<String> ogGroupIdList = new ArrayList<>();
            if (ogGroupList != null && ogGroupList.size() > 0) {
                for (OgGroup ogGroup : ogGroupList) {
                    ogGroupIdList.add(ogGroup.getGroupId());
                }
            }
            map.put("loginId", userId);
            map.put("receiveDeptId", ogPerson.getOrgId());
            map.put("levelCode", levelCode);
            map.put("receiveGroupIds", "");
            if (ogGroupIdList != null && ogGroupIdList.size() > 0) {
                map.put("receiveGroupIds", ogGroupIdList);
            }
            notice.setParams(map);
            if ("/000000000/".equals(levelCode) || "/000000000/010000888/".equals(levelCode) ) {
                return noticeMapper.selectNoticeList(notice);
            } else {
                return noticeMapper.selectPageNoticeList(notice);
            }
        } else if ("3".equals(notice.getMyIdentity())) {
            map1.put("releaseBeginTime", notice.getParams().get("releaseBeginTime"));
            String releaseEndTime = (String) notice.getParams().get("releaseEndTime");
            if (StringUtils.isNotEmpty(releaseEndTime)) {
                releaseEndTime += " " + "23:59:59";
            }
            map1.put("releaseEndTime", releaseEndTime);
            notice.setParams(map1);
            notice.setCreateId(ShiroUtils.getOgUser().getpId());
        }
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 查询接收iDs
     * @return 接收ID数组
     */
    @Override
    public String[] receiveAmBizIds() {
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonMapper.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptMapper.selectDeptById(orgId);
        //当前登录人的工作组信息
        List<OgGroup> ogGroup =  workMapper.selectGroupByUserId(pId);
        //当前登录人岗位
        List<OgPost> ogPosts = noticeMapper.selectPostForByUserId(pId);
        if (ogPosts.size() == 0) {
            String[] ids = {"0"};
            return  ids;
        }
        List<String>  groupIds= new ArrayList<>();
        for (OgGroup ogGroup2: ogGroup){
            groupIds.add(ogGroup2.getGroupId());
        }
        SysNoticeReceive sysNoticeReceive = new SysNoticeReceive();
        List<SysNoticeReceive> list = sysNoticeReceiveMapper.selectNoticeReceiveList(sysNoticeReceive);
        list = list.stream().filter((item) -> {
            return groupIds.contains( item.getReceiveGroup()) || ogOrg.getOrgId().equals(item.getReceiveDept());
        }).collect(Collectors.toList());

        String[] amBizIds = new String[list.size()];
        if (list != null && list.size() > 0) {
            for (int i = 0 ; i < list.size() ; i ++) {
                String ambizid = list.get(i).getAmBizId();
                SysNotice noticevo = noticeMapper.selectNoticeById(ambizid);
                String[] noticeReceivePost = noticevo.getReceiveRoleId().split(",");
                for (int j = 0 ; j < ogPosts.size() ; j ++) {
                    for (String receiveRoleId : noticeReceivePost) {
                        if (ogPosts.get(j).getPostId().equals(receiveRoleId)) {
                            amBizIds[i] = ambizid;
                            break;
                        } else {
                            amBizIds[i] = "0";
                        }
                    }
                    if (amBizIds[i] == ambizid) {
                        break;
                    }
                }
            }
        } else {
            String[] amBizIdc = {"0"};//没有接收的公告
            return amBizIdc;
        }
        return amBizIds;
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice)
    {
        String nowDate = DateUtils.dateTimeNow("yyyyMMdd");
        notice.setAddTime(DateUtils.getTime());
        SysNotice sysNotice = null;
        if("mysql".equals(dbType)){
            sysNotice = noticeMapper.selectNoticeMaxAmCodeMysql();
        }else{
            sysNotice = noticeMapper.selectNoticeMaxAmCode();
        }

        Integer index = 1;
        if (sysNotice != null) {
            String amCodeDate = sysNotice.getAmCode().substring(2,10);
            if ( nowDate.equals(amCodeDate) ) {
                index = Integer.valueOf(sysNotice.getAmCode().substring(11)) + 1;
            }
        }
        notice.setAmCode("GG" + nowDate + StringUtils.leftPad(index.toString(), 4, "0"));
        String pId = null;
        if(StringUtils.isEmpty(notice.getCreateId())){
            pId = ShiroUtils.getOgUser().getpId();
            notice.setCreateId(pId);
        }else{
            pId = notice.getCreateId();
        }
        //获取人员信息
        OgPerson ogPerson = ogPersonMapper.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        notice.setReleaseOrg(orgId);
        if ("02".equals(notice.getCurrentStatus())) {
            notice.setCurrentStatus("01");//在调用查询接收人个数的方法时出现了问题 现在设置状态为新建去调用这个方法
            int num = insertSysnoticeReceive(notice);//判断所选的机构&&工作组&&岗位下有没有人
            if (num == 0) {
                return 0;//没有接收人员得话就不能发布
            }
            notice.setCurrentStatus("02");//如果接收人的个数大于0 那么将状态改为待审核
        }
        insertSysNoticeReleaseLog(notice);//公告发布日志
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 序列AmCode
     *
     * @return 结果
     */
    public SysNotice selectNoticeMaxAmCode() {
        if("mysql".equals(dbType)){
            return noticeMapper.selectNoticeMaxAmCodeMysql();
        }else{
            return noticeMapper.selectNoticeMaxAmCode();
        }
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        String cheCkerFlag = notice.getCheckerFlag();
        if (StringUtils.isEmpty(cheCkerFlag)) {//无审核标志为修改方法//有审核标志为审核
            if ("02".equals(notice.getCurrentStatus())) {
                notice.setCurrentStatus("01");//在调用查询接收人个数的方法时出现了问题 现在设置状态为新建去调用这个方法
                int num = insertSysnoticeReceive(notice);//公告接收表insert
                if (num == 0) {//判断所选的机构&&工作组&&岗位下有没有人
                    return 0;
                }
                notice.setCurrentStatus("02");//如果接收人的个数大于0 那么将状态改为待审核
            }
            insertSysNoticeReleaseLog(notice);//公告发布日志
            return noticeMapper.updateNotice(notice);
        }else if("0".equals(cheCkerFlag)){//通过
            int num = insertSysnoticeReceive(notice);//公告接收表insert
            notice.setCurrentStatus("04");//待处理
            String createId = notice.getCreateId();
            OgPerson ogPerson = ogPersonMapper.selectOgPersonById(createId);
            notice.setReleaseDate(DateUtils.getTime());
            notice.setDealSchdule("0/"+num);
            //公告接收日志
        }else if("1".equals(cheCkerFlag)){//不通过
            notice.setCurrentStatus("03");//退回
        }
        notice.setCheckerFlag("");//置空审核标志
        notice.setCheckerTime(DateUtils.getTime());
        insertSysNoticeReleaseLog(notice);//公告发布日志
        return noticeMapper.checkNotice(notice);
    }

    /**
     * 删除公告对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids)
    {
        String[] amBidIds = ids.split(",");
        for (int i = 0 ; i < amBidIds.length ; i ++) {
            SysNotice notice = noticeMapper.selectNoticeById(amBidIds[i]);
            if (!"01".equals(notice.getCurrentStatus())) {
                return 0;//不可删除
            }
        }
        return noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询审核公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeCheckList(SysNotice notice)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTime", notice.getParams().get("beginTime"));
        String endTime = (String) notice.getParams().get("endTime");
        if (StringUtils.isNotEmpty(endTime)) {
            endTime += " " + "23:59:59";
        }
        map.put("endTime", endTime);
        notice.setParams(map);
        notice.setCheckerId(ShiroUtils.getUserId());
        return noticeMapper.selectNoticeCheckList(notice);
    }

    /**
     * 公告通知岗位下拉
     *
     * @param sysNotice 公告实体传rid 与 postName
     * @return 结果
     */
    @Override
    public List<OgRole> selectReceptionPostList(SysNotice sysNotice) {
        return noticeMapper.selectReceptionPostList(sysNotice);
    }

    /**
     * 公告发布日志变更
     *
     * @param notice
     * @return 结果
     */
    private int insertSysNoticeReleaseLog(SysNotice notice){
        SysNoticeReleaseLog sysNoticeReleaseLog = new SysNoticeReleaseLog();
        sysNoticeReleaseLog.setAmBizId(notice.getAmBizId());
        sysNoticeReleaseLog.setAmTaskId(UUID.getUUIDStr());
        sysNoticeReleaseLog.setUpdateTime(DateUtils.getTime());
        sysNoticeReleaseLog.setAmState(notice.getCurrentStatus());
        sysNoticeReleaseLog.setDescription(notice.getDescription());
        String username = ShiroUtils.getLoginName();
        OgUser ogUser = new OgUser();
        ogUser.setUsername(username);
        List<OgUser> userList = ogUserMapper.selectAccountList(ogUser);
        sysNoticeReleaseLog.setPersonId(userList.get(0).getpId());
        if ("01".equals(notice.getCurrentStatus())) {
            sysNoticeReleaseLog.setProcessName("新建工作通知");//过程
        } else if ("02".equals(notice.getCurrentStatus())) {
            sysNoticeReleaseLog.setProcessName("待审核");
        } else if ("03".equals(notice.getCurrentStatus())) {
            sysNoticeReleaseLog.setProcessName("退回待修改");
        } else if ("04".equals(notice.getCurrentStatus())) {
            sysNoticeReleaseLog.setProcessName("处理工作通知");
        }
        String maxNum = "";
        if ( sysNoticeReleaseLogMapper.selectMaxNum()!= null) {
            maxNum = sysNoticeReleaseLogMapper.selectMaxNum().getNum();
        } else {
            maxNum = "0";
        }
        Integer num = Integer.valueOf(maxNum);
        maxNum = String.valueOf(num+1);
        sysNoticeReleaseLog.setNum(maxNum);//编号
        sysNoticeReleaseLog.toString();
        return sysNoticeReleaseLogMapper.insertNoticeReleaseLog(sysNoticeReleaseLog);
    }

    /**
     * 公告接收inser
     *
     * @param notice
     * @return 结果
     */
    private int insertSysnoticeReceive(SysNotice notice) {
        SysNoticeReceive sysNoticeReceive = new SysNoticeReceive();
        sysNoticeReceive.setAmBizId(notice.getAmBizId());
        sysNoticeReceive.setEditTime(DateUtils.getTime());
        sysNoticeReceive.setIfReceive("0");//待处理
        String[] postIds = notice.getReceiveRoleId().split(",");
        String[] receiveDeptGroupIds = null;
        notice.setPostId(postIds);//接收岗位postIds-----------------------
        List<OgPerson> personList = null;
        int num  = 0;
        if (StringUtils.isNotEmpty(notice.getReceiveDeptId())) {
            receiveDeptGroupIds = notice.getReceiveDeptId().split(",");
            for (int i = 0 ; i < receiveDeptGroupIds.length ; i++) {
                String receivePersons = "";//接收人员列表
                notice.setOrgId(receiveDeptGroupIds[i]);//接收机构orgId-----------------------
                personList = noticeMapper.selectReceivePersonDept(notice);
                for (OgPerson ogPerson : personList) {
                    if ("1".equals(ogPerson.getInvalidationMark())) {
                        receivePersons += ogPerson.getpId() + ",";
                    }
                }
                sysNoticeReceive.setReceiveDept(receiveDeptGroupIds[i]);
                sysNoticeReceive.setAmReceiveId(UUID.getUUIDStr());
                if (StringUtils.isNotEmpty(receivePersons) && receivePersons.length() > 0) {
                    if("02".equals(notice.getCurrentStatus())) {//获取当前的公告状态如果是待审核状态过来的 说明是审核通过了
                        sysNoticeReceive.setReceivePersonList(receivePersons);//接收人员列表
                        sysNoticeReceiveMapper.insertNoticeReceive(sysNoticeReceive);
                    }
                    num ++;//如何所选机构工作组 岗位下有人 就代表可以发布这条公告
                }
            }
        }
        if (StringUtils.isNotEmpty(notice.getReceiveGroupId())) {
            receiveDeptGroupIds = notice.getReceiveGroupId().split(",");
            for (int i = 0 ; i < receiveDeptGroupIds.length ; i++) {
                String receivePersons = "";//接收人员列表
                notice.setGroupId(receiveDeptGroupIds[i]);//接收工作组orgId-----------------------
                personList = noticeMapper.selectReceivePersonGroup(notice);
                for (OgPerson ogPerson : personList) {
                    if ("1".equals(ogPerson.getInvalidationMark())) {
                        receivePersons += ogPerson.getpId() + ",";
                    }
                }
                sysNoticeReceive.setReceiveGroup(receiveDeptGroupIds[i]);
                sysNoticeReceive.setAmReceiveId(UUID.getUUIDStr());
                if (StringUtils.isNotEmpty(receivePersons) && receivePersons.length() > 0) {
                    if("02".equals(notice.getCurrentStatus())) {//获取当前的公告状态如果是待审核状态过来的 说明是审核通过了
                        sysNoticeReceive.setReceivePersonList(receivePersons);//接收人员列表
                        sysNoticeReceiveMapper.insertNoticeReceive(sysNoticeReceive);
                    }
                    num ++;//如何所选机构工作组 岗位下有人 就代表可以发布这条公告
                }
            }
        }
        return num;
    }

    /**
     * 公告通知接收工作组
     *@param ogGroup
     * @return 结果
     */
    public List<OgGroup> selectNoticeReceiveGroupList(OgGroup ogGroup) {
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonMapper.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = iSysDeptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg ogOrg1 = new OgOrg();
        ogOrg1.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        list = noticeMapper.selectDeptByOrgCodeRightLike(ogOrg1);
        List<OgGroup> groupList = new ArrayList<>();
        String idstr = "";
        if (list.size() > 0) {
            for (int i = 0 ; i < list.size() ; i ++) {
                idstr += list.get(i).getOrgId() + ",";
            }
            String[] ids = idstr.split(",");
            ogGroup.getParams().put("orgIds" , ids);
            groupList = noticeMapper.selectNoticeReceiveGroup(ogGroup);
        }
        return groupList;
    }

    /**
     * 公告通知接收机构
     *@param ogOrg
     * @return 结果
     */
    public List<OgOrg> selectNoticeReceiveDeptList(OgOrg ogOrg) {
        List<OgOrg> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(ogOrg.getOrgName())) {
            String[] orgNames = ogOrg.getOrgName().split("/");
            List<String> orgNameList = new ArrayList<>();
            for (int i = 0 ; i < orgNames.length ; i ++) {
                orgNameList.add(orgNames[i]);
            }
            Map<String , Object> orgNameMap = new HashMap<>();
            orgNameMap.put("orgNameList", orgNameList);
            ogOrg = new OgOrg();
            ogOrg.setParams(orgNameMap);
        }
        list = noticeMapper.selectNoticeReceiveDeptList(ogOrg);
        return  list;
    }

    /**
     * 查询接收公告人员列表/机构
     *
     * @param sysNotice 将接收机构orgID 接收岗位 postId 封装到Sysnotice属性中
     * @return 查询接收公告人员列表
     */
    public List<OgPerson> selectReceivePersonDept(SysNotice sysNotice){
        return noticeMapper.selectReceivePersonDept(sysNotice);
    }

    /**
     * 查询接收公告人员列表/工作组
     *
     * @param sysNotice 将工作组groupId 接收岗位 postId 封装到Sysnotice属性中
     * @return 查询接收公告人员列表
     */
    public List<OgPerson> selectReceivePersonGroup(SysNotice sysNotice){
        return noticeMapper.selectReceivePersonGroup(sysNotice);
    }

    /**
     * 查询省分行 信息技术局 单列市
     *
     * @param ogOrg 机构查询
     * @return 机构集合
     */
    public List<OgOrg> DeptTreeBankch(OgOrg ogOrg){

        List<OgOrg> list = noticeMapper.DeptTreeBankch(ogOrg);
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
            return iSysDeptService.selectDeptByCode(twoLevelCode);
        }

    }
}