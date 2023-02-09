package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.PubAttachmentMapper;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *附件服务层实现类
 * @author 14735
 */
@Service
public class PubAttachmentServiceImpl implements IPubAttachmentService {

    @Autowired
    private PubAttachmentMapper pubAttachmentMapper;
    @Value("${pagehelper.helperDialect}")
    private String dbType;


    @Override
    public List<Attachment> selectAttachmentList(Attachment attachment){
        if("mysql".equals(dbType)){
            return pubAttachmentMapper.selectAttachmentListMysql(attachment);
        }else{
            return pubAttachmentMapper.selectAttachmentList(attachment);
        }
    }

    @Override
    public Attachment selectAttachmentById(String id){
        if("mysql".equals(dbType)){
            return pubAttachmentMapper.selectAttachmentByIdMysql(id);
        }else{
            return pubAttachmentMapper.selectAttachmentById(id);
        }
    }

    @Override
    public int insertAttachment(Attachment attachment){
        return pubAttachmentMapper.insertAttachment(attachment);
    }

    @Override
    public int updateAttachment(Attachment attachment){
        return pubAttachmentMapper.updateAttachment(attachment);
    }

    @Override
    public int deleteAttachmentByIds(String ids){
        return pubAttachmentMapper.deleteAttachmentByIds(Convert.toStrArray(ids));
    }

    @Override
    public void deleteAttachmentByOwnerIds(String ownerIds) {
        pubAttachmentMapper.deleteAttachmentByOwnerIds(Convert.toStrArray(ownerIds));
    }

    @Override
    public List<Attachment> selectAttachmentByVersionCancel() {
        if("mysql".equals(dbType)){
            return pubAttachmentMapper.selectAttachmentByVersionCancelMysql();
        }else{
            return pubAttachmentMapper.selectAttachmentByVersionCancel();
        }
    }

    @Override
    public void deleteAttachment(List<Attachment> attachments, boolean autoClean){
        if (!CollectionUtils.isEmpty(attachments)) {
            List<String> list = new ArrayList<>();
            for (Attachment att : attachments) {
                list.add(att.getAtId());
                if(StringUtils.isNotEmpty(att.getGroupName()) && StringUtils.isNotEmpty(att.getFilePath())){
                    FastDFSHelper.getInstance().deleteFile(att.getGroupName(), att.getFilePath());
                }
                // 定时任务清除更新表flag字段
                if(autoClean){
                    Attachment attachment = new Attachment();
                    attachment.setAtId(att.getAtId());
                    attachment.setFlag("autoClean");
                    updateAttachment(attachment);
                }
            }
            // 页面删除版本单直接删除附件信息
            if(!autoClean){
                this.deleteAttachmentByIds(String.join(",", list));
            }
        }
    }

    @Override
    public List<Attachment> selectAttachmentByOwnerIds(String ownerIds) {
        if("mysql".equals(dbType)){
           return pubAttachmentMapper.selectAttachmentByOwnerIdsMysql(Convert.toStrArray(ownerIds));
        }else{
           return pubAttachmentMapper.selectAttachmentByOwnerIds(Convert.toStrArray(ownerIds));
        }
    }
}
