package com.ruoyi.activiti.web.esb.utils;

import com.ruoyi.common.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLCodeUtils {
    public static String encode(String s) {
        if (StringUtils.isEmpty(s))
            return "";
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return null;
    }

    public static String decode(String s) {
        if (StringUtils.isEmpty(s))
            return "";
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return null;
    }
}
