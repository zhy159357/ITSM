package com.ruoyi.form.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.constants.EventFlowConstants;
import com.ruoyi.form.domain.EventSheet;
import com.ruoyi.form.enums.EventStatusEnum;
import com.ruoyi.form.service.IEventSheetService;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgTypeinfoService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件单Controller
 * 
 * @author zc
 * @date 2022-06-07
 */
@Controller
@RequestMapping("/event/sheet")
public class EventSheetController extends BaseController
{
    /**事件单新建、编辑、列表**/
    private String prefix_sheet = "event/sheet";

    /**事件单退回补全**/
    private String prefix_supply = "event/supply";

    /**事件单IT服务台**/
    private String prefix_assign = "event/assign";

    /**事件单接单领取**/
    private String prefix_receive = "event/receive";

    /**事件单处理**/
    private String prefix_deal = "event/deal";

    /**事件单二线解决**/
    private String prefix_solution_pre = "event/solution/pre";

    /**事件单一线解决**/
    private String prefix_solution_check = "event/solution/check";

    /**事件单关闭**/
    private String prefix_close = "event/close";

    /**事件单查询**/
    private String prefix_search = "event/search";

    @Autowired
    private IEventSheetService eventSheetService;

    @Autowired
    private IIdGeneratorService generatorService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    @Autowired
    private IOgPersonService personService;

    /**事件单新建列表*/
    @GetMapping()
    public String sheet(ModelMap map)
    {
        selectFistCategoryType(map);
        return prefix_sheet + "/list";
    }

    /**事件单退回补全*/
    @GetMapping("/supply")
    public String supply(ModelMap map)
    {
        return prefix_supply + "/list";
    }

    /**事件单IT服务台待办列表*/
    @GetMapping("/assign")
    public String assign()
    {
        return prefix_assign + "/list";
    }

    /**事件单接单领取*/
    @GetMapping("/receive")
    public String receive()
    {
        return prefix_receive + "/list";
    }

    /**事件单处理*/
    @GetMapping("/deal")
    public String deal()
    {
        return prefix_deal + "/list";
    }

    /**事件单二线解决*/
    @GetMapping("/pre/solution")
    public String preSolution()
    {
        return prefix_solution_pre + "/list";
    }

    /**事件单一线解决*/
    @GetMapping("/check/solution")
    public String checkSolution()
    {
        return prefix_solution_check + "/list";
    }

    /**事件单关闭评价*/
    @GetMapping("/close")
    public String close()
    {
        return prefix_close + "/list";
    }

    /**事件单查询*/
    @GetMapping("/search")
    public String search(ModelMap map)
    {
        selectFistCategoryType(map);
        return prefix_search + "/list";
    }

    /**事件单IT服务台分派页面*/
    @GetMapping("/assignPage/{eventId}/{taskId}")
    public String assignPage(@PathVariable("eventId") String eventId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.getParams().put("taskId", taskId);
        map.put("eventSheet", eventSheet);
        return prefix_assign + "/assign";
    }

    /**查询受派组页面*/
    @GetMapping("/assignedGroup/{eventId}")
    public String assignedGroup(@PathVariable("eventId") String eventId, ModelMap map)
    {
        map.put("eventId", eventId);
        return prefix_assign + "/assignedGroup";
    }

    /**查询受派人页面*/
    @GetMapping("/assignedPerson/{groupId}")
    public String assignedPerson(@PathVariable("groupId") String groupId, ModelMap map)
    {
        map.put("groupId", groupId);
        return prefix_assign + "/assignedPerson";
    }

    /**查询二线转派人页面*/
    @GetMapping("/preSolutionName/{eventId}")
    public String preSolutionName(@PathVariable("eventId") String eventId, ModelMap map)
    {
        map.put("eventId", eventId);
        return prefix_deal + "/preSolutionName";
    }

    /**事件单补全页面*/
    @GetMapping("/supplyPage/{eventId}/{taskId}")
    public String supplyPage(@PathVariable("eventId") String eventId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.getParams().put("taskId", taskId);
        map.put("eventSheet", eventSheet);
        return prefix_supply + "/supply";
    }

    /**接单环节领取页面*/
    @GetMapping("/receivePage/{eventId}/{taskId}")
    public String receivePage(@PathVariable("eventId") String eventId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.getParams().put("taskId", taskId);
        map.put("eventSheet", eventSheet);
        return prefix_receive + "/receive";
    }

