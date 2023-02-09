package com.ruoyi.form.controller.bmp;

import com.ruoyi.activiti.bmp.service.IAcitivtiConfigService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程配置相关controller
 */
@RequestMapping("/bmp/config")
@Controller
public class BmpConfigController extends BaseController {

    private final String prefix = "listener";

    @Autowired
    private IAcitivtiConfigService acitivtiConfigService;

    @GetMapping("/listener")
    public String listener() {
        return prefix + "/listener";
    }

    /**
     * 查询所有监听器
     * @param params
     * @return
     */
    @PostMapping("/selectActivityListeners")
    @ResponseBody
    public TableDataInfo selectActivityListeners(String listenerName) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<Map<String, Object>> listeners = acitivtiConfigService.getActivityListeners(listenerName);
        return getDataTable_app(listeners, pageNum.toString(), pageSize.toString());
    }

    /**
     * 查询所有流程用户
     */
    @PostMapping("/selectActivityUsers")
    @ResponseBody
    public TableDataInfo selectActivityUsers(Integer pageNum, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        List<Map<String, Object>> users = acitivtiConfigService.selectActivityUsers(params);
        return getDataTable_app(users, pageNum.toString(), pageSize.toString());
    }

    /**
     * 查询所有流程用户
     */
    @PostMapping("/selectActivityGroups")
    @ResponseBody
    public TableDataInfo selectActivityGroups(Integer pageNum, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        List<Map<String, Object>> groups = acitivtiConfigService.selectActivityGroups(params);
        return getDataTable_app(groups, pageNum.toString(), pageSize.toString());
    }

    /**
     * activiti编辑器查询所有表单编号
     * @return
     */
    @PostMapping("/selectFromKeyList")
    @ResponseBody
    public TableDataInfo selectFromKeyList() {
        String[] s = {"userForm","leaveForm","workForm"};
        return getDataTable(Arrays.asList(s));
    }

    @PostMapping("/selectUserGroupByIds")
    @ResponseBody
    public AjaxResult selectUserGroupByIds(String ids, Integer type) {
        String names = acitivtiConfigService.selectUserGroupByIds(ids, type);
        return AjaxResult.success((Object) names);
    }

    @PostMapping("/showUserGroupName")
    @ResponseBody
    public AjaxResult showUserGroupName(String assignee, String candidateUser, String candidateGroup) {
        Map<String, String> nameMap = acitivtiConfigService.showUserGroupName(assignee, candidateUser, candidateGroup);
        return AjaxResult.success(nameMap);
    }
}
