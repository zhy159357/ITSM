package com.ruoyi.form.service.impl;

import com.ruoyi.form.domain.ChangeTaskOrg;
import com.ruoyi.form.domain.ChangeTaskScene;
import com.ruoyi.form.mapper.IChangeTaskOrgMapper;
import com.ruoyi.form.mapper.IChangeTaskSceneMapper;
import com.ruoyi.form.service.IChangeTaskSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ChangeTaskSceneServiceImpl implements IChangeTaskSceneService {
    @Autowired
    IChangeTaskSceneMapper changeTaskSceneMapper;
    @Autowired
    IChangeTaskOrgMapper changeTaskOrgMapper;
    private static Lock lock = new ReentrantLock();

    @Override
    public int insert(ChangeTaskScene changeTaskScene) {
        return changeTaskSceneMapper.insert(changeTaskScene);
    }

    @Override
    public int updateById(ChangeTaskScene changeTaskScene) {
        return changeTaskSceneMapper.updateById(changeTaskScene);
    }

    @Override
    public List<ChangeTaskScene> getListByOrgId(String orgId) {
        return changeTaskSceneMapper.getListByOrgId(orgId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void syncSceneOrgId() {
        if (lock.tryLock()) {
            syncMessage.set("同步开始");
            try {
                List<ChangeTaskScene> sceneList = changeTaskSceneMapper.getAll();
                sceneList.forEach(p -> {
                    String orgName = p.getOrgname();
                    ChangeTaskOrg changeTaskOrg = changeTaskOrgMapper.getByOrgName(orgName);
                    changeTaskSceneMapper.updateOrgIdByOrgName(changeTaskOrg.getOrgid(), orgName);
                });
            } finally {
                lock.unlock();
            }
        } else {
            syncMessage.set("正在同步...");

        }
    }

    @Override
    public ChangeTaskScene getChangeTaskSceneByCode(String code) {
        return changeTaskSceneMapper.getChangeTaskSceneByCode(code);
    }

    @Override
    public ChangeTaskScene getAutoChangeTaskSceneByCode(String code) {
        return changeTaskSceneMapper.getAutoChangeTaskSceneByCode(code);
    }

    @Override
    public List<ChangeTaskScene> getAutoAll() {
        return changeTaskSceneMapper.getAutoAll();
    }

    @Override
    public List<ChangeTaskScene> getAutoListByOrgId(String orgId) {
        return changeTaskSceneMapper.getAutoListByOrgId(orgId);
    }

    @Override
    public ChangeTaskScene getAutoChangeTaskScene(String orgId, String sceneName) {
        return changeTaskSceneMapper.getAutoChangeTaskScene(orgId,sceneName);
    }
}
