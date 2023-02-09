package com.ruoyi.activiti.controller.itsm.phone;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.*;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.activiti.service.ITelFlowLogService;
import com.ruoyi.activiti.service.impl.OgPhoneServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 电话事件单（查询）
 * */
@Controller
@RequestMapping("phone/cx")
public class OgCxPhoneController extends BaseController
{

    //新增界面路径
    private String prefix_cx = "phone/cx";

    //业务事件单路经
    private String prefix = "issueList/build";

    @Autowired
    private OgPhoneServiceImpl phoneService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_cx + "/phone";
    }

    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix_cx + "/selectOneApplication";
    }

    //查询当前登录人的电话事件单
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TelBiz telBiz) {
        // 创建时间---开始时间
        if (StringUtils.isNotEmpty(telBiz.getCreateTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getCreateTime());
            telBiz.setCreateTime(parseStart);
        }
        // 创建时间---截止时间
        if (StringUtils.isNotEmpty(telBiz.getStartTimeBiz())) {
            String parseStart = DateUtils.handleTimeYYYYMMDD(telBiz.getStartTimeBiz());
            telBiz.setStartTimeBiz(parseStart+"240000");
        }
        startPage();
        List<TelBiz> list = phoneService.selectPhoneList(telBiz);
        return getDataTable(list);
    }

    //关系表
    @Autowired
    private IPubRelationService pubRelationService;

    //业务事件单的service
    @Autowired
    private IFmBizService fmBizService;

    //操作记录 tel_flow_log
    @Autowired
    private ITelFlowLogService telFlowLogService;

    //业务事件单的流程信息
    @PostMapping("/listFmBiz/{telId}")
    @ResponseBody
    public TableDataInfo listFmBiz(@PathVariable("telId") String telId) {
        startPage();
        //通过电话事件单的id获取业务事件单的id
        String pubType = "88";
        PubRelation relation  = new PubRelation();
        relation.setType(pubType);
        relation.setObj2Id(telId);
        List<PubRelation> pubRelationList =  pubRelationService.selectPubRelationList(relation);
        List listStr = new ArrayList();
        for(PubRelation pubRelation:pubRelationList){
            //通过业务事件单的id来获取业务事件单里面的信息
            FmBiz fmBiz = fmBizService.selectFmBizById( pubRelation.getObj1Id());

            listStr.add(fmBiz);
            return  getDataTable(listStr);
        }
        return getDataTable(listStr);
    }

    /**
     * 查询【流程】列表
     */
    @PostMapping("/listLog")
    @ResponseBody
    public TableDataInfo list(TelFlowLog telFlowLog, @PathVariable("telid") String telid) {
        startPage();
        TelBiz telBiz = phoneService.selectPhoneById(telid);
        List<TelFlowLog> listLog = telFlowLogService.selectTelFlowLogList(telFlowLog);
        return getDataTable(listLog);
    }

    //导出的电话事件单
    /*@Log(title = "电话事件单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelBiz telBiz)
    {
        if(StringUtils.isNotEmpty(telBiz.getStartTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(telBiz.getStartTime());
            telBiz.setStartTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(telBiz.getCloseTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(telBiz.getCloseTime());
            telBiz.setCloseTime(parseEnd+"240000");
        }

        String isCurrentPage = (String) telBiz.getParams().get("currentPage");
        String pid = ShiroUtils.getOgUser().getpId();
        telBiz.setCreateId(pid);
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<TelBiz> list = phoneService.selectPhoneList(telBiz);
            ExcelUtil<TelBiz> util = new ExcelUtil<TelBiz>(TelBiz.class);
            return util.exportExcel(list, "电话事件单");
        } else {
            List<TelBiz> list = phoneService.selectPhoneList(telBiz);
            ExcelUtil<TelBiz> util = new ExcelUtil<TelBiz>(TelBiz.class);
            return util.exportExcel(list, "电话事件单");
        }
    }*/

    //导出
    @Log(title = "电话事件单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelBiz telBiz)
    {
        String isCurrentPage = (String) telBiz.getParams().get("currentPage");
        if(StringUtils.isNotEmpty(telBiz.getCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(telBiz.getCreateTime());
            telBiz.setCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(telBiz.getStartTimeBiz())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(telBiz.getStartTimeBiz());
            telBiz.setStartTimeBiz(parseEnd+"240000");
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<TelBiz> list = phoneService.selectPhoneList(telBiz);
            ExcelUtil<TelBiz> util = new ExcelUtil<TelBiz>(TelBiz.class);
            return util.exportExcel(list, "电话事件单");
        } else if ("all".equals(isCurrentPage)) {
            List<TelBiz> list = phoneService.selectPhoneList(telBiz);
            ExcelUtil<TelBiz> util = new ExcelUtil<TelBiz>(TelBiz.class);
            return util.exportExcel(list, "电话事件单");
        }
        return null;
    }

    //查看详情
    @GetMapping("/detail/{telid}")
    public String detail(@PathVariable("telid") String telid, ModelMap mmap) {

        mmap.put("TelBiz", phoneService.selectPhoneById(telid));

        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String username = ogUser.getUsername();
        mmap.put("username",username);

        return prefix_cx + "/detail";
    }

}
