package com.ruoyi.activiti.controller.itsm.phone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.domain.TelFlowLog;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.activiti.service.ITelFlowLogService;
import com.ruoyi.activiti.service.ITelSkillService;
import com.ruoyi.activiti.service.impl.OgPhoneServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.TelTerminal;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ITelTerminalService;

/*
 *
 * 电话事件单（新增）
 * */
@Controller
@RequestMapping("phone/xz")
public class OgPhoneController extends BaseController {
    //新增界面路径
    private String prefix_xz = "phone/xz";

    //业务事件单路经
    private String prefix = "issueList/build";

    //新建（添加）业务事件单的路径    templates//fmbiz/flow/start.html
    private String prefix_public = "fmbiz";

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    private OgPhoneServiceImpl phoneService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ITelTerminalService telTerminalService;

    //关联关系service接口
    @Autowired
    private IPubRelationService iPubRelationService;

    //业务事件单的service
    @Autowired
    private IFmBizService fmBizService;

    //操作记录 tel_flow_log
    @Autowired
    private ITelFlowLogService telFlowLogService;

    //用戶
    @Autowired
    private IOgUserService ogUserService;

    //人员
    @Autowired
    private IOgPersonService ogPersonService;

    //关系表
    @Autowired
    private IPubRelationService pubRelationService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_xz + "/phone";
    }

    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix_xz + "/selectOneApplication";
    }

    //查询
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
        //分页
        startPage();
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userName = ogUser.getUsername();
        if (!"admin".equals(userName)) {
            telBiz.setCreateId(ogUser.getpId());
        }
        List<TelBiz> list = phoneService.selectPhoneList(telBiz);
        return getDataTable(list);
    }

    @Autowired
    private ITelSkillService skillService;

    //去到新增页面
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        //公用的一个生成时间单号p'hon
        String bizType = "DHSJ"; //YJSJ   BB
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        String username = ogUser.getUsername();

        TelTerminal terminal = new TelTerminal();

        OgUser ogUser1 = new OgUser();
        ogUser1.setUsername(username);
        terminal.setOgUser(ogUser1);

        //之前按组号查询的
        /*List<TelTerminal> telTerminals = telTerminalService.selectTelTerminalListThree(terminal);
        if (telTerminals.size() > 0) {
            TelTerminal terminal1 = telTerminals.get(0);
            String serviceIp = terminal1.getServiceIp();
            String s1 = serviceIp.substring(1, 4);
            mmap.put("groupNo", s1);
        }*/

        //技能组的
        String groupNo=phoneService.getGroupNo();
        mmap.put("groupNo",groupNo);
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime", nowTime);

        return prefix_xz + "/add";
    }


    /**
     * 获取当前登陆人员信息
     *
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId() {
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }

    //添加保存
    @Log(title = "电话事件单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TelBiz telBiz) {
        telBiz.setTelid(UUID.getUUIDStr());
        String personId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(personId);
        telBiz.setCreateId(ogPerson.getpId());
        telBiz.setStartTime(ogPerson.getpName());

        if(StringUtils.isEmpty(telBiz.getCloseTime())){
            Date nowDate = DateUtils.getNowDate();
            String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
            //赋值挂机时间   关闭时间
            telBiz.setCloseTime(nowTime);
        }

        if (StringUtils.isNotEmpty(telBiz.getCreateTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getCreateTime());
            telBiz.setCreateTime(parseStart);
        }
        if (StringUtils.isNotEmpty(telBiz.getCloseTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getCloseTime());
            telBiz.setCloseTime(parseStart);
        }
        if (StringUtils.isNotEmpty(telBiz.getOverFlowTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getOverFlowTime());
            telBiz.setOverFlowTime(parseStart);
        }
        if (StringUtils.isNotEmpty(telBiz.getCloseTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getCloseTime());
            telBiz.setCloseTime(parseStart);
        }

        int i = phoneService.insertOgPhone(telBiz);

        if (!"1".equals(telBiz.getState())) {
            //流程记录
            TelFlowLog telFlowLog = new TelFlowLog();
            telFlowLog.setLogId(UUID.getUUIDStr());
            telFlowLog.setCreatTime(telBiz.getCreateTime());
            telFlowLog.setTelId(telBiz.getTelid());
            OgPerson ogPerson1 = ogPersonService.selectOgPersonById(telBiz.getCreateId());
            telFlowLog.setOperator(ogPerson1.getpName());
            telFlowLog.setOperatorTel(ogPerson1.getMobilPhone());
            telFlowLog.setSerialNum("1");
            telFlowLog.setOperationName("电话接入");
            telFlowLog.setState("待提交");
            telFlowLogService.insertTelFlowLog(telFlowLog);

            TelFlowLog telBizFlowLog = new TelFlowLog();
            telBizFlowLog.setLogId(UUID.getUUIDStr());
            telBizFlowLog.setCreatTime(telBiz.getCreateTime());
            telBizFlowLog.setTelId(telBiz.getTelid());
            telBizFlowLog.setSerialNum("2");
            telBizFlowLog.setOperationName("工单提交");
            telBizFlowLog.setOperator(ogPerson1.getpName());
            telBizFlowLog.setOperatorTel(ogPerson1.getMobilPhone());
            if (("1".equals(telBiz.getIsSolve()))) {
                telBiz.setState("3");
                telBizFlowLog.setState("已处理");
            } else if ("2".equals(telBiz.getIsSolve())) {
                telBiz.setState("2");
                telBizFlowLog.setState("未处理");
            }
            phoneService.updatePhone(telBiz);
            telFlowLogService.insertTelFlowLog(telBizFlowLog);
        }
        return toAjax(i);
    }

    //去修改页面
    @GetMapping("/edit/{telid}")
    public String edit(@PathVariable("telid") String telid, ModelMap mmap) {
        mmap.put("TelBiz", phoneService.selectPhoneById(telid));
        return prefix_xz + "/edit";
    }

    //修改完保存
    @Log(title = "电话事件单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TelBiz telBiz) {

        if (StringUtils.isNotEmpty(telBiz.getCreateTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getCreateTime());
            telBiz.setCreateTime(parseStart);
        }
        if (StringUtils.isNotEmpty(telBiz.getCloseTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getCloseTime());
            telBiz.setCloseTime(parseStart);
        }
        if (StringUtils.isNotEmpty(telBiz.getOverFlowTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(telBiz.getOverFlowTime());
            telBiz.setOverFlowTime(parseStart);
        }

        if (!"1".equals(telBiz.getState())) {
            //流程记录
            TelFlowLog telFlowLog = new TelFlowLog();
            telFlowLog.setTelId(telBiz.getTelid());
            telFlowLog.setLogId(UUID.getUUIDStr());
            telFlowLog.setCreatTime(telBiz.getCreateTime());
            telFlowLog.setTelId(telBiz.getTelid());
            TelBiz telBizCreateId = phoneService.selectPhoneById(telBiz.getTelid());
            OgPerson ogPerson1 = ogPersonService.selectOgPersonById(telBizCreateId.getCreateId());
            telFlowLog.setOperator(ogPerson1.getpName());
            telFlowLog.setOperatorTel(ogPerson1.getMobilPhone());
            telFlowLog.setSerialNum("1");
            telFlowLog.setOperationName("电话接入");
            telFlowLog.setState("待提交");
            telFlowLogService.insertTelFlowLog(telFlowLog);

            TelFlowLog telBizFlowLog = new TelFlowLog();

            telBizFlowLog.setLogId(UUID.getUUIDStr());
            telBizFlowLog.setCreatTime(telBiz.getCreateTime());
            telBizFlowLog.setTelId(telBiz.getTelid());
            telBizFlowLog.setSerialNum("2");
            telBizFlowLog.setOperationName("工单提交");
            telBizFlowLog.setState("已处理");
            telBizFlowLog.setOperator(ogPerson1.getpName());
            telBizFlowLog.setOperatorTel(ogPerson1.getMobilPhone());
            telFlowLogService.insertTelFlowLog(telBizFlowLog);

            if (("1".equals(telBiz.getIsSolve()))) {
                telBiz.setState("3");
                telBizFlowLog.setState("已处理");
            } else if ("2".equals(telBiz.getIsSolve())) {
                telBiz.setState("2");
                telBizFlowLog.setState("未处理");
            }
        }
        return toAjax(phoneService.updatePhone(telBiz));
    }

    //去关闭页面
    @GetMapping("/close/{telid}")
    public String close(@PathVariable("telid") String telid, ModelMap mmap) {
        mmap.put("TelBiz", phoneService.selectPhoneById(telid));
        return prefix_xz + "/close";
    }

    //关闭完保存
    @Log(title = "电话事件单", businessType = BusinessType.UPDATE)
    @PostMapping("/close")
    @ResponseBody
    public AjaxResult close(TelBiz telBiz) {

            TelBiz telBizCreateId = phoneService.selectPhoneById(telBiz.getTelid());
            OgPerson ogPerson1 = ogPersonService.selectOgPersonById(telBizCreateId.getCreateId());

            TelFlowLog telBizFlowLog = new TelFlowLog();
            Date nowDate = DateUtils.getNowDate();
            String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);

            //流程记录
            telBizFlowLog.setLogId(UUID.getUUIDStr());
            telBizFlowLog.setCreatTime(nowTime);
            telBizFlowLog.setTelId(telBiz.getTelid());
            telBizFlowLog.setSerialNum("3");
            telBizFlowLog.setOperationName("工单提交");
            telBizFlowLog.setState("已处理");
            telBizFlowLog.setOperator(ogPerson1.getpName());
            telBizFlowLog.setOperatorTel(ogPerson1.getMobilPhone());
            telFlowLogService.insertTelFlowLog(telBizFlowLog);

        return toAjax(phoneService.updatePhone(telBiz));
    }

    //删除草稿
    @Log(title = "电话事件单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(phoneService.deletePhoneByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    //电话接入自动弹出工单
    @GetMapping("/addTwo/{g_ani}/{g_type}")
    public String open_telBiz_page(ModelMap mmap, @PathVariable("g_ani") String g_ani,
                                   @PathVariable("g_type") String g_type) {
        //公用的一个生成时间单号   YJSJ   BB
        String bizType = "DHSJ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String username = ogUser.getUsername();

        TelTerminal terminal = new TelTerminal();
        OgUser ogUser1 = new OgUser();
        ogUser1.setUsername(username);
        terminal.setOgUser(ogUser1);

        //截取组号 也就是话机终端的电话号
        List<TelTerminal> telTerminals = telTerminalService.selectTelTerminalListThree(terminal);
        if (telTerminals.size() > 0) {
            TelTerminal terminal1 = telTerminals.get(0);
            String serviceIp = terminal1.getServiceIp();
            String s1 = serviceIp.substring(1, 4);
            mmap.put("groupNoTwo", s1);
        }

        //技能组的
        String groupNo=phoneService.getGroupNo();
        mmap.put("groupNo",groupNo);

        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        mmap.put("nowTime", nowTime);

        //电话接入时直接赋值给它
        //电话 溢出组  溢出时间
        mmap.put("contactPhone", g_ani);
        mmap.put("overFlowGroup", g_type);
        mmap.put("overFlowTime", nowTime);

        return prefix_xz + "/addTwo";
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping("/selectById")
    @ResponseBody
    public AjaxResult selectById(String telid) {
        AjaxResult ajaxResult = new AjaxResult();
        TelBiz telBiz = phoneService.selectPhoneById(telid);
        ajaxResult.put("data", telBiz);
        return ajaxResult;
    }

    //查看
    @GetMapping("/detail/{telid}")
    public String detail(@PathVariable("telid") String telid, ModelMap mmap) {
        mmap.put("TelBiz", phoneService.selectPhoneById(telid));
        return prefix_xz + "/detail";
    }

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


    //跳转事务事件单界面
    @GetMapping("/alter/{telid}")
    public String alter(ModelMap mmap, @PathVariable("telid") String telid) {
        //打开新建页面生成单号
        String bizType = "SJ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        //打开新建页面回显创建机构填报人及创建机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());
        mmap.put("occurrenceOrgName", org.getOrgName());
        mmap.put("occurrenceOrgId", org.getOrgId());
        mmap.put("loginName", person.getpName());
        mmap.put("contactPhone", person.getPhone());
        mmap.put("reportTime", DateUtils.getTime());

        //获取电话事件里的基本信息
        TelBiz telBiz = phoneService.selectPhoneById(telid);
        mmap.put("reportTime", DateUtils.dateTimeNow());
        mmap.put("loginName", telBiz.getContactName());
        mmap.put("contactPhone", telBiz.getContactPhone());

        mmap.put("sysid", telBiz.getSysId());

        mmap.put("applicationName", telBiz.getCaption());
        mmap.put("content", telBiz.getContent());
        mmap.put("faultDecriptDetail", telBiz.getContent());
        mmap.put("telId", telBiz.getTelid());

        //机构
        String contactOrg = telBiz.getContactOrg();
        mmap.put("occurrenceOrgId", contactOrg);

        //fmbiz.setFaultSource("05");    //05电话事件单转
        mmap.put("fmSource", "05");

        //fmbiz.setFaultSource("05");
        //mmap.put("faultSource",faultSource);

        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String s = ogUser.getpId();
        String s1 = s.substring(1, 4);
        mmap.put("groupNo", s1);

        return prefix_xz + "/start";
    }


    //根据事件单ID业务事件单
    @PostMapping("/ywList/{telid}")
    @ResponseBody
    public TableDataInfo getImWt(@PathVariable("telid") String telid) {
        PubRelation pr = new PubRelation();
        List<FmBiz> fmbiz = new ArrayList<>();
        pr.setObj1Id(telid);
        pr.setType("15");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                FmBiz cb = fmBizService.selectFmBizById(prtion.getObj2Id());
                if (cb != null) {
                    fmbiz.add(cb);
                }
            }
        }
        return getDataTable(fmbiz);
    }

    //查询电话事件单流程记录
    @PostMapping("/listTwo")
    @ResponseBody
    public TableDataInfo listTwo(TelFlowLog telFlowLog) {
        List<TelFlowLog> listTwo = telFlowLogService.selectTelFlowLogList(telFlowLog);
        return getDataTable(listTwo);
    }

}
