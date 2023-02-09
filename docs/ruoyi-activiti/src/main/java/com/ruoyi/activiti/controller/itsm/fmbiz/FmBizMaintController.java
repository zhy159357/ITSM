package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.BizLeaveVo;
import com.ruoyi.activiti.service.FmBizMaintService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CustInfo;
import com.ruoyi.common.core.domain.entity.FmBizMaint;
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

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/fmbizmaint")
public class FmBizMaintController extends BaseController {
    //事件维护路径前缀
    private String prefix = "fmbiz/maint";

    @Autowired
    private FmBizMaintService fmBizMaintService;

    @GetMapping("/maint")
    public String publicApplyPage() {
        return prefix + "/fmbizmaint";
    }

    /**
     * 查询事件维护列表
     *
     * @param fmBizMaint
     * @return
     * @throws ParseException
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizMaint fmBizMaint) throws ParseException {
        startPage();
        List<FmBizMaint> list = fmBizMaintService.checkFmBizMaint(fmBizMaint);
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
        return prefix + "/add";
    }

    /**
     * 新增保存
     */
    @Log(title = "事件维护新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated FmBizMaint fmBizMaint) {
        int size = fmBizMaintService.getByCount(fmBizMaint.getmTitle());
        if (size > 0) {
            return error("事件标题已存在");
        } else {
            fmBizMaint.setmId(UUID.getUUIDStr());
            return toAjax(fmBizMaintService.insertFmBizMaint(fmBizMaint));
        }


    }

    /**
     * 修改跳转页面
     *
     * @param mId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{mId}")
    public String edit(@PathVariable("mId") String mId, ModelMap mmap) {
        mmap.put("info", fmBizMaintService.getByMId(mId));
        return prefix + "/edit";
    }

    /**
     * 修改保存
     *
     * @param
     * @return
     */
    @Log(title = "事件维护修改保存", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated FmBizMaint fmBizMaint) {
        int size = 0;
        FmBizMaint fmBizMaintObj = fmBizMaintService.getByMId(fmBizMaint.getmId());
        //比较修改前后是否相同
        if (!fmBizMaint.getmTitle().equals(fmBizMaintObj.getmTitle())) {
            size = fmBizMaintService.getByCount(fmBizMaint.getmTitle());
        }
        if (size > 0) {
            return error("事件标题已存在");
        } else {
            fmBizMaint.setmId(fmBizMaint.getmId());
            return toAjax(fmBizMaintService.updateById(fmBizMaint));
        }

    }

    @Log(title = "事件维护删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(fmBizMaintService.deleteByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());

        }
    }

    /**
     * 查询事件标题or描述
     * @param type
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectList/{type}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult selectList(@PathVariable("type") int type, HttpServletRequest request)
    {
        AjaxResult ajax = new AjaxResult();
        List<FmBizMaint> list = fmBizMaintService.selectList(type);
        ajax.put("code", 200);
        ajax.put("value", list);
        return ajax;
    }
}
