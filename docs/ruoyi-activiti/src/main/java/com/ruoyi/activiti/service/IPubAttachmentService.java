package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.Attachment;

import java.util.List;

/**
 *
 * 附件服务层接口
 * @author 14735
 */
public interface IPubAttachmentService {
    /**
     * 查询附件信息列表
     * @param attachment
     * @return
     */
    public List<Attachment> selectAttachmentList(Attachment attachment);

    /**
     * 根据id查询附件信息
     * @param id
     * @return
     */
    public Attachment selectAttachmentById(String id);

    /**
     * 新增附件
     * @param attachment
     * @return
     */
    public int insertAttachment(Attachment attachment);

    /**
     * 修改附件
     * @param attachment
     * @return
     */
    public int updateAttachment(Attachment attachment);

    /**
     * 删除附件
     * @param ids
     * @return
     */
    public int deleteAttachmentByIds(String ids);

    /**
     * 根据ownerId删除附件
     * @param ownerIds
     */
    public void deleteAttachmentByOwnerIds(String ownerIds);

    /**
     * 删除已作废版本单的所有附件
     */
    public List<Attachment> selectAttachmentByVersionCancel();

    /**
     * 删除附件（从fast删除，如果是删除版本单直接删除附件，如果是定时任务清除修改表字段记录）
     * @param attachments
     * @param autoClean
     */
    public void deleteAttachment(List<Attachment> attachments, boolean autoClean);

    /**
     * 根据ownerId数组查询附件列表
     * @return
     */
    public List<Attachment> selectAttachmentByOwnerIds(String ownerIds);
}
