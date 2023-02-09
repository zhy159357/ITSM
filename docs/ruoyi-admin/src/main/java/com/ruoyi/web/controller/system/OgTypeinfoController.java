package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.OgTypeInfoExcelHeader;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.domain.entity.OgTypeinfoVo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgTypeinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 参数列别Controller
 *
 * @author ruoyi
 * @date 2020-12-06
 */
@Controller
@RequestMapping("/system/typeinfo")
@Api(tags = "调试级联接口")
public class OgTypeinfoController extends BaseController {
    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OgTypeinfoController.class);

    private String prefix = "system/typeinfo";

    private static final String PARENT_ID = null;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    @RequiresPermissions("system:typeinfo:view")
    @GetMapping()
    public String typeinfo() {
        return prefix + "/typeinfo";
    }

    /**
     * 查询参数列别列表
     */
    //@RequiresPermissions("system:typeinfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgTypeinfo ogTypeinfo) {
        startPage();
        List<OgTypeinfo> list = ogTypeinfoService.selectOgTypeinfoList(ogTypeinfo);
        return getDataTable(list);
    }

    /**
     * 根据指定typeTypeNo查询并组装树状的结构
     *
     * @return
     */
    @PostMapping("/treeListOgTypeinfo")
    @ResponseBody
    @ApiOperation(value = "级联数据")
    public List<OgTypeinfoVo> treeList() {
        OgTypeinfo ogTypeinfo = new OgTypeinfo();
        List<OgTypeinfo> list = ogTypeinfoService.selectOgTypeinfoList(ogTypeinfo);
        return this.getTree(list);
    }

    /**
     * 组装树形结构
     *
     * @param record
     * @return
     */
    public List<OgTypeinfoVo> getTree(List<OgTypeinfo> record) {
        List<OgTypeinfoVo> ogTypeinfoVoList = new LinkedList();
        for (OgTypeinfo typeinfo : record) {
            if (PARENT_ID == typeinfo.getParentId()) {
                OgTypeinfoVo ogTypeinfo = new OgTypeinfoVo();
                ogTypeinfo.setTypeinfoId(typeinfo.getTypeinfoId());
                ogTypeinfo.setTypeName(typeinfo.getTypeName());
                ogTypeinfo.setChildOgTypeInfo(getChild(typeinfo.getTypeinfoId(), record));
                ogTypeinfoVoList.add(ogTypeinfo);
            }
        }
        return ogTypeinfoVoList;
    }

    /**
     * 递归获取每级的子类
     *
     * @param code
     * @param record
     * @return
     */
    private static List<OgTypeinfoVo> getChild(String code, List<OgTypeinfo> record) {
        List<OgTypeinfoVo> childrenList = new LinkedList();
        for (OgTypeinfo ogTypeinfo : record) {
            if (code.equals(ogTypeinfo.getParentId())) {
                OgTypeinfoVo typeInfo = new OgTypeinfoVo();
                typeInfo.setTypeinfoId(ogTypeinfo.getTypeinfoId());
                typeInfo.setTypeName(ogTypeinfo.getTypeName());
                typeInfo.setChildOgTypeInfo(getChild(ogTypeinfo.getTypeinfoId(), record));
                childrenList.add(typeInfo);
            }
        }
        return childrenList;
    }

    /**
     * 导出参数列别列表
     */
    //@RequiresPermissions("system:typeinfo:export")
    @Log(title = "参数列别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgTypeinfo ogTypeinfo) {
        List<OgTypeinfo> list = ogTypeinfoService.selectOgTypeinfoList(ogTypeinfo);
        ExcelUtil<OgTypeinfo> util = new ExcelUtil<OgTypeinfo>(OgTypeinfo.class);
        return util.exportExcel(list, "typeinfo");
    }

    /**
     * 新增参数列别
     */
    @GetMapping("/addTypeInfo/{typeinfoId}")
    public String add(@PathVariable("typeinfoId") String typeinfoId, ModelMap mmap) {
        /*mmap.put("parentId",info.getTypeinfoId());
        mmap.put("typeTypeNo",info.getTypeTypeNo());
        mmap.put("typeTypeName",info.getTypeTypeName());
        mmap.put("parentTypeName",info.getParentTypeName());*/
        OgTypeinfo info = ogTypeinfoService.selectOgTypeinfoById(typeinfoId);
        if (info == null) {// 防止没有parentId的情况报错空指针
            info = new OgTypeinfo();
        }
        mmap.put("ogTypeinfo", info);
        return prefix + "/add";
    }

    /**
     * 新增保存参数列别
     */
    //@RequiresPermissions("system:typeinfo:add")
    @Log(title = "参数列别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OgTypeinfo ogTypeinfo) {
        return toAjax(ogTypeinfoService.insertOgTypeinfo(ogTypeinfo));
    }

    /**
     * 修改参数列别
     */
    @GetMapping("/edit/{typeinfoId}")
    public String edit(@PathVariable("typeinfoId") String typeinfoId, ModelMap mmap) {
        OgTypeinfo ogTypeinfo = ogTypeinfoService.selectOgTypeinfoById(typeinfoId);
        mmap.put("ogTypeinfo", ogTypeinfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存参数列别
     */
    //@RequiresPermissions("system:typeinfo:edit")
    @Log(title = "参数列别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OgTypeinfo ogTypeinfo) {
        return toAjax(ogTypeinfoService.updateOgTypeinfo(ogTypeinfo));
    }

    /**
     * 删除参数列别
     */
    //@RequiresPermissions("system:typeinfo:remove")
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(ogTypeinfoService.deleteOgTypeinfoByIds(ids));
    }

    /**
     * 加载参数设置列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZtreeStr> treeData() {
        List<ZtreeStr> ztrees = ogTypeinfoService.selectTypeinfoTree(new OgTypeinfo());
        return ztrees;
    }

    /**
     * 资源变更单分类树加载
     */
    @GetMapping("/cmBizTree")
    public String cmBizTree(ModelMap mmap) {
        List<OgTypeinfo> list = ogTypeinfoService.selectCmbizType();
        mmap.put("typeinfo", list);
        return "cmbiz/flow/subpage/tree";
    }

    /**
     * 加载部门列表树（排除下级）
     */
    @GetMapping("/treeData/{typeinfoId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "typeinfoId", required = false) String typeinfoId) {
        OgTypeinfo ogTypeinfo = new OgTypeinfo();
        ogTypeinfo.setTypeinfoId(typeinfoId);
        List<Ztree> ztrees = ogTypeinfoService.selectTypeTreeExcludeChild(ogTypeinfo);
        return ztrees;
    }

    /**
     * 选择上级类别树
     *
     * @param typeinfoId 类别ID
     */
    @GetMapping(value = {"/selectType/{typeinfoId}"})
    public String selectType(@PathVariable("typeinfoId") String typeinfoId,
                             ModelMap mmap) {

        //根据获取的id查出他的父id
        mmap.put("typeinfo", ogTypeinfoService.selectOgTypeinfoById(typeinfoId));
        return prefix + "/tree";
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, String typeinfoId) throws Exception {
        ExcelUtil<OgTypeInfoExcelHeader> util = new ExcelUtil<>(OgTypeInfoExcelHeader.class);
        List<OgTypeInfoExcelHeader> userList = null;
        try {
            userList = util.importExcel(file.getInputStream());
        } catch (Exception e) {
            log.error("导入数据异常:{}", e.getMessage());
            return AjaxResult.warn("导入数据有误的！");
        }
        String operName = ShiroUtils.getSysUser().getLoginName();
        ogTypeinfoService.importOgTypeData(userList, typeinfoId);
        return AjaxResult.success("导入成功");
    }

    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<OgTypeInfoExcelHeader> util = new ExcelUtil<>(OgTypeInfoExcelHeader.class);
        return util.importTemplateExcel("导入模板");
    }

    @PostMapping("/selectOgTypeInfoByEvent")
    @ResponseBody
    public AjaxResult selectOgTypeInfoByEvent(String typeNo, String level) {
        List<OgTypeinfo> list = ogTypeinfoService.selectOgTypeInfoByEvent(typeNo, level);
        return AjaxResult.success(list);
    }

    @PostMapping("/selectOgTypeInfoByTypeNo")
    @ResponseBody
    @ApiOperation("查询级联1级数据,如类别")
    public AjaxResult selectOgTypeInfoByTypeNo(@RequestBody Map<String, String> requestMap) {
        List<OgTypeinfo> list = ogTypeinfoService.selectOgTypeInfoByEvent(requestMap.get("typeNo"), "1");
        List<Map<String, String>> objects = new ArrayList<>();
        for (OgTypeinfo ogTypeinfo : list) {
            Map<String, String> map = new HashMap<>();
            map.put("value", ogTypeinfo.getTypeNo());
            map.put("label", ogTypeinfo.getTypeName());
            objects.add(map);
        }
        return AjaxResult.success(objects);
    }


    @PostMapping("/selectSubOgTypeInfoByTypeNo")
    @ResponseBody
    public AjaxResult selectSubOgTypeInfoByTypeNo(@RequestBody Map<String, String> requestMap) {
        List<OgTypeinfo> list = ogTypeinfoService.selectCurrentOgTypeInfoList(requestMap.get("typeTypeNo"), requestMap.get("typeNo"));
        List<Map<String, String>> subOgTypeinfoList = new ArrayList<>();
        for (OgTypeinfo ogTypeinfo : list) {
            Map<String, String> map = new HashMap<>();
            map.put("value", ogTypeinfo.getTypeNo());
            map.put("label", ogTypeinfo.getTypeName());
            subOgTypeinfoList.add(map);
        }
        return AjaxResult.success(subOgTypeinfoList);
    }
}
