package com.ruoyi.form.controller;

import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.ChangeDeptEntity;
import com.ruoyi.form.entity.TwWorkNode;
import com.ruoyi.form.entity.TwWorkOrder;
import com.ruoyi.form.entity.automation.CheckStatus;
import com.ruoyi.form.service.AutomationPlatformsService;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.ITwWorkNodeService;
import com.ruoyi.form.service.ITwWorkOrderService;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.*;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Controller
@RequestMapping("/twWorkOrder")
public class TwWorkOrderController extends BaseController {

    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private ITwWorkOrderService twWorkOrderService;

    @Autowired
    private ITwWorkNodeService twWorkNodeService;

    @Autowired
    private AutomationPlatformsService automationPlatformsService;
    @Resource
    private CustomerFormService customerFormService;
    @Autowired
    private OgPersonMapper ogPersonMapper;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;


    private final String prefix = "work";

    /**
     * ???????????????
     *
     * @param mmap
     * @return chw
     */
    @GetMapping("/insert")
    public String list(ModelMap mmap) {
        mmap.addAttribute("type", "list");
        return prefix + "/list";
        // return "event/sheet"+"/list01";
    }

    /**
     * ?????????????????????
     * @param mmap
     * @return
     */
    @GetMapping("/listAll")
    public String listAll(ModelMap mmap) {
        return prefix + "/listAll";
    }

    /**
     * ??????????????????
     *
     * @param mmap
     * @return chw
     */
    @GetMapping("/myCreate")
    public String myCreate(ModelMap mmap) {
        mmap.addAttribute("type", "list");
        String userId = ShiroUtils.getUserId();
        mmap.addAttribute("createBy",userId);
        return prefix + "/workOrder/myWorkOrder";
    }

    @GetMapping("/increase")
    public String add(ModelMap mmap) {
        // mmap.addAttribute("type", "list");
        String bizType = "TW";
//        IdGenerator generator = new IdGenerator();
//        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
//        generator.setCurrentDate(nowDateStr);
//        generator.setBizType(bizType);
//        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        String problemNo = idGeneratorService.createNoAsLength(bizType, 3);
        mmap.addAttribute("issuefxNo", problemNo);
        return prefix + "/increase";
    }

    /**
     * ???????????????????????????
     */
    @Log(title = "?????????????????????", businessType = BusinessType.INSERT)
    @PostMapping("/addSave")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(TwWorkOrder twWorkOrder) {

        twWorkOrderService.addSave(twWorkOrder);
        return success("????????????");
    }

