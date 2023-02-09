package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.FmBizJJAppr;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizCDealerD;
import com.ruoyi.common.core.domain.entity.FmSysDTime;
import com.ruoyi.common.core.domain.entity.FmbizAndIssue;

import java.util.List;

/**
 * 业务事件单Mapper接口
 *
 * @author ruoyi
 * @date 2020-12-23
 */
public interface FmBizMapper {
    /**
     * 查询业务事件单
     *
     * @param fmId 业务事件单ID
     * @return 业务事件单
     */
    public FmBiz selectFmBizById(String fmId);

    /**
     * 查询业务事件单
     *
     * @param faultNo 业务事件单编号
     * @return 业务事件单
     */
    public FmBiz selectFmBizByFaultNo(String faultNo);

    /**
     * 查询业务事件单列表
     *
     * @param fmBiz 业务事件单
     * @return 业务事件单集合
     */
    public List<FmBiz> selectFmBizList(FmBiz fmBiz);

    /**
     * 新增业务事件单
     *
     * @param fmBiz 业务事件单
     * @return 结果
     */
    public int insertFmBiz(FmBiz fmBiz);

    /**
     * 修改业务事件单
     *
     * @param fmBiz 业务事件单
     * @return 结果
     */
    public int updateFmBiz(FmBiz fmBiz);

    /**
     * 删除业务事件单
     *
     * @param fmId 业务事件单ID
     * @return 结果
     */
    public int deleteFmBizById(String fmId);

    /**
     * 批量删除业务事件单
     *
     * @param fmIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizByIds(String[] fmIds);

    /**
     * 查询我的事件单
     *
     * @param fmBiz 当前登录人ID
     * @return 结果
     */
    public List<FmBiz> selectMylist(FmBiz fmBiz);

    /**
     * 查询事件单
     */
    public List<FmBiz> selectFmBizListPager(FmBiz fmBiz);

    /**
     * 工作组分析查询
     */
    public List<FmSysDTime> selectGroupAnalysisList(FmBiz fmBiz);

    /**
     * 工作组督办查询
     */
    public List<FmBiz> selectGroupHandleList(FmBiz fmBiz);

    /**
     * 查看紧急事件单统计
     */
    public List<FmBizJJAppr> selectFmBizCount(FmBiz fmBiz);
    public List<FmBizJJAppr> selectFmBizCountMysql(FmBiz fmBiz);

    /**
     * 代办查询专用
     */
    public FmBiz getFlowFmBiz(FmBiz fmBiz);

    /**
     * 查询人员处理数量根据机构
     */
    public List<FmBizCDealerD> getDealerDCount1(FmBiz fmBiz);
    public List<FmBizCDealerD> getDealerDCount1Mysql(FmBiz fmBiz);

    /**
     * 查询人员处理数量根据工作组
     */
    public List<FmBizCDealerD> getDealerDCount2(FmBiz fmBiz);
    public List<FmBizCDealerD> getDealerDCount2Mysql(FmBiz fmBiz);

    /**
     * 查询满足自动关闭的事件单
     */
    public List<FmBiz> getPowerOffFmYw(String day);

    /**
     * 查询未处理事件单数量（按系统）
     */
    public List<FmBiz> getSysNDealCount(String sysid);

    /**
     * 查询发送给反欺诈失败的事件单
     *
     * @return
     */
    public List<FmBiz> getFmBizByRequestFail();

    public List<FmBiz> getCompletedFmBizByDate(String endTime);

    public List<FmBiz> getCompletedFmBizByDate2(String endTime);


    /**
     * 根据id更新有效无效标识
     *
     * @param fmId
     * @return
     */
    int updateFmBizByInvalidationMark(String fmId);

    /**
     * 根据工作组分组查询未完成的事件单
     */
    List<FmBizCDealerD> getGroupCount();

    public List<FmBiz> selectFmBizListByMonitor(FmBiz fmBiz);

    public List<FmBiz> getFmBizByAll(List<String> list);

    public List<FmBiz> getFmBizAndIssueList(FmBiz fmBiz);

    /**
     * 通过问题单ID来查询相关时间内的事件单
     *
     * @param fmbizAndIssue
     * @return 结果
     */
    public List<FmBiz> selectFmbizIdList(FmbizAndIssue fmbizAndIssue);

    public List<FmBiz> getFmBizByFlowLogCount(int order);

    public List<FmBiz> getFmBizByToQgzxTime(String toQgzxTime);

    public List<FmBiz> getFmBizAndIssueEdit(FmBiz fmBiz);
}
