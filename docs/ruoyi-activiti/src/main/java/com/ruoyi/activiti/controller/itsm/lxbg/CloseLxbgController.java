package com.ruoyi.activiti.controller.itsm.lxbg;


import com.ruoyi.activiti.service.AddlxbgService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *关闭例行变更计划
 */
@Controller
@RequestMapping("/lxbg/closelxbg")
@Transactional(rollbackFor = Exception.class)
public class CloseLxbgController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CloseLxbgController.class);


    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;


    //关闭例行变更路径
    private String prefix = "lxbg/closelxbg";


    /**
     * 转到关闭例行变更页面
     * @param map
     * @return
     */
    @GetMapping()
    public String addlxbg(ModelMap map)
    {
        map.addAttribute("userId",ShiroUtils.getUserId());

        return prefix + "/closelxbg";
    }


    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizScheduling scheduling)
    {

        scheduling.setCreateTime(handleTimeYYYYMMDDHHMMSS(scheduling.getCreateTime()));
        scheduling.setEndTime(handleTimeYYYYMMDDHHMMSS(scheduling.getEndTime()));

        scheduling.setEffectiveTime(handleTimeYYYYMMDDHHMMSS(scheduling.getEffectiveTime()));
        scheduling.setEndEffTime(handleTimeYYYYMMDDHHMMSS(scheduling.getEndEffTime()));

        startPage();

        List<SmBizScheduling> list = addlxbgService.selectCloseLxbgId(scheduling);
        return getDataTable(list);

    }


    /**
     * 关闭
     *
     * @param
     * @return
     */
    @PostMapping("/closelxbg")
    @ResponseBody
    public AjaxResult closelxbg(String schedulingId,String planStatus) {

        SmBizScheduling smBizScheduling = new SmBizScheduling();
        smBizScheduling.setSchedulingId(schedulingId);
        smBizScheduling.setPlanStatus(planStatus);
        try{
            addlxbgService.updatelxbg(smBizScheduling);
        }catch (Exception e){
            log.error("例行变更计划单关闭失败: "+e.getMessage());
            throw  new BusinessException("例行变更计划单关闭失败,请刷新页面进行重试");
        }
        return AjaxResult.success("操作成功");


    }









}
