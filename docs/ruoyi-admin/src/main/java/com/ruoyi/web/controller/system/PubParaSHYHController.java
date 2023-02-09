package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PriorityParavalue;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.domain.entity.SystemManagementing;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.domain.PubParaSHYH;
import com.ruoyi.form.service.ForeignService;
import com.ruoyi.form.service.IPubParaSHYHService;
import com.ruoyi.system.mapper.PriorityParaValueMapper;
import com.ruoyi.system.service.IPubParaValueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/dict")
public class PubParaSHYHController extends BaseController {

    private String prefix = "system/dict/type";
    /*优先级页面前缀*/
    private static final String priorityPrefix="system/priority";
    @Resource
    private IPubParaSHYHService iPubParaSHYHService;


    @Resource
    private IPubParaValueService pubParaValueService;


    @Resource
    private PriorityParaValueMapper priorityParaValueMapper;



    @GetMapping()
    public String dictType() {
        return prefix + "/type";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubParaSHYH pubPara) {
        startPage();
        List<PubParaSHYH> list = iPubParaSHYHService.selectPubParaList(pubPara);
        return getDataTable(list);
    }

    /**
     * 自定义表单配置数据源类型
     *
     * @param pubPara
     * @return
     */
    @PostMapping("/listCustomerForm")
    @ResponseBody
    public TableDataInfo listCustomerForm(PubParaSHYH pubPara) {
        startPage();
        List<PubParaSHYH> list = iPubParaSHYHService.selectPubParaCustomerList(pubPara);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PubParaSHYH pubPara) {
        if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(iPubParaSHYHService.checkParaNameUnique(pubPara))) {
            return error("新增参数项'" + pubPara.getParaName() + "'失败，参数项代码已存在");
        }
        pubPara.setParaId(UUID.getUUIDStr());
        pubPara.setState("1");
        return toAjax(iPubParaSHYHService.insertPubPara(pubPara));
    }

    /**
     * 修改参数页面
     *
     * @param paraId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") String paraId, ModelMap mmap) {
        mmap.put("pubPara", iPubParaSHYHService.selectPubParaById(paraId));
        return prefix + "/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult addEdit(PubParaSHYH pubPara) {
        return toAjax(iPubParaSHYHService.updatePubParaById(pubPara));
    }


    /**
     * jf  2022.6.15
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SystemManagementing> util = new ExcelUtil<SystemManagementing>(SystemManagementing.class);
        List<SystemManagementing> userList = null;
        try {
            userList = util.importExcel(file.getInputStream());


        } catch (Exception e) {
            return AjaxResult.warn("导入数据有误！");
        }

        String operName = ShiroUtils.getSysUser().getLoginName();


        iPubParaSHYHService.importData(userList);
        // String message = dutySchedulingService.importScheduling(userList, operName);
        return AjaxResult.success();


    }


    /**
     * 查询优先级列表
     * @param code
     * @return
     */
    @GetMapping("/priorityData/{code}")
    @ResponseBody
    public TableDataInfo priorityData(@PathVariable("code") String code) {
        startPage();
        List<PriorityParavalue> priorityParavalueList = iPubParaSHYHService.priorityData(code);
        return getDataTable(priorityParavalueList);
    }



    /**
     *  进入编辑优先级页面
     * @param id 条目ID
     * @param mmap  Model对象
     * @return
     */
    @GetMapping("/edit/priority/{id}")
    public String editPriority(@PathVariable String id,ModelMap mmap){
        PriorityParavalue entity=priorityParaValueMapper.selectOneById(id);
        Map<String, String> stringMap = iPubParaSHYHService.selectOneById(id);
        entity.setPubParavalueIdDegree(stringMap.get("degreeValueDetail"));
        entity.setPubParavalueIdScope(stringMap.get("scopeValueDetail"));
        entity.setPriority(stringMap.get("priority"));

        mmap.put("priorityInfo",entity);
        return priorityPrefix + "/edit";
    }

    /**
     * 编辑优先级接口
     * @param priorityParavalue
     * @return
     */
    @PostMapping("/edit/priority")
    @ResponseBody
    public AjaxResult editPriority(PriorityParavalue priorityParavalue){
        priorityParaValueMapper.updatePriorityParavalue(priorityParavalue);
        return AjaxResult.success();
    }

    /**
     * 进入优先级列表页面
     * @return
     */
    @GetMapping("/priorityList")
    public String demoHtml() {
        return priorityPrefix + "/priority";
    }

    /**
     * 根据影响程度和影响范围查询时间优先级
     * @param infLevel 影响程度
     * @param infRange 影响范围
     * @return
     */
    @PostMapping("/selectPriorityParavalue")
    @ResponseBody
    public AjaxResult selectPriorityParavalue(String infLevel, String infRange) {
        // 将影响程度和影响范围传递来的码值转换成数据库所需要的id
        List<PubParaValue> impactDegreeList = pubParaValueService.selectPubParaValueByParaName("event_impact_degree");
        for(PubParaValue value : impactDegreeList) {
            if(infLevel.equals(value.getValue())) {
                infLevel = value.getParaValueId();
            }
        }
        List<PubParaValue> impactScopeList = pubParaValueService.selectPubParaValueByParaName("event_impact_scope");
        for(PubParaValue value : impactScopeList) {
            if(infRange.equals(value.getValue())) {
                infRange = value.getParaValueId();
            }
        }
        PriorityParavalue priorityParavalue = new PriorityParavalue();
        priorityParavalue.setPubParavalueIdDegree(infLevel);
        priorityParavalue.setPubParavalueIdScope(infRange);
        String priority = iPubParaSHYHService.selectPriorityParavalue(priorityParavalue);
        return AjaxResult.success(priority);
    }

    /**
     * 根据影响程度和影响范围查询时间优先级
     * @param  map infLevel 影响程度 infRange 影响范围
     * @return
     */
    @PostMapping("/selectPriorityParavalueVue")
    @ResponseBody
    public AjaxResult selectPriorityParavalueVue(@RequestBody Map<String, String> map) {
        String infLevel = map.get("infLevel");
        String infRange = map.get("infRange");
        if(StringUtils.isEmpty(infLevel) || StringUtils.isEmpty(infRange)) {
            return AjaxResult.success(new ArrayList<>());
        }
        // 将影响程度和影响范围传递来的码值转换成数据库所需要的id
        List<PubParaValue> impactDegreeList = pubParaValueService.selectPubParaValueByParaName("event_impact_degree");
        for(PubParaValue value : impactDegreeList) {
            if(infLevel.equals(value.getValue())) {
                infLevel = value.getParaValueId();
            }
        }
        List<PubParaValue> impactScopeList = pubParaValueService.selectPubParaValueByParaName("event_impact_scope");
        for(PubParaValue value : impactScopeList) {
            if(infRange.equals(value.getValue())) {
                infRange = value.getParaValueId();
            }
        }
        PriorityParavalue priorityParavalue = new PriorityParavalue();
        priorityParavalue.setPubParavalueIdDegree(infLevel);
        priorityParavalue.setPubParavalueIdScope(infRange);
        String priority = iPubParaSHYHService.selectPriorityParavalue(priorityParavalue);
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        result.put("value", priority);
        result.put("label", priority);
        resultList.add(result);
        return AjaxResult.success(resultList);
    }

    @PostMapping("/selectEventLevelParavalueVue")
    @ResponseBody
    public AjaxResult selectEventLevelParavalueVue(@RequestBody Map<String, String> map) {
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        String infLevel = map.get("infFace");
        // 事件影响面的值为7（潜在隐患的生产故障事件及服务请求），则赋值事件单级别为六级，其他为五级
        String level = "";
        if("7".equals(infLevel)) {
            level = "六级";
        } else {
            level = "五级";
        }
        result.put("value", level);
        result.put("label", level);
        resultList.add(result);
        return AjaxResult.success(resultList);
    }
    

 	@Autowired
 	private ForeignService foreignService;
 	
     @PostMapping("/ittablesCol/{tableName}")
     @ResponseBody
     public AjaxResult ittablesCol(@PathVariable String tableName){
     	List<Map<String, Object>> paramMap = foreignService.ittablesCol(tableName);
     	return AjaxResult.success(paramMap);
     }
     
     @PostMapping("/ittablesCols")
     @ResponseBody
     public AjaxResult ittablesCols(@RequestBody String tableName)
     {
     	List<Map<String, Object>> paramMap = foreignService.ittablesCols(tableName);
     	return AjaxResult.success(paramMap);
     }
}
