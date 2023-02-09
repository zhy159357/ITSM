package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.SysUpdMapper;
import com.ruoyi.system.service.ISysUpdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 上传下载业务层处理
 *
 * @author Mr.Xy
 */
@Service
public class SysUpdServiceImpl implements ISysUpdService {
    @Autowired
    private SysUpdMapper sysUpdMapper;

    @Override
    public SysPubFolder selectUpdById(String id_) {
        return sysUpdMapper.selectUpdById(id_);
    }

    @Override
    public String selectOrgById(String orgId) {
        return sysUpdMapper.selectOrgById(orgId);
    }

    @Override
    public int insertTree(SysPubFolder sysPubFolder) {
        sysPubFolder.setId_(UUID.getUUIDStr());
        String parent_ = sysPubFolder.getParent_();
        SysPubFolder pa= sysUpdMapper.selectUpdById(parent_);
        if(pa!=null){
//            if(pa.getPath_()!=null){
                sysPubFolder.setPath_(pa.getPath_());
//            }
        }
        return sysUpdMapper.insertTree(sysPubFolder);
    }


    @Override
    @Transactional
    public int updateUpd(SysPubFolder sysPubFolder) {
        return sysUpdMapper.updateUpd(sysPubFolder);
    }

    @Override
    @Transactional
    public int selectCountById(String id_) {
        // 修改角色信息
        return sysUpdMapper.selectCountById(id_);
    }
    @Override
    @Transactional
    public String selectParent(String selecttreeId) {
        // 修改角色信息
        return sysUpdMapper.selectParent(selecttreeId);
    }
    @Override
    @Transactional
    public String selectParentName(String par) {
        return sysUpdMapper.selectParentName(par);
    }
    @Override
    @Transactional
    public int deleteUpd(String id_) {
        // 修改角色信息
        return sysUpdMapper.deleteUpd(id_);
    }

    /**
     * 查询是否个人节点
     *
     * @param sysPubFolder
     * @return
     */
    public int selectSelfTreeById(SysPubFolder sysPubFolder) {
        return sysUpdMapper.selectSelfTreeById(sysPubFolder);
    }

    /**
     * 查询是否存在子节点
     *
     * @param id_
     * @return
     */
    public int selectUpdLeafById(String id_) {
        return sysUpdMapper.selectUpdLeafById(id_);
    }

    @Override
    public List<SysUpdown> selectUpdownById(SysUpdown updown) {
        return sysUpdMapper.selectUpdownById(updown);
    }

}