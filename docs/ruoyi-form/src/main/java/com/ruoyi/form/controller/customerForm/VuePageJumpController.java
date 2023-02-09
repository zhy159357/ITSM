package com.ruoyi.form.controller.customerForm;

import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName VuePageJump
 * @Description 用于跳转相应的vue页面前端
 * @Author JiaQi Zhang
 * @Date 2022/7/19 13:41
 */
@Controller
public class VuePageJumpController {

    @Value("${vueIndex}")
    private String vueIndex;


    @RequestMapping("/vueIndex/{code}")
    @ApiOperation("跳转到相应的vue业务列表页面")
    public String redirect(@PathVariable String code) {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/processlist/"+code+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/formBiz")
    @ApiOperation("跳转到相应的vue业务列表页面")
    public String redirectFormBiz() {
        return StringUtils.format("redirect:{}", vueIndex+"/processlist/");
    }

    @RequestMapping("/vueIndex/fieldStatus/{code}")
    @ApiOperation("跳转到对应业务的流程设计页面")
    public String redirectFieldStatus(@PathVariable String code) {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/activiti-analysis/"+code+"?CurrentUserId="+userId);
    }


    @RequestMapping("/vueIndex/customerFormVue")
    @ApiOperation("跳转到对应业务的流程设计页面")
    public String redirectCustomerFormVue() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/pagelist"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/datasource")
    @ApiOperation("跳转到对应业务的流程设计页面")
    public String redirectDatasource() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/datasource"+"?CurrentUserId="+userId);
    }
    @RequestMapping("/vueIndex/alignList")
    @ApiOperation("排班发布页面")
    public String alignList() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/align-list"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/comprehensiveQuery")
    @ApiOperation("综合查询页面")
    public String redirectComprehensiveQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/comprehensiveQuery"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/index")
    @ApiOperation("首页工作台")
    public String redirectIndex() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/dashboard"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/change/query")
    @ApiOperation("跳转到变更单统计页面")
    public String redirectChangeQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/changeQuery"+"?CurrentUserId="+userId);
    }
    @RequestMapping("/vueIndex/queuelist")
    @ApiOperation("跳转到排班页面")
    public String queuelist() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/queue-list"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/eventConsole")
    @ApiOperation("跳转到事件控制台")
    public String eventConsole() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/event-dashboard"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/eventQuery")
    @ApiOperation("事件单检索")
    public String eventQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/event-query"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/changeQuery")
    @ApiOperation("变更单检索页面")
    public String changeQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/change-query"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/changeConsole")
    @ApiOperation("变更单控制台")
    public String changeConsole() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/change-dashboard"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/changeTaskQuery")
    @ApiOperation("变更任务单检索")
    public String changeTaskQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/change-task-query"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/problemQuery")
    @ApiOperation("问题单检索")
    public String problemQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/problem-query"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/problemConsole")
    @ApiOperation("问题单控制台")
    public String problemConsole() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/problem-dashboard"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/problemTaskQuery")
    @ApiOperation("问题任务单检索")
    public String problemTaskQuery() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/problem-task-query"+"?CurrentUserId="+userId);
    }

    @RequestMapping("/vueIndex/addProblem")
    @ApiOperation("问题单新增页面")
    public String addProblem() {
        String userId = ShiroUtils.getUserId();
        return StringUtils.format("redirect:{}", vueIndex+"/add-process-list"+"?Code=problem&CurrentUserId="+userId);
    }
}
