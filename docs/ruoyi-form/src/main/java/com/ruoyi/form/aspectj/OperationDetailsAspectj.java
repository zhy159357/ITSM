package com.ruoyi.form.aspectj;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.OperationDetails;
import com.ruoyi.form.service.IChangeDeptService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @ClassName
 * @Description TODO
 * @Author zzz
 * @Date
 */
@Aspect
@Component
@Slf4j
public class OperationDetailsAspectj extends Base {

    @Autowired
    TaskService taskService;
    @Autowired
    IChangeDeptService changeDeptService;

    @Pointcut("execution(public * com.ruoyi.form.service.impl.CustomerFormServiceImpl.getOperationDetails(..))")
    public void buttonPointCut() {
    }

    @Around("buttonPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] param = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder("增强方法的入参值：");
        Arrays.stream(param).forEach(a -> {
            stringBuilder.append(a + ",");
        });
        log.info("增强方法的入参值：" + stringBuilder.toString());
        Object proceed = joinPoint.proceed();
        log.info("目标方法执行完后得到返回值并进行相应对象转化：");
        AjaxResult result = (AjaxResult) proceed;
        if (param.length == 2) {
            String bizNo = param[0].toString();
            Page page = (Page) param[1];
            if (StringUtils.startsWith(bizNo, "CHM")) {
                //获取任务单的操作记录
                List<OperationDetails> taskRecordList = getAllOperationDetails(bizNo, page);
                Map<String, Object> map = (Map<String, Object>) result.get("data");
                List<OperationDetails> changeRecordList = (List<OperationDetails>) map.get("list");
                changeRecordList.addAll(taskRecordList);
                //list根据时间排序
                List<OperationDetails> resultList = changeRecordList.stream().sorted(Comparator.comparing(OperationDetails::getCreatedTime, Comparator.reverseOrder())
                                .thenComparing((a,b)->{
                                   String alength = a.getCreatedName();
                                   String blength = b.getCreatedName();
                                   return alength.length()-blength.length();
                                }).thenComparing((a,b)->{
                                    int alength = a.getBizNo().length();
                                    int blength = b.getBizNo().length();
                                    return blength-alength;
                                }))
                        .collect(Collectors.toList());
                map.put("list", resultList);
                List<Map<String, Object>> colLists = new ArrayList<>();
                Map<String, Object> colMap2 = new HashMap<>();
                Map<String, Object> colMap3 = new HashMap<>();
                Map<String, Object> colMap4 = new HashMap<>();
                Map<String, Object> colMap5 = new HashMap<>();
                colMap2.put("label", "表单编号");
                colMap2.put("val", "bizNo");
                colMap2.put("width", "190");
                colMap3.put("label", "内容");
                colMap3.put("val", "description");
                colMap3.put("width", "650");
                colMap4.put("label", "操作人员");
                colMap4.put("val", "createdName");
                colMap4.put("width", "140");
                colMap5.put("label", "操作时间");
                colMap5.put("val", "createdTime");
                colLists.add(colMap2);
                colLists.add(colMap3);
                colLists.add(colMap4);
                colLists.add(colMap5);
                map.put("col", colLists);
            }
        }
        System.out.println(result.toString());
        return result;
    }
}

