package com.ruoyi.activiti.controller.itsm.attachment;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.service.IAutoItsmFastfileService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.activiti.service.IForwardFileService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.AutoItsmFastfile;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.netty.vo.FileUploadResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ISysApplicationManagerService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 附件功能控制层
 *
 * @author 14735
 * @date 2020-12-16
 */
@Controller
@RequestMapping("pub/attachment")
public class PubAttachmentController extends BaseController {
    private String prefix = "attachment";

    @Autowired
    private IPubAttachmentService iPubAttachmentService;

    @Autowired
    private IForwardFileService forwardFileService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private ICmBizSingleService cmBizSingleService;

    @Autowired
    private IAutoItsmFastfileService autoItsmFastfileService;

    /**
     * 查询附件列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Attachment attachment) {
        Map<String, Object> params = attachment.getParams();
        if (StringUtils.isNotEmpty((String) params.get("fileTimeStart"))) {
            params.put("fileTimeStart", handleTimeYYYYMMDDHHMMSS((String) params.get("fileTimeStart")));
        }
        if (StringUtils.isNotEmpty(attachment.getFileTime())) {
            attachment.setFileTime(handleTimeYYYYMMDD(attachment.getFileTime()) + "240000");
        }
        startPage();
        List<Attachment> list = iPubAttachmentService.selectAttachmentList(attachment);
        if("versionReadyStatus".equals(attachment.getRemark())){
            for(Attachment att : list) {
                if("2".equals(att.getFileType())){
                    AutoItsmFastfile autoItsmFastfile = new AutoItsmFastfile();
                    autoItsmFastfile.setFastno(att.getFilePath());
                    List<AutoItsmFastfile> autoItsmFastfiles = autoItsmFastfileService.selectAutoItsmFastfileList(autoItsmFastfile);
                    if(!CollectionUtils.isEmpty(autoItsmFastfiles)){
                        AutoItsmFastfile fastfile = autoItsmFastfiles.get(0);
                        att.getParams().put("yzStatus", "1".equals(fastfile.getYzCenterStatus()) ? 1 : 0);
                        att.getParams().put("hfStatus", "1".equals(fastfile.getHfCenterStatus()) ? 1 : 0);
                    } else {
                        att.getParams().put("yzStatus", 0);
                        att.getParams().put("hfStatus", 0);
                    }
                    att.getParams().put("hiddenStatus", 0);
                } else {
                    // hiddenStatus=1表示不是版本包类型的附件前端需要隐藏亦庄｜合肥以及圆圈
                    att.getParams().put("hiddenStatus", 1);
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 查询附件列表 去掉分页查询所有
     */
    @PostMapping("/listAll")
    @ResponseBody
    public List<Attachment> listAll(Attachment attachment) {
        List<Attachment> list = iPubAttachmentService.selectAttachmentList(attachment);
        return list;
    }

