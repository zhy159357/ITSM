package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.activiti.mapper.DifficultEventsMapper;
import com.ruoyi.activiti.service.IDifficultEventsService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-23
 */
@Service
public class DifficultEventsServiceImpl implements IDifficultEventsService {

    private static final Logger log = LoggerFactory.getLogger(DifficultEventsServiceImpl.class);

    private final static String STATUS_PENDING = "01";
    private final static String STATUS_PROCESSED = "02";
    private final static String NO_TYPE = "YNSJ";

    @Autowired
    private DifficultEventsMapper difficultEventsMapper;
    @Autowired
    private IIdGeneratorService iIdGeneratorService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private IOgGroupPersonService iOgGroupPersonService;
    @Autowired
    private IPubAttachmentService iPubAttachmentService;
    @Autowired
    private IFmBizService iFmBizService;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DifficultEvents selectDifficultEventsById(String id) {
        return difficultEventsMapper.selectDifficultEventsById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param difficultEvents 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DifficultEvents> selectDifficultEventsList(DifficultEvents difficultEvents) {
        DifficultEvents de = formatObject(difficultEvents);
        return difficultEventsMapper.selectDifficultEventsList(de);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param difficultEvents 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDifficultEvents(DifficultEvents difficultEvents) {
        return difficultEventsMapper.insertDifficultEvents(difficultEvents);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param difficultEvents 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDifficultEvents(DifficultEvents difficultEvents) {
        return difficultEventsMapper.updateDifficultEvents(difficultEvents);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDifficultEventsByIds(String ids) {
        return difficultEventsMapper.deleteDifficultEventsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDifficultEventsById(String id) {
        return difficultEventsMapper.deleteDifficultEventsById(id);
    }

    public DifficultEvents formatObject(DifficultEvents difficultEvents) {
        String startCreatTime = (String) difficultEvents.getParams().get("startCreatTime");
        String endCreatTime = (String) difficultEvents.getParams().get("endCreatTime");
        String startDealTime = (String) difficultEvents.getParams().get("startDealTime");
        String endDealTime = (String) difficultEvents.getParams().get("endDealTime");
        if (StringUtils.isNotEmpty(startCreatTime)) {
            difficultEvents.getParams().put("startCreatTime", DateUtils.formatStartTime(startCreatTime));
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            difficultEvents.getParams().put("endCreatTime", DateUtils.formatEndTime(endCreatTime));
        }
        if (StringUtils.isNotEmpty(startDealTime)) {
            difficultEvents.getParams().put("startDealTime", DateUtils.formatStartTime(startDealTime));
        }
        if (StringUtils.isNotEmpty(endDealTime)) {
            difficultEvents.getParams().put("endDealTime", DateUtils.formatEndTime(endDealTime));
        }
        return difficultEvents;
    }

    @Override
    public int insertDifficultEventsByFmBiz(FmBiz fmBiz, String source) {
        DifficultEvents diffs = new DifficultEvents();
        String fmId = fmBiz.getFmId();
        diffs.setFmId(fmId);
        List<DifficultEvents> diffList = selectDifficultEventsList(diffs);
        if (!diffList.isEmpty()) {
            if ("1".equals(source)) {
                throw new BusinessException("该事件单已转过疑难事件单。");
            } else {
                log.error("事件单号为：" + fmBiz.getFaultNo() + "的事件单已转疑难事件无法再次创建。");
                return 0;
            }
        }
        FmBiz fb = new FmBiz();
        fb.setFmId(fmId);
        fb.setIfYn("1");
        iFmBizService.updateFmBiz(fb);
        DifficultEvents difficultEvents = new DifficultEvents();
        difficultEvents.setId(UUID.getUUIDStr());
        difficultEvents.setNo(iIdGeneratorService.createNoAsLength(NO_TYPE, 6));
        difficultEvents.setFmId(fmId);
        difficultEvents.setCreaterTime(DateUtils.dateTimeNow());
        difficultEvents.setGroupId(fmBiz.getGroupId());
        difficultEvents.setSource(source);
        difficultEvents.setSysId(fmBiz.getSysid());
        difficultEvents.setStatus(STATUS_PENDING);
        return difficultEventsMapper.insertDifficultEvents(difficultEvents);
    }

    @Override
    public List<DifficultEvents> getDealList(DifficultEvents difficultEvents) {
        OgGroupPerson ogGroupPerson = new OgGroupPerson();
        ogGroupPerson.setPid(ShiroUtils.getUserId());
        List<OgGroupPerson> list = iOgGroupPersonService.selectOgGroupPersonList(ogGroupPerson);//拿到登陆人所有的工作组
        if (!list.isEmpty()) {
            List<String> groupId = new ArrayList<>();
            for (OgGroupPerson groupPerson : list) {
                groupId.add(groupPerson.getGroupId());
            }
            difficultEvents.getParams().put("sGroupId", groupId);
        }
        difficultEvents.setStatus(STATUS_PENDING);
        return difficultEventsMapper.getDealList(formatObject(difficultEvents));
    }

    @Override
    public AjaxResult saveFlowDeal(DifficultEvents difficultEvents) {
        FmBiz fmBiz = iFmBizService.selectFmBizById(difficultEvents.getFmId());
        if (!"09".equals(fmBiz.getCurrentState())) {
            return AjaxResult.error("关联的事件单未关闭，无法处理疑难事件单。");
        }
        //判断当前登录人是否上传了会议纪要
        String userId = ShiroUtils.getUserId();
        //获取数据变更单ID
        String singleId = difficultEvents.getId();
        Attachment attachment = new Attachment();
        attachment.setOwnerId(singleId);
        attachment.setCreateId(userId);
        List<Attachment> list = iPubAttachmentService.selectAttachmentList(attachment);
        if (StringUtils.isEmpty(list)) {
            return AjaxResult.error("请提交会议纪要");
        }
        if ("01".equals(difficultEvents.getStatus())) {//判断是否未待处理状态
            DifficultEvents de = new DifficultEvents();
            de.setId(difficultEvents.getId());
            de.setDealDesc(difficultEvents.getDealDesc());
            de.setDealTime(DateUtils.dateTimeNow());
            OgPerson person = iOgPersonService.selectOgPersonById(userId);
            de.setDealId(userId);
            de.setDept(person.getOrgId());
            de.setStatus(STATUS_PROCESSED);
            updateDifficultEvents(de);
        } else {
            return AjaxResult.error("该疑难事件已被处理，请刷新列表后操作。");
        }
        return AjaxResult.success("疑难事件处理成功");
    }
}
