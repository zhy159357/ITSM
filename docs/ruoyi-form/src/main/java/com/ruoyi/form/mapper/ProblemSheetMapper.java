package com.ruoyi.form.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.ProblemSheet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题单Mapper接口
 * 
 * @author ruoyi
 * @date 2022-06-21
 */
public interface ProblemSheetMapper extends BaseMapper<ProblemSheet>
{
    /**
     * 查询问题单
     * 
     * @param problemId 问题单ID
     * @return 问题单
     */
    public ProblemSheet selectProblemSheetById(@Param("problemId") String problemId);

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
     * 删除问题单
     * 
     * @param problemId 问题单ID
     * @return 结果
     */
    public int deleteProblemSheetById(String problemId);

    /**
     * 批量删除问题单
     * 
     * @param problemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProblemSheetByIds(String[] problemIds);
}
