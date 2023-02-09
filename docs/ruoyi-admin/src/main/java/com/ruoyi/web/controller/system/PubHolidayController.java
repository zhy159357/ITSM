package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PubHoliday;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IPubHolidayService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//节假日
@Controller
@RequestMapping("/system/holiday")
public class PubHolidayController extends BaseController {

    private String prefix = "system/holiday";

    @Autowired
    private IPubHolidayService holidayService;


    /**
     * 转到前端页面
     * @return
     */
    @GetMapping()
    public String holiday()
    {
        return prefix + "/holiday";
    }

    /**
     * 节假日列表展示
     * @param holiday
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubHoliday holiday)
    {
        startPage();
        List<PubHoliday> list = holidayService.selectHolidayList(holiday);
        return getDataTable(list);
    }

    /**
     * 删除节假日
     * @param ids
     * @return
     */
    @Log(title = "节假日管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(holidayService.deleteHolidayByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
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
     * 保存节假日信息
     * @param pubHoliday
     * @return
     */

    @Log(title = "节假日管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@Validated PubHoliday pubHoliday)
    {
        try
        {
            pubHoliday.setHolidayId(UUID.getUUIDStr());
            return toAjax(holidayService.insert(pubHoliday));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }


    }


    /**
     * 转到修改节假日页面
     */
    @GetMapping("/edit/{holidayId}")
    public String edit(@PathVariable("holidayId") String holidayId, ModelMap mmap)
        {
        mmap.put("holiday", holidayService.selectholidayById(holidayId));
        return prefix + "/edit";
    }

    /**
     * 修改节假日
     */
    @Log(title = "修改节假日", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated PubHoliday holiday)
    {
        holiday.setUpdateBy(ShiroUtils.getLoginName());

        return toAjax(holidayService.edit(holiday));

    }







}
