package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.IAmBizParaService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.AmBizPara;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysNoticeReceive;
import com.ruoyi.system.domain.SysNoticeReceiveLog;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysNoticeReceiveService;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/noticeHandle")
public class SysNoticeHandleController extends BaseController {

    private String prefix = "system/notice";

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ISysNoticeReceiveService sysNoticeReceiveService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IOgPersonService ogPersonService;

    @GetMapping()
    public String checkNotice()
    {
        return prefix + "/noticeHandle";
    }

    /**
     * 查询处理公告列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNoticeReceive sysNoticeReceive)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTime", sysNoticeReceive.getParams().get("beginTime"));
        String endTime = (String) sysNoticeReceive.getParams().get("endTime");
        if (StringUtils.isNotEmpty(endTime)) {
            endTime += " " + "23:59:59";
        }
        map.put("endTime", endTime);
        map.put("currentStatus", "03");
        sysNoticeReceive.setParams(map);
        sysNoticeReceive.setAmBizIds(noticeService.receiveAmBizIds());
        List<SysNoticeReceive> list = sysNoticeReceiveService.selectNoticeReceiveList(sysNoticeReceive);
        for (int i = 0 ; i < list.size() ; i ++) {
            String addTime = list.get(i).getAddTime();
            list.get(i).setAddTime(handleTimeYYYY_MM_DD_HH_MM_SS(addTime));
        }
        list = list.stream().sorted(Comparator.comparing(SysNoticeReceive::getAddTime).reversed()).collect(Collectors.toList());
        return getDataTable_ideal(list);
    }

    /**
     * 查询接收公告列表查询公告通知，监控公告通知接口
     */
    @PostMapping("/noticeList")
    @ResponseBody
    public TableDataInfo noticeList(SysNoticeReceive sysNoticeReceive)
    {
        List<SysNoticeReceive> list = sysNoticeReceiveService.selectNoticeReceives(sysNoticeReceive);
        for (int i = 0 ; i < list.size() ; i ++) {
            String receiveTime = list.get(i).getReceiveTime();
            list.get(i).setReceiveTime(handleTimeYYYY_MM_DD_HH_MM_SS(receiveTime));
        }
        list = list.stream().sorted(Comparator.comparing(SysNoticeReceive::getEditTime).reversed()).collect(Collectors.toList());
        return getDataTable_ideal(list);
    }

    /**
     * 处理公告详情
     */
    @GetMapping("/details/{amReceiveId}")
    public String details(@PathVariable("amReceiveId") String amReceiveId, ModelMap mmap)
    {
        mmap.put("noticeReceive", sysNoticeReceiveService.selectNoticeReceiveById(amReceiveId));
        return prefix + "/receiveNoticeDetails";
    }

    /**
     * 跳转处理公告
     */
    @GetMapping("/edit/{amReceiveId}")
    public String edit(@PathVariable("amReceiveId") String amReceiveId, ModelMap mmap)
    {
        SysNoticeReceive sysNoticeReceive =  sysNoticeReceiveService.selectNoticeReceiveById(amReceiveId);
        mmap.put("sendRangeList", getSengRangeList());
        mmap.put("noticeReceive", sysNoticeReceive);
        return prefix + "/noticeHandleDetails";
    }

