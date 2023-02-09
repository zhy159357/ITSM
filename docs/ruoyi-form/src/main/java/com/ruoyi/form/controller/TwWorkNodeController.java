package com.ruoyi.form.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.entity.TwUserDb;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkOrder;
import com.ruoyi.form.service.ITwTaskDbService;
import com.ruoyi.form.service.ITwUserDbService;
import com.ruoyi.form.service.ITwWorkNodeService;
import com.ruoyi.form.service.impl.TwUserDbServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Controller
@RequestMapping("/twWorkNode")
public class TwWorkNodeController extends BaseController {

    private final String prefix = "work";

    private final String prefix_01 = "work/node";

    @Autowired
    private ITwWorkNodeService twWorkNodeService;

    @Autowired
    private ITwUserDbService twUserDbService;
    @Autowired
    private ITwTaskDbService iTwTaskDbService;

    /**
     * 跳转新增服务列表
     *
     * @return chw
     */
    @GetMapping("/addSever")
    public String addSever(@PathVariable("id") String id, ModelMap modelMap) {
        // TwWorkOrder twWorkOrder = twWorkOrderService.getById(id);
        // modelMap.addAttribute("twWorkOrder", twWorkOrder);
        return prefix + "/addSever";
    }

    @GetMapping("/editWorkDialog/{workOrderId}/{id}")
    public String editWorkDialog(@PathVariable("workOrderId") String workOrderId, @PathVariable("id") String id, ModelMap modelMap) {
        modelMap.put("workOrderId", workOrderId);
        modelMap.put("workNodeId", id);
        return prefix + "/editWorkDialog";
    }

    /**
     * 新增服务器申请单服务设备
     */
    @Log(title = "新增服务器申请单服务设备", businessType = BusinessType.INSERT)
    @PostMapping("/addOrUpdateSever")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addOrUpdateSever(TwWorkNode twWorkNode) {
        if (StringUtils.isNotEmpty(twWorkNode.getNodeType())) {
            twWorkNode.setClassify(twWorkNode.getNodeType());
        }
        twWorkNodeService.insert(twWorkNode);
        return AjaxResult.success("操作成功", twWorkNode.getId());
    }

    /**
     * 新增服务器申请单服务设备
     */
    @Log(title = "新增服务器申请单服务设备", businessType = BusinessType.INSERT)
    @PostMapping("/updateUserSever")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult updateUserSever(TwWorkNode twWorkNode) {
        if (StringUtils.isNotEmpty(twWorkNode.getNodeType())) {
            twWorkNode.setClassify(twWorkNode.getNodeType());
        }
        twWorkNodeService.update(twWorkNode);
        return AjaxResult.success("操作成功", twWorkNode.getId());
    }

    /**
     * 新增服务器申请单服务设备
     */
    @Log(title = "修改服务器申请单服务设备", businessType = BusinessType.UPDATE)
    @PostMapping("/updateSever")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult updateSever(TwWorkNode twWorkNode) {
        twWorkNodeService.updateById(twWorkNode);
        return success("修改成功");
    }

    /**
     * 新增服务器申请单服务设备
     */
    @Log(title = "提交或者暂存流程", businessType = BusinessType.UPDATE)
    @PostMapping("/saveOrSubmit/{type}/{workNum}")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveOrSubmit(@PathVariable("type") Integer type, @PathVariable("workNum") String workNum) {
        return success("新增成功成功");
    }

    /**
     * 根据工单id查询子node（服务列表）
     *
     * @return chw
     */
    @GetMapping("/getWorkNodeByWorkOrderId/{id}")
    @ResponseBody
    public AjaxResult getWorkNodeByWorkOrderId(@PathVariable("id") String id) {
        List<TwWorkNode> workNodeByWorkOrderId = twWorkNodeService.getWorkNodeByWorkOrderId(id);
        workNodeByWorkOrderId.stream().forEach(p -> {
            if (p.getNodeType().equals("1")) {
                p.setNodeType("应用服务类");
            }
            if (p.getNodeType().equals("2")) {
                p.setNodeType("数据库类");
            }
        });
        return AjaxResult.success(workNodeByWorkOrderId);
    }

    /**
     * 跳转编辑服务列表详细页面
     *
     * @param
     * @return chw
     */
    @GetMapping("/addType/{orderId}")
    public String addType(@PathVariable("orderId") String orderId, ModelMap modelMap) {
        modelMap.addAttribute("orderId", orderId);
        modelMap.addAttribute("nodeId", UUID.getUUIDStr());
        return prefix + "/addType";
    }

