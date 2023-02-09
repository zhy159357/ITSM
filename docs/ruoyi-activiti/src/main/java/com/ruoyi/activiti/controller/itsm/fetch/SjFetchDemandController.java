package com.ruoyi.activiti.controller.itsm.fetch;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.SjFetchDemand;
import com.ruoyi.activiti.domain.SjFetchSingle;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2021-11-23
 */
@Controller
@RequestMapping("/fetch/demand")
public class SjFetchDemandController extends BaseController
{
    private String prefix = "fetch/demand";

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SjFetchSingleController.class);

    @Autowired
    private ISjFetchDemandService sjFetchDemandService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ISjFetchSingleService sjFetchSingleService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IPubAttachmentService iPubAttachmentService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @GetMapping()
    public String demand()
    {
        return prefix + "/myDemandList";
    }

    @Value("${cntxtag.enabled}")
    private String cntxtag;

    /**
     * 查询【我的数据需求单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SjFetchDemand sjFetchDemand)
    {
        startPage();
        sjFetchDemand.setCreateId(ShiroUtils.getUserId());
        List<SjFetchDemand> list = sjFetchDemandService.selectSjFetchDemandList(sjFetchDemand);
        return getDataTable(list);
    }

    /**
     * 查询数据需求单List
     */
    @GetMapping("/selectList")
    public String selectList()
    {
        return prefix + "/demandList";
    }

    /**
     * 查询【数据需求单】列表
     */
    @PostMapping("/demandList")
    @ResponseBody
    public TableDataInfo demandList(SjFetchDemand sjFetchDemand)
    {
        startPage();
        List<SjFetchDemand> list = sjFetchDemandService.selectSjFetchDemandList(sjFetchDemand);
        return getDataTable(list);
    }

    /**
     * 导出【数据需求单】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SjFetchDemand sjFetchDemand)
    {
        List<SjFetchDemand> list = sjFetchDemandService.selectSjFetchDemandList(sjFetchDemand);
        ExcelUtil<SjFetchDemand> util = new ExcelUtil<SjFetchDemand>(SjFetchDemand.class);
        return util.exportExcel(list, "demand");
    }

    /**
     * 新增【数据需求单】
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap)
    {
        //自动对应类型单子的单号
        String fetchNo = getFetchNo();
        modelMap.put("fetchNo", fetchNo);
        modelMap.put("fetchId", UUID.getUUIDStr());
        modelMap.put("cntxtag", cntxtag);
        return prefix + "/add";
    }

    /**
     * OA创建数据提取单
     *
     * @param sjFetchDemand
     * @return
     */
    @Log(title = "OA创建数据提取单", businessType = BusinessType.UPDATE)
    @PostMapping("/addSave")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(SjFetchDemand sjFetchDemand) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        String processDefinitionKey = "DaPu";
        //获取当前登陆人
        String pId = ShiroUtils.getOgUser().getpId();
        sjFetchDemand.setCreateId(pId);
        sjFetchDemand.setCreatTime(DateUtils.dateTimeNow());
        sjFetchDemand.setSourceType("02");
        sjFetchDemand.setSourceTypeText("OA");
        //获取当前提交是暂存还是提交
        Object obj = sjFetchDemand.getParams().get("flag");
        if (StringUtils.isNotNull(obj)) {
            String flag = sjFetchDemand.getParams().get("flag").toString();
            if ("1".equals(flag)) {
                String[] sysIdArr = sjFetchDemand.getSysId().split(",");
                String[] sysnameArr = sjFetchDemand.getSysName().split(",");
                String[] sysManagerIdArr = sjFetchDemand.getSysManagerId().split(",");
                String[] sysManagerNameArr = sjFetchDemand.getSysManagerName().split(",");
                if (sysnameArr.length != sysManagerIdArr.length) {
                    return AjaxResult.error("所选的涉及系统与系统负责人个数不匹配");
                }
                sjFetchDemand.setFetchState("02"); //02待系统负责人处理
                sjFetchDemand.setFetchStateText("待处理");
                sjFetchDemand.setDealSchdule("0/" + sjFetchDemand.getSysName().split(",").length);
                String businessTypeText = getBusinessTypeText(sjFetchDemand);
                if (StringUtils.isNotEmpty(businessTypeText)) {
                    sjFetchDemand.setBusinessTypeText(businessTypeText);
                }
                SjFetchDemand sjFetchDemand1 = sjFetchDemandService.selectSjFetchDemandById(sjFetchDemand.getFetchId());
                if (sjFetchDemand1 == null) {
                    sjFetchDemandService.insertSjFetchDemand(sjFetchDemand);
                } else {
                    sjFetchDemandService.updateSjFetchDemand(sjFetchDemand);
                }
                try {
                    for (int i = 0 ; i < sysnameArr.length ; i ++) {
                        SjFetchSingle sjFetchSingle = getSjFetchSingle(sjFetchDemand);
                        sjFetchSingle.setSysId(sysIdArr[i]);
                        sjFetchSingle.setSysName(sysnameArr[i]);
                        sjFetchSingle.setSysManagerId(sysManagerIdArr[i]);
                        sjFetchSingle.setSysManagerName(sysManagerNameArr[i]);
                        sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                        reMap.put("reCode", "0");
                        //获取系统的负责人信息
                        reMap.put("dealId", sjFetchSingle.getSysManagerId());
                        //启动流程实例
                        String businessKey = sjFetchSingle.getFetchId();
                        activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("数据提取单新增失败", e);
                    throw new BusinessException("数据提取单新增失败,请刷新页面进行重试");
                }
                //TODO 短信业务
                String[] pids = sjFetchDemand.getSysManagerId().split(",");
                String text = "数据提取单号："+ sjFetchDemand.getFetchNo() +"已提交，请登录运维管理平台处理";
                for (int i = 0; i < pids.length; i++) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
                    sendSms(text, ogPerson, ShiroUtils.getUserId());
                }
                return AjaxResult.success("操作成功");
            } else {
                //当前状态为待提交
                sjFetchDemand.setFetchState("01");
                sjFetchDemand.setFetchStateText("待提交");
                if (StringUtils.isNotEmpty(sjFetchDemand.getBusinessType()) && StringUtils.isNotNull(sjFetchDemand.getBusinessType())) {
                    String businessTypeText = getBusinessTypeText(sjFetchDemand);
                    if (StringUtils.isNotEmpty(businessTypeText)) {
                        sjFetchDemand.setBusinessTypeText(businessTypeText);
                    }
                }
                try {
                    SjFetchDemand sjFetchDemand1 = sjFetchDemandService.selectSjFetchDemandById(sjFetchDemand.getFetchId());
                    if (sjFetchDemand1 == null) {
                        sjFetchDemandService.insertSjFetchDemand(sjFetchDemand);
                    } else {
                        sjFetchDemandService.updateSjFetchDemand(sjFetchDemand);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("数据提取单暂存失败: {} ", e);
                    return AjaxResult.error("数据提取单暂存失败");
                }
                return AjaxResult.success("数据提取单暂存成功");
            }
        } else {
            return AjaxResult.error("OA创建数据提取单flag参数为null");
        }

    }

    /**
     * 新增保存【数据提取单】
     */
    @Log(title = "【数据提取单新增】", businessType = BusinessType.INSERT)
    @PostMapping("/bigDataAddSave")
    @ResponseBody
    @RepeatSubmit
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult bigDataAddSave(HttpServletRequest req) {
        SjFetchDemand sjFetchDemand = new SjFetchDemand();
        try {
            sjFetchDemand = this.parseRequest(req);
        } catch (Exception e) {
            return AjaxResult.error("附件不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getSourceType())) {
            return AjaxResult.error("需求来源不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getTitleName())) {
            return AjaxResult.error("需求标题不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getProcessid())) {
            return AjaxResult.error("流程号不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getOrgName())) {
            return AjaxResult.error("联系部门不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getPersonName())) {
            return AjaxResult.error("联系人不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getBusinessNumber())) {
            return AjaxResult.error("需求单号不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getBusinessType())) {
            return AjaxResult.error("需求类别不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getSysName())) {
            return AjaxResult.error("涉及系统不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getInterfaceName())) {
            return AjaxResult.error("接口人姓名不能为空");
        }
        if (StringUtils.isEmpty(sjFetchDemand.getInterfacePhone())) {
            return AjaxResult.error("接口人手机号不能为空");
        }
        if (sjFetchDemand.getFileSize() == null || sjFetchDemand.getFileSize() == 0) {
            return AjaxResult.error("附件个数不能为空");
        }
        Map<String, Object> reMap = new HashMap<>();
        String processDefinitionKey = "DaPu";
        //1.判断当前请求是否已经形成数据提取单 根据流程实例id查询
        SjFetchDemand fetchDemand = sjFetchDemandService.selectSjFetchDemandByProcessId(sjFetchDemand.getProcessid());
        if (fetchDemand != null) { //已存在
            return AjaxResult.error("数据提取单流程id已存在，不可重复发送", "流程实例Id:" + fetchDemand.getProcessid());
        }
        Map<String,Object> map1 = new HashMap<>();
        map1.put("username","admin");
        map1.put("dbType",dbType);
        String pid = ogUserService.selectUserByLoginName(map1).getpId();
        //生成数据需求单+
        SjFetchDemand fetch = new SjFetchDemand();
        BeanUtils.copyProperties(sjFetchDemand, fetch);
        fetch.setFetchId(UUID.getUUIDStr());
        fetch.setCreateId(pid);
        fetch.setFetchNo(getFetchNo());
        fetch.setSourceType("01");
        fetch.setSourceTypeText("大数据门户");
        List<PubParaValue> business_type = pubParaValueService.selectPubParaValueByParaName("business_type");
        String businessTypeText = "";
        for (PubParaValue pubParaValue : business_type) {
            if (pubParaValue.getValue().equals(sjFetchDemand.getBusinessType())) {
                businessTypeText = pubParaValue.getValueDetail();
            }
        }
        fetch.setBusinessTypeText(businessTypeText);
        fetch.setCreatTime(DateUtils.dateTimeNow());

        //判断文件是否丢失
        Integer fileLen = sjFetchDemand.getFiles().length;
        if (sjFetchDemand.getFileSize().longValue() != fileLen) {
            return AjaxResult.error("传输文件数量错误", null);
        } else {
            //进行文件的上传
            try {
                for (MultipartFile file : sjFetchDemand.getFiles()) {
                    String[] arr = FastDFSHelper.getInstance().uploadBreakpointFile(file);
                    Attachment attachment = new Attachment();
                    attachment.setAtId(UUID.getUUIDStr());
                    attachment.setCreateId(pid);//先给他默认成管理员 上传附件人员
                    attachment.setOwnerId(fetch.getFetchId());
                    attachment.setSize(Convert.convertSize(file.getSize()));
                    attachment.setFileTime(DateUtils.dateTimeNow());
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setGroupName(arr[0]);
                    attachment.setFilePath(arr[1]);
                    attachment.setFileType("1");// 附件类型默认为1
                    iPubAttachmentService.insertAttachment(attachment);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("上传文件失败，失败原因:" + e.getMessage());
            }
        }

        String sysnameJson = sjFetchDemand.getSysName();
        List<Map> parseList = (List) JSONArray.parse(sysnameJson);
        String createId = "";
        String sysName = "";
        String sysId = "";
        String sysManagerId = "";
        String sysManagerName = "";
        //判断系统名称系统负责人是否存在
        for (Map<String, Object> map : parseList) {
            String sysname1 = (String)map.get("sysName");
            if (StringUtils.isEmpty(sysname1)) {
                return AjaxResult.error("系统名称不能为空");
            }
            String leader = (String)map.get("leader");
            if (StringUtils.isEmpty(leader)) {
                return AjaxResult.error("系统负责人不能为空");
            }
            String telephone = (String)map.get("telephone");
            if (StringUtils.isEmpty(telephone)) {
                return AjaxResult.error("负责人电话不能为空");
            }
            OgSys ogSys = new OgSys();
            ogSys.setCaption(sysname1);
            List<OgSys> sysList = applicationManagerService.selectOgSysList(ogSys);
            if (sysList == null) {
                return AjaxResult.error("系统名称不存在，系统名称：", sysname1);
            }
            OgPerson person = new OgPerson();
            person.setpName(leader);
            person.setMobilPhone(telephone);
            List<OgPerson> ogPersonList = ogPersonService.selectList(person);
            if (ogPersonList == null) {
                person.setPhone(telephone);
                ogPersonList = ogPersonService.selectList(person);
                if (ogPersonList == null) {
                    return AjaxResult.error("系统负责人不存在，系统负责人：", leader + "电话：" + telephone);
                }
            }
        }
        //入库  启动流程
        try {
            for (Map<String, Object> map : parseList) {
                fetch.setFetchState("02"); //02待处理
                fetch.setFetchStateText("待处理"); //02待处理文本
                String sysname1 = (String)map.get("sysName");
                String leader = (String)map.get("leader");
                String telephone = (String)map.get("telephone");
                OgSys ogSys = new OgSys();
                ogSys.setCaption(sysname1);
                List<OgSys> sysList = applicationManagerService.selectOgSysList(ogSys);
                if (sysList == null) {
                    return AjaxResult.error("系统名称不存在，系统名称：", sysname1);
                }
                OgPerson person = new OgPerson();
                person.setpName(leader);
                person.setMobilPhone(telephone);
                List<OgPerson> ogPersonList = ogPersonService.selectList(person);
                if (ogPersonList == null) {
                    person.setPhone(telephone);
                    ogPersonList = ogPersonService.selectList(person);
                    if (ogPersonList == null) {
                        return AjaxResult.error("系统负责人不存在，系统负责人：", leader + "电话：" + telephone);
                    }
                }
                sysName += sysname1 + ",";
                sysId += sysList.get(0).getSysId() + ",";
                person = ogPersonList.get(0);
                sysManagerId += person.getpId() + ",";
                sysManagerName += person.getpName() + ",";
                SjFetchSingle sjFetchSingle = getSjFetchSingle(fetch);
                sjFetchSingle.setSysId(sysList.get(0).getSysId());
                sjFetchSingle.setSysName(sysname1);
                sjFetchSingle.setSysManagerId(person.getpId());
                sjFetchSingle.setSysManagerName(person.getpName());
                sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                reMap.put("reCode", "0");
                //获取系统的负责人信息
                reMap.put("userId", person.getpId());
                reMap.put("dealId", sjFetchSingle.getSysManagerId());
                //启动流程实例
                String businessKey = sjFetchSingle.getFetchId();
                activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                //TODO 短信业务
                OgPerson ogPerson = ogPersonService.selectOgPersonById(person.getpId());
                String text = "数据提取单号："+ fetch.getFetchNo() +"已提交，请登录运维管理平台处理";
                sendSms(text, ogPerson, "8b8080f457fffe39015800015ce60006");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据提取单新增失败", e);
            return AjaxResult.error("数据提取单新增失败,请刷新页面进行重试");
        }
        fetch.setSysId(sysId);
        fetch.setSysName(sysName);//涉及系统
        fetch.setSysManagerId(sysManagerId);
        fetch.setSysManagerName(sysManagerName);//系统负责人
        fetch.setDealSchdule("0/" + sysName.split(",").length);
        sjFetchDemandService.insertSjFetchDemand(fetch);
        return AjaxResult.success("数据提取单操作成功", sjFetchDemand.getFetchNo());
    }

    private String getBusinessTypeText(SjFetchDemand sjFetchDemand) {
        String businessTypeText = "";
        //判断需求类别是否选择
        if (StringUtils.isNotEmpty(sjFetchDemand.getBusinessType())) {
            List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("business_type");

            if (list.size() > 0) {
                list = list.stream().filter(item -> item.getValue().equals(sjFetchDemand.getBusinessType())).collect(Collectors.toList());
                if (list.size() > 0) {
                    businessTypeText = list.get(0).getValueDetail();
                }
            }
        }
        return businessTypeText;
    }

    /**
     * 获取数据提取单单号
     *
     * @return
     */
    private String getFetchNo() {
        String bizType = "DaPu";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        String fetchNo = bizType + nowDateStr + numStr;
        return fetchNo;
    }

    /**
     * 修改【数据需求单】
     */
    @GetMapping("/edit/{fetchId}")
    public String edit(@PathVariable("fetchId") String fetchDemandId, ModelMap mmap)
    {
        SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(fetchDemandId);
        mmap.put("sjFetchDemand", sjFetchDemand);
        mmap.put("cntxtag", cntxtag);
        return prefix + "/edit";
    }

    /**
     * 修改保存【数据需求单】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SjFetchDemand sjFetchDemand)
    {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        String processDefinitionKey = "DaPu";
        //获取当前提交是暂存还是提交
        String flag = sjFetchDemand.getParams().get("flag").toString();
        if ("1".equals(flag)) {
            String[] sysIdArr = sjFetchDemand.getSysId().split(",");
            String[] sysnameArr = sjFetchDemand.getSysName().split(",");
            String[] sysManagerIdArr = sjFetchDemand.getSysManagerId().split(",");
            String[] sysManagerNameArr = sjFetchDemand.getSysManagerName().split(",");
            if (sysnameArr.length != sysManagerIdArr.length) {
                return AjaxResult.error("所选的涉及系统与系统负责人个数不匹配");
            }
            sjFetchDemand.setFetchState("02"); //02待系统负责人处理
            sjFetchDemand.setFetchStateText("待处理");
            sjFetchDemand.setDealSchdule("0/" + sjFetchDemand.getSysName().split(",").length);
            String businessTypeText = getBusinessTypeText(sjFetchDemand);
            if (StringUtils.isNotEmpty(businessTypeText)) {
                sjFetchDemand.setBusinessTypeText(businessTypeText);
            }
            sjFetchDemandService.updateSjFetchDemand(sjFetchDemand);
            sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(sjFetchDemand.getFetchId());
            try {
                for (int i = 0 ; i < sysnameArr.length ; i ++) {
                    SjFetchSingle sjFetchSingle = getSjFetchSingle(sjFetchDemand);
                    sjFetchSingle.setSysId(sysIdArr[i]);
                    sjFetchSingle.setSysName(sysnameArr[i]);
                    sjFetchSingle.setSysManagerId(sysManagerIdArr[i]);
                    sjFetchSingle.setSysManagerName(sysManagerNameArr[i]);
                    sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                    reMap.put("reCode", "0");
                    //获取系统的负责人信息
                    reMap.put("dealId", sjFetchSingle.getSysManagerId());
                    //启动流程实例
                    String businessKey = sjFetchSingle.getFetchId();
                    activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("数据提取单新增失败", e);
                throw new BusinessException("数据提取单新增失败,请刷新页面进行重试");
            }
            //TODO 短信业务
            String[] pids = sjFetchDemand.getSysManagerId().split(",");
            String text = "数据提取单号："+ sjFetchDemand.getFetchNo() +"已提交，请登录运维管理平台处理";
            for (int i = 0; i < pids.length; i++) {
                OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
                sendSms(text, ogPerson, ShiroUtils.getUserId());
            }
            return AjaxResult.success("操作成功");
        } else {
            //当前状态为待提交
            sjFetchDemand.setFetchState("01");
            sjFetchDemand.setFetchStateText("待提交");
            if (StringUtils.isNotEmpty(sjFetchDemand.getBusinessType()) && StringUtils.isNotNull(sjFetchDemand.getBusinessType())) {
                String businessTypeText = getBusinessTypeText(sjFetchDemand);
                if (StringUtils.isNotEmpty(businessTypeText)) {
                    sjFetchDemand.setBusinessTypeText(businessTypeText);
                }
            }
            sjFetchDemandService.updateSjFetchDemand(sjFetchDemand);
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 删除【数据需求单】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sjFetchDemandService.deleteSjFetchDemandByIds(ids));
    }

    /**
     * 数据需求单详情页面
     *
     * @param fetchId
     * @param mmap
     * @return
     */
    @GetMapping("/details/{fetchId}")
    public String details(@PathVariable("fetchId") String fetchId, ModelMap mmap) {
        SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(fetchId);
        mmap.put("sjFetchDemand", sjFetchDemand);
        return prefix + "/details";
    }

    /**
     * 获取当前数据提取单的所涉及的系统名称
     *
     * @param id
     * @return
     */
    @GetMapping("/selectSjFetchDemandById/{id}")
    @ResponseBody
    public AjaxResult selectById(@PathVariable("id") String id) {
        AjaxResult ajaxResult = new AjaxResult();
        SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(id);
        if (StringUtils.isNotEmpty(sjFetchDemand.getSysName()) && StringUtils.isNotNull(sjFetchDemand.getSysId())) {
            String[] split = sjFetchDemand.getSysId().split(",");
            ajaxResult.put("data", split);
        } else {
            ajaxResult.put("data", "0");
        }
        return ajaxResult;
    }

    /**
     * 暂存
     *
     * @return
     */
    @Log(title = "新增保存", businessType = BusinessType.INSERT)
    @PostMapping("/fileSave")
    @ResponseBody
    public AjaxResult fileSave(SjFetchDemand sjFetchDemand) {
        try {
            if (StringUtils.isNotEmpty(sjFetchDemand.getFetchId())) {
                SjFetchDemand sjFetchDemand1 = sjFetchDemandService.selectSjFetchDemandById(sjFetchDemand.getFetchId());
                if (sjFetchDemand1 == null) {
                    sjFetchDemand.setCreateId(ShiroUtils.getOgUser().getpId());
                    sjFetchDemand.setCreatTime(DateUtils.dateTimeNow());
                    sjFetchDemand.setSourceType("02");
                    sjFetchDemand.setSourceTypeText("OA");
                    sjFetchDemand.setFetchState("01");
                    sjFetchDemand.setFetchStateText("待提交");
                    sjFetchDemandService.insertSjFetchDemand(sjFetchDemand);
                    return AjaxResult.success("操作成功", sjFetchDemand.getFetchNo());
                } else {
                    sjFetchDemandService.updateSjFetchDemand(sjFetchDemand);
                    return AjaxResult.success("操作成功", sjFetchDemand.getFetchNo());
                }
            } else {
                return AjaxResult.error("操作失败，ID不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据提取单暂存失败: {}", e);
            throw new BusinessException("暂存失败，单号是：" + sjFetchDemand.getFetchNo());
        }

    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    public void sendSms(String setSmsText, OgPerson person, String userId) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(userId);// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    //对象转换
    private SjFetchSingle getSjFetchSingle(SjFetchDemand sjFetchDemand) {
        SjFetchSingle sjFetchSingle = new SjFetchSingle();
        sjFetchSingle.setFetchId(UUID.getUUIDStr());
        sjFetchSingle.setFetchNo(sjFetchDemand.getFetchNo());
        sjFetchSingle.setCreatTime(sjFetchDemand.getCreatTime());
        sjFetchSingle.setCreateId(sjFetchDemand.getCreateId());
        sjFetchSingle.setSourceType(sjFetchDemand.getSourceType());
        sjFetchSingle.setSourceTypeText(sjFetchDemand.getSourceTypeText());
        sjFetchSingle.setTitleName(sjFetchDemand.getTitleName());
        sjFetchSingle.setProcessid(sjFetchDemand.getProcessid());
        sjFetchSingle.setOrgName(sjFetchDemand.getOrgName());
        sjFetchSingle.setOrgId(sjFetchDemand.getOrgId());
        sjFetchSingle.setPersonName(sjFetchDemand.getPersonName());
        sjFetchSingle.setBusinessNumber(sjFetchDemand.getBusinessNumber());
        sjFetchSingle.setBusinessType(sjFetchDemand.getBusinessType());
        sjFetchSingle.setBusinessTypeText(sjFetchDemand.getBusinessTypeText());
        sjFetchSingle.setBusinessOrgName(sjFetchDemand.getBusinessOrgName());
        sjFetchSingle.setBusinessOrgId(sjFetchDemand.getBusinessOrgId());
        sjFetchSingle.setBbusinessPersonName(sjFetchDemand.getBbusinessPersonName());
        sjFetchSingle.setSysName(sjFetchDemand.getSysName());
        sjFetchSingle.setSysId(sjFetchDemand.getSysId());
        sjFetchSingle.setInterfaceName(sjFetchDemand.getInterfaceName());
        sjFetchSingle.setInterfacePhone(sjFetchDemand.getInterfacePhone());
        sjFetchSingle.setFileSize(sjFetchDemand.getFileSize());
        sjFetchSingle.setFetchState(sjFetchDemand.getFetchState());
        sjFetchSingle.setCreatTimeText(sjFetchDemand.getCreatTimeText());
        sjFetchSingle.setFetchStateText(sjFetchDemand.getFetchStateText());
        sjFetchSingle.setGroupId(sjFetchDemand.getGroupId());
        sjFetchSingle.setBbusinessPersonName(sjFetchDemand.getBbusinessPersonName());
        sjFetchSingle.setAssingId(sjFetchDemand.getAssingId());
        sjFetchSingle.setSysOrgId(sjFetchDemand.getSysOrgId());
        sjFetchSingle.setSysOrgName(sjFetchDemand.getSysOrgName());
        sjFetchSingle.setSysManagerId(sjFetchDemand.getSysManagerId());
        sjFetchSingle.setSysManagerName(sjFetchDemand.getSysManagerName());
        sjFetchSingle.setDealId(sjFetchDemand.getDealId());
        sjFetchSingle.setAuditBackId(sjFetchDemand.getAuditBackId());
        sjFetchSingle.setCollectBackId(sjFetchDemand.getCollectBackId());
        sjFetchSingle.setFetchDemandId(sjFetchDemand.getFetchId());
        sjFetchSingle.setDealFlag("1");//系统负责人处理
        return sjFetchSingle;
    }

    public SjFetchDemand parseRequest(HttpServletRequest req) {
        SjFetchDemand sjFetchDemand = new SjFetchDemand();
        Map<String, Object> dataMap = MapDataUtil.convertDataMap(req);
        ConverterUtils.mapToObject(sjFetchDemand, dataMap);

        log.info("新增数据提取接口对象=" + sjFetchDemand.toString());
        // 校验实体类对象，主要是非空检查，非空校验不通过抛出异常信息
        //sjFetchDemand.validate();

        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) req;
        Map<String, MultipartFile> files = mureq.getFileMap();
        if (!files.isEmpty()) {
            MultipartFile[] multipartFiles = new MultipartFile[files.size()];
            Collection<MultipartFile> values = files.values();
            sjFetchDemand.setFiles(values.toArray(multipartFiles));
        } else {
            throw new BusinessException("附件不能为空！");
        }
        return sjFetchDemand;
    }

    public static void main(String[] args) {
        String url = "http://127.0.0.1:9999/fetch/demand/bigDataAddSave";
        Map<String, Object> postParam = new HashMap<>();
        postParam.put("sourceType", "01");// 需求来源
        postParam.put("titleName", "测试数据");// 标题
        postParam.put("processid", "202112080001");// 流程id
        postParam.put("orgName", "运行调度处");// 联系部门
        postParam.put("personName", "尹磊");// 联系人
        postParam.put("businessNumber", "2021023525");// 需求单号
        postParam.put("businessType", "4");// 需求类别
        postParam.put("sysName", "[{\"leader\":\"总行业务人员2\",\"sysName\":\"个人网银二代系统\",\"telephone\":\"13255666655\"},{\"leader\":\"王冬云\",\"sysName\":\"自动化\",\"telephone\":\"13601013586\"}]");// 涉及系统
        postParam.put("interfaceName", "管理员");// 接口人姓名
        postParam.put("interfacePhone", "18088888888");// 接口人手机号
        postParam.put("fileSize", "1");// 附件数量
        File[] accessories = new File[1];// 附件列表
        String path = "C:";
        String filename = "qqqq.txt";
        accessories[0] = new File(path + File.separator + filename);
        FileBody[] fileBodys = new FileBody[1];
        fileBodys[0] = new FileBody(accessories[0]);
        String[] accessoriesFileName = new String[1];// 附件名称列表
        accessoriesFileName[0] = filename;
        String returnInfo = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httpost = new HttpPost(url);
            // FilePart：用来上传文件的篿
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            if (null != postParam && !postParam.isEmpty()) {
                for (Object key : postParam.keySet()) {
                    entity.addPart(key.toString(), new StringBody((String) postParam.get(key), Charset.forName("UTF-8")));
                }
                entity.addPart("accessoriesFileName", new StringBody(accessoriesFileName[0], Charset.forName("UTF-8")));
            }
            entity.addPart("accessories", fileBodys[0]);
            httpost.setEntity(entity);
            HttpResponse response = httpclient.execute(httpost);
            System.out.println(response.getStatusLine().getStatusCode());

            returnInfo = new String(EntityUtils.toString(response.getEntity()).trim().getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
        System.out.println(returnInfo);
    }
}
