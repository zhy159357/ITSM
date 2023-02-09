package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.OgReviewChangingDateConfig;

public interface IOgReviewChangingDateConfigService extends IService<OgReviewChangingDateConfig> {
    Integer config(OgReviewChangingDateConfig config);
}

