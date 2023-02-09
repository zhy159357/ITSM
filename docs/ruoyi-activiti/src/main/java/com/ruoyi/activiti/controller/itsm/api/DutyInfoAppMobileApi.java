package com.ruoyi.activiti.controller.itsm.api;


import com.ruoyi.activiti.service.IDutyInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * APP值班接口
 */
@Controller
@RequestMapping("/appMobile/duty")
public class DutyInfoAppMobileApi extends BaseController {

    @Autowired
    private IDutyInfoService iDutyInfoService;


    /**
     * 值班日历日期高亮接口
     * @param  map
     *  dutyDate
     *  custNo
     * @return
     */
    @PostMapping("/getDutyCalendar")
    @ResponseBody
    public AjaxResult getDutyCalendar(@RequestBody Map<String, String> map){
        if(StringUtils.isEmpty(map.get("custNo"))){
            return AjaxResult.error("柜员号为空");
        }
        if(StringUtils.isEmpty(map.get("dutyDate"))){
            return AjaxResult.error("时间为空");
        }

        return iDutyInfoService.selectDutyCalendar(map.get("custNo"),map.get("dutyDate"));
    }

    /**
     * 获取值班类型的前端查询项集合接口
     * @return
     */
    @PostMapping("/getDutyChooseList")
    @ResponseBody
    public AjaxResult getDutyTypeInfoChooseList(){

        return iDutyInfoService.getDutyTypeInfoChooseList();
    }

    /**
     * 获取值班的excel 接口
     * @param map
     *      dutyDate 月份时间
     *      typeNo   值班大类型（全部、白班、夜班、领导）
     * @return
     */
    @PostMapping("/getDutyCalendarExcel")
    @ResponseBody
    public AjaxResult getDutyCalendarExcel(@RequestBody Map<String, String> map){

        if(StringUtils.isEmpty(map.get("dutyDate")) ){
            return AjaxResult.error("月份为空");
        }
        if(StringUtils.isEmpty(map.get("typeNo"))){
            return AjaxResult.error("值班类型为空");
        }

        return iDutyInfoService.getDutyCalendarExcel(map.get("dutyDate"),map.get("typeNo"));

    }

    /**
     * 获取当前时间的前面12个月的日期结果集
     * @return
     */
    @PostMapping("/getLatest12Month")
    @ResponseBody
    public AjaxResult getLatest12Month(){
        LocalDate today = LocalDate.now();
        Map<String,Object> map =  new HashMap<String,Object>();

        String thisMonth = today.toString().substring(0,7);
        List<String> timeList = new ArrayList<>();
        timeList.add(thisMonth);
        for(long i = 1L;i < 12L; i++){
            LocalDate localDate = today.minusMonths(i);
            String ss = localDate.toString().substring(0,7);
            timeList.add(ss);
        }
        map.put(thisMonth,timeList);

        return AjaxResult.success(map);
    }

}
