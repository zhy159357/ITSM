package com.ruoyi.activiti.controller.itsm.dispatch;


import com.ruoyi.activiti.constants.FmDdConstants;
import com.ruoyi.activiti.constants.FmSwConstants;
import com.ruoyi.activiti.service.FmDispatchService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 查询调度请求
 */

@Controller
@RequestMapping("/dispatch/query")
public class QueryDispatchController extends BaseController {

    @Autowired
    private FmDispatchService dispatchService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysRoleService sysRoleService;



    private String prefix = "dispatch/query";


    /**
     * 转到 查询调度请求 页面
     * @return
     */
    @GetMapping()
    public String mydispatch(ModelMap modelMap)
    {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ogUser.getpId());
        if(ogPerson!=null){
            OgOrg ogOrg = deptService.selectDeptById(ogPerson.getOrgId());
            modelMap.put("ogOrg",ogOrg);
        }
        //根据当前用户获取对应的角色信息
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(ogUser.getUserId());
        if(StringUtils.isNotEmpty(ogRoles)){
            String isHasRole = "false";
            for(OgRole role : ogRoles){
                if(FmDdConstants.sourceRole.equals(role.getRid())){
                    isHasRole = "true";
                }
            }
            modelMap.put("isHasRole",isHasRole);
        }
        return prefix + "/query";
    }


    /**
     * 列表展示
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmDd fmDd)
    {
        startPage();
        //获取当前登录人的id
        OgUser u = ShiroUtils.getOgUser();
        String userId = u.getUserId();
        //根据当前登录人id查询出他所在的机构id
        OgPerson person = ogPersonService.selectOgPersonById(userId);
        String orgId = person.getOrgId();
        //根据当前机构id查询出层级码
        OgOrg org = deptService.selectDeptById(orgId);
        String levelCode = org.getLevelCode();

        //邮政金融运维 丰台 合肥  查看所有
        if("/000000000/".equals(levelCode)
                || "/000000000/010000888/".equals(levelCode)
                || levelCode.startsWith("/000000000/010000888/010100888/")
                || levelCode.startsWith("/000000000/010000888/010300888/")
                || levelCode.startsWith("/000000000/010000888/010400888/")
                || "/000000000/010000888/100000889/".equals(levelCode)
                || "/000000000/010000888/100000987/".equals(levelCode)
                ){
            startPage();
            fmDd.setInvalidationMark("1");
            fmDd.setOrgId(resetOrgId(fmDd.getOrgId(),fmDd.getOrgName()));
            if (!StringUtils.isEmpty(fmDd.getLabel())) {
                String myFlag = fmDd.getLabel();
                if ("1".equals(myFlag)) {//我的标识
                    fmDd.setCreateId(u.getpId());//默认我创建的 创建人是当前登陆人
                } else {
                    fmDd.setParticipatorIds(u.getpId());//我处理过的
                }
            }
            List<FmDd> list = dispatchService.selectQueryDispatchList(fmDd);
            return getDataTable(list);
        }else {
            //根据当前登录人机构id查询
            startPage();
            fmDd.setCreateorgId(orgId);
        }

        fmDd.setInvalidationMark("1");
        fmDd.setOrgId(resetOrgId(fmDd.getOrgId(),fmDd.getOrgName()));
        if (!StringUtils.isEmpty(fmDd.getLabel())) {
            String myFlag = fmDd.getLabel();
            if ("1".equals(myFlag)) {//我的标识
                fmDd.setCreateId(u.getpId());//默认我创建的 创建人是当前登陆人
            } else {
                fmDd.setParticipatorIds(u.getpId());//我处理过的
            }
        }

        List<FmDd> list = dispatchService.selectQueryDispatchList(fmDd);
        return getDataTable(list);

    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap map)
    {
        //获取页面的信息
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd != null){
            map.put("fmdd",fmDd);
        }

        //获取审核人信息
        OgPerson person = ogPersonService.selectOgPersonById(fmDd.getCheckerId());
        if(person!=null){
            map.put("pname",person.getpName());
        }


        //创建时间进行日期回显
        String createTime = fmDd.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            fmDd.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //计划操作时间进行日期回显
        String planTime = fmDd.getPlanTime();
        if(StringUtils.isNotEmpty(planTime)){
            fmDd.setPlanTime(DateUtils.formatDateStr(planTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        return prefix + "/detail";
    }


    /**
     * 导出调度事件单
     * @param
     * @return
     */
    @Log(title = "调度请求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmDd fmDd)
    {
        //获取当前登录人的id
        OgUser u = ShiroUtils.getOgUser();
        String isCurrentPage = (String)fmDd.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            startPage();
            fmDd.setOrgId(resetOrgId(fmDd.getOrgId(),fmDd.getOrgName()));
            if (!StringUtils.isEmpty(fmDd.getLabel())) {
                String myFlag = fmDd.getLabel();
                if ("1".equals(myFlag)) {//我的标识
                    fmDd.setCreateId(u.getpId());//默认我创建的 创建人是当前登陆人
                } else {
                    fmDd.setParticipatorIds(u.getpId());//我处理过的
                }
            }
            List<FmDd> list = dispatchService.selectDispatchList(fmDd);
            ExcelUtil<FmDd> util = new ExcelUtil<FmDd>(FmDd.class);
            return util.exportExcel(list, "调度请求单");
        }else {
            fmDd.setOrgId(resetOrgId(fmDd.getOrgId(),fmDd.getOrgName()));
            if (!StringUtils.isEmpty(fmDd.getLabel())) {
                String myFlag = fmDd.getLabel();
                if ("1".equals(myFlag)) {//我的标识
                    fmDd.setCreateId(u.getpId());//默认我创建的 创建人是当前登陆人
                } else {
                    fmDd.setParticipatorIds(u.getpId());//我处理过的
                }
            }
            List<FmDd> list = dispatchService.selectDispatchList(fmDd);
            ExcelUtil<FmDd> util = new ExcelUtil<FmDd>(FmDd.class);
            return util.exportExcel(list, "调度请求单");
        }
    }



    /**
     * 删除附件
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }





}
