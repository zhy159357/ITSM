package com.ruoyi.activiti.service.forward.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.service.forward.IPubAttachmentService;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跳板机接口查询附件
 *
 * @author 14735
 */
@Service("pubAttachmentManager")
public class PubAttachmentServiceImpl implements IPubAttachmentService {

    @Autowired
    private com.ruoyi.activiti.service.IPubAttachmentService attachmentService;

    @Override
    @EsbServiceMapping
    public PagerRecords getPagerPubAttachmentsByOwner(Pager pager, @ServiceParam(name = "ownerId") String ownerId) {
        if (StringUtils.isEmpty(ownerId)) {
            throw new BusinessException("100004", "所传参数不能为空");
        }
        Attachment attachment = new Attachment();
        attachment.setOwnerId(ownerId);
        Page<Attachment> attachmentPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        List<Attachment> attachments = attachmentService.selectAttachmentList(attachment);
        List<Map> maps = new ArrayList<>();
        for (Attachment att : attachments) {
            Record record = new Record();
            record.put("ownerId", att.getOwnerId());
            record.put("uploadTime", DateUtils.handleTimeYYYYMMDDHHMMSS(att.getFileTime()));
            record.put("uploadFileName", att.getFileName());
            if (StringUtils.isNotNull(att.getPerson())) {
                Map create = new HashMap<>();
                create.put("pname", att.getPerson().getpName());
                create.put("pid", att.getPerson().getpId());
                record.put("create", create);
            }
            record.put("memo", att.getMemo());
            record.put("size", att.getSize());
            record.put("fileType", att.getFileType());
            maps.add(record);
        }
        PagerRecords pagerRecords = new PagerRecords(maps, (int) attachmentPage.getTotal());
        pagerRecords.setPager(pager);
        return pagerRecords;
    }
}
