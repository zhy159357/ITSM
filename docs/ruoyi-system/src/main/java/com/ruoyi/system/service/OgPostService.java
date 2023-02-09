package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.system.domain.OgPost;

import java.util.List;

public interface OgPostService {
    /**
     *列表信息
     * @param post
     * @return
     */
    List<OgPost> selectPostList(OgPost post);


    /***
     * 删除数据
     * @param id
     * @return
     */
    int deletePostByIds(String id);


    String checkPostNameUnique(OgPost post);


    String checkPostCodeUnique(OgPost post);


    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int insertPost(OgPost post);


    OgPost selectPostById(String postId);


    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int updatePost(OgPost post);


    /**
     *
     * @param postId
     * @param rids
     * @return
     */
    int insertAuthRoles(String postId, String rids);


    int deleteRolePost(String rids,String postId);


    List<OgPost> selectAllPostByUserId(String userId,String postName);

    /**
     * 根据userId查询当前人岗位，如果userId为空则查询当前登录人的岗位
     * @param userId
     * @return
     */
    List<OgPost> selectLoginUserOgPosts(String userId);

    /**
     * 查询当前岗位下的角色
     * @param id
     * @return
     */
    List<OgPost> selectPostRoleById(String id);
    /**
     * 查询当前岗位下的账号
     * @param id
     * @return
     */
    List<OgPost> selectPostUserById(String id);


    /**
     * 查询当前用户没有分配的岗位信息
     * @param postIds
     * @return
     */
    List<OgPost> selectBatchPostList(List<String> postIds,String postName,Boolean centerFlag);


}
