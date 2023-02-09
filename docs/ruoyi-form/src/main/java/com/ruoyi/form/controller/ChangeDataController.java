package com.ruoyi.form.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.cache.CacheManager;
import com.ruoyi.form.domain.ChangeTaskOrg;
import com.ruoyi.form.domain.ChangeTaskScene;
import com.ruoyi.form.domain.ImplRecord;
import com.ruoyi.form.domain.RiskAccessRecord;
import com.ruoyi.form.enums.ChangeFieldEnum;
import com.ruoyi.form.enums.ChangeTableNameEnum;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import com.ruoyi.form.service.*;
import com.ruoyi.form.vo.ChangeRetrievalVo;
import com.ruoyi.form.vo.ChangeTaskRetrievalVo;
import com.ruoyi.form.vo.ExportExcelParams;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 */
@RestController
@RequestMapping("/changeData")
public class ChangeDataController {

    @Autowired
    IChangeTaskSceneService changeTaskSceneService;
    @Autowired
    IChangeTaskOrgService changeTaskOrgService;
    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IChangeService changeService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    ISysDeptService sysDeptService;
    @Autowired
    ISysRoleService sysRoleService;
    @Autowired
    IRiskAccessRecordService riskAccessRecordService;
    @Autowired
    IChangePersonService changePersonService;
    @Autowired
    Base base;
    @Autowired
    IImplRecordService iImplRecordService;
    @Autowired
    OgPostService ogPostService;
    @Autowired
    IOgUserService ogUserService;
    @Autowired
    ForeignService foreignService;
    @Autowired
    DesignBizChangetaskService designBizChangetaskService;
    @Autowired
    DesignBizChangeService designBizChangeService;
    CacheManager<List<Map<String, String>>> cacheManager = new CacheManager<>();


    @GetMapping("/task/dept/list")
    @ApiOperation("任务需要的部门列表")
    public AjaxResult findDeptList(@RequestHeader("CurrentUserId") String currentUserId) {
        List<Map<String, String>> list = new ArrayList<>();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(currentUserId);
        String orgId = ogPerson.getOrgId();
        OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
        if ("1".equals(ogOrg.getType())) {
            String branchOrgId = changePersonService.selectDept(ogPerson.getOrgId());
            OgOrg branch = sysDeptService.selectDeptById(branchOrgId);
            Map<String, String> map = new HashMap<>();
            map.put("label", branch.getOrgName());
            map.put("value", branchOrgId);
            list.add(map);
            return AjaxResult.success(list);
        }
        List<ChangeTaskOrg> orgList = changeTaskOrgService.getAll();
        orgList.forEach(p -> {
            if (!"分行".equals(p.getOrgName())) {
                Map<String, String> map = new HashMap<>();
                map.put("label", p.getOrgName());
                map.put("value", p.getOrgid());
                list.add(map);
            }
        });
        return AjaxResult.success(list);
    }

