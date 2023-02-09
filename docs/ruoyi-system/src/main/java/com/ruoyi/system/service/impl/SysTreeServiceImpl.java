package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.SysTreeMapper;
import com.ruoyi.system.service.ISysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上传下载业务层处理
 *
 * @author Mr.Xy
 */
@Service
public class SysTreeServiceImpl implements ISysTreeService {
    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Override
    public SysFolder selectTreeById(String id_) {
        return sysTreeMapper.selectTreeById(id_);
    }

    @Override
    @Transactional
    public String selectParentName(String par) {
        return sysTreeMapper.selectParentName(par);
    }

    @Override
    public int insertTree(SysFolder sysFolder) {
        sysFolder.setId_(UUID.getUUIDStr());
        String parent_ = sysFolder.getParent_();
        SysFolder pa= sysTreeMapper.selectTreeById(parent_);
        if(pa!=null){
            sysFolder.setPath_(pa.getPath_());
        }
        return sysTreeMapper.insertTree(sysFolder);
    }


    @Override
    @Transactional
    public int updateTree(SysFolder sysFolder) {
        return sysTreeMapper.updateTree(sysFolder);
    }

    @Override
    @Transactional
    public int deleteTree(String id_) {
        // 修改角色信息
        return sysTreeMapper.deleteTree(id_);
    }

    /**
     * 查询是否个人节点
     *
     * @param sysFolder
     * @return
     */
    public int selectSelfTreeById(SysFolder sysFolder) {
        return sysTreeMapper.selectSelfTreeById(sysFolder);
    }

    /**
     * 查询是否存在子节点
     *
     * @param id_
     * @return
     */
    public int selectTreeLeafById(String id_) {
        return sysTreeMapper.selectTreeLeafById(id_);
    }
}