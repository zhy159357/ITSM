package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.core.domain.entity.SysInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysInfoMapper;
import com.ruoyi.system.service.ISysInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 话机绑定业务层处理
 */
@Service
public class SysInfoServiceImpl implements ISysInfoService
{
    @Autowired
    private SysInfoMapper infoMapper;
    private final String SUCCESS = "1";// 启用状态
    private final String FAIL = "0";// 停用状态
    /**
     * 查询所有话机绑定
     * @return 话机绑定列表
     */
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public List<SysInfo> selectInfoAll()
    {
        return SpringUtils.getAopProxy(this).selectInfoList(new SysInfo());
    }


    @Override
    public SysInfo selectInfoById(String regime_info_id)
    {
        return infoMapper.selectInfoById(regime_info_id);
    }
    @Override
    public SysInfo selectState(String regime_info_id)
    {
        return infoMapper.selectState(regime_info_id);
    }

    @Override
    public SysFolder selectTreeById(String folder)
    {
        return infoMapper.selectTreeById(folder);
    }
    @Override
    public String selectTreeNameById(String selecttreeId)
    {
        return infoMapper.selectTreeNameById(selecttreeId);
    }
    /**
     * 通过话机绑定ID删除
     * @param regime_info_id 话机绑定ID
     * @return 结果
     */
    @Override
    public boolean deleteInfoById(String regime_info_id)
    {
        return infoMapper.deleteInfoById(regime_info_id) > 0 ? true : false;
    }

    @Override
    public int deleteInfoByIds(String ids)
    {
        /*Long[] infoIds = Convert.toLongArray(ids);
        int count = infoMapper.deleteInfoByIds(Convert.toStrArray(ids));
        if (count > 0)
        {
            CacheUtils.removeAll(getCacheName());
        }
        return count;*/

        String[] list= Convert.toStrArray(",",ids);
        return infoMapper.deleteInfoByIds(list);

    }
    /**
     * 获取cache name
     *
     * @return 缓存名
     */
    private String getCacheName()
    {
        return Constants.SYS_CONFIG_CACHE;
    }
    /**
     * 新增保存话机绑定信息
     *
     * @return 结果
     */
    @Override
    @Transactional
    public int insertInfo(SysInfo info)
    {
        info.setCommit_time(DateUtils.dateTimeNow());
        return infoMapper.insertInfo(info);
    }
    /**
     * 根据id查询
     * @param regime_title
     * @return
     */
    @Override
    public SysInfo selectInfoByNo(String regime_title)
    {
        return infoMapper.selectInfoByNo(regime_title);
    }
    /**
     * 修改保存话机绑定信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateInfo(SysInfo info)
    {
        // 修改角色信息
        return infoMapper.updateInfo(info);
    }

    @Override
    @Transactional
    public int deleteInfo(SysInfo info)
    {
        // 修改角色信息
        return infoMapper.deleteInfo(info);
    }

    /**
     * 查询管理树
     * @param folder 信息
     */
    @Override
    public List<Ztree> selectFolderTree(SysFolder folder)
    {
        List<SysFolder> folderList = infoMapper.selectFolderList(folder);
        List<Ztree> ztrees = initZtree(folderList);
        return ztrees;
    }

    /**
     * 对象转树
     * @param folderList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysFolder> folderList)
    {
        return initZtree(folderList, null);
    }

    /**
     * 对象转部门树
     *
     * @param folderList 部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysFolder> folderList, List<String> roleDeptList)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (SysFolder folder : folderList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(folder.getId_());
            ztree.setpId(folder.getParent_());
            ztree.setName(folder.getName_());
            ztree.setTitle(folder.getName_());
            if (isCheck)
            {
                ztree.setChecked(roleDeptList.contains(folder.getId_() + folder.getName_()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }
    /**
     * @param info 话机绑定信息
     * @return 结果
     */
    @Override
    public String checkInfoNameUnique(SysInfo info)
    {
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    @Override
    public int changeStatus(SysInfo info) {
        return 0;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param info 角色信息
     */
    @Override
    public void checkInfoAllowed(SysInfo info)
    {
    }
    /**
     * 根据条件分页查询角色数据
     * @param info 话机绑定信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysInfo> selectInfoList(SysInfo info)
    {
        if("mysql".equals(dbType)){
            return infoMapper.selectInfoMysqlList(info);
        }else{
            return infoMapper.selectInfoList(info);
        }


    }

    @Override
    public List<SysInfo> selectIFList(SysInfo info) {
        return infoMapper.selectIFList(info);
    }


    /**
     * 根据用户ID查询应用话机绑定
     * @param userId 用户ID
     */
    @Override
    public List<SysInfo> selectInfosByUserId(Long userId)
    {
        List<SysInfo> userInfos = infoMapper.selectInfosByUserId(userId);
        List<SysInfo> infos = selectInfoAll();
        for (SysInfo info : infos)
        {
            for (SysInfo userRole : userInfos)
            {
            }
        }
        return infos;
    }
    /**
     * @param ogPerson 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<OgPerson> selectCheckerList(OgPerson ogPerson) {
        return infoMapper.selectCheckerList(ogPerson);
    }

    @Override
    public List<SysInfo> selectAuditList(SysInfo info) {

        if("mysql".equals(dbType)){
            return infoMapper.selectAuditMysqlList(info);
        }else{
            return infoMapper.selectAuditList(info);
        }

//        return infoMapper.selectAuditList(info);

    }
}