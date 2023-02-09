package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员 信息操作处理
 */

@Controller
@RequestMapping("/system/peo")
public class OgPeoController extends BaseController {

    private String prefix = "system/peo";

    @Autowired
    private ISysDeptService deptService;


    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysWorkService workService;


    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgPerson ogPerson)
    {
        startPage();
        List<OgPerson> list = ogPersonService.selectOgPersonList(ogPerson);
        return getDataTable(list);
    }


    @RequestMapping()
    public String show(){
        return prefix+"/show";
    }

    /**
     * 新增人员
     */
    @RequestMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    /**
     * 新人人员保存
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgPerson ogPerson){
        ogPerson.setpId(UUID.getUUIDStr());
        ogPerson.setAdder(ShiroUtils.getLoginName());
        ogPerson.setAddtime(DateUtils.getTime());
        return toAjax(ogPersonService.insertOgPerson(ogPerson));
    }

    /**
     * 人员修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, ModelMap modelMap) {
        OgPerson ogPerson =  ogPersonService.selectOgPersonById(id);
        modelMap.addAttribute("ogPerson",ogPerson);
        return prefix + "/edit";
    }

    /**
     * 人员修改提交
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSubmit(OgPerson ogPerson){
        ogPerson.setUpdatetime(DateUtils.getTime());
        return toAjax(ogPersonService.updateOgPerson(ogPerson));
    }

    /**
     * 启用禁用人员
     */
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(String pId,String status){
        OgPerson ogPerson = new OgPerson();
        ogPerson.setpId(pId);
        ogPerson.setInvalidationMark(status);
        return toAjax(ogPersonService.updateOgPersonStatus(ogPerson));
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
    @PostMapping("/levelcode")
    @ResponseBody
    public AjaxResult selectListByLevelCode(String nodeId,String rId){
        AjaxResult ajaxResult = new AjaxResult();
        //获取当前节点对象的信息
        List<OgPerson> ogPeople = ogPersonService.selectListByOrgIdAndRoleId(nodeId,rId);
        return ajaxResult.put("data",ogPeople);
    }
}
