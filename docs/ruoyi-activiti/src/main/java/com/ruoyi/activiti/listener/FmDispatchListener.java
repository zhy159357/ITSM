package com.ruoyi.activiti.listener;


import com.ruoyi.activiti.service.impl.FmDispatchServiceImpl;
import com.ruoyi.common.core.domain.entity.FmDd;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 调度事件单监听
 *
 * @author 14735
 */
@Service("FmDispatchListener")
@Transactional
public class FmDispatchListener implements JavaDelegate, ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(FmDispatchListener.class);

    @Override
    public void notify(DelegateExecution delegateExecution) {

    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

    }

    /**
     *  协同处理人处理完成之后改变状态
     *
     * @param execution
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleFmDispatch(DelegateExecution execution) {
        FmDispatchServiceImpl fmDispatchService= SpringUtils.getBean(FmDispatchServiceImpl.class);
        log.info("###############调度请求工作流监听开始################");
        DelegateExecution parent = execution.getParent();
        String businessId = parent.getProcessInstanceBusinessKey();
        Map<String, Object> variable = parent.getVariables();
        String reCode = (String) variable.get("reCode");

        //根据reCode判断
        if("0".equals(reCode)){
            //根据businessId查询出单子
            FmDd fmDd = fmDispatchService.selectFmddById(businessId);
            //状态变为已处理
            fmDd.setCurrentState("7");
            fmDispatchService.updateDispatch(fmDd);
        }


    }

}
