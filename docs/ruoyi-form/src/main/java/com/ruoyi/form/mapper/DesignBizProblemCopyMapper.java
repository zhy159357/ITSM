package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.DesignBizProblemCopy;

import java.util.List;
import java.util.Map;

public interface DesignBizProblemCopyMapper extends BaseMapper<DesignBizProblemCopy> {
    List<Map<String,String>> queryIds(List<String> noList);
}