package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.core.domain.entity.SysInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.DeleteRea;
import com.ruoyi.common.enums.InfoStateList;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.ISysInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 信息管理信息
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/infoSearch")
public class SysInfoSearchController extends BaseController {
    private String prefix = "system/info";

    @Autowired
    private ISysInfoService infoService;

    @GetMapping()
    public String info(ModelMap mmap)
    {
        List stateOfList = InfoStateList.getList();
        List deleList = DeleteRea.getList();
        mmap.addAttribute("stateOfList",stateOfList);
        mmap.addAttribute("deleList",deleList);
        return prefix + "/infoSearch";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysInfo info) {
        startPage();
        List<SysInfo> list = infoService.selectInfoList(info);
        return getDataTable(list);
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = infoService.selectFolderTree(new SysFolder());
        return ztrees;
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{regime_info_id}")
    public String detail(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap)
    {
        List<OgPerson> checkerList = infoService.selectCheckerList(new OgPerson());
        SysInfo info = infoService.selectInfoById(regime_info_id);
        info.setPrint_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getPrint_time()));
        mmap.put("info",info );
        mmap.addAttribute("checkerList",checkerList);
        mmap.addAttribute("commit_id",ShiroUtils.getOgUser().getUsername());
        SimpleDateFormat dateformat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return prefix + "/detail";
    }
    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

    /**
     * 选择部门树
     */
    @GetMapping(value = { "/selectFolder/{folder}"})
    public String selectInfoTree(@PathVariable("folder") String folder,ModelMap mmap)
    {
        mmap.put("dept", infoService.selectTreeById(folder));
        return prefix + "/tree";
    }

}