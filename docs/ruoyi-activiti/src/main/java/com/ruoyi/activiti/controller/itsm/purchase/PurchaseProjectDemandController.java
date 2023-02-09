package com.ruoyi.activiti.controller.itsm.purchase;

import com.ruoyi.activiti.domain.*;
import com.ruoyi.activiti.service.IPurchaseProjectDemandService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 采购计划Controller
 * 
 * @author ruoyi
 * @date 2021-11-17
 */
@Controller
@RequestMapping("/purchase/change")
public class PurchaseProjectDemandController extends BaseController
{
    // 采购计划相关页面
    private String prefix_demand = "purchase/change/demand";
    // 采购实施相关页面
    private String prefix_practice = "purchase/change/practice";
    // 采购计划查询页面
    private String prefix_search = "purchase/change/search";

    @Autowired
    private IPurchaseProjectDemandService purchaseProjectDemandService;

    @Autowired
    private IOgPersonService personService;

    /**
     * 查询采购计划新建页面
     */
    @GetMapping()
    public String demand()
    {
        return prefix_demand + "/demand";
    }

    /**
     * 审批
     */
    @GetMapping("/approval")
    public String approval() {
        return prefix_demand + "/approval";
    }

    /**
     * 查询采购计划管理页面
     */
    @GetMapping("/manager")
    public String manager()
    {
        return prefix_demand + "/manager";
    }

    /**
     * 采购计划实施页面
     */
    @GetMapping("/practice")
    public String practice()
    {
        return prefix_practice + "/practice";
    }

    /**
     * 采购实施管理页面
     */
    @GetMapping("/practiceManager")
    public String practiceManager()
    {
        return prefix_practice + "/manager";
    }

    /**
     * 采购计划查询页面
     */
    @GetMapping("/searchList")
    public String searchList()
    {
        return prefix_search + "/list";
    }

