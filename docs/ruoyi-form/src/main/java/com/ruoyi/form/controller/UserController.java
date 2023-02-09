package com.ruoyi.form.controller;


import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private IConsumerService consumerService;


    @RequestMapping(value = "/logoninfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> info() {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        List<OgGroup> groupList = workService.selectGroupByUserId(pId);
        List<String> groupNameList = new ArrayList<>();
        for (OgGroup group : groupList) {
            String name = group.getGrpName();
            groupNameList.add(name);
        }
        OgUser user = new OgUser();
        user.setpId(pId);
        List<OgUser> userList = ogUserService.selectAccountList(user);
        String uid = userList.get(0).getUserId();
        List<OgRole> roleList = roleService.selectRolesByUserId(uid);
        List<String> roleNameList = new ArrayList<>();
        for (OgRole role : roleList) {
            roleNameList.add(role.getrName());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("orgName", ogPerson.getOrgname());
        result.put("name", ogPerson.getpName());
        result.put("position", ogPerson.getPosition());
        result.put("leader", ogPerson.getLeader());
        result.put("address", ogPerson.getAddress());
        result.put("mobilPhone", ogPerson.getMobilPhone());
        result.put("newMobilPhone", ogPerson.getNewMobilPhone());
        result.put("email", ogPerson.getEmail());
        result.put("phone", ogPerson.getPhone());
        result.put("orgGroup", groupNameList);
        result.put("role", roleNameList);
        return result;
    }

    @RequestMapping(value = "/type/list/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public TableDataInfo type(@PathVariable("typeId") Integer typeId) {
        int total = 0;
        List<Map<String, Object>> data = new ArrayList<>();
        if (typeId == 1) {
            //部门
            List<OgOrg> list = sysDeptService.selectDeptList(null);
            for (OgOrg ogOrg : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", ogOrg.getOrgName());
                map.put("id", ogOrg.getOrgId());
                data.add(map);
            }
            total = list.size();
        } else if (typeId == 2) {
            //角色
            List<OgRole> list = roleService.selectRoleList(null);
            for (OgRole ogRole : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", ogRole.getrName());
                map.put("id", ogRole.getRid());
                data.add(map);
            }
            total = list.size();
        } else if (typeId == 3) {
            //工作组
            List<OgGroup> list = workService.selectOgGroupList(null);
            for (OgGroup ogGroup : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", ogGroup.getGrpName());
                map.put("id", ogGroup.getGroupId());
                data.add(map);
            }
            total = list.size();
        }
        return new TableDataInfo(data, total);
    }

    @RequestMapping(value = "/user/list/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public TableDataInfo userList(@PathVariable("typeId") Integer typeId,@RequestParam(value = "id") String id){
        List<Map<String,Object>> data = new ArrayList<>();
        OgPerson ogPerson = new OgPerson();
        if(typeId==1){
            //部门
            ogPerson.setOrgId(id);
            List<OgPerson> list = consumerService.selectOgPersonJqList(ogPerson);
            for(OgPerson man:list){
                Map<String,Object> map = new HashMap<>();
                map.put("id",man.getpId());
                map.put("name",man.getpName());
                data.add(map);
            }
        }else if(typeId==2){
            //角色
            List<OgPerson> list = consumerService.selectListByRoleId(id);
            for(OgPerson man:list){
                Map<String,Object> map = new HashMap<>();
                map.put("id",man.getpId());
                map.put("name",man.getpName());
                data.add(map);
            }
        }else if(typeId==3){
            //工作组
            OgGroupPerson person = new OgGroupPerson();
            person.setGroupId(id);
            List<OgGroupPerson> list = workService.selectOgGroupPersonList(person);
            for(OgGroupPerson groupPerson:list){
                Map<String,Object> map = new HashMap<>();
                OgPerson man = groupPerson.getPerson();
                map.put("id",man.getpId());
                map.put("name",man.getpName());
                data.add(map);
            }
        }
        int total = data.size();
        return new TableDataInfo(data, total);
    }

}
