package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.DutySubstituteRemarkMapper;
import com.ruoyi.activiti.service.IDutySubstituteRemarkService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.domain.entity.DutySubstituteRemark;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.OgPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class IDutySubstituteRemarkServiceImpl implements IDutySubstituteRemarkService {



    @Autowired
    private DutySubstituteRemarkMapper dutySubstituteRemarkMapper;
    
    @Autowired
    private OgPostService ogPostService;

    /**
     * 查询替班备注列表
     * @param dutySubstituteRemark
     * @return
     */
    @Override
    public List<DutySubstituteRemark> selectSubstituteRemarkList(DutySubstituteRemark dutySubstituteRemark) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);
        if(!status.equals("2")){
            dutySubstituteRemark.setPid(userId);
        }

        return dutySubstituteRemarkMapper.selectSubstituteRemarkList(dutySubstituteRemark);
    }

    /**
     * 根据值班时间查询备注信息
     * @param substituteRemark
     * @return
     */
    @Override
    public DutySubstitute selectRemarkByDutyDate(DutySubstitute substituteRemark) {
        return dutySubstituteRemarkMapper.selectRemarkByDutyDate(substituteRemark);
    }

    /**
     * 查询审批通过的替班信息
     * @param dutySubstitute
     * @return
     */
    @Override
    public List<DutySubstitute> selectSubstituteList(DutySubstitute dutySubstitute) {
        return dutySubstituteRemarkMapper.selectSubstituteList(dutySubstitute);
    }

    /**
     * 查询模板
     * @param template
     * @return
     */
    @Override
    public DutySubstituteRemark selectTemplateById(String template) {
        return dutySubstituteRemarkMapper.selectTemplateById(template);
    }

    @Override
    public String addCheckSave(DutySubstituteRemark dutySubstituteRemark) {

        String msg = "success";
        //通过值班时间和值班类型，检测是否存在
        DutySubstituteRemark substituteRemark  = dutySubstituteRemarkMapper.addCheckSave(dutySubstituteRemark);

        if(substituteRemark != null){
            msg = "已存在对应替班备注信息!";
        }

        return msg;
    }

    /**
     * 保存替班备注
     * @param dutySubstituteRemark
     * @return
     */
    @Override
    @Transactional
    public int insertDutySubstituteRemark(DutySubstituteRemark dutySubstituteRemark) {
        dutySubstituteRemark.setSubstituteRemarkId(UUID.getUUIDStr());
        int result =  dutySubstituteRemarkMapper.insertDutySubstituteRemark(dutySubstituteRemark);
        return result;
    }

    /**
     * 根据替班备注ID查询替班备注信息
     * @param substituteRemarkId
     * @return
     */
    @Override
    public DutySubstituteRemark selectDutySubstituteRemarkById(String substituteRemarkId) {
        return dutySubstituteRemarkMapper.selectDutySubstituteRemarkById(substituteRemarkId);
    }

    /**
     * 保存替班备注
     * @param dutySubstituteRemark
     * @return
     */
    @Override
    @Transactional
    public int updateDutySubstituteRemark(DutySubstituteRemark dutySubstituteRemark) {
        int result =  dutySubstituteRemarkMapper.updateDutySubstituteRemark(dutySubstituteRemark);
        return result;
    }

    /**
     * 删除替班备注
     */
    @Override
    public AjaxResult deleteDutySubstituteRemarkByIds(String ids) {
        return dutySubstituteRemarkMapper.deleteDutySubstituteRemarkByIds(Convert.toStrArray(ids)) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    private String getStatusByUserId(String userId) {
        String status = "-1";//其他
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId, null);
        for (OgPost op : list) {
            if ( PostConstants.SUPERADMIN.equals(op.getPostId()) || PostConstants.SJZXLD.equals(op.getPostId()) || PostConstants.ZBGLY.equals(op.getPostId()) ) {
                status = "2";//值班管理员
                break;
            }
        }
        if ("2".equals(status)) {
            return status;
        }
        return status;
    }

    /**
     * 查询当前登录人是否是创建人
     * @param id
     * @return
     */
    @Override
    public int selectCreateIdById(String id) {
        int result = 0;
        DutySubstituteRemark substituteRemark  = dutySubstituteRemarkMapper.selectContById(id);
        if (null != substituteRemark && substituteRemark.getPid().equals(ShiroUtils.getOgUser().getUserId())) {
            result = 1;
        }
        return result;
    }

}
