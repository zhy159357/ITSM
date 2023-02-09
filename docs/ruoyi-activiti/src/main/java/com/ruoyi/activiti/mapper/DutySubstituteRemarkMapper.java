package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.domain.entity.DutySubstituteRemark;

import java.util.List;

public interface DutySubstituteRemarkMapper {

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
     * 通过值班时间和值班类型，检测是否存在
     * @param dutySubstituteRemark
     * @return
     */
    public DutySubstituteRemark addCheckSave(DutySubstituteRemark dutySubstituteRemark);

    /**
     * 插入替班备注
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
    public int deleteDutySubstituteRemarkByIds(String[] substituteRemarkId);

    DutySubstituteRemark selectContById(String id);
}
