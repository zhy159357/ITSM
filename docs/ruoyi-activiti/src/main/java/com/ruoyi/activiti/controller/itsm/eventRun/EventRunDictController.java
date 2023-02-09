package com.ruoyi.activiti.controller.itsm.eventRun;

import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.PubPara;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.IPubParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("eventRun/Dict")
public class EventRunDictController extends BaseController {
    private String prefix = "run/type";
    @Autowired
    private IPubParaService pubParaService;
    @Autowired
    private ICommonService commonService;
    @GetMapping()
    public String dictData() {
        return prefix + "/type";
    }
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubPara pubPara)
    {
        pubPara.setUpdateFlag("e");
        startPage();
        List<PubPara> list = pubParaService.selectPubParaList(pubPara);
        return getDataTable(list);
    }
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PubPara pubPara)
    {
        if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(pubParaService.checkParaNameUnique(pubPara)))
        {
            return error("新增参数项'" + pubPara.getParaName() + "'失败，参数项代码已存在");
        }
        pubPara.setParaId(UUID.getUUIDStr());
        pubPara.setState("1");
        pubPara.setUpdateFlag("e");
        return toAjax(pubParaService.insertPubPara(pubPara));
    }

    /**
     * 修改参数页面
     * @param paraId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") String  paraId, ModelMap mmap)
    {
        mmap.put("pubPara", pubParaService.selectPubParaById(paraId));
        return prefix + "/edit";
    }
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String  paraId)
    {
        int deletePara=pubParaService.deleteByParaId(paraId);
        return toAjax(deletePara);
    }
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult addEdit(PubPara pubPara){
        return toAjax(pubParaService.updatePubParaById(pubPara));
    }
    @GetMapping("/goListUser/{paraId}")
    public String goListUser(@PathVariable("paraId")String paraId,ModelMap mp) {
        mp.put("paraId",paraId);
        return prefix + "/auditUser";
    }
    /**
     * 查询数据中心人员
     */
    @PostMapping("/listUser")
    @ResponseBody
    public TableDataInfo listUser(OgPerson ogPerson) {
        ogPerson.setrId("6211");
        List<OgPerson> userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        return getDataTable_ideal(userlist);
    }
}
