package com.ruoyi.form.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.entity.TwHistoryEntity;
import com.ruoyi.form.entity.TwPaylistEntity;
import com.ruoyi.form.service.ITwPaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: ruoyi
 * @description: 历史数据查询
 * @author: ma zy
 * @date: 2022-12-05 11:05
 **/
@Controller
@RequestMapping("/twPaylist")
public class TwPayListController extends BaseController {

    private final String prefix = "work/workOrder";
    @Autowired
    private ITwPaylistService iTwPaylistService;


    /**
     * 列表
     *
     * @return chw
     */
    @PostMapping("/getList")
    @ResponseBody
    public TableDataInfo getUserDbByWorkNodeId(TwPaylistEntity twPaylistEntity) {
        startPage();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("work_order_id",twPaylistEntity.getWorkOrderId());
        queryWrapper.eq("type",1);
        List<TwPaylistEntity> twPaylistEntityList = iTwPaylistService.list(queryWrapper);
        return getDataTable(twPaylistEntityList);
    }

    /**
     * 列表
     *
     * @return chw
     */
    @PostMapping("/getListDb")
    @ResponseBody
    public TableDataInfo getListDb(TwPaylistEntity twPaylistEntity) {
        startPage();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("work_order_id",twPaylistEntity.getWorkOrderId());
        queryWrapper.eq("type",twPaylistEntity.getType());
        List<TwPaylistEntity> twPaylistEntityList = iTwPaylistService.list(queryWrapper);
        return getDataTable(twPaylistEntityList);
    }
    /**
     * 编辑
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String editType(@PathVariable("id") String id, ModelMap modelMap) {
        TwPaylistEntity twPaylistEntity = iTwPaylistService.getById(id);
        modelMap.put("twPaylistEntity",twPaylistEntity);
        return prefix + "/historyedit";
    }

    /**
     * 保存
     */
    @Log(title = "新增服务器申请单服务设备", businessType = BusinessType.INSERT)
    @PostMapping("/addOrUpdateSever")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult updateUserSever(TwPaylistEntity twPaylistEntity) {
        twPaylistEntity.setId(UUID.getUUIDStr());
        iTwPaylistService.saveOrUpdate(twPaylistEntity);
        return AjaxResult.success("操作成功", twPaylistEntity.getId());
    }
    /**
     * 编辑
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/historyview/{id}")
    public String viewType(@PathVariable("id") String id, ModelMap modelMap) {
        TwPaylistEntity twPaylistEntity = iTwPaylistService.getById(id);
        modelMap.put("twPaylistEntity",twPaylistEntity);
        return prefix + "/historyview";
    }

}
