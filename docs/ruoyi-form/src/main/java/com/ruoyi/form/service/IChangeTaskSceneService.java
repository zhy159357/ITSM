package com.ruoyi.form.service;

import com.ruoyi.form.domain.ChangeTaskScene;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface IChangeTaskSceneService {
    AtomicReference<String> syncMessage = new AtomicReference<>();
    int insert(ChangeTaskScene changeTaskScene);
    int updateById(ChangeTaskScene changeTaskScene);
    List<ChangeTaskScene> getListByOrgId(String orgId);
    void syncSceneOrgId();
    ChangeTaskScene getChangeTaskSceneByCode(String code);
    ChangeTaskScene getAutoChangeTaskSceneByCode(String code);
    List<ChangeTaskScene> getAutoAll();
    List<ChangeTaskScene> getAutoListByOrgId(String orgId);
    ChangeTaskScene getAutoChangeTaskScene(String orgId,String sceneName);
}