    /**
     * 保存附件
     * file_type 1-普通附件  2-版本包  3-自动化步骤
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult saveAttachment(String ownerId, String fileType, String businessNumberNo, String sysId, String memo,
                                     @RequestParam("file_data") MultipartFile file) {
        try {
            //String filePathName = FileUploadUtils.upload(RuoYiConfig.getProfile(), businessNumberNo, file);
            // 附件内容大小如果大于2GB，直接抛出异常信息提示
            long fileSize = file.getSize();
            long maxSize = 2 * 1024 * 1024 * 1024L;// 2GB
            if(fileSize > maxSize) {
                return error("附件【" + file.getOriginalFilename() + "】的大小超过2GB，请自行拆分后再进行上传！");
            }
            String  filename=file.getOriginalFilename();
            String ext=filename.substring(filename.lastIndexOf(".")+1);
            if("exe".equals(ext)){
                return error("禁止上传exe类型附件！");
            }
            // 如果是升级后评估页面上传的附件图片，默认附件类型为普通附件，标识flag作为上传截图的依据
            String flag = "";
            if(VersionStatusConstants.VERSION_INFO_IMAGE.equals(businessNumberNo)){
                fileType = "1";
                flag = VersionStatusConstants.VERSION_INFO_IMAGE;
                businessNumberNo = "";
            }

            checkBusinessNumberNo(sysId, fileType, businessNumberNo, file);
            getCmBizByFileCount(ownerId, fileType);
            String md5 = null;
            if("2".equals(fileType)){
                // 校验版本包名称不能有中文
                if(StringUtils.hasChinese(filename))
                    throw new BusinessException("版本包名称不能包含中文！");
                // 版本包需要获取md5码保存
                md5 = Md5Utils.md5(file.getInputStream());
            }
            String[] arr = FastDFSHelper.getInstance().uploadBreakpointFile(file, md5);
            if(StringUtils.isEmpty(arr)){
                return error("文件上传失败，失败原因:运维管理平台连接fastDFS文件服务器错误！");
            }
            Attachment attachment = new Attachment();
            attachment.setAtId(UUID.getUUIDStr());
            attachment.setCreateId(ShiroUtils.getUserId());
            attachment.setOwnerId(ownerId);
            attachment.setSize(Convert.convertSize(fileSize));
            attachment.setFileTime(DateUtils.dateTimeNow());
            attachment.setFileName(StringUtils.replaceSpace(file.getOriginalFilename()));
            attachment.setGroupName(arr[0]);
            attachment.setFilePath(arr[1]);
            attachment.setFileType(fileType);
            attachment.setFlag(flag);
            attachment.setMemo(memo);
            attachment.setMd5(md5);
            attachment.setAutomateStatus("1");
            return toAjax(iPubAttachmentService.insertAttachment(attachment));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("上传文件失败，失败原因:" + e.getMessage());
        }
    }

    @PostMapping("/saveSh")
    @ResponseBody
    public AjaxResult saveSh(String ownerId, String memo, @RequestParam("file_data") MultipartFile file) {
        long fileSize = file.getSize();
        long maxSize = 2 * 1024 * 1024 * 1024L;// 2GB
        if(fileSize > maxSize) {
            return error("附件【" + file.getOriginalFilename() + "】的大小超过2GB，请自行拆分后再进行上传！");
        }
        String filePathName = "";
        try {
            filePathName = FileUploadUtils.upload(RuoYiConfig.getProfile(), file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("文件上传失败！");
        }

        Attachment attachment = new Attachment();
        attachment.setAtId(UUID.getUUIDStr());
        attachment.setCreateId(ShiroUtils.getUserId());
        attachment.setOwnerId(ownerId);
        attachment.setSize(Convert.convertSize(fileSize));
        attachment.setFileName(StringUtils.replaceSpace(file.getOriginalFilename()));
        attachment.setFilePath(filePathName);
        attachment.setFileTime(DateUtils.dateTimeNow());
        attachment.setMemo(memo);
        return toAjax(iPubAttachmentService.insertAttachment(attachment));
    }

    /**
     *
     */
    public void getCmBizByFileCount(String ownerId, String fileType) {
        if (cmBizSingleService.selectCmBizSingleById(ownerId) != null && "3".equals(fileType)) {
            Attachment att = new Attachment();
            att.setFileType("3");
            att.setOwnerId(ownerId);
            List<Attachment> list = iPubAttachmentService.selectAttachmentList(att);
            if (!list.isEmpty()) {
                throw new BusinessException("已存在自动化步骤excl，如需重新上传excl请删除文件列表中的自动化步骤excl。");
            }
        }
    }

