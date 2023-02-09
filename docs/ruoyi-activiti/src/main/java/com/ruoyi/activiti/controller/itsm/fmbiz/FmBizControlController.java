package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.ruoyi.activiti.service.IFmBizControlService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizControl;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fmBizControl")
public class FmBizControlController extends BaseController {
    private String prefix_public = "fmbiz";
    @Autowired
    private IFmBizControlService fmBizControlService;
    @Autowired
    private IFmBizService fmBizService;

    /**
     * 查看事件单监控
     *
     * @return
     */
    @GetMapping("/list")
    public String getfmBizControlAll() {
        return prefix_public + "/controlFmBiz";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizControl fmBizControl) {
        startPage();
        List<FmBizControl> list = fmBizControlService.selectFmBizControlList(fmBizControl);
        return getDataTable(list);
    }

    /**
     * 导出事件单列表
     */
    @Log(title = "业务事件单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmBizControl fmBizControl) throws Exception {
        String isCurrentPage = (String) fmBizControl.getParams().get("currentPage");

        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<FmBizControl> list = fmBizControlService.selectFmBizControlList(fmBizControl);
        ExcelUtil<FmBizControl> util = new ExcelUtil<FmBizControl>(FmBizControl.class);
        return util.exportExcel(list, "业务事件单监控信息");
    }

    /**
     * 通报打开子页面
     *
     * @return
     */
    @GetMapping("/tongbao/{fmId}")
    public String tongbao(@PathVariable("fmId") String fmId, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmBiz", fmBiz);
        return prefix_public + "/tongbao";
    }

    /**
     * 通报事件单
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/savetongbao")
    @ResponseBody
    public AjaxResult savetongbao(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        f.setIfBulletion("1");
        return toAjax(fmBizService.updateFmBiz(f));
    }

    @GetMapping("/sysGrouplist")
    public String selectOneApplication() {
        return prefix_public + "/sys/selectOgSysOne";
    }

}
