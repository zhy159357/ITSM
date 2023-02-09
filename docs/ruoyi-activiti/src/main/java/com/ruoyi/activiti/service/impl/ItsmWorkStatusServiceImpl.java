package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.activiti.domain.ItsmWorkStatus;
import com.ruoyi.activiti.mapper.ItsmWorkStatusMapper;
import com.ruoyi.activiti.service.ItsmWorkStatusService;
import com.ruoyi.common.core.domain.entity.OgUserPost;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.OgUserPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

import static com.ruoyi.common.utils.DateUtils.handleTimeYYYYMMDDHHMMSS;

/**
 * Service业务层处理
 *
 * @author ruoyi
 * @date 2021-06-25
 */
@Service
public class ItsmWorkStatusServiceImpl implements ItsmWorkStatusService
{
    @Autowired
    private ItsmWorkStatusMapper itsmWorkStatusMapper;

    @Autowired
    private OgUserPostMapper ogUserPostMapper;

    /**
     * 查询
     *
     * @param pid ID
     * @return
     */
    @Override
    public ItsmWorkStatus selectItsmWorkStatusByPid(String pid)
    {
        return itsmWorkStatusMapper.selectItsmWorkStatusByPid(pid);
    }

    /**
     * 查询列表
     *
     * @param itsmWorkStatus
     * @return
     */
    @Override
    public List<ItsmWorkStatus> selectItsmWorkStatusList(ItsmWorkStatus itsmWorkStatus)
    {
        return itsmWorkStatusMapper.selectItsmWorkStatusList(itsmWorkStatus);
    }

    /**
     * 新增
     *
     * @param itsmWorkStatus
     * @return 结果
     */
    @Override
    public int insertItsmWorkStatus(ItsmWorkStatus itsmWorkStatus)
    {
        if(StringUtils.isNotEmpty(itsmWorkStatus.getOperateTime())){
            itsmWorkStatus.setOperateTime(handleTimeYYYYMMDDHHMMSS(itsmWorkStatus.getOperateTime()));
        }
        return itsmWorkStatusMapper.insertItsmWorkStatus(itsmWorkStatus);
    }

    /**
     * 修改
     *
     * @param itsmWorkStatus
     * @return 结果
     */
    @Override
    public int updateItsmWorkStatus(ItsmWorkStatus itsmWorkStatus)
    {
        if(StringUtils.isNotEmpty(itsmWorkStatus.getOperateTime())){
            itsmWorkStatus.setOperateTime(handleTimeYYYYMMDDHHMMSS(itsmWorkStatus.getOperateTime()));
        }
        return itsmWorkStatusMapper.updateItsmWorkStatus(itsmWorkStatus);
    }

    /**
     * 删除对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteItsmWorkStatusByIds(String ids)
    {
        return itsmWorkStatusMapper.deleteItsmWorkStatusByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除信息
     *
     * @param workId ID
     * @return 结果
     */
    @Override
    public int deleteItsmWorkStatusById(String workId)
    {
        return itsmWorkStatusMapper.deleteItsmWorkStatusById(workId);
    }


    /**
     * 判断当前登录是否为数据中心或厂商人员
     */
    @Override
    public boolean isDataCenter(String userId){
        //根据登录人id查出他的岗位代码
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(userId);
        List<OgUserPost> ogUserPosts = ogUserPostMapper.selectPostByUserId(ogUserPost);
        List<String> list = new ArrayList<>();
        // 数据中心领导
        list.add("0012");
        // 数据中心处长
        list.add("0011");
        // 数据中心人员
        list.add("0010");
        // 厂商人员
        list.add("0002");
        //循环
        for(OgUserPost post : ogUserPosts){
            //判断查询出的岗位信息是否为数据中心
            if(list.contains(post.getPostId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItsmWorkStatus selectItsmWorkStatusNameById(String workId) {
        return itsmWorkStatusMapper.selectItsmWorkStatusNameById(workId);
    }


}