    /**处理环节页面*/
    @GetMapping("/dealPage/{eventId}/{taskId}")
    public String dealPage(@PathVariable("eventId") String eventId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.getParams().put("taskId", taskId);
        map.put("eventSheet", eventSheet);
        return prefix_deal + "/deal";
    }

    /**二线(一线)解决页面*/
    @GetMapping({"/preSolutionPage/{eventId}/{taskId}", "/solutionPage/{eventId}/{taskId}"})
    public String preSolutionPage(@PathVariable("eventId") String eventId, HttpServletRequest request,
                                  @PathVariable("taskId") String taskId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.getParams().put("taskId", taskId);
        map.put("eventSheet", eventSheet);
        String url = "";
        if(request.getRequestURI().contains("preSolutionPage")) {
            // 二线解决
            url = prefix_solution_pre + "/solution";
            //事件单级别json数据
            List<OgTypeinfo> firstTypeList = ogTypeinfoService.selectOgTypeInfoByEvent(EventFlowConstants.EVENT_TYPE_NO, "1");
            map.put("firstTypeList", JSON.toJSONString(firstTypeList));
            // 事件单类别json数据
            List<OgTypeinfo> firstCategoryList = ogTypeinfoService.selectOgTypeInfoByEvent(EventFlowConstants.EVENT_CATEGORY_NO, "1");
            map.put("firstCategoryList", JSON.toJSONString(firstCategoryList));
        } else {
            // 一线解决
            url = prefix_solution_check + "/solution";
            // 查询类别、子类、条目、子条目当前级别list数据
            if(StringUtils.isNotEmpty(eventSheet.getEventCategory())) {
                // 事件单类别不为空，才去查询子类、条目、字条目等信息
                List<OgTypeinfo> eventCategoryList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_CATEGORY_NO, eventSheet.getEventCategory());
                map.put("eventCategoryList", eventCategoryList);
                if(StringUtils.isNotEmpty(eventSheet.getEventSubclass())) {
                    List<OgTypeinfo> eventSubclassList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_CATEGORY_NO, eventSheet.getEventSubclass());
                    map.put("eventSubclassList", eventSubclassList);
                }
                if(StringUtils.isNotEmpty(eventSheet.getEventEntry())) {
                    List<OgTypeinfo> eventEntryList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_CATEGORY_NO, eventSheet.getEventEntry());
                    map.put("eventEntityList", eventEntryList);
                }
                if(StringUtils.isNotEmpty(eventSheet.getEventSubentry())) {
                    List<OgTypeinfo> eventSubentryList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_CATEGORY_NO, eventSheet.getEventSubentry());
                    map.put("eventSubentryList", eventSubentryList);
                }
                map.put("firstCategoryEmpty", false);
            } else {
                // 事件单类别为空，直接查询事件单分类下的所有数据
                // 事件单类别json数据
                List<OgTypeinfo> eventTypeList = ogTypeinfoService.selectOgTypeInfoByEvent(EventFlowConstants.EVENT_CATEGORY_NO, "1");
                map.put("firstCategoryList", JSON.toJSONString(eventTypeList));
                map.put("firstCategoryEmpty", true);
            }
            // 查询三级信息
            if(StringUtils.isNotEmpty(eventSheet.getEventFirstType())) {
                // 事件单级别一级信息不为空，才去查询二级三级
                List<OgTypeinfo> eventFirstTypeList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_TYPE_NO, eventSheet.getEventFirstType());
                map.put("eventFirstTypeList", eventFirstTypeList);
                if(StringUtils.isNotEmpty(eventSheet.getEventSecondType())) {
                    List<OgTypeinfo> eventSecondTypeList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_TYPE_NO, eventSheet.getEventSecondType());
                    map.put("eventSecondTypeList", eventSecondTypeList);
                }
                if(StringUtils.isNotEmpty(eventSheet.getEventThreeType())) {
                    List<OgTypeinfo> eventThreeTypeList = ogTypeinfoService.selectCurrentOgTypeInfoList(EventFlowConstants.EVENT_TYPE_NO, eventSheet.getEventThreeType());
                    map.put("eventThreeTypeList", eventThreeTypeList);
                }
                map.put("firstTypeEmpty", false);
            } else {
                // 事件单第一级别为空，则查询事件单一级所有信息
                //事件单级别json数据
                List<OgTypeinfo> firstTypeList = ogTypeinfoService.selectOgTypeInfoByEvent(EventFlowConstants.EVENT_TYPE_NO, "1");
                map.put("firstTypeList", JSON.toJSONString(firstTypeList));
                map.put("firstTypeEmpty", true);
            }
        }
        if(StringUtils.isNotEmpty(eventSheet.getPreSolutionId())) {
            OgPerson person = personService.selectOgPersonById(eventSheet.getPreSolutionId());
            eventSheet.setSecondDealOrg(person.getOrgname());
        }
        return url;
    }

    /**一线关闭评价页面*/
    @GetMapping("/evaluatePage/{eventId}/{taskId}")
    public String evaluatePage(@PathVariable("eventId") String eventId, @PathVariable("taskId") String taskId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.getParams().put("taskId", taskId);
        map.put("eventSheet", eventSheet);
        return prefix_close + "/evaluate";
    }

    /**
     * 查询事件单列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EventSheet eventSheet)
    {
        startPage();
        String remark = eventSheet.getRemark();
        if("eventCreateSearch".equals(remark)) {
            eventSheet.setCreateBy(ShiroUtils.getUserId());
        }
        List<EventSheet> list = eventSheetService.selectEventSheetList(eventSheet);
        return getDataTable(list);
    }

    /**
     * 导出事件单列表
     */
    @Log(title = "事件单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EventSheet eventSheet)
    {
        List<EventSheet> list = eventSheetService.selectEventSheetList(eventSheet);
        ExcelUtil<EventSheet> util = new ExcelUtil<EventSheet>(EventSheet.class);
        return util.exportExcel(list, "sheet");
    }

    /**
     * 事件单查询待办
     * @param eventSheet
     * @return
     */
    @PostMapping("/eventAgencyList")
    @ResponseBody
    public TableDataInfo eventAgencyList(EventSheet eventSheet) {
        String description = (String) eventSheet.getParams().get("description");
        List<EventSheet> list = eventSheetService.selectEventAgencyList("SjBk", description, eventSheet);
        return getDataTable_ideal(list);
    }

    /**
     * 新增事件单
     */
    @GetMapping("/add")
    public String add(ModelMap map)
    {
        String no = generatorService.createNoAsLength("IM", 5);
        OgPerson person = personService.selectOgPersonById(ShiroUtils.getUserId());
        map.put("num", no);
        map.put("currentPerson", person);
        //List<OgTypeinfo> firstTypeList = ogTypeinfoService.selectOgTypeInfoByEvent("BankOfShangHaiEventLevel", "1");
        //map.put("firstTypeList", JSON.toJSONString(firstTypeList));
        return prefix_sheet + "/add";
    }

    /**
     * 新增保存事件单
     */
    @Log(title = "事件单", businessType = BusinessType.INSERT)
    @PostMapping("/saveOrSubmit")
    @ResponseBody
    public AjaxResult saveOrSubmit(EventSheet eventSheet)
    {
        String remark = eventSheet.getRemark();
        if("save".equals(remark)) {
            // 保存操作需要返回事件单的主键id
            if(StringUtils.isEmpty(eventSheet.getEventId())) {
                eventSheetService.insertEventSheet(eventSheet);
            } else {
                eventSheetService.updateEventSheet(eventSheet);
            }
        } else {
            if(StringUtils.isEmpty(eventSheet.getEventId())) {
                eventSheetService.insertEventSheet(eventSheet);
            } else {
                eventSheetService.updateEventSheet(eventSheet);
            }
            // 提交操作需要启动事件单流程
            Map<String, Object> variables = new HashMap<>();
            variables.put("reCode", "0");
            String roleId = "1000";// IT服务台角色id
            variables.put("receptionId", roleId);
            BmpEntity bmpEntity = eventSheetService.startProcess(eventSheet.getEventId(), variables);
            if(bmpEntity.isSuccessFlag()) {
                logger.info("事件单流程启动成功，事件单单号 eventNo:{}，流程实例 processInstanceId:{}", eventSheet.getEventNo(), bmpEntity.getProcessInstanceId());
                EventSheet event = new EventSheet();
                event.setEventStatus(EventStatusEnum.ASSIGNED.getCode());
                event.setEventId(eventSheet.getEventId());
                event.setAssignId(roleId);
                eventSheetService.updateEventSheet(event);
            }
        }
        return AjaxResult.success("操作成功", eventSheet.getEventId());
    }

    /**
     * 修改事件单
     */
    @GetMapping("/edit/{eventId}")
    public String edit(@PathVariable("eventId") String eventId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        map.put("eventSheet", eventSheet);
        return prefix_sheet + "/edit";
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{eventId}")
    public String detail(@PathVariable("eventId") String eventId, ModelMap map)
    {
        EventSheet eventSheet = eventSheetService.selectEventSheetById(eventId);
        eventSheet.setEventStatusName(EventStatusEnum.getStatusName(eventSheet.getEventStatus()));
        eventSheetService.translateEventType(eventSheet);
        if(StringUtils.isNotEmpty(eventSheet.getPreSolutionId())) {
            OgPerson person = personService.selectOgPersonById(eventSheet.getPreSolutionId());
            eventSheet.setSecondDealOrg(person.getOrgname());
        }
        map.put("eventSheet", eventSheet);
        return prefix_sheet + "/detail";
    }

    /**
     * 删除事件单
     */
    @Log(title = "事件单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(eventSheetService.deleteEventSheetByIds(ids));
    }

    /**
     * 撤销事件单
     */
    @PostMapping( "/cancel")
    @ResponseBody
    public AjaxResult cancel(String id)
    {
        eventSheetService.cancelEventSheetById(id);
        return success();
    }

    /**
     * 事件单查询受派组
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
        List<OgGroup> ogGroups = workService.selectGroupByGposition(map);
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

    /**
     * 事件单分派环节
     * @param eventSheet
     * @return
     */
    @PostMapping("/assignEvent")
    @ResponseBody
    public AjaxResult assignEvent(EventSheet eventSheet) {
        eventSheetService.assignEvent(eventSheet);
        return AjaxResult.success();
    }

    /**
     * 接单领取环节
     * @param eventSheet
     * @return
     */
    @PostMapping("/receiveEvent")
    @ResponseBody
    public AjaxResult receiveEvent(EventSheet eventSheet) {
        eventSheetService.receiveEvent(eventSheet);
        return AjaxResult.success();
    }

    /**
     * 处理环节
     * @param eventSheet
     * @return
     */
    @PostMapping("/dealEvent")
    @ResponseBody
    public AjaxResult dealEvent(EventSheet eventSheet) {
        eventSheet.setDealId(ShiroUtils.getUserId());
        eventSheetService.dealEvent(eventSheet);
        return AjaxResult.success();
    }

    /**
     * 事件单查询二线转派人
     * @param person
     * @return
     */
    @PostMapping("/selectPreSolutionName")
    @ResponseBody
    public TableDataInfo selectPreSolutionName(OgPerson person) {
        person.setrId("");
        startPage();
        List<OgPerson> personList = personService.selectListByRoleId(person);
        return getDataTable(personList);
    }

    /**
     * 事件单二线解决环节
     * @param eventSheet
     * @return
     */
    @PostMapping("/preSolutionEvent")
    @ResponseBody
    public AjaxResult preSolutionEvent(EventSheet eventSheet) {
        //eventSheet.setPreSolutionId(ShiroUtils.getUserId());
        eventSheetService.preSolutionEvent(eventSheet);
        return AjaxResult.success();
    }

    /**
     * 事件单一线解决环节
     * @param eventSheet
     * @return
     */
    @PostMapping("/solutionEvent")
    @ResponseBody
    public AjaxResult eventSheet(EventSheet eventSheet) {
        //eventSheet.setSolutionId(ShiroUtils.getUserId());
        eventSheetService.solutionEvent(eventSheet);
        return AjaxResult.success();
    }

    /**
     * 事件单关闭评价
     * @param eventSheet
     * @return
     */
    @PostMapping("/evaluateEvent")
    @ResponseBody
    public AjaxResult evaluateEvent(EventSheet eventSheet) {
        eventSheetService.evaluateEvent(eventSheet);
        return AjaxResult.success();
    }

    /**
     * 事件单退回补全提交
     * @param eventSheet
     * @return
     */
    @PostMapping("/supplyEvent")
    @ResponseBody
    public AjaxResult supplyEvent(EventSheet eventSheet) {
        eventSheetService.supplyEvent(eventSheet);
        return AjaxResult.success();
    }

    private void selectFistCategoryType(ModelMap map) {
        List<OgTypeinfo> firstTypeList = ogTypeinfoService.selectOgTypeInfoByEvent(EventFlowConstants.EVENT_TYPE_NO, "1");
        map.put("firstTypeList", JSON.toJSONString(firstTypeList));
        List<OgTypeinfo> eventCategoryList = ogTypeinfoService.selectOgTypeInfoByEvent(EventFlowConstants.EVENT_CATEGORY_NO, "1");
        map.put("eventCategoryList", JSON.toJSONString(eventCategoryList));
    }
}