    @PostMapping("/scene")
    public AjaxResult findSceneType(@RequestBody String orgId) {
        JSONObject object = JSONObject.parseObject(orgId);
        String orgIdStr = object.getString("orgId");
        OgOrg ogOrg = sysDeptService.selectDeptById(orgIdStr);
        List<Map<String, String>> result = new ArrayList<>();
        if ("1".equals(ogOrg.getType())) {
            Map<String, String> map = new HashMap<>();
            map.put("label", "分行变更任务");
            map.put("value", "branch");
            result.add(map);
            return AjaxResult.success(result);
        }
        List<ChangeTaskScene> sceneList = changeTaskSceneService.getListByOrgId(orgIdStr);
        sceneList.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", p.getName());
            map.put("value", p.getCode());
            if ("通用".equals(p.getName())) {
                result.add(0, map);
            } else {
                result.add(map);
            }
        });
        return AjaxResult.success(result);
    }

    @PostMapping("/auto/scene")
    public AjaxResult findAutoSceneType(@RequestBody String orgId) {
        JSONObject object = JSONObject.parseObject(orgId);
        String orgIdStr = object.getString("orgId");
        List<Map<String, String>> result = new ArrayList<>();
        List<ChangeTaskScene> sceneList = changeTaskSceneService.getAutoListByOrgId(orgIdStr);
        sceneList.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", p.getName());
            map.put("value", p.getCode());
            result.add(map);
        });
        return AjaxResult.success(result);
    }

    @GetMapping("/scene/field")
    public AjaxResult findSceneField(@RequestParam("code") String code, @RequestParam(value = "changeTaskNo", required = false, defaultValue = "") String changeTaskNo) {
        List<Map<String, Object>> list = changeService.getSceneData(code, changeTaskNo);
        return AjaxResult.success(list);
    }

    @PostMapping("/dept/users")
    @ApiOperation("根据部门id获取该部门下的所有人")
    public AjaxResult getOrgUser(@RequestBody String orgId) {
        JSONObject object = JSONObject.parseObject(orgId);
        List<Map<String, String>> ogPersonList = new ArrayList<>();
        List<OgPerson> personList = ogPersonService.selectOgPersonListByOrgId(object.getString("orgId"));
        personList.forEach(p -> {
            String userId = p.getpId();
            OgUser user = ogUserService.selectOgUserByUserId(userId);
            if (user != null) {
                Map<String, String> map = new HashMap<>();
                map.put("label", p.getpName() + "(" + user.getUsername() + ")");
                map.put("value", userId);
                ogPersonList.add(map);
            }
        });
        return AjaxResult.success(ogPersonList);
    }

    @PostMapping("/dept/approval/users")
    public AjaxResult getOrgApprovalUser(@RequestBody String orgId) {
        JSONObject object = JSONObject.parseObject(orgId);
        List<Map<String, String>> ogPersonList = new ArrayList<>();
        OgPost ogPost = new OgPost();
        ogPost.setPostName("预审人");
        List<OgPost> postList = ogPostService.selectPostList(ogPost);
        String postStr = postList.get(0).getPostId();
        String orgIdStr = object.getString("orgId");
        List<OgPerson> personList = ogPersonService.selectListByOrgIdAllAndPostId(orgIdStr, postStr);
        OgOrg ogOrg = sysDeptService.selectDeptById(orgIdStr);
        if ("1".equals(ogOrg.getType())) {
            //如果是分行，那么要把分行下属机构的预审人都放进去
            personList = ogPersonService.selectListByOrgIdAllAndPostIdBosc(orgIdStr, postStr);
        }
        personList.forEach(p -> {
            String userId = p.getpId();
            OgUser user = ogUserService.selectOgUserByUserId(userId);
            if (user != null) {
                Map<String, String> map = new HashMap<>();
                map.put("label", p.getpName() + "(" + user.getUsername() + ")");
                map.put("value", userId);
                ogPersonList.add(map);
            }
        });
        return AjaxResult.success(ogPersonList);
    }

    @PostMapping("/param/value/{paraName}")
    public AjaxResult accessValue(@PathVariable("paraName") String paraName) {
        List<PubParaValue> pubParaValueList = pubParaValueService.selectPubParaValueByParaName(paraName);
        List<Map<String, String>> result = new ArrayList<>();
        pubParaValueList.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", p.getValueDetail());
            map.put("value", p.getValue());
            result.add(map);
        });
        return AjaxResult.success(result);
    }

    @PostMapping("/ip/list")
    public AjaxResult getIpListByCode(@RequestBody JSONObject jsonObject) {
        Object type = jsonObject.get("type");
        if (type == null) {
            return AjaxResult.warn("请传入type");
        }
        List<Map<String, Object>> pubParaValueList = new ArrayList<>();
        if ("1".equals(type.toString())) {
            //TODO 应用
            Object code = jsonObject.get("appCode");
            if (code == null) {
                return AjaxResult.warn("请传入应用paso");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("", "");
            pubParaValueList.add(map);
        } else if ("2".equals(type.toString())) {
            //TODO 设备
            Object code = jsonObject.get("netCode");
            if (code == null) {
                return AjaxResult.warn("请传入网络设备参数");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("", "");
            pubParaValueList.add(map);
        }
        List<Map<String, String>> result = new ArrayList<>();
        pubParaValueList.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", "127.0.0.1");
            map.put("value", "127.0.0.1");
            result.add(map);
        });
        return AjaxResult.success(result);
    }


    @PostMapping("/param/detail/{paramName}")
    public AjaxResult accessStatusValue(@PathVariable("paramName") String paramName) {
        List<PubParaValue> pubParaValueList = pubParaValueService.selectPubParaValueByParaName(paramName);
        List<Map<String, String>> result = new ArrayList<>();
        pubParaValueList.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", p.getValueDetail());
            map.put("value", p.getValueDetail());
            result.add(map);
        });
        return AjaxResult.success(result);
    }

    @GetMapping("/syncOrgId")
    public AjaxResult sync() {
        Thread thread = new Thread(() -> {
            changeTaskSceneService.syncSceneOrgId();
        });
        thread.setName("sync-orgId");
        thread.start();
        //待解决，如何在有值的时候返回，总不能用while(true)吧
        String message = IChangeTaskSceneService.syncMessage.get();
        return AjaxResult.success(message);
    }

    @PostMapping("/addOrg")
    public AjaxResult insertOrg(@RequestBody ChangeTaskOrg changeTaskOrg) {
        changeTaskOrgService.insert(changeTaskOrg);
        return AjaxResult.success();
    }

    @PostMapping("/addScene")
    public AjaxResult insertScene(@RequestBody ChangeTaskScene changeTaskScene) {
        changeTaskSceneService.insert(changeTaskScene);
        return AjaxResult.success();
    }

    @PostMapping("/updateOrg")
    public AjaxResult updateOrg(@RequestBody ChangeTaskOrg changeTaskOrg) {
        changeTaskOrgService.updateById(changeTaskOrg);
        return AjaxResult.success();
    }

    @PostMapping("/updateScene")
    public AjaxResult updateScene(@RequestBody ChangeTaskScene changeTaskScene) {
        changeTaskSceneService.updateById(changeTaskScene);
        return AjaxResult.success();
    }


    @PostMapping("/sys/all")
    public AjaxResult getAllSys() {
        List<Map<String, String>> valueMap = cacheManager.getValue("sys");
        if (valueMap == null) {
            List<OgSys> list = sysApplicationManagerService.selectOgSysList(new OgSys());
            List<Map<String, String>> result = new ArrayList<>();
            list.forEach(p -> {
                Map<String, String> map = new HashMap<>();
                map.put("label", p.getCaption() + "(" + p.getCode() + ")");
                map.put("value", p.getCode());
                result.add(map);
            });
            cacheManager.addOrUpdateCache("sys", result);
            return AjaxResult.success(result);
        }
        return AjaxResult.success(valueMap);
    }

    @PostMapping("/infrastructure/all")
    public AjaxResult getAllInfrastructure() {
        List<Map<String, String>> result = new ArrayList<>();
        List<Map<String, String>> valueMap = cacheManager.getValue("sys");
        if (valueMap == null) {
            List<OgSys> list = sysApplicationManagerService.selectOgSysList(new OgSys());
            list.forEach(p -> {
                Map<String, String> map = new HashMap<>();
                map.put("label", p.getCaption() + "(" + p.getCode() + ")");
                map.put("value", p.getCode());
                result.add(map);
            });
            cacheManager.addOrUpdateCache("sys", result);
        } else {
            result.addAll(valueMap);
        }
        //加上换成基础设施的数据
        List<PubParaValue> pubParaValueList = pubParaValueService.selectPubParaValueByParaName("infrastructure");
        pubParaValueList.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", p.getValueDetail());
            map.put("value", p.getValue());
            result.add(map);
        });
        return AjaxResult.success(result);
    }

    @PostMapping("/person/all")
    public AjaxResult getAllPerson() {
        List<Map<String, String>> valueMap = cacheManager.getValue("person");
        if (valueMap == null) {
            List<OgPerson> list = ogPersonService.selectOgPersonList(new OgPerson());
            List<Map<String, String>> result = new ArrayList<>();
            list.forEach(p -> {
                String userId = p.getpId();
                OgUser user = ogUserService.selectOgUserByUserId(userId);
                if (user != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("label", p.getpName() + "(" + user.getUsername() + ")");
                    map.put("value", userId);
                    result.add(map);
                }
            });
            cacheManager.addOrUpdateCache("person", result);
            return AjaxResult.success(result);
        }
        return AjaxResult.success(valueMap);
    }

    @GetMapping("/cache/clear")
    @ApiOperation("清空缓存")
    public AjaxResult clearCache() {
        cacheManager.clearCache();
        return AjaxResult.success();
    }


    @PostMapping("/process/data")
    @ApiOperation("获取发起科室的流程数据")
    public AjaxResult getProcessData(@RequestParam("changeNo") String changeNo) {
        return changeService.processData(changeNo);
    }

    @PostMapping("/task/button/list")
    @ApiOperation("获取任务详情列表里的按钮")
    public AjaxResult getButtonList(@RequestHeader("CurrentUserId") String currentId, @RequestParam("changeNo") String changeNo) {
        return changeService.getButtonList(currentId, changeNo);
    }

    @GetMapping("/adpm/data")
    @ApiOperation("获取adpm的数据")
    public AjaxResult getAdpmData(@RequestParam("changeNo") String changeNo) {
        String appProcessId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.APP_PROCESS_ID, ChangeFieldEnum.EXTRA1, changeNo);
        if (appProcessId == null || "".equals(appProcessId.trim())) {
            return AjaxResult.success("非adpm单子");
        }
        return foreignService.queryAdpmTask(changeNo);
    }

    @PostMapping("/dept/all")
    public AjaxResult getAllDept() {
        List<OgOrg> list = sysDeptService.selectDeptList(new OgOrg());
        List<Map<String, String>> result = new ArrayList<>();
        list.forEach(p -> {
            Map<String, String> map = new HashMap<>();
            if (p.getParentName() == null) {
                map.put("label", p.getOrgName());
            } else {
                map.put("label", p.getParentName() + "-" + p.getOrgName());
            }
            map.put("value", p.getOrgId());
            result.add(map);
        });
        return AjaxResult.success(result);
    }

    @PostMapping("/person/multi/dept")
    @ApiOperation("根据部门列表获取数据")
    public AjaxResult getAllPersonByDeptList(@RequestBody JSONObject jsonObject) {
        List<String> deptList = (List<String>) jsonObject.get("deptList");
        List<Map<String, Object>> result = new ArrayList<>();
        deptList.forEach(p -> {
            List<OgPerson> list = ogPersonService.selectOgPersonListByOrgId(p);
            list.stream().filter(Objects::nonNull).forEach(a -> {
                String userId = a.getpId();
                OgUser user = ogUserService.selectOgUserByUserId(userId);
                if (user != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("label", a.getpName() + "(" + user.getUsername() + ")");
                    map.put("value", userId);
                    result.add(map);
                }
            });
        });
        return AjaxResult.success(result);
    }

    @PostMapping("/checkMan/all")
    @ApiOperation("获取所有复核人")
    public AjaxResult getAllCheckMan() {
        List<Map<String, String>> valueMap = cacheManager.getValue("person");
        if (valueMap == null) {
            List<OgPerson> list = ogPersonService.selectOgPersonList(new OgPerson());
            List<Map<String, String>> result = new ArrayList<>();
            list.forEach(p -> {
                String userId = p.getpId();
                OgUser user = ogUserService.selectOgUserByUserId(userId);
                if (user != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("label", p.getpName() + "(" + user.getUsername() + ")");
                    map.put("value", userId);
                    result.add(map);
                }
            });
            cacheManager.addOrUpdateCache("person", result);
            return AjaxResult.success(result);
        }
        return AjaxResult.success(valueMap);
    }

    @GetMapping("/arrange/detail")
    public AjaxResult arrangeDetail(@RequestParam("changeTaskNo") String changeTaskNo) {
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        Map<String, Object> map = list.get(0);
        String referSys = map.get("referSys").toString();
        OgSys ogSys = sysApplicationManagerService.selectOgSysBySysCode(referSys);
        String sysName = ogSys.getCaption();
        String content = map.get("content").toString();
        ImplRecord implRecord = new ImplRecord();
        implRecord.setChangeTaskNo(changeTaskNo);
        List<ImplRecord> recordList = iImplRecordService.selectImplRecordList(implRecord);
        ImplRecord record = recordList.get(0);
        String userId = record.getUserid();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
        String username = ogPerson.getpName();
        Map<String, Object> result = new HashMap<>();
        result.put("changeTaskNo", changeTaskNo);
        result.put("referSys", sysName);
        result.put("content", content);
        result.put("implPerson", username);
        result.put("memo", "");
        return AjaxResult.success(result);
    }

    @PostMapping("/risk/access/record/all")
    public AjaxResult getRiskAccessRecords(@RequestParam("changeNo") String changeNo) {
        RiskAccessRecord riskAccessRecord = new RiskAccessRecord();
        riskAccessRecord.setChangeNo(changeNo);
        List<RiskAccessRecord> recordList = riskAccessRecordService.selectRiskAccessRecordList(riskAccessRecord);
        List<Map<String, Object>> colList = new ArrayList<>();
        Map<String, Object> timeMap = new HashMap<>();
        timeMap.put("label", "操作时间");
        timeMap.put("val", "operateTime");
        Map<String, Object> operatorMap = new HashMap<>();
        operatorMap.put("label", "操作人员");
        operatorMap.put("val", "operator");
        Map<String, Object> initLevelMap = new HashMap<>();
        initLevelMap.put("label", "原始风险级别");
        initLevelMap.put("val", "initLevel");
        Map<String, Object> updateLevelMap = new HashMap<>();
        updateLevelMap.put("label", "当前风险级别");
        updateLevelMap.put("val", "currentLevel");
        Map<String, Object> reasonMap = new HashMap<>();
        reasonMap.put("label", "变更原因");
        reasonMap.put("val", "reason");
        colList.add(timeMap);
        colList.add(operatorMap);
        colList.add(initLevelMap);
        colList.add(updateLevelMap);
        colList.add(reasonMap);
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        result.put("list", recordList);
        result.put("total", recordList.size());
        result.put("pageNum", 1);
        result.put("pageSize", 50);
        return AjaxResult.success(result);
    }

    @PostMapping("/scene/data/list")
    @ApiOperation("获取自定义表单的数据列表")
    public AjaxResult getSceneDataList(@RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("code") String code,
                                       @RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize) {
        List<Map<String, Object>> fieldList = changeService.getFormField(code);
        Map<String, Object> idMap = new HashMap<>();
        idMap.put("name", "id");
        idMap.put("description", "ID");
        fieldList.add(0, idMap);
        Page<Map<String, Object>> page = new Page<>();
        page.setCurrent(Long.parseLong(pageNum));
        page.setSize(Long.parseLong(pageSize));
        Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(code, "changeTaskNo", changeTaskNo, page);
        if (resultPage == null) {
            return AjaxResult.warn("不存在表：" + code);
        }
        List<Map<String, Object>> recordList = resultPage.getRecords();
        recordList.forEach(p -> {
            p.put("t_identify", "del_scene_data");
        });
        List<Map<String, Object>> colList = new ArrayList<>();
        for (Map<String, Object> field : fieldList) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", field.get("description"));
            map.put("val", field.get("name"));
            colList.add(map);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        result.put("list", recordList);
        result.put("total", resultPage.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return AjaxResult.success(result);
    }

    @PostMapping("/del/scene/data")
    @ApiOperation("根据id删除一个表单数据")
    public AjaxResult delSceneData(@RequestParam("id") String id, @RequestParam("code") String code) {
        changeService.delARowBySingleCondition(code, "id", id);
        return AjaxResult.success();
    }

    @PostMapping("/save/scene/data")
    @ApiOperation("新增一个表单数据")
    public AjaxResult saveSceneData(@RequestParam("code") String code, @RequestBody Map<String, Object> fields) {
        changeService.addARowSceneData(code, fields);
        return AjaxResult.success();
    }

    @PostMapping("/task/list/to/table")
    public AjaxResult getTaskListToTable(@RequestParam("changeNo") String changeNo) {
        return changeService.changeTaskListToTable(changeNo);
    }

    @PostMapping("/admin/approval/record/list")
    public AjaxResult getAdminApprovalRecordList(@RequestParam("changeNo") String changeNo) {
        return changeService.getAdminApprovalRecord(changeNo);
    }

    @PostMapping("/total/list")
    public AjaxResult getTotalList(@RequestParam("type") Integer type, @RequestParam(value = "date", required = false, defaultValue = "") String date,
                                   @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        if ("".equals(date)) {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            date = dateTimeFormatter.format(localDate);
        }
        //TODO 校验日期格式
        return changeService.getTotalList(type, date, pageNum, pageSize);
    }

    @PostMapping("/export/change/total")
    @ApiOperation("变更统计台账导出")
    public void exportTotalDataList(@RequestParam(value = "date", required = false, defaultValue = "") String date, HttpServletResponse response) {
        if ("".equals(date)) {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            date = dateTimeFormatter.format(localDate);
        }
        //TODO 校验日期格式
        List<String> changeIdList = base.getChangeAllTotalId(date);
        ChangeRetrievalVo changeRetrievalVo = new ChangeRetrievalVo();
        ExportExcelParams exportExcelParams = new ExportExcelParams();
        exportExcelParams.setCode("change");
        exportExcelParams.setIds(changeIdList);
        exportExcelParams.setType("2");
        changeRetrievalVo.setExcelParams(exportExcelParams);
        designBizChangeService.changeRetrieval(changeRetrievalVo, response);
    }

    @PostMapping("/export/changetask/total")
    @ApiOperation("变更统计--缺陷任务台账导出")
    public void exportDefectTaskTotalDataList(@RequestParam(value = "date", required = false, defaultValue = "") String date, HttpServletResponse response) {
        if ("".equals(date)) {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            date = dateTimeFormatter.format(localDate);
        }
        //TODO 校验日期格式
        List<String> changeTaskIdList = base.getChangeTaskAllTotalId(date);
        ChangeTaskRetrievalVo changeTaskRetrievalVo = new ChangeTaskRetrievalVo();
        ExportExcelParams exportExcelParams = new ExportExcelParams();
        exportExcelParams.setCode("changeTask");
        exportExcelParams.setIds(changeTaskIdList);
        exportExcelParams.setType("2");
        changeTaskRetrievalVo.setExcelParams(exportExcelParams);
        designBizChangetaskService.changeTaskRetrieval(changeTaskRetrievalVo, response);
    }

    @PostMapping("/sys/deal/list")
    @ApiOperation("获取变更单所涉系统的相关统计数据")
    public AjaxResult getSysDealList(@RequestParam("changeNo") String changeNo) {
        return changeService.getSysDealList(changeNo);
    }

    @PostMapping("/start/dept/data")
    @ApiOperation("获取变更单发起科室管理相关数据")
    public AjaxResult getStartDeptMangerData(@RequestParam("changeNo") String changeNo) {
        return changeService.getStartDeptMangerData(changeNo);
    }

    @GetMapping("/total/page")
    public AjaxResult getTotalPage() {
        return changeService.getChangeTotalPage();
    }

    @PostMapping("/year/list")
    public AjaxResult getYearList() {
        List<Map<String, Object>> list = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        int yearNum = localDate.getYear();
        for (int i = 2022; i <= yearNum; ++i) {
            Map<String, Object> year = new HashMap<>();
            year.put("label", i + "");
            year.put("value", i + "");
            list.add(year);
        }
        return AjaxResult.success(list);
    }


    @PostMapping("/app/access/data")
    @ApiOperation("获取变更单评估页签的数据")
    public AjaxResult getAppAccessData(@RequestParam("changeNo") String changeNo, @RequestParam("type") Integer type) {
        if (type == 1) {
            return changeService.getAppAccessData(changeNo, "env_");
        } else if (type == 2) {
            return changeService.getAppAccessData(changeNo, "data_");
        } else if (type == 3) {
            return changeService.getAppAccessData(changeNo, "code_");
        } else if (type == 4) {
            return changeService.getAppAccessData(changeNo, "mo_");
        } else if (type == 5) {
            return changeService.getAppAccessData(changeNo, "releate_");
        }
        return AjaxResult.warn("无此类型数据");
    }
 /*   @Autowired
    RuntimeService runtimeService;
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @RequestMapping("/cancel/all/instance")
    public AjaxResult cancelAllInstanceId(@RequestParam("table") String table) {
        ChangeTableNameEnum changeTableNameEnum = ChangeTableNameEnum.valueOf(table);
        String sql = "select instance_id from " + changeTableNameEnum.getName();
        List<String> list = jdbcTemplate.queryForList(sql, new HashMap<>(), String.class);
        list.forEach(p -> {
            try {
                runtimeService.deleteProcessInstance(p, "");
            }catch (Exception e){
                System.out.println(p);
            }
        });
        return AjaxResult.success();
    }*/
}
