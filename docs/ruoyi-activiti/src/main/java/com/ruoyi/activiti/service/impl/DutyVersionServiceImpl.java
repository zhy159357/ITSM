package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.DutySchedulingMapper;
import com.ruoyi.activiti.mapper.DutyTypeinfoMapper;
import com.ruoyi.activiti.mapper.DutyVersionMapper;
import com.ruoyi.activiti.mapper.DutyViewMapper;
import com.ruoyi.activiti.service.IDutyVersionService;
import com.ruoyi.activiti.service.IDutyViewService;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 版本 业务层处理
 *
 * @author dayong_sun
 */
@Service
public class DutyVersionServiceImpl implements IDutyVersionService {

    @Autowired
    private DutyVersionMapper dutyVersionMapper;
    @Autowired
    private DutyTypeinfoMapper dutyTypeinfoMapper;
    @Autowired
    private DutySchedulingMapper dutySchedulingMapper;
    @Autowired
    private DutyViewMapper dutyViewMapper;
    @Autowired
    private IDutyViewService dutyViewService;
    @Autowired
    IOgPersonService iOgPersonService;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 根据条件分页查询版本数据
     *
     * @param dutyVersion 版本信息
     * @return 版本数据集合信息
     */
    @Override
    public List<DutyVersion> selectVersionList(DutyVersion dutyVersion) {
        if ("mysql".equals(dbType)) {
            return dutyVersionMapper.selectVersionMysqlList(dutyVersion);
        } else {
            return dutyVersionMapper.selectVersionList(dutyVersion);
        }
    }

    /**
     * 通过versionId查询版本
     *
     * @param versionId 版本ID
     */
    @Override
    public DutyVersion selectVersionById(String versionId) {
        if ("mysql".equals(dbType)) {
            return dutyVersionMapper.selectVersionByIdMysql(versionId);
        }
        return dutyVersionMapper.selectVersionById(versionId);
    }

    /**
     * 通过versionId预览
     *
     * @param dutyVersion 版本ID
     */
    @Override
    public Map<String, Object> selectPreview(DutyVersion dutyVersion) {
        DutyVersion dutyVersion1 = new DutyVersion();
        DutyVersion dv;
        if ("mysql".equals(dbType)) {
            dv = dutyVersionMapper.selectVersionByIdMysql(dutyVersion.getVersionId());
        } else {
            dv = dutyVersionMapper.selectVersionById(dutyVersion.getVersionId());
        }
        if (null != dv && null != dv.getDutyDate()) {
            if (dv.getDutyDate().contains(",")) {
                dutyVersion1.setDutyDate(dv.getDutyDate().split(",")[0]);
            } else {
                dutyVersion1.setDutyDate(dv.getDutyDate());
            }
        }
        Map<String, Object> map = dutyViewService.selectVersionByDutyDate(dutyVersion1);
        return map;
    }

    /**
     * 通过versionTypeinfoId查询版本
     *
     * @param versionTypeinfoId 版本ID
     */
    @Override
    public DutyVersion selectVersionTypeinfoById(String versionTypeinfoId) {
        if ("mysql".equals(dbType)) {
            return dutyVersionMapper.selectVersionTypeinfoMysqlById(versionTypeinfoId);
        } else {
            return dutyVersionMapper.selectVersionTypeinfoById(versionTypeinfoId);
        }
    }

