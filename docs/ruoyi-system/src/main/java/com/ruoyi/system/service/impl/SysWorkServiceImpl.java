package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;
import com.ruoyi.system.mapper.SysWorkMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工作组管理｜成员业务层
 *
 * @author chao_zhang
 */
@Service("oggroup")
public class SysWorkServiceImpl implements ISysWorkService {
    @Autowired
    private SysWorkMapper workMapper;
    @Autowired
    private ISysDeptService iSysDeptService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public List<OgGroup> selectOgGroupList(OgGroup group) {
        return workMapper.selectOgGroupList(group);
    }

    @Override
    public List<OgGroup> selectOgGroupAllList(OgGroup group) {
        return workMapper.selectOgGroupAllList(group);
    }

    @Override
    public OgGroup selectOgGroupById(String id) {
        OgGroup group = workMapper.selectOgGroupById(id);
        return group;
    }

    @Override
    public List<OgGroup> selectGroupByUserId(String userId) {
        return workMapper.selectGroupByUserId(userId);
    }

    @Override
    public OgGroup selectOgGroupByGrpName(String grpName) {
        return workMapper.selectOgGroupByGrpName(grpName);
    }

    @Override
    public List<OgGroup> selectGroupByGposition(Map map) {
        return workMapper.selectGroupByGposition(map);
    }
    @Override
    public List<OgGroup> selectProblemGroupByGposition(Map map) {
        return workMapper.selectProblemGroupByGposition(map);
    }

    @Override
    public String checkGroupNameUnique(String grpName) {
        int count = workMapper.checkGroupNameUnique(grpName);
        if (count > 0) {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    @Override
    public int insertOgGroup(OgGroup group) {
        // 此时传递过来的sysId是名称需要通过查询转换成id
        group.setGroupId(UUID.getUUIDStr());
        if("mysql".equals(dbType)){
            return workMapper.insertOgGroupMysql(group);
        }else{
            return workMapper.insertOgGroup(group);
        }
    }

    @Override
    public int updateOgGroup(OgGroup group) {
        if("mysql".equals(dbType)){
            return workMapper.updateOgGroupMysql(group);
        }else{
            return workMapper.updateOgGroup(group);
        }

    }

    @Override
    public int deleteOgGroupByIds(String ids) {
        String[] groupIds = Convert.toStrArray(ids);
        return workMapper.deleteOgGroupByIds(groupIds);
    }

    @Override
    public List<OgGroupPerson> selectOgGroupPersonList(OgGroupPerson person) {
        return workMapper.selectOgGroupPersonList(person);
    }

    @Override
    public List<OgPerson> selectUserList(OgPerson person) {
        return workMapper.selectUserList(person);
    }

    @Override
    public int insertOgGroupPerson(String groupId, String ids) {
        String[] pIds = Convert.toStrArray(ids);
        int rows = 0;
        for (String pid : pIds) {
            OgGroupPerson person = new OgGroupPerson();
            // 主键
            person.setGpId(UUID.getUUIDStr());
            person.setGroupId(groupId);
            person.setPid(pid);
            person.setGpOsition("");
            person.setGpOrder("");
            rows += workMapper.insertOgGroupPerson(person);
        }
        return rows;
    }

    @Override
    public int updateOgGroupPerson(OgGroupPerson person) {
        return workMapper.updateOgGroupPerson(person);
    }

    @Override
    public OgGroupPerson selectOgGroupPersonById(String gpId) {
        return workMapper.selectOgGroupPersonById(gpId);
    }

    @Override
    public int deleteOgGroupPersonByIds(String ids) {
        String[] gpIds = Convert.toStrArray(ids);
        return workMapper.deleteOgGroupPersonByIds(gpIds);
    }

    @Override
    public List<OgPerson> selectOgPersonByGroupName(String groupName) {
        return workMapper.selectOgPersonByGroupName(groupName);
    }

    @Override
    public List<OgGroup> getOgProviceGroups() {
        OgUser u = ShiroUtils.getOgUser();
        String ocode = iSysDeptService.getLoginOrgCode(u.getUserId());
        OgOrg ogOrg = iSysDeptService.selectDeptByCode(ocode);
        String code = "";
        //1.当前登陆用户的机构就是一级或者是二级机构
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            code = ogOrg.getOrgCode();
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            code = split[2];
        }
        List<OgGroup> list = workMapper.getOgProviceGroups(code);
        return list;
    }

    @Override
    public List<OgGroup> selectOgGroupBySys(String sysId) {
        List<OgGroup> list = workMapper.selectOgGroupBySys(sysId);
        return list;
    }

    @Override
    public List<OgPerson> selectGroupByPerson(String groupId) {
        List<OgPerson> list = workMapper.selectGroupByPerson(groupId);
        return list;
    }

    @Override
    public List<OgGroup> selectOgGroupByFmbiz(OgGroup group) {
        List<OgGroup> list = workMapper.selectOgGroupByFmbiz(group);
        return list;
    }

    @Override
    public List<OgGroup> selectGroupBySysId(String sysId) {
        return workMapper.selectGroupBySysId(sysId);
    }

    /**
     * 根据登录人id查询工作组
     *
     * @param pId
     * @return
     */
    @Override
    public OgGroup selectOgPersonById(String pId) {
        return workMapper.selectOgPersonById(pId);
    }

    //获取对应的工作组
    @Override
    public List<OgGroupPerson> selectOgPersonByIdTwo(String pId) {
        return workMapper.selectOgPersonByIdTwo(pId);
    }

    @Override
    public List<OgPerson> selectLxbgGroupByPerson(String groupId) {
        return workMapper.selectLxbgGroupByPerson(groupId);
    }

    @Override
    public List<OgGroup> selectGroupByGpositionTwo(OgGroup ogGroup) {
        return workMapper.selectGroupByGpositionTwo(ogGroup);
    }


    @Override
    public List<OgGroup> selectOgGroupLxbgList(String groupIds, OgGroup group) {


        List<OgGroup> groupList = workMapper.selectOgGroupList(group);

        if (!"0".equals(groupIds)) {
            String[] grou = Convert.toStrArray(groupIds);
            for (OgGroup og : groupList) {
                for (String groupids : grou) {
                    if (og.getGroupId().equals(groupids)) {
                        og.setFlag(true);
                        break;
                    }
                }
            }
        }


        return groupList;
    }

    public List<OgGroupPerson> selectGroupIdByPersonList(String groupId) {
        return workMapper.selectGroupIdByPersonList(groupId);
    }

    public List<OgGroup> selectLoginGroups(String userId) {
        return workMapper.selectLoginGroups(userId);
    }
    @Override
    public List<OgGroup> selectOgGroupListAll(OgGroup group){
        return workMapper.selectOgGroupListAll(group);
    }
    @Override
   public List<OgGroup> selectOgGroupListSysIdIn(OgGroup group){
        return workMapper.selectOgGroupListSysIdIn(group);
    }

    @Override
    public List<LabelValue> selectOgGroupPersonListByGroupName(String groupName) {
        //value:id lable:
        List<OgGroupPerson> ogGroupPeople = workMapper.selectOgGroupPersonListByGroupName(groupName);
        if(!CollectionUtils.isEmpty(ogGroupPeople)){
            return ogGroupPeople.stream().map(ogGroupPerson -> {
                LabelValue labelValue = new LabelValue();
                labelValue.setValue(ogGroupPerson.getPid());
                labelValue.setLabel(ogGroupPerson.getPerson().getpName());
                return labelValue;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
