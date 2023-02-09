package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.EventConsumeDetails;

import java.util.List;

public interface EventConsumeDetailsService extends IService<EventConsumeDetails> {

    /**
     *
     * @param bizNo               单号
     * @param currentOperatorId   当前操作人
     * @param currentOperatorId   当前流程处理人
     * @param currentNodeName     当前节点名称
     * @param nextNodeName        下一节点名称
     * @param startTime           开始时间
     */
    void saveEventConsumeDetails(String bizNo, String currentOperatorId, String currentProcessId, String currentNodeName, String nextNodeName, String startTime);

    AjaxResult selectEventConsumeDetailsByNo(String bizNo, Page page);
}
