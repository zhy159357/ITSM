package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.form.domain.ChangeDeptPersonEntity;
import com.ruoyi.form.domain.ChangeDeptPersonEntityVo;
import com.ruoyi.form.domain.ChangeDeptPersonVo;

import java.util.List;

/**
 * @program: ruoyi
 * @description: 机构负责人和分管领导
 * @author: ma zy
 * @date: 2022-10-04 09:16
 **/
public interface IChangePersonService extends IService<ChangeDeptPersonEntity> {
    List<ChangeDeptPersonEntityVo> listAll(OgOrg dept);

    void saveAdd(ChangeDeptPersonEntityVo changeDeptPersonEntityVo);

    /**
     * 根据id查询一条数据
     * @param Id
     * @return
     */
    ChangeDeptPersonEntityVo selectOne(String Id);

    /**
     * 根据机构id查询一条数据
     * @param deptId
     * @return
     */
    ChangeDeptPersonVo selectOneId(String deptId);

    /**
     * 根据机构id获取机构分行对应的id
     * @param deptId
     * @return
     */
    String selectDept(String deptId);
}
