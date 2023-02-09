package com.ruoyi.activiti.controller.itsm.fmKind;

import com.ruoyi.activiti.service.IFmKindService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmKind;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SmBizScheduling;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.OgSysMapper;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fmKind")
public class fmKindController extends BaseController {
    @Autowired
    private IFmKindService iFmKindService;
    @Autowired
    private OgSysMapper ogSysMapper;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private IOgPersonService iOgPersonService;

    /**
     * 保存事件单系统分类
     *
     * @param fmKind
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult saveflow(FmKind fmKind) {
        fmKind.setParentId(fmKind.getFmTypeId());

        fmKind.setFmTypeId(UUID.getUUIDStr());
            OgUser u = ShiroUtils.getOgUser();//获取当前登陆人
            fmKind.setAdderId(u.getpId());
            fmKind.setAddTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
            fmKind.setType("1");
            //保存当前上级
            return toAjax(iFmKindService.insertFmKind(fmKind));
    }

    /**
     * 修改事件单系统分类
     *
     * @param fmKind
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult Edit(FmKind fmKind) {
        return toAjax(iFmKindService.updateFmKind(fmKind));
    }

    /**
     * 事件定义打开页面
     *
     * @return
     */
    @GetMapping("/fmFaultKind")
    public String passignPage() {
        startPage();
        return "fmbiz/fmFaultKind/fmFaultKind";
    }

    /**
     * 事件定义打开页面默认加载查询
     */
    @PostMapping("/syslist")
    @ResponseBody
    public TableDataInfo selectSysList(OgSys ogSys) {
        startPage();
        ogSys.setInvalidationMark("1");
        List<OgSys> list = ogSysMapper.selectOgSysList(ogSys);
        for (OgSys sys : list) {
            if (!"".equals(sys.getOrgId()) && sys.getOrgId() != null) {
                sys.setOrgName(iSysDeptService.selectDeptById(sys.getOrgId()).getOrgName());
            }
        }
        return getDataTable(list);
    }

    /**
     * 事件定义打开页面默认加载查询
     */
    @PostMapping("/list")
    @ResponseBody
    public List<FmKind> selectList(FmKind fmKind) {

        if (StringUtils.isEmpty(fmKind.getSysId())) {
            fmKind.setSysId("1");
        }
        fmKind.setParentId(StringUtils.isNotEmpty(fmKind.getParentName()) ? fmKind.getParentId() : "");

        List<FmKind> list = iFmKindService.selectFmKindList(fmKind);
        for (FmKind fk : list) {
            fk.setSysId(iSysApplicationManagerService.selectOgSysBySysId(fk.getSysId()).getCaption());
            fk.setAdderId(iOgPersonService.selectOgPersonById(fk.getAdderId()).getpName());
        }
        return list;
    }

    /**
     * 点击增加按钮(有上级)
     *
     * @param sysId
     * @param mmap
     * @return
     */
    @GetMapping("/add/{sysId}/{fmTypeId}")
    public String add(@PathVariable("sysId") String sysId,@PathVariable("fmTypeId") String fmTypeId, ModelMap mmap) {
        mmap.put("sysId", sysId);
        mmap.put("caption", iSysApplicationManagerService.selectOgSysBySysId(sysId).getCaption());
        mmap.put("fmTypeId", fmTypeId);
        mmap.put("fmTypeName", iFmKindService.selectFmKindById(fmTypeId).getName());
        return "fmbiz/fmFaultKind/editFmFaultKind";
    }

    /**
     * 点击增加按钮(无上级)
     *
     * @param sysId
     * @param mmap
     * @return
     */
    @GetMapping("/add/{sysId}")
    public String addWS(@PathVariable("sysId") String sysId, ModelMap mmap) {
        mmap.put("sysId", sysId);
        mmap.put("caption", iSysApplicationManagerService.selectOgSysBySysId(sysId).getCaption());
        return "fmbiz/fmFaultKind/editFmFaultKind";
    }

    /**
     * 点击修改按钮
     *
     * @param fmTypeId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{fmTypeId}")
    public String edit(@PathVariable String fmTypeId, ModelMap mmap) {
        FmKind fk = iFmKindService.selectFmKindById(fmTypeId);
        mmap.put("caption", iSysApplicationManagerService.selectOgSysBySysId(fk.getSysId()).getCaption());
        mmap.put("FmKind", fk);
        //获取父级id
        if(fk.getParentId() != null && fk.getParentId() !=""){
            String parentId = fk.getParentId();
            //获取父级名称
            String paretName = iFmKindService.selectFmKindById(parentId).getName();
            mmap.put("parentId", parentId);
            mmap.put("paretName", paretName);
        }

        return "fmbiz/fmFaultKind/editFmFaultKind2";
    }




}
