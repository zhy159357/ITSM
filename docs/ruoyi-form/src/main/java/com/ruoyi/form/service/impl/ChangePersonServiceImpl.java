package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.form.domain.ChangeDeptPersonEntity;
import com.ruoyi.form.domain.ChangeDeptPersonEntityVo;
import com.ruoyi.form.domain.ChangeDeptPersonVo;
import com.ruoyi.form.domain.CommonTree;
import com.ruoyi.form.mapper.ChangePersonMapper;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.service.ICommonTreeService;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: ruoyi
 * @description: 机构负责人和分管领导
 * @author: ma zy
 * @date: 2022-10-04 09:18
 **/
@Service
public class ChangePersonServiceImpl extends ServiceImpl<ChangePersonMapper, ChangeDeptPersonEntity> implements IChangePersonService {

    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IChangePersonService iChangePersonService;
    @Autowired
    private ChangePersonMapper changePersonMapper;
    @Autowired
    private OgPersonMapper ogPersonMapper;
    @Autowired
    private ICommonTreeService commonTreeService;
    /**
     * 查询列表数据
     * @param dept
     * @return
     */
    @Override
    public List<ChangeDeptPersonEntityVo> listAll(OgOrg dept) {
        QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<ChangeDeptPersonEntity>();
        if(StringUtils.isNotEmpty(dept.getOrgName())){
            queryWrapper.like("dept_name",dept.getOrgName());
        }
        List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
        List<ChangeDeptPersonEntityVo> changeDeptPersonEntityVoList = new ArrayList<>();
        changeDeptPersonEntityList.stream().forEach(p->{
            ChangeDeptPersonEntityVo changeDeptPersonEntityVo = new ChangeDeptPersonEntityVo();
            BeanUtils.copyProperties(p,changeDeptPersonEntityVo);
            changeDeptPersonEntityVo.setDeptPerson(getPerson(p.getDeptPerson()));
            changeDeptPersonEntityVo.setDeptLeaderPerson(getPerson(p.getDeptLeaderPerson()));
            changeDeptPersonEntityVo.setCreateDate(p.getCreateDate());
            changeDeptPersonEntityVoList.add(changeDeptPersonEntityVo);
        });
        return changeDeptPersonEntityVoList;
    }

    public String getPerson(String personId){
        OgPerson ogPerson = ogPersonMapper.selectOgPersonById(personId);
        if(ogPerson==null){
            return "";
        }
        return ogPerson.getpName();
    }

    /**
     * 新增
     * @param changeDeptPersonEntityVo
     * @return
     */
    @Override
    public void saveAdd(ChangeDeptPersonEntityVo changeDeptPersonEntityVo) {
        boolean flag = true;
        ChangeDeptPersonEntity changeDeptPersonEntity = new ChangeDeptPersonEntity();
        BeanUtils.copyProperties(changeDeptPersonEntityVo,changeDeptPersonEntity);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowDate = df.format(new Date());
        changeDeptPersonEntity.setCreateDate(nowDate);
        changeDeptPersonEntity.setId(IdUtils.fastSimpleUUID());
        // CommonTree commonTree=commonTreeService.selectCommonTreeByStrId(changeDeptPersonEntityVo.getParentId());
        CommonTree commonTree=commonTreeService.selectCommonTreeByOgId(changeDeptPersonEntityVo.getParentId());

        changeDeptPersonEntity.setOrgId(commonTree.getOgId());
        iChangePersonService.save(changeDeptPersonEntity);
    }

    /**
     * 根据机构获取一条数据
     * @param
     * @return
     */
    @Override
    public ChangeDeptPersonEntityVo selectOne(String id) {
        ChangeDeptPersonEntityVo changeDeptPersonEntityVo = new ChangeDeptPersonEntityVo();
        QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<ChangeDeptPersonEntity>().eq("id", id);
        ChangeDeptPersonEntity changeDeptPersonEntityOne = changePersonMapper.selectOne(queryWrapper);
        if(changeDeptPersonEntityOne == null){
            return null;
        }
        // 修改组织查询，从查询og_org修改到查询common_tree表
        CommonTree commonTree=commonTreeService.selectCommonTreeByOgId(changeDeptPersonEntityOne.getOrgId());
        BeanUtils.copyProperties(changeDeptPersonEntityOne,changeDeptPersonEntityVo);
        if(null != commonTree){
            changeDeptPersonEntityVo.setOrgName(commonTree.getName());
        }
        changeDeptPersonEntityVo.setDeptPersonName(getPerson(changeDeptPersonEntityOne.getDeptPerson()));
        changeDeptPersonEntityVo.setDeptLeaderPersonName(getPerson(changeDeptPersonEntityOne.getDeptLeaderPerson()));
        return changeDeptPersonEntityVo;
    }

    /**
     * 根据机构id查询人信息
     * @param deptId
     * @return
     */
    @Override
    public ChangeDeptPersonVo selectOneId(String deptId) {
        QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<ChangeDeptPersonEntity>().eq("dept_id", deptId);
        ChangeDeptPersonEntity changeDeptPersonEntityOne = changePersonMapper.selectOne(queryWrapper);
        if(changeDeptPersonEntityOne==null){
            return null;
        }
        ChangeDeptPersonVo changeDeptPersonVo = new ChangeDeptPersonVo();
        changeDeptPersonVo.setDeptPersonId(changeDeptPersonEntityOne.getDeptPerson());
        changeDeptPersonVo.setDeptPersonName(getPerson(changeDeptPersonEntityOne.getDeptPerson()));
        changeDeptPersonVo.setDeptLeaderPersonId(changeDeptPersonEntityOne.getDeptLeaderPerson());
        changeDeptPersonVo.setDeptLeaderPersonName(getPerson(changeDeptPersonEntityOne.getDeptLeaderPerson()));
        changeDeptPersonVo.setOrgId(changeDeptPersonEntityOne.getOrgId());
        changeDeptPersonVo.setUserId(changeDeptPersonEntityOne.getUserId());
        changeDeptPersonVo.setUserAccount(changeDeptPersonEntityOne.getUserAccount());
        return changeDeptPersonVo;
    }

    /**根据机构id查询分行id
     *
     * @param deptId
     * @return
     */
    @Override
    public String selectDept(String deptId) {
        OgOrg ogOrg = deptService.selectDeptById(deptId);
        String deptIds = null;
        if(ogOrg.getLevelCode().contains("310100002")){
            String[] str = ogOrg.getLevelCode().split("/");
            if(str.length > 3){
                deptIds = str[3];
            }
        }
        return deptIds;
    }

}
