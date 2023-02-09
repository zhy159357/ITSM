package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.SmBizFolder;

import java.util.List;

public interface FolderService {

    /**
     *
     * @param folder
     * @return
     */
    List<ZtreeStr> selectFolderTree(SmBizFolder folder);

    /**
     *
     * @param id_
     * @return
     */
    public SmBizFolder selectFolderTreeById(String id_);

    /**
     *
     * @param smBizFolder
     * @return
     */
    public int insertTree(SmBizFolder smBizFolder);

    /**
     *
     * @param smBizFolder
     * @return
     */
    int updateTree(SmBizFolder smBizFolder);


    /**
     *
     * @param id_
     * @return
     */
    int deleteTree(String id_);


//    /**
//     *
//     * @param smBizFolder
//     * @return
//     */
//    int selectUserTreeById(SmBizFolder smBizFolder);

    /**
     * 查询是否有子节点
     * @param id_
     * @return
     */
    int selectUpdLeafById(String id_);

    /**
     *
     * @param parent_
     * @return
     */
    String selectParentName(String parent_);

    /**
     *
     * @param bizFolder
     * @return
     */
    List<SmBizFolder> selectFolderList(SmBizFolder bizFolder);



}
