package com.ruoyi.activiti.controller.itsm.itsmWorkStatus;

import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.domain.ItsmWorkStatus;
import com.ruoyi.activiti.domain.ItsmWorkStatusLog;
import com.ruoyi.activiti.service.IOgPersonDutyService;
import com.ruoyi.activiti.service.ItsmWorkStatusLogService;
import com.ruoyi.activiti.service.ItsmWorkStatusService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.activiti.constants.WorkStatusConstants;


@Controller
@RequestMapping("/system/status")
public class ItsmWorkStatusController extends BaseController {


    private String prefix = "system/status";

    @Autowired
    private ItsmWorkStatusService itsmWorkStatusService;

    @Autowired
    private IOgPersonDutyService dutyService;

    @Autowired
    private ItsmWorkStatusLogService itsmWorkStatusLogService;

    @GetMapping()
    public String status()
    {
        return prefix + "/status";
    }

    /**
     * 查询
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ItsmWorkStatus itsmWorkStatus)
    {
        startPage();
        List<ItsmWorkStatus> list = itsmWorkStatusService.selectItsmWorkStatusList(itsmWorkStatus);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ItsmWorkStatus itsmWorkStatus)
    {
        List<ItsmWorkStatus> list = itsmWorkStatusService.selectItsmWorkStatusList(itsmWorkStatus);
        ExcelUtil<ItsmWorkStatus> util = new ExcelUtil<ItsmWorkStatus>(ItsmWorkStatus.class);
        return util.exportExcel(list, "status");
    }

    /**
     * 新增页面
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 状态保存
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ItsmWorkStatus itsmWorkStatus)
    {
        //获取当前登录人id
        String pid = ShiroUtils.getOgUser().getpId();

        //签到
        if("1".equals(itsmWorkStatus.getLabel())){
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.qdzt);
        }else if("2".equals(itsmWorkStatus.getLabel())){//签退
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.qtzt);
        }else if("3".equals(itsmWorkStatus.getLabel())){//午休
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.wxzt);
        }else if("4".equals(itsmWorkStatus.getLabel())){//午休结束
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.wxjszt);
        }else if("5".equals(itsmWorkStatus.getLabel())){//小休
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.xxzt);
        }else if("6".equals(itsmWorkStatus.getLabel())){//小休结束
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.xxjszt);
        }else if("7".equals(itsmWorkStatus.getLabel())){//离线
            itsmWorkStatus.setWorkStatus(WorkStatusConstants.lxzt);
        }

        itsmWorkStatus.setPid(pid);
        itsmWorkStatus.setUserId(pid);

        String message = "操作成功";

        //查询工作表信息
        ItsmWorkStatus workStatus = itsmWorkStatusService.selectItsmWorkStatusByPid(pid);
        if(workStatus !=null){
            if(StringUtils.isNotEmpty(workStatus.getPid())){
                //当前登录人的id在表中是否存在   存在更新状态，不存在新增
                if(workStatus.getPid().contains(pid)){
                    if(itsmWorkStatus.getWorkStatus().equals(workStatus.getWorkStatus())){
                        return AjaxResult.error("选择状态与当前状态重复！");
                    }
                    itsmWorkStatus.setOperateTime(DateUtils.getTime());

                    // 此处判断签退时当前登录人所在值班组人数，若该操作人名下有监控事件单任务需要转发
//                    if(WorkStatusConstants.qtzt.equals(itsmWorkStatus.getWorkStatus())) {
//                        Map map = dutyService.checkDutyPerson(workStatus.getUserId());
//                        if(map.containsKey("flag") && (boolean)map.get("flag")){
//                            message = (String)map.get("message");
//                            return AjaxResult.error(message);
//                        }
//                    }

                    itsmWorkStatusService.updateItsmWorkStatus(itsmWorkStatus);
                }else {
                    String workId = UUID.getUUIDStr();
                    itsmWorkStatus.setWorkId(workId);
                    itsmWorkStatus.setOperateTime(DateUtils.getTime());
                    itsmWorkStatusService.insertItsmWorkStatus(itsmWorkStatus);
                }
            }
        }else {
            String workId = UUID.getUUIDStr();
            itsmWorkStatus.setWorkId(workId);
            itsmWorkStatus.setOperateTime(DateUtils.getTime());
            itsmWorkStatusService.insertItsmWorkStatus(itsmWorkStatus);
        }
        // 工作状态记录表添加数据
        ItsmWorkStatusLog workStatusLog = new ItsmWorkStatusLog();
        workStatusLog.setLogId(UUID.getUUIDStr());
        workStatusLog.setPid(itsmWorkStatus.getPid());
        workStatusLog.setUserId(itsmWorkStatus.getUserId());
        workStatusLog.setOperateTime(itsmWorkStatus.getOperateTime());
        workStatusLog.setWorkId(itsmWorkStatus.getWorkId());
        workStatusLog.setWorkStatus(itsmWorkStatus.getWorkStatus());
        itsmWorkStatusLogService.insertItsmWorkStatusLog(workStatusLog);


        return AjaxResult.success(message);
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{workId}")
    public String edit(@PathVariable("workId") String workId, ModelMap mmap)
    {
        ItsmWorkStatus itsmWorkStatus = itsmWorkStatusService.selectItsmWorkStatusByPid(workId);
        mmap.put("itsmWorkStatus", itsmWorkStatus);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ItsmWorkStatus itsmWorkStatus)
    {
        return toAjax(itsmWorkStatusService.updateItsmWorkStatus(itsmWorkStatus));
    }

    /**
     * 删除
     */
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(itsmWorkStatusService.deleteItsmWorkStatusByIds(ids));
    }


}
