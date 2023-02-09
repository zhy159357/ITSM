package com.ruoyi.form.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.entity.TwTaskEntity;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.service.ITwTaskDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: ruoyi
 * @description: 任务控制层
 * @author: ma zy
 * @date: 2022-11-22 10:27
 **/
@Controller
@RequestMapping("/twTask")
public class TwTaskController extends BaseController {

    @Autowired
    private ITwTaskDbService twTaskDbService;

    private final String prefix = "work";

    @Log(title = "新增任务", businessType = BusinessType.INSERT)
    @PostMapping("/addTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult add(TwTaskEntity twTaskEntity) {
        TwTaskEntity twTaskEntityDo = twTaskDbService.getById(twTaskEntity.getId());
        if(StringUtils.isEmpty(twTaskEntityDo)){
            twTaskEntity.setId(UUID.getUUIDStr());
            twTaskDbService.save(twTaskEntity);
        }else{
            twTaskDbService.updateById(twTaskEntity);
        }

        return success("新增成功成功");
    }
    /**
     * 列表
     *
     * @return chw
     */
    @PostMapping("/getListTask/{workOrderId}")
    @ResponseBody
    public TableDataInfo getUserDbByWorkNodeId(@PathVariable("workOrderId") String workOrderId) {
        startPage();
        List<TwTaskEntity> taskList = twTaskDbService.getList(workOrderId);
        return getDataTable(taskList);
    }
    @GetMapping("/edit/{id}")
    public String editType(@PathVariable("id") String id, ModelMap modelMap) {
        TwTaskEntity twTaskEntity = twTaskDbService.getById(id);
        modelMap.addAttribute("orderId", id);
        modelMap.put("twTaskEntity",twTaskEntity);
        modelMap.put("orderId",id);
        return prefix + "/editWorkEditDialog";
    }

}
