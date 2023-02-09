package com.ruoyi.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysWorkService;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程记录Controller
 *
 * @author ruoyi
 * @date 2020-12-25
 */
@Controller
@RequestMapping("/system/log")
public class PubFlowLogController extends BaseController {
    private String prefix = "pubFlowLog";

    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysDeptService iSysDeptService;

    @GetMapping()
    public String log() {
        return prefix + "/log";
    }

    /**
     * 查询流程记录列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubFlowLog pubFlowLog) {
        startPage();
        List<PubFlowLog> list = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        for (PubFlowLog Pub : list) {
            String nextPerson = Pub.getNextPerformerDesc();
            if (StringUtils.isNotEmpty(nextPerson)) {
                String nextPerformerDesc = "";
                String[] nextPersonL = nextPerson.split(":");
                List<Map<String, Object>> reList = new ArrayList<>();
                String performerId = Pub.getPerformerId();
                OgPerson performer = iOgPersonService.selectOgPersonEvoById(performerId);
                if (StringUtils.isNotEmpty(performer.getPhone())) {
                    Pub.setPerformerTel(performer.getPhone());
                } else {
                    Pub.setPerformerTel(performer.getMobilPhone());
                }
                for (String nextPersonm : nextPersonL) {
                    //每个任务
                    String[] nextP = nextPersonm.split(",");
                    for (String pId : nextP) {
                        OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(pId);
                        if (ogPerson == null) {
                            //组
                            OgGroup OgGroup = iSysWorkService.selectOgGroupById(pId);
                            if (OgGroup != null) {
                                nextPerformerDesc += OgGroup.getGrpName() + ",";
                            }
                            //角色
                            OgRole ogRole = iSysRoleService.selectRoleById(pId);
                            if (ogRole != null) {
                                nextPerformerDesc += ogRole.getrName() + ",";
                            }
                            //机构
                            OgOrg OgOrg = iSysDeptService.selectDeptById(pId);
                            if (OgOrg != null) {
                                nextPerformerDesc += OgOrg.getOrgName() + ",";
                            }
                        } else {
                            nextPerformerDesc += ogPerson.getpName() + ",";
                        }
                    }
                    if (!StringUtils.isEmpty(nextPerformerDesc)) {
                        nextPerformerDesc = nextPerformerDesc.substring(0, nextPerformerDesc.length() - 1);
                        nextPerformerDesc += ":";
                    }

                }
                if (!StringUtils.isEmpty(nextPerformerDesc)) {
                    nextPerformerDesc = nextPerformerDesc.substring(0, nextPerformerDesc.length() - 1);
                    Pub.setNextPerformerDesc(nextPerformerDesc);
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出流程记录列表
     */
    @Log(title = "流程记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PubFlowLog pubFlowLog) {
        List<PubFlowLog> list = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        ExcelUtil<PubFlowLog> util = new ExcelUtil<PubFlowLog>(PubFlowLog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 新增流程记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存流程记录
     */
    @Log(title = "流程记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PubFlowLog pubFlowLog) {
        return toAjax(pubFlowLogService.insertPubFlowLog(pubFlowLog));
    }

    /**
     * 修改流程记录
     */
    @GetMapping("/view/{logId}")
    public String edit(@PathVariable("logId") String logId, ModelMap mmap) {
        PubFlowLog pubFlowLog = pubFlowLogService.selectPubFlowLogById(logId);
        String nextPerson = pubFlowLog.getNextPerformerDesc();
        List<Map<String, Object>> reList = new ArrayList<>();
        if (StringUtils.isNotEmpty(nextPerson)) {
            String[] nextPersonL = nextPerson.split(":");
            for (String nextPersonm : nextPersonL) {
                //每个任务
                Map<String, Object> task = new HashMap<>();
                String[] nextP = nextPersonm.split(",");
                List<Map<String, Object>> users = new ArrayList<>();
                for (String pId : nextP) {
                    Map<String, Object> user = new HashMap<>();
                    OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(pId);
                    if (ogPerson == null) {
                        //组
                        OgGroup OgGroup = iSysWorkService.selectOgGroupById(pId);
                        if (OgGroup != null) {
                            user.put("type", "工作组");
                            user.put("name", OgGroup.getGrpName());
                            user.put("phone", "-");
                        }
                        //角色
                        OgRole ogRole = iSysRoleService.selectRoleById(pId);
                        if (ogRole != null) {
                            user.put("type", "角色");
                            user.put("name", ogRole.getrName());
                            user.put("phone", "-");
                        }
                        //机构
                        OgOrg OgOrg = iSysDeptService.selectDeptById(pId);
                        if (OgOrg != null) {
                            user.put("type", "机构");
                            user.put("name", OgOrg.getOrgName());
                            user.put("phone", "-");
                        }
                    } else {
                        user.put("type", "用户");
                        user.put("name", ogPerson.getpName());
                        if (StringUtils.isNotEmpty(ogPerson.getPhone())) {
                            user.put("phone", ogPerson.getPhone());
                        } else {
                            user.put("phone", ogPerson.getMobilPhone());
                        }
                    }
                    users.add(user);
                }
                task.put("task", users);
                reList.add(task);
            }
        }
        if (CollectionUtils.isEmpty(reList)) {
            Map<String, Object> user = new HashMap<>();
            user.put("type", "无");
            user.put("name", "无");
            user.put("phone", "无");
            List<Map<String, Object>> users = new ArrayList<>();
            Map<String, Object> task = new HashMap<>();
            users.add(user);
            task.put("task", users);
            reList.add(task);
        }
        mmap.put("pubFlowLog", pubFlowLog);
        mmap.put("tasklist", reList);
        return prefix + "/pubFlowLogView";
    }

    /**
     * 修改保存流程记录
     */
    @Log(title = "流程记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PubFlowLog pubFlowLog) {
        return toAjax(pubFlowLogService.updatePubFlowLog(pubFlowLog));
    }

    /**
     * 删除流程记录
     */
    @Log(title = "流程记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(pubFlowLogService.deletePubFlowLogByIds(ids));
    }

    /**
     * 查询运维事件单历史流程记录列表
     */
    @PostMapping("/listemp")
    @ResponseBody
    public TableDataInfo listemp(PubFlowLog pubFlowLog) {
        startPage();
        List<PubFlowLog> list = pubFlowLogService.selectPubFlowLogEmpList(pubFlowLog);
        for (PubFlowLog Pub : list) {
            String nextPerson = Pub.getNextPerformerDesc();
            if (StringUtils.isNotEmpty(nextPerson)) {
                String nextPerformerDesc = "";
                String[] nextPersonL = nextPerson.split(":");
                List<Map<String, Object>> reList = new ArrayList<>();
                String performerId = Pub.getPerformerId();
                OgPerson performer = iOgPersonService.selectOgPersonEvoById(performerId);
                if (StringUtils.isNotEmpty(performer.getPhone())) {
                    Pub.setPerformerTel(performer.getPhone());
                } else {
                    Pub.setPerformerTel(performer.getMobilPhone());
                }
                for (String nextPersonm : nextPersonL) {
                    //每个任务
                    String[] nextP = nextPersonm.split(",");
                    for (String pId : nextP) {
                        OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(pId);
                        if (ogPerson == null) {
                            //组
                            OgGroup OgGroup = iSysWorkService.selectOgGroupById(pId);
                            if (OgGroup != null) {
                                nextPerformerDesc += OgGroup.getGrpName() + ",";
                            }
                            //角色
                            OgRole ogRole = iSysRoleService.selectRoleById(pId);
                            if (ogRole != null) {
                                nextPerformerDesc += ogRole.getrName() + ",";
                            }
                            //机构
                            OgOrg OgOrg = iSysDeptService.selectDeptById(pId);
                            if (OgOrg != null) {
                                nextPerformerDesc += OgOrg.getOrgName() + ",";
                            }
                        } else {
                            nextPerformerDesc += ogPerson.getpName() + ",";
                        }
                    }
                    if (!StringUtils.isEmpty(nextPerformerDesc)) {
                        nextPerformerDesc = nextPerformerDesc.substring(0, nextPerformerDesc.length() - 1);
                        nextPerformerDesc += ":";
                    }

                }
                if (!StringUtils.isEmpty(nextPerformerDesc)) {
                    nextPerformerDesc = nextPerformerDesc.substring(0, nextPerformerDesc.length() - 1);
                    Pub.setNextPerformerDesc(nextPerformerDesc);
                }
            }
        }
        return getDataTable(list);
    }
}
