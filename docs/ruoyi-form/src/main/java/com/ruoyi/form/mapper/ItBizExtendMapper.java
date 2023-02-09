package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.form.domain.ItBizExtend;

import java.util.List;

public interface ItBizExtendMapper extends BaseMapper<ItBizExtend> {

    List<OgGroup> selectITserviceAllGroups();
}
