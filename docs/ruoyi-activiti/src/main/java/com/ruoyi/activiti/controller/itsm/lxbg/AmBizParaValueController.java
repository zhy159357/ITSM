package com.ruoyi.activiti.controller.itsm.lxbg;

import java.util.List;

import com.ruoyi.activiti.service.IAmBizParaValueService;
import com.ruoyi.common.core.domain.entity.AmBizParaValue;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【快捷标签接受机构页面】Controller
 *
 * @author ruoyi
 * @date 2021-01-19
 */
@Controller
@RequestMapping("system/value")
public class AmBizParaValueController extends BaseController
{
    private String prefix = "system/value";

    private String add_prefix = "lxbg/addlxbg";

    @Autowired
    private IAmBizParaValueService amBizParaValueService;

    /**
     * 转到接受机构页面
     * @return
     */
    @GetMapping()
    public String value()
    {
        return prefix + "/value";
    }

    /**
     * 查询列表
     * @param amBizParaValue
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AmBizParaValue amBizParaValue)
    {
        startPage();
        List<AmBizParaValue> list = amBizParaValueService.selectAmBizParaValueList(amBizParaValue);
        return getDataTable(list);
    }
    /**
     * 查询列表
     * @param amBizParaValue
     * @return
     */
    @PostMapping("/listPara")
    @ResponseBody
    public List<AmBizParaValue> listPara(AmBizParaValue amBizParaValue)
    {
        //startPage();
        List<AmBizParaValue> list = amBizParaValueService.selectAmBizParaValueList(amBizParaValue);
        return list;
    }
    /**
     * 导出列表
     * @param amBizParaValue
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AmBizParaValue amBizParaValue)
    {
        List<AmBizParaValue> list = amBizParaValueService.selectAmBizParaValueList(amBizParaValue);
        ExcelUtil<AmBizParaValue> util = new ExcelUtil<AmBizParaValue>(AmBizParaValue.class);
        return util.exportExcel(list, "value");
    }

    /**
     * 转到新增页面
     * @param amParaId
     * @param mmap
     * @return
     */
    @GetMapping("/add/{amParaId}")
    public String add(@PathVariable String amParaId, ModelMap mmap)
    {
        mmap.put("amParaId",amParaId);
        return add_prefix + "/labelvalueadd";
    }

    /**
     * 新增保存
     * @param amBizParaValue
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AmBizParaValue amBizParaValue)
    {

        //根据机构生成执行机构id
        String masterOrgId=amBizParaValue.getReceivedeptid()==null?"":amBizParaValue.getReceivedeptid().toString();
        String[] masterOrgIds=masterOrgId.split(",");
        //根据工作组生成工作组id
        String workGroup=amBizParaValue.getReceivegroupid()==null?"":amBizParaValue.getReceivegroupid().toString();
        String[] workGroups=workGroup.split(",");

        //根据机构任务表生成
        if(masterOrgIds.length>0){

            for(String mId:masterOrgIds){
                if(StringUtils.isNotEmpty(mId)){
                    AmBizParaValue bizParaValue = new AmBizParaValue();
                    bizParaValue.setAmParaValueId(UUID.getUUIDStr());
                    bizParaValue.setReceivedeptid(mId);
                    bizParaValue.setAmParaId(amBizParaValue.getAmParaId());
                    amBizParaValueService.insertAmBizParaValue(bizParaValue);
                    bizParaValue.setReceivedeptid("");
                }

            }
        }
        //根据工作组任务表生成
        if(workGroups.length>0){

            for(String wId:workGroups){
                if(StringUtils.isNotEmpty(wId)){
                    AmBizParaValue bizParaValue = new AmBizParaValue();
                    bizParaValue.setAmParaValueId(UUID.getUUIDStr());
                    bizParaValue.setReceivegroupid(wId);
                    bizParaValue.setAmParaId(amBizParaValue.getAmParaId());
                    amBizParaValueService.insertAmBizParaValue(bizParaValue);
                    bizParaValue.setReceivegroupid("");

                }
            }

        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 转到修改页面
     * @param amParaValueId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{amParaValueId}")
    public String edit(@PathVariable("amParaValueId") String amParaValueId, ModelMap mmap)
    {
        AmBizParaValue amBizParaValue = amBizParaValueService.selectAmBizParaValueById(amParaValueId);
        mmap.put("amBizParaValue", amBizParaValue);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param amBizParaValue
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AmBizParaValue amBizParaValue)
    {
        return toAjax(amBizParaValueService.updateAmBizParaValue(amBizParaValue));
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(amBizParaValueService.deleteAmBizParaValueByIds(ids));
    }
}