    /**
     * 通过版本ID查询类别
     *
     * @param dutyVersion
     * @return 岗位对象信息
     */
    @Override
    public List<DutyVersion> selectTypeinfoByVersionId(DutyVersion dutyVersion) {
        DutyTypeinfo dutyTypeinfo = new DutyTypeinfo();
        if (StringUtils.isNotEmpty(dutyVersion.getTypeName()) || StringUtils.isNotEmpty(dutyVersion.getTypeNo())) {
            if ("mysql".equals(dbType)) {
                return dutyVersionMapper.selectTypeinfoByVersionIdMysql(dutyVersion);
            } else {
                return dutyVersionMapper.selectTypeinfoByVersionId(dutyVersion);
            }
        }
        if ("mysql".equals(dbType)) {
            dutyTypeinfo.setParentId("-1");
        } else {
            dutyTypeinfo.setParentId("");
        }
        List<DutyVersion> allList = new ArrayList<>();
        List<DutyTypeinfo> rlist = new ArrayList<>();
        List<DutyTypeinfo> typeinfos = selectVersionTypeinfoByParentId(rlist, dutyTypeinfo);
//        List<DutyTypeinfo> typeinfos = dutyTypeinfoMapper.selectDutyTypeinfoList(new DutyTypeinfo());
        List<DutyVersion> versions;
        if ("mysql".equals(dbType)) {
            versions = dutyVersionMapper.selectTypeinfoByVersionIdMysql(dutyVersion);
        } else {
            versions = dutyVersionMapper.selectTypeinfoByVersionId(dutyVersion);
        }
        if (null != versions && versions.size() > 0) {
            //如果已经生成过类别，对比哪些类别减少了，再次生成
//            List<DutyVersion> typeinfoList = dutyVersionMapper.selectTypeinfoByVersionTypeinfo(dutyVersion);
//            if(null!=typeinfoList&&typeinfoList.size()>0){
//                for(DutyVersion info : typeinfoList){
//                    DutyVersion version = new DutyVersion();
//                    version.setVersionTypeinfoId(UUID.getUUIDStr());
//                    version.setVersionId(dutyVersion.getVersionId());
//                    version.setTypeinfoId(info.getTypeinfoId());
//                    version.setPid(info.getPid());
//                    version.setTypeNo(info.getTypeNo());
//                    version.setTypeName(info.getTypeName());
//                    version.setMobilephone(info.getMobilephone());
//                    version.setLeader(info.getLeader());
//                    version.setInvalidationMark(info.getInvalidationMark());
//                    version.setParentId(info.getParentId());
//                    version.setSerial(info.getSerial());
//                    version.setDutyNumber(info.getDutyNumber());
//                    allList.add(version);
//                }
//            }
        } else {
            for (DutyTypeinfo info : typeinfos) {
                DutyVersion version = new DutyVersion();
                version.setVersionTypeinfoId(UUID.getUUIDStr());
                version.setVersionId(dutyVersion.getVersionId());
                version.setTypeinfoId(info.getTypeinfoId());
                version.setPid(info.getPid());
                version.setTypeNo(info.getTypeNo());
                version.setTypeName(info.getTypeName());
                version.setMobilephone(info.getMobilephone());
                version.setLeader(info.getLeader());
                version.setInvalidationMark(info.getInvalidationMark());
                version.setParentId(info.getParentId());
                version.setSerial(info.getSerial());
                version.setDutyNumber(info.getDutyNumber());
                version.setTypeRows(info.getTypeRows());
                version.setTypeColumns(info.getTypeColumns());
                version.setTypeDescription(info.getTypeDescription());
                allList.add(version);
            }
        }
        if (null != allList && allList.size() > 0) {
            if ("mysql".equals(dbType)) {
                dutyVersionMapper.insertVersionTypeinfoMysql(allList);
            } else {
                dutyVersionMapper.insertVersionTypeinfo(allList);
            }
        }
        if ("mysql".equals(dbType)) {
            return dutyVersionMapper.selectTypeinfoByVersionIdMysql(dutyVersion);
        } else {
            return dutyVersionMapper.selectTypeinfoByVersionId(dutyVersion);
        }
    }

    public List<DutyTypeinfo> selectVersionTypeinfoByParentId(List<DutyTypeinfo> rlist, DutyTypeinfo dutyTypeinfo) {
        DutyTypeinfo du;
        List<DutyTypeinfo> versions = dutyTypeinfoMapper.selectVersionTypeinfoByParentId(dutyTypeinfo);
        if (null == versions || versions.size() == 0) {
            return rlist;
        }
        for (DutyTypeinfo dv : versions) {
            rlist.add(dv);
            du = new DutyTypeinfo();
            du.setParentId(dv.getTypeinfoId());
            selectVersionTypeinfoByParentId(rlist, du);
        }
        return rlist;
    }

    /**
     * 通过版本ID删除版本
     *
     * @param versionId 版本ID
     * @return 结果
     */
    @Override
    public boolean deleteVersionById(Long versionId) {
        return dutyVersionMapper.deleteVersionById(versionId) > 0 ? true : false;
    }

