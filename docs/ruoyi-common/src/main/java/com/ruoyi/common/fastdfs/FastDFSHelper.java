package com.ruoyi.common.fastdfs;

import com.gosft.tools.core.util.ObjectUtil;
import com.gsoft.cps.forward.api.util.MD5Utils;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;

import com.ruoyi.common.utils.security.Md5Utils;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * FastDFS上传工具类
 *
 * @author zhangchao
 */
public class FastDFSHelper {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSHelper.class);
    private static FastDFSHelper fastDFSHelper = new FastDFSHelper();
    private TrackerClient trackerClient;
    private static final String configPath = "config/fdfs_client.conf";
    private static final String linuxConfigpath = "/app/ideal/itsm/config/fdfs_client.conf";

    private FastDFSHelper() {
        try {
            String path = System.getProperty("catalina.home");
            if (StringUtils.isEmpty(path) || path.contains("emp")) {
                path = configPath;
            } else {
                path = path + File.separator + configPath;
            }
            logger.debug("--------------fastdfs配置文件路径：" + path + "--------------");
            ClientGlobal.init(path);
            trackerClient = new TrackerClient();
        } catch (IOException | MyException e) {
            try {
                ClientGlobal.init(linuxConfigpath);
                trackerClient = new TrackerClient();
            } catch (IOException | MyException ex) {
                logger.error("error", ex);
            }
        }
    }

    public static FastDFSHelper getInstance() {
        return fastDFSHelper;
    }

    /**
     * 向FastDFS上传文件
     *
     * @param localFilename 本地文件名
     * @return 上传成功，返回组名和该文件在FastDFS中的名称；上传失败，返回null
     */
    public String[] uploadFile(String localFilename, String fileName, String md5) {
        String filExtName = FilenameUtils.getExtension(fileName);
        NameValuePair[] meta = new NameValuePair[2];
        meta[0] = new NameValuePair("fileName", StringUtils.replaceSpace(fileName));
        meta[1] = new NameValuePair("md5", md5);
        String[] arr = null;
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return arr;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            arr = storageClient.upload_file(localFilename, filExtName, meta);
            if (arr == null || arr.length != 2) {
                logger.error("向FastDFS上传文件失败");
            } else {
                logger.debug("向FastDFS上传文件成功,组名:" + arr[0]);
                logger.debug("向FastDFS上传文件成功,文件在FastDFS中的名称:" + arr[1]);
            }
        } catch (IOException | MyException e) {
            e.printStackTrace();
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
        return arr;
    }

    /**
     * 上传文件，防止文件过大分次追加上传
     *
     * @param file
     * @return
     */
    public String[] uploadBreakpointFile(MultipartFile file, String... params) {
        String[] results = null;
        long start = System.currentTimeMillis();
        logger.debug("------------fastdfs上传文件开始，文件名称:" + file.getOriginalFilename() + "，文件大小:" + Convert.convertSize(file.getSize()) + "，开始时间戳为：" + start + "----------------");

        NameValuePair[] meta = new NameValuePair[2];
        meta[0] = new NameValuePair("fileName", StringUtils.replaceSpace(file.getOriginalFilename()));
        // 上传md5码值供自动化后续校验
        if(StringUtils.isNotEmpty(params) && StringUtils.isNotEmpty(params[0]))
            meta[1] = new NameValuePair("md5", params[0]);
        else
            meta[1] = new NameValuePair("md5", "");

        String fileName = file.getOriginalFilename();
        String filExtName = FileUploadUtils.getExtension(file);
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return results;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        long originalFileSize = file.getSize();
        long defaultSize = 100 * 1024 * 1024;
        byte[] file_buff;
        int number = (int) (originalFileSize / defaultSize), leftLength;
        number = originalFileSize % defaultSize == 0 ? number : number + 1;
        byte[] bytes;
        try {
            InputStream input = file.getInputStream();
            file_buff = new byte[input.available()];
            input.read(file_buff);
            //存进redis
            CacheUtils.put(fileName, filExtName);
            if (originalFileSize > defaultSize) {
                // 如果文件块大，则实现分块上传，需要准备一个空的文件
                for (int i = 0; i < number; i++) {
                    //文件名称+id为redis的key
                    Object value = CacheUtils.get(fileName + "_" + i);
                    if (ObjectUtil.isNotEmpty(value)) {
                        i = Integer.valueOf(value.toString());
                    }
                    CacheUtils.put(fileName + "_" + i, i);
                    if (originalFileSize - (i) * defaultSize < defaultSize) {
                        leftLength = (int) (originalFileSize - (i) * defaultSize);
                        leftLength = leftLength < 0 ? (int) originalFileSize : leftLength;
                        bytes = new byte[leftLength];
                    } else {
                        bytes = new byte[(int) defaultSize];
                        leftLength = (int) defaultSize;
                    }
                    String resultKey = "group" + fileName + i;
                    String md5Key = "md5" + fileName + i;
                    if (i == 0) {
                        try {
                            results = storageClient.upload_appender_file(bytes, 0, leftLength, filExtName, meta);
                        } catch (Exception f) {
                            CacheUtils.put(resultKey, results[0]);
                            CacheUtils.put(md5Key, results[1]);
                            results = storageClient.upload_appender_file(bytes, 0, leftLength, filExtName, meta);
                        }
                    } else {
                        //采用追加的方式
                        try {
                            storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
                        } catch (Exception f) {
                            logger.info("出现异常情况，继续上传。");
                            CacheUtils.put(resultKey, results[0]);
                            CacheUtils.put(md5Key, results[1]);
                            storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
                        }
                    }
                    CacheUtils.remove(fileName + "_" + i);
                    CacheUtils.remove(resultKey);
                    CacheUtils.remove(md5Key);
                }
                //写入内容
                storageClient.modify_file(results[0], results[1], 0, file_buff, 0, file_buff.length);
                CacheUtils.remove(fileName);

                long end = System.currentTimeMillis();
                logger.debug("---------- fastdfs文件上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
            } else {
                try {
                    results = storageClient.upload_file(file_buff, filExtName, meta);
                    long end = System.currentTimeMillis();
                    logger.debug("---------- fastdfs文件上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
                } catch (Exception f) {
                    results = storageClient.upload_file(file_buff, filExtName, meta);
                    long end = System.currentTimeMillis();
                    logger.debug("fastdfs重新上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
        return results;
    }

    public String[] uploadInputStream(InputStream input, String fileName, long size){
        long start = System.currentTimeMillis();
        logger.debug("------------fastdfs上传文件开始，文件名称:" + fileName + "，开始时间戳为：" + start + "----------------");

        NameValuePair[] meta = new NameValuePair[1];
        meta[0] = new NameValuePair("fileName", StringUtils.replaceSpace(fileName));
        String filExtName = FilenameUtils.getExtension(fileName);
        String[] results = null;
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return results;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        long originalFileSize = size;
        long defaultSize = 100 * 1024 * 1024;
        byte[] file_buff;
        int number = (int) (originalFileSize / defaultSize), leftLength;
        number = originalFileSize % defaultSize == 0 ? number : number + 1;
        byte[] bytes;
        try {
            file_buff = new byte[input.available()];
            input.read(file_buff);
            if (originalFileSize > defaultSize) {
                // 如果文件块大，则实现分块上传，需要准备一个空的文件
                for (int i = 0; i < number; i++) {
                    if (originalFileSize - (i) * defaultSize < defaultSize) {
                        leftLength = (int) (originalFileSize - (i) * defaultSize);
                        leftLength = leftLength < 0 ? (int) originalFileSize : leftLength;
                        bytes = new byte[leftLength];
                        if (i == 0) {
                            results = storageClient.upload_appender_file(bytes, 0, leftLength, filExtName, meta);
                        } else {
                            //采用追加的方式
                            storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
                        }
                    } else {
                        bytes = new byte[(int) defaultSize];
                        leftLength = (int) defaultSize;
                        if (i == 0) {
                            results = storageClient.upload_appender_file(bytes, 0, leftLength, filExtName, meta);
                        } else {
                            //采用追加的方式
                            storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
                        }
                    }
                }
                //写入内容
                storageClient.modify_file(results[0], results[1], 0, file_buff, 0, file_buff.length);
                long end = System.currentTimeMillis();
                logger.debug("---------- fastdfs文件上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
            } else {
                // 如果文件比默认的文件要小，则直接上传
                results = storageClient.upload_file(file_buff, filExtName, meta);
                long end = System.currentTimeMillis();
                logger.debug("---------- fastdfs文件上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
        return results;
    }



//    public String[] uploadBreakpointFile(MultipartFile file) {
//        long start = System.currentTimeMillis();
//        logger.debug("------------fastdfs上传文件开始，文件名称:" + file.getOriginalFilename() + "，文件大小:" + Convert.convertSize(file.getSize()) + "，开始时间戳为：" + start + "----------------");
//
//        NameValuePair[] meta = new NameValuePair[1];
//        meta[0] = new NameValuePair("fileName", StringUtils.replaceSpace(file.getOriginalFilename()));
//        String filExtName = FileUploadUtils.getExtension(file);
//        String[] results = null;
//        TrackerServer trackerServer;
//        try {
//            trackerServer = trackerClient.getTrackerServer();
//        } catch (IOException e) {
//            logger.error("error", e);
//            return results;
//        }
//        StorageClient storageClient = new StorageClient(trackerServer, null);
//        long originalFileSize = file.getSize();
//        long defaultSize = 100 * 1024 * 1024;
//        byte[] file_buff;
//        int number = (int) (originalFileSize / defaultSize), leftLength;
//        number = originalFileSize % defaultSize == 0 ? number : number + 1;
//        byte[] bytes;
//        try {
//            InputStream input = file.getInputStream();
//            file_buff = new byte[input.available()];
//            input.read(file_buff);
//            if (originalFileSize > defaultSize) {
//                // 如果文件块大，则实现分块上传，需要准备一个空的文件
//                for (int i = 0; i < number; i++) {
//                    if (originalFileSize - (i) * defaultSize < defaultSize) {
//                        leftLength = (int) (originalFileSize - (i) * defaultSize);
//                        leftLength = leftLength < 0 ? (int) originalFileSize : leftLength;
//                        bytes = new byte[leftLength];
//                        if (i == 0) {
//                            results = storageClient.upload_appender_file(bytes, 0, leftLength, filExtName, meta);
//                        } else {
//                            //采用追加的方式
//                            storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
//                        }
//                    } else {
//                        bytes = new byte[(int) defaultSize];
//                        leftLength = (int) defaultSize;
//                        if (i == 0) {
//                            results = storageClient.upload_appender_file(bytes, 0, leftLength, filExtName, meta);
//                        } else {
//                            //采用追加的方式
//                            storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
//                        }
//                    }
//                }
//                //写入内容
//                storageClient.modify_file(results[0], results[1], 0, file_buff, 0, file_buff.length);
//                long end = System.currentTimeMillis();
//                logger.debug("---------- fastdfs文件上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
//            } else {
//                // 如果文件比默认的文件要小，则直接上传
//                results = storageClient.upload_file(file_buff, filExtName, meta);
//                long end = System.currentTimeMillis();
//                logger.debug("---------- fastdfs文件上传成功," + "组名:" + results[0] + ",FastDFS文件名称:" + results[1] + "，上传文件总耗时:【" + (end - start) + "】毫秒");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("error", e);
//        } finally {
//            closeClient(storageClient);
//        }
//        return results;
//    }


    /**
     * *
     * 从FastDFS下载文件
     *
     * @param localFilename  本地文件名
     * @param groupName      文件在FastDFS中的组名
     * @param remoteFilename 文件在FastDFS中的名称
     */
    public void downloadFile(String localFilename, String groupName, String remoteFilename) {
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        File file = new File(localFilename);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] content = storageClient.download_file(groupName, remoteFilename);
            if (content == null || content.length == 0) {
                logger.error("文件大小为空！");
                boolean flag = file.delete();
                logger.debug("删除文件结果：{}", flag);
                return;
            }
            bos.write(content);
            logger.debug("-------------fastdfs下载文件成功： " + localFilename);
        } catch (IOException | MyException e) {
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
    }

    /**fastdfs下载读取额外的扩展参数需要使用时设置为true(本地测试)，不需要设置成false*/
    private boolean metadataFlag = false;

    /**
     * 从FastDFS下载文件
     *
     * @param groupName      文件在FastDFS中的组名
     * @param remoteFilename 文件在FastDFS中的名称
     */
    public byte[] downloadFile(String groupName, String remoteFilename) {
        long start = System.currentTimeMillis();
        logger.debug("------------fastdfs下载文件开始，" + "开始时间戳为:" + start + "----------------");

        byte[] bytes = null;
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return bytes;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            bytes = storageClient.download_file(groupName, remoteFilename);
            if (bytes == null || bytes.length == 0) {
                logger.error("文件大小为空！");
                return bytes;
            }
            if(metadataFlag){
                NameValuePair[] metadataArr = getMetadata(groupName, remoteFilename);
                if(metadataArr != null && metadataArr.length > 0){
                    for(NameValuePair nameValuePair : metadataArr){
                        logger.debug("-----------fastdfs文件[" + remoteFilename + "]扩展参数:" + nameValuePair.getName() + "=" + nameValuePair.getValue());
                    }
                }
            }
            logger.debug("-----------------fastdfs下载文件成功： " + remoteFilename);
        } catch (IOException | MyException e) {
            e.printStackTrace();
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
        long end = System.currentTimeMillis();
        logger.debug("------------fastdfs下载文件结束，" + "下载总耗时:【" + (end - start) + "】毫秒----------------");
        return bytes;
    }

    /**
     * 从FastDFS删除文件
     *
     * @param groupName      文件在FastDFS中的组名
     * @param remoteFilename 文件在FastDFS中的名称
     */
    public void deleteFile(String groupName, String remoteFilename) {
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            int i = storageClient.delete_file(groupName, remoteFilename);
            if (i == 0) {
                logger.debug("文件删除成功," + "组名:" + groupName + ",FastDFS文件名称:" + remoteFilename);
            } else {
                logger.debug("文件删除失败," + "组名:" + groupName + ",FastDFS文件名称:" + remoteFilename);
            }
        } catch (IOException | MyException e) {
            e.printStackTrace();
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
    }

    /**
     * 获取fastdfs额外的参数
     * @param groupName
     * @param remoteFilename
     * @return
     */
    public NameValuePair[] getMetadata(String groupName, String remoteFilename) {
        NameValuePair[] metadata = null;
        TrackerServer trackerServer;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            logger.error("error", e);
            return metadata;
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            metadata = storageClient.get_metadata(groupName, remoteFilename);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("------获取fastdfs额外参数错误，失败原因为:" + e.getMessage());
        } finally {
            closeClient(storageClient);
        }
        return metadata;
    }


    /**
     * 关闭客户端连接
     *
     * @param storageClient
     */
    public void closeClient(StorageClient storageClient) {
        try {
            if (storageClient != null) {
                storageClient.close();
                logger.debug("close fastDFS connection......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 向FastDFS图片服务器上传图片，返回图片url
     * @param data 已转化为byte数组的图片文件
     * @param filename 文件名
     * @return 上传成功，返回http访问url；上传失败，返回空字符串
     */
    public String uploadImage(byte[] data, String filename) {
    	String url = "";
    	TrackerServer trackerServer;
    	try {
			trackerServer = trackerClient.getTrackerServer();
		} catch (IOException e) {
			logger.error("error", e);
			return url;
		}
    	StorageClient storageClient = new StorageClient(trackerServer, null);
    	try {
    		String fileExtName = filename.substring(filename.lastIndexOf(".")+1, filename.length());//获取文件扩展名
            String[] arr = storageClient.upload_file(data, fileExtName, null);
            if (arr == null || arr.length != 2) {
                logger.error("向FastDFS上传文件失败");
            } else {
                url = "http:/" + ClientGlobal.getG_tracker_group().getTrackerServer(0).getInetSocketAddress().getAddress()
                		+ ":" + ClientGlobal.getG_tracker_http_port() + "/" + arr[0]
                		+ "/" +arr[1];		
                logger.debug("向FastDFS上传文件成功,文件访问路径:" + url);
            }
        } catch (IOException | MyException e) {
            logger.error("error", e);
        } finally {
            closeClient(storageClient);
        }
    	return url;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("C:/Users/14735/Documents/AOMS.tar");
        FileInputStream inputStream = new FileInputStream(file);
        String s = Md5Utils.md5(inputStream);
        String str = "628928631c7b41109fa8ce16eebb5f63";
        System.out.println(str.equals(s));
        //FastDFSHelper.getInstance().uploadFile("C:/Users/14735/Documents/AOMS.tar");
    }
}
