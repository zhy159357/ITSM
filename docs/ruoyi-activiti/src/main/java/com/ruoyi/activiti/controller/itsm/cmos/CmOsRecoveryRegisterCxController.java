package com.ruoyi.activiti.controller.itsm.cmos;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.CmOsRecoveryRegister;
import com.ruoyi.activiti.service.ICmOsRecoveryRegisterService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【操作系统恢复登记】Controller
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
@Controller
@RequestMapping("cmos/cx")
public class CmOsRecoveryRegisterCxController extends BaseController
{
    private String prefix = "cmos/cx";

    @Autowired
    private ICmOsRecoveryRegisterService cmOsRecoveryRegisterService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @GetMapping()
    public String register()
    {
        return prefix + "/cmos";
    }

    /**
     * 查询【操作系统恢复登记】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        startPage();
        List<CmOsRecoveryRegister> list = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterList(cmOsRecoveryRegister);
        return getDataTable(list);
    }

    /**
     * 详情【操作系统恢复登记】列表
     */
    @GetMapping("/detail/{recoveryRegisterId}")
    public String detail(@PathVariable("recoveryRegisterId") String recoveryRegisterId, ModelMap mmap)
    {
        CmOsRecoveryRegister cmOsRecoveryRegister = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterById(recoveryRegisterId);
        if (StringUtils.isNotEmpty(cmOsRecoveryRegister.getOrgId())) {
            String orgId = cmOsRecoveryRegister.getOrgId();
            String org = orgId.substring(0,orgId.length() - 1);
            OgOrg ogOrg = iSysDeptService.selectDeptById(org);
            if(ogOrg!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "666667");
                if(list!=null){
                    mmap.put("pidList", list);
                }
            }
        }

        mmap.put("cmOsRecoveryRegister",cmOsRecoveryRegister);
        return prefix + "/detail";
    }

    /**
     * 导出【操作系统恢复登记】列表
     */
    @Log(title = "【操作系统恢复登记】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        List<CmOsRecoveryRegister> list = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterList(cmOsRecoveryRegister);
        ExcelUtil<CmOsRecoveryRegister> util = new ExcelUtil<CmOsRecoveryRegister>(CmOsRecoveryRegister.class);
        return util.exportExcel(list, "操作系统恢复登记");
    }

    //响应请求分页数据
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    //设置请求分页数据
    @Override
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
}
