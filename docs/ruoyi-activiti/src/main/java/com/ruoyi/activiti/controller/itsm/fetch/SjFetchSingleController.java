package com.ruoyi.activiti.controller.itsm.fetch;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.SjFetchDemand;
import com.ruoyi.activiti.domain.SjFetchSingle;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.utils.RestTemplateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import io.swagger.annotations.ApiOperation;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2021-04-07
 */
@Controller
@RequestMapping("/fetch/single")
public class SjFetchSingleController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SjFetchSingleController.class);

    //数据提取URL
    @Value("${bigData.url}")
    private String url;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    private String prefix = "fetch/single";

    @Autowired
    private ISjFetchSingleService sjFetchSingleService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IPubAttachmentService iPubAttachmentService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysWorkService sysWorkService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private ISjFetchDemandService sjFetchDemandService;


    @GetMapping()
    public String single(ModelMap modelMap) {
        //获取当前登陆人
        modelMap.put("createId", ShiroUtils.getOgUser().getpId());
        return prefix + "/single";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SjFetchSingle sjFetchSingle) {

        startPage();
        List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
        return getDataTable(list);
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        //自动对应类型单子的单号
        String fetchNo = getFetchNo();
        modelMap.put("fetchNo", fetchNo);
        modelMap.put("fetchId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * OA创建数据提取单
     *
     * @param sjFetchSingle
     * @return
     */
    @Log(title = "OA创建数据提取单", businessType = BusinessType.UPDATE)
    @PostMapping("/addSave")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(SjFetchSingle sjFetchSingle) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        //获取当前登陆人
        String pId = ShiroUtils.getOgUser().getpId();
        sjFetchSingle.setCreateId(pId);
        sjFetchSingle.setCreatTime(DateUtils.dateTimeNow());
        sjFetchSingle.setSourceType("02");
        sjFetchSingle.setSourceTypeText("OA");
        //获取当前提交是暂存还是提交
        Object obj = sjFetchSingle.getParams().get("flag");
        if (StringUtils.isNotNull(obj)) {
            String flag = sjFetchSingle.getParams().get("flag").toString();
            if ("1".equals(flag)) {
                sjFetchSingle.setFetchState("02"); //02待系统负责人处理
                sjFetchSingle.setFetchStateText("待处理");
                String businessTypeText = getBusinessTypeText(sjFetchSingle);
                if (StringUtils.isNotEmpty(businessTypeText)) {
                    sjFetchSingle.setBusinessTypeText(businessTypeText);
                }

                //判断提交是单系统还是多系统
                String[] split = sjFetchSingle.getSysName().split(",");
                //1.单系统为待指派 2.多系统为待处理
                if (split.length == 1) {
                    reMap.put("reCode", "0");
                    //获取系统的负责人信息
                    reMap.put("dealId", sjFetchSingle.getSysManagerId());
                } else {
                    //OA多系统
                    reMap.put("reCode", "1");
                    //获取所选的应用系统负责人
                    reMap.put("sysManagerId", sjFetchSingle.getSysManagerId());
                }

                try {
                    if (StringUtils.isEmpty(sjFetchSingle.getFetchId())) {
                        sjFetchSingle.setFetchId(UUID.getUUIDStr());
                        sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                    } else {
                        sjFetchSingleService.updateSjFetchSingle(sjFetchSingle);
                    }
                    //启动流程实例
                    String businessKey = sjFetchSingle.getFetchId();
                    String processDefinitionKey = "DaPu";
                    activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("数据提取单新增失败: {} ", e);
                    throw new BusinessException("数据提取单新增失败,请刷新页面进行重试");
                }
                //TODO 短信业务
                String[] pids = sjFetchSingle.getSysManagerId().split(",");
                String text = "数据提取单已提交，请登录运维管理平台处理";
                for (int i = 0; i < pids.length; i++) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
                    sendSms(text, ogPerson);
                }
                return AjaxResult.success("操作成功");

            } else {
                //当前状态为待提交
                sjFetchSingle.setFetchState("01");
                sjFetchSingle.setFetchStateText("待提交");
                if (StringUtils.isNotEmpty(sjFetchSingle.getBusinessType()) && StringUtils.isNotNull(sjFetchSingle.getBusinessType())) {
                    String businessTypeText = getBusinessTypeText(sjFetchSingle);
                    if (StringUtils.isNotEmpty(businessTypeText)) {
                        sjFetchSingle.setBusinessTypeText(businessTypeText);
                    }
                }


                try {
                    if (StringUtils.isEmpty(sjFetchSingle.getFetchId())) {
                        sjFetchSingle.setFetchId(UUID.getUUIDStr());
                        sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                    } else {
                        sjFetchSingleService.updateSjFetchSingle(sjFetchSingle);
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

    @ApiOperation("测试调用大数据接口")
    @RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public AjaxResult bigDataAddSave(@RequestBody SjFetchSingle sjFetchSingle) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("code", "1");
        ajaxResult.put("message", "接收成功");
        return ajaxResult;
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
        SjFetchSingle sjFetchSingle = this.parseRequest(req);
        Map<String, Object> reMap = new HashMap<>();
        //1.判断当前请求是否已经形成数据提取单 根据流程实例id查询
        SjFetchSingle fetchSingle = sjFetchSingleService.selectSjFetchSingleByProcessId(sjFetchSingle.getProcessid());
        if (fetchSingle != null) { //已存在
            return AjaxResult.error("数据提取单流程id已存在，不可重复发送", "流程实例Id:" + fetchSingle.getProcessid());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("username","admin");
        map.put("dbType",dbType);
        String pid = ogUserService.selectUserByLoginName(map).getpId();
        //生成数据提取单
        SjFetchSingle fetch = new SjFetchSingle();
        BeanUtils.copyProperties(sjFetchSingle, fetch);
        fetch.setFetchId(UUID.getUUIDStr());
        fetch.setCreateId(pid);
        fetch.setFetchNo(getFetchNo());
        fetch.setSourceType("01");
        fetch.setSourceTypeText("大数据门户");
        List<PubParaValue> business_type = pubParaValueService.selectPubParaValueByParaName("business_type");
        List<PubParaValue> collect = business_type.stream().filter(type -> sjFetchSingle.getBusinessType().equals(type.getValue())).collect(Collectors.toList());
        String businessTypeText = collect.get(0).getValueDetail();
        fetch.setBusinessTypeText(businessTypeText);
        fetch.setCreatTime(DateUtils.dateTimeNow());
        String[] split = sjFetchSingle.getSysName().split(",");

        String createId = "";
        //1.单系统为待指派 2.多系统为待处理
        if (split.length == 1) {
            fetch.setFetchState("02"); //02待处理
            fetch.setFetchStateText("待处理"); //02待处理文本
            reMap.put("reCode", "0");
            //获取单系统手机号
            OgPerson ogPerson = ogPersonService.bigDataPersonByPhone(fetch.getInterfacePhone());
            if (ogPerson == null) {
                return AjaxResult.error("手机号对应的人员信息在运维管理平台不存在或未启用", fetch.getInterfacePhone());
            }
            createId = ogPerson.getpId();
            fetch.setSysManagerId(ogPerson.getpId());
            fetch.setSysManagerName(ogPerson.getpName());
            reMap.put("dealId", ogPerson.getpId());
        } else {
            fetch.setFetchState("03"); //03待分派
            fetch.setFetchStateText("待分派"); //02待分派文本
            //多系统固定温新华(可能会有新的人员分派)
            reMap.put("reCode", "2");
            List<OgPerson> ogPeople = ogPersonService.selectPersonListByPostAndRole("0087", "9210");
            if (StringUtils.isNotEmpty(ogPeople)) {
                StringBuffer candidateUser = new StringBuffer();
                ogPeople.stream().forEach(ogPerson -> {
                    candidateUser.append(ogPerson.getpId());
                    candidateUser.append(",");
                });
                String candidateUsers = candidateUser.toString();
                reMap.put("assingId", candidateUsers);
            }
            createId = pid;
        }

        //判断文件是否丢失
        Integer fileLen = sjFetchSingle.getFiles().length;
        if (sjFetchSingle.getFileSize().longValue() != fileLen) {
            return AjaxResult.error("传输文件数量错误！", null);
        } else {
            //进行文件的上传
            try {
                for (MultipartFile file : sjFetchSingle.getFiles()) {
                    String[] arr = FastDFSHelper.getInstance().uploadBreakpointFile(file);
                    Attachment attachment = new Attachment();
                    attachment.setAtId(UUID.getUUIDStr());
                    attachment.setCreateId(createId);
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

        try {
            //数据提取单入库
            sjFetchSingleService.insertSjFetchSingle(fetch);
            //启动流程实例
            String businessKey = fetch.getFetchId();
            String processDefinitionKey = "DaPu";
            reMap.put("userId", createId);
            activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
        } catch (Exception e) {
            e.printStackTrace();
            return error("服务处理错误，错误原因:" + e.getMessage());
        }
        return AjaxResult.success("数据提取单操作成功", sjFetchSingle.getFetchNo());
    }

    /**
     * 修改数据提取单
     */
    @GetMapping("/edit/{fetchId}")
    public String edit(@PathVariable("fetchId") String fetchId, ModelMap mmap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchId);
        mmap.put("sjFetchSingle", sjFetchSingle);
        return prefix + "/edit";
    }

    /**
     * OA系统修改
     */
    @Log(title = "OA系统修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SjFetchSingle sjFetchSingle) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        //获取当前登陆人
        String pId = ShiroUtils.getOgUser().getpId();
        sjFetchSingle.setCreateId(pId);
        sjFetchSingle.setCreatTime(DateUtils.dateTimeNow());
        //判断是否进行了选择
        if (StringUtils.isNotEmpty(sjFetchSingle.getSourceType())) {
            sjFetchSingle.setSourceTypeText("OA");
        }
        //获取当前提交是暂存还是提交
        String flag = sjFetchSingle.getParams().get("flag").toString();
        if ("1".equals(flag)) {
            String businessTypeText = getBusinessTypeText(sjFetchSingle);
            if (StringUtils.isNotEmpty(businessTypeText)) {
                sjFetchSingle.setBusinessTypeText(businessTypeText);
            }
            if (StringUtils.isNotEmpty(sjFetchSingle.getSysName())) {
                //判断提交是单系统还是多系统
                String[] split = sjFetchSingle.getSysName().split(",");
                //1.单系统为待指派 2.多系统为待处理
                sjFetchSingle.setFetchState("02"); //02待系统负责人处理
                sjFetchSingle.setFetchStateText("待处理");
                if (split.length == 1) {
                    reMap.put("reCode", "0");
                    //获取系统的负责人信息
                    reMap.put("dealId", sjFetchSingle.getSysManagerId());
                } else {
                    reMap.put("reCode", "1");
                    //获取所选的应用系统负责人
                    reMap.put("sysManagerId", sjFetchSingle.getSysManagerId());
                }

                //启动流程实例
                String businessKey = sjFetchSingle.getFetchId();
                String processDefinitionKey = "DaPu";
                activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                //TODO 短信业务
                String[] pids = sjFetchSingle.getSysManagerId().split(",");
                String text = "数据提取单已提交，请登录运维管理平台处理";
                for (int i = 0; i < pids.length; i++) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
                    sendSms(text, ogPerson);
                }
            }
        } else {
            //当前状态为待提交
            sjFetchSingle.setFetchState("01");
            sjFetchSingle.setFetchStateText("待提交");
            if (StringUtils.isNotEmpty(sjFetchSingle.getBusinessType()) && StringUtils.isNotNull(sjFetchSingle.getBusinessType())) {
                String businessTypeText = getBusinessTypeText(sjFetchSingle);
                if (StringUtils.isNotEmpty(businessTypeText)) {
                    sjFetchSingle.setBusinessTypeText(businessTypeText);
                }
            }
        }

        return toAjax(sjFetchSingleService.updateSjFetchSingle(sjFetchSingle));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sjFetchSingleService.deleteSjFetchSingleByIds(ids));
    }


    /**
     * 联系人和业务人选择页面
     *
     * @param flag
     * @param orgId
     * @param modelMap
     * @return
     */
    @GetMapping("/selectUser/{flag}/{orgId}")
    public String selectUser(@PathVariable("flag") String flag, @PathVariable("orgId") String orgId, ModelMap modelMap) {


        if ("2".equals(flag)) {
            String[] split = orgId.split(",");
            if (split.length > 1) {
                modelMap.put("orgId", split[0]);
                modelMap.put("count", split[1]);
            }

        } else {
            modelMap.put("orgId", orgId);
        }
        modelMap.put("flag", flag);

        return prefix + "/selectUser";
    }

    @RequestMapping("/getAllSys")
    @ResponseBody
    public AjaxResult getAllSys() {
        AjaxResult ajaxResult = new AjaxResult();
        List<OgSys> ogSys = applicationManagerService.selectOgSysList(null);
        List<OgSys> list = ogSys.stream().filter(sys -> "1".equals(sys.getInvalidationMark())).collect(Collectors.toList());
        ajaxResult.put("data", list);
        return ajaxResult;
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
     * 获取当前数据提取单的所涉及的系统名称
     *
     * @param id
     * @return
     */
    @GetMapping("/selectSjFetchSingleById/{id}")
    @ResponseBody
    public AjaxResult selectById(@PathVariable("id") String id) {
        AjaxResult ajaxResult = new AjaxResult();
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(id);
        if (StringUtils.isNotEmpty(sjFetchSingle.getSysName()) && StringUtils.isNotNull(sjFetchSingle.getSysId())) {
            String[] split = sjFetchSingle.getSysId().split(",");
            ajaxResult.put("data", split);
        } else {
            ajaxResult.put("data", "0");
        }
        return ajaxResult;
    }

    /**
     * 我的数据提取单导出
     */
    @Log(title = "我的数据提取单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SjFetchSingle sjFetchSingle) {
        String isCurrentPage = (String) sjFetchSingle.getParams().get("currentPage");
        sjFetchSingle.setCreateId(ShiroUtils.getOgUser().getpId());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "数据提取单");
        } else {
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "数据提取单");
        }

    }

    /**
     * 我的数据提取单导出
     */
    @Log(title = "我的数据提取单", businessType = BusinessType.EXPORT)
    @PostMapping("/query/export")
    @ResponseBody
    public AjaxResult queryExport(SjFetchSingle sjFetchSingle) {
        String isCurrentPage = (String) sjFetchSingle.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "数据提取单");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "数据提取单");

        }

    }

    /**
     * 处理数据提取单列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/deal")
    public String deal(ModelMap modelMap) {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("createId", ogUser.getpId());
        return prefix + "/deal";
    }

    /**
     * 查询当前用户的待处理的任务
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/deallist")
    @ResponseBody
    public TableDataInfo deallist(SjFetchSingle sjFetchSingle) {
        //获取当前单系统的处理人和多系统的处理人
        List<Map<String, Object>> reList = new ArrayList<>();
        List<Map<String, Object>> reList1 = activitiCommService.userTask("DaPu", "deal");
        List<Map<String, Object>> reList2 = activitiCommService.userTask("DaPu", "sysManager");
        List<Map<String, Object>> reList3 = activitiCommService.userTask("DaPu", "groupIds");
        reList.addAll(reList1);
        reList.addAll(reList2);
        reList.addAll(reList3);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());

        if (userGroupList.size() > 0) {
            reList.addAll(activitiCommService.groupTasks("DaPu", "deal"));
            reList.addAll(activitiCommService.groupTasks("DaPu", "sysManager"));
            reList.addAll(activitiCommService.groupTasks("DaPu", "groupIds"));
        }
        List<String> fetchIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("deal".equals(map.get("description")) || "sysManager".equals(map.get("description")) || "groupIds".equals(map.get("description"))) {
                String business_key = map.get("businesskey").toString();
                if (business_key != null) {
                    if (business_key != null) {
                        fetchIds.add(business_key);
                    }
                }
            }

        }
        List<SjFetchSingle> resultlist = null;
        if (fetchIds.size() > 0) {
            sjFetchSingle.setIds(fetchIds);
            startPage();
            resultlist = sjFetchSingleService.selectListByTask(sjFetchSingle);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }

    /**
     * 处理操作页面
     *
     * @return
     */
    @GetMapping("/dealEdit/{id}")
    public String dealEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(id);
        modelMap.put("sjFetchSingle", sjFetchSingle);
        //判断是数据来源是大数据还是OA多系统还是单系统
        //判断提交是单系统还是多系统
        String[] split = sjFetchSingle.getSysName().split(",");
        if (split.length == 1) {
            //判断当前系统是待应用系统负责人处理还是待工作组处理
            //获取当前单系统的处理人和多系统的处理人
            List<Map<String, Object>> reList = new ArrayList<>();
            List<Map<String, Object>> reList1 = activitiCommService.userTask("DaPu", "groupIds");
            reList.addAll(reList1);
            List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());

            if (userGroupList.size() > 0) {
                reList.addAll(activitiCommService.groupTasks("DaPu", "groupIds"));
            }
            List<String> fetchIds = new ArrayList<>();
            for (Map<String, Object> map : reList) {
                if ("groupIds".equals(map.get("description"))) {
                    String business_key = map.get("businesskey").toString();
                    if (business_key != null) {
                        if (business_key != null) {
                            fetchIds.add(business_key);
                        }
                    }
                }

            }

            //工作组处理
            if (fetchIds.contains(sjFetchSingle.getFetchId())) {
                modelMap.put("flag", "1");
            } else {
                modelMap.put("flag", "0");
                //获取工作组
                OgGroup ogGroups = new OgGroup();
                ogGroups.setSysId(sjFetchSingle.getSysId());
                List<OgGroup> ogGroupList = sysWorkService.selectOgGroupList(ogGroups);
                modelMap.put("ogGroups", ogGroupList);
            }

        } else {
            modelMap.put("flag", "1");
        }
        return prefix + "/dealEdit";
    }

    /**
     * 分派页面
     *
     * @return
     */
    @GetMapping("/assignEdit/{id}")
    public String assignEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(id);
        modelMap.put("sjFetchSingle", sjFetchSingle);
        return prefix + "/assignEdit";
    }

    /**
     * 处理操作页面
     *
     * @return
     */
    @PostMapping("/dealSave")
    @ResponseBody
    public AjaxResult dealSave(SjFetchSingle fetchSingle) {
        Map<String, Object> reMap = new HashMap<>();
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchSingle.getFetchId());
        String businessKey = sjFetchSingle.getFetchId();
        String processDefinitionKey = "DaPu";
        reMap.put("businessKey", businessKey);
        reMap.put("userId", ShiroUtils.getUserId());
        reMap.put("processDefinitionKey", processDefinitionKey);
        //如果是处理则进行数据变量的填充
        String flag = fetchSingle.getFlag();
        if ("1".equals(flag)) { //处理
            if ("1".equals(sjFetchSingle.getDealFlag())) {
                reMap.put("reCode", "2");
                reMap.put("comment", fetchSingle.getLogPerformDesc());
                sjFetchSingle.setFetchState("04");
                sjFetchSingle.setFetchStateText("已处理");
                SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(sjFetchSingle.getFetchDemandId());
                String[] counts = sjFetchDemand.getDealSchdule().split("/");
                String num = String.valueOf(Integer.valueOf(counts[0]) + 1);
                String count = String.valueOf(Integer.valueOf(counts[1]));
                SjFetchDemand sjFetchDemand1 = new SjFetchDemand();
                sjFetchDemand1.setFetchId(sjFetchDemand.getFetchId());
                sjFetchDemand1.setDealSchdule(num + "/" + count);
                if (num.equals(count)) {
                    //判断是OA还是大数据
                    if ("01".equals(sjFetchSingle.getSourceType())) {
                        Map<String, String> map = new HashMap<>();
                        map.put("processId", sjFetchDemand.getProcessid());//流程实例id
                        map.put("businessNumber", sjFetchDemand.getBusinessNumber());//需求单号
                        map.put("businessNo", sjFetchDemand.getFetchNo());//业务单号
                        map.put("opinion", "1");//意见（1-处理完成，2-需求取消）
                        JSONObject json = (JSONObject) JSON.toJSON(map);
                        //JSONObject json = new JSONObject();
                        //json.put("json", map);
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("json", JSONUtils.toJSONString(json));
                        String responseResult = restTemplateUtil.sendPost(url, map1);
                        Map msgMap = JSON.parseObject(responseResult);
                        String ret = (String) msgMap.get("ret");
                        Map mssageMap = JSON.parseObject(ret);
                        String code = (String) mssageMap.get("code");
                        String message = (String) mssageMap.get("message");
                        log.info("大数据返回值" + responseResult);
                        if ("0000".equals(code)) {
                            activitiCommService.complete(reMap);
                        } else {
                            return AjaxResult.error(message + "，请联系运维管理平台人员处理");
                        }
                    }
                    sjFetchDemand1.setFetchState("06");
                    sjFetchDemand1.setFetchStateText("已关闭");
                }
                sjFetchDemandService.updateSjFetchDemand(sjFetchDemand1);
            } else {
                reMap.put("checkerId", sjFetchSingle.getSysManagerId());
                sjFetchSingle.setFetchState("05");
                sjFetchSingle.setFetchStateText("待审核反馈");
            }
        } else {//指派
            reMap.put("reCode", "1");
            reMap.put("groupId", fetchSingle.getGroupId());
            reMap.put("comment", fetchSingle.getLogPerformDesc());
            sjFetchSingle.setFetchState("02");
            sjFetchSingle.setFetchStateText("待处理");
            sjFetchSingle.setDealFlag("2");//工作组处理
        }
        activitiCommService.complete(reMap);
        return toAjax(sjFetchSingleService.updateSjFetchSingle(sjFetchSingle));
    }

    /**
     * 指派页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/assignGroup/{id}/{fetchId}")
    public String assignGroup(@PathVariable("id") String id, @PathVariable("fetchId") String fetchId, ModelMap modelMap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchId);
        //根据系统id查询工作组
        List<OgGroup> ogGroups = sysWorkService.selectGroupBySysId(id);
        modelMap.put("ogGroups", ogGroups);
        modelMap.put("sysName", sjFetchSingle.getSysName());
        //获取+
        return prefix + "/assignGroup";
    }


    /**
     * 查询当前运维组的待处理的任务
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/groupDealList")
    @ResponseBody
    public TableDataInfo groupDealList(SjFetchSingle sjFetchSingle) {
        //获取当前单系统的处理人和多系统的处理人
        List<Map<String, Object>> reList = new ArrayList<>();
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());

        if (userGroupList.size() > 0) {
            reList.addAll(activitiCommService.groupTasks("DaPu", "groupIds"));
        }
        List<String> fetchIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("groupIds".equals(map.get("description"))) {
                String business_key = map.get("businesskey").toString();
                if (business_key != null) {
                    if (business_key != null) {
                        fetchIds.add(business_key);
                    }
                }
            }

        }
        List<SjFetchSingle> resultlist = null;
        if (fetchIds.size() > 0) {
            sjFetchSingle.setIds(fetchIds);
            startPage();
            resultlist = sjFetchSingleService.selectListByTask(sjFetchSingle);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }


    /**
     * 处理数据提取单列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/groupDeal")
    public String groupDeal(ModelMap modelMap) {
        return prefix + "/groupDeal";
    }

    /**
     * 分派数据提取单列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/assign")
    public String assign(ModelMap modelMap) {
        return prefix + "/assign";
    }


    /**
     * 查询当前用户的待分派的任务
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/assignlist")
    @ResponseBody
    public TableDataInfo assignlist(SjFetchSingle sjFetchSingle) {
        //获取当前单系统的处理人和多系统的处理人
        List<Map<String, Object>> reList = new ArrayList<>();
        List<Map<String, Object>> reList1 = activitiCommService.userTask("DaPu", "assign");
        reList.addAll(reList1);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        if (userGroupList.size() > 0) {
            reList.addAll(activitiCommService.groupTasks("DaPu", "assign"));
        }
        List<String> fetchIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("assign".equals(map.get("description"))) {
                String business_key = map.get("businesskey").toString();
                if (business_key != null) {
                    if (business_key != null) {
                        fetchIds.add(business_key);
                    }
                }
            }

        }
        List<SjFetchSingle> resultlist = null;
        if (fetchIds.size() > 0) {
            sjFetchSingle.setIds(fetchIds);
            startPage();
            resultlist = sjFetchSingleService.selectListByTask(sjFetchSingle);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }


    /**
     * 分派操作
     *
     * @return
     */
    @PostMapping("/assignSave")
    @ResponseBody
    public AjaxResult assignSave(SjFetchSingle fetchSingle) {
        fetchSingle.setFetchState("02");
        fetchSingle.setFetchStateText("待处理");
        Map<String, Object> reMap = new HashMap<>();
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchSingle.getFetchId());
        String businessKey = sjFetchSingle.getFetchId();
        String processDefinitionKey = "DaPu";
        reMap.put("businessKey", businessKey);
        reMap.put("userId", ShiroUtils.getUserId());
        reMap.put("processDefinitionKey", processDefinitionKey);
        reMap.put("sysManagerId", fetchSingle.getSysManagerId());
        activitiCommService.complete(reMap);
        //TODO 短信业务
        String[] pids = fetchSingle.getSysManagerId().split(",");
        String text = "数据提取单已分派，请登录运维管理平台处理";
        for (int i = 0; i < pids.length; i++) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
            sendSms(text, ogPerson);
        }
        return toAjax(sjFetchSingleService.updateSjFetchSingleStatus(fetchSingle));

    }

    /**
     * 查询数据提取单页面
     *
     * @return
     */
    @GetMapping("/query")
    public String query() {
        return prefix + "/query";
    }

    private String getBusinessTypeText(SjFetchSingle sjFetchSingle) {
        String businessTypeText = "";
        //判断需求类别是否选择
        if (StringUtils.isNotEmpty(sjFetchSingle.getBusinessType())) {
            List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("business_type");

            if (list.size() > 0) {
                list = list.stream().filter(item -> item.getValue().equals(sjFetchSingle.getBusinessType())).collect(Collectors.toList());
                if (list.size() > 0) {
                    businessTypeText = list.get(0).getValueDetail();
                }
            }
        }
        return businessTypeText;
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

    /**
     * 数据提取单详情页面
     *
     * @param fetchId
     * @param mmap
     * @return
     */
    @GetMapping("/details/{fetchId}")
    public String details(@PathVariable("fetchId") String fetchId, ModelMap mmap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchId);
        mmap.put("sjFetchSingle", sjFetchSingle);
        return prefix + "/details";
    }


    /**
     * 暂存
     *
     * @return
     */
    @Log(title = "新增保存", businessType = BusinessType.INSERT)
    @PostMapping("/fileSave")
    @ResponseBody
    public AjaxResult fileSave(SjFetchSingle sjFetchSingle) {
        try {
            if (StringUtils.isEmpty(sjFetchSingle.getFetchId())) {
                sjFetchSingle.setFetchId(UUID.getUUIDStr());
                sjFetchSingle.setCreateId(ShiroUtils.getOgUser().getpId());
                sjFetchSingle.setCreatTime(DateUtils.dateTimeNow());
                sjFetchSingle.setSourceType("02");
                sjFetchSingle.setSourceTypeText("OA");
                sjFetchSingle.setFetchState("01");
                sjFetchSingle.setFetchStateText("待提交");
                sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                return AjaxResult.success("操作成功", sjFetchSingle.getFetchId());
            } else {
                sjFetchSingleService.updateSjFetchSingle(sjFetchSingle);
                return AjaxResult.success("操作成功", sjFetchSingle.getFetchId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据提取单暂存失败: {}", e);
            throw new BusinessException("暂存失败，单号是：" + sjFetchSingle.getFetchId());
        }

    }

    /**
     * 审核反馈列表页面跳转
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/check")
    public String check(ModelMap modelMap) {
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("createId", ogUser.getpId());
        return prefix + "/check";
    }

    /**
     * 审核反馈列表LIst
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/checkList")
    @ResponseBody
    public TableDataInfo checkList(SjFetchSingle sjFetchSingle) {
        List<SjFetchSingle> list = new ArrayList<>();
        List<Map<String, Object>> reList = activitiCommService.userTask("DaPu", "check");
        reList.addAll(activitiCommService.groupTasks("DaPu", "check"));

        for(Map<String, Object> mp : reList){
            String id = mp.get("businesskey")==null ? "" : mp.get("businesskey").toString();
            String taskName = mp.get("taskName").toString();
            sjFetchSingle.setFetchId(id);
//            if (StringUtils.isNotEmpty(sjFetchSingle.getParams().get("createTime").toString())) {
//                sjFetchSingle.getParams().put("createTime",handleTimeYYYYMMDDHHMMSS(sjFetchSingle.getParams().get("createTime").toString()));
//            }
//            if (StringUtils.isNotEmpty(sjFetchSingle.getParams().get("endCreateTime").toString())) {
//                sjFetchSingle.getParams().put("endCreateTime",handleTimeYYYYMMDD(sjFetchSingle.getParams().get("endCreateTime").toString()) + "240000");
//            }
            List<SjFetchSingle> sjFetchSingleList = new ArrayList<>();
            SjFetchSingle sjFetchSingle1 = new SjFetchSingle();
            sjFetchSingleList = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            if (sjFetchSingleList != null) {
                sjFetchSingle1 = sjFetchSingleList.get(0);
            }
            list.add(sjFetchSingle1);
        }
        return getDataTable(list);
    }

    /**
     * 审核反馈操作页面
     *
     * @return
     */
    @GetMapping("/checkEdit/{id}")
    public String checkEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(id);
        modelMap.put("sjFetchSingle", sjFetchSingle);
        return prefix + "/checkEdit";
    }

    /**
     * 审核反馈操作
     *
     * @return
     */
    @PostMapping("/checkDeal")
    @ResponseBody
    public AjaxResult checkDeal(SjFetchSingle fetchSingle) {
        Map<String, Object> reMap = new HashMap<>();
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchSingle.getFetchId());
        String businessKey = sjFetchSingle.getFetchId();
        String processDefinitionKey = "DaPu";
        reMap.put("businessKey", businessKey);
        reMap.put("userId", ShiroUtils.getUserId());
        reMap.put("processDefinitionKey", processDefinitionKey);
        reMap.put("comment", fetchSingle.getLogPerformDesc());
        activitiCommService.complete(reMap);
        fetchSingle.setFetchState("04");
        fetchSingle.setFetchStateText("已处理");
        sjFetchSingleService.updateSjFetchSingle(fetchSingle);
        SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(sjFetchSingle.getFetchDemandId());
        String[] counts = sjFetchDemand.getDealSchdule().split("/");
        String num = String.valueOf(Integer.valueOf(counts[0]) + 1);
        String count = String.valueOf(Integer.valueOf(counts[1]));
        SjFetchDemand sjFetchDemand1 = new SjFetchDemand();
        sjFetchDemand1.setFetchId(sjFetchDemand.getFetchId());
        sjFetchDemand1.setDealSchdule(num + "/" + count);
        if (num.equals(count)) {
            //判断是OA还是大数据
            if ("01".equals(sjFetchSingle.getSourceType())) {
                Map<String, String> map = new HashMap<>();
                map.put("processId", sjFetchDemand.getProcessid());//流程实例id
                map.put("businessNumber", sjFetchDemand.getBusinessNumber());//需求单号
                map.put("businessNo", sjFetchDemand.getFetchNo());//业务单号
                map.put("opinion", "1");//意见（1-处理完成，2-需求取消）
                JSONObject json = (JSONObject) JSON.toJSON(map);
                //JSONObject json = new JSONObject();
                //json.put("json", map);
                Map<String, String> map1 = new HashMap<>();
                map1.put("json", JSONUtils.toJSONString(json));
                String responseResult = restTemplateUtil.sendPost(url, map1);
                Map msgMap = JSON.parseObject(responseResult);
                String ret = (String) msgMap.get("ret");
                Map mssageMap = JSON.parseObject(ret);
                String code = (String) mssageMap.get("code");
                String message = (String) mssageMap.get("message");
                log.info("大数据返回值" + responseResult);
                if ("0000".equals(code)) {
                    activitiCommService.complete(reMap);
                } else {
                    return AjaxResult.error(message + "，请联系运维管理平台人员处理");
                }
            }
            sjFetchDemand1.setFetchState("06");
            sjFetchDemand1.setFetchStateText("已关闭");
        }
        sjFetchDemandService.updateSjFetchDemand(sjFetchDemand1);
        return AjaxResult.success("审核成功");
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
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    public SjFetchSingle parseRequest(HttpServletRequest req) {
        SjFetchSingle sjFetchSingle = new SjFetchSingle();
        Map<String, Object> dataMap = MapDataUtil.convertDataMap(req);
        ConverterUtils.mapToObject(sjFetchSingle, dataMap);

        // 校验实体类对象，主要是非空检查，非空校验不通过抛出异常信息
        sjFetchSingle.validate();

        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) req;
        Map<String, MultipartFile> files = mureq.getFileMap();
        if (!files.isEmpty()) {
            MultipartFile[] multipartFiles = new MultipartFile[files.size()];
            Collection<MultipartFile> values = files.values();
            sjFetchSingle.setFiles(values.toArray(multipartFiles));
        } else {
            throw new BusinessException("附件不能为空！");
        }
        return sjFetchSingle;
    }

    public static void main(String[] args) {
        String url = "http://20.200.84.91:9999/itsm/fetch/single/bigDataAddSave";
        Map<String, Object> postParam = new HashMap<>();
        postParam.put("sourceType", "01");// 需求来源
        postParam.put("titleName", "测试数据");// 标题
        postParam.put("processid", "12322655");// 流程id
        postParam.put("orgName", "运行调度处");// 联系部门
        postParam.put("personName", "尹磊");// 联系人
        postParam.put("businessNumber", "2021023525");// 需求单号
        postParam.put("businessType", "4");// 需求类别
        postParam.put("sysName", "运维管理平台");// 涉及系统
        postParam.put("interfaceName", "管理员");// 接口人姓名
        postParam.put("interfacePhone", "18088888888");// 接口人手机号
        postParam.put("fileSize", "1");// 附件数量
        File[] accessories = new File[1];// 附件列表
        String path = "F:";
        String filename = "fujian.sql";
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
