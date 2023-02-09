package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.SmBizFolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FolderMapper {


    /**
     *
     * @param folder
     * @return
     */
    List<SmBizFolder> selectFolderTree(SmBizFolder folder);


    /**
     *
     * @param id_
     * @return
     */
    SmBizFolder selectFolderTreeById(String id_);


    /**
     *
     * @param smBizFolder
     * @return
     */
    int insertTree(SmBizFolder smBizFolder);

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

//    int selectUserTreeById(SmBizFolder smBizFolder);

    /**
     *
     * @param id_
     * @return
     */
    int selectUpdLeafById(String id_);


    SmBizFolder selectFolderById(String id_);


    String selectParentName(String parent_);


    List<SmBizFolder> selectFolderList(SmBizFolder bizFolder);

}
