package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.OgRolePost;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysRolePost;
import com.ruoyi.system.mapper.OgPostMapper;
import com.ruoyi.system.mapper.SysRolePostMapper;
import com.ruoyi.system.service.OgPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OgPostServiceImpl implements OgPostService {


    @Autowired
    private OgPostMapper ogPostMapper;

    @Autowired
    private SysRolePostMapper rolePostMapper;

    /**
     * 列表信息
     * @param post
     * @return
     */
    @Override
    public List<OgPost> selectPostList(OgPost post)
    {
        return ogPostMapper.selectPostList(post);
    }

    /**
     *删除数据
     * @param id
     * @return
     */
    @Override
    public int deletePostByIds(String id) {
        String[] ids = id.split(",");
        return ogPostMapper.deletePostByIds(ids);
    }


    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(OgPost post)
    {
        String postId = StringUtils.isNull(post.getPostId()) ? "-1L" : post.getPostId();
        OgPost info = ogPostMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().equals(postId))
        {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }


    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(OgPost post)
    {
        String postId = StringUtils.isNull(post.getPostId()) ? "-1L" : post.getPostId();
        OgPost info = ogPostMapper.checkPostCodeUnique(post.getPostId());
        if (StringUtils.isNotNull(info) && info.getPostId().equals(postId))
        {
            return UserConstants.POST_IDPOST_NOT_UNIQUE;
        }
        return UserConstants.POST_IDPOST_UNIQUE;
    }


    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(OgPost post) {
        post.setAddTime(DateUtils.dateTimeNow());
        return ogPostMapper.insertPost(post);
    }





    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public OgPost selectPostById(String postId) {

        return ogPostMapper.selectPostById(postId);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(OgPost post) {

        return ogPostMapper.updatePost(post);
    }




    /**
     *
     * @param postId 岗位id
     * @param ids
     * @return
     */
    @Override
    public int insertAuthRoles(String postId, String ids) {
        String[] rids = Convert.toStrArray(",",ids);
        int rows = 0;
        for (String rid : rids)
        {
            OgRolePost rp = new OgRolePost();
            rp.setRid(rid);
            rp.setPostId(postId);
            rows += rolePostMapper.insertAuthRoles(rp);
        }

        return rows;
    }

    @Override
    public int deleteRolePost(String rids,String postId) {
        String[] ridss = Convert.toStrArray(rids);

        return ogPostMapper.deleteRolePost(ridss,postId);
    }

    @Override
    public List<OgPost> selectAllPostByUserId(String userId,String postName) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId",userId);
        map.put("postName",postName);
        return ogPostMapper.selectAllPostByUserId(map);
    }

    @Override
    public List<OgPost> selectLoginUserOgPosts(String userId) {
        return ogPostMapper.selectLoginUserOgPosts(userId);
    }



    /**
     * 查询当前岗位下的角色
     * @param id
     * @return
     */
    @Override
    public List<OgPost> selectPostRoleById(String id) {
        return ogPostMapper.selectPostRoleById(id);
    }

    /**
     * 查询当前岗位下的账号
     * @param id
     * @return
     */
    @Override
    public List<OgPost> selectPostUserById(String id) {
        return ogPostMapper.selectPostUserById(id);
    }

    @Override
    public List<OgPost> selectBatchPostList(List<String> postIds, String postName, Boolean centerFlag) {
        Map<String,Object> map = new HashMap<>();
        map.put("postIds",postIds);
        map.put("postName",postName);
        map.put("centerFlag",centerFlag);
        return ogPostMapper.selectBatchPostList(map);
    }


    //    /**
//     * 通过岗位ID查询岗位使用数量
//     *
//     * @param postId 岗位ID
//     * @return 结果
//     */
//    @Override
//    private boolean countUserPostById(String postId) {
//
//        return userOgpostMapper.countUserPostById(postId);
//    }


}
