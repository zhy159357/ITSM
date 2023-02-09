package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.mapper.PubFlowLogMapper;
import com.ruoyi.activiti.service.ICmBizQingqiuService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程记录Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-25
 */
@Service
public class PubFlowLogServiceImpl implements IPubFlowLogService {
    @Autowired
    private PubFlowLogMapper pubFlowLogMapper;
    @Autowired
    private IVmBizInfoService iVmBizInfoService;
    @Autowired
    private ICmBizSingleService iCmBizSingleService;
    @Autowired
    private ICmBizQingqiuService iCmBizQingqiuService;

    /**
     * 查询流程记录
     *
     * @param logId 流程记录ID
     * @return 流程记录
     */
    @Override
    public PubFlowLog selectPubFlowLogById(String logId) {
        return pubFlowLogMapper.selectPubFlowLogById(logId);
    }

    /**
     * 查询流程记录列表
     *
     * @param pubFlowLog 流程记录
     * @return 流程记录
     */
    @Override
    public List<PubFlowLog> selectPubFlowLogList(PubFlowLog pubFlowLog) {
        return pubFlowLogMapper.selectPubFlowLogList(pubFlowLog);
    }

    /**
     * 新增流程记录
     *
     * @param pubFlowLog 流程记录
     * @return 结果
     */
    @Override
    public int insertPubFlowLog(PubFlowLog pubFlowLog) {
        return pubFlowLogMapper.insertPubFlowLog(pubFlowLog);
    }

    /**
     * 修改流程记录
     *
     * @param pubFlowLog 流程记录
     * @return 结果
     */
    @Override
    public int updatePubFlowLog(PubFlowLog pubFlowLog) {
        return pubFlowLogMapper.updatePubFlowLog(pubFlowLog);
    }

    /**
     * 删除流程记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePubFlowLogByIds(String ids) {
        return pubFlowLogMapper.deletePubFlowLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程记录信息
     *
     * @param logId 流程记录ID
     * @return 结果
     */
    @Override
    public int deletePubFlowLogById(String logId) {
        return pubFlowLogMapper.deletePubFlowLogById(logId);
    }

    /**
     * 查询流程记录biz_id
     *
     * @param
     * @return 流程记录集合
     */
    @Override
    public List<String> selectPubFlowLogBizList(PubFlowLog pubFlowLog) {
        return pubFlowLogMapper.selectPubFlowLogBizList(pubFlowLog);
    }

    @Override
    public List<PubFlowLog> selectPubFlowLogAll(String bizId) {
        List<PubFlowLog> list = pubFlowLogMapper.selectPubFlowLogAll(bizId);
        return list;
    }

    @Override
    public List<PubFlowLog> selectPubFlowLogAsc(String bizId) {
        List<PubFlowLog> list = pubFlowLogMapper.selectPubFlowLogAsc(bizId);
        return list;
    }

    @Override
    public List<PubFlowLog> selectPubFlowLogDesc(String bizId) {
        List<PubFlowLog> list = pubFlowLogMapper.selectPubFlowLogDesc(bizId);
        return list;
    }

    @Override
    public List<PubFlowLog> findFmIds(String performerTime) {
        return pubFlowLogMapper.findFmIds(performerTime);
    }

    @Override
    public List<Object> getAlreadyTasks(String pId, String logType, String search) {
        List<Object> resultList = new ArrayList<>();
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setPerformerId(pId);
        // 取一个月之前的时间字符串并格式化成 yyyyMMddHHmmss
        String oneMonthAgo = DateUtils.formatDate(DateUtils.addMonths(DateUtils.getNowDate(), -1), DateUtils.YYYYMMDDHHMMSS);
        pubFlowLog.setPerformerTime(oneMonthAgo);

        switch (logType) {
            case "VmBn":
                pubFlowLog.setLogType("VmBn");
                break;
            case "CmZy":
                pubFlowLog.setLogType("CmZy");
                break;
            case "BGQQ":
                pubFlowLog.setLogType("BGQQ");
                break;
            default:
                pubFlowLog.setLogType("NO_TYPE");
        }
        List<PubFlowLog> pubFlowLogs = selectPubFlowLogList(pubFlowLog);
        if (CollectionUtils.isEmpty(pubFlowLogs)) {
            return resultList;
        }
        List<String> bizIdList = pubFlowLogs.stream().map(PubFlowLog::getBizId).collect(Collectors.toList());
        resultList = getAlreadyList(bizIdList, logType, search);
        return resultList;
    }

    public List<Object> getAlreadyList(List<String> bizIdList, String logType, String search) {
        List<Object> resultList = new ArrayList<>();
        // 1000条查询一次
        int size = bizIdList.size();
        int num = size / 1000 + 1;
        int startIndex, endIndex;
        for (int i = 0; i < num; i++) {
            startIndex = i * 1000;
            int nextIndex = i * 1000 + 1000;
            endIndex = nextIndex > size ? size : nextIndex;
            if (startIndex >= endIndex) {
                break;
            }
            List<String> subList = bizIdList.subList(startIndex, endIndex);
            if ("VmBn".equals(logType)) {
                List<VmBizInfo> vmBizInfoList = iVmBizInfoService.selectVmBizInfoByIdList(subList);
                resultList.addAll(vmBizInfoList);
            } else if ("CmZy".equals(logType)) {
                CmBizSingle cbs = new CmBizSingle();
                cbs.getParams().put("subList", subList);
                cbs.setChangeCode(search);
                List<CmBizSingle> cmBizList = iCmBizSingleService.selectCmBizByIdList(cbs);
                resultList.addAll(cmBizList);
            } else if ("BGQQ".equals(logType)) {
                List<CmBizQingqiu> qingqiuList = iCmBizQingqiuService.selectQingQiuListApp(subList, search);
                resultList.addAll(qingqiuList);
            }
        }
        return resultList;
    }

    @Override
    public int selectPubFlowLogByBusinessKey(String businessKey) {
        return pubFlowLogMapper.selectPubFlowLogByBusinessKey(businessKey);
    }

    @Override
    public List<PubFlowLog> selectPubFlowLogEmpList(PubFlowLog pubFlowLog) {
        return pubFlowLogMapper.selectPubFlowLogEmpList(pubFlowLog);
    }
}
