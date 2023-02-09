package com.ruoyi.form.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.constants.ProblemConstant;
import com.ruoyi.form.domain.ProblemSheet;
import com.ruoyi.form.enums.ProblemStatus;
import com.ruoyi.form.service.IProblemSheetService;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgTypeinfoService;
import com.ruoyi.system.service.ISysWorkService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.ProblemConstant.*;
import static com.ruoyi.form.constants.ProblemFlowConstants.*;

/**
 * 问题单Controller
 *
 * @author ruoyi
 * @date 2022-06-21
 */
@Controller
@RequestMapping("/problem/sheet")
public class ProblemSheetController extends BaseController {
    private static final String PREFIX_IMP = "IMP";
    /**问题单新建、编辑、列表**/
    private String prefix_sheet = "problem/sheet";

    /**问题单分派**/
    private String prefix_assign = "problem/assign";

    /**问题单接收**/
    private String prefix_receive = "problem/receive";

    /**问题单处理**/
    private String prefix_deal = "problem/deal";

    /**问题单原因调查**/
//    private String prefix_solution_pre = "problem/solution/pre";

    /**制定解决方案**/
    private String prefix_solution = "problem/solution";

    /**问题解决确认**/
    private String prefix_confirm = "problem/confirm";

    /**
     * 问题解决管理员确认
     */
    private String prefix_manage_confirm = "problem/confirm/manage";

    /**事件单关闭**/
    private String prefix_close = "problem/close";

    @Autowired
    private IProblemSheetService problemSheetService;

    @Autowired
    private IIdGeneratorService generatorService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    @Autowired
    private IOgPersonService ogPersonService;


    @GetMapping()
    public String sheet() {
        return prefix_sheet + "/sheet";
    }

    /**问题单待办列表*/
    @GetMapping("/assign")
    public String assign()
    {
        return prefix_assign + "/list";
    }

    /**问题单领取*/
    @GetMapping("/receive")
    public String receive()
    {
        return prefix_receive + "/list";
    }

    /**问题单处理*/
    @GetMapping("/deal")
    public String deal()
    {
        return prefix_deal + "/list";
    }

    /**问题单解决*/
    @GetMapping("/solution")
    public String solution()
    {
        return prefix_solution + "/list";
    }

    /**问题单确认*/
    @GetMapping("/confirm")
    public String confirm()
    {
        return prefix_confirm + "/list";
    }

    /**
     * 查询问题单列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProblemSheet problemSheet) {
        startPage();
        List<ProblemSheet> list = problemSheetService.selectProblemSheetList(problemSheet);
        return getDataTable(list);
    }

    /**
     * 导出问题单列表
     */

    @Log(title = "问题单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProblemSheet problemSheet) {
        List<ProblemSheet> list = problemSheetService.selectProblemSheetList(problemSheet);
        ExcelUtil<ProblemSheet> util = new ExcelUtil<ProblemSheet>(ProblemSheet.class);
        return util.exportExcel(list, "sheet");
    }

    /**
     * 新增问题单
     */
    @GetMapping("/add")
    public String add(ModelMap map) {
        String no = generatorService.createNoAsLength(PREFIX_IMP, 3);
        String userId = ShiroUtils.getUserId();
        OgPerson op = iOgPersonService.selectOgPersonById(userId);
        // 查询发起人所有部室经理
        OgPerson ogPersonQuery = new OgPerson();
        ogPersonQuery.setOrgId(op.getOrgId());
        List<OgPerson> ogPeopleList = ogPersonService.selectOgPersonAddList(ogPersonQuery);
        List<OgPerson> leaderPerson = ogPeopleList.stream().filter(ogPerson ->StringUtils.equals("1", ogPerson.getLeader())).collect(Collectors.toList());
        List<OgTypeinfo> problemCauseList = ogTypeinfoService.selectOgTypeInfoByEvent("root_cause", "1");
        map.put("problemCauseList", JSON.toJSONString(problemCauseList));
        List<OgTypeinfo> problemCategoryList = ogTypeinfoService.selectOgTypeInfoByEvent("problemsheet_category", "1");
        map.put("problemCategoryList", JSON.toJSONString(problemCategoryList));
        map.put(NUM, no);
        map.put(PROBLEM_ORIGINATOR, userId);
        map.put(PROBLEM_ORIGINATOR_NAME, op.getpName());
        map.put(PROBLEM_ORIGINATE_DEPART_MANAGER, leaderPerson.get(0).getpId());
        map.put(PROBLEM_ORIGINATE_DEPART_MANAGER_NAME, leaderPerson.get(0).getpName());
        return prefix_sheet + "/add";
    }

