package com.ruoyi.form.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.enums.ChangeTableNameEnum;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import com.ruoyi.form.service.ForeignService;
import com.ruoyi.form.service.IChangeService;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.system.service.IPubParaValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.form.domain.DesignBizShieldWarn;
import com.ruoyi.form.service.IDesignBizShieldWarnService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 屏蔽告警Controller
 *
 * @author ruoyi
 * @date 2022-11-22
 */
@RestController
@CrossOrigin
@RequestMapping("/changeData/shield/warn")
@Api(tags = "告警屏蔽模块接口")
public class DesignBizShieldWarnController extends BaseController {
    private String prefix = "system/warn";

    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IDesignBizShieldWarnService designBizShieldWarnService;
    @Autowired
    IChangeService changeService;
    @Autowired
    private ForeignService foreignService;
    @Autowired
    Base base;
    /*@RequiresPermissions("system:warn:view")
    @GetMapping()
    public String warn()
    {
        return prefix + "/warn";
    }*/

    @PostMapping("/create/shield/warn")
    @ResponseBody
    @ApiOperation("开启告警屏蔽")
    public AjaxResult maintain(@RequestParam("changeTaskNo") String changeTaskNo) {
        Map<String, Object> data = new HashMap<>();
        String timeType = "perm";
//        LocalDateTime nowTime = LocalDateTime.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String beginTime = dateTimeFormatter.format(nowTime);
        List<DesignBizShieldWarn> dataList = designBizShieldWarnService.selectDesignBizShieldWarnByChangeTaskNo(changeTaskNo);
        if(dataList.isEmpty()){
            return AjaxResult.success("无屏蔽数据");
        }
        Map<String,Object> condition = base.parseToCondition(dataList);
        System.out.println(JSON.toJSONString(condition));
        String name = "变更任务屏蔽告警"+"【"+changeTaskNo+"】";
        data.put("id",System.currentTimeMillis());
        data.put("name", name);
        data.put("active", true);
        data.put("timeType", timeType);
//        data.put("beginTime", beginTime);
        /*data.put("endTime", "");*/
        data.put("changeId", changeTaskNo);
        data.put("condition", condition);
        return foreignService.maintain(data);
    }

    /**
     * 维护窗口关闭接口
     *
     * @param
     * @return
     */
    @PostMapping("/close/shield/warn")
    @ResponseBody
    @ApiOperation("关闭告警屏蔽")
    public AjaxResult updateActive(@RequestParam("changeTaskNo")String changeTaskNo) {
        List<DesignBizShieldWarn> dataList = designBizShieldWarnService.selectDesignBizShieldWarnByChangeTaskNo(changeTaskNo);
        if(dataList.isEmpty()){
            return AjaxResult.success("无屏蔽数据");
        }
        return foreignService.updateActive(changeTaskNo, false);
    }

    /**
     * 查询屏蔽告警列表
     */
    @PostMapping("/list")
    @ApiOperation("屏蔽告警列表")
    public AjaxResult list(@RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        DesignBizShieldWarn designBizShieldWarn = new DesignBizShieldWarn();
        designBizShieldWarn.setChangeTaskNo(changeTaskNo);
        List<PubParaValue> pubParaValueList = pubParaValueService.selectPubParaValueByParaName("systemCode");
        List<DesignBizShieldWarn> list = designBizShieldWarnService.selectDesignBizShieldWarnList(designBizShieldWarn,pageNum,pageSize);
        list.forEach(p->{
             String location = p.getSystemCode();
             String type = p.getShieldType();
             if("1".equals(type)){
                 p.setShieldType("应用");
             }else if("2".equals(type)){
                 p.setShieldType("设备");
             }
             if(location!=null&&!"".equals(location)){
                 String[] locationArray = location.split(",");
                 StringJoiner stringJoiner = new StringJoiner(",");
                 for(String l:locationArray){
                     PubParaValue pubParaValue = pubParaValueList.stream().filter(a->l.equals(a.getValue())).findFirst().orElse(null);
                     if(pubParaValue!=null){
                         stringJoiner.add(pubParaValue.getValueDetail());
                     }else {
                         stringJoiner.add(l);
                     }
                 }
                 p.setSystemCode(stringJoiner.toString());
             }
        });
        Integer count = designBizShieldWarnService.count(designBizShieldWarn);
        List<Map<String, Object>> colList = new ArrayList<>();
        Map<String, Object> IDMap = new HashMap<>();
        IDMap.put("label", "ID");
        IDMap.put("val", "id");
        Map<String, Object> timeMap = new HashMap<>();
        timeMap.put("label", "类型");
        timeMap.put("val", "shieldType");
        Map<String, Object> operatorMap = new HashMap<>();
        operatorMap.put("label", "应用PASO");
        operatorMap.put("val", "pasoCode");
        Map<String, Object> initLevelMap = new HashMap<>();
        initLevelMap.put("label", "网络基础设备");
        initLevelMap.put("val", "net");
        Map<String, Object> updateLevelMap = new HashMap<>();
        updateLevelMap.put("label", "IP地址");
        updateLevelMap.put("val", "ipAddress");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("label", "监控指标类型");
        map3.put("val", "indexType");
        Map<String, Object> map4 = new HashMap<>();
        map4.put("label", "监控源");
        map4.put("val", "systemCode");
        Map<String, Object> map5 = new HashMap<>();
        map5.put("label", "告警对象");
        map5.put("val", "objectName");
        Map<String, Object> map6 = new HashMap<>();
        map6.put("label", "告警实例");
        map6.put("val", "insName");
        colList.add(IDMap);
        colList.add(timeMap);
        colList.add(operatorMap);
        colList.add(initLevelMap);
        colList.add(updateLevelMap);
        colList.add(map3);
        colList.add(map4);
        colList.add(map5);
        colList.add(map6);
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        result.put("list", list);
        result.put("total", count);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return AjaxResult.success(result);
    }

