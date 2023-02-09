package com.ruoyi.es.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.es.domain.*;

import java.util.List;

/**
 * @date 2021-03-12
 */
public interface KnowledgeContentService
{
    public List<KnowledgeContent> ContentList(KnowledgeContent knowledgeContent);

    public List<KnowledgeContent> ContentListAud(KnowledgeContent knowledgeContent);

    public KnowledgeContent selectContById(String id);

    public KnowledgeContent SearchStatus(String id);

    public int deleteContByIds(String ids);

    public List<Ztree> selectCategoryTree(KnowledgeCategory knowledgeCategory);

    /**
     *  查询树节点
     */
    public KnowledgeCategory selectCateById(String folder);
    /**
     * 新增
     * @param knowledgeCategory
     */
    public int insertContTree(KnowledgeCategory knowledgeCategory);

    public String selectParentName(String par);

    public int saveContTree(KnowledgeCategory knowledgeCategory);

    public int deleteCont(String categoryId);

    public List<KnowledgeContent>  countContentList(String categoryId);

    public int updateCont(KnowledgeContent knowledgeContent);

	public int save(KnowledgeContent knowledgeContent);
	
	public int submit(KnowledgeContent knowledgeContent);
	
	public int backApply(KnowledgeContent knowledgeContent);
	
	public int trashApply(KnowledgeContent knowledgeContent);
	
	// =============== 审核页面 ==================
	public int auditPass(KnowledgeContent knowledgeContent);
	
	public int auditRefuse(KnowledgeContent knowledgeContent);

	public int backPass(KnowledgeContent knowledgeContent);

	public int backRefuse(KnowledgeContent knowledgeContent);

	public int trashPass(KnowledgeContent knowledgeContent);

	public int trashRefuse(KnowledgeContent knowledgeContent);

	public int forceBack(KnowledgeContent knowledgeContent);

	public int forceTrash(KnowledgeContent knowledgeContent);

	// ================= 查询页面 ====================
	public List<KnowledgeContent> monitoringSearchtList(KnowledgeContent knowledgeContent);

	public String importAlarmData(List<KnowledgeAlarmExample> alarmList);

	public String importAlarmKnowledge(List<KnowledgeAlarmImport> alarmList);

	public void updateDescribeCont(KnowledgeContent knowledgeContent);

	public void updateConcatDescribeCont(KnowledgeContent knowledgeContent);

	int savetemplate(KnowledgeContent knowledgeContent);
}