    /**
     * 查询采购计划列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PurchaseProjectDemandChange change)
    {
        String userId = ShiroUtils.getUserId();
        Map<String, Object> params = change.getParams();
        String[] statusArray = null;
        if(StringUtils.isNotEmpty(change.getPurchaseStatus())) {
            statusArray = Convert.toStrArray(change.getPurchaseStatus());
            params.put("statusArray", statusArray);
        }
        if(!StringUtils.isEmpty(params.get("createTimeStart"))) {
            params.put("createTimeStart", params.get("createTimeStart") + " 00:00:00");
        }
        if(!StringUtils.isEmpty(params.get("createTimeEnd"))) {
            params.put("createTimeEnd", params.get("createTimeEnd") + " 23:59:59");
        }
        // 此处表示新建页面可查询两种状态数据，"草稿"｜"已退回"
        if(StringUtils.isNotEmpty(statusArray) && statusArray.length > 1) {
            change.setCreateBy(userId);
        } else if("02".equals(change.getPurchaseStatus())) {
            change.setPurchaseApproveId(userId);
        }
        startPage();
        List<?> list;
        if("practice".equals(change.getRemark()) || "practiceManager".equals(change.getRemark())
                || "searchList".equals(change.getRemark())) {
            PurchaseProjectDemand purchaseProjectDemand = new PurchaseProjectDemand();
            BeanUtils.copyBeanProp(purchaseProjectDemand, change);
            purchaseProjectDemand.setPurchaseApproveId("");
            // 采购计划实施需要控制当前操作人可见，采购的查询不做控制
            if("practice".equals(change.getRemark())) {
                purchaseProjectDemand.setCreateBy(userId);
            }
            list = purchaseProjectDemandService.selectPurchaseProjectDemandList(purchaseProjectDemand);
        } else {
            list = purchaseProjectDemandService.selectPurchaseProjectDemandChangeList(change);
        }
        return getDataTable(list);
    }

    /**
     * 采购计划导入
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file)
    {
        ExcelUtil<PurchaseProjectDemandVo> util = new ExcelUtil<>(PurchaseProjectDemandVo.class);
        List<PurchaseProjectDemandVo> purchaseList;
        try{
            purchaseList = util.importExcel(file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("数据导入错误！");
        }
        String message = purchaseProjectDemandService.importPurchaseProjectDemand(purchaseList);
        return AjaxResult.success(message);
    }

    /**
     * 下载采购计划模版
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            InputStream in = this.getClass().getResourceAsStream("/excel/采购计划管理.xlsx");
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, "采购计划管理.xlsx"));
            FileUtils.writeBytes(in, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("采购计划模板下载失败!");
        }
    }

    /**
     * 导出采购计划列表
     */
    @Log(title = "采购计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PurchaseProjectDemand purchase)
    {
        Map<String, Object> params = purchase.getParams();
        if(StringUtils.isNotEmpty(purchase.getPurchaseStatus())) {
            String[] statusArray = Convert.toStrArray(purchase.getPurchaseStatus());
            params.put("statusArray", statusArray);
        }
        if(!StringUtils.isEmpty(params.get("createTimeStart"))) {
            params.put("createTimeStart", params.get("createTimeStart") + " 00:00:00");
        }
        if(!StringUtils.isEmpty(params.get("createTimeEnd"))) {
            params.put("createTimeEnd", params.get("createTimeEnd") + " 23:59:59");
        }
        if ("currentPage".equals(params.get("currentPage"))) {
            startPage();
        }
        List<PurchaseProjectDemand> list = purchaseProjectDemandService.selectPurchaseProjectDemandList(purchase);
        ExcelUtil<PurchaseProjectDemand> util = new ExcelUtil<>(PurchaseProjectDemand.class);
        return util.exportExcel(list, "采购计划");
    }

    /**
     * 新增采购计划
     */
    @GetMapping("/add")
    public String add(ModelMap map)
    {
        String numStr = purchaseProjectDemandService.getPurchaseNumStr("CG");
        map.put("num", numStr);
        return prefix_demand + "/add";
    }

    /**
     * 新增保存采购计划
     */
    @Log(title = "采购计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PurchaseProjectDemandChange purchaseProjectDemandChange)
    {
        return toAjax(purchaseProjectDemandService.insertPurchaseProjectDemandChange(purchaseProjectDemandChange));
    }

    /**
     * 修改采购计划
     */
    @GetMapping("/edit/{changeId}")
    public String edit(@PathVariable("changeId") String changeId, ModelMap mmap)
    {
        PurchaseProjectDemandChange purchaseProjectDemandChange = purchaseProjectDemandService.selectPurchaseProjectDemandChangeById(changeId);
        /*String purchaseMode = purchaseProjectDemandChange.getPurchaseMode();
        if(StringUtils.isNotEmpty(purchaseMode)) {
            // 翻译采购建议字段
            String purchaseModeTemp = "";
            for(String mode : Convert.toStrArray(purchaseMode)) {
                String purchaseModeDetail = pubParaValueService.selectPubParaValueByNameValue("purchase_mode", mode);
                purchaseModeTemp = purchaseModeDetail + ",";
            }
            purchaseModeTemp = purchaseModeTemp.substring(0, purchaseModeTemp.length() - 1);
            purchaseProjectDemandChange.setPurchaseMode(purchaseModeTemp);
        }*/
        mmap.put("purchaseProjectDemandChange", purchaseProjectDemandChange);
        return prefix_demand + "/edit";
    }

    /**
     * 修改保存采购计划
     */
    @Log(title = "采购计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PurchaseProjectDemandChange purchaseProjectDemandChange)
    {
        return toAjax(purchaseProjectDemandService.updatePurchaseProjectDemandChange(purchaseProjectDemandChange));
    }

    /**
     * 删除采购计划
     */
    @Log(title = "采购计划", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(purchaseProjectDemandService.deletePurchaseProjectDemandChangeByIds(ids));
    }

    /**
     * 采购计划查看详情
     */
    @GetMapping("/detail/{changeId}")
    public String detail(@PathVariable("changeId") String changeId, ModelMap mmap)
    {
        PurchaseProjectDemandChange purchaseProjectDemandChange = purchaseProjectDemandService.selectPurchaseProjectDemandChangeById(changeId);
        mmap.put("purchaseProjectDemandChange", purchaseProjectDemandChange);
        return prefix_demand + "/detail";
    }

    /**
     * 采购实施阶段查看详情
     */
    @GetMapping("/practiceDetail/{purchaseId}")
    public String practiceDetail(@PathVariable("purchaseId") String purchaseId, ModelMap mmap)
    {
        PurchaseProjectDemand purchaseProjectDemand = purchaseProjectDemandService.selectPurchaseProjectDemandById(purchaseId);
        mmap.put("purchaseProjectDemand", purchaseProjectDemand);
        return prefix_practice + "/detail";
    }

    @GetMapping("/selectPurchaseApproval")
    public String selectPurchaseApproval() {
        return prefix_demand + "/selectPurchaseApproval";
    }

    /**
     * 选择部门领导审核人
     */
    @PostMapping("/selectPurchaseApproval")
    @ResponseBody
    public TableDataInfo selectPurchaseApproval(OgPerson person) {
        OgPerson op = personService.selectOgPersonById(ShiroUtils.getUserId());
        List<String> orgIds = new ArrayList<>();
        orgIds.add(op.getOrgId());
        Map<String, Object> params = person.getParams();
        // 查询条件为数据中心处长和当前操作人的机构
        params.put("postId", "0011");
        params.put("orgIds", orgIds);
        startPage();
        List<OgPerson> personList = personService.selectOgPersonByOrgAndPostId(person);
        return getDataTable(personList);
    }

    /**
     * 审批
     */
    @GetMapping("/demandApproval/{changeId}")
    public String approval(@PathVariable("changeId") String changeId, ModelMap mmap) {
        PurchaseProjectDemandChange PurchaseProjectDemandChange = purchaseProjectDemandService.selectPurchaseProjectDemandChangeById(changeId);
        mmap.put("purchaseProjectDemandChange", PurchaseProjectDemandChange);
        return prefix_demand + "/demandApproval";
    }

    /**
     * 批量审批
     */
    @GetMapping("/demandApprovalList/{changeId}")
    public String approvalList(@PathVariable("changeId") String changeId, ModelMap mmap) {
        mmap.put("changeId", changeId);
        return prefix_demand + "/demandApprovalList";
    }

    // 审批接口
    @PostMapping("/demandApproval")
    @ResponseBody
    @Transactional
    public AjaxResult demandApproval(PurchaseProjectDemandChange change) {
        int rows = 0;
        String[] changeIds = Convert.toStrArray(change.getChangeId());
        for(String changeId : changeIds) {
            PurchaseProjectDemandChange newChange = new PurchaseProjectDemandChange();
            if("1".equals(change.getRemark()))
                // 审批通过置状态为"已审核"
                newChange.setPurchaseStatus("03");
            else
                // 退回置状态为"已退回"
                newChange.setPurchaseStatus("05");
            newChange.setChangeId(changeId);
            rows += purchaseProjectDemandService.updatePurchaseProjectDemandChange(newChange);
            if(rows > 0) {
                PurchaseApproveLog approveLog = new PurchaseApproveLog();
                approveLog.setChangeId(changeId);
                String comment = (String)change.getParams().get("comment");
                approveLog.setMemo(comment);
                purchaseProjectDemandService.insertPurchaseApproveLog(approveLog);
            }
        }
        return toAjax(rows);
    }

    @GetMapping("/managerApproval/{changeId}")
    public String managerApproval(@PathVariable("changeId") String changeId, ModelMap map) {
        PurchaseProjectDemandChange PurchaseProjectDemandChange = purchaseProjectDemandService.selectPurchaseProjectDemandChangeById(changeId);
        map.put("purchaseProjectDemandChange", PurchaseProjectDemandChange);
        return prefix_demand + "/managerApproval";
    }

    /**
     * 采购计划管理员退回操作，状态变更为"已退回"，同时新建页面能够查询到
     * 确认操作生成采购计划实施记录
     */
    @PostMapping("/managerApproval")
    @ResponseBody
    public AjaxResult managerApproval(PurchaseProjectDemandChange change) {
        int rows = 0;
        PurchaseProjectDemandChange newChange = new PurchaseProjectDemandChange();
        if("1".equals(change.getRemark())) {// 通过
            PurchaseProjectDemand purchaseProjectDemand = new PurchaseProjectDemand();
            BeanUtils.copyBeanProp(purchaseProjectDemand, change);
            purchaseProjectDemand.setPurchaseId(UUID.getUUIDStr());
            purchaseProjectDemand.setChangeId(change.getChangeId());
            purchaseProjectDemand.setPurchaseStatus("01");
            rows = purchaseProjectDemandService.insertPurchaseProjectDemand(purchaseProjectDemand);
            if(rows > 0) {
                // 采购管理员通过设置状态为"04"，退回为"05"
                newChange.setPurchaseStatus("04");
                newChange.setChangeId(change.getChangeId());
                rows = purchaseProjectDemandService.updatePurchaseProjectDemandChange(newChange);
            }
        } else {// 退回
            newChange.setChangeId(change.getChangeId());
            newChange.setPurchaseStatus("05");
            rows = purchaseProjectDemandService.updatePurchaseProjectDemandChange(newChange);
        }
        if(rows > 0) {
            PurchaseApproveLog approveLog = new PurchaseApproveLog();
            approveLog.setChangeId(change.getChangeId());
            String comment = (String)change.getParams().get("comment");
            approveLog.setMemo(comment);
            purchaseProjectDemandService.insertPurchaseApproveLog(approveLog);
        }
        return toAjax(rows);
    }

    /**
     * 采购计划实施
     */
    @GetMapping("/purchasePractice/{purchaseId}")
    public String implement(@PathVariable("purchaseId") String purchaseId, ModelMap map) {
        PurchaseProjectDemand purchaseProjectDemand = purchaseProjectDemandService.selectPurchaseProjectDemandById(purchaseId);
        map.put("purchaseProjectDemand", purchaseProjectDemand);
        return prefix_practice + "/purchasePractice";
    }

    /**
     * 采购计划实施提交方法(包含实施操作员提交和管理员确认｜退回操作)
     */
    @PostMapping("/purchasePractice")
    @ResponseBody
    @Transactional
    public AjaxResult purchasePractice(PurchaseProjectDemand purchaseProjectDemand) {
        int rows = purchaseProjectDemandService.purchasePractice(purchaseProjectDemand);
        return toAjax(rows);
    }

    /**
     * 查询采购实施当前进度
     */
    @PostMapping("/selectPurchaseCurrentProgressList")
    @ResponseBody
    public TableDataInfo selectPurchaseCurrentProgressList(String purchaseId) {
        startPage();
        List<PurchaseCurrentProgress> list = purchaseProjectDemandService.selectPurchaseCurrentProgressListByPurchaseId(purchaseId);
        return getDataTable(list);
    }

    /**
     * 采购计划当前进度维护页面
     */
    @GetMapping("/purchaseCurrentProgress/{purchaseId}")
    public String purchaseCurrentProgress(@PathVariable("purchaseId") String purchaseId, ModelMap map)
    {
        map.put("purchaseId", purchaseId);
        return prefix_practice + "/currentProgress";
    }

    /**
     * 保存采购计划当前进度
     */
    @PostMapping("/purchaseCurrentProgress")
    @ResponseBody
    public AjaxResult purchaseCurrentProgress (PurchaseCurrentProgress currentProgress) {
        return toAjax(purchaseProjectDemandService.insertPurchaseCurrentProgress(currentProgress));
    }

    /**
     * 采购实施管理审核页面
     */
    @GetMapping("/practiceManagerApproval/{purchaseId}")
    public String practiceManagerApproval(@PathVariable("purchaseId") String purchaseId, ModelMap map) {
        PurchaseProjectDemand purchaseProjectDemand = purchaseProjectDemandService.selectPurchaseProjectDemandById(purchaseId);
        map.put("purchaseProjectDemand", purchaseProjectDemand);
        return prefix_practice + "/managerApproval";
    }

    /**
     * 保存采购计划当前进度
     */
    @PostMapping("/selectPurchaseApproveLog")
    @ResponseBody
    public TableDataInfo selectPurchaseApproveLog (PurchaseApproveLog purchaseApproveLog) {
        List<PurchaseApproveLog> purchaseApproveLogs = purchaseProjectDemandService.selectPurchaseApproveLogById(purchaseApproveLog);
        return getDataTable(purchaseApproveLogs);
    }
}
