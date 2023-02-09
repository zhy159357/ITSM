package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.system.domain.SysNotice;

/**
 * 公告 服务层
 *
 * @author ruoyi
 */
public interface ISysNoticeService
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
     * 查询接收iDs
     * @return 接收ID数组
     */
    public String[] receiveAmBizIds();

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
     * 删除公告信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNoticeByIds(String ids);

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
     * @param sysNotice 公告实体穿rid 与 postName
     * @return 结果
     */
    public List<OgRole> selectReceptionPostList(SysNotice sysNotice);

    /**
     * 公告通知接收工作组
     *@param ogGroup
     * @return 结果
     */
    public List<OgGroup> selectNoticeReceiveGroupList(OgGroup ogGroup);

    /**
     * 公告通知接收机构
     *@param ogOrg
     * @return 结果
     */
    public List<OgOrg> selectNoticeReceiveDeptList(OgOrg ogOrg);

    /**
     * 序列AmCode
     *
     * @return 结果
     */
    public SysNotice selectNoticeMaxAmCode();

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
     * 查询省分行 信息技术局 单列市
     *
     * @param ogOrg 机构查询
     * @return 机构集合
     */
    public List<OgOrg> DeptTreeBankch(OgOrg ogOrg);
}
