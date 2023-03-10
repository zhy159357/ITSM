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
 * ???????????????????????????Controller
 *
 * @author ruoyi
 * @date 2021-04-07
 */
@Controller
@RequestMapping("/fetch/single")
public class SjFetchSingleController extends BaseController {

    //????????????
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SjFetchSingleController.class);

    //????????????URL
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
        //?????????????????????
        modelMap.put("createId", ShiroUtils.getOgUser().getpId());
        return prefix + "/single";
    }

    /**
     * ???????????????????????????????????????
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SjFetchSingle sjFetchSingle) {

        startPage();
        List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
        return getDataTable(list);
    }

    /**
     * ?????????????????????????????????
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        //?????????????????????????????????
        String fetchNo = getFetchNo();
        modelMap.put("fetchNo", fetchNo);
        modelMap.put("fetchId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * OA?????????????????????
     *
     * @param sjFetchSingle
     * @return
     */
    @Log(title = "OA?????????????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/addSave")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(SjFetchSingle sjFetchSingle) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        //?????????????????????
        String pId = ShiroUtils.getOgUser().getpId();
        sjFetchSingle.setCreateId(pId);
        sjFetchSingle.setCreatTime(DateUtils.dateTimeNow());
        sjFetchSingle.setSourceType("02");
        sjFetchSingle.setSourceTypeText("OA");
        //???????????????????????????????????????
        Object obj = sjFetchSingle.getParams().get("flag");
        if (StringUtils.isNotNull(obj)) {
            String flag = sjFetchSingle.getParams().get("flag").toString();
            if ("1".equals(flag)) {
                sjFetchSingle.setFetchState("02"); //02????????????????????????
                sjFetchSingle.setFetchStateText("?????????");
                String businessTypeText = getBusinessTypeText(sjFetchSingle);
                if (StringUtils.isNotEmpty(businessTypeText)) {
                    sjFetchSingle.setBusinessTypeText(businessTypeText);
                }

                //???????????????????????????????????????
                String[] split = sjFetchSingle.getSysName().split(",");
                //1.????????????????????? 2.?????????????????????
                if (split.length == 1) {
                    reMap.put("reCode", "0");
                    //??????????????????????????????
                    reMap.put("dealId", sjFetchSingle.getSysManagerId());
                } else {
                    //OA?????????
                    reMap.put("reCode", "1");
                    //????????????????????????????????????
                    reMap.put("sysManagerId", sjFetchSingle.getSysManagerId());
                }

                try {
                    if (StringUtils.isEmpty(sjFetchSingle.getFetchId())) {
                        sjFetchSingle.setFetchId(UUID.getUUIDStr());
                        sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                    } else {
                        sjFetchSingleService.updateSjFetchSingle(sjFetchSingle);
                    }
                    //??????????????????
                    String businessKey = sjFetchSingle.getFetchId();
                    String processDefinitionKey = "DaPu";
                    activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("???????????????????????????: {} ", e);
                    throw new BusinessException("???????????????????????????,???????????????????????????");
                }
                //TODO ????????????
                String[] pids = sjFetchSingle.getSysManagerId().split(",");
                String text = "????????????????????????????????????????????????????????????";
                for (int i = 0; i < pids.length; i++) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
                    sendSms(text, ogPerson);
                }
                return AjaxResult.success("????????????");

            } else {
                //????????????????????????
                sjFetchSingle.setFetchState("01");
                sjFetchSingle.setFetchStateText("?????????");
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
                    log.error("???????????????????????????: {} ", e);
                    return AjaxResult.error("???????????????????????????");
                }
                return AjaxResult.success("???????????????????????????");


            }
        } else {
            return AjaxResult.error("OA?????????????????????flag?????????null");
        }

    }

    @ApiOperation("???????????????????????????")
    @RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public AjaxResult bigDataAddSave(@RequestBody SjFetchSingle sjFetchSingle) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("code", "1");
        ajaxResult.put("message", "????????????");
        return ajaxResult;
    }

    /**
     * ?????????????????????????????????
     */
    @Log(title = "???????????????????????????", businessType = BusinessType.INSERT)
    @PostMapping("/bigDataAddSave")
    @ResponseBody
    @RepeatSubmit
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult bigDataAddSave(HttpServletRequest req) {
        SjFetchSingle sjFetchSingle = this.parseRequest(req);
        Map<String, Object> reMap = new HashMap<>();
        //1.??????????????????????????????????????????????????? ??????????????????id??????
        SjFetchSingle fetchSingle = sjFetchSingleService.selectSjFetchSingleByProcessId(sjFetchSingle.getProcessid());
        if (fetchSingle != null) { //?????????
            return AjaxResult.error("?????????????????????id??????????????????????????????", "????????????Id:" + fetchSingle.getProcessid());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("username","admin");
        map.put("dbType",dbType);
        String pid = ogUserService.selectUserByLoginName(map).getpId();
        //?????????????????????
        SjFetchSingle fetch = new SjFetchSingle();
        BeanUtils.copyProperties(sjFetchSingle, fetch);
        fetch.setFetchId(UUID.getUUIDStr());
        fetch.setCreateId(pid);
        fetch.setFetchNo(getFetchNo());
        fetch.setSourceType("01");
        fetch.setSourceTypeText("???????????????");
        List<PubParaValue> business_type = pubParaValueService.selectPubParaValueByParaName("business_type");
        List<PubParaValue> collect = business_type.stream().filter(type -> sjFetchSingle.getBusinessType().equals(type.getValue())).collect(Collectors.toList());
        String businessTypeText = collect.get(0).getValueDetail();
        fetch.setBusinessTypeText(businessTypeText);
        fetch.setCreatTime(DateUtils.dateTimeNow());
        String[] split = sjFetchSingle.getSysName().split(",");

        String createId = "";
        //1.????????????????????? 2.?????????????????????
        if (split.length == 1) {
            fetch.setFetchState("02"); //02?????????
            fetch.setFetchStateText("?????????"); //02???????????????
            reMap.put("reCode", "0");
            //????????????????????????
            OgPerson ogPerson = ogPersonService.bigDataPersonByPhone(fetch.getInterfacePhone());
            if (ogPerson == null) {
                return AjaxResult.error("????????????????????????????????????????????????????????????????????????", fetch.getInterfacePhone());
            }
            createId = ogPerson.getpId();
            fetch.setSysManagerId(ogPerson.getpId());
            fetch.setSysManagerName(ogPerson.getpName());
            reMap.put("dealId", ogPerson.getpId());
        } else {
            fetch.setFetchState("03"); //03?????????
            fetch.setFetchStateText("?????????"); //02???????????????
            //????????????????????????(??????????????????????????????)
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

        //????????????????????????
        Integer fileLen = sjFetchSingle.getFiles().length;
        if (sjFetchSingle.getFileSize().longValue() != fileLen) {
            return AjaxResult.error("???????????????????????????", null);
        } else {
            //?????????????????????
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
                    attachment.setFileType("1");// ?????????????????????1
                    iPubAttachmentService.insertAttachment(attachment);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("?????????????????????????????????:" + e.getMessage());
            }
        }

        try {
            //?????????????????????
            sjFetchSingleService.insertSjFetchSingle(fetch);
            //??????????????????
            String businessKey = fetch.getFetchId();
            String processDefinitionKey = "DaPu";
            reMap.put("userId", createId);
            activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
        } catch (Exception e) {
            e.printStackTrace();
            return error("?????????????????????????????????:" + e.getMessage());
        }
        return AjaxResult.success("???????????????????????????", sjFetchSingle.getFetchNo());
    }

    /**
     * ?????????????????????
     */
    @GetMapping("/edit/{fetchId}")
    public String edit(@PathVariable("fetchId") String fetchId, ModelMap mmap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchId);
        mmap.put("sjFetchSingle", sjFetchSingle);
        return prefix + "/edit";
    }

    /**
     * OA????????????
     */
    @Log(title = "OA????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SjFetchSingle sjFetchSingle) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        //?????????????????????
        String pId = ShiroUtils.getOgUser().getpId();
        sjFetchSingle.setCreateId(pId);
        sjFetchSingle.setCreatTime(DateUtils.dateTimeNow());
        //???????????????????????????
        if (StringUtils.isNotEmpty(sjFetchSingle.getSourceType())) {
            sjFetchSingle.setSourceTypeText("OA");
        }
        //???????????????????????????????????????
        String flag = sjFetchSingle.getParams().get("flag").toString();
        if ("1".equals(flag)) {
            String businessTypeText = getBusinessTypeText(sjFetchSingle);
            if (StringUtils.isNotEmpty(businessTypeText)) {
                sjFetchSingle.setBusinessTypeText(businessTypeText);
            }
            if (StringUtils.isNotEmpty(sjFetchSingle.getSysName())) {
                //???????????????????????????????????????
                String[] split = sjFetchSingle.getSysName().split(",");
                //1.????????????????????? 2.?????????????????????
                sjFetchSingle.setFetchState("02"); //02????????????????????????
                sjFetchSingle.setFetchStateText("?????????");
                if (split.length == 1) {
                    reMap.put("reCode", "0");
                    //??????????????????????????????
                    reMap.put("dealId", sjFetchSingle.getSysManagerId());
                } else {
                    reMap.put("reCode", "1");
                    //????????????????????????????????????
                    reMap.put("sysManagerId", sjFetchSingle.getSysManagerId());
                }

                //??????????????????
                String businessKey = sjFetchSingle.getFetchId();
                String processDefinitionKey = "DaPu";
                activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                //TODO ????????????
                String[] pids = sjFetchSingle.getSysManagerId().split(",");
                String text = "????????????????????????????????????????????????????????????";
                for (int i = 0; i < pids.length; i++) {
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
                    sendSms(text, ogPerson);
                }
            }
        } else {
            //????????????????????????
            sjFetchSingle.setFetchState("01");
            sjFetchSingle.setFetchStateText("?????????");
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
     * ?????????????????????????????????
     */
    @Log(title = "???????????????????????????", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sjFetchSingleService.deleteSjFetchSingleByIds(ids));
    }


    /**
     * ?????????????????????????????????
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
     * ???????????????????????????
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
     * ??????????????????????????????????????????????????????
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
     * ???????????????????????????
     */
    @Log(title = "?????????????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SjFetchSingle sjFetchSingle) {
        String isCurrentPage = (String) sjFetchSingle.getParams().get("currentPage");
        sjFetchSingle.setCreateId(ShiroUtils.getOgUser().getpId());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "???????????????");
        } else {
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "???????????????");
        }

    }

    /**
     * ???????????????????????????
     */
    @Log(title = "?????????????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/query/export")
    @ResponseBody
    public AjaxResult queryExport(SjFetchSingle sjFetchSingle) {
        String isCurrentPage = (String) sjFetchSingle.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "???????????????");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<SjFetchSingle> list = sjFetchSingleService.selectSjFetchSingleList(sjFetchSingle);
            ExcelUtil<SjFetchSingle> util = new ExcelUtil<>(SjFetchSingle.class);
            return util.exportExcel(list, "???????????????");

        }

    }

    /**
     * ?????????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/deal")
    public String deal(ModelMap modelMap) {
        //?????????????????????????????????
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("createId", ogUser.getpId());
        return prefix + "/deal";
    }

    /**
     * ???????????????????????????????????????
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/deallist")
    @ResponseBody
    public TableDataInfo deallist(SjFetchSingle sjFetchSingle) {
        //?????????????????????????????????????????????????????????
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
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/dealEdit/{id}")
    public String dealEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(id);
        modelMap.put("sjFetchSingle", sjFetchSingle);
        //???????????????????????????????????????OA????????????????????????
        //???????????????????????????????????????
        String[] split = sjFetchSingle.getSysName().split(",");
        if (split.length == 1) {
            //???????????????????????????????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????
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

            //???????????????
            if (fetchIds.contains(sjFetchSingle.getFetchId())) {
                modelMap.put("flag", "1");
            } else {
                modelMap.put("flag", "0");
                //???????????????
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
     * ????????????
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
     * ??????????????????
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
        //?????????????????????????????????????????????
        String flag = fetchSingle.getFlag();
        if ("1".equals(flag)) { //??????
            if ("1".equals(sjFetchSingle.getDealFlag())) {
                reMap.put("reCode", "2");
                reMap.put("comment", fetchSingle.getLogPerformDesc());
                sjFetchSingle.setFetchState("04");
                sjFetchSingle.setFetchStateText("?????????");
                SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(sjFetchSingle.getFetchDemandId());
                String[] counts = sjFetchDemand.getDealSchdule().split("/");
                String num = String.valueOf(Integer.valueOf(counts[0]) + 1);
                String count = String.valueOf(Integer.valueOf(counts[1]));
                SjFetchDemand sjFetchDemand1 = new SjFetchDemand();
                sjFetchDemand1.setFetchId(sjFetchDemand.getFetchId());
                sjFetchDemand1.setDealSchdule(num + "/" + count);
                if (num.equals(count)) {
                    //?????????OA???????????????
                    if ("01".equals(sjFetchSingle.getSourceType())) {
                        Map<String, String> map = new HashMap<>();
                        map.put("processId", sjFetchDemand.getProcessid());//????????????id
                        map.put("businessNumber", sjFetchDemand.getBusinessNumber());//????????????
                        map.put("businessNo", sjFetchDemand.getFetchNo());//????????????
                        map.put("opinion", "1");//?????????1-???????????????2-???????????????
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
                        log.info("??????????????????" + responseResult);
                        if ("0000".equals(code)) {
                            activitiCommService.complete(reMap);
                        } else {
                            return AjaxResult.error(message + "??????????????????????????????????????????");
                        }
                    }
                    sjFetchDemand1.setFetchState("06");
                    sjFetchDemand1.setFetchStateText("?????????");
                }
                sjFetchDemandService.updateSjFetchDemand(sjFetchDemand1);
            } else {
                reMap.put("checkerId", sjFetchSingle.getSysManagerId());
                sjFetchSingle.setFetchState("05");
                sjFetchSingle.setFetchStateText("???????????????");
            }
        } else {//??????
            reMap.put("reCode", "1");
            reMap.put("groupId", fetchSingle.getGroupId());
            reMap.put("comment", fetchSingle.getLogPerformDesc());
            sjFetchSingle.setFetchState("02");
            sjFetchSingle.setFetchStateText("?????????");
            sjFetchSingle.setDealFlag("2");//???????????????
        }
        activitiCommService.complete(reMap);
        return toAjax(sjFetchSingleService.updateSjFetchSingle(sjFetchSingle));
    }

    /**
     * ????????????
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/assignGroup/{id}/{fetchId}")
    public String assignGroup(@PathVariable("id") String id, @PathVariable("fetchId") String fetchId, ModelMap modelMap) {
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchId);
        //????????????id???????????????
        List<OgGroup> ogGroups = sysWorkService.selectGroupBySysId(id);
        modelMap.put("ogGroups", ogGroups);
        modelMap.put("sysName", sjFetchSingle.getSysName());
        //??????+
        return prefix + "/assignGroup";
    }


    /**
     * ??????????????????????????????????????????
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/groupDealList")
    @ResponseBody
    public TableDataInfo groupDealList(SjFetchSingle sjFetchSingle) {
        //?????????????????????????????????????????????????????????
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
     * ?????????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/groupDeal")
    public String groupDeal(ModelMap modelMap) {
        return prefix + "/groupDeal";
    }

    /**
     * ?????????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/assign")
    public String assign(ModelMap modelMap) {
        return prefix + "/assign";
    }


    /**
     * ???????????????????????????????????????
     *
     * @param sjFetchSingle
     * @return
     */
    @PostMapping("/assignlist")
    @ResponseBody
    public TableDataInfo assignlist(SjFetchSingle sjFetchSingle) {
        //?????????????????????????????????????????????????????????
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
     * ????????????
     *
     * @return
     */
    @PostMapping("/assignSave")
    @ResponseBody
    public AjaxResult assignSave(SjFetchSingle fetchSingle) {
        fetchSingle.setFetchState("02");
        fetchSingle.setFetchStateText("?????????");
        Map<String, Object> reMap = new HashMap<>();
        SjFetchSingle sjFetchSingle = sjFetchSingleService.selectSjFetchSingleById(fetchSingle.getFetchId());
        String businessKey = sjFetchSingle.getFetchId();
        String processDefinitionKey = "DaPu";
        reMap.put("businessKey", businessKey);
        reMap.put("userId", ShiroUtils.getUserId());
        reMap.put("processDefinitionKey", processDefinitionKey);
        reMap.put("sysManagerId", fetchSingle.getSysManagerId());
        activitiCommService.complete(reMap);
        //TODO ????????????
        String[] pids = fetchSingle.getSysManagerId().split(",");
        String text = "????????????????????????????????????????????????????????????";
        for (int i = 0; i < pids.length; i++) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(pids[i]);
            sendSms(text, ogPerson);
        }
        return toAjax(sjFetchSingleService.updateSjFetchSingleStatus(fetchSingle));

    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/query")
    public String query() {
        return prefix + "/query";
    }

    private String getBusinessTypeText(SjFetchSingle sjFetchSingle) {
        String businessTypeText = "";
        //??????????????????????????????
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
     * ??????????????????????????????
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
     * ???????????????????????????
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
     * ??????
     *
     * @return
     */
    @Log(title = "????????????", businessType = BusinessType.INSERT)
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
                sjFetchSingle.setFetchStateText("?????????");
                sjFetchSingleService.insertSjFetchSingle(sjFetchSingle);
                return AjaxResult.success("????????????", sjFetchSingle.getFetchId());
            } else {
                sjFetchSingleService.updateSjFetchSingle(sjFetchSingle);
                return AjaxResult.success("????????????", sjFetchSingle.getFetchId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("???????????????????????????: {}", e);
            throw new BusinessException("???????????????????????????" + sjFetchSingle.getFetchId());
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/check")
    public String check(ModelMap modelMap) {
        //?????????????????????????????????
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("createId", ogUser.getpId());
        return prefix + "/check";
    }

    /**
     * ??????????????????LIst
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
     * ????????????????????????
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
     * ??????????????????
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
        fetchSingle.setFetchStateText("?????????");
        sjFetchSingleService.updateSjFetchSingle(fetchSingle);
        SjFetchDemand sjFetchDemand = sjFetchDemandService.selectSjFetchDemandById(sjFetchSingle.getFetchDemandId());
        String[] counts = sjFetchDemand.getDealSchdule().split("/");
        String num = String.valueOf(Integer.valueOf(counts[0]) + 1);
        String count = String.valueOf(Integer.valueOf(counts[1]));
        SjFetchDemand sjFetchDemand1 = new SjFetchDemand();
        sjFetchDemand1.setFetchId(sjFetchDemand.getFetchId());
        sjFetchDemand1.setDealSchdule(num + "/" + count);
        if (num.equals(count)) {
            //?????????OA???????????????
            if ("01".equals(sjFetchSingle.getSourceType())) {
                Map<String, String> map = new HashMap<>();
                map.put("processId", sjFetchDemand.getProcessid());//????????????id
                map.put("businessNumber", sjFetchDemand.getBusinessNumber());//????????????
                map.put("businessNo", sjFetchDemand.getFetchNo());//????????????
                map.put("opinion", "1");//?????????1-???????????????2-???????????????
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
                log.info("??????????????????" + responseResult);
                if ("0000".equals(code)) {
                    activitiCommService.complete(reMap);
                } else {
                    return AjaxResult.error(message + "??????????????????????????????????????????");
                }
            }
            sjFetchDemand1.setFetchState("06");
            sjFetchDemand1.setFetchStateText("?????????");
        }
        sjFetchDemandService.updateSjFetchDemand(sjFetchDemand1);
        return AjaxResult.success("????????????");
    }

    /**
     * ????????????
     *
     * @param setSmsText ????????????
     * @param person     ???????????????
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// ?????????
        p.setName(person.getpName());// ??????
        p.setSmsType("4");// ???????????????3???4????????????,2????????????
        p.setSmsText(setSmsText);// ????????????
        p.setInspectTime(DateUtils.dateTimeNow());// ????????????
        p.setInspectObject("050100");// ????????????
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// ?????????
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//????????????
    }

    public SjFetchSingle parseRequest(HttpServletRequest req) {
        SjFetchSingle sjFetchSingle = new SjFetchSingle();
        Map<String, Object> dataMap = MapDataUtil.convertDataMap(req);
        ConverterUtils.mapToObject(sjFetchSingle, dataMap);

        // ???????????????????????????????????????????????????????????????????????????????????????
        sjFetchSingle.validate();

        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) req;
        Map<String, MultipartFile> files = mureq.getFileMap();
        if (!files.isEmpty()) {
            MultipartFile[] multipartFiles = new MultipartFile[files.size()];
            Collection<MultipartFile> values = files.values();
            sjFetchSingle.setFiles(values.toArray(multipartFiles));
        } else {
            throw new BusinessException("?????????????????????");
        }
        return sjFetchSingle;
    }

    public static void main(String[] args) {
        String url = "http://20.200.84.91:9999/itsm/fetch/single/bigDataAddSave";
        Map<String, Object> postParam = new HashMap<>();
        postParam.put("sourceType", "01");// ????????????
        postParam.put("titleName", "????????????");// ??????
        postParam.put("processid", "12322655");// ??????id
        postParam.put("orgName", "???????????????");// ????????????
        postParam.put("personName", "??????");// ?????????
        postParam.put("businessNumber", "2021023525");// ????????????
        postParam.put("businessType", "4");// ????????????
        postParam.put("sysName", "??????????????????");// ????????????
        postParam.put("interfaceName", "?????????");// ???????????????
        postParam.put("interfacePhone", "18088888888");// ??????????????????
        postParam.put("fileSize", "1");// ????????????
        File[] accessories = new File[1];// ????????????
        String path = "F:";
        String filename = "fujian.sql";
        accessories[0] = new File(path + File.separator + filename);
        FileBody[] fileBodys = new FileBody[1];
        fileBodys[0] = new FileBody(accessories[0]);
        String[] accessoriesFileName = new String[1];// ??????????????????
        accessoriesFileName[0] = filename;
        String returnInfo = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httpost = new HttpPost(url);
            // FilePart???????????????????????????
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
