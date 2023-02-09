package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.mapper.FmbizAndIssueMapper;
import com.ruoyi.activiti.service.IFmbizAndIssueService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmbizAndIssue;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 事件单关联问题单关联关系Service业务层处理
 *
 * @author liul
 * @date 2021-10-15
 */
@Service
public class FmbizAndIssueServiceImpl implements IFmbizAndIssueService {
    @Autowired
    private FmbizAndIssueMapper fmbizAndIssueMapper;
    @Autowired
    private IPubRelationService iPubRelationService;
    @Autowired(required=false)
    private KnowledgeBusinessContentService knowledgeBusinessContentService;

    /**
     * 查询事件单关联问题单关联关系
     *
     * @param id 事件单关联问题单关联关系ID
     * @return 事件单关联问题单关联关系
     */
    @Override
    public FmbizAndIssue selectFmbizAndIssueById(String id) {
        return fmbizAndIssueMapper.selectFmbizAndIssueById(id);
    }

    /**
     * 查询事件单关联问题单关联关系列表
     *
     * @param fmbizAndIssue 事件单关联问题单关联关系
     * @return 事件单关联问题单关联关系
     */
    @Override
    public List<FmbizAndIssue> selectFmbizAndIssueList(FmbizAndIssue fmbizAndIssue) {
        return fmbizAndIssueMapper.selectFmbizAndIssueList(fmbizAndIssue);
    }

    /**
     * 新增事件单关联问题单关联关系
     *
     * @param fmbizAndIssue 事件单关联问题单关联关系
     * @return 结果
     */
    @Override
    public int insertFmbizAndIssue(FmbizAndIssue fmbizAndIssue) {
        return fmbizAndIssueMapper.insertFmbizAndIssue(fmbizAndIssue);
    }

    /**
     * 修改事件单关联问题单关联关系
     *
     * @param fmbizAndIssue 事件单关联问题单关联关系
     * @return 结果
     */
    @Override
    public int updateFmbizAndIssue(FmbizAndIssue fmbizAndIssue) {
        return fmbizAndIssueMapper.updateFmbizAndIssue(fmbizAndIssue);
    }

    /**
     * 删除事件单关联问题单关联关系对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmbizAndIssueByIds(String ids) {
        return fmbizAndIssueMapper.deleteFmbizAndIssueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除事件单关联问题单关联关系信息
     *
     * @param id 事件单关联问题单关联关系ID
     * @return 结果
     */
    @Override
    public int deleteFmbizAndIssueById(String id) {
        return fmbizAndIssueMapper.deleteFmbizAndIssueById(id);
    }

    @Override
    public void saveFmbizAndIssue(FmBiz fmBiz) {
        String fmId = fmBiz.getFmId();
        FmbizAndIssue fai = new FmbizAndIssue();
        fai.setId(UUID.getUUIDStr());
        fai.setFmId(fmId);
        fai.setGroupId(fmBiz.getGroupId());
        fai.setOperationTime(DateUtils.dateTimeNow());
        String issuefxId = fmBiz.getParams().get("issuefxId").toString();
        if (StringUtils.isNotEmpty(issuefxId)) {
            fai.setIssuefxId(issuefxId);
            fai.setState("0");
            fai.setOperationId(ShiroUtils.getUserId());
            savePubRelation(fai);
        } else {
            fai.setState("1");
        }
        insertFmbizAndIssue(fai);
    }

    @Override
    public AjaxResult relationIssue(FmBiz fmBiz) {
        String fmId = fmBiz.getFmId();
        FmbizAndIssue fai = new FmbizAndIssue();
        fai.setFmId(fmId);
        fai.setState("1");
        List<FmbizAndIssue> list = selectFmbizAndIssueList(fai);
        if (!list.isEmpty()) {
            FmbizAndIssue fb = list.get(0);//拿到保存的延迟记录
            fb.setGroupId(fmBiz.getGroupId());
            String issuefxId = fmBiz.getParams().get("issuefxId").toString();
            fb.setIssuefxId(issuefxId);
            fb.setState("0");
            fb.setOperationId(ShiroUtils.getUserId());
            fb.setOperationTime(DateUtils.dateTimeNow());
            updateFmbizAndIssue(fb);
            savePubRelation(fb);
        } else {
            throw new BusException("该事件单不可再关联问题单，请刷新列表后操作。");
        }
        return AjaxResult.success("关联问题单成功");
    }

