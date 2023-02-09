package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ImBizDataq;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2021-09-28
 */
public interface IImBizDataqService {
    /**
     * 查询【请填写功能名称】
     *
     * @param imId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ImBizDataq selectImBizDataqById(String imId);


    /**
     * 新增
     *
     * @param imBizDataq
     * @return 结果
     */
    public int insertImBizDataq(ImBizDataq imBizDataq);

    /**
     * 修改【请填写功能名称】
     *
     * @param imBizDataq 【请填写功能名称】
     * @return 结果
     */
    public int updateImBizDataq(ImBizDataq imBizDataq);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteImBizDataqByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param imId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteImBizDataqById(String imId);

    /**
     * 修改页面
     *
     * @param imId
     * @param mmap
     * @return
     */
    ModelMap view(String imId, ModelMap mmap);

    /**
     * 修改页面
     *
     * @param imId
     * @param mmap
     * @return
     */
    ModelMap editView(String imId, ModelMap mmap);

    /**
     * 新增页面
     *
     * @return
     */
    ModelMap addview(ModelMap modelMap);

    /**
     * 数据变更单新增时进入修改页面
     *
     * @param modelMap
     * @return
     */
    ModelMap xgsjwt(String imNo, ModelMap modelMap);

    /**
     * 根据单号查询
     *
     * @param imNo
     * @return
     */
    public ImBizDataq selectImBizDataqByNo(String imNo);

    /**
     * 根据Id查询数据问题单集合
     *
     * @param id
     * @return
     */
    List<ImBizDataq> selectImBizDataqListById(String id);

    /**
     * 查询我的问题单列表
     *
     * @param imBizDataq 传入对象
     * @return 【返回匹配到的结果集
     */
    public List<ImBizDataq> selectImBizDataqList(ImBizDataq imBizDataq);

    /**
     * 查询问题单列表
     *
     * @param imBizDataq 传入对象
     * @return 【返回匹配到的结果集
     */
    public List<ImBizDataq> pageList(ImBizDataq imBizDataq);

    /**
     * 导出数据问题单
     *
     * @param imBizDataq 支持根据传入参数查询结果导出
     * @return 查询到的结果集
     */
    public List<ImBizDataq> exprot(ImBizDataq imBizDataq);

    /**
     * 提交数据问题单
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult addSave(ImBizDataq imBizDataq);

    /**
     * 总行业务部门审核打开修改问题单页面
     *
     * @param imNo
     * @param modelMap
     * @return
     */
    ModelMap xgsjwtsh(String imNo, ModelMap modelMap);


    /**
     * 数据问题单过滤业务部门数据
     *
     * @param
     * @return
     */
    public List<Map> getBusinessList(ImBizDataq imBizDataq);

    /**
     * 查询待办任务
     *
     * @param imBizDataq
     * @return
     */
    public List<ImBizDataq> getBenchTaskList(ImBizDataq imBizDataq);

    /**
     * 待评估任务处理
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveflowAssessor(ImBizDataq imBizDataq);

    /**
     * 受理任务处理
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveAccept(ImBizDataq imBizDataq);

    /**
     * 待分发/关闭任务处理
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveflowCloseOrFenFa(ImBizDataq imBizDataq);
    /**
     * 解决任务处理
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveFlowJj(ImBizDataq imBizDataq);
    /**
     * 转发任务处理
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveFlowZf(ImBizDataq imBizDataq);
    /**
     * 受理退回任务处理
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveFlowTuihui(ImBizDataq imBizDataq);

    /**
     * 审批页面数据问题单提交
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult addSjwtSh(ImBizDataq imBizDataq);

    /**
     * 批量分发问题单
     *
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveFlowPlFenFa(ImBizDataq imBizDataq);

    /**
     * 数据变更单关联数据问题单展示页面
     * @param imBizDataq
     * @return
     */
    List<ImBizDataq> pageListBg(ImBizDataq imBizDataq);

    /**
     * 我的数据问题单修改保存数据问题单
     * @param imBizDataq
     * @return
     */
    public AjaxResult saveOrCancellation(ImBizDataq imBizDataq);


}