    @GetMapping("/page")
    public AjaxResult page(@RequestParam("changeTaskNo") String changeTaskNo) {
        //校验changeTaskNo是否存在
        List<Map<String,Object>> changeTaskList = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK,ChangeTaskFieldEnum.EXTRA1.getName(),changeTaskNo);
        if (changeTaskList.isEmpty()) {
            return AjaxResult.warn("任务单号不存在！");
        }
        CustomerFormContent customerFormContent = changeService.initCustomerFormContent("design_biz_shield_warn");
        String formJson = customerFormContent.getDataJson();
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> taskData = changeTaskList.get(0);
        map.put("changeTaskNo", changeTaskNo);
        Object referSys = taskData.get(ChangeTaskFieldEnum.REFER_SYS.getName());
        if(referSys!=null&&!"".equals(referSys.toString().trim())){
            map.put("passCodeArray",new String[]{referSys.toString().trim()});
        }
        Object referInfrastructure = taskData.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
        if(referInfrastructure!=null&&!"".equals(referInfrastructure.toString())){
            String referInfrastructureStr = referInfrastructure.toString();
            referInfrastructureStr = StringUtils.strip(referInfrastructureStr, "[]");
            referInfrastructureStr = referInfrastructureStr.replaceAll("\"", "");
            String[] referInfrastructureArr = referInfrastructureStr.split(",");
            map.put("netArray",referInfrastructureArr);
        }
        String dataJson = VueDataJsonUtil.analysisDataJson(formJson, map);
        customerFormContent.setDataJson(dataJson);
        Map<String,Object> field = customerFormContent.getFields();
        field.putAll(map);
        customerFormContent.setFields(map);
        return AjaxResult.success(customerFormContent);
    }
    /*
     *//**
     * 导出屏蔽告警列表
     *//*
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DesignBizShieldWarn designBizShieldWarn) {
        List<DesignBizShieldWarn> list = designBizShieldWarnService.selectDesignBizShieldWarnList(designBizShieldWarn);
        ExcelUtil<DesignBizShieldWarn> util = new ExcelUtil<DesignBizShieldWarn>(DesignBizShieldWarn.class);
        return util.exportExcel(list, "warn");
    }*/

    /* *//**
     * 新增屏蔽告警
     *//*
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }*/

    /**
     * 新增保存屏蔽告警
     */
    @PostMapping("/add")
    @ApiOperation("新增屏蔽告警")
    public AjaxResult addSave(@RequestBody DesignBizShieldWarn designBizShieldWarn) {
        return designBizShieldWarnService.insertDesignBizShieldWarn(designBizShieldWarn);
    }

    /*   *//**
     * 修改屏蔽告警
     *//*
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        DesignBizShieldWarn designBizShieldWarn = designBizShieldWarnService.selectDesignBizShieldWarnById(id);
        mmap.put("designBizShieldWarn", designBizShieldWarn);
        return prefix + "/edit";
    }*/

    /**
     * 修改保存屏蔽告警
     */
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DesignBizShieldWarn designBizShieldWarn) {
        return toAjax(designBizShieldWarnService.updateDesignBizShieldWarn(designBizShieldWarn));
    }

    @PostMapping("/parse/doc")
    @ApiOperation("解析文档存批量的告警规则")
    public AjaxResult parseDoc(@RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("file") MultipartFile file) {
        //TODO 判断任务单号是否存在
        return designBizShieldWarnService.parseShieldWarnExcel(changeTaskNo, file);
    }

    @GetMapping("/download/template")
    @ApiOperation("下载上传的模板文件")
    public void downLoadDoc(HttpServletResponse response) {
        String templeteName = "告警屏蔽模板";
        List<List<String>> exportable = new ArrayList<>();
        String[] array = {"应用PASO","IP地址","监控指标类型","监控源","告警对象","告警实例"};
        for(String headStr: array){
            List<String> head = new ArrayList<>();
            head.add(headStr);
            exportable.add(head);
        }
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(templeteName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream()).head(exportable).sheet("模板").doWrite(new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除屏蔽告警
     */
    @PostMapping("/remove")
    @ApiOperation("批量删除屏蔽告警，id用,分隔")
    public AjaxResult remove(@RequestParam("ids") String ids) {
        return toAjax(designBizShieldWarnService.deleteDesignBizShieldWarnByIds(ids));
    }
}
