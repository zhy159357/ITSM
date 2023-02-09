package com.ruoyi.activiti.web.esb.impl;

import com.ruoyi.activiti.web.esb.EsbHttpFileService;
import com.ruoyi.activiti.web.esb.entity.FileStore;
import com.ruoyi.activiti.web.esb.entity.SecurityUTF;
import com.ruoyi.activiti.web.esb.utils.SecurityUTFUtils;
import com.ruoyi.activiti.web.esb.utils.URLCodeUtils;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.UUID;

@Service
public class EsbHttpFileServiceImpl implements EsbHttpFileService {

    @Value("${esb.file.serverUrl}")
    private String serverUrl;

    @Value("${esb.syscode}")
    private String syscode;

    @Value("${esb.file.token}")
    private String token;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Override
    public String uploadFile(String fileName, File file, String userId, String fileGroup, String fileType)
            throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String md5 = com.ruoyi.activiti.web.esb.utils.FileUtils.getMd5ByFile(file);
        SecurityUTF securityUTF = SecurityUTF.newSecurityUTF(this.syscode, this.token);

        HttpPost request = new HttpPost(this.serverUrl + "/upload.html");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        String storeFilePath = null;
        try {
            builder.addPart("ef_security_header", new ByteArrayBody(SecurityUTFUtils.getByteArrayBody(securityUTF), "ef_security_header"));

            FileBody fileBody = new FileBody(file);
            builder.addPart(fileName, fileBody);

            builder.addTextBody("userId", URLCodeUtils.encode(userId));
            builder.addTextBody("fileGroup", URLCodeUtils.encode(fileGroup));
            builder.addTextBody("fileType", URLCodeUtils.encode(fileType));
            builder.addTextBody("md5", URLCodeUtils.encode(md5));

            HttpEntity httpEntity = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get("UTF-8")).build();

            request.setEntity(httpEntity);

            storeFilePath = (String) httpclient.execute(request, new ResponseHandler() {
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    if (200 == response.getStatusLine().getStatusCode()) {
                        Header filePathHeader = response.getLastHeader("ef_storeFilePath");
                        return URLCodeUtils.decode(filePathHeader.getValue());
                    }
                    if (400 == response.getStatusLine().getStatusCode()) {
                        Header errorMsg = response.getLastHeader("ef_errormsg");
                        throw new BusException(URLCodeUtils.decode(errorMsg.getValue()));
                    }
                    if (404 == response.getStatusLine().getStatusCode()) {
                        throw new BusException("连接文件服务器失败...");
                    }
                    throw new BusException("未知错误..." + response.getStatusLine().getStatusCode());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("上传文件失败..." + e.getMessage());
        }
        return storeFilePath;
    }

    public File downloadFile(String filePath, String absolute) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost request = new HttpPost(this.serverUrl + "/download.html");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        SecurityUTF securityUTF = SecurityUTF.newSecurityUTF(this.syscode, this.token);
        try {
            builder.addPart("ef_security_header", new ByteArrayBody(SecurityUTFUtils.getByteArrayBody(securityUTF), "ef_security_header"));

            builder.addTextBody("filePath", URLCodeUtils.encode(filePath));
            builder.addTextBody("absolute", URLCodeUtils.encode(absolute));

            HttpEntity httpEntity = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get("UTF-8")).build();

            request.setEntity(httpEntity);

