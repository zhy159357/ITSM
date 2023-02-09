package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.DutyTypeinfoMapper;
import com.ruoyi.activiti.service.IDutyTypeinfoService;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数列别Service业务层处理
 * @author ruoyi
 * @date 2020-12-06
 */
@Service
public class DutyTypeinfoServiceImpl implements IDutyTypeinfoService {

    @Autowired
    private DutyTypeinfoMapper dutyTypeinfoMapper;

    private final String SUCCESS = "1";// 启用状态

    /**
     * 查询参数列别
     * @param typeinfoId 参数列别ID
     * @return 参数列别
     */
    @Override
    public DutyTypeinfo selectDutyTypeinfoById(String typeinfoId) {
        return dutyTypeinfoMapper.selectDutyTypeinfoById(typeinfoId);
    }

    /**
     * 查询值班人员
     * @param rid 参数列别ID
     * @return 参数列别
     */
    @Override
    public  List<OgPerson> selectUserList(String rid) {
        return dutyTypeinfoMapper.selectUserList(rid);
    }

    /**
     * 查询参数列别
     * @param parentId 父参数ID
     * @return 参数列别
     */
    @Override
    public DutyTypeinfo selectDutyTypeinfoByParentId(String parentId) {
        return dutyTypeinfoMapper.selectDutyTypeinfoByParentId(parentId);
    }

    /**
     * 查询参数列别列表
     * @param dutyTypeinfo 参数列别
     * @return 参数列别
     */
    @Override
    public List<DutyTypeinfo> selectDutyTypeinfoList(DutyTypeinfo dutyTypeinfo) {
        return dutyTypeinfoMapper.selectDutyTypeinfoList(dutyTypeinfo);
    }

    /**
     * 新增参数列别
     * @param dutyTypeinfo 参数列别
     * @return 结果
     */
    @Override
    public int insertDutyTypeinfo(DutyTypeinfo dutyTypeinfo) {
        // 赋值主键id
        dutyTypeinfo.setParentId(dutyTypeinfo.getTypeinfoId());
        dutyTypeinfo.setTypeinfoId(UUID.getUUIDStr());
        dutyTypeinfo.setInvalidationMark("1");// 默认正常
        return dutyTypeinfoMapper.insertDutyTypeinfo(dutyTypeinfo);
    }

    /**
     * 修改参数列别
     * @param dutyTypeinfo 参数列别
     * @return 结果
     */
    @Override
    public int updateDutyTypeinfo(DutyTypeinfo dutyTypeinfo) {
        dutyTypeinfo.setUpdateTime(DateUtils.getDate());
        return dutyTypeinfoMapper.updateDutyTypeinfo(dutyTypeinfo);
    }

    /**
     * 删除参数列别对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDutyTypeinfoByIds(String ids) {
        return dutyTypeinfoMapper.deleteDutyTypeinfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除参数列别信息
     * @param typeinfoId 参数列别ID
     * @return 结果
     */
    @Override
    public int deleteDutyTypeinfoById(String typeinfoId) {
        return dutyTypeinfoMapper.deleteDutyTypeinfoById(typeinfoId);
    }

    /**
     * 查询参数树
     * @param info
     * @return 结果
     */
    @Override
    public List<ZtreeStr> selectTypeinfoTree(DutyTypeinfo info) {
        List<DutyTypeinfo> infos = dutyTypeinfoMapper.selectTypeinfoTree(info);
        return initZtreeStr(infos);
    }

    public List<Ztree> initZtree(List<DutyTypeinfo> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (DutyTypeinfo dept : deptList) {
            if (SUCCESS.equals(dept.getInvalidationMark())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getTypeinfoId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getTypeName());
                ztree.setTitle(dept.getTypeName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getTypeinfoId() + dept.getTypeName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    public List<Ztree> initZtree(List<DutyTypeinfo> list) {
        return initZtree(list, null);
    }

    @Override
    public List<Ztree> selectTypeTreeExcludeChild(DutyTypeinfo dutyTypeinfo) {
        List<DutyTypeinfo> deptList = dutyTypeinfoMapper.selectDutyTypeinfoList(dutyTypeinfo);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 对象转参数树
     * @param typeinfoList 参数列表
     * @return 树结构列表
     */
    public List<ZtreeStr> initZtreeStr(List<DutyTypeinfo> typeinfoList) {

        List<ZtreeStr> ztrees = new ArrayList<ZtreeStr>();
        for (DutyTypeinfo info : typeinfoList) {
            if ("1".equals(info.getInvalidationMark())) {
                ZtreeStr ztreeStr = new ZtreeStr();
                ztreeStr.setId(info.getTypeinfoId());
                ztreeStr.setpId(info.getParentId());
                ztreeStr.setName(info.getTypeName());
                ztreeStr.setTitle(info.getTypeName());
                ztrees.add(ztreeStr);
            }
        }
        return ztrees;
    }

    /**
     * 校验类别编码是否唯一
     * @param dutyTypeinfo 角色信息
     * @return 结果
     */
    @Override
    public String checkTypeNoUnique(DutyTypeinfo dutyTypeinfo)
    {
        String typeNo = StringUtils.isNull(dutyTypeinfo.getTypeNo()) ? "" : dutyTypeinfo.getTypeNo();
        DutyTypeinfo info = dutyTypeinfoMapper.checkTypeNoUnique(dutyTypeinfo.getTypeNo());
        if (StringUtils.isNotNull(info) && info.getTypeNo().equals(typeNo))
        {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 查询类别编码
     * @param dutyTypeinfo 类别信息
     * @return 结果
     */
    @Override
    public List<DutyTypeinfo> selectTypeNo(DutyTypeinfo dutyTypeinfo)
    {
        return dutyTypeinfoMapper.selectTypeNo(dutyTypeinfo);
    }

}
