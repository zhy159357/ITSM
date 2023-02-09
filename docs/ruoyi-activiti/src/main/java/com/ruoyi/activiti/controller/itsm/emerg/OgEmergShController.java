package com.ruoyi.activiti.controller.itsm.emerg;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.service.impl.OgEmergServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.domain.OgEmerg;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("emerg/sh")
public class OgEmergShController extends BaseController
{

    //审核界面路径
    private String prefix_sh = "emerg/sh";

    @Autowired
    private ISysWorkService ISysWorkService;

    @Autowired
    private OgEmergServiceImpl emergService;

    /**
     * 业务审核列表页面
     *
     * @return
     */
    @GetMapping()
    public String emerg() {
        return prefix_sh + "/emerg";
    }

    //审核事件单
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgEmerg emerg)
    {
        startPage();
        //获取待审核状态
        emerg.setEmergMark("2");
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userName = ogUser.getUsername();
        if (!"admin".equals(userName)){
            emerg.setPersonemergId(ogUser.getpId());
        }
        List<OgEmerg> list = emergService.selectEmergList(emerg);
        return getDataTable(list);
    }

    /**
     * 审核事件单根据id查询出来
     */
    @GetMapping("/emergApprove/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        //获取工作组
        List<OgGroup> ogGroups = ISysWorkService.selectOgGroupList(null);
        mmap.put("groups",ogGroups);

        mmap.put("OgEmerg", emergService.selectEmergById(id));
        return prefix_sh + "/emergApprove";
    }

    /**
     * 审核保存事件单
     */
    @Log(title = "事件单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/emergApprove")
    @ResponseBody
    public AjaxResult editSave(@Validated OgEmerg emerg)
    {
        return toAjax(emergService.updateEmergMark(emerg));
    }

    /**
     * 响应请求分页数据
     */
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

    /**
     * 设置请求分页数据
     */
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
