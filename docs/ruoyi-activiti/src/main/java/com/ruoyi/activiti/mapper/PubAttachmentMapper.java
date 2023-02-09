package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.Attachment;

import java.util.List;

/**
 * @author 14735
 */
public interface PubAttachmentMapper {
    /**
     * 查询附件信息列表
     *
     * @param attachment
     * @return
     */
    public List<Attachment> selectAttachmentList(Attachment attachment);
    public List<Attachment> selectAttachmentListMysql(Attachment attachment);

    /**
     * 根据id查询附件信息
     *
     * @param id
     * @return
     */
    public Attachment selectAttachmentById(String id);
    public Attachment selectAttachmentByIdMysql(String id);


    /**
     * 新增附件
     *
     * @param attachment
     * @return
     */
    public int insertAttachment(Attachment attachment);

    /**
     * 修改附件
     *
     * @param attachment
     * @return
     */
    public int updateAttachment(Attachment attachment);

    /**
     * 删除附件
     *
     * @param ids
     * @return
     */
    public int deleteAttachmentByIds(String[] ids);

    /**
     * 根据ownrid查询附件
     */
    public List<Attachment> selectAttachmentByOwnerIds(String[] ownerIds);

    public List<Attachment> selectAttachmentByOwnerIdsMysql(String[] ownerIds);

    /**
     * 根据ownerId删除附件
     * @param ownerIds
     */
    public int deleteAttachmentByOwnerIds(String[] ownerIds);

    /**
     * 删除已作废版本单的所有附件
     */
    public List<Attachment> selectAttachmentByVersionCancel();

    public List<Attachment> selectAttachmentByVersionCancelMysql();
}
