package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.service.IAmBizParaService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.AmBizPara;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/noticeCheck")
public class SysNoticeCheckController extends BaseController {

    private String prefix = "system/notice";

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @GetMapping()
    public String checkNotice()
    {
        return prefix + "/noticeCheck";
    }

    /**
     * 查询公告列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeCheckList(notice);
        for (int i = 0 ; i < list.size() ; i ++) {
            String addTime = list.get(i).getAddTime();
            list.get(i).setAddTime(handleTimeYYYY_MM_DD_HH_MM_SS(addTime));
        }
        list = list.stream().sorted(Comparator.comparing(SysNotice::getAddTime).reversed()).collect(Collectors.toList());
        return getDataTable_ideal(list);
    }

    /**
     * 公告详情
     */
    @GetMapping("/edit/{amBizId}")
    public String edit(@PathVariable("amBizId") String amBizId, ModelMap mmap)
    {
        mmap.put("sendRangeList", getSengRangeList());
        mmap.put("notice", noticeService.selectNoticeById(amBizId));
        return prefix + "/noticeCheckDetails";
    }

    /**
     * 审核修改保存公告
     */
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotice notice)
    {
        return toAjax(noticeService.updateNotice(notice));
    }

    //获取发送范围下拉框
    private List<Map<String, String>> getSengRangeList() {

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        return reList;
    }

}
