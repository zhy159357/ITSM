package com.ruoyi.form.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.domain.entity.SysDeptVo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.ChangeDeptPersonEntity;
import com.ruoyi.form.domain.CommonTree;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.service.ICommonTreeService;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * commonTreeController
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@Controller
@RequestMapping("system/commonTree")
@Api(tags = "机构树")
public class CommonTreeController extends BaseController
{
    private String prefix = "commonTree/commonTree";

    @Autowired
    private ICommonTreeService commonTreeService;
    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Resource
    ISysWorkService iSysWorkService;
    @Resource
    IChangePersonService changePersonService;
    @Resource
    private IOgGroupPersonService ogGroupPersonService;
    @Resource
    ISysDeptService iSysDeptService;
    @Resource
    IChangePersonService iChangePersonService;
    @Autowired
    private IOgUserPostService ogUserPostService;
    @GetMapping()
    public String commonTree()
    {
        return prefix + "/commonTree";
    }

    /**
     * 查询commonTree列表
     */
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation(value = "查询机构")
    public AjaxResult list(CommonTree commonTree)
    {
        List<SysDeptVo> list = commonTreeService.selectCommonTreeList(commonTree);
        return AjaxResult.success(list);
    }
    /**
     * 查询commonTree列表
     */
    @PostMapping("/listPage")
    @ResponseBody
    public TableDataInfo listPage(CommonTree commonTree)
    {
        startPage();
        List<CommonTree> list = commonTreeService.selectCommonTreeListPage(commonTree);
        return getDataTable(list);
    }

    /**
     * 查询commonTree列表
     */
    @GetMapping("/listData")
    @ResponseBody
    public List<Ztree> listData()
    {
        List<Ztree> list = commonTreeService.selectCommonTreeListData();
        return list;
    }
    /**
     * 查询commonTree分行列表
     */
    @PostMapping("/listDataChild")
    @ResponseBody
    public AjaxResult listDataChild()
    {
        List<Object> list = commonTreeService.selectCommonTreeListDataChild();
        return AjaxResult.success(list);
    }
    /**
     * 加载问题审核人/部室经理
     *
     * @return
     */
    @PostMapping("/auditOgPerson")
    @ResponseBody
    @ApiOperation(value = "加载问题单审核人")
    public AjaxResult auditPerson(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("Id"))) {
            throw new BusinessException("请先选择部室!");
        }
        CommonTree commonTree=commonTreeService.selectCommonTreeById(jsonObject.getLong("Id"));
        String orgId=commonTree.getOgId();
        QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<ChangeDeptPersonEntity>();
        queryWrapper.eq("dept_id",orgId);
        List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
        List<Object> ogPeopleMapList = changeDeptPersonEntityList.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            if(StringUtils.isNotEmpty(op.getDeptPerson())){
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(op.getDeptPerson());
                if(ogPerson!=null){
                    mapPeople.put("value",ogPerson.getpId());
                    mapPeople.put("label", ogPerson.getpName());
                }
            }
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }

    /*** 加载问题牵头人列表
     *
     * @return
     */
    @PostMapping("/listOgPerson")
    @ResponseBody
    @ApiOperation(value = "加载问题单牵头部室人员列表")
    public AjaxResult listOgPerson(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("Id"))) {
            return AjaxResult.error("请先选择机构!");
        }
        CommonTree commonTree=commonTreeService.selectCommonTreeById(jsonObject.getLong("Id"));
        String orgId=commonTree.getOgId();
        List<OgPerson> ogPeopleList =new ArrayList<>();
        if("1".equals(commonTree.getType())){
            //如果选择的是组 查询组内人员
            if(StringUtils.isNotEmpty(orgId)){
                ogPeopleList = iSysWorkService.selectGroupByPerson(orgId);
            }
            if(CollectionUtil.isEmpty(ogPeopleList)){
                return AjaxResult.error("该组下尚未配置人员！");
            }
        }else {
            // 分行情况
            List<PubParaValue> bankList = pubParaValueService.selectPubParaValueById("60a02cbc224344749c4d1d0ec65f6d5a");
            // 获取到各分行id
            List<String> orgidList = bankList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList());
            if (orgidList.contains(orgId)) {
                OgOrg org = new OgOrg();
                org.setOrgName("信息技术部");
                org.setLevelCode("/310100001/310100002/" + orgId);
                org.setInoutsideMark("1");
                List<OgOrg> ogOrgs = iSysDeptService.selectDeptList(org);
                if (CollectionUtils.isEmpty(ogOrgs)) {
                    return AjaxResult.error("机构不存在");
                }
                OgPerson ogPersonQuery = new OgPerson();
                ogPersonQuery.setInvalidationMark("1");
                ogPersonQuery.setOrgId(ogOrgs.get(0).getOrgId());
                ogPeopleList = iOgPersonService.selectOgPersonList(ogPersonQuery);
                if (CollectionUtils.isEmpty(ogPeopleList)) {
                    return AjaxResult.error("该所属机构下尚未配置人员！");
                }
            } else {
                // 总行
                //如果选择的是机构查询机构内人员
                OgPerson ogPersonQuery = new OgPerson();
                ogPersonQuery.setOrgId(orgId);
                ogPersonQuery.setInvalidationMark("1");
                ogPeopleList = iOgPersonService.selectOgPersonList(ogPersonQuery);
                if (CollectionUtils.isEmpty(ogPeopleList)) {
                    return AjaxResult.error("该所属机构下尚未配置人员！");
                }
            }
        }
