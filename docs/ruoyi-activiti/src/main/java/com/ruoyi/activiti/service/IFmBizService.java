package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.FmBizJJAppr;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizCDealerD;
import com.ruoyi.common.core.domain.entity.FmSysDTime;
import com.ruoyi.common.core.domain.entity.OgPerson;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * 业务事件单Service接口
 *
 * @author ruoyi
 * @date 2020-12-23
 */
public interface IFmBizService {
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
     * 批量删除业务事件单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizByIds(String ids);

    /**
     * 删除业务事件单信息
     *
     * @param fmId 业务事件单ID
     * @return 结果
     */
    public int deleteFmBizById(String fmId);

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
     * 查看紧急事件单
     */
    public List<FmBizJJAppr> selectFmBizCount(FmBiz fmBiz);

    /**
     * 代办查询专用
     */
    public FmBiz getFlowFmBiz(FmBiz fmBiz);

    /**
     * 查询人员处理数量根据机构
     */
    public List<FmBizCDealerD> getDealerDCount1(FmBiz fmBiz);

    /**
     * 查询人员处理数量根据工作组
     */
    public List<FmBizCDealerD> getDealerDCount2(FmBiz fmBiz);

    /**
     * 查询满足自动关闭的事件单
     */
    public List<FmBiz> getPowerOffFmYw(int day);

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

    /**
     * 查询当月关闭事件单
     */
    public List<FmBiz> getCompletedFmBizByDate(String endTime);

    public List<FmBiz> getCompletedFmBizByDate2(String endTime);

    /**
     * 根据id更新有效无效标识
     *
     * @param id
     * @return
     */

    int updateFmBizByInvalidationMark(String id);

    /**
     * 根据工作组分组查询未完成的事件单
     */
    List<FmBizCDealerD> getGroupCount();

    /**
     * 查询事件单列表（非数据变更单相关的）
     *
     * @param fmBiz
     * @return
     */
    public List<FmBiz> selectFmBizListByMonitor(FmBiz fmBiz);

    /**
     * 根据传入事件单对应格式化为查询初始化条件返回
     *
     * @param fmBiz
     * @return
     */
    public FmBiz formatFmbiz(FmBiz fmBiz);

    public void savebacktoYnSj(FmBiz f, String pId);

    /**
     * 判断事件来源是电话银行还是信用卡传入的事件单
     * 发送信息给对应系统
     *
     * @param fmBiz
     * @param dealResult 处理方式 1处理 0退回
     * @param obj        处理意见
     */
    public void sendPhoneBankOrCard(FmBiz fmBiz, String dealResult, Object obj);

    /**
     * 根据传入事件单信息分装JSON发送反欺诈系统
     *
     * @param f
     * @param url
     */
    public void sendAntifraud(FmBiz f, String url);

    /**
     * 根据传入事件单对象计算该事件单处理时间
     *
     * @param f
     */
    public void calculationDealTime(FmBiz f);

    public void sendSms(String setSmsText, OgPerson person);

    public void ifJJToSendMessage(String flag, String groupId, String sysId, String faultNo);

    public void sendMessageToApplication(String faultNo, String sysname);

    public void ifJJSendMessage(String groupId, String sysId, String faultNo);

    public List<FmBiz> getFmBizByAll(List<String> list);

    public ModelMap getTypeList(FmBiz fmbiz, ModelMap mmap);

    /**
     * 评价未解决退回事件单
     *
     * @param fmBiz
     * @return
     */
    public AjaxResult saveflowremoveRecord(FmBiz fmBiz);

    /**
     * 根据传入参数查询待关联问题的事件单
     *
     * @param fmBiz
     * @return
     */
    public List<FmBiz> getFmBizAndIssueList(FmBiz fmBiz);

    /**
     * 判断知识是否关联了问题
     */
    public String ifKnowledgeRelationProblem(FmBiz fmBiz);

    public AjaxResult getFmBizOneLineApi(Map<String, String> map);

    public void pushOneLine(FmBiz fmBiz);

    /**
     * 导出待关联问题的事件单
     *
     * @param fmBiz
     * @param flag  1 当前页 2 全部数据
     * @return
     */
    public List<FmBiz> getFmBizAndIssueListExport(FmBiz fmBiz, String flag);

    /**
     * 查询流程步骤超过20的事件单
     *
     * @param order 步骤数
     * @return
     */
    public List<FmBiz> getFmBizByFlowLogCount(int order);

    /**
     * 根据全国中心时间获取小于该时间的事件单
     *
     * @param toQgzxTime 全国中心时间
     * @return
     */
    public List<FmBiz> getFmBizByToQgzxTime(String toQgzxTime);

    /**
     * 组内转发
     *
     * @param fmBiz
     * @return
     */
    public AjaxResult saveflowrepeatIn(FmBiz fmBiz);

    public void ifIdToDealId(String id1, String id2);

    public List<FmBiz> compareList(List<FmBiz> resultlist);

    public List<FmBiz> compareList2(List<FmBiz> resultlist);

    /**
     * 查询已关联的问题单
     * @param fmBiz
     * @return
     */
    public List<FmBiz> getFmBizAndIssueEdit(FmBiz fmBiz);

    public List<OgPerson> getPersonByTransfer(FmBiz fmBiz);

    public AjaxResult saveflowTransfer(FmBiz fmBiz);

}
