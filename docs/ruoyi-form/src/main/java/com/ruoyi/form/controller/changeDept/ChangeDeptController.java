package com.ruoyi.form.controller.changeDept;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.form.domain.ChangeDeptEntity;
import com.ruoyi.form.domain.ChangeDeptEntityVo;
import com.ruoyi.form.service.IChangeDeptService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: ruoyi
 * @description: 变更额度配置功能
 * @author: ma zy
 * @date: 2022-09-23 14:24
 **/
@Controller
@RequestMapping("/changeDept")
public class ChangeDeptController extends BaseController {

    private String prefix = "changeDept";

    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IChangeDeptService iChangeDeptService;


    /**
     * 变更单列表
     */
    @GetMapping("/deptList")
    public String add(ModelMap mmap)
    {
        return prefix + "/dept";
    }

    /**
     * 额度和机构列表展示
     * @param dept
     * @return
     */
    @PostMapping("/listDept")
    @ResponseBody
    public TableDataInfo listDept(OgOrg dept) {
        List<ChangeDeptEntityVo> deptList = iChangeDeptService.listAll(dept);
        return getDataTable(deptList);
    }

    /**
     * 额度和机构列表展示VUE
     */
    @PostMapping("/listDeptAll")
    @ResponseBody
    public TableDataInfo listDeptAll(Integer pageNum, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        List<ChangeDeptEntity> deptList = iChangeDeptService.list();
        return getDataTable_app(deptList, pageNum.toString(), pageSize.toString());
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") String parentId, ModelMap mmap) {
        OgOrg org = deptService.selectDeptById(parentId);
        if(StringUtils.isNotEmpty(org.getOrgLv())) {
            org.setOrgLv(String.valueOf(Integer.valueOf(org.getOrgLv()) + 1));
        }

        mmap.put("dept", org);
        return prefix + "/add";
    }

    @GetMapping("/treeAll")
    public String treeAll() {
        return prefix + "/treeAll";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(ChangeDeptEntityVo changeDeptEntityVo) {
        boolean flag = iChangeDeptService.saveAdd(changeDeptEntityVo);
        if(!flag){
            return AjaxResult.error("该机构已添加,可修改！");
        }
        return AjaxResult.success("保存成功！");
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") String deptId, ModelMap mmap) {
        ChangeDeptEntityVo changeDeptEntityVo = iChangeDeptService.selectOne(deptId);
        mmap.put("dept", changeDeptEntityVo);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ChangeDeptEntityVo changeDeptEntityVo) {
        ChangeDeptEntity changeDeptEntity = new ChangeDeptEntity();
        BeanUtils.copyProperties(changeDeptEntityVo,changeDeptEntity);
        return toAjax(iChangeDeptService.saveOrUpdate(changeDeptEntity));
    }

    /**
     * 重置日期
     */
    @GetMapping("/replace")
    public String replace( ModelMap mmap) {
        return prefix + "/replace";
    }


    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") String deptId) {
        if (deptService.selectDeptCount(deptId) > 0) {
            return AjaxResult.warn("存在下级机构,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.warn("机构存在人员,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 获取配置日期
     * @param
     * @return
     */
    @PostMapping("/selectDate")
    @ResponseBody
    public AjaxResult selectDate() {
        AjaxResult ajaxResult = new AjaxResult();
        List<PubParaValue> pubParaValueList =iPubParaValueService.selectPubParaValueByParaName("change_replace");
        if(!CollectionUtils.isEmpty(pubParaValueList)){
            ajaxResult.put("num", pubParaValueList.get(0).getValue());
        }else{
            ajaxResult.put("flag", false);
        }
        return ajaxResult;
    }

    /**
     * 重置日期
     * @param deptId
     * @param reportTime
     * @return
     */
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult submit(String deptId, String reportTime) {
        ChangeDeptEntity changeDeptEntity = new ChangeDeptEntity();
        try{
            Integer day = Integer.parseInt(reportTime);
            if(day>28||day<1){
                return AjaxResult.warn("请输入1~28");
            }
        }catch (Exception e){
            return AjaxResult.warn("请输入正整数！");
        }
        changeDeptEntity.setReplaceTime(reportTime);
        List<ChangeDeptEntity> changeDeptEntityList = iChangeDeptService.list();
        changeDeptEntityList.stream().forEach(p->{
            QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", p.getOrgId());
            p.setReplaceTime(reportTime);
            iChangeDeptService.saveOrUpdate(p,queryWrapper);
        });
        return  AjaxResult.success("重置成功！");
    }

}
