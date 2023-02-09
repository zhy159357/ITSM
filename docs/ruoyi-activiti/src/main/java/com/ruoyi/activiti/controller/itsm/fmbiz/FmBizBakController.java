package com.ruoyi.activiti.controller.itsm.fmbiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.service.IFmBizBakService;
import com.ruoyi.activiti.service.IFmKindService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
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
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2021-10-14
 */
@Controller
@RequestMapping("/system/bak")
public class FmBizBakController extends BaseController {
    private String prefix = "system/bak";
    private String prefix_public = "fmbiz";

    @Autowired
    private IFmBizBakService fmBizBakService;
    @Autowired
    private IFmKindService iFmKindService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private ISysWorkService iSysWorkService;

    @GetMapping("/fmbizbak")
    public String publicApplyPage() {
        return prefix_public + "/fmbizbak";
    }

    @GetMapping()
    public String bak() {
        return prefix + "/bak";
    }

    /**
     * 查询事件单历史列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizBak fmBizBak) {
        fmBizBak = fmBizBakService.formatFmbizBak(fmBizBak);
        if (fmBizBak.getParams().containsKey("ifAuto")) {
            fmBizBak.getParams().put("ifAuto", "1");
        }
        startPage();
        List<FmBizBak> list = fmBizBakService.selectFmBizBakListPager(fmBizBak);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmBizBak fmBizBak) {
        String isCurrentPage = (String) fmBizBak.getParams().get("currentPage");
        fmBizBak = fmBizBakService.formatFmbizBak(fmBizBak);
        if (fmBizBak.getParams().containsKey("ifAuto")) {
            fmBizBak.getParams().put("ifAuto", "1");
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<FmBizBak> list = fmBizBakService.selectFmBizBakListPager(fmBizBak);
        ExcelUtil<FmBizBak> util = new ExcelUtil<FmBizBak>(FmBizBak.class);
        return util.exportExcel(list, "运维事件单");
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
    public AjaxResult addSave(FmBizBak fmBizBak) {
        return toAjax(fmBizBakService.insertFmBizBak(fmBizBak));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{fmId}")
    public String edit(@PathVariable("fmId") String fmId, ModelMap mmap) {
        FmBizBak fmBizBak = fmBizBakService.selectFmBizBakById(fmId);
        mmap.put("fmBizBak", fmBizBak);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @RequiresPermissions("system:bak:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmBizBak fmBizBak) {
        return toAjax(fmBizBakService.updateFmBizBak(fmBizBak));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:bak:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmBizBakService.deleteFmBizBakByIds(ids));
    }

    /**
     * 根据系统ID查询历史分类信息
     *
     * @param sysid
     * @return
     */
    @PostMapping("/selectFmKindBySys")
    @ResponseBody
    public TableDataInfo selectFmKindBySys(String sysid) {
        List<FmKind> list = iFmKindService.selectFmKindBySysid(sysid);
        return getDataTable(list);
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        FmBizBak fmBizBak = fmBizBakService.selectFmBizBakById(id);
        Map<String, Object> reMap = getReMap(fmBizBak);
        fmBizBak.setParams(reMap);
        mmap.put("FmBizBak", fmBizBak);
        return "fmbiz/bakdetail";
    }

    public Map<String, Object> getReMap(FmBizBak fmBizBak) {
        Map<String, Object> reMap = new HashMap<>();
        if (StringUtils.isNotEmpty(fmBizBak.getCreateOrgId())) {
            OgOrg org = iSysDeptService.selectDeptById(fmBizBak.getCreateOrgId());
            if (!ObjectUtils.isEmpty(org)) {
                reMap.put("createOrg", org.getOrgName());//创建机构
            } else {
                reMap.put("createOrg", "");
            }
        } else {
            reMap.put("createOrg", "");
        }
        if (StringUtils.isNotEmpty(fmBizBak.getSysid())) {
            OgSys sys = iSysApplicationManagerService.selectOgSysBySysId(fmBizBak.getSysid());
            if (!ObjectUtils.isEmpty(sys)) {
                reMap.put("sysName", sys.getCaption());//所属系统
            } else {
                reMap.put("sysName", "");
            }
        } else {
            reMap.put("sysName", "");
        }

        if (StringUtils.isNotEmpty(fmBizBak.getGroupId())) {
            OgGroup group = iSysWorkService.selectOgGroupById(fmBizBak.getGroupId());
            if (!ObjectUtils.isEmpty(group)) {
                reMap.put("groupName", group.getGrpName());//所属工作组
            } else {
                reMap.put("groupName", "");
            }
        } else {
            reMap.put("groupName", "");
        }
        if (StringUtils.isNotEmpty(fmBizBak.getDealerId())) {
            OgPerson person = iOgPersonService.selectOgPersonEvoById(fmBizBak.getDealerId());
            if (!ObjectUtils.isEmpty(person)) {
                reMap.put("dealName", person.getpName());// 处理人
            } else {
                reMap.put("dealName", "");
            }
        } else {
            reMap.put("dealName", "");
        }
        if (StringUtils.isNotEmpty(fmBizBak.getEvaluaterId())) {
            OgPerson person2 = iOgPersonService.selectOgPersonEvoById(fmBizBak.getEvaluaterId());
            if (!ObjectUtils.isEmpty(person2)) {
                reMap.put("evaluaterName", person2.getpName());//评价人
            } else {
                reMap.put("evaluaterName", "");
            }
        } else {
            reMap.put("evaluaterName", "");
        }
        if (!ObjectUtils.isEmpty(fmBizBak.getFmKind())) {
            reMap.put("fmKindName", fmBizBak.getFmKind().getName());//评价人
        } else {
            reMap.put("fmKindName", "");
        }
        return reMap;
    }
}