    /**
     * 分派页面
     *
     * @param problemId
     * @param taskId
     * @param map
     * @return
     */
    @GetMapping("/assignPage/{problemId}/{taskId}")
    public String assignPage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheetService.translateCatType(problemSheet);
        problemSheet.getParams().put(TASK_ID, taskId);
        map.put(PROBLEM_SHEET, problemSheet);
        return prefix_assign + "/assign";
    }

    /**问题单接收页面*/

    @GetMapping("/receivePage/{problemId}/{taskId}")
    public String receivePage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map) {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheet.setStatus(ProblemStatus.UNDER_INVESTIGATION.getCode());
        problemSheet.setStage(ProblemStatus.stageMap.get(problemSheet.getStatus()));
        problemSheetService.updateProblemSheet(problemSheet);
        problemSheet.getParams().put(TASK_ID, taskId);
        List<OgTypeinfo> problemCauseList = ogTypeinfoService.selectOgTypeInfoByEvent("root_cause", "1");
        problemSheetService.translateCatType(problemSheet);
        problemSheet.getParams().put("problemCauseList", JSON.toJSONString(problemCauseList));
        map.put(PROBLEM_SHEET, problemSheet);
        return prefix_receive + "/receive";
    }

    /**
     * 新增保存或提交问题单
     */

    @Log(title = "问题单", businessType = BusinessType.INSERT)
    @PostMapping("/saveOrSubmit")
    @ResponseBody
    public AjaxResult addSave(ProblemSheet problemSheet) {
        String remark = problemSheet.getRemark();
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        // 暂存
        if (ProblemConstant.Remark.SAVE.equals(remark)) {
            // 新增
            if (StringUtils.isEmpty(problemSheet.getProblemId())) {
                problemSheet.setProblemId(UUID.getUUIDStr());
                problemSheet.setStatus(ProblemStatus.WAIT_SUBMIT.getCode());
                problemSheet.setStage(ProblemStatus.stageMap.get(problemSheet.getStatus()));
                problemSheetService.insertProblemSheet(problemSheet);
            } else {
                // 编辑后暂存
                problemSheet.setCreateBy(ShiroUtils.getUserId());
                problemSheetService.updateProblemSheet(problemSheet);
            }
            // 提交
        } else {
            // 提交操作需要启动问题单流程
            Map<String, Object> variables = new HashMap<>();
            // 合规审核
            if (StringUtils.isBlank(problemSheet.getProblemSolver())) {
                variables.put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_ZERO);
                // 角色id固定值,todo 管理员默认为当前登录人
//                String roleId = "8b8080f457fffe39015800015ce60006";// 管理员角色id 测试先写成固定值
                variables.put(COMPLIANCE_ID, ShiroUtils.getUserId());
                problemSheet.setStatus(ProblemStatus.COMPLIANCE_AUDIT.getCode());
                // 已知错误,根因已明解决方案未定
            } else {
                variables.put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_POSITIVE1);
                variables.put(PROBLEM__FLOW_NODE_KEY_SOLVER, problemSheet.getProblemSolver());
                problemSheet.setStatus(ProblemStatus.SOLUTION_PENDING.getCode());
            }
            problemSheet.setStage(ProblemStatus.stageMap.get(problemSheet.getStatus()));
            // 直接提交
            if (StringUtils.isEmpty(problemSheet.getProblemId())) {
                problemSheet.setProblemId(UUID.getUUIDStr());
                problemSheetService.insertProblemSheet(problemSheet);
                // 编辑后提交
            } else {
                problemSheetService.updateProblemSheet(problemSheet);
            }
            // 启动流程
            BmpEntity bmpEntity = problemSheetService.startProcess(problemSheet.getProblemId(), variables);
            if (bmpEntity.isSuccessFlag()) {
                logger.info("问题单流程启动成功，问题单编号 problemNo:{}，流程实例 processInstanceId:{}", problemSheet.getProblemNo(), bmpEntity.getProcessInstanceId());
            }
        }
        return AjaxResult.success("操作成功", problemSheet.getProblemId());
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{problemId}")
    public String detail(@PathVariable("problemId") String problemId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheetService.translateCatType(problemSheet);

        map.put("problemSheet", problemSheet);
        return prefix_sheet + "/detail";
    }


    /**
     * 问题单查询待办
     * @param problemSheet
     * @return
     */
    @PostMapping("/problemAgencyList")
    @ResponseBody
    public TableDataInfo problemAgencyList(ProblemSheet problemSheet) {
        String description = (String) problemSheet.getParams().get("description");
        List<ProblemSheet> list = problemSheetService.selectProblemAgencyList("bizIMP", description, problemSheet);
        return getDataTable_ideal(list);
    }

    /**
     * 修改问题单
     */
    @GetMapping("/edit/{problemId}")
    public String edit(@PathVariable("problemId") String problemId, ModelMap mmap) {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        if(problemSheet == null) {
            throw new BusinessException("问题单不存在!");
        }
        // 关闭或取消的问题单不允许再编辑 管理员可以在关闭或取消前任意环节更新问题单,发起人只能在草稿阶段编辑
        List<String> statusList = Lists.newArrayList();
        statusList.add(ProblemStatus.CANCEL.getCode());
        statusList.add(ProblemStatus.CLOSE.getCode());
        if(statusList.contains(problemSheet.getStatus())) {
            throw new BusinessException("该问题单已取消或已关闭，不允许再编辑!");
        }
//        String userId = ShiroUtils.getUserId();
//        // todo 管理员角色
//        if (!userId.equals("管理员")
//                && !(Arrays.asList(userId, "管理员").contains(problemSheet.getCreateBy())
//                && ProblemStatus.WAIT_SUBMIT.getCode().equals(problemSheet.getStatus()))
//        ) {
//            throw new BusinessException("管理员或问题单发起人才有权限编辑!");
//        }
//        problemSheetService.translateCatType(problemSheet);
        mmap.put("problemSheet", problemSheet);
        return prefix_sheet + "/edit";
    }

    /**
     * 修改保存问题单
     */

    @Log(title = "问题单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ProblemSheet problemSheet) {
        return toAjax(problemSheetService.updateProblemSheet(problemSheet));
    }

    /**
     * 取消事件单
     */
    @PostMapping( "/cancel")
    @ResponseBody
    public AjaxResult cancel(String id)
    {
        problemSheetService.cancelProblemSheetById(id);
        return success();
    }

    /**
     * 关闭问题单
     *
     * @param id
     * @return
     */
    @PostMapping( "/closeProblem")
    @ResponseBody
    public AjaxResult closeProblem(String id)
    {
        problemSheetService.closeProblem(id);
        return success();
    }

    /**
     * 接单领取环节
     * @param problemSheet
     * @return
     */
    @PostMapping("/receiveProblem")
    @ResponseBody
    public AjaxResult receiveEvent(ProblemSheet problemSheet) {
        problemSheetService.receiveProblem(problemSheet);
        return AjaxResult.success();
    }

    /**查询问题牵头部室页面*/
    @GetMapping("/assignedGroup/{problemId}")
    public String assignedGroup(@PathVariable("problemId") String problemId, ModelMap map)
    {
        map.put("problemId", problemId);
        return prefix_assign + "/assignedGroup";
    }

    /**查询问题牵头部室页面*/
    @GetMapping("/assignedGroup")
    public String assignedGroup(ModelMap map)
    {
        return prefix_assign + "/assignedGroup";
    }


    /**查询受派人页面*/
    @GetMapping("/assignedPerson/{groupId}")
    public String assignedPerson(@PathVariable("groupId") String groupId, ModelMap map)
    {
        map.put("groupId", groupId);
        return prefix_assign + "/assignedPerson";
    }

