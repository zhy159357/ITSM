package com.ruoyi.form.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import org.springframework.stereotype.Service;

public interface ChmApiService {
    JSONObject createEvent(JSONObject request);
    JSONObject getUserListDetail(JSONObject jsonObject);

    JSONObject createHeshengOpenApi(String imCode, OgUser ogUser,String businessKey) throws Exception;

    JSONObject getprocessingProgress(String imCode) throws Exception;

    JSONObject receiveResult(JSONObject request);

    JSONObject whitelistVerification(JSONObject request);

    AjaxResult getprocessingProgressList(String imCode);
}
