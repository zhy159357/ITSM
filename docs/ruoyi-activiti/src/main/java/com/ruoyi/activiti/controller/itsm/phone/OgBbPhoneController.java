package com.ruoyi.activiti.controller.itsm.phone;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.domain.TelBizTwo;
import com.ruoyi.activiti.service.impl.OgPhoneServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*
*
* 电话事件单（报表）
* */
@Controller
@RequestMapping("phone/bb")
public class OgBbPhoneController extends BaseController
{
    //新增界面路径
    private String prefix_bb = "phone/bb";

    @Autowired
    private OgPhoneServiceImpl phoneService;

    //跳转到展示的界面
    @GetMapping()
    public String phone() {
        return prefix_bb + "/phone";
    }

    //局部变量
    public static List<TelBiz> listStatic;
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TelBiz telBiz)
    {
        List<TelBiz> list = new ArrayList<>();
        List<TelBiz> list2 = new ArrayList<>();
        String phoneState = telBiz.getPhoneState();
        if(StringUtils.isNotEmpty(telBiz.getCreateTime()) && StringUtils.isNotEmpty(telBiz.getStartTimeBiz()) && StringUtils.isNotEmpty(telBiz.getPhoneState())){
            if(StringUtils.isNotEmpty(telBiz.getCreateTime())){
                String parseStart = DateUtils.handleTimeYYYYMMDD(telBiz.getCreateTime());
                telBiz.setCreateTime(parseStart);
            }
            if(StringUtils.isNotEmpty(telBiz.getStartTimeBiz())){
                String parseStart = DateUtils.handleTimeYYYYMMDD(telBiz.getStartTimeBiz());
                telBiz.setStartTimeBiz(parseStart);
            }
            if(StringUtils.isNotEmpty(telBiz.getOverFlowTime())){
                String parseStart = DateUtils.handleTimeYYYYMMDD(telBiz.getOverFlowTime());
                telBiz.setOverFlowTime(parseStart);
            }
            /*if(!StringUtils.isEmpty(telBiz.getCreateTime())){
                telBiz.setCreateTime(telBiz.getCreateTime().replaceAll("-",""));
                telBiz.setCreateTime(telBiz.getCreateTime().replaceAll(" ",""));
                telBiz.setCreateTime(telBiz.getCreateTime().replaceAll(":",""));
            }
            if(!StringUtils.isEmpty(telBiz.getStartTimeBiz())){
                telBiz.setStartTimeBiz(telBiz.getStartTimeBiz().replaceAll("-",""));
                telBiz.setStartTimeBiz(telBiz.getStartTimeBiz().replaceAll(" ",""));
                telBiz.setStartTimeBiz(telBiz.getStartTimeBiz().replaceAll(":",""));
            }*/
            //if (phoneState == "0"){
            if (phoneState.equals(0)){
                phoneService.selectPhoneList(telBiz);
            }else if ("1".equals(phoneState)){
                list = phoneService.selectPhoneDeptNameList(telBiz);
                listStatic=list;
            }else if ("2".equals(phoneState)){
                list = phoneService.selectPhoneSysIdList(telBiz);
                listStatic=list;
            }else if ("3".equals(phoneState)){
                list = phoneService.selectPhoneDeptNameAndSysIdList(telBiz);
                listStatic=list;
            }
        }else {
            //throw new BusinessException("搜索前必须选择事件三个条件同时出现才能查询，否则查詢失败。");
        }

        if(!list.isEmpty()){
            int Count=0;
            for(TelBiz tb:list){
                Count+=Integer.parseInt(tb.getT16());
            }
            for(TelBiz tbs:list){
                if(Count == 0){
                    tbs.setHoldContrast("0");
                }else{
                    float t16 = Integer.parseInt(tbs.getT16());
                    float a = t16/Count * 100;
                    String b = String.format("%.2f", a);
                    tbs.setHoldContrast( b  + "%");
                    //tbs.setHoldContrast((Integer.parseInt(tbs.getT16())/Count)+"%");

                }
                list2.add(tbs);
            }
        }
       return getDataTable(list2);
    }

    //导出电话事件单报表
    @Log(title = "电话事件单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelBiz telBiz)
    {
        List<TelBizTwo> list2 = new ArrayList<TelBizTwo>();
        if(StringUtils.isNotEmpty(listStatic)){
            for(TelBiz tb:listStatic){
                TelBizTwo telBizTmp = new TelBizTwo();
                telBizTmp.setCaption(tb.getCaption());
                telBizTmp.setContactOrg(tb.getContactOrg());
                telBizTmp.setOrgName(tb.getOrgName());
                telBizTmp.setHoldContrast(tb.getHoldContrast());
                telBizTmp.setT1(tb.getT1());
                telBizTmp.setT2(tb.getT2());
                telBizTmp.setT3(tb.getT3());
                telBizTmp.setT4(tb.getT4());
                telBizTmp.setT5(tb.getT5());
                telBizTmp.setT6(tb.getT6());
                telBizTmp.setT7(tb.getT7());
                telBizTmp.setT8(tb.getT8());
                telBizTmp.setT9(tb.getT9());
                telBizTmp.setT10(tb.getT10());
                telBizTmp.setT11(tb.getT11());
                telBizTmp.setT12(tb.getT12());
                telBizTmp.setT13(tb.getT13());
                telBizTmp.setT14(tb.getT14());
                telBizTmp.setT15(tb.getT15());
                telBizTmp.setT16(tb.getT16());
                list2.add(telBizTmp);
            }

        }

        ExcelUtil<TelBizTwo> util = new ExcelUtil<TelBizTwo>(TelBizTwo.class);
        return util.exportExcel(list2, "电话事件单报表");
    }

    //响应请求分页数据
    @Override
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    //设置请求分页数据
    @Override
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

}
