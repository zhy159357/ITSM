package com.ruoyi.activiti.controller.itsm.collectingForms;

import com.ruoyi.activiti.service.CollectingFormsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CollectingFormsInspection;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@Controller
@RequestMapping("/collecting/forms")
public class CollectingFormsController extends BaseController {
    /**
     * 报表管理路径前缀
     */
    private String prefix_public = "collectionForms";

    @Autowired
    private CollectingFormsService collectingFormsService;

    /**
     * 报表管理-采集巡检记录单页面打开
     *
     * @return
     */
    @GetMapping("collection")
    public String publicApplyPage() {
        return prefix_public + "/collection";
    }

    /**
     * 查询采集巡检记录单列表
     */
    @PostMapping("/List")
    @ResponseBody
    public TableDataInfo collectingList(CollectingFormsInspection cfi) throws ParseException {
        startPage();
        List<CollectingFormsInspection> list = collectingFormsService.selectCollectingList(cfi);
        return getDataTable(list);
    }

    /**
     * 打开新增日常巡检记录单页面
     *
     * @return
     */
    @GetMapping("/add")
    public String CollectingFormsPage() {
        return prefix_public + "/add";
    }

    /**
     * 保存日常巡检记录单
     *
     * @param collectingFormsInspection
     * @return
     */
    @PostMapping("/saveCollectingData")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveInspectionData1(CollectingFormsInspection collectingFormsInspection) {
        try {
            String id = collectingFormsInspection.getId();
            if (StringUtils.isEmpty(id)) {
                //创建ID
                collectingFormsInspection.setId(UUID.getUUIDStr());
                collectingFormsService.insertCollecting(collectingFormsInspection);
            } else {
                collectingFormsService.updateCollecting(collectingFormsInspection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("采集巡检记录单操作失败，请刷新页面后重新尝试。");
        }
        return success("采集巡检记录单提交成功。");


    }

    /**
     * 删除
     */
    @Log(title = "根据id删除采集巡检记录", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(String id) {
        return toAjax(collectingFormsService.deleteCollectingById(id));
    }

    /**
     * 打开修改页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        CollectingFormsInspection collectingFormsInspection = collectingFormsService.selectCollectingById(id);
        mmap.put("collectingFormsInspection", collectingFormsInspection);
        String url = prefix_public + "/edit";
        return url;
    }


    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        CollectingFormsInspection collectingFormsInspection = collectingFormsService.selectCollectingById(id);
        mmap.put("collectingFormsInspection", collectingFormsInspection);
        return prefix_public + "/detail";
    }



}