    @GetMapping("/edit/{orderId}")
    public String editType(@PathVariable("orderId") String orderId, ModelMap modelMap) {
        modelMap.addAttribute("orderId", orderId);
        TwWorkNode twWorkNode = twWorkNodeService.getById(orderId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("word_node_id",orderId);
        List<TwUserDb> twUserDbList = twUserDbService.list(queryWrapper);
        List<String> stringList = twUserDbList.stream().map(p->p.getDbType()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(stringList) && stringList.contains("1")){
            twWorkNode.setDbType("1");
        }else if(!CollectionUtils.isEmpty(stringList) && stringList.contains("2")){
            twWorkNode.setDbType("2");
        }else{
            twWorkNode.setDbType("3");
        }
        modelMap.put("twWorkNode", twWorkNode);
        modelMap.put("orderId", orderId);
        return prefix + "/editType";
    }

    @GetMapping("/view/{orderId}")
    public String view(@PathVariable("orderId") String orderId, ModelMap modelMap) {
        modelMap.addAttribute("orderId", orderId);
        TwWorkNode twWorkNode = twWorkNodeService.getById(orderId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("word_node_id",orderId);
        List<TwUserDb> twUserDbList = twUserDbService.list(queryWrapper);
        List<String> stringList = twUserDbList.stream().map(p->p.getDbType()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(stringList) && stringList.contains("1")){
            twWorkNode.setDbType("1");
        }else if(!CollectionUtils.isEmpty(stringList) && stringList.contains("2")){
            twWorkNode.setDbType("2");
        }else{
            twWorkNode.setDbType("3");
        }
        modelMap.put("twWorkNode", twWorkNode);
        modelMap.put("orderId", orderId);
        return prefix + "/viewAll";
    }


    /**
     * 根据工单id查询子node（服务列表）
     *
     * @return chw
     */
    @PostMapping("/getWorkNodeByWorkOrderIdAndNodeType/{id}")
    @ResponseBody
    public TableDataInfo getWorkNodeByWorkOrderIdAndNodeType(@PathVariable("id") Integer id) {
        startPage();
        ArrayList<TwWorkNode> twWorkNodes = new ArrayList<>();
        TwWorkNode twWorkNode = twWorkNodeService.getById(id);
        twWorkNodes.add(twWorkNode);
        return getDataTable(twWorkNodes);
    }

    /**
     * 新增userDb
     */
    @GetMapping("/add/{orderId}/{type}")
    public String add(@PathVariable("orderId") String id, @PathVariable("type") String type, ModelMap mmap) {
        //mmap.addAttribute("nodeType",nodeType);
        mmap.addAttribute("id", id);
        mmap.addAttribute("type", type);
        return prefix_01 + "/addService";
    }

    /**
     * 新增
     *
     * @param id
     * @param type   节点类型
     * @param dbType 数据库类型  * @param mmap
     * @return
     */
    @GetMapping("/addDb/{id}/{type}/{dbType}")
    public String addDb(@PathVariable("id") String id, @PathVariable("type") String type, @PathVariable("dbType") String dbType, ModelMap mmap) {
        //mmap.addAttribute("nodeType",nodeType);
        mmap.addAttribute("id", id);
        mmap.addAttribute("type", type);
        mmap.addAttribute("dbType", dbType);
        return prefix_01 + "/addDbMySqlService";
    }

    /**
     * 新增
     *
     * @param id
     * @param type   节点类型
     * @param dbType 数据库类型  * @param mmap
     * @return
     */
    @GetMapping("/addOracleDb/{id}/{type}/{dbType}")
    public String addOracleDb(@PathVariable("id") String id, @PathVariable("type") String type, @PathVariable("dbType") String dbType, ModelMap mmap) {
        //mmap.addAttribute("nodeType",nodeType);
        mmap.addAttribute("id", id);
        mmap.addAttribute("type", type);
        mmap.addAttribute("dbType", dbType);
        return prefix_01 + "/addOracleService";
    }


    /**
     * 新增userDb
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, ModelMap mmap) {
        TwUserDb twUserDb = twUserDbService.getById(id);
        mmap.addAttribute("id", id);
        mmap.addAttribute("twUserDb", twUserDb);
        return prefix_01 + "/updateService";
    }

    /**
     * 跳转编辑服务列表详细页面
     *
     * @return chw
     */
    @GetMapping("/copy/{id}")
    @ResponseBody
    public AjaxResult copy(@PathVariable("id") String id) {
        twWorkNodeService.copy(id);
        return AjaxResult.success("复制成功");

    }

    /**
     * 根据id删除 节点
     *
     * @return chw
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxResult delete(@PathVariable("id") String id) {
        twWorkNodeService.delete(id);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("word_node_id", id);
        twUserDbService.remove(queryWrapper);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("work_node_id", id);
        iTwTaskDbService.remove(qw);

        return AjaxResult.success("删除成功");

    }


}