    /**
     * 批量版本信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public AjaxResult deleteVersionByIds(String ids) throws BusinessException {
        String[] versionIds = Convert.toStrArray(ids);
        List<DutyVersion> versions = dutyVersionMapper.selectVersionByIds(versionIds);
        List<DutyScheduling> schedulings = null;
        for (DutyVersion version : versions) {
            if (null != version) {
                String dutyDate = version.getDutyDate();
                if (dutyDate.contains(",")) {
                    String dates[] = dutyDate.split(",");
                    for (String date : dates) {
                        schedulings = dutySchedulingMapper.checkDutyDate(date);
                        if (null != schedulings && schedulings.size() > 0) {
                            break;
                        }
                    }
                } else {
                    schedulings = dutySchedulingMapper.checkDutyDate(dutyDate);
                    if (null != schedulings && schedulings.size() > 0) {
                        break;
                    }
                }
            }
        }
        if (null != schedulings && schedulings.size() > 0) {
            return AjaxResult.error("当前版本已设置排版信息，不可删除");
        }
        return dutyVersionMapper.deleteVersionByIds(versionIds) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 批量类别信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public AjaxResult deleteVersionTypeinfos(String ids) throws BusinessException {
        String[] typeinfoIds = Convert.toStrArray(ids);
        List<DutyVersion> versions = dutyVersionMapper.selectVersionByVersionTypeinfoIds(typeinfoIds);
        List<DutyScheduling> schedulings = null;
        for (DutyVersion version : versions) {
            if (null != version && null != version.getDutyDate()) {
                String dutyDate = version.getDutyDate();
                if (dutyDate.contains(",")) {
                    String dates[] = dutyDate.split(",");
                    for (String date : dates) {
                        schedulings = dutySchedulingMapper.checkDutyDate(date);
                        if (null != schedulings && schedulings.size() > 0) {
                            break;
                        }
                    }
                } else {
                    schedulings = dutySchedulingMapper.checkDutyDate(dutyDate);
                    if (null != schedulings && schedulings.size() > 0) {
                        break;
                    }
                }
            }
        }
        if (null != schedulings && schedulings.size() > 0) {
            return AjaxResult.error("当前版本已设置排版信息，不可删除");
        }
        return dutyVersionMapper.deleteVersionTypeinfos(typeinfoIds) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 新增版本信息
     *
     * @param dutyVersion 版本信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertVersion(DutyVersion dutyVersion) {
        // 新增版本信息
        dutyVersion.setVersionId(UUID.getUUIDStr());
        dutyVersion.setAddTime(DateUtils.dateTimeNow());
        return dutyVersionMapper.insertVersion(dutyVersion);
    }

    /**
     * 修改版本信息
     *
     * @param dutyVersion 版本信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateVersion(DutyVersion dutyVersion) {
        if ("mysql".equals(dbType)) {
            return dutyVersionMapper.updateVersionMysql(dutyVersion);
        }
        return dutyVersionMapper.updateVersion(dutyVersion);
    }

    /**
     * 修改类别信息
     *
     * @param dutyVersion
     * @return 结果
     */
    @Override
    @Transactional
    public int updateVersionTypeinfo(DutyVersion dutyVersion) {
        return dutyVersionMapper.updateVersionTypeinfo(dutyVersion);
    }

