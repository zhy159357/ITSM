package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.OgUserPost;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.OgUserPostMapper;
import com.ruoyi.system.service.IOgUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.AmBizNoticeMapper;
import com.ruoyi.activiti.domain.AmBizNotice;
import com.ruoyi.activiti.service.IAmBizNoticeService;
import com.ruoyi.common.core.text.Convert;

/**
 * 【公告通知】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Service
public class AmBizNoticeServiceImpl implements IAmBizNoticeService
{
    @Autowired
    private AmBizNoticeMapper amBizNoticeMapper;

    @Autowired
    private IOgUserPostService ogUserPostService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询【公告通知】
     *
     * @param amBizId 【公告通知】ID
     * @return 【公告通知】
     */
    @Override
    public AmBizNotice selectAmBizNoticeById(String amBizId)
    {
        return amBizNoticeMapper.selectAmBizNoticeById(amBizId);
    }

    /**
     * 查询【公告通知】列表
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】
     */
    @Override
    public List<AmBizNotice> selectAmBizNoticeList(AmBizNotice amBizNotice)
    {
        if("mysql".equals(dbType)){
            return amBizNoticeMapper.selectAmBizNoticeListMysql(amBizNotice);
        }else{
            return amBizNoticeMapper.selectAmBizNoticeList(amBizNotice);
        }
    }

    /**
     * 查询待审核公告通知
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> selectCheckNoticeList(AmBizNotice amBizNotice){
        amBizNotice.setCheckerId(ShiroUtils.getUserId());
        amBizNotice.setCurrentStatus("02");
        if("mysql".equals(dbType)){
            return amBizNoticeMapper.selectAmBizNoticeListMysql(amBizNotice);
        }else{
            return amBizNoticeMapper.selectAmBizNoticeList(amBizNotice);
        }
    }

    /**
     * 查询所有公告通知
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】集合
     */
    @Override
    public List<AmBizNotice> selectAllNoticeList(AmBizNotice amBizNotice)
    {
        if("mysql".equals(dbType)){
            return amBizNoticeMapper.selectAmBizNoticeListMysql(amBizNotice);
        }else{
            return amBizNoticeMapper.selectAmBizNoticeList(amBizNotice);
        }

    }

    /**
     * 新增【公告通知】
     *
     * @param amBizNotice 【公告通知】
     * @return 结果
     */
    @Override
    public int insertAmBizNotice(AmBizNotice amBizNotice)
    {
        return amBizNoticeMapper.insertAmBizNotice(amBizNotice);
    }

    /**
     * 修改【公告通知】
     *
     * @param amBizNotice 【公告通知】
     * @return 结果
     */
    @Override
    public int updateAmBizNotice(AmBizNotice amBizNotice)
    {
        return amBizNoticeMapper.updateAmBizNotice(amBizNotice);
    }

    /**
     * 删除【公告通知】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAmBizNoticeByIds(String ids)
    {
        return amBizNoticeMapper.deleteAmBizNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【公告通知】信息
     *
     * @param amBizId 【公告通知】ID
     * @return 结果
     */
    @Override
    public int deleteAmBizNoticeById(String amBizId)
    {
        return amBizNoticeMapper.deleteAmBizNoticeById(amBizId);
    }

    /**
     * 查询我的【公告通知】列表  移动运维
     *
     * @param userId 登录人ID
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> getNoticeListForApp(String userId , String amTitle){
        AmBizNotice amBizNotice = new AmBizNotice();
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(userId);
        String sendRanges = "3";//1 数据中心 2 厂商+数据中心（72号院） 3 所有用户
        List<OgUserPost> postslist = ogUserPostService.selectPostByUserId(ogUserPost);
        for (OgUserPost ogUserPost1 : postslist) {
            if ("0010".equals(ogUserPost1.getPostId()) || "0011".equals(ogUserPost1.getPostId()) ||"0012".equals(ogUserPost1.getPostId())) {
                sendRanges = "1,2,3";
                break;
            }
            if ("0002".equals(ogUserPost1.getPostId())) {
                sendRanges = "2,3";
            }
        }
        if (StringUtils.isNotEmpty(amTitle)) {
            amBizNotice.setAmTitle(amTitle);
        }
        amBizNotice.setCurrentStatus("04");
        amBizNotice.setSendRanges(sendRanges.split(","));
        List<AmBizNotice> list = null;
        if("mysql".equals(dbType)){
            list =  amBizNoticeMapper.selectAmBizNoticeList(amBizNotice);
        }else{
            list =  amBizNoticeMapper.selectAmBizNoticeList(amBizNotice);
        }
        return list;
    }
}
