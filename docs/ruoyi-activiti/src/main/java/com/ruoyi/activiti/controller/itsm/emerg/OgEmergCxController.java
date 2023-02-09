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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.domain.OgEmerg;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("emerg/cx")
public class OgEmergCxController extends BaseController
{

    //审核界面路径
    private String prefix_cx = "emerg/cx";

    @Autowired
    private ISysWorkService ISysWorkService;

    @Autowired
    private OgEmergServiceImpl emergService;

    /**
     * 查询列表页面
     *
     * @return
     */
    @GetMapping()
    public String emerg() {
        return prefix_cx + "/emerg";
    }

    //查询事件单
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgEmerg emerg)
    {
        startPage();
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userName = ogUser.getUsername();
        if (!"admin".equals(userName)){
            emerg.setPersonemergId(ogUser.getpId());
        }
        List<OgEmerg> list = emergService.selectEmergList(emerg);
        return getDataTable(list);
    }

    /*
     * 导出的事件单
     * */
    @Log(title = "事件单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgEmerg emerg)
    {
        String isCurrentPage = (String) emerg.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<OgEmerg> list = emergService.selectEmergList(emerg);
            ExcelUtil<OgEmerg> util = new ExcelUtil<OgEmerg>(OgEmerg.class);
            return util.exportExcel(list, "应急事件单数据");
        } else {
           /* //根据当前登录人
            emerg = new OgEmerg();
            String pid = ShiroUtils.getOgUser().getpId();
            emerg.setPersonemergId(pid);*/
            List<OgEmerg> list = emergService.selectEmergList(emerg);
            ExcelUtil<OgEmerg> util = new ExcelUtil<OgEmerg>(OgEmerg.class);
            return util.exportExcel(list, "应急事件单数据");
        }
    }

    /**
     * 查看详情事件单
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        //获取工作组
        List<OgGroup> ogGroups = ISysWorkService.selectOgGroupList(null);
        mmap.put("groups",ogGroups);
        mmap.put("OgEmerg", emergService.selectById(id));
        return prefix_cx + "/detail";
    }

    /**
     * 查看流程
     */
    @GetMapping("/selectProcess/{emergId}")
    public String selectProcess(@PathVariable("emergId") String emergId, ModelMap mmap) {
        mmap.put("OgEmerg", emergService.selectOgEmergById(emergId));
        return prefix_cx + "/processDetail";
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
