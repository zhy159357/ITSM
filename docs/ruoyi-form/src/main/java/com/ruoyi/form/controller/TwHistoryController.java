package com.ruoyi.form.controller;

import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.entity.TwHistoryEntity;
import com.ruoyi.form.entity.TwTaskEntity;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkOrder;
import com.ruoyi.form.service.ITwHistoryService;
import org.aspectj.lang.annotation.After;
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
@RequestMapping("/twHistory")
public class TwHistoryController extends BaseController {

    private final String prefix = "work/history";
    @Autowired
    private ITwHistoryService iTwHistoryService;


    @GetMapping("/list")
    public String list(ModelMap mmap) {
        return prefix + "/historylist";
    }


    /**
     * 列表
     *
     * @return chw
     */
    @PostMapping("/getList")
    @ResponseBody
    public TableDataInfo getUserDbByWorkNodeId(TwHistoryEntity twHistoryEntity) {
        startPage();
        List<TwHistoryEntity> twHistoryEntities = iTwHistoryService.list();
        return getDataTable(twHistoryEntities);
    }

    /**
     * 系统选择页面
     *
     * @return
     */
    @GetMapping("/selectOneApplication/{flag}")
    public String selectOneApplication(@PathVariable("flag")String flag,ModelMap map) {
        map.put("flag",flag);
        return prefix + "/selectOneApplication";
    }

    /**
     * 编辑
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String editType(@PathVariable("id") String id, ModelMap modelMap) {
        TwHistoryEntity twHistoryEntity = iTwHistoryService.getById(id);
        modelMap.put("twHistoryEntity",twHistoryEntity);
        return prefix + "/historyedit";
    }

    /**
     * 保存
     */
    @Log(title = "新增服务器申请单服务设备", businessType = BusinessType.INSERT)
    @PostMapping("/updateHistory")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult updateUserSever(TwHistoryEntity twHistoryEntity) {

        iTwHistoryService.saveOrUpdate(twHistoryEntity);
        return AjaxResult.success("操作成功", twHistoryEntity.getId());
    }
    /**
     * 编辑
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/historyview/{id}")
    public String viewType(@PathVariable("id") String id, ModelMap modelMap) {
        TwHistoryEntity twHistoryEntity = iTwHistoryService.getById(id);
        modelMap.put("twHistoryEntity",twHistoryEntity);
        return prefix + "/historyview";
    }


    /**
     * 导出监控事件单列表
     */
    @Log(title = "监控事件单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export()
    {
        List<TwHistoryEntity> twHistoryEntityList = iTwHistoryService.list();
        ExcelUtil<TwHistoryEntity> util = new ExcelUtil<TwHistoryEntity>(TwHistoryEntity.class);
        return util.exportExcel(twHistoryEntityList, "历史数据");
    }

    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return prefix + "/historyadd";
    }

    /**
     * 新增保存
     */
    @Log(title = "新增环境管理", businessType = BusinessType.INSERT)
    @PostMapping("/saveAdd")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(TwHistoryEntity twHistoryEntity) {
        iTwHistoryService.saveOrUpdate(twHistoryEntity);
        return success("新增成功");
    }
    /**
     * 删除
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult delete(String ids) {
        iTwHistoryService.removeById(ids);
        return success("删除成功！");
    }

}
