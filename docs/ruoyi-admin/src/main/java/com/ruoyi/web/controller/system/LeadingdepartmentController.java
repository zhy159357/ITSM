package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.Leadingdepartment;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.service.ILeadingdepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


//牵头部门
@Controller
@RequestMapping("/system/leadingdepartment")
public class LeadingdepartmentController extends BaseController {

    private String prefix = "system/leadingdepartment";

    @Autowired
    private ILeadingdepartmentService leadingdepartmentService;


    /**
     * 转到前端页面
     * @return
     */
    @GetMapping()
    public String leadingdepartment()
    {
        return prefix + "/leadingdepartment";
    }

    /**
     * 节假日列表展示
     * @param leadingdepartment
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Leadingdepartment leadingdepartment)
    {
        startPage();
        List<Leadingdepartment> list = leadingdepartmentService.selectLpList(leadingdepartment);
        return getDataTable(list);
    }

    /**
     * 节假日列表展示
     * @param leadingdepartment
     * @return
     */
    @GetMapping("/configLeadingdepartment")
    public String configLeadingdepartment(Leadingdepartment leadingdepartment)
    {
        return prefix + "/configLeadingdepartment";
    }


    /**
     * 转到新增节假日页面
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }


    /**
     * 保存牵头部门
     * @param Leadingdepartment
     * @return
     */

    @Log(title = "牵头部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@Validated OgRole role)
    {
        try
        {
            // 配置牵头部门
            List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
            for (Long menuId : role.getMenuIds())
            {
                Leadingdepartment  leadingdepartment =new Leadingdepartment();
                leadingdepartment.setLpid(UUID.getUUIDStr());
                leadingdepartment.setOrgId(menuId.toString());
                leadingdepartmentService.insert(leadingdepartment);
            }

            return toAjax(1);
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }



}
