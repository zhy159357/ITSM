package com.ruoyi.activiti.controller.itsm.cmdb;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.CmDbRecoveryRegister;
import com.ruoyi.activiti.service.ICmDbRecoveryRegisterService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
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
 * 【数据库恢复登记】Controller
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
@Controller
@RequestMapping("cmdb/cx")
public class CmDbRecoveryRegisterCxController extends BaseController
{
    private String prefix = "cmdb/cx";

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CmDbRecoveryRegisterCxController.class);

    @Autowired
    private ICmDbRecoveryRegisterService cmDbRecoveryRegisterService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    private IOgPersonService iOgPersonService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    @GetMapping()
    public String register()
    {
        return prefix + "/cmdb";
    }

    /**
     * 查询【数据库恢复登记】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        startPage();
        List<CmDbRecoveryRegister> list = cmDbRecoveryRegisterService.selectCmDbRecoveryRegisterList(cmDbRecoveryRegister);
        return getDataTable(list);
    }

    /**
     * 导出【数据库恢复登记表】列表
     */
    @Log(title = "【数据库恢复登记】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        List<CmDbRecoveryRegister> list = cmDbRecoveryRegisterService.selectCmDbRecoveryRegisterList(cmDbRecoveryRegister);
        ExcelUtil<CmDbRecoveryRegister> util = new ExcelUtil<CmDbRecoveryRegister>(CmDbRecoveryRegister.class);
        return util.exportExcel(list, "数据库恢复登记");
    }

    /**
     * 详情【数据库恢复登记】列表
     */
    @GetMapping("/detail/{recoveryRegisterId}")
    public String detail(@PathVariable("recoveryRegisterId") String recoveryRegisterId, ModelMap mmap)
    {
        CmDbRecoveryRegister cmDbRecoveryRegister = cmDbRecoveryRegisterService.selectCmDbRecoveryRegisterById(recoveryRegisterId);
        if (StringUtils.isNotEmpty(cmDbRecoveryRegister.getOrgId())) {
            String orgId = cmDbRecoveryRegister.getOrgId();
            //String org = orgId.substring(0,orgId.length() - 1);
            OgOrg ogOrg = iSysDeptService.selectDeptById(orgId);
            if(ogOrg!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "666668");
                if(list!=null){
                    mmap.put("pidList", list);
                }
            }
        }
        mmap.put("cmDbRecoveryRegister",cmDbRecoveryRegister);
        return prefix + "/detail";
    }

    /**
     * 暂存
     *
     * @return
     */
    @Log(title = "新增保存", businessType = BusinessType.INSERT)
    @PostMapping("/fileSave")
    @ResponseBody
    public AjaxResult fileSave(CmDbRecoveryRegister cmDbRecoveryRegister) {
        try {
            if (StringUtils.isEmpty(cmDbRecoveryRegister.getRecoveryRegisterId())) {
                //cmDbRecoveryRegister.setBackupDate(DateUtils.dateTimeNow());
                //cmDbRecoveryRegister.setRecoveryRegisterId(UUID.getUUIDStr());
                cmDbRecoveryRegisterService.insertCmDbRecoveryRegister(cmDbRecoveryRegister);
                return AjaxResult.success("操作成功", cmDbRecoveryRegister.getRecoveryRegisterId());
            } else {
                cmDbRecoveryRegisterService.updateCmDbRecoveryRegister(cmDbRecoveryRegister);
                return AjaxResult.success("操作成功", cmDbRecoveryRegister.getRecoveryRegisterId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据库恢复暂存失败: {}",e);
            throw new BusinessException("暂存失败，所屬中心是：" + cmDbRecoveryRegister.getCentreName());
        }

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