    /**
     * ????????????
     */
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/submit/{state}")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult submit(@PathVariable("state") Integer state, TwWorkOrder twWorkOrder) {
        //System.err.println(twWorkOrder);
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setBizId(twWorkOrder.getWorkNum());
        List<PubFlowLog> pList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        if (!CollectionUtils.isEmpty(pList)) {
            twWorkOrder.getParams().put("flag", "1");
        } else {
            twWorkOrder.getParams().put("flag", "0");
        }
        twWorkOrderService.submit(state, twWorkOrder);
        return success("??????????????????");
    }

    /**
     * ??????????????????
     * @param state
     * @param twWorkOrder
     * @return
     */
    @PostMapping("/auditPass/{state}")
    @ResponseBody
    public AjaxResult auditPass(@PathVariable("state") String state, TwWorkOrder twWorkOrder) {
        try {
            twWorkOrder.getParams().put("type", state);
            twWorkOrderService.auditPass(twWorkOrder);
            return AjaxResult.success("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.success("????????????");
        }
    }

    /**
     * ????????????
     *
     * @param
     * @return
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TwWorkOrder twWorkOrder) {
        startPage();
        setTime(twWorkOrder);
        List<TwWorkOrder> twWorkOrderList = twWorkOrderService.getTwWorkMyOrder(twWorkOrder);
        return getDataTable(twWorkOrderList);
    }


    @PostMapping("/listExecute")
    @ResponseBody
    public TableDataInfo listExecute(TwWorkOrder twWorkOrder) {
        startPage();
        List<TwWorkOrder> twWorkOrderList = twWorkOrderService.getTwWorkOrder(twWorkOrder);
        return getDataTable(twWorkOrderList);
    }

    /**
     * ??????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/exportChange")
    @ResponseBody
    public AjaxResult exportChange() {
        TwWorkOrder twWorkOrder = new TwWorkOrder();
        List<TwWorkOrder> twWorkOrderList = twWorkOrderService.getTwWorkOrderAll(twWorkOrder);
        ExcelUtil<TwWorkOrder> util = new ExcelUtil<TwWorkOrder>(TwWorkOrder.class);
        return util.exportExcel(twWorkOrderList, "?????????");
    }
    @PostMapping("/listAll")
    @ResponseBody
    public TableDataInfo listAll(TwWorkOrder twWorkOrder) {
        startPage();
        setTime(twWorkOrder);
        List<TwWorkOrder> twWorkOrderList = twWorkOrderService.getTwWorkOrderAll(twWorkOrder);
        return getDataTable(twWorkOrderList);
    }

    private TwWorkOrder setTime(TwWorkOrder twWorkOrder) {

        if (StringUtils.isNotEmpty(twWorkOrder.getParams().get("startTime"))) {
            String startTime = (String)twWorkOrder.getParams().get("startTime");
            twWorkOrder.getParams().put("startTime", startTime + " 00:00:00");
        }

        if (StringUtils.isNotEmpty(twWorkOrder.getParams().get("endTime"))) {
            String endTime = (String)twWorkOrder.getParams().get("endTime");
            twWorkOrder.getParams().put("endTime", endTime + " 23:59:59");
        }

        return twWorkOrder;
    }


    /**
     * ???????????????
     *
     * @param
     * @return
     */

    @PostMapping("/recordList/{orderId}")
    @ResponseBody
    public TableDataInfo recordList(@PathVariable("orderId") String orderId) {
        //startPage();
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(orderId);
        String token = automationPlatformsService.getToken("ideal", "ideal");

        List<CheckStatus> checkStatus = automationPlatformsService.checkStatus(token, twWorkOrder.getWorkNum());
//        CheckStatus checkStatus = new CheckStatus();
//        checkStatus.setStatus("??????");
        return getDataTable(checkStatus);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/editWorkOrder/{id}")
    public String update(@PathVariable("id") String id, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        modelMap.addAttribute("twWorkOrder", twWorkOrder);
        //TwWorkNode twWorkNode = twWorkNodeService.getOneNodeId(twWorkOrder.getId());
        modelMap.addAttribute("id", id);
        //modelMap.addAttribute("nodeId",twWorkNode.getId());
        return prefix + "/editWorkOrder";
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/workOrderDetail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        modelMap.addAttribute("twWorkOrder", twWorkOrder);
        modelMap.addAttribute("id", id);
        return prefix + "/workOrder/orderDetail";
    }

    /**
     * ??????????????????
     *
     * @return chw
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        modelMap.addAttribute("twWorkOrder", twWorkOrder);
        return prefix + "/edit";
    }

    /**
     * ????????????????????????
     *
     * @return chw
     */
    @GetMapping("/editServer/{id}")
    public String editServer(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("id", id);
        return prefix + "/editServer";
    }

    /**
     * ?????????????????????
     */
    @Log(title = "?????????????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult update(TwWorkOrder twWorkOrder) {
        twWorkOrderService.updateById(twWorkOrder);
        return success("????????????");
    }

    /**
     * ???????????????
     *
     * @param mmap
     * @return chw
     */
    @GetMapping("/executeList")
    public String execute(ModelMap mmap) {
        mmap.addAttribute("type", "list");
        return prefix + "/execute";
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/executeWorkOrder/{id}")
    public String executeWorkOrder(@PathVariable("id") String id, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        modelMap.addAttribute("twWorkOrder", twWorkOrder);
        modelMap.addAttribute("id", id);
        return prefix + "/executeWorkOrder";
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/exeRecord/{id}")
    public String executeRecord(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("id", id);
        return prefix + "/exeRecord";
    }

    /**
     * ????????????
     *
     * @param id workOrderID
     * @return chw
     */
    @PostMapping("/execute/{id}")
    @ResponseBody
    public AjaxResult execute(@PathVariable("id") Integer id) {
        //????????????
        try {
            twWorkOrderService.execute(id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("????????????");
        }
        return AjaxResult.success("????????????");
    }


    /**
     * ????????????
     *
     * @param
     * @return chw
     */
    @GetMapping("/executeTask/{workOrderId}")
    @ResponseBody
    public AjaxResult executeTask(@PathVariable("workOrderId") Integer workOrderId) {
        //????????????
        try {
            twWorkOrderService.execute(workOrderId);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("????????????");
        }
        return AjaxResult.success("????????????");
    }



    /**
     * ????????????
     *
     * @param mmap
     * @return chw
     */
    @GetMapping("/backlog")
    public String backlog(ModelMap mmap) {
        mmap.addAttribute("type", "list");
        return prefix + "/workOrder/backlog";
    }

    /**
     * ????????????
     *
     * @param
     * @return
     */
    @PostMapping("/backlog/list")
    @ResponseBody
    public TableDataInfo backlogList(TwWorkOrder twWorkOrder) {
        String userId =  ShiroUtils.getUserId();
        //twWorkOrder.setImplementorId(userId);
        setTime(twWorkOrder);
        List<TwWorkOrder> twWorkOrders = twWorkOrderService.backlogList(twWorkOrder);
        if (twWorkOrders == null) {
            twWorkOrders = new ArrayList<TwWorkOrder>();
        }
        return getDataTable(twWorkOrders);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/deal/{id}")
    public String deal(@PathVariable("id") String id, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        twWorkOrder.setImplementorOrgid(null);
        twWorkOrder.setImplementorName(null);
        twWorkOrder.setImplementorId(null);
        twWorkOrder.setImplementorPeopleName(null);
        modelMap.addAttribute("twWorkOrder", twWorkOrder);
        modelMap.addAttribute("id", id);
        return prefix + "/workOrder/dealWorkOrder";
    }

    /**
     * ????????????????????????????????????
     *
     * @param  workOrderId
     * @return chw
     */
    @GetMapping("/addDelivery/{workOrderId}")
    public String addDelivery(@PathVariable("workOrderId") String workOrderId, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(workOrderId);
        twWorkOrder.setImplementorOrgid(null);
        twWorkOrder.setImplementorName(null);
        twWorkOrder.setImplementorId(null);
        twWorkOrder.setImplementorPeopleName(null);
        modelMap.addAttribute("nodeById", twWorkOrder);
        modelMap.addAttribute("workOrderId", workOrderId);
        return prefix + "/workOrder/addDelivery";
    }

    /**
     * ????????????????????????????????????
     *
     * @param  workOrderId
     * @return chw
     */
    @GetMapping("/addDb/{workOrderId}")
    public String addDb(@PathVariable("workOrderId") String workOrderId, ModelMap modelMap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(workOrderId);
        twWorkOrder.setImplementorOrgid(null);
        twWorkOrder.setImplementorName(null);
        twWorkOrder.setImplementorId(null);
        twWorkOrder.setImplementorPeopleName(null);
        modelMap.addAttribute("nodeById", twWorkOrder);
        modelMap.addAttribute("workOrderId", workOrderId);
        return prefix + "/workOrder/addDb";
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @PostMapping("/syslist")
    @ResponseBody
    @ApiOperation("????????????????????????????????????")
    @CrossOrigin
    public TableDataInfo syslist(OgSys sys) {
        startPage();
        List<OgSys> list = twWorkOrderService.syslist(sys);
        return getDataTable(list);
    }

//    /**
//     * ????????????????????????????????????
//     *
//     * @return
//     */
//    @PostMapping("/syslistAll")
//    @ResponseBody
//    @ApiOperation("????????????????????????????????????")
//    @CrossOrigin
//    public TableDataInfo syslistAll(OgSys sys) {
//        startPage();
//        List<OgSys> list = twWorkOrderService.syslistAll(sys);
//        return getDataTable(list);
//    }


    @GetMapping("/getHistoryImage")
    @ApiOperation("?????????????????????????????????????????????")
    public void getHistoryImage(String instanceId, HttpServletResponse response){
        customerFormService.createHistoryImage(instanceId,response);
    }
    /**
     * ????????????????????????
     * @return
     */
    @GetMapping("/selectBusinessAuditPerson")
    public ModelAndView selectBusinessAuditPerson(String pflag, String rId, ModelMap mmap) {
        ModelAndView model = new ModelAndView(prefix+"/selectPerson");
        model.addObject("rId",rId);
        model.addObject("pflag",pflag);
        return model;
    }

    /**
     * ???????????????
     * pflag???????????????????????????1?????????????????????rid?????????????????????like??????
     * ????????????1??????rid???orgId???????????????????????????????????????
     * person????????????rid????????????
     *
     * @param person
     * @return
     */
    @PostMapping("/selectPersonByOrgIdOrOrgLvAndRole")
    @ResponseBody
    public TableDataInfo selectPersonByOrgOrOrgLvAndRole(OgPerson person) {
        if (person.getParams() != null && person.getParams().containsKey("isselect") && "1".equals(person.getParams().get("isselect"))) {
        } else {
            startPage();
        }
        OgPerson persons = new OgPerson();
        persons.getParams().put("postIds", person.getrId().split(","));
        List<OgPerson> personList = ogPersonMapper.selectCheckForTinyWeb(persons);
        return getDataTable(personList);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/selectAcceptor/{id}/{type}")
    public String synergy(@PathVariable("id") String id, @PathVariable("type") String type, ModelMap mmap) {
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        mmap.put("twWorkOrder", twWorkOrder);
        mmap.put("id", id);
        mmap.put("type", type);
        return prefix + "/workOrder/selectAcceptor";

    }

    /**
     * ???????????????
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String type, String id, ModelMap mmap) {

        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        String status = twWorkOrder.getStatus();
        String postId = "";

        if(status.equals("??????")) {

            twWorkOrder.setStatus("?????????");

        } else if (("??????").equals(status)) {

            if ("1".equals(type)) {//????????????
                postId = "0622,0624";
            } else if ("2".equals(type)) {//?????????????????????
                postId = "0627";
            }

        } else if(("??????").equals(status)) {

            if ("1".equals(type)) {//????????????
                postId = "0622,0624";
            } else if ("2".equals(type)) {//?????????????????????
                postId = "0627";
            }
        } else if(("??????").equals(status)) {
            if ("3".equals(type)) {
                postId = "0627";
            } else if ("1".equals(type)) {
                postId = "0627";
            }
        }

        ModelAndView model = new ModelAndView(prefix + "/workOrder/selectPerson");
        model.addObject("status",status);
        model.addObject("postId",postId);
        model.addObject("type",type);
        return model;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @PostMapping("/dealEnvironment")
    @ResponseBody
    public AjaxResult dealEnvironment(TwWorkOrder twWorkOrder) {
        try {
            twWorkOrderService.auditPass(twWorkOrder);
            return AjaxResult.success("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.success("????????????");
        }
    }

    /**
     * id??????
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        TwWorkOrder twWorkOrder = twWorkOrderService.selectTwWorkOrderById(id);
        //?????????????????????ID
        String pId = ShiroUtils.getOgUser().getpId();
        String flag = "0";//????????????????????????????????? ?????????
        if (pId.equals(twWorkOrder.getCreateBy())) {
            flag = "1";
        }
        ajaxResult.put("data",twWorkOrder);
        ajaxResult.put("flag",flag);
        return  ajaxResult;
    }

    /**
     * ??????
     */
    @Log(title = "???????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/cancle")
    @ResponseBody
    @Transactional
    public AjaxResult cancle(String id, String status) {

        try {
            //endTaskAdapterImplBGQQ.close(id);
            twWorkOrderService.toCancle(id);
        } catch (Exception e) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????");
        }
        return success("????????????????????????????????????");
    }

    @GetMapping("/sysGrouplist")
    public String selectOneApplication() {
        return prefix + "/selectOneApplication";
    }

}

