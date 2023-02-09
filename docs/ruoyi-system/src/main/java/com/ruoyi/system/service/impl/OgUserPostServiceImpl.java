package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgUserPost;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.mapper.OgUserPostMapper;
import com.ruoyi.system.service.IOgUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OgUserPostServiceImpl implements IOgUserPostService {

    @Autowired
    private OgUserPostMapper ogUserPostMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public int batchAdd(List<OgUserPost> ogUserPost) {
        if("mysql".equals(dbType)){
            return ogUserPostMapper.batchAddMysql(ogUserPost);
        }else{
            return ogUserPostMapper.batchAdd(ogUserPost);
        }

    }

    @Override
    public int deletePostByUserId(String userId) {
        return ogUserPostMapper.deletePostByUserId(userId);
    }

    @Override
    public int deletePostsByUserId(String userId, String idsStr) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("ids",Convert.toStrArray(idsStr));
        return ogUserPostMapper.deletePostsByUserId(map);
    }

    @Override
    public List<OgUserPost> selectPostByUserId(OgUserPost ogUserPost) {
        return ogUserPostMapper.selectPostByUserId(ogUserPost);
    }

    @Override
    public List<OgUserPost> selectPostByUserIdTwo(String userId) {
        return ogUserPostMapper.selectPostByUserIdTwo(userId);
    }


}