    /**
     * 版本发布上传自动化步骤附件增加校验
     *
     * @param fileType
     * @param businessNumberNo
     * @param file
     */
    public void checkBusinessNumberNo(String sysId, String fileType, String businessNumberNo, MultipartFile file) {
        if (StringUtils.isNotEmpty(businessNumberNo) && "3".equals(fileType)) {
            if (businessNumberNo.contains("BB")) {
                Workbook wb = null;
                Sheet sheet = null;
                try {
                    wb = WorkbookFactory.create(file.getInputStream());
                    // 检验单号时默认取第一个sheet页，第一行第二列
                    sheet = wb.getSheetAt(0);
                    Row row0 = sheet.getRow(0);
                    Cell cell1 = row0.getCell(1);
                    if (StringUtils.isNotNull(cell1)) {
                        String versionInfoNo = cell1.getStringCellValue();
                        if (!businessNumberNo.equals(versionInfoNo)) {
                            throw new BusinessException("上传自动化步骤附件单号不正确,正确单号为:" + businessNumberNo + ",请检查单号后重新上传！");
                        }
                    } else {
                        throw new BusinessException("上传自动化步骤附件单号不能为空！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BusinessException("上传自动化步骤附件单号不正确，请检查单号后重新上传！");
                }

                try {
                    if (StringUtils.isNotEmpty(sysId)) {
                        // 检验系统编码时默认取第一个sheet页，第二行第二列
                        Row row1 = sheet.getRow(1);
                        Cell cell1 = row1.getCell(1);
                        if (StringUtils.isNotNull(cell1)) {
                            String sysCode = cell1.getStringCellValue();
                            OgSys ogSys = applicationManagerService.selectOgSysBySysId(sysId);
                            if (!sysCode.equals(ogSys.getCode())) {
                                throw new BusinessException("上传自动化步骤附件中的系统简称编码与版本单选择的系统编码不一致！");
                            }
                        }
                    } else {
                        throw new BusinessException("上传自动化步骤附件中的系统简称编码与版本单选择的系统编码不一致！");
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                    throw new BusinessException("上传自动化步骤附件中的系统简称编码与版本单选择的系统编码不一致！");
                }
            }
        }
    }

    /**
     * 删除附件的同时删除磁盘保存的附件
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult deleteAttachmentByIds(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] ss = Convert.toStrArray(ids);
            for (String id : ss) {
                Attachment attachment = iPubAttachmentService.selectAttachmentById(id);
                // 如果当前附件不是自己上传则不能删除
                if (StringUtils.isNotNull(attachment) && !attachment.getCreateId().equals(ShiroUtils.getUserId())) {
                    return error("当前附件不属于自己创建不能删除！");
                }
                if (StringUtils.isNotEmpty(attachment.getFilePath())) {
                    FastDFSHelper.getInstance().deleteFile(attachment.getGroupName(), attachment.getFilePath());
                }
            }
        }
        return toAjax(iPubAttachmentService.deleteAttachmentByIds(ids));
    }

    @GetMapping("/upknow/{ownerId}")
    public String upknow(@PathVariable("ownerId") String ownerId, ModelMap mmap) {
        mmap.put("ownerId", ownerId);
        return prefix + "/upknow";
    }

    /**
     * 上传附件页面
     * ownerId为业务表主键id
     * businessNumberNo为业务单编号（唯一，作为自动化查找附件的依据）
     */
    @RequestMapping(value = {"/upload/{ownerId}/{businessNumberNo}", "/upload/{ownerId}/{businessNumberNo}/{sysId}"}, method = RequestMethod.GET)
    public String upload(@PathVariable("ownerId") String ownerId, @PathVariable("businessNumberNo") String businessNumberNo,
                         @PathVariable(value = "sysId", required = false) String sysId, ModelMap mmap) {
        mmap.put("ownerId", ownerId);
        mmap.put("businessNumberNo", businessNumberNo);
        mmap.put("sysId", sysId);
        return prefix + "/upload";
    }

    @GetMapping("/upload/{ownerId}")
    public String upload(@PathVariable("ownerId") String ownerId, ModelMap mmap) {
        mmap.put("ownerId", ownerId);
        return prefix + "/upload";
    }

    /**
     * 上海银行附件上传
     */
    @GetMapping("/uploadSH/{ownerId}")
    public String uploadSH(@PathVariable("ownerId") String ownerId, ModelMap map) {
        map.put("ownerId", ownerId);
        return prefix + "/uploadSH";
    }

    /**
     * 附件下载
     */
    @GetMapping("/download/{ownerId}/{atId}")
    public void download(@PathVariable("ownerId") String ownerId, @PathVariable("atId") String atId,
                         HttpServletResponse response, HttpServletRequest request) throws Exception {
        Attachment att = new Attachment();
        att.setOwnerId(ownerId);
        att.setAtId(atId);
        List<Attachment> attachments = iPubAttachmentService.selectAttachmentList(att);
        for (Attachment attachment : attachments) {
            // 版本包如果已被清理，则直接抛出异常信息提示
            if ("autoClean".equals(attachment.getFlag())) {
                throw new BusinessException("该版本包已被清理，不可下载！");
            }
            String filePath = attachment.getFilePath();
            String fileName = attachment.getFileName();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, fileName));
            byte[] bytes = FastDFSHelper.getInstance().downloadFile(attachment.getGroupName(), filePath);
            FileUtils.writeBytes(bytes, response.getOutputStream());
        }
    }

    /**
     * 附件下载上海银行
     */
    @GetMapping("/downloadSH/{ownerId}/{atId}")
    public void downloadSH(@PathVariable("ownerId") String ownerId, @PathVariable("atId") String atId,
                         HttpServletResponse response, HttpServletRequest request) {
        Attachment att = new Attachment();
        att.setOwnerId(ownerId);
        att.setAtId(atId);
        List<Attachment> attachments = iPubAttachmentService.selectAttachmentList(att);
        for (Attachment attachment : attachments) {
            String filePath = attachment.getFilePath();
            String fileName = attachment.getFileName();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            try {
                response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, fileName));
                FileUtils.writeBytes(filePath, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("附件下载失败！");
            }
        }
    }

