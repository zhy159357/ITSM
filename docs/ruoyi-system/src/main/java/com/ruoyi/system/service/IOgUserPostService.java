package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgUserPost;

import java.util.List;
public interface IOgUserPostService {

    int batchAdd(List<OgUserPost> ogUserPost);


    int deletePostByUserId(String userId);

    int deletePostsByUserId(String userId, String ids);

    List<OgUserPost> selectPostByUserId(OgUserPost ogUserPost);

    List<OgUserPost> selectPostByUserIdTwo(String userId);



}
