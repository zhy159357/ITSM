package com.ruoyi.activiti.controller.itsm.sawo;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.activiti.service.IForwardFileService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.common.core.domain.entity.ApiResData;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.sign.aop.ApiSignValid;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 【态势感知工单】Controller
 *
 * @author ruoyi
 * @date 2021-10-12
 */
//@Controller
@RestController
@RequestMapping("sawo/newly")
@Transactional(rollbackFor = Exception.class)
public class FmSawoController extends BaseController
{
    private String prefix_newly = "sawo/newly";

    private String processDefinitionKey="FMSAWO";

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FmSawoController.class);

    @Autowired
    private IFmSawoService fmSawoService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IForwardFileService forwardFileService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IPubAttachmentService attachmentService;

    @GetMapping()
    public String sawo()
    {
        return prefix_newly + "/sawo";
    }

    // 默认电话银行创建人ID
    @Value("${tsgz.createId}")
    private String CREATE_ID;

    @Value("${tsgz.userId}")
    private String USER_ID;

    @Value("${tsgz.app}")
    private String app;

    @Value("${tsgz.FM_SAWO_js}")
    private String FM_SAWO;

    /**
     * 查询【态势感知工单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSawo fmSawo)
    {
        startPage();
        List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        return getDataTable(list);
    }

    /**
     * 导出【态势感知工单】列表
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmSawo fmSawo)
    {
        List<FmSawo> list = fmSawoService.selectFmSawoList(fmSawo);
        ExcelUtil<FmSawo> util = new ExcelUtil<FmSawo>(FmSawo.class);
        return util.exportExcel(list, "sawo");
    }

    /**
     * 新增【态势感知工单】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        //公用的一个生成时间单号
        String bizType = "DTGZ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, nowDate);
        mmap.put("nowTime", nowTime);

        //生成UUID
        //mmap.put("ordId", UUID.getUUIDStr());
        return prefix_newly + "/add";
    }

    public String getNumber(){
        //公用的一个生成时间单号
        String bizType = "DTGZ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        return numStr;
    }

    /**
     * 新增保存【态势感知工单】
     */
    @PostMapping(value = "/addSave")
    @ResponseBody
    @Transactional
    @ApiSignValid
    public AjaxResult addSave(@RequestBody Map<String, Object> map) throws Exception
    {

        logger.info("====== in addSave successed====");

        //他调我的方法
        String bizType = "DTGZ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);

        String gaaName = (String) map.get("gaaName");
        String gaaLev = (String) map.get("gaaLev");
        String gaaCategory = (String) map.get("gaaCategory");
        String gaaDesc = (String) map.get("gaaDesc");
        String belDept = (String) map.get("belDept");
        String ordType = (String) map.get("ordType");
        String sourceIp = (String) map.get("sourceIp");
        String objIp = (String) map.get("objIp");
        String resDept = (String) map.get("resDept");
        String gaaType = (String) map.get("gaaType");
        String sysId = (String) map.get("sysId");
        String sysAdmin = (String) map.get("sysAdmin");
        String appAdmin = (String) map.get("appAdmin");
        String gaaCategoryValue = (String) map.get("gaaCategoryValue");
        String orderId = (String) map.get("orderId");
        String filePast = (String) map.get("filePast");

        logger.debug("-------------------filePast-------------------------"+filePast);

        if (StringUtils.isEmpty(gaaName)) {
            return AjaxResult.error("告警/漏洞/问题名称非空！请输入");
        }
        if (StringUtils.isEmpty(gaaLev)) {
            return AjaxResult.error("告警/漏洞/问题级别非空！请输入");
        }
        if (StringUtils.isEmpty(gaaCategory)) {
            return AjaxResult.error("告警/漏洞/问题类别key非空！请输入");
        }
        if (StringUtils.isEmpty(gaaCategoryValue)) {
            return AjaxResult.error("告警/漏洞/问题类别value非空！请输入");
        }
        if (StringUtils.isEmpty(gaaDesc)) {
            return AjaxResult.error("告警/漏洞/问题描述非空！请输入");
        }
        if (StringUtils.isEmpty(orderId)){
            return AjaxResult.error("工单编号非空！请输入");
        }

        FmSawo fmSawo=new FmSawo();
        String ordNo=getNumber();
        fmSawo.setCreaterTime(DateUtils.dateTimeNow());
        fmSawo.setOrdId(UUID.getUUIDStr());
        fmSawo.setOrdNo(ordNo);
        //保存map传过来的值到fmSawo中
        fmSawo.setGaaName(gaaName);
        fmSawo.setGaaLev(gaaLev);
        fmSawo.setOrdType(ordType);
        fmSawo.setSourceIp(sourceIp);
        fmSawo.setObjIp(objIp);
        fmSawo.setGaaType(gaaType);
        fmSawo.setGaaCategory(gaaCategory);
        fmSawo.setSysId(sysId);
        fmSawo.setBelDept(belDept);
        fmSawo.setGaaCategoryValue(gaaCategoryValue);
        fmSawo.setResDept(resDept);
        fmSawo.setSysAdmin(sysAdmin);
        fmSawo.setAppAdmin(appAdmin);
        fmSawo.setGaaDesc(gaaDesc);
        //工单id
        fmSawo.setOrderId(orderId);
        fmSawo.setOrdState("1");
        fmSawo.setOrdNo(bizType + nowDateStr + numStr);
        fmSawo.setOrdId(UUID.getUUIDStr());
        fmSawo.setCreaterTime(DateUtils.dateTimeNow());

        if (StringUtils.isEmpty(filePast)){
            log.debug("============================没有传附件的情况下=================================="+filePast);
        }else {
            log.debug("============================传附件的情况下=================================="+filePast);
            String base64=filePast;
            String path=app;
            try {
                byte[] byteBase64 =Base64.getDecoder().decode(base64);
                ByteArrayInputStream byteArray = new ByteArrayInputStream(byteBase64);
                ZipInputStream zipInput = new ZipInputStream(byteArray);
                ZipEntry entry = zipInput.getNextEntry();
                File fout = null;
                File file = new File(path);
                //获取它的一个跟目录
                //String parent = file.getParent();
                while (entry != null && !entry.isDirectory()) {
                    log.info("文件名称:    [{}]", entry.getName());
                    fout = new File(file, entry.getName());
                    if (!fout.exists()) {
                        (new File(fout.getParent())).mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fout));
                    int offo = -1;
                    byte[] buffer = new byte[2048];
                    while ((offo = zipInput.read(buffer)) != -1) {
                        bos.write(buffer, 0, offo);
                    }
                    bos.close();
                    // 获取 下一个文件
                    entry = zipInput.getNextEntry();

                    //添加附件
                    InputStream in = new FileInputStream(fout);
                    //文件的大小（我）
                    float available = (float) in.available() / 1024;
                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                    String format = decimalFormat.format(available);
                    //String sizetwo = String.valueOf(available / 1024);
                    //转为字符流(我添加的)
                    Reader reader = new InputStreamReader(in);
                    String[] results= FastDFSHelper.getInstance().uploadInputStream(in,file.getName(),in.available());
                    if (ObjectUtils.isEmpty(results)) {
                        logger.debug("上传失败");
                    }
                    Attachment attachment = new Attachment();
                    attachment.setAtId(UUID.getUUIDStr());
                    attachment.setCreateId(CREATE_ID);
                    attachment.setOwnerId(fmSawo.getOrdId());
                    attachment.setSize(format+"KB");
                    attachment.setFileTime(DateUtils.dateTimeNow());
                    attachment.setFileName(StringUtils.replaceSpace(fout.getName()));
                    attachment.setGroupName(results[0]);
                    attachment.setFilePath(results[1]);
                    attachment.setFileType("1");
                    attachment.setAutomateStatus("1");
                    attachmentService.insertAttachment(attachment);

                }
                zipInput.close();
                byteArray.close();
            } catch (Exception e) {
                throw new RuntimeException("解压流出现异常", e);
            }
        }

        //流程相关代码
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("reCode","0");
        reMap.put("createId", USER_ID);
        reMap.put("groupId", FM_SAWO);
        reMap.put("userId",USER_ID);
        try {
            fmSawoService.insertFmSawo(fmSawo);
            activitiCommService.startProcess(fmSawo.getOrdId(),processDefinitionKey,reMap);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("工单创建失败");
        }

        return AjaxResult.success("工单创建成功");
    }

    /**
     * 修改【态势感知工单】
     */
    @GetMapping("/edit/{ordId}")
    public String edit(@PathVariable("ordId") String ordId, ModelMap mmap)
    {
        FmSawo fmSawo = fmSawoService.selectFmSawoById(ordId);
        mmap.put("fmSawo", fmSawo);
        return prefix_newly + "/edit";
    }

    /**
     * 修改保存【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmSawo fmSawo)
    {
        return toAjax(fmSawoService.updateFmSawo(fmSawo));
    }

    /**
     * 删除【态势感知工单】
     */
    @Log(title = "【态势感知工单】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(fmSawoService.deleteFmSawoByIds(ids));
    }
}
