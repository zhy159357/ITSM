package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.service.CustInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CustInfo;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cust")
public class CustInfoController extends BaseController {
    //我的客户信息路径前缀
    private String prefix_public = "fmbiz/cust";

    @Autowired
    private CustInfoService custInfoService;

    @GetMapping("/cust")
    public String publicApplyPage() {
        return prefix_public + "/cust";
    }

    /**
     * 查询我的事件单列表
     *
     * @param custInfo
     * @return
     * @throws ParseException
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CustInfo custInfo) throws ParseException {
        startPage();
        List<CustInfo> list = custInfoService.checkCustInfo(custInfo);
        return getDataTable(list);
    }

    /**
     * 响应请求分页数据
     *
     * @param list
     * @return
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 设置请求分页数据
     */
    @Override
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 跳转新增页面
     *
     * @param mmap
     * @return
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return prefix_public + "/add";
    }

    /**
     * 新增保存
     */
    @Log(title = "客户信息新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated CustInfo custInfo) {
        int size = custInfoService.getByCount(custInfo.getcPhone());
        if (size > 0) {
            return error("手机号重复");
        } else {
            custInfo.setcId(UUID.getUUIDStr());
            custInfo.setCreateTime(new Date());
            return toAjax(custInfoService.insertCustInfo(custInfo));
        }
    }

    /**
     * 修改跳转页面
     *
     * @param cId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{cId}")
    public String edit(@PathVariable("cId") String cId, ModelMap mmap) {
        mmap.put("info", custInfoService.getByCId(cId));
        return prefix_public + "/edit";
    }

    /**
     * 修改保存
     *
     * @param
     * @return
     */
    @Log(title = "客户信息修改保存", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated CustInfo custInfo) {
        int size = 0;
        CustInfo custInfoObj = custInfoService.getByCId(custInfo.getcId());
        //比较修改前后是否相同，不同是则修改了手机号
        if (!custInfo.getcPhone().equals(custInfoObj.getcPhone())) {
             size = custInfoService.getByCount(custInfo.getcPhone());
        }
        if (size > 0) {
            return error("手机号重复");
        } else {
            custInfo.setcId(custInfo.getcId());
            custInfo.setCreateTime(new Date());
            return toAjax(custInfoService.updateById(custInfo));
        }
    }

    /**
     * 查询客户信息
     *
     * @param cName
     * @param cPhone
     * @return
     */
    @PostMapping("/checkCustInfo")
    @ResponseBody
    public List<CustInfo> checkCustInfo(String cName, String cPhone) {
        CustInfo custInfo = new CustInfo();
        custInfo.setcName(cName);
        custInfo.setcPhone(cPhone);
        List<CustInfo> list = custInfoService.checkCustInfo(custInfo);
        return list;
    }

    @Log(title = "客户信息删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(custInfoService.deleteCustByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());

        }
    }
}