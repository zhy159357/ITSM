package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.ApiResData;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.netty.vo.FileUploadResult;

/**
 * 跳板机文件下载接口
 *
 * @author 14735
 */
public interface IForwardFileService {

    /**
     * 根据文件密文获取文件信息
     *
     * @param fileCiphertext
     * @return
     */

    public FileUploadResult getFileUploadResult(String fileCiphertext);


    /**
     * 保存附件
     *
     * @param fileCiphertext
     * @param create
     * @param ownerId
     * @param fileGroup
     * @return
     */

    public Attachment saveForwardFile(String fileCiphertext, OgPerson create, String ownerId, String fileGroup);

    /**
     * 电话银行运维事件单接口专用
     *
     * @param ownerId  工单ID
     * @param userId  创建人ID
     * @param filePath 文件URL
     * @param resData  返回值
     * @return
     */
    public ApiResData saveFortFile(String ownerId, String userId, String filePath, ApiResData resData);
}

