package com.ruoyi.web.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.quartz.task.EventEmailP2P4Task;
import com.ruoyi.system.service.IPubParaValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ShDuty;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.IShDutyService;


//值班
@Controller
@RequestMapping("/system/duty")
@Component("dutyShController")
public class DutyShController extends BaseController {

    private String prefix = "system/duty";

    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IShDutyService shDutyService;
    private static final Logger log = LoggerFactory.getLogger(DutyShController.class);
    /**
     * 转到前端页面
     * @return
     */
    @GetMapping()
    public String holiday()
    {
        return prefix + "/shduty";
    }

    /**
     * 值班列表展示
     * @param holiday
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ShDuty shDuty)
    {
        startPage();
        List<ShDuty> list = shDutyService.selectDutyList(shDuty);
        return getDataTable(list);
    }


    /**
     * 定时任务执行-清除历史值班信息
     */
    public void cleanHistorydutydata() {
    String cleanHistorydutydata = "clean_historydutydata";
        List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName(cleanHistorydutydata);
        String chddStr;
        if(!CollectionUtils.isEmpty(list)) {
             chddStr = list.get(0).getValue();
        }else{
            log.info("------清除历史数据标识 clean_historydutydata 未配置----");
            // 若是不配置, 默认删除7天的数据
             chddStr = "7";
        }

            if(null!=chddStr) {
                // 获取指定前的日期
                int chddInt=Integer.valueOf(chddStr);
             Date beforeDay = new Date(new Date().getTime()-chddInt*24*60*60*1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                 chddStr = sdf.format(beforeDay);
                try {
                    log.info("------------清除历史数据开始-----------------");
                    log.info("------------清除日期:"+chddStr+"------------");
                    shDutyService.deleteDutyBeforeDays(chddStr);
                    log.info("------------清除历史数据结束-----------------");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("------------清除日期:"+chddStr+"------------");
                    log.error("------------清除历史数据错误-----------------");
                }
            } else {
                log.info("------清除历史数据标识 cleanHistorydutydata 未配置----");
            }




    }

    /**
     * 删除值班
     * @param ids
     * @return
     */
    @Log(title = "值班管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(shDutyService.deleteDutyByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }


    /**
     * 转到新增值班页面
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }


    /**
     * 保存值班信息
     * @param pubHoliday
     * @return
     */

    @Log(title = "值班管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@Validated ShDuty shDuty)
    {
        try
        {
            return toAjax(shDutyService.insert(shDuty));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }


    /**
     * 转到修改值班页面
     */
    @GetMapping("/edit/{dutyId}")
    public String edit(@PathVariable("dutyId") String id, ModelMap mmap)
    {
    	ShDuty shDuty =new ShDuty();
    	shDuty.setId(id);
        mmap.put("shDuty", shDutyService.selectDutyByIdV(shDuty));
        return prefix + "/edit";
    }

    /**
     * 修改值班
     */
    @Log(title = "修改值班", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ShDuty shDuty)
    {
    	shDuty.setUserNo(ShiroUtils.getLoginName());
        return toAjax(shDutyService.edit(shDuty));

    }

    @RequestMapping("/sync")
    @ResponseBody
    public AjaxResult sync() {
        return toAjax(shDutyService.sync());
    }
    
    @RequestMapping("/queryDutyPerson")
    @ResponseBody
    public ShDuty queryDutyPerson() {
    	  SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd");
          String startTime=dateformat1.format(new Date());
        return shDutyService.queryDutyPerson(startTime,"test");
    }
    
}
