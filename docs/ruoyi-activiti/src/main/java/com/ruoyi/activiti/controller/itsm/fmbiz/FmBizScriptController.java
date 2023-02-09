package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.service.IFmBizScriptService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.ISelfServiceQueryService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizScript;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.SelfServiceQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IEntegorServer;
import com.ruoyi.system.service.ISysApplicationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Controller
 *
 * @author liul
 * @date 2021-03-30
 */
@Controller
@RequestMapping("/system/script")
public class FmBizScriptController extends BaseController {
    private String prefix = "system/script";

    @Autowired
    private IFmBizScriptService fmBizScriptService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    private IEntegorServer entegorServer;
    @Autowired
    private ISelfServiceQueryService iSelfServiceQueryService;

    @GetMapping()
    public String script() {
        return prefix + "/script";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizScript fmBizScript) {
        startPage();
        List<FmBizScript> list = fmBizScriptService.selectFmBizScriptList(fmBizScript);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmBizScript fmBizScript) {
        List<FmBizScript> list = fmBizScriptService.selectFmBizScriptList(fmBizScript);
        ExcelUtil<FmBizScript> util = new ExcelUtil<FmBizScript>(FmBizScript.class);
        return util.exportExcel(list, "script");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmBizScript fmBizScript) {
        return toAjax(fmBizScriptService.insertFmBizScript(fmBizScript));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{fbsId}")
    public String edit(@PathVariable("fbsId") String fbsId, ModelMap mmap) {
        FmBizScript fmBizScript = fmBizScriptService.selectFmBizScriptById(fbsId);
        mmap.put("fmBizScript", fmBizScript);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmBizScript fmBizScript) {
        return toAjax(fmBizScriptService.updateFmBizScript(fmBizScript));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmBizScriptService.deleteFmBizScriptByIds(ids));
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        FmBizScript fbs = fmBizScriptService.selectFmBizScriptById(id);
        fbs = fmBizScriptService.getAutoResult(fbs);
        mmap.put("FmBizScript", fbs);
        return "fmbiz/flow/subpage/autoView";
    }

    /**
     * 查询系统对应脚本列表
     */
    @PostMapping("/scirptList")
    @ResponseBody
    public TableDataInfo scirptList(FmBiz fmBiz) {
        List<Map<String, String>> list = new ArrayList();
        String sysId = fmBiz.getSysid();
        if (StringUtils.isNotEmpty(sysId)) {
            OgSys sys = sysApplicationManagerService.selectOgSysBySysId(sysId);
            if (sys != null) {
                list = entegorServer.getCommonTaskList(sys.getCaption());
            }
        }
        return getDataTable_ideal(list);
    }

    /**
     * 查询脚本Id查询对应参数列表
     */
    @PostMapping("/paraList")
    @ResponseBody
    public TableDataInfo paraList(FmBizScript fmBizScript) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<FmBizScript> list2 = new ArrayList<>();
        if (StringUtils.isNotEmpty(fmBizScript.getScriptId())) {
            list = entegorServer.getCommonTaskInfo(fmBizScript.getScriptId());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    FmBizScript fbs = new FmBizScript();
                    fbs.setParams(list.get(i));
                    fbs.getParams().put("value", null);
                    list2.add(fbs);
                }
            }
        }
        return getDataTable(list2);
    }

    //执行脚本
    @PostMapping("/executorScript")
    @ResponseBody
    public AjaxResult executorScript(@RequestBody List<Map<String, Object>> listMap) {
        //保存脚本執行信息到执行记录表中
        FmBizScript fbs = new FmBizScript();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        Map<String, Object> map2 = new HashMap<>();
        String startTaskJsonKey = "";
        String fmId = "";
        String scriptName = "";
        String scriptVersion = "";
        String commonTaskId = "0";
        //组装json串传入脚本服务化执行脚本
        if (!listMap.isEmpty()) {
            for (int i = 0; i < listMap.size(); i++) {
                if (listMap.get(i).containsKey("params")) {
                    map2 = (Map<String, Object>) listMap.get(i).get("params");
                    if (!map2.isEmpty()) {
                        for (int y = 0; y < map2.size(); y++) {
                            jsonObject3.put(map2.get("iid").toString(), map2.get("value"));
                            if (map2.containsKey("startTaskJsonKey") && StringUtils.isNotEmpty(map2.get("startTaskJsonKey").toString())) {
                                startTaskJsonKey = (String) map2.get("startTaskJsonKey");
                            }
                        }
                    }
                }
                if (listMap.get(i).containsKey("scriptId")) {
                    commonTaskId = (String) listMap.get(i).get("scriptId");
                }
                if (listMap.get(i).containsKey("scriptName")) {
                    scriptName = (String) listMap.get(i).get("scriptName");
                }
                if (listMap.get(i).containsKey("fmId")) {
                    fmId = (String) listMap.get(i).get("fmId");
                    if (StringUtils.isNotEmpty(fmId)) {
                        fbs.setType("1");
                        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
                        fbs.setExecutorGroupid(fmBiz.getGroupId());
                        fbs.setExecutorId(fmBiz.getDealerId());
                    }
                } else if (listMap.get(i).containsKey("id")) {
                    fmId = (String) listMap.get(i).get("id");
                    fbs.setType("2");
                    SelfServiceQuery selfServiceQuery = iSelfServiceQueryService.selectSelfServiceQueryById(fmId);
                    fbs.setExecutorId(selfServiceQuery.getCreaterId());
                }
                if (listMap.get(i).containsKey("scriptVersion")) {
                    scriptVersion = (String) listMap.get(i).get("scriptVersion");
                }
            }
            jsonObject2.put("execUser", "");
            jsonObject2.put("agentIds", "");
            jsonObject2.put("scriptParams", jsonObject3);//脚本參數
            jsonObject.put("commonTaskId", Integer.parseInt(commonTaskId));
            jsonObject.put("taskName", scriptName);
            jsonObject.put("startType", 0);
            jsonObject.put(startTaskJsonKey, jsonObject2);
            //保存脚本執行信息到执行记录表中
            fbs.setFbsId(UUID.getUUIDStr());
            fbs.setFmId(fmId);
            fbs.setExecutorTime(DateUtils.dateTimeNow());
            fbs.setScriptId(commonTaskId + "");
            fbs.setScriptName(scriptName);
            fbs.setScriptVersion(scriptVersion);
            fbs.setResultStatus("运行中");
            fbs.setScriptPara(jsonObject3.toJSONString());
            fmBizScriptService.insertFmBizScript(fbs);

            Map<String, Object> mmap = entegorServer.startCommonTaskByConfig(jsonObject.toString());
            if (!mmap.isEmpty()) {
                fbs.setFlowId(mmap.get("content").toString());
                fmBizScriptService.updateFmBizScript(fbs);
            }
        }
        return AjaxResult.success("执行脚本成功！", fbs.getFbsId());
    }

    //根据脚本名称获取脚本列表(申请单专用)
    @PostMapping("/getScriptList")
    @ResponseBody
    public TableDataInfo getScriptList(SelfServiceQuery selfServiceQuery) {
        List<Map<String, String>> list = new ArrayList();
        list = entegorServer.getCommonTaskListByQuery((String) selfServiceQuery.getParams().get("taskName"));
        return getDataTable_ideal(list);
    }
}
