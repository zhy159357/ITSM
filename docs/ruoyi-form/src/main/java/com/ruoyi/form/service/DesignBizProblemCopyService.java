package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.domain.DesignBizProblemCopy;

import java.util.List;
import java.util.Map;

public interface DesignBizProblemCopyService extends IService<DesignBizProblemCopy>{

    List<Map<String,String>> queryIds(List<String> noList);
}