//        List<OgPerson> unleaderPersonList = ogPeopleList.stream().filter(ogPerson ->  !StringUtils.equals("1", ogPerson.getLeader())).collect(Collectors.toList());
        List<Object> ogPeopleMapList = ogPeopleList.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            mapPeople.put("value", op.getpId());
            String name = op.getpName();
            if (StringUtils.isNotBlank(op.getEmail())) {
                name = name + "(" + StringUtils.substring(op.getEmail(), 0, op.getEmail().indexOf("@")) + ")";
            }
            mapPeople.put("label", name);
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }
    /*** 硬件报障加载硬件支持人员 分行
     *加载分行下 硬件报障岗位人员
     * @return
     */
    @PostMapping("/chmListPerson")
    @ResponseBody
    @ApiOperation(value = "硬件报障加载硬件支持人员 分行")
    public AjaxResult chmListPerson(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("Id"))) {
            return AjaxResult.error("请先选择机构!");
        }
        String orgId=jsonObject.getString("Id");
        List<OgPerson> ogPeopleList = iOgPersonService.selectListByOrgIdAllAndGroupId(orgId, CustomerFlowConstants.CHM_YJ_GROUP);
        if (CollectionUtils.isEmpty(ogPeopleList)) {
            return AjaxResult.error("该所属机构下尚未配置人员！");
        }
        List<OgPerson> unleaderPersonList = ogPeopleList.stream().filter(ogPerson ->  !StringUtils.equals("1", ogPerson.getLeader())).collect(Collectors.toList());
        List<Object> ogPeopleMapList = unleaderPersonList.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            mapPeople.put("value", op.getpId());
            mapPeople.put("label", op.getpName());
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }
    /*** 根据问题发起人加载问题发起部室
     *
     * @return
     */
    @PostMapping("/queryOriDept")
    @ResponseBody
    @ApiOperation(value = "根据问题发起人加载问题发起部室")
    public AjaxResult queryOriDept(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("userId"))) {
            return AjaxResult.error("请先选择发起人!");
        }
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(jsonObject.getString("userId"));
        OgOrg org = iSysDeptService.selectDeptById(ogPerson.getOrgId());
        List<Map<String, Object>> orgList = Lists.newArrayList();
        Map<String, Object> oriDept = new HashMap<>();
        oriDept.put("value", org.getOrgId());
        oriDept.put("label", org.getOrgName());
        orgList.add(oriDept);
        return AjaxResult.success(orgList);
    }

    /*** 根据问题发起人加载问题发起部室经理
     *
     * @return
     */
    @PostMapping("/queryOriDeptManagerId")
    @ResponseBody
    @ApiOperation(value = "根据问题发起人加载问题发起部室经理")
    public AjaxResult queryOriDeptManagerId(@RequestBody JSONObject jsonObject) {
        if (ObjectUtil.isEmpty(jsonObject.get("userId"))) {
            return AjaxResult.error("请先选择发起人!");
        }
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(jsonObject.getString("userId"));
        if (ogPerson == null) {
            return AjaxResult.error(String.format("根据id:%s查询问题发起人为空!", jsonObject.getString("userId")));
        }
        QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<ChangeDeptPersonEntity>();
        queryWrapper.eq("dept_id", ogPerson.getOrgId());
        List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
        if (CollectionUtils.isEmpty(changeDeptPersonEntityList)) {
            return AjaxResult.error(String.format("请配置问题发起人:%s所在部室经理!", ogPerson.getpName()));
        }
        List<Object> ogPeopleMapList = changeDeptPersonEntityList.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            if (StringUtils.isNotEmpty(op.getDeptPerson())) {
                OgPerson og = iOgPersonService.selectOgPersonById(op.getDeptPerson());
                if (og != null) {
                    mapPeople.put("value", og.getpId());
                    mapPeople.put("label", og.getpName());
                }
            }
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }
    /**
     * 导出commonTree列表
     */
//    @Log(title = "commonTree", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(CommonTree commonTree)
//    {
//        List<SysDeptVo> list = commonTreeService.selectCommonTreeList(commonTree);
//        ExcelUtil<CommonTree> util = new ExcelUtil<CommonTree>(CommonTree.class);
//        return util.exportExcel(list, "commonTree");
//    }

    /**
     * 新增commonTree
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存commonTree
     */
    @Log(title = "commonTree", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CommonTree commonTree)
    {
        return toAjax(commonTreeService.insertCommonTree(commonTree));
    }

    /**
     * 修改commonTree
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CommonTree commonTree = commonTreeService.selectCommonTreeById(id);
        mmap.put("commonTree", commonTree);
        return prefix + "/edit";
    }

    /**
     * 修改保存commonTree
     */
    @Log(title = "commonTree", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CommonTree commonTree)
    {
        return toAjax(commonTreeService.updateCommonTree(commonTree));
    }

    /**
     * 删除commonTree
     */
    @Log(title = "commonTree", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(commonTreeService.deleteCommonTreeByIds(ids));
    }

    /***
     * 查询问题发起人
     *
     * @return
     */
    @PostMapping("/queryOgPersonList")
    @ResponseBody
    @ApiOperation(value = "查询人")
    public AjaxResult queryProblemOriginators() {
        OgPerson ogPersonQuery = new OgPerson();
        List<OgPerson> personList = iOgPersonService.selectOgPersonList(ogPersonQuery);
        List<Object> ogPeopleMapList = personList.stream().map(op -> {
            Map<String, String> mapPeople = new HashMap<>();
            mapPeople.put("value", op.getpId());
            String name = op.getpName();
            if (StringUtils.isNotBlank(op.getEmail())) {
                name = name + "("+StringUtils.substring(op.getEmail(), 0, op.getEmail().indexOf("@")) +")";
            }
            mapPeople.put("label", name);
            return mapPeople;
        }).collect(Collectors.toList());
        return AjaxResult.success(ogPeopleMapList);
    }
}
