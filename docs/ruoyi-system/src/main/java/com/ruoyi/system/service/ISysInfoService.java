package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.core.domain.entity.SysInfo;

import java.util.List;

/**
 * 信息制度业务层
 * @author ye_xu
 */
public interface ISysInfoService
{
    /**
     * @param info
     * @return
     */
    public List<SysInfo> selectInfoList(SysInfo info);

    /**
     * 查询节点数据
     * @param info
     */
    public List<SysInfo> selectIFList(SysInfo info);

    /**
     * 查询所有
     */
    public List<SysInfo> selectInfoAll();

    /**
     * @param regime_info_id
     */
    public SysInfo selectInfoById(String regime_info_id);
    //查询废止状态
    public SysInfo selectState(String regime_info_id);

    /**
     *  查询树节点
     */
    public SysFolder selectTreeById(String folder);
    //查询树名称
    public String selectTreeNameById(String selecttreeId);

    /**
     * @param regime_info_id
     */
    public boolean deleteInfoById(String regime_info_id);

    /**
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteInfoByIds(String ids);

    /**
     * @param info 角色信息
     * @return 结果
     */
    public int insertInfo(SysInfo info);

    /**
     * 根据id、获取列表个数
     */
    public SysInfo selectInfoByNo(String regime_title);

    public int updateInfo(SysInfo info);


    public int deleteInfo(SysInfo info);

    /**
     * @param folder 信息
     * @return 所有信息
     */
    public List<Ztree> selectFolderTree(SysFolder folder);
    /**
     * @param info
     * @return
     */
    public String checkInfoNameUnique(SysInfo info);

    /**
     * 状态修改
     */
    public int changeStatus(SysInfo info);

    /**
     * @param info 角色信息
     */
    public void checkInfoAllowed(SysInfo info);

    public List<SysInfo> selectInfosByUserId(Long userId);

    /**
     * 根据条件查询用户列表
     */
    public List<OgPerson> selectCheckerList(OgPerson ogPerson);

    List<SysInfo> selectAuditList(SysInfo info);

}