    /**
     * 克隆版本信息
     *
     * @param versionId
     * @return 结果
     */
    @Override
    @Transactional
    public int versionClone(String versionId) {
        DutyVersion maxNo = dutyVersionMapper.selectMaxTypeNo();
        DutyVersion dutyVersion;
        if ("mysql".equals(dbType)) {
            dutyVersion = dutyVersionMapper.selectVersionByIdMysql(versionId);
        } else {
            dutyVersion = dutyVersionMapper.selectVersionById(versionId);
        }
        DutyVersion version = new DutyVersion();
        version.setVersionId(UUID.getUUIDStr());
        version.setRemark(dutyVersion.getRemark());
        version.setVersionNo(maxNo.getVersionNo());
        version.setVersionName(dutyVersion.getVersionName() + maxNo.getVersionNo());
        version.setAddTime(DateUtils.dateTimeNow());
        List<DutyVersion> allList = new ArrayList<>();
        List<DutyVersion> versions;
        if ("mysql".equals(dbType)) {
            versions = dutyVersionMapper.selectTypeinfoByVersionIdMysql(dutyVersion);
        } else {
            versions = dutyVersionMapper.selectTypeinfoByVersionId(dutyVersion);
        }
        if (null != versions && versions.size() > 0) {
            for (DutyVersion versi : versions) {
                DutyVersion ver = new DutyVersion();
                ver.setVersionTypeinfoId(UUID.getUUIDStr());
                ver.setVersionId(version.getVersionId());
                ver.setTypeinfoId(versi.getTypeinfoId());
                ver.setPid(versi.getPid());
                ver.setTypeNo(versi.getTypeNo());
                ver.setTypeName(versi.getTypeName());
                ver.setMobilephone(versi.getMobilephone());
                ver.setLeader(versi.getLeader());
                ver.setInvalidationMark(versi.getInvalidationMark());
                ver.setParentId(versi.getParentId());
                ver.setSerial(versi.getSerial());
                ver.setDutyNumber(versi.getDutyNumber());
                version.setTypeRows(versi.getTypeRows());
                version.setTypeColumns(versi.getTypeColumns());
                allList.add(ver);
            }
        }
        if (null != allList && allList.size() > 0) {
            if ("mysql".equals(dbType)) {
                dutyVersionMapper.insertVersionTypeinfoMysql(allList);
            } else {
                dutyVersionMapper.insertVersionTypeinfo(allList);
            }
        }
        return dutyVersionMapper.insertVersion(version);
    }

    /**
     * 校验版本代码是否唯一
     *
     * @return 版本信息
     */
    @Override
    public String checkVersionNoUnique(DutyVersion dutyVersion) {
        String versionNo = StringUtils.isNull(dutyVersion.getVersionNo()) ? "" : dutyVersion.getVersionNo();
        DutyVersion info;
        if ("mysql".equals(dbType)) {
            info = dutyVersionMapper.checkVersionNoUniqueMysql(dutyVersion.getVersionNo());
        } else {
            info = dutyVersionMapper.checkVersionNoUnique(dutyVersion.getVersionNo());
        }
        if (StringUtils.isNotNull(info) && info.getVersionNo().equals(versionNo)) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 校验版本代码是否唯一
     *
     * @return 版本信息
     */
    @Override
    public String checkDutyDateUnique(DutyVersion dutyVersion) {
        String dutyDate = StringUtils.isNull(dutyVersion.getDutyDate()) ? "" : dutyVersion.getDutyDate();
        String[] month = dutyDate.split(",");
        for (String str : month) {
            DutyVersion info;
            if ("mysql".equals(dbType)) {
                info = dutyVersionMapper.checkDutyDateUniqueMysql(str);
            } else {
                info = dutyVersionMapper.checkDutyDateUnique(str);
            }
            if (StringUtils.isNotNull(info) && info.getDutyDate().contains(str) && !info.getVersionId().equals(dutyVersion.getVersionId())) {
                return UserConstants.ROLE_KEY_NOT_UNIQUE;
            }
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 根据类别ID查询信息
     *
     * @param dutyVersion 类别ID
     * @return 类别信息
     */
    @Override
    public DutyVersion selectTypeinfoById(DutyVersion dutyVersion) {
        return dutyVersionMapper.selectTypeinfoById(dutyVersion);
    }

    /**
     * 查询类别管理树
     *
     * @param dutyVersion 部门信息
     * @return 所有部门信息
     */
    @Override
    public List<Ztree> selectTypeinfoTree(DutyVersion dutyVersion) {
        List<DutyVersion> typeinfoList = dutyVersionMapper.selectTypeinfoList(dutyVersion);
        List<Ztree> ztrees = initZtree(typeinfoList);
        return ztrees;
    }

    /**
     * 对象转部门树
     *
     * @param typeinfoList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<DutyVersion> typeinfoList) {
        return initZtree(typeinfoList, null);
    }

    /**
     * 对象转部门树
     *
     * @param typeinfoList 部门列表
     * @param roleDeptList 版本已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<DutyVersion> typeinfoList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (DutyVersion version : typeinfoList) {
            Ztree ztree = new Ztree();
            ztree.setId(version.getTypeinfoId());
            ztree.setpId(version.getParentId());
            ztree.setName(version.getTypeName());
            ztree.setTitle(version.getTypeName());
            if (isCheck) {
                ztree.setChecked(roleDeptList.contains(version.getTypeinfoId() + version.getParentName()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

}