    public void savePubRelation(FmbizAndIssue fb) {
        //添加绑定关系方便事件单展示关联问题单
        PubRelation pr = new PubRelation();
        pr.setType("14");
        pr.setObj1Id(fb.getFmId());
        pr.setObj2Id(fb.getIssuefxId());
        pr.setRelationId(UUID.getUUIDStr());
        iPubRelationService.insertPubRelation(pr);
    }

    @Override
    public void deleteFmbizAndIssueByFmId(String fmId) {
        //判断是不是有记录 有就删除
        FmbizAndIssue fai = new FmbizAndIssue();
        fai.setFmId(fmId);
        List<FmbizAndIssue> list = selectFmbizAndIssueList(fai);//拿到保存的记录
        if (!list.isEmpty()) {
            if ("0".equals(list.get(0).getState())) {//如果是已绑定的需要删除关联关系
                iPubRelationService.deletePubRelationByFmId(fmId);
            }
            deleteFmbizAndIssueById(list.get(0).getId());
        }
    }

    public List<FmbizAndIssue> getFmBizAndIssueByDay(int day) {
        String time = DateUtils.dateTimeNow();
        String day5 = DateUtils.getday5(time, day);
        return fmbizAndIssueMapper.getFmBizAndIssueByDay(day5);
    }

    @Override
    public void relationIssueOne(FmBiz fmBiz) {
        String fmId = fmBiz.getFmId();
        FmbizAndIssue fai = new FmbizAndIssue();
        fai.setFmId(fmId);
        fai.setState("0");
        List<FmbizAndIssue> list = selectFmbizAndIssueList(fai);
        if (list.isEmpty()) {
            String issuefxId = knowledgeBusinessContentService.selectIssuefxIdForKnowledgeId(fmBiz.getKnowledgeId()).getIssuefxId();
            FmbizAndIssue fb = new FmbizAndIssue();
            fb.setGroupId(fmBiz.getGroupId());
            fb.setIssuefxId(issuefxId);
            fb.setState("0");
            fb.setFmId(fmId);
            fb.setOperationId(ShiroUtils.getUserId());
            fb.setOperationTime(DateUtils.dateTimeNow());
            fb.setId(UUID.getUUIDStr());
            insertFmbizAndIssue(fb);
            savePubRelation(fb);
        } else {
            throw new BusException("该事件单已关联问题单，请刷新列表后操作。");
        }
    }

    @Override
    public List<FmbizAndIssue> getRelationByDay(String day) {
        String time = DateUtils.dateTimeNow();
        String day5 = DateUtils.getday5(time, Integer.parseInt(day));
        return fmbizAndIssueMapper.getRelationByDay(day5);
    }

    /**
     * 通过问题单ID来查询相关时间内的事件单
     *
     * @param fmbizAndIssue
     * @return 结果
     */
    @Override
    public List<FmbizAndIssue> selectFmbizIdList(FmbizAndIssue fmbizAndIssue) {
        return fmbizAndIssueMapper.selectFmbizIdList(fmbizAndIssue);
    }

    @Override
    public AjaxResult editRelationIssue(FmBiz fmBiz) {
        String fmId = fmBiz.getFmId();
        iPubRelationService.deletePubRelationByFmId(fmId);//刪除待关联展示关联信息
        FmbizAndIssue fb = new FmbizAndIssue();
        fb.setFmId(fmId);
        List<FmbizAndIssue> list = selectFmbizAndIssueList(fb);
        if (!list.isEmpty()) {
            FmbizAndIssue fbi = list.get(0);
            String issuefxId = fmBiz.getParams().get("issuefxId").toString();
            fbi.setIssuefxId(issuefxId);//问题单ID
            fbi.setOperationTime(DateUtils.dateTimeNow());//关联时间
            updateFmbizAndIssue(fbi);
            savePubRelation(fbi);
        } else {
            throw new BusinessException("该事件单未关联问题单，请刷新列表操作。");
        }

        return AjaxResult.success("关联问题单成功");
    }
}
