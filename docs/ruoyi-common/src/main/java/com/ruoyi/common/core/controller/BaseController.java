package com.ruoyi.common.core.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxResult.Type;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.netty.utils.DESUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    protected TableDataInfo getDataTable(List<?> list, long total, String msg) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(total);
        rspData.setMsg(msg);
        return rspData;
    }

    /**
     * 间接分页
     * @param list 处理后的list
     * @param oldList 原分页list
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list,List<?> oldList) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(oldList).getTotal());
        return rspData;
    }
    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(Type type, String message) {
        return new AjaxResult(type, message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 页面隐藏的机构id不能清除，只有机构id时默认赋值为空
     */
    public String resetOrgId(String orgId, String orgName) {
        if (orgId != null && StringUtils.isNotEmpty(orgName)) {
            return orgId;
        } else {
            return null;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss类型的时间格式化为yyyyMMddHHmmss
     *
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDDHHMMSS(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, date);
        return formatDateStr;
    }

    /**
     * 将yyyy-MM-dd类型的时间格式化为yyyyMMdd
     *
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDD(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDD, date);
        return formatDateStr;
    }

    /**
     * 将yyyyMMddHHMMSS类型的时间格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public String handleTimeYYYY_MM_DD_HH_MM_SS(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
        return formatDateStr;
    }

    /**
     * 自定义分页
     *
     * @param list
     * @return
     */
    protected TableDataInfo getDataTable_ideal(List<?> list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<?> newList;
        if (pageNum * pageSize > list.size()) {
            if (pageSize > list.size()) {
                newList = list;
            } else {
                if((pageNum - 1) * pageSize>list.size()){
                    int freeV=list.size()%pageSize;
                    newList = list.subList(list.size()-freeV, list.size());
                }else{
                    newList = list.subList((pageNum - 1) * pageSize, list.size());
                }
            }
        } else {
            newList = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(newList);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 移动app自定义分页
     *
     * @param list
     * @param pageNumStr
     * @param pageSizeStr
     * @return
     */
    protected TableDataInfo getDataTable_app(List<?> list, String pageNumStr, String pageSizeStr) {
        Integer pageNum = Integer.valueOf(pageNumStr);
        Integer pageSize = Integer.valueOf(pageSizeStr);
        List<?> newList;
        if (pageNum * pageSize > list.size()) {
            if (pageSize > list.size()) {
                newList = list;
            } else {
                if((pageNum - 1) * pageSize>list.size()){
                    newList = null;
                }else{
                    newList = list.subList((pageNum - 1) * pageSize, list.size());
                }
            }
        } else {
            newList = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(newList);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 从HttpSession中获取app请求过来的参数map
     */
    public Map<String, Object> getAppParamsMap(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object appMobileParams = session.getAttribute("appMobileParams");
        logger.debug("----------邮e助请求运维管理app后台接收参数为：" + appMobileParams.toString());
        return (Map<String, Object>) appMobileParams;
    }

    /**
     * response统一返回加密信息
     * @param response
     * @param ajaxResult
     * @param encryKey  秘钥key
     */
    public void renderString(HttpServletResponse response, AjaxResult ajaxResult, String encryKey) {
        try {
            logger.debug("----------邮e助请求运维管理app后台返回参数为：" + ajaxResult.toString());
            String encrypt = DESUtils.encrypt(encryKey, JSON.toJSONString(ajaxResult));
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Page buildPageObject(){
        Integer pageNum = ServletUtils.getParameterToInt(Constants.PAGE_NUM);
        Integer pageSize = ServletUtils.getParameterToInt(Constants.PAGE_SIZE);
        Page page=new Page<>(pageNum,pageSize);
        return page;
    }
}
