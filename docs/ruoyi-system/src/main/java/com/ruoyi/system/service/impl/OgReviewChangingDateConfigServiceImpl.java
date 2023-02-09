package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.OgReviewChangingDateConfig;
import com.ruoyi.system.mapper.OgReviewChangingDateConfigMapper;
import com.ruoyi.system.service.IOgReviewChangingDateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OgReviewChangingDateConfigServiceImpl extends ServiceImpl<OgReviewChangingDateConfigMapper, OgReviewChangingDateConfig> implements IOgReviewChangingDateConfigService {
    @Autowired
    private OgReviewChangingDateConfigMapper mapper;

    @Override
    public Integer config(OgReviewChangingDateConfig config) {
        OgReviewChangingDateConfig config1 = this.mapper.selectOne(null);
        if (config1 == null) {
            config1 = new OgReviewChangingDateConfig();
            config1.setAheadOfHours(config.getAheadOfHours());
            config1.setDayOfWeek(config.getDayOfWeek());
            return this.mapper.insert(config1);
        }
        config1.setAheadOfHours(config.getAheadOfHours());
        config1.setDayOfWeek(config.getDayOfWeek());
        return this.mapper.updateById(config1);
    }
}

