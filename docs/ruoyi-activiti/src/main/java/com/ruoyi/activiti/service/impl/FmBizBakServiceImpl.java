package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.activiti.mapper.FmBizBakMapper;
import com.ruoyi.activiti.service.IFmBizBakService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-14
 */
@Service
public class FmBizBakServiceImpl implements IFmBizBakService {
    @Autowired
    private FmBizBakMapper fmBizBakMapper;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysWorkService iSysWorkService;

    /**
     * 查询【请填写功能名称】
     *
     * @param fmId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public FmBizBak selectFmBizBakById(String fmId) {
        return fmBizBakMapper.selectFmBizBakById(fmId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param fmBizBak 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FmBizBak> selectFmBizBakList(FmBizBak fmBizBak) {
        return fmBizBakMapper.selectFmBizBakList(fmBizBak);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param fmBizBak 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFmBizBak(FmBizBak fmBizBak) {
        return fmBizBakMapper.insertFmBizBak(fmBizBak);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param fmBizBak 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFmBizBak(FmBizBak fmBizBak) {
        return fmBizBakMapper.updateFmBizBak(fmBizBak);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizBakByIds(String ids) {
        return fmBizBakMapper.deleteFmBizBakByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param fmId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteFmBizBakById(String fmId) {
        return fmBizBakMapper.deleteFmBizBakById(fmId);
    }

    @Override
    public List<FmBizBak> selectFmBizBakListPager(FmBizBak fmBizBak) {
        return fmBizBakMapper.selectFmBizBakListPager(fmBizBak);
    }

    @Override
    public FmBizBak formatFmbizBak(FmBizBak fmBizBak) {
        String startCreatTime = fmBizBak.getParams().get("startCreatTime").toString();
        String endCreatTime = fmBizBak.getParams().get("endCreatTime").toString();
        String startDealTime = fmBizBak.getParams().get("startDealTime").toString();
        String endDealTime = fmBizBak.getParams().get("endDealTime").toString();
        String startToQgzxTime = fmBizBak.getParams().get("startToQgzxTime").toString();
        String endToQgzxTime = fmBizBak.getParams().get("endToQgzxTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            String d1 = DateUtils.handleTimeYYYYMMDD(startCreatTime);
            fmBizBak.getParams().put("startCreatTime", d1 + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            String d2 = DateUtils.handleTimeYYYYMMDD(endCreatTime);
            fmBizBak.getParams().put("endCreatTime", d2 + "240000");
        }
        if (StringUtils.isNotEmpty(startDealTime)) {
            String d3 = DateUtils.handleTimeYYYYMMDD(startDealTime);
            fmBizBak.getParams().put("startDealTime", d3 + "000000");
        }
        if (StringUtils.isNotEmpty(endDealTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(endDealTime);
            fmBizBak.getParams().put("endDealTime", d4 + "240000");
        }
        if (StringUtils.isNotEmpty(startToQgzxTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(startToQgzxTime);
            fmBizBak.getParams().put("startToQgzxTime", d4 + "000000");
        }
        if (StringUtils.isNotEmpty(endToQgzxTime)) {
            String d4 = DateUtils.handleTimeYYYYMMDD(endToQgzxTime);
            fmBizBak.getParams().put("endToQgzxTime", d4 + "240000");
        }
        if (StringUtils.isNotEmpty(fmBizBak.getCurrentState())) {
            String[] CurrentState = fmBizBak.getCurrentState().split(",");
            fmBizBak.getParams().put("state", CurrentState);
        }
        if (StringUtils.isNotEmpty(fmBizBak.getFmCause())) {
            String[] fmCause = fmBizBak.getFmCause().split(",");
            fmBizBak.getParams().put("cause", fmCause);
        }
        if (StringUtils.isNotEmpty(fmBizBak.getDealMode())) {
            String[] dealModes = fmBizBak.getDealMode().split(",");
            fmBizBak.getParams().put("dealMode", dealModes);
        }
        if (StringUtils.isNotEmpty(fmBizBak.getOccurrenceOrgId())) {
            OgOrg org = iSysDeptService.selectDeptById(fmBizBak.getOccurrenceOrgId());
            fmBizBak.getParams().put("levelCode", org.getLevelCode());
        }
        if (StringUtils.isNotEmpty(fmBizBak.getParticipatorIds())) {
            OgPerson p = new OgPerson();
            p.setpName(fmBizBak.getParticipatorIds());
            List<OgPerson> pList = iOgPersonService.selectOgPersonList(p);
            if (!pList.isEmpty()) {
                List<String> list = new ArrayList<>();
                for (OgPerson gp : pList) {
                    list.add(gp.getpId());
                }
                fmBizBak.getParams().put("pList", list);
            }
        }
        //默认查询规则1、全国中心除厂商外其他人看所有2、厂商看自己工作组参与的事件单3、省内看自己省创建的
        OgUser u = ShiroUtils.getOgUser();
        OgOrg o = iSysDeptService.selectDeptById(iOgPersonService.selectOgPersonEvoById(u.getpId()).getOrgId());
        String lvCode = o.getLevelCode();
        if ("/000000000/".equals(lvCode)) {
            // 邮政金融运维人员，查看全部
        } else if (lvCode.startsWith("/000000000/010000888/")) {
            // 全国中心，查看全部
            if (lvCode.startsWith("/000000000/010000888/010900888/")) {
                List<OgGroup> group = iSysWorkService.selectGroupByUserId(u.getUserId());
                if (!group.isEmpty()) {
                    List<String> list = new ArrayList<>();
                    for (OgGroup gp : group) {
                        list.add(gp.getGroupId());
                    }
                    fmBizBak.getParams().put("sGroupid", list);
                }
            }
        } else {
            fmBizBak.getParams().put("sorgId", o.getOrgId());
        }
        return fmBizBak;
    }
}
