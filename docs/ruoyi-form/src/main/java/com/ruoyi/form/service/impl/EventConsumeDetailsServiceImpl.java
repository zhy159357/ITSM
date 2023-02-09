package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.EventConsumeDetails;
import com.ruoyi.form.domain.OperationDetails;
import com.ruoyi.form.mapper.EventConsumeDetailsMapper;
import com.ruoyi.form.service.EventConsumeDetailsService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventConsumeDetailsServiceImpl extends ServiceImpl<EventConsumeDetailsMapper, EventConsumeDetails> implements EventConsumeDetailsService {

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private EventConsumeDetailsMapper eventConsumeDetailsMapper;

    public void saveEventConsumeDetails(String bizNo, String currentOperatorId, String currentProcessId, String currentNodeName, String nextNodeName, String startTime) {
        if("开始".equals(currentNodeName)) {
            // 开始插入两条数据
            Map<String, String> currentOperator = convertPersonOrg(currentOperatorId);

            EventConsumeDetails details = EventConsumeDetails.builder().bizNo(bizNo).dealPerson(currentOperatorId).
                    dealPersonName(currentOperator.get("pName")).dealOrg(currentOperator.get("dealOrg")).
                    dealOrgName(currentOperator.get("dealOrgName")).currentNodeName(currentNodeName).
                    nextNodeName(nextNodeName).startTime(startTime).endTime(startTime).consumeType(EventConsumeDetails.CONSUME_TYPE_0).build();
            baseMapper.insert(details);

            Map<String, String> currentProcess = convertPersonOrg(currentProcessId);

            EventConsumeDetails details1 = EventConsumeDetails.builder().bizNo(bizNo).dealPerson(currentProcessId).
                    dealPersonName(currentProcess.get("pName")).dealOrg(currentProcess.get("dealOrg")).
                    dealOrgName(currentProcess.get("dealOrgName")).currentNodeName(currentNodeName).
                    nextNodeName(nextNodeName).startTime(startTime).consumeType(EventConsumeDetails.CONSUME_TYPE_1).build();
            baseMapper.insert(details1);
        } else if(StringUtils.isNotEmpty(currentProcessId)) {
            updateConsumeDetailsLastOneByNo(startTime, bizNo);

            Map<String, String> currentProcess = convertPersonOrg(currentProcessId);

            EventConsumeDetails details1 = EventConsumeDetails.builder().bizNo(bizNo).dealPerson(currentProcessId).
                    dealPersonName(currentProcess.get("pName")).dealOrg(currentProcess.get("dealOrg")).
                    dealOrgName(currentProcess.get("dealOrgName")).currentNodeName(currentNodeName).
                    nextNodeName(nextNodeName).startTime(startTime).build();
            if(StringUtils.isNotEmpty(currentNodeName) && currentNodeName.contains("接单")) {
                // 如果当前节点名称包含"接单"，则该条数据是接单响应时间
                details1.setConsumeType(EventConsumeDetails.CONSUME_TYPE_2);
            } else if(StringUtils.isNotEmpty(nextNodeName) && nextNodeName.contains("接单")) {
                // 如果下一节点名称包含"接单"，则该条数据是接单处理时间
                details1.setConsumeType(EventConsumeDetails.CONSUME_TYPE_1);
            } else {
                // 如果都不符合说明该条数据是解决关闭时间
                details1.setConsumeType(EventConsumeDetails.CONSUME_TYPE_3);
            }
            baseMapper.insert(details1);
        } else {
            updateConsumeDetailsLastOneByNo(startTime, bizNo);
        }
    }

    /**
     * 翻译人员和机构名称
     * @param userId
     * @return
     */
    private Map<String, String> convertPersonOrg(String userId) {
        Map<String, String> map = new HashMap<>();
        String pName = "";
        String dealOrg = "";
        String dealOrgName = "";
        if(StringUtils.isNotEmpty(userId)) {
            OgPerson person = ogPersonService.selectOgPersonById(userId);
            if(StringUtils.isNotEmpty(person)) {
                pName = person.getpName();
                OgOrg ogOrg = deptService.selectDeptById(person.getOrgId());
                if(ogOrg != null) {
                    dealOrg = ogOrg.getOrgId();
                    dealOrgName = ogOrg.getOrgName();
                }
            }
        }
        map.put("pName", pName);
        map.put("dealOrg", dealOrg);
        map.put("dealOrgName", dealOrgName);
        return map;
    }

    /**
     * 更新最后一条耗时记录的时间
     * @param time
     * @param bizNo
     */
    private void updateConsumeDetailsLastOneByNo(String time, String bizNo) {
        EventConsumeDetails eventConsumeDetails = eventConsumeDetailsMapper.selectEventConsumeDetailsOneByBizNo(bizNo);
        if(eventConsumeDetails != null) {
            // 如果不为空，更新最近一条记录的end_time值
            EventConsumeDetails updateEntity = new EventConsumeDetails();
            updateEntity.setEndTime(time);
            updateEntity.setId(eventConsumeDetails.getId());
            baseMapper.updateById(updateEntity);
        }
    }

    @Override
    public AjaxResult selectEventConsumeDetailsByNo(String bizNo, Page page) {
        Page page1 = this.page(page, Wrappers.<EventConsumeDetails>query().eq("biz_no", bizNo).orderByDesc("id"));
        List<EventConsumeDetails> records = page1.getRecords();
        if(!CollectionUtils.isEmpty(records)) {
            records.stream().map(detail -> {
                if (StringUtils.isNotEmpty(detail.getEndTime()) && StringUtils.isNotEmpty(detail.getStartTime())) {
                    Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, detail.getEndTime());
                    Date startTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, detail.getStartTime());
                    Map<String, Long> dateDiff = DateUtils.getDateDiff(endTime, startTime);
                    detail.setDays(dateDiff.get("day"));
                    detail.setHours(dateDiff.get("hour"));
                    detail.setMinutes(dateDiff.get("min"));
                    detail.setSeconds(dateDiff.get("second"));
                }
                return detail;
            }).collect(Collectors.toList());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", page1.getTotal());
        List<Map<String, Object>> colLists = new ArrayList<>();

        Map<String, Object> colMap1 = new HashMap<>();
        Map<String, Object> colMap2 = new HashMap<>();
        Map<String, Object> colMap3 = new HashMap<>();
        Map<String, Object> colMap4 = new HashMap<>();
        Map<String, Object> colMap5 = new HashMap<>();
        Map<String, Object> colMap6 = new HashMap<>();
        Map<String, Object> colMap7 = new HashMap<>();
        Map<String, Object> colMap8 = new HashMap<>();
        colMap1.put("label", "部门");
        colMap1.put("val", "dealOrgName");
        colMap2.put("label", "处理人");
        colMap2.put("val", "dealPersonName");
        colMap3.put("label", "开始处理时间");
        colMap3.put("val", "startTime");
        colMap4.put("label", "结束处理时间");
        colMap4.put("val", "endTime");
        colMap5.put("label", "天");
        colMap5.put("val", "days");
        colMap6.put("label", "时");
        colMap6.put("val", "hours");
        colMap7.put("label", "分");
        colMap7.put("val", "minutes");
        colMap8.put("label", "秒");
        colMap8.put("val", "seconds");
        colLists.add(colMap1);
        colLists.add(colMap2);
        colLists.add(colMap3);
        colLists.add(colMap4);
        colLists.add(colMap5);
        colLists.add(colMap6);
        colLists.add(colMap7);
        colLists.add(colMap8);
        resultMap.put("col", colLists);
        resultMap.put("pageNum", page.getCurrent());
        resultMap.put("pageSize", page.getSize());
        resultMap.put("list", page1.getRecords());

        return AjaxResult.success(resultMap);
    }
}
