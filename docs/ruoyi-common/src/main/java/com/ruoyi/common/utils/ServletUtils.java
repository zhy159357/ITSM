package com.ruoyi.common.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruoyi.common.exception.BusinessException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ruoyi.common.core.text.Convert;

/**
 * 客户端工具类
 * 
 * @author ruoyi
 */
public class ServletUtils
{
    /**
     * 定义移动端请求的所有可能类型
     */
    private final static String[] AGENT = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };

    /**
     * 获取String参数
     */
    public static String getParameter(String name)
    {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue)
    {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name)
    {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue)
    {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     * 
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1)
        {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml"))
        {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (StringUtils.inStringIgnoreCase(ajax, "json", "xml"))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断User-Agent 是不是来自于手机
     */
    public static boolean checkAgentIsMobile(String ua)
    {
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;")))
        {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh"))
            {
                for (String item : AGENT)
                {
                    if (ua.contains(item))
                    {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 根据请求路径判断是否访问静态资源
     * @param uri
     * @return
     */
    public static boolean isStaticResources(String uri)
    {
        return uri.lastIndexOf(".css") > -1 || uri.lastIndexOf(".js")> -1 ||
                uri.lastIndexOf(".png") > -1 || uri.lastIndexOf(".jpg") > -1 ||
                uri.lastIndexOf(".woff2") > -1 || uri.lastIndexOf(".ttf") > -1 ||
                uri.lastIndexOf(".eot") > -1 || uri.lastIndexOf(".svg") > -1;
    }

    /**
     * 从request中接收参数
     * @param request
     * @return
     */
    public static String convertParam(HttpServletRequest request) {
        StringBuilder sbInfo;
        try {
            InputStream in = request.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            sbInfo = new StringBuilder();
            byte[] contents = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(contents)) != -1) {
                sbInfo.append(new String(contents, 0, bytesRead));
            }
            bis.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("参数转换异常", e);
        }
        return sbInfo.toString();
    }
}
