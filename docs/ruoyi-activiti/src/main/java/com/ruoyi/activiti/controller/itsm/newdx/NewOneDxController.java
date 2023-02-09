package com.ruoyi.activiti.controller.itsm.newdx;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.service.IOgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
*
* 验证码查询
* */
@Controller
@RequestMapping("new/newOne")
public class NewOneDxController extends BaseController
{
    //查询界面路径
    private String prefix_dx = "new/newOne";

    @Autowired
    private IOgUserService ogUserService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_dx + "/newOne";
    }

    //查询
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgUser ogUser)
    {
        startPage();
        List<OgUser> list = ogUserService.selectOgUserList(ogUser);
        return getDataTable(list);
    }

    //响应请求分页数据
    @Override
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
