package com.ruoyi.activiti.listener;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.service.impl.CmBizQingqiuServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 变更请求单监听
 *
 * @author 14735
 */
@Service("CmBizQingQiuListener")
public class CmBizQingQiuListener implements JavaDelegate, ExecutionListener {

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
    public void updateStautsForCmQQ(DelegateExecution execution) {
        CmBizQingqiuServiceImpl cmBizQingqiuService = SpringUtils.getBean(CmBizQingqiuServiceImpl.class);
        log.info("###############变更请求工作流监听开始################");
        DelegateExecution parent = execution.getParent();
        String businessId = parent.getProcessInstanceBusinessKey();
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(businessId);
        //状态变为已受理
        cmBizQingqiu.setStauts("0500");
        cmBizQingqiu.setPracticleTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
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
}