//    /**接单环节领取页面*/
//    @GetMapping("/receivePage/{eventId}/{taskId}")
//    public String receivePage(@PathVariable("eventId") String eventId, @PathVariable("taskId") String taskId, ModelMap map)
//    {
//        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
//        eventSheet.getParams().put("taskId", taskId);
//        map.put("eventSheet", eventSheet);
//        return prefix_receive + "/receive";
//    }
//
    /**处理环节页面*/
    @GetMapping("/dealPage/{problemId}/{taskId}")
    public String dealPage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheetService.translateCatType(problemSheet);
        problemSheet.getParams().put("taskId", taskId);
        List<OgTypeinfo> problemCauseList = ogTypeinfoService.selectOgTypeInfoByEvent("root_cause", "1");
        map.put("problemCauseList", JSON.toJSONString(problemCauseList));
        map.put("problemSheet", problemSheet);
        return prefix_deal + "/deal";
    }

    /**解决环节页面*/
    @GetMapping("/solutionPage/{problemId}/{taskId}")
    public String solutionPage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheetService.translateCatType(problemSheet);
        problemSheet.getParams().put("taskId", taskId);
        map.put("problemSheet", problemSheet);
        return prefix_solution + "/solution";
    }

    /**确认环节页面*/
    @GetMapping("/confirmPage/{problemId}/{taskId}")
    public String confirmPage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheetService.translateCatType(problemSheet);
        problemSheet.getParams().put("taskId", taskId);
        map.put("problemSheet", problemSheet);
        return prefix_confirm + "/confirm";
    }
    /**管理员确认环节页面*/
    @GetMapping("/manageConfirmPage/{problemId}/{taskId}")
    public String manageConfirmPage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheetService.translateCatType(problemSheet);
        problemSheet.getParams().put("taskId", taskId);
        map.put("problemSheet", problemSheet);
        return prefix_manage_confirm + "/confirm";
    }

    /**一线关闭评价页面*/
    @GetMapping("/evaluatePage/{problemId}/{taskId}")
    public String evaluatePage(@PathVariable("problemId") String problemId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        ProblemSheet problemSheet = problemSheetService.selectProblemSheetById(problemId);
        problemSheet.getParams().put("taskId", taskId);
        map.put("problemSheet", problemSheet);
        return prefix_close + "/evaluate";
    }

    /**
     * 问题单分派环节
     * @param problemSheet
     * @return
     */
    @PostMapping("/assignProblem")
    @ResponseBody
    public AjaxResult assignProblem(ProblemSheet problemSheet) {
        problemSheetService.assignProblem(problemSheet);
        return AjaxResult.success();
    }

    /**
     * 问题单退回
     */
    @PostMapping("/returnProblem")
    @ResponseBody
    public AjaxResult returnProblem(ProblemSheet problemSheet) {
        problemSheetService.returnProblem(problemSheet);
        return AjaxResult.success();
    }


    /**
     * 处理问题单
     *
     * @param problemSheet
     * @return
     */
    @PostMapping("/dealProblem")
    @ResponseBody
    public AjaxResult dealEvent(ProblemSheet problemSheet) {
//        problemSheet.setDealId(ShiroUtils.getUserId());
        problemSheetService.dealProblem(problemSheet);
        return AjaxResult.success();
    }

    /**
     * 问题单解决环节
     *
     * @param problemSheet
     * @return
     */
    @PostMapping("/solutionProblem")
    @ResponseBody
    public AjaxResult solutionProblem(ProblemSheet problemSheet) {
        problemSheetService.solutionProblem(problemSheet);
        return AjaxResult.success();
    }

    /**
     * 问题单确认
     *
     * @param problemSheet
     * @return
     */
    @PostMapping("/confirmProblem")
    @ResponseBody
    public AjaxResult confirmProblem(ProblemSheet problemSheet) {
        problemSheetService.confirmProblem(problemSheet);
        return AjaxResult.success();
    }

    /**
     * 查询组
     * @param group
     * @return
     */
    @PostMapping("/selectAssignedGroup")
    @ResponseBody
    public TableDataInfo selectAssignedGroup(OgGroup group) {
        Map<String, Object> map = new HashMap<>();
        map.put("grpName", group.getGrpName());
        map.put("userId", ShiroUtils.getUserId());
        startPage();
        List<OgGroup> ogGroups = workService.selectProblemGroupByGposition(map);
        return getDataTable(ogGroups);
    }

    /**
     * 事件单查询受派人
     * @param person
     * @return
     */
    @PostMapping("/selectAssignedPerson")
    @ResponseBody
    public TableDataInfo selectAssignedPerson(OgGroupPerson person) {
        startPage();
        List<OgGroupPerson> personList = workService.selectOgGroupPersonList(person);
        List<OgPerson> pList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(personList)) {
            pList = personList.stream().map(p -> {
                return p.getPerson();
            }).collect(Collectors.toList());
        }
        return getDataTable(pList);
    }
}
