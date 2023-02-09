package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgUserPost;

import java.util.List;
import java.util.Map;

public interface OgUserPostMapper {
    int batchAdd(List<OgUserPost> ogUserPosts);

    int batchAddMysql(List<OgUserPost> ogUserPosts);

    int deletePostByUserId(String userId);

    int deletePostsByUserId(Map map);

    List<OgUserPost> selectPostByUserId(OgUserPost ogUserPost);

    List<OgUserPost> selectPostByUserIdTwo(String userId);


}
