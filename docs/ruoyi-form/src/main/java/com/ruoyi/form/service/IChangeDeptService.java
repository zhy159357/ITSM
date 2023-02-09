package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.form.domain.ChangeDeptEntity;
import com.ruoyi.form.domain.ChangeDeptEntityVo;

import java.util.List;

/**
 * @program: ruoyi
 * @description: 变更额度配置接口
 * @author: ma zy
 * @date: 2022-09-23 14:26
 **/
public interface IChangeDeptService extends IService<ChangeDeptEntity> {
    /**
     * 查询所有
     * @param dept
     * @return
     */
    List<ChangeDeptEntityVo> listAll(OgOrg dept);
    /**
     * 根据机构部门查询
     * @param deptId
     * @return
     */
    ChangeDeptEntityVo selectOne(String deptId);
    /**
     * 保存接口
     * @param changeDeptEntityVo
     * @return
     */
    boolean saveAdd(ChangeDeptEntityVo changeDeptEntityVo);

    /**
     * 根据机构id查询默认和剩余
     * @param deptId
     * @return
     */
    ChangeDeptEntity selectByDeptId(String deptId);
    /**
     * 根据机构id修改剩余额度
     * @return
     */
    void updateOverSizer(String deptId,String overSize);
    /**
     * 根据机构id，修改剩余额度递减1（参数是几递减几）
     * @return
     */
    void updateOverSizerOne(String deptId,String reduce);


    void addOverSizerOne(String deptId);


    /**
     * 根据机构id判断是否存在配置
     * @param deptId
     * @return 有：true，无：false
     */
    boolean isDeptAndSystem(String deptId);


}
