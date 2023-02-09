package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysPost;

import java.util.List;
import java.util.Map;

/**
 * 岗位信息 数据层
 * 
 * @author ruoyi
 */
public interface OgPostMapper
{


    /**
     *
     * @param post
     * @return
     */
    List<OgPost> selectPostList(OgPost post);


    /**
     *
     * @param postId
     * @return
     */
    OgPost selectPostById(String postId);


    /**
     * 批量删除岗位信息
     *
     *
     * @return 结果
     */
    int deletePostByIds(String[] ids);


    /**
     * 校验岗位名称
     *
     * @param postName 岗位名称
     * @return 结果
     */
    OgPost checkPostNameUnique(String postName);



    OgPost checkPostCodeUnique(String postId);


    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int insertPost(OgPost post);

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int updatePost(OgPost post);

    int deleteRolePost(String[] ridss,String postId);


    List<OgPost> selectAllPostByUserId(Map<String,String> map);

    List<OgPost> selectLoginUserOgPosts(String userId);

    List<OgPost> selectPostRoleById(String id);

    List<OgPost> selectPostUserById(String id);


    List<OgPost> selectBatchPostList(Map<String,Object> map);



}
