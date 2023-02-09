package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;

public interface IPubAttachmentService {

    /**
     * 610004查询附件列表（公共）
     *
     * @param ownerId
     * @return
     */
    PagerRecords getPagerPubAttachmentsByOwner(Pager pager, String ownerId);
}
