package com.ruoyi.form.service;

import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.form.domain.EventSheet;
import com.ruoyi.form.domain.ProblemSheet;
import java.util.List;
import java.util.Map;


/**
 * 问题单Service接口
 * 
 * @author ruoyi
 * @date 2022-06-21
 */
public interface IProblemSheetService 
{
    /**
     * 查询问题单
     * 
     * @param problemId 问题单ID
     * @return 问题单
     */
    public ProblemSheet selectProblemSheetById(String problemId);

    /**
     * 查询问题单列表
     * 
     * @param problemSheet 问题单
     * @return 问题单集合
     */
    public List<ProblemSheet> selectProblemSheetList(ProblemSheet problemSheet);

    /**
     * 新增问题单
     * 
     * @param problemSheet 问题单
     * @return 结果
     */
    public int insertProblemSheet(ProblemSheet problemSheet);

    /**
     * 修改问题单
     * 
     * @param problemSheet 问题单
     * @return 结果
     */
    public int updateProblemSheet(ProblemSheet problemSheet);

    /**
     * 批量删除问题单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProblemSheetByIds(String ids);

    /**
     * 删除问题单信息
     * 
     * @param problemId 问题单ID
     * @return 结果
     */
    public int deleteProblemSheetById(String problemId);

    BmpEntity startProcess(String problemId, Map<String, Object> variables);

    /**
     * 查询待办事项
     *
     * @param imp
     * @param description
     * @param problemSheet
     * @return
     */
    List<ProblemSheet> selectProblemAgencyList(String imp, String description, ProblemSheet problemSheet);

    /**
     * 取消问题单
     *
     * @param id
     */
    void cancelProblemSheetById(String id);

    /**
     * 问题单分派
     *
     * @param problemSheet
     */
    void assignProblem(ProblemSheet problemSheet);

    /**
     * 接收问题单
     *
     * @param problemSheet
     */
    void receiveProblem(ProblemSheet problemSheet);

    /**
     * 处理问题单
     *
     * @param problemSheet
     */
    void dealProblem(ProblemSheet problemSheet);

    /**
     * 解决问题单
     *
     * @param problemSheet
     */
    void solutionProblem(ProblemSheet problemSheet);

    /**
     * 问题单确认
     *
     * @param problemSheet
     */
    void confirmProblem(ProblemSheet problemSheet);

    /**
     * 关闭问题单
     *
     * @param id
     */
    void closeProblem(String id);

    /**
     * 问题单退回
     *
     * @param problemSheet
     */
    void returnProblem(ProblemSheet problemSheet);

    /**
     * 级联数据转换
     *
     * @param problemSheet
     */
    void translateCatType(ProblemSheet problemSheet);
}
