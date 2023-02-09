package com.ruoyi.form.aspectj;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.ChangeFieldEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName
 * @Description TODO
 * @Author zzz
 * @Date
 */
@Aspect
@Component
@Slf4j
public class ProcessSubmitAspectj extends Base {


    @Pointcut("execution(public * com.ruoyi.form.service.impl.CustomerFormServiceImpl.processSubmit(..))")
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
        if (param.length == 4) {
            String code = param[0].toString();
            if (CHANGE.equals(code)) {
                String businessKey = param[1].toString();
                Map<String, Object> map = (Map<String, Object>) param[3];
                map.put(BUSINESS_KEY, businessKey);
            }else if (CHANGE_TASK.equals(code)) {
                String businessKey = param[1].toString();
                Map<String, Object> map = (Map<String, Object>) param[3];
                map.put(BUSINESS_KEY, businessKey);
                String changeId = getChangeIdByTaskId(businessKey);
                String status = getChangeColumnValueBySingleCondition(ChangeFieldEnum.STATUS, ChangeFieldEnum.ID, changeId);
                if (changeId == null||"".equals(changeId.trim())) {
                    return AjaxResult.warn("无所属变更单号！");
                }
                String instanceId = getChangeInstanceIdByChangeId(changeId);
                if (instanceId == null||"".equals(instanceId.trim())) {
                    return AjaxResult.warn("所属变更单未启动流程，请稍后提交");
                }
                if (!"待提交".equals(status)) {
                    return AjaxResult.warn("所属变更单现在不允许关联，请修改变更单号！");
                }
            }
        }
        //继续执行方法
        Object proceed = joinPoint.proceed();
        log.info("目标方法执行完后得到返回值并进行相应对象转化：");
        AjaxResult result = (AjaxResult) proceed;
        System.out.println(result.toString());
        return result;
    }


}

