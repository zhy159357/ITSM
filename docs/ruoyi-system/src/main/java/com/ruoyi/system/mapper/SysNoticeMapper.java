package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysNotice;

/**
 * 公告 数据层
 *
 * @author ruoyi
 */
public interface SysNoticeMapper
{
    /**
     * 查询公告信息
     *
     * @param ambizid 公告ID
     * @return 公告信息
     */
    public SysNotice selectNoticeById(String ambizid);

    /**
     * 查询公告列表
     *
     * @param sysNotice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice sysNotice);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int updateNotice(SysNotice notice);

    /**
     * 审核修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public int checkNotice(SysNotice notice);

    /**
     * 批量删除公告
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNoticeByIds(String[] ids);

    /**
     * 获取公告编码
     *
     * @return 结果
     */
    public SysNotice selectNoticeMaxAmCode();

    public SysNotice selectNoticeMaxAmCodeMysql();

    /**
     * 查询审核公告列表
     *
     * @param sysNotice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeCheckList(SysNotice sysNotice);

    /**
     * 公告通知岗位下拉
     *
     * @param sysNotice 公告实体穿rid 与 postNam
     * @return 结果
     */
    public List<OgRole> selectReceptionPostList(SysNotice sysNotice);

    /**
     * 公告通知接收工作组
     */
    public List<OgGroup> selectNoticeReceiveGroup(OgGroup ogGroup);

    /**
     * 公告通知接收机构
     */
    public List<OgOrg> selectNoticeReceiveDeptList(OgOrg ogOrg);

    /**
     * 查询接收公告人员列表/机构
     *
     * @param sysNotice 将接收机构orgID 接收岗位 postId 封装到Sysnotice属性中
     * @return 查询接收公告人员列表
     */
    public List<OgPerson> selectReceivePersonDept(SysNotice sysNotice);

    /**
     * 查询接收公告人员列表/工作组
     *
     * @param sysNotice 将工作组groupId 接收岗位 postId 封装到Sysnotice属性中
     * @return 查询接收公告人员列表
     */
    public List<OgPerson> selectReceivePersonGroup(SysNotice sysNotice);

    /**
     * 查询接收公告人员列表
     *
     * @param userId 同构userId查询该岗位
     * @return 岗位集合
     */
    public List<OgPost> selectPostForByUserId(String userId);

    /**
     * 查询省分行 信息技术局 单列市
     *
     * @param ogOrg 机构查询
     * @return 机构集合
     */
    public List<OgOrg> DeptTreeBankch(OgOrg ogOrg);

    /**
     * 查询公告通知列表
     *
     * @param sysNotice
     * @return
     */
    public List<SysNotice> selectPageNoticeList(SysNotice sysNotice);

    /**
     * 查询省分行 信息技术局 单列市
     *
     * @param ogOrg 机构查询
     * @return 机构集合
     */
    public List<OgOrg> selectDeptByOrgCodeRightLike(OgOrg ogOrg);

}