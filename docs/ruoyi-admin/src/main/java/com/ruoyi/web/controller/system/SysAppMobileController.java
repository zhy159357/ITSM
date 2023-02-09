package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysWorkService;
import com.ruoyi.system.service.OgPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动APP相关接口
 */

@RestController
@RequestMapping("/appMobile/user")
public class SysAppMobileController extends BaseController {

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private OgPostService ogPostService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgGroupPersonService ogGroupPersonService;

    @Autowired
    private IOgUserService ogUserService;

    /**
     * 个人中心详细信息
     * @param map
     *        custNo    柜员号
     *        pageNum   页码
     *        pageSize  显示条数
     * @return 人员信息
     */
    @PostMapping("/person")
    public AjaxResult selectPerson(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("custNo"))) {
            return  AjaxResult.warn("custNo不能为空");
        }
        String userId = getUserId(map.get("custNo"));
        if ("erro".equals(userId)) {
            return  AjaxResult.warn("未查到该用户信息");
        }
        AjaxResult ajaxResult = new AjaxResult();
        OgPerson ogPerson =  ogPersonService.selectOgPersonById(userId);
        if (ogPerson == null) {
            return  AjaxResult.warn("未查到该人员信息");
        }
        return AjaxResult.success(ogPerson);
    }

    /**
     * 岗位信息
     * @param map
     *        custNo    柜员号
     *        postName  工作组名称
     *        pageNum   页码
     *        pageSize  显示条数
     * @return 岗位列表
     */
    @PostMapping("/postList")
    public TableDataInfo selectPostList(@RequestBody Map<String, String> map)
    {
        String userId = getUserId(map.get("custNo"));
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId, map.get("postName"));
        return getDataTable_app(list, map.get("pageNum"), map.get("pageSize"));
    }

    /**
     * 工作组信息
     * @param map1
     *        custNo    柜员号
     *        groupName 工作组名称
     *        pageNum   页码
     *        pageSize  显示条数
     * @return 工作组列表
     */
    @PostMapping("/groupList")
    public TableDataInfo selectGroupList(@RequestBody Map<String, String> map1)
    {
        String userId = getUserId(map1.get("custNo"));
        Map map = new HashMap<>();
        map.put("userId", userId);
        map.put("grpName", map1.get("groupName"));
        List<OgGroup> list = workService.selectGroupByGposition(map);
        if(!CollectionUtils.isEmpty(list)){
            for(OgGroup group : list){
                List<String> strList = new ArrayList<>();
                OgGroupPerson groupPerson = new OgGroupPerson();
                groupPerson.setGroupId(group.getGroupId());
                groupPerson.setGpOsition("1");
                // 查询该工作组的组长
                List<OgGroupPerson> groupPersonList = ogGroupPersonService.selectOgGroupPersonList(groupPerson);
                if(!CollectionUtils.isEmpty(groupPersonList)){
                    for(OgGroupPerson person : groupPersonList){
                        OgPerson ogPerson = ogPersonService.selectOgPersonById(person.getPid());
                        if(null != ogPerson){
                            strList.add(ogPerson.getpName());
                        }
                    }
                }
                // 获取该工作组下的所有组长名称赋值到remark字段（用逗号拼接），然后前端显示
                group.setRemark(String.join(",", strList));
            }
        }
        return getDataTable_app(list, map1.get("pageNum"), map1.get("pageSize"));
    }

    //通过柜员号查询用户ID （返回erro为查不到该柜员号的用户）
    public String getUserId(String custNo) {
        OgUser ogUser = ogUserService.selectOgUserByCustNo(custNo);
        if (ogUser != null ) {
            return ogUser.getUserId();
        }
        return "erro";
    }
}
