package com.ruoyi.form.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.entity.DbUserAndTableMessageVO;
import com.ruoyi.form.entity.TwUserDb;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkOrder;
import com.ruoyi.form.service.ITwUserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Controller
@RequestMapping("/twUserDb")
public class TwUserDbController extends BaseController {

    @Autowired
    private ITwUserDbService twUserDbService;

    private final String prefix = "work/userDb";

    /**
     * 新增userDb
     */
    @GetMapping("/addUserDb/{id}")
    public String addUserDb(@PathVariable("id") Integer id, ModelMap modelMap) {
        modelMap.addAttribute("id", id);
        return prefix + "/addUser";
    }

    /**
     * 用户列表
     *
     * @return chw
     */
    @PostMapping("/getUserUserByWorkNodeId/{id}")
    @ResponseBody
    public TableDataInfo getUserUserByWorkNodeId(@PathVariable("id") String id) {
        startPage();
        List<TwUserDb> userDbByWorkNodeId = twUserDbService.getUserByWorkNodeId(id);
        return getDataTable(userDbByWorkNodeId);
    }


    /**
     * Db列表
     *
     * @return chw
     */
    @PostMapping("/getUserDbByWorkNodeId/{id}")
    @ResponseBody
    public TableDataInfo getUserDbByWorkNodeId(@PathVariable("id") String id) {
        startPage();
        List<TwUserDb> userDbByWorkNodeId = twUserDbService.getUserDbByWorkNodeId(id);
        return getDataTable(userDbByWorkNodeId);
    }
    /**
     * MySql列表
     *
     * @return chw
     */
    @PostMapping("/getUserSqlDbByWorkNodeId/{id}/{classify}/{dbType}")
    @ResponseBody
    public TableDataInfo getUserSqlDbByWorkNodeId(@PathVariable("id") String id,@PathVariable("classify") String classify,@PathVariable("dbType") String dbType) {
        startPage();
        List<TwUserDb> userDbByWorkNodeId = twUserDbService.getUserSqlDbByWorkNodeId(id,classify,dbType);
        return getDataTable(userDbByWorkNodeId);
    }
    /**
     * 根据工单id查询子node（服务列表）
     *
     * @return chw
     */
    @PostMapping("/getUserDbByWorkNodeId")
    @ResponseBody
    public TableDataInfo getUserDbByWorkNodeId(TwWorkNode twWorkNode) {
        //startPage();
        List<DbUserAndTableMessageVO> list = twUserDbService.getDbUserAndTableMessage(twWorkNode.getId());
        if (list == null) {
            list = new ArrayList<DbUserAndTableMessageVO>();
        }
        return getDataTable(list);
    }

    /**
     * 新增用户数据库相关信息
     */
    @Log(title = "新增服务器申请单服务设备", businessType = BusinessType.INSERT)
    @PostMapping("/addUserDb")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addUserDb(TwUserDb twUserDb) {
        twUserDb.setId(UUID.getUUIDStr());
        twUserDbService.save(twUserDb);
        return success("新增成功成功");
    }

    /**
     * 新增服务器申请单服务设备
     */
    @Log(title = "新增数据详细用户信息", businessType = BusinessType.INSERT)
    @PostMapping("/addUserDbForDetail")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addUserDbForDetail(DbUserAndTableMessageVO dbUserAndTableMessageVO) {
        twUserDbService.addUserDbForDetail(dbUserAndTableMessageVO);
        return success("新增成功成功");
    }

    /**
     * 修改用户数据库相关信息
     */
    @Log(title = "修改用户数据库相关信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult update(TwUserDb twWorkOrder) {
        TwUserDb twUserDb = twUserDbService.selectone(twWorkOrder);
        twWorkOrder.setWordNodeId(twUserDb.getWordNodeId());
        twUserDbService.updateById(twWorkOrder);
        return success("修改成功");
    }

    /**
     * 数据库
     */
    @Log(title = "修改用户数据库相关信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDb")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult updateDb(TwUserDb twWorkOrder) {
        TwUserDb twUserDb = twUserDbService.selectone(twWorkOrder);
        twWorkOrder.setWordNodeId(twUserDb.getWordNodeId());
        twUserDbService.updateById(twWorkOrder);
        return success("修改成功");
    }


    /**
     * 删除用户数据库相关信息
     */
    @Log(title = "删除用户数据库相关信息", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{id}")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult delete(@PathVariable("id")  String id) {
        twUserDbService.removeById(id);
        return success("删除成功");
    }
    @GetMapping("/deleteDb/{orderId}")
    @ResponseBody
    public AjaxResult deleteDb(@PathVariable("orderId")  String orderId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("word_node_id",orderId);
        twUserDbService.remove(queryWrapper);
        return success("删除成功");
    }


    /**
     * 删除数据库相关详细信息
     */
    @Log(title = "删除数据库相关详细信息", businessType = BusinessType.DELETE)
    @PostMapping("/deleteUserDbDetail/{id}/{onlyKey}")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult deleteUserDbDetail(@PathVariable("id") String id,@PathVariable("onlyKey") String onlyKey) {
        twUserDbService.deleteUserDbDetail(id,onlyKey);
        return AjaxResult.success("操作成功","");
    }
}