            File file = (File) httpclient.execute(request, new ResponseHandler() {
                public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    if (200 == response.getStatusLine().getStatusCode()) {
                        InputStream in = response.getEntity().getContent();
                        String fileName = null;
                        Header fileNameHeader = response.getLastHeader("fileName");
                        if (fileNameHeader != null)
                             fileName = URLDecoder.decode(fileNameHeader.getValue(), "UTF-8");
                        else {
                            fileName = UUID.randomUUID().toString();
                        }
                        File file = EsbHttpFileServiceImpl.this.storeTmp(in, fileName);
                        return file;
                    }
                    if (400 == response.getStatusLine().getStatusCode()) {
                        Header errorMsg = response.getLastHeader("ef_errormsg");
                        throw new BusException(URLCodeUtils.decode(errorMsg.getValue()));
                    }
                    if (404 == response.getStatusLine().getStatusCode()) {
                        throw new BusException("连接文件服务器失败...");
                    }
                    throw new BusException("未知错误..." + response.getStatusLine().getStatusCode());
                }
            });
            if (file.exists()) {
                return file;
            }
            throw new BusException("未知错误...");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("下载文件失败..." + e.getMessage());
        }
    }

    private File storeTmp(InputStream in, String fileName) {
        File tmpPath = this.fileUploadConfig.getRandomTmpPath("webdowntmp");
        File file = new File(tmpPath, fileName);
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(in, file);
        } catch (IOException e) {
            throw new BusException("文件写入异常");
        }
        return file;
    }

    public void delFile(String refPath) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost request = new HttpPost(this.serverUrl + "/delFile.html");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        SecurityUTF securityUTF = SecurityUTF.newSecurityUTF(this.syscode, this.token);
        try {
            builder.addPart("ef_security_header", new ByteArrayBody(SecurityUTFUtils.getByteArrayBody(securityUTF), "ef_security_header"));

            builder.addTextBody("refPath", URLCodeUtils.encode(refPath));

            HttpEntity httpEntity = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get("UTF-8")).build();

            request.setEntity(httpEntity);

            httpclient.execute(request, new ResponseHandler() {
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    if (200 == response.getStatusLine().getStatusCode())
                         return "1";
                    if (400 == response.getStatusLine().getStatusCode()) {
                        Header errorMsg = response.getLastHeader("ef_errormsg");
                        throw new BusException(URLCodeUtils.decode(errorMsg.getValue()));
                    }
                    if (404 == response.getStatusLine().getStatusCode()) {
                        throw new BusException("连接文件服务器失败...");
                    }
                    throw new BusException("未知错误..." + response.getStatusLine().getStatusCode());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("删除fileStore失败..." + refPath + ":" + e.getMessage());
        }
    }

    public FileStore getFileStoreByPath(String refPath) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost request = new HttpPost(this.serverUrl + "/getFileStoreByPath.html");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        SecurityUTF securityUTF = SecurityUTF.newSecurityUTF(this.syscode, this.token);
        try {
            builder.addPart("ef_security_header", new ByteArrayBody(SecurityUTFUtils.getByteArrayBody(securityUTF), "ef_security_header"));

            builder.addTextBody("refPath", URLCodeUtils.encode(refPath));

            HttpEntity httpEntity = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get("UTF-8")).build();

            request.setEntity(httpEntity);
            return (FileStore) httpclient.execute(request, new ResponseHandler() {
                public FileStore handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

                    if (200 == response.getStatusLine().getStatusCode()) {
                        FileStore fileStore = new FileStore();
                        fileStore.setFileStoreId(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "fileStoreId"));
                        fileStore.setFilePath(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "filePath"));
                        fileStore.setUploadFileName(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "uploadFileName"));
                        fileStore.setStatus(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "status"));
                        fileStore.setTimestamp(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "timestamp"));
                        try {
                            fileStore.setFileSize(Integer.valueOf(Integer.parseInt(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "fileSize"))));
                        } catch (Exception e) {
                        }
                        fileStore.setUserId(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "userId"));
                        fileStore.setFileType(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "fileType"));
                        fileStore.setFileGroup(EsbHttpFileServiceImpl.this.getLastHeaderValue(response, "fileGroup"));

                        if ((StringUtils.isEmpty(fileStore.getFileStoreId())) || (StringUtils.isEmpty(fileStore.getFilePath()))) {
                            return null;
                        }
                        return fileStore;
                    }
                    if (400 == response.getStatusLine().getStatusCode()) {
                        Header errorMsg = response.getLastHeader("ef_errormsg");
                        throw new BusException(URLCodeUtils.decode(errorMsg.getValue()));
                    }
                    if (404 == response.getStatusLine().getStatusCode()) {
                        throw new BusException("连接文件服务器失败...");
                    }
                    throw new BusException("未知错误..." + response.getStatusLine().getStatusCode());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("获取fileStore失败..." + refPath + ":" + e.getMessage());
        }
    }

    public void updateFileStore(FileStore fileStore) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost request = new HttpPost(this.serverUrl + "/updateFileStore.html");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        SecurityUTF securityUTF = SecurityUTF.newSecurityUTF(this.syscode, this.token);
        try {
            builder.addPart("ef_security_header", new ByteArrayBody(SecurityUTFUtils.getByteArrayBody(securityUTF), "ef_security_header"));

            builder.addTextBody("fileStoreId", URLCodeUtils.encode(fileStore.getFileStoreId()));
            builder.addTextBody("filePath", URLCodeUtils.encode(fileStore.getFilePath()));
            builder.addTextBody("uploadFileName", URLCodeUtils.encode(fileStore.getUploadFileName()));
            builder.addTextBody("status", URLCodeUtils.encode(fileStore.getStatus()));
            builder.addTextBody("timestamp", URLCodeUtils.encode(fileStore.getTimestamp()));
            builder.addTextBody("fileSize", URLCodeUtils.encode(String.valueOf(fileStore.getFileSize())));
            builder.addTextBody("userId", URLCodeUtils.encode(fileStore.getUserId()));
            builder.addTextBody("fileType", URLCodeUtils.encode(fileStore.getFileType()));
            builder.addTextBody("fileGroup", URLCodeUtils.encode(fileStore.getFileGroup()));

            HttpEntity httpEntity = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(CharsetUtils.get("UTF-8")).build();

            request.setEntity(httpEntity);
            httpclient.execute(request, new ResponseHandler() {
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    if (200 == response.getStatusLine().getStatusCode())
                         return null;
                    if (400 == response.getStatusLine().getStatusCode()) {
                        Header errorMsg = response.getLastHeader("ef_errormsg");
                        throw new BusException(URLCodeUtils.decode(errorMsg.getValue()));
                    }
                    if (404 == response.getStatusLine().getStatusCode()) {
                        throw new BusException("连接文件服务器失败...");
                    }
                    throw new BusException("未知错误..." + response.getStatusLine().getStatusCode());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("更新fileStore失败..." + fileStore + ":" + e.getMessage());
        }
    }

    private String getLastHeaderValue(HttpResponse response, String key) {
        Header header = response.getLastHeader(key);
        if (header != null) {
            return URLCodeUtils.decode(header.getValue());
        }
        return null;
    }
}
