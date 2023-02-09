package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.form.domain.ChangeDeptEntity;
import com.ruoyi.form.domain.ChangeDeptEntityVo;
import com.ruoyi.form.mapper.ChangeDeptMapper;
import com.ruoyi.form.service.IChangeDeptService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ruoyi
 * @description: 变更管理额度接口实现
 * @author: ma zy
 * @date: 2022-09-23 14:40
 **/
@Service
public class ChangeDeptServiceImpl extends ServiceImpl<ChangeDeptMapper, ChangeDeptEntity> implements IChangeDeptService {
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ChangeDeptMapper changeDeptMapper;
    @Autowired
    private IChangeDeptService iChangeDeptService;

    @Autowired
    private ISysDeptService deptService;
    @Override
    public List<ChangeDeptEntityVo> listAll(OgOrg dept) {
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("start_status", "1");
        List<ChangeDeptEntity> changeDeptEntityList = changeDeptMapper.selectList(queryWrapper);
        List<ChangeDeptEntityVo> changeDeptEntityVoList = new ArrayList<>();
        changeDeptEntityList.stream().forEach(p->{
            ChangeDeptEntityVo changeDeptEntityVo = new ChangeDeptEntityVo();
            BeanUtils.copyProperties(p,changeDeptEntityVo);
            OgOrg ogOrg = deptService.selectDeptById(p.getOrgId());
            changeDeptEntityVo.setOrgName(ogOrg.getOrgName());
            changeDeptEntityVo.setOrgCode(ogOrg.getOrgCode());
            changeDeptEntityVoList.add(changeDeptEntityVo);
        });
        return changeDeptEntityVoList;
    }

    /**
     * 根据机构不查询
     * @param deptId
     * @return
     */
    @Override
    public ChangeDeptEntityVo selectOne(String deptId) {
        ChangeDeptEntityVo changeDeptEntityVo = new ChangeDeptEntityVo();
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", deptId);
        ChangeDeptEntity changeDeptEntity = changeDeptMapper.selectOne(queryWrapper);
        OgOrg ogOrg = deptService.selectDeptById(deptId);
        BeanUtils.copyProperties(changeDeptEntity,changeDeptEntityVo);
        changeDeptEntityVo.setOrgName(ogOrg.getOrgName());
        return changeDeptEntityVo;
    }

    /**
     * 添加功能对数据新增
     * @param changeDeptEntityVo
     * @return
     */
    @Override
    public boolean saveAdd(ChangeDeptEntityVo changeDeptEntityVo) {
        boolean flag = true;
        ChangeDeptEntity changeDeptEntity = new ChangeDeptEntity();
        BeanUtils.copyProperties(changeDeptEntityVo,changeDeptEntity);
        changeDeptEntity.setId(IdUtils.fastSimpleUUID());
        List<PubParaValue> pubParaValueList =iPubParaValueService.selectPubParaValueByParaName("change_replace");
        changeDeptEntity.setReplaceTime(pubParaValueList.get(0).getValue());
        changeDeptEntityVo.setOrgId(changeDeptEntityVo.getOrgId());
        changeDeptEntity.setOverSize(changeDeptEntityVo.getInitSize());
        changeDeptEntity.setOrgId(changeDeptEntityVo.getParentId());
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", changeDeptEntityVo.getParentId());
        ChangeDeptEntity changeDeptEntityOne = changeDeptMapper.selectOne(queryWrapper);
        if(StringUtils.isEmpty(changeDeptEntityOne)){
            iChangeDeptService.save(changeDeptEntity);
        }else{
            flag = false;
        }
        return flag;
    }

    /**
     * 根据id查询默认和剩余额度
     * @param deptId
     * @return
     */
    @Override
    public ChangeDeptEntity selectByDeptId(String deptId) {
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", deptId);
        ChangeDeptEntity changeDeptEntity = changeDeptMapper.selectOne(queryWrapper);
        return changeDeptEntity;
    }

    /**
     * 根据机构id修改剩余额度
     * @param deptId
     * @return
     */
    @Override
    public void updateOverSizer(String deptId,String overSize) {
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", deptId);
        ChangeDeptEntity changeDeptEntity = new ChangeDeptEntity();
        changeDeptEntity.setOverSize(overSize);
        changeDeptMapper.update(changeDeptEntity,queryWrapper);
    }

    /**
     * 根据参数减少
     * @param deptId
     * @param reduce
     */
    @Override
    public void updateOverSizerOne(String deptId, String reduce) {
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", deptId);
        ChangeDeptEntity changeDeptEntity = changeDeptMapper.selectOne(queryWrapper);
        int upOverSize = Integer.valueOf(changeDeptEntity.getOverSize())-Integer.valueOf(reduce);
        ChangeDeptEntity changeDeptEntityDto = new ChangeDeptEntity();
        if(upOverSize > 0){
            changeDeptEntityDto.setOverSize(String.valueOf(upOverSize));
        }else {
            changeDeptEntityDto.setOverSize("0");
        }
        changeDeptMapper.update(changeDeptEntityDto,queryWrapper);
    }

    @Override
    public void addOverSizerOne(String deptId) {
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", deptId);
        ChangeDeptEntity changeDeptEntity = changeDeptMapper.selectOne(queryWrapper);
        int upOverSize = Integer.valueOf(changeDeptEntity.getOverSize())+1;
        ChangeDeptEntity changeDeptEntityDto = new ChangeDeptEntity();
        if(upOverSize > 0){
            changeDeptEntityDto.setOverSize(String.valueOf(upOverSize));
        }else {
            changeDeptEntityDto.setOverSize("0");
        }
        changeDeptMapper.update(changeDeptEntityDto,queryWrapper);
    }

    /**
     * 根据机构id，判断是否存在变更配置
     * @param deptId
     * @return
     */
    @Override
    public boolean isDeptAndSystem(String deptId) {
        boolean flag = true;
        QueryWrapper<ChangeDeptEntity> queryWrapper = new QueryWrapper<ChangeDeptEntity>().eq("dept_id", deptId);
        ChangeDeptEntity changeDeptEntity = changeDeptMapper.selectOne(queryWrapper);
        if(StringUtils.isEmpty(changeDeptEntity)){
            flag = false;
        }
        return flag;
    }
}
