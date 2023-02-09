package com.ruoyi.form.service;

import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.form.entity.TwWorkOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
public interface ITwWorkOrderService extends IService<TwWorkOrder> {

    /**
     * 新增工单
     *
     * @param twWorkOrder 工单对象
     * @author chw
     */
    void addSave(TwWorkOrder twWorkOrder);

    /**
     * 查询twWorkOrder
     *
     * @param twWorkOrder
     * @return
     */
    List<TwWorkOrder> getTwWorkOrder(TwWorkOrder twWorkOrder);

    /**
     * 查询待办
     * @param twWorkOrder
     * @return
     */
    List<TwWorkOrder> backlogList(TwWorkOrder twWorkOrder);

    /**
     * 根据workOrderId 执行自动化脚本
     *
     * @param id
     * @return
     */
    Object execute(Integer id);

    /**
     *
     * @param state state=0 暂存 state=1 提交
     * @param twWorkOrder workOrder对象
     * @return
     */
    Object submit(Integer state,TwWorkOrder twWorkOrder);

    BmpEntity startProcess(String workNum, Map<String,Object> variables);

    /**
     * 审批
     * @param twWorkOrder
     */
    void auditPass(TwWorkOrder twWorkOrder);


    /**
     *
     * @param sys
     * @return
     */
    List<OgSys>  syslist(OgSys sys);

    List<TwWorkOrder> getTwWorkOrderAll(TwWorkOrder twWorkOrder);

    List<TwWorkOrder> getTwWorkMyOrder(TwWorkOrder twWorkOrder);

    TwWorkOrder selectTwWorkOrderById(String id);

    int toCancle(String id);
}