    /**
     * selectByid
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        SysNoticeReceive sysNoticeReceive = sysNoticeReceiveService.selectNoticeReceiveById(id);
        ajaxResult.put("data",sysNoticeReceive);
        return  ajaxResult;
    }

    /**
     * 处理修改保存公告
     */
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNoticeReceive sysNoticeReceive)
    {

        return toAjax(sysNoticeReceiveService.updateNoticeReceive(sysNoticeReceive));
    }

    //获取发送范围下拉框
    private List<Map<String, String>> getSengRangeList() {

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        return reList;
    }

    /**
     * 导出接收公告通知列表
     */
    @Log(title = "公告通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export1(SysNoticeReceive sysNoticeReceive)
    {
        String isCurrentPage = (String) sysNoticeReceive.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        } else  if ("all".equals(isCurrentPage)) {
//            String id = sysNoticeReceive.getAmBizId();
//            sysNoticeReceive = new SysNoticeReceive();
//            sysNoticeReceive.setAmBizId(id);
        }
        List<SysNoticeReceive> list = sysNoticeReceiveService.selectNoticeReceives(sysNoticeReceive);
        ExcelUtil<SysNoticeReceive> util = new ExcelUtil<SysNoticeReceive>(SysNoticeReceive.class);
        return util.exportExcel(list, "公告通知");
    }

    /**
     * 导出公告接收详情列表
     */
    @Log(title = "公告通知", businessType = BusinessType.EXPORT)
    @PostMapping("/exportDetails")
    @ResponseBody
    public AjaxResult export2(SysNoticeReceiveLog sysNoticeReceiveLog)
    {
        String isCurrentPage = (String) sysNoticeReceiveLog.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        } else  if ("all".equals(isCurrentPage)) {
//            String id = sysNoticeReceiveLog.getAmReceiveId();
//            sysNoticeReceiveLog = new SysNoticeReceiveLog();
//            sysNoticeReceiveLog.setAmReceiveId(id);
        }
        List<SysNoticeReceiveLog> list = sysNoticeReceiveService.receiveLogList(sysNoticeReceiveLog);
        ExcelUtil<SysNoticeReceiveLog> util = new ExcelUtil<SysNoticeReceiveLog>(SysNoticeReceiveLog.class);
        return util.exportExcel(list, "公告通知");
    }

    /**
     * 跳转接收公告详情
     */
    @GetMapping("/receiveDetails/{id}")
    public String receiveDetails(@PathVariable("id") String amReceiveId, ModelMap mmap)
    {
        mmap.put("amReceiveId", amReceiveId);
        return prefix + "/receiveNoticeDetails";

    }

    /**
     * 查询接收公告详情列表
     */
    @PostMapping("/receiveNoticeLists")
    @ResponseBody
    public TableDataInfo receiveNoticeLists(SysNoticeReceiveLog sysNoticeReceiveLog)
    {
        startPage();
        List<SysNoticeReceiveLog> list = sysNoticeReceiveService.receiveLogList(sysNoticeReceiveLog);
        return getDataTable(list);
    }

    /**
     * 跳转短信通知页面
     */
    @GetMapping("/smsNotice/{id}")
    public String smsNotice(@PathVariable("id") String amReceiveId, ModelMap mmap)
    {
        SysNoticeReceive sysNoticeReceive = new SysNoticeReceive();
        sysNoticeReceive = sysNoticeReceiveService.selectNoticeReceiveById(amReceiveId);
        mmap.put("amReceiveId", amReceiveId);
        mmap.put("amTitle", sysNoticeReceive.getAmTitle());
        mmap.put("amCode", sysNoticeReceive.getAmCode());
        return prefix + "/smsNotice";

    }

    /**
     * 短信通知
     */
    @PostMapping("/smsNotice")
    @ResponseBody
    public TableDataInfo smsNotices(SysNoticeReceiveLog sysNoticeReceiveLog)
    {
        startPage();
        SysNoticeReceive sysNoticeReceive = new SysNoticeReceive();
        sysNoticeReceive = sysNoticeReceiveService.selectNoticeReceiveById(sysNoticeReceiveLog.getAmReceiveId());
        String[] persons = sysNoticeReceive.getReceivePersonList().split(",");
        List<SysNoticeReceive> personList = new ArrayList<>();
        OgPerson ogPerson = new OgPerson();
        for (int i = 0 ; i < persons.length ; i ++) {
            sysNoticeReceive = sysNoticeReceiveService.smsNoticePersonById(persons[i]);
            if (sysNoticeReceive != null) {
                personList.add(sysNoticeReceive);
            }
        }
        return getDataTable(personList);
    }

    /**
     * 发送短信
     *
     * @param pIds 发送人id
     * @param smsText 短信内容
     * @return
     */
    @PostMapping("/sendSms")
    @ResponseBody
    public AjaxResult sendSmsNotice(String[] pIds, String smsText) {

        if (pIds == null) {
            return AjaxResult.error("接收人不能为空！");
        } else if (smsText == null) {
            return AjaxResult.error("短信内容不能为空！");
        }
        OgPerson person = new OgPerson();
        for (int i = 0 ; i < pIds.length ; i ++) {
            person = ogPersonService.selectOgPersonById(pIds[i]);// 获取短信接收人
            sendSms(smsText, person);
        }
        return AjaxResult.success("发送成功");
    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        //pubBizPresmsService.insertPubBizPresms(p);

        //pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }
}
