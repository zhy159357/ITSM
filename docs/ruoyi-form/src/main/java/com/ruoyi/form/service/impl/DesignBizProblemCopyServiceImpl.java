package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.form.domain.DesignBizProblemCopy;
import com.ruoyi.form.mapper.DesignBizProblemCopyMapper;
import com.ruoyi.form.service.DesignBizProblemCopyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DesignBizProblemCopyServiceImpl extends ServiceImpl<DesignBizProblemCopyMapper, DesignBizProblemCopy> implements DesignBizProblemCopyService {
    @Resource
    private  DesignBizProblemCopyMapper designBizProblemCopyMapper;
    @Override
    public List<Map<String,String>> queryIds(List<String> noList) {
        return designBizProblemCopyMapper.queryIds(noList);
    }
}
