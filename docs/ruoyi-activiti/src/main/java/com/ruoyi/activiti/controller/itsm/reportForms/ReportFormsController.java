package com.ruoyi.activiti.controller.itsm.reportForms;

import com.ruoyi.activiti.service.ReportFormsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FormsInspection;
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
@RequestMapping("/report/forms")
public class ReportFormsController extends BaseController {
    /**
     * 报表管理路径前缀
     */
    private String prefix_public = "reportForms";

    @Autowired
    private ReportFormsService reportFormsService;

    /**
     * 报表管理-日常巡检记录单页面打开
     *
     * @return
     */
    @GetMapping("/inspection")
    public String publicApplyPage() {
        return prefix_public + "/inspection/inspection";
    }

    /**
     * 查询日常巡检记录单列表
     */
    @PostMapping("/inspectionList")
    @ResponseBody
    public TableDataInfo inspectionList(FormsInspection formsInspection) throws ParseException {
        startPage();
        List<FormsInspection> list = reportFormsService.selectInspectionList(formsInspection);
        return getDataTable(list);
    }

    /**
      * 打开新增日常巡检记录单页面
      *
      * @return
     */
    @GetMapping("/add")
    public String FmBizPage() {
        return prefix_public + "/inspection/add";
    }

    /**
     * 保存日常巡检记录单
     *
     * @param formsInspection
     * @return
     */
    @PostMapping("/saveInspectionData")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveInspectionData(FormsInspection formsInspection) {
        try {
            String id = formsInspection.getId();
            if (StringUtils.isEmpty(id)) {
                //创建ID
                formsInspection.setId(UUID.getUUIDStr());
                reportFormsService.insertInspection(formsInspection);
            } else {
                reportFormsService.updateInspection(formsInspection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("日常巡检记录单操作失败，请刷新页面后重新尝试。");
        }
        return success("日常巡检记录单提交成功。");


    }

    /**
     * 删除
     */
    @Log(title = "根据id删除日常巡检记录", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(String id) {
        return toAjax(reportFormsService.deleteInspectionById(id));
    }

    /**
     * 打开修改页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        FormsInspection formsInspection = reportFormsService.selectFormsById(id);
        mmap.put("formsInspection", formsInspection);
        String url = prefix_public + "/inspection/edit";
        return url;
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        FormsInspection formsInspection = reportFormsService.selectFormsById(id);
        mmap.put("formsInspection", formsInspection);
        return prefix_public + "/inspection/detail";
    }

}
