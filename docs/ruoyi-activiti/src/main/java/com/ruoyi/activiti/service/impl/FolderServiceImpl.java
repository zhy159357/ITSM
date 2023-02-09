package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.FolderMapper;
import com.ruoyi.activiti.service.FolderService;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.domain.entity.SmBizFolder;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService{

    @Autowired
    private FolderMapper folderMapper;

    @Override
    public List<ZtreeStr> selectFolderTree(SmBizFolder folder) {

        List<SmBizFolder> folders = folderMapper.selectFolderTree(folder);
        return initZtreeStr(folders);
    }

    @Override
    public SmBizFolder selectFolderTreeById(String id_) {
        return folderMapper.selectFolderTreeById(id_);
    }

    @Override
    public int insertTree(SmBizFolder smBizFolder) {
        smBizFolder.setId_(UUID.getUUIDStr());
        String parent_ = smBizFolder.getParent_();
        if(!"-1".equals(parent_)){
            smBizFolder.setPath_(folderMapper.selectFolderById(parent_).getPath_());
        }else {
            smBizFolder.setParent_("-1");

        }
        return folderMapper.insertTree(smBizFolder);
    }

    @Override
    public int updateTree(SmBizFolder smBizFolder) {
        return folderMapper.updateTree(smBizFolder);
    }

    @Override
    public int deleteTree(String id_) {
        return folderMapper.deleteTree(id_);
    }

//    @Override
//    public int selectUserTreeById(SmBizFolder smBizFolder) {
//        return folderMapper.selectUserTreeById(smBizFolder);
//    }

    @Override
    public int selectUpdLeafById(String id_) {
        return folderMapper.selectUpdLeafById(id_);
    }

    @Override
    public String selectParentName(String parent_) {
        return folderMapper.selectParentName(parent_);
    }

    @Override
    public List<SmBizFolder> selectFolderList(SmBizFolder bizFolder) {
        return folderMapper.selectFolderList(bizFolder);
    }



    /**
     * 对象转参数树
     *
     * @param folderList 目录列表
     * @return 树结构列表
     */
    public List<ZtreeStr> initZtreeStr(List<SmBizFolder> folderList)
    {

        List<ZtreeStr> ztrees = new ArrayList<ZtreeStr>();
        for (SmBizFolder folder : folderList)
        {
                ZtreeStr ztreeStr = new ZtreeStr();
                ztreeStr.setId(folder.getId_());
                ztreeStr.setpId(folder.getParent_());
                ztreeStr.setName(folder.getName_());
                ztreeStr.setTitle(folder.getName_());
                ztrees.add(ztreeStr);
        }
        return ztrees;
    }


}
