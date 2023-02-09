package com.ruoyi.web.controller.system;

import com.ideal.design.common.utils.DesignFtpFileUtil;
import com.ideal.design.domain.DesignFile;
import com.ruoyi.common.annotation.validation.Query;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.SysBizFile;
import com.ruoyi.system.service.ISysBizFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/system/bizFile")
@CrossOrigin
@Slf4j
public class SysBizFileController extends BaseController {
    @Autowired
    private ISysBizFileService service;

    @PostMapping("/list")
    public TableDataInfo list(SysBizFile file) {
        startPage();
        List<SysBizFile> files = this.service.list(file);
        return getDataTable(files);
    }

    @PostMapping("/listByInstanceId")
    public AjaxResult listByInstanceId(@RequestBody @Validated({Query.class}) SysBizFile file, BindingResult result) {
        if (result.hasErrors()) {
            return AjaxResult.error(result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining()));
        }
        return AjaxResult.success("获取成功", this.service.list(file));
    }

    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile[] files, String ticketId, HttpServletRequest request) throws FileUploadBase.FileSizeLimitExceededException, IOException {
        String currentUser = request.getHeader("CurrentUserId");
        List<SysBizFile> ret = new ArrayList<>();
        for (MultipartFile file : files) {
            SysBizFile file1 = this.service.upload(file, ticketId, currentUser);
            ret.add(file1);
        }
        return AjaxResult.success("上传成功", ret);
    }

    @GetMapping("/download/{id}")
    public void download(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        log.info("download."+id);
    	SysBizFile file = this.service.get(id);
    	// 客服派单下载图片处理逻辑
        if("客服派单图片".equals(file.getFileName())) {
            this.downloadServiceFiles(response, file);
        } else {
            try {
                File f = new File(file.getLocation());
                String filename = file.getOriginFileName();
                FileInputStream fileInputStream = new FileInputStream(f);
                InputStream fis = new BufferedInputStream(fileInputStream);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                //response.reset();
                response.setCharacterEncoding("UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                response.addHeader("Content-Length", "" + f.length());
                //OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                //outputStream.write(buffer);

                FileUtils.writeBytes(buffer, response.getOutputStream());
                fis.close();
                //outputStream.flush();
            } catch (IOException ex) {
                log.error("download",ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * 客服派单图片下载
     * @param response
     * @param file
     */
    public void downloadServiceFiles(HttpServletResponse response, SysBizFile file) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int size = 0;
        URL url = null;
        int bufferSize = 1024;
        byte[] buf = new byte[bufferSize];
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            url = new URL(file.getUrl());
            inputStream = new BufferedInputStream(url.openStream());
            outputStream = new BufferedOutputStream(response.getOutputStream());
            while ((size = inputStream.read()) != -1) {
                outputStream.write(buf, 0 ,size);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("客服派单下载图片信息失败，失败原因:" + e.getMessage());
        } finally {
            try{
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("客服派单下载图片信息关闭连接异常，异常原因:"+ e.getMessage());
            }
        }
        log.info("客服派单图片信息下载成功!");
    }
    
    @DeleteMapping("delete/{ids}")
    public AjaxResult delete(@PathVariable("ids") Long[] ids) {
        return AjaxResult.success(this.service.delete(ids));
    }
}