    /**
     * 关联附件保存
     *
     * @param attachment
     * @return
     */
    @PostMapping("/relation")
    @ResponseBody
    public AjaxResult relation(Attachment attachment) {
        FileUploadResult fileUploadResult = forwardFileService.getFileUploadResult(attachment.getFileCiphertext());
        if (StringUtils.isNull(fileUploadResult)) {
            throw new BusinessException("文件信息解密失败");
        }
        attachment.setFileName(fileUploadResult.getFileName());
        attachment.setFileTime(DateUtils.dateTimeNow());
        attachment.setFilePath(fileUploadResult.getPath());
        attachment.setAtId(UUID.getUUIDStr());
        attachment.setCreateId(ShiroUtils.getUserId());
        // 默认附加未下载
        attachment.setFileStatus("1");
        return toAjax(iPubAttachmentService.insertAttachment(attachment));
    }

    /**
     * 资源变更单自动化删除附件专用
     */
    @PostMapping("/removeCmsj")
    @ResponseBody
    public AjaxResult removeCmsj(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] ss = Convert.toStrArray(ids);
            for (String id : ss) {
                Attachment attachment = iPubAttachmentService.selectAttachmentById(id);

                //如果当前附件不是自己上传则不能删除
                if (attachment != null && !attachment.getCreateId().equals(ShiroUtils.getUserId())) {
                    return error("当前附件不属于自己创建不能删除！");
                }
                if ("3".equals(attachment.getFileType()) && "0".equals(attachment.getAutomateStatus()) && StringUtils.isNotEmpty(attachment.getAutomateResultMsg())) {
                    iPubAttachmentService.deleteAttachmentByIds(attachment.getAutomateResultMsg());
                }
                if (StringUtils.isNotEmpty(attachment.getFilePath())) {
                    FastDFSHelper.getInstance().deleteFile(attachment.getGroupName(), attachment.getFilePath());
                }
            }
        }

        return toAjax(iPubAttachmentService.deleteAttachmentByIds(ids));
    }

    /**
     * 附件管理页面
     *
     * @return
     */
    @GetMapping("attachmentList")
    public String attachmentList() {
        return prefix + "/attachment";
    }

    /**
     * 附件管理界面删除附件
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(String ids) {
        for (String id : Convert.toStrArray(ids)) {
            Attachment attachment = iPubAttachmentService.selectAttachmentById(id);
            if (attachment == null || "autoClean".equals(attachment.getFlag())) {
                continue;
            }
            if (StringUtils.isNotEmpty(attachment.getFilePath())) {
                FastDFSHelper.getInstance().deleteFile(attachment.getGroupName(), attachment.getFilePath());
                Attachment attachment1 = new Attachment();
                attachment1.setFlag("autoClean");
                attachment1.setAtId(attachment.getAtId());
                iPubAttachmentService.updateAttachment(attachment1);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 附件管理批量删除
     *
     * @param mmap
     * @return
     */
    @GetMapping("/removeList")
    public String removeList(ModelMap mmap) {
        return prefix + "/attachmentList";
    }
}
