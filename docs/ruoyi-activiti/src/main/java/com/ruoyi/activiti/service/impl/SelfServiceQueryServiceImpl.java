package com.ruoyi.activiti.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.mapper.SelfServiceQueryMapper;
import com.ruoyi.activiti.service.IFmBizScriptService;
import com.ruoyi.activiti.service.ISelfServiceQueryService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBizScript;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.SelfServiceQuery;
import com.ruoyi.common.sign.aop.ApiSignValid;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-05
 */
@Service
public class SelfServiceQueryServiceImpl implements ISelfServiceQueryService {
    @Autowired
    private SelfServiceQueryMapper selfServiceQueryMapper;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private IFmBizScriptService iFmBizScriptService;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SelfServiceQuery selectSelfServiceQueryById(String id) {
        return selfServiceQueryMapper.selectSelfServiceQueryById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SelfServiceQuery> selectSelfServiceQueryList(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryMapper.selectSelfServiceQueryList(selfServiceQuery);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSelfServiceQuery(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryMapper.insertSelfServiceQuery(selfServiceQuery);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param selfServiceQuery 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSelfServiceQuery(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryMapper.updateSelfServiceQuery(selfServiceQuery);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSelfServiceQueryByIds(String ids) {
        return selfServiceQueryMapper.deleteSelfServiceQueryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSelfServiceQueryById(String id) {
        return selfServiceQueryMapper.deleteSelfServiceQueryById(id);
    }

    @Override
    public List<SelfServiceQuery> selectSelfServiceQueryMyList(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryMapper.selectSelfServiceQueryMyList(selfServiceQuery);
    }

    @Override
    public AjaxResult addSave(SelfServiceQuery selfServiceQuery) {
        SelfServiceQuery ssq = formatTime(selfServiceQuery);
        ssq.setState("0");
        ssq.setDeadline(DateUtils.handleTimeYYYYMMDDHHMMSS(DateUtils.addHourMinute(new Date(), 0, 20)));

        if (StringUtils.isNotEmpty(ssq.getId())) {
            updateSelfServiceQuery(ssq);
        } else {
            ssq.setId(UUID.getUUIDStr());
            insertSelfServiceQuery(ssq);
        }
        return AjaxResult.success("提交申请单成功", ssq.getId());
    }

    @Override
    public AjaxResult saveDesc(SelfServiceQuery selfServiceQuery) {
        SelfServiceQuery ssq = formatTime(selfServiceQuery);
        ssq.setState("1");
        if (StringUtils.isNotEmpty(ssq.getId())) {
            updateSelfServiceQuery(ssq);
        } else {
            ssq.setId(UUID.getUUIDStr());
            insertSelfServiceQuery(ssq);
        }
        return AjaxResult.success("暂存申请单成功", ssq.getId());
    }

    public SelfServiceQuery formatTime(SelfServiceQuery selfServiceQuery) {
        selfServiceQuery.setCreaterTime(DateUtils.handleTimeYYYYMMDDHHMMSS(selfServiceQuery.getCreaterTime()));
        if (StringUtils.isNotEmpty(selfServiceQuery.getOccurrenceTime())) {
            selfServiceQuery.setOccurrenceTime(DateUtils.handleTimeYYYYMMDDHHMMSS(selfServiceQuery.getOccurrenceTime()));
        }
        return selfServiceQuery;
    }

    public List<SelfServiceQuery> selectSelfList(SelfServiceQuery selfServiceQuery) {
        return selfServiceQueryMapper.selectSelfList(selfServiceQuery);
    }

    @Override
    public SelfServiceQuery formatSelf(SelfServiceQuery selfServiceQuery) {
        Map<String, Object> reMap = new HashMap<>();
        String createOrgId = selfServiceQuery.getCreateOrgId();
        if (StringUtils.isNotEmpty(createOrgId)) {
            OgOrg org = iSysDeptService.selectDeptById(createOrgId);
            reMap.put("levelCode", org.getLevelCode());
        }
        if (selfServiceQuery.getParams().containsKey("startCreatTime")) {
            String startCreatTime = (String) selfServiceQuery.getParams().get("startCreatTime");
            if (StringUtils.isNotEmpty(startCreatTime)) {
                String d1 = DateUtils.handleTimeYYYYMMDD(startCreatTime);
                reMap.put("startCreatTime", d1 + "000000");
            }
        }
        if (selfServiceQuery.getParams().containsKey("endCreatTime")) {
            String endCreatTime = selfServiceQuery.getParams().get("endCreatTime").toString();
            if (StringUtils.isNotEmpty(endCreatTime)) {
                String d2 = DateUtils.handleTimeYYYYMMDD(endCreatTime);
                reMap.put("endCreatTime", d2 + "240000");
            }
        }
        if (StringUtils.isNotEmpty(selfServiceQuery.getState())) {
            String[] state = selfServiceQuery.getState().split(",");
            reMap.put("state", state);
        }
        selfServiceQuery.setParams(reMap);
        return selfServiceQuery;
    }

    @Override
    public List<SelfServiceQuery> selectSelfServiceQueryCloseList(String deadline) {
        return selfServiceQueryMapper.selectSelfServiceQueryCloseList(deadline);
    }

    @Override
    @Transactional
    public String saveToAuto(String id, ModelMap mmap) {
        //根据系统编码调用自动化接口获取脚本信息并返回页面展示
        SelfServiceQuery selfServiceQuery = selectSelfServiceQueryById(id);
        long startTime = DateUtils.parseDate(DateUtils.timeToTimeMillis(selfServiceQuery.getDeadline()), "yyyy-MM-dd HH:mm:ss").getTime();
        long Endtime = DateUtils.getNowDate().getTime();
        if (startTime > Endtime) {
            long minute = (((startTime - Endtime) % 86400000) % 3600000) / 60000;
            long second = ((((startTime - Endtime) % 86400000) % 3600000) % 60000) / 1000;
            mmap.put("minute", minute);
            mmap.put("second", second);
        } else {
            completeSelf(selfServiceQuery);
            mmap.put("minute", "00");
            mmap.put("second", "00");
        }
        mmap.put("selfServiceQuery", selfServiceQuery);
        return "selfServiceQuery/subpage/auto";
    }

    @Override
    @Transactional
    public void completeSelf(SelfServiceQuery selfServiceQuery) {
        selfServiceQuery.setState("2");
        FmBizScript fbs = new FmBizScript();
        fbs.setFmId(selfServiceQuery.getId());
        List<FmBizScript> list = iFmBizScriptService.selectFmBizScriptList(fbs);
        selfServiceQuery.setN1(Integer.toString(list.size()));
        updateSelfServiceQuery(selfServiceQuery);
    }

    @Override
    @Transactional
    public String saveToAutoEdit(String id, ModelMap mmap) {
        //根据系统编码调用自动化接口获取脚本信息并返回页面展示
        SelfServiceQuery selfServiceQuery = selectSelfServiceQueryById(id);
        long startTime = DateUtils.parseDate(DateUtils.timeToTimeMillis(selfServiceQuery.getDeadline()), "yyyy-MM-dd HH:mm:ss").getTime();
        long Endtime = DateUtils.getNowDate().getTime();
        if (startTime > Endtime) {
            long minute = (((startTime - Endtime) % 86400000) % 3600000) / 60000;
            long second = ((((startTime - Endtime) % 86400000) % 3600000) % 60000) / 1000;
            mmap.put("minute", minute);
            mmap.put("second", second);
        } else {
            completeSelf(selfServiceQuery);
            mmap.put("minute", "00");
            mmap.put("second", "00");
        }
        mmap.put("selfServiceQuery", selfServiceQuery);
        return "selfServiceQuery/subpage/autoEdit";
    }
}
