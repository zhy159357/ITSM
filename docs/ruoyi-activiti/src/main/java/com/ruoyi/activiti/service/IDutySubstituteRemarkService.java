package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.domain.entity.DutySubstituteRemark;

import java.util.List;

/**
 * 替班备注Service接口
 */
public interface IDutySubstituteRemarkService {

    /**
     * 查询替班备注列表
     * @param dutySubstituteRemark
     * @return
     */
    public List<DutySubstituteRemark> selectSubstituteRemarkList(DutySubstituteRemark dutySubstituteRemark);


    /**
     * 根据值班时间查询备注信息
     * @param substituteRemark
     * @return
     */
    public DutySubstitute selectRemarkByDutyDate(DutySubstitute substituteRemark);

    /**
     * 查询审批通过的替班信息
     * @param dutySubstitute
     * @return
     */
    public List<DutySubstitute> selectSubstituteList(DutySubstitute dutySubstitute);

    /**
     * 查询模板
     * @param template
     * @return
     */
    public DutySubstituteRemark selectTemplateById(String template);


    /**
     * 根据值班时间和值班类别检查是否存在
     */
    public String addCheckSave(DutySubstituteRemark dutySubstituteRemark);

    /**
     * 保存替班备注-页面
     * @param dutySubstituteRemark
     * @return
     */
    public int insertDutySubstituteRemark(DutySubstituteRemark dutySubstituteRemark);

    /**
     * 根据替班备注ID查询替班备注信息
     * @param substituteRemarkId
     * @return
     */
    public DutySubstituteRemark selectDutySubstituteRemarkById(String substituteRemarkId);

    /**
     * 保存替班备注
     * @param dutySubstituteRemark
     * @return
     */
    public int updateDutySubstituteRemark(DutySubstituteRemark dutySubstituteRemark);

    /**
     * 删除替班备注
     */
    public AjaxResult deleteDutySubstituteRemarkByIds(String ids);

    int selectCreateIdById(String id);
}
