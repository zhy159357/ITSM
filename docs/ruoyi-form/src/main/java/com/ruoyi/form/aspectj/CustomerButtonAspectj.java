package com.ruoyi.form.aspectj;

import com.ruoyi.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerButtonAspectj
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/7/27 11:36
 */
@Aspect
@Component
@Slf4j
public class CustomerButtonAspectj {

    @Pointcut("@annotation(com.ruoyi.common.annotation.CustomerButton)")
    public void buttonPointCut(){
    };

    @Around("buttonPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] param = joinPoint.getArgs();
        StringBuilder stringBuilder=new StringBuilder("增强方法的入参值：");
        Arrays.stream(param).forEach(a->{
            stringBuilder.append(a+",");
        });
        log.info("增强方法的入参值："+stringBuilder.toString());

        //继续执行方法
        Object proceed = joinPoint.proceed();
        AjaxResult result = (AjaxResult) proceed;
        Map<String, Object> map = (Map<String, Object>) result.get("data");
        List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("buttonInfo");
        List<Map<String, Object>> jsonDataList = (List<Map<String, Object>>) map.get("jsonData");
        log.info("目标方法执行完成，返回参数信息 buttonInfoList:{},jsonDataList:{}", buttonInfoList, jsonDataList);
        //TODO  这里写你自己的业务逻辑  需要什么在返回对象里面增加   方法建议抽出来 在本类下写 便于后期维护
        return result;
    }



}
