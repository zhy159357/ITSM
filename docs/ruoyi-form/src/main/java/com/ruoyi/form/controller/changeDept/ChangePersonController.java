package com.ruoyi.form.controller.changeDept;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.ChangeDeptEntityVo;
import com.ruoyi.form.domain.ChangeDeptPersonEntity;
import com.ruoyi.form.domain.ChangeDeptPersonEntityVo;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: ruoyi
 * @description: 机构负责人和机构分管领导配置
 * @author: ma zy
 * @date: 2022-10-04 09:13
 **/
@Controller
@RequestMapping("/changePerson")
public class ChangePersonController extends BaseController {


    private String prefix = "changePerson";

    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IChangePersonService iChangePersonService;

    @Autowired
    IOgPersonService ogPersonService;

    /**
     * 变更单列表
     */
    @GetMapping("/deptList")
    public String add(ModelMap mmap) {
        return prefix + "/deptPersonList";
    }

    /**
     * 额度和机构列表展示
     *
     * @param dept
     * @return
     */
    @PostMapping("/listDept")
    @ResponseBody
    public TableDataInfo listDept(OgOrg dept) {
        List<ChangeDeptPersonEntityVo> deptList = iChangePersonService.listAll(dept);
        return getDataTable(deptList);
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") String parentId, ModelMap mmap) {
        OgOrg org = deptService.selectDeptById(parentId);
        if (StringUtils.isNotEmpty(org.getOrgLv())) {
            org.setOrgLv(String.valueOf(Integer.valueOf(org.getOrgLv()) + 1));
        }
        mmap.put("dept", org);
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(ChangeDeptPersonEntityVo changeDeptPersonEntityVo) {
        boolean  result=true;
        try {
            iChangePersonService.saveAdd(changeDeptPersonEntityVo);

        } catch (Exception e) {
            result=false;
        e.printStackTrace();
    }
        if(result){
            return AjaxResult.success("保存成功！");
        }
        return AjaxResult.warn("请先完善本机构的定制机构树配置");
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        ChangeDeptPersonEntityVo changeDeptPersonEntityVo = iChangePersonService.selectOne(id);
        mmap.put("dept", changeDeptPersonEntityVo);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ChangeDeptPersonEntityVo changeDeptPersonEntityVo) {
        ChangeDeptPersonEntity changeDeptPersonEntity = new ChangeDeptPersonEntity();
        BeanUtils.copyProperties(changeDeptPersonEntityVo, changeDeptPersonEntity);
        return toAjax(iChangePersonService.saveOrUpdate(changeDeptPersonEntity));
    }

    /**
     * 跳转选人页面
     */
    @GetMapping("/choosePerson/{type}")
    public String choosePerson(@PathVariable("type") String type, ModelMap mmap) {
        mmap.put("type", type);
        return prefix + "/choosePerson";
    }

    /**
     * 跳转选人页面
     */
    @GetMapping("/chooseAccount")
    public String chooseAccount( ModelMap mmap) {
        return prefix + "/chooseAccount";
    }

    /**
     * 查询协同评估人
     */
    @PostMapping("/selectMultiusers")
    @ResponseBody
    public TableDataInfo selectMultiusers(OgUser users) {
        startPage();
        OgUser user = new OgUser();
        if(StringUtils.isNotEmpty(users.getOrgname())){
            user.setOrgname(users.getOrgname());
        }
        if(StringUtils.isNotEmpty(users.getPname())){
            user.setPname(users.getPname());
        }
        List<OgUser> list = userService.selectAllocatedListAll(user);
        return getDataTable(list);
    }
    /**
     * 选择部门树
     *
     */
    @GetMapping(value = "/selectDeptTree")
    public String selectDeptTree() {
        return prefix + "/tree";
    }
}
