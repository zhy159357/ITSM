package com.ruoyi.activiti.controller.itsm.selfServiceQuery;

import java.util.List;

import com.ruoyi.activiti.service.ISelfServiceQueryService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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
 * @date 2021-11-05
 */
@Controller
@RequestMapping("/self/query")
public class SelfServiceQueryController extends BaseController {
    private String prefix = "selfServiceQuery";

    @Autowired
    private ISelfServiceQueryService selfServiceQueryService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysDeptService iSysDeptService;

    @GetMapping("/myList")
    public String query() {
        return prefix + "/myList";
    }

    @GetMapping("/search")
    public String search(ModelMap mmap) {
        String pId = ShiroUtils.getUserId();
        String orgId = "";
        String isCenter = iSysDeptService.getIsCenter();
        if ("0".equals(isCenter)) {
            String orgCode = iSysDeptService.getpCode(pId);
            if (StringUtils.isNotEmpty(orgCode)) {
                orgId = iSysDeptService.selectDeptByCode(orgCode).getOrgId();
            }
        }
        mmap.put("orgId", orgId);
        return prefix + "/search";
    }

    /**
     * 查询【我的申请】列表
     */
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(SelfServiceQuery selfServiceQuery) {
        SelfServiceQuery ssq = selfServiceQueryService.formatSelf(selfServiceQuery);
        ssq.setCreaterId(ShiroUtils.getUserId());
        startPage();
        List<SelfServiceQuery> list = selfServiceQueryService.selectSelfServiceQueryMyList(ssq);
        return getDataTable(list);
    }

    /**
     * 查询【申请单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SelfServiceQuery selfServiceQuery) {
        if (StringUtils.isEmpty(selfServiceQuery.getCreateOrgId())) {
            String isCenter = iSysDeptService.getIsCenter();
            if ("0".equals(isCenter)) {
                String orgCode = iSysDeptService.getpCode(ShiroUtils.getUserId());
                if (StringUtils.isNotEmpty(orgCode)) {
                    selfServiceQuery.setCreateOrgId(iSysDeptService.selectDeptByCode(orgCode).getOrgId());
                }
            }
        }
        SelfServiceQuery ssq = selfServiceQueryService.formatSelf(selfServiceQuery);
        startPage();
        List<SelfServiceQuery> list = selfServiceQueryService.selectSelfList(ssq);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SelfServiceQuery selfServiceQuery) {
        String isCurrentPage = (String) selfServiceQuery.getParams().get("currentPage");
        if (StringUtils.isEmpty(selfServiceQuery.getCreateOrgId())) {
            String isCenter = iSysDeptService.getIsCenter();
            if ("0".equals(isCenter)) {
                String orgCode = iSysDeptService.getpCode(ShiroUtils.getUserId());
                if (StringUtils.isNotEmpty(orgCode)) {
                    selfServiceQuery.setCreateOrgId(iSysDeptService.selectDeptByCode(orgCode).getOrgId());
                }
            }
        }
        SelfServiceQuery ssq = selfServiceQueryService.formatSelf(selfServiceQuery);

        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<SelfServiceQuery> list = selfServiceQueryService.selectSelfList(ssq);

        ExcelUtil<SelfServiceQuery> util = new ExcelUtil<SelfServiceQuery>(SelfServiceQuery.class);
        return util.exportExcel(list, "自助申请单");
    }

    /**
     * 新增【申请单】页面
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        //打开新建页面生成单号
        String bizType = "ZZCX";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);
        //打开新建页面回显创建机构填报人及创建机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonEvoById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());
        mmap.put("createOrgName", org.getOrgName());
        mmap.put("createOrgId", org.getOrgId());
        mmap.put("loginName", person.getpName());
        mmap.put("loginId", person.getpId());
        mmap.put("contactPhone", person.getMobilPhone());
        mmap.put("reportTime", DateUtils.dateTimeNow());
        return prefix + "/add";
    }

    /**
     *
     */
    @Log(title = "【暂存申请单】", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    @ResponseBody
    @Transactional
    public AjaxResult saveDesc(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryService.saveDesc(selfServiceQuery);
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【提交申请单】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryService.addSave(selfServiceQuery);
    }

    /**
     * 修改【自助申请单】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        SelfServiceQuery selfServiceQuery = selfServiceQueryService.selectSelfServiceQueryById(id);
        if (!"1".equals(selfServiceQuery.getState())) {
            throw new BusinessException("申请单状态不符，刷新列表后操作。");
        }
        mmap.put("selfServiceQuery", selfServiceQuery);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(SelfServiceQuery selfServiceQuery) {
        return toAjax(selfServiceQueryService.updateSelfServiceQuery(selfServiceQuery));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    @Transactional
    public AjaxResult remove(String ids) {
        return toAjax(selfServiceQueryService.deleteSelfServiceQueryByIds(ids));
    }

    @GetMapping("/selectogOrg")
    public String selectogOrg() {
        return prefix + "/subpage/selectOgOrg";
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        SelfServiceQuery selfServiceQuery = selfServiceQueryService.selectSelfServiceQueryById(id);
        mmap.put("selfServiceQuery", selfServiceQuery);
        return prefix + "/view";
    }

    /**
     * q
     * 全国中心自动化处理对应子页面
     *
     * @return
     */
    @GetMapping("/saveToAuto/{id}/{flag}")
    public String saveToAuto(@PathVariable("id") String id, @PathVariable("flag") String flag, ModelMap mmap) {
        mmap.put("flag", flag);
        return selfServiceQueryService.saveToAuto(id, mmap);
    }

    @PostMapping("/completeSelf")
    @ResponseBody
    @Transactional
    public AjaxResult completeSelf(SelfServiceQuery selfServiceQuery) {
        selfServiceQueryService.completeSelf(selfServiceQuery);
        return AjaxResult.success("申请单完成操作成功。");
    }

    /**
     * 修改打开脚本列表
     *
     * @return
     */
    @GetMapping("/saveToAutoEdit/{id}")
    public String saveToAutoEdit(@PathVariable("id") String id, ModelMap mmap) {
        return selfServiceQueryService.saveToAutoEdit(id, mmap);
    }
}
