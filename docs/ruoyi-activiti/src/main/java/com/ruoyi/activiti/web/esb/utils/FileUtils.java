package com.ruoyi.activiti.web.esb.utils;

/*
 * YOUI框架
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件工具类
 *
 * @author LiuPeng
 *
 */
public class FileUtils {

    private static Log logger = LogFactory.getLog(FileUtils.class);

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private FileUtils() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 获取文件md5
     *
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, length);
            }
            value = bufferToHex(messagedigest.digest());
        } catch (Exception ex) {
            logger.error("获取文件md5失败", ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                logger.error("关闭文件流失败", ex);
            }
        }
        return value;
    }

    /**
     * 获取文件流MD5
     *
     * @param in
     * @return
     */
    public static String getMd5ByInputStream(InputStream in) {
        String value = null;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, length);
            }
            value = bufferToHex(messagedigest.digest());
        } catch (Exception ex) {
            logger.error("获取文件流md5失败", ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                logger.error("关闭文件流失败", ex);
            }
        }
        return value;
    }

    /**
     * 文件大小转换文字
     *
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 删除文件/文件夹
     *
     * @param filePaths
     */
    public static void delete(String... filePaths) {
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists()) {
                delete(file);
            }
        }
    }

    public static void delete(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    delete(f);
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }

    public static String formatFileName(String fileName) {
        char[] content = new char[fileName.length()];
        char[] result = new char[fileName.length()];
        fileName.getChars(0, fileName.length(), content, 0);

        int len = 0;
        for (int i = 0; i < content.length; ++i) {
            if ((content[i] == '%') && (content[(i + 1)] == '2') && (content[(i + 2)] == '0')) {
                result[(len++)] = ' ';
                i += 2;
            } else {
                result[(len++)] = content[i];
            }
        }
        return String.valueOf(result, 0, len);
    }

    /**
     *
     * @param dirPath
     * @return
     */
    public static File checkAndCreateDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    public static void checkAndCreateSubDir(String dirPath, String subPath) {
        //
        subPath = subPath.replace("\\", "/");
        String[] subDirs = subPath.split("/");
        // for()
        String checkPath = dirPath;
        for (String subDir : subDirs) {
            checkPath = checkPath + "/" + subDir;
            checkAndCreateDir(checkPath);
        }
    }
}
