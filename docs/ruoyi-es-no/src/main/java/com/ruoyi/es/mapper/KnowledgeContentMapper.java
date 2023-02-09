package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface KnowledgeContentMapper {
    /**
     * 右侧别表操作
     * @param knowledgeContent
     * @return
     */
    public List<KnowledgeContent> ContentList(KnowledgeContent knowledgeContent);
    public List<KnowledgeContent> ContentListMysql(KnowledgeContent knowledgeContent);

    public List<KnowledgeContent> ContentListAud(KnowledgeContent knowledgeContent);

    public int insertContent(KnowledgeContent knowledgeContent);

    public int insertOperationHistory(KnowledgeOperationHistory knowledgeOperationHistory);

    public int updateCont(KnowledgeContent knowledgeContent);

    public int updateDescribeCont(KnowledgeContent knowledgeContent);

    public int updateConcatDescribeCont(KnowledgeContent knowledgeContent);

    List<KnowledgeContent> selectContentByIds(List<String> list);

    public KnowledgeContent selectContById(String id);
    public KnowledgeContent selectContMySqlById(String id);

    public KnowledgeContent SearchStatus(String id);

    public int deleteContByIds(String[] ids);

    public int offlineContByIds(String[] ids);

    public int auditContent(KnowledgeContent knowledgeContent);

    public int noPass(KnowledgeContent knowledgeContent);

    /**
     * 左侧树操作
     * @param knowledgeCategory
     * @return
     */
    public List<KnowledgeCategory> selectCategoryTree(KnowledgeCategory knowledgeCategory);

    public KnowledgeCategory selectCateById(String folder);

    public int insertContTree(KnowledgeCategory knowledgeCategory);

    public String selectParentName(String par);

    public int saveContTree(KnowledgeCategory knowledgeCategory);

    public int deleteCont(String categoryId);

    public List<KnowledgeContent> CountContentList(String categoryId);

    public List<KnowledgeContent> selectContentMySqlList(@Param("categoryIds")List<String> categoryIds);

	public List<KnowledgeContent> selectContentByIdsParams(@Param("ids") List<String> ids, @Param("params") Map<String, String> params);
	public List<KnowledgeContent> selectContentByIdsParamsMySql(@Param("ids") List<String> ids, @Param("params") Map<String, String> params);

	public List<KnowledgeAlarmExample> selectAlarm(KnowledgeAlarmExample knowledgeAlarmExample);

	public List<KnowledgeContent> selectAlarmExport(KnowledgeContent knowledgeContent);

	public List<KnowledgeContent> alarmAnalizeList(@Param("ids")List<String> ids, @Param("knowledgeContent")KnowledgeContent knowledgeContent);

	public List<KnowledgeContent> monitoringSearchtList(@Param("knowledgeContent")KnowledgeContent knowledgeContent, @Param("status")List<String> status);

    public int deleteAlarmData();

    public int insertAlarmData(List<KnowledgeAlarmExample> list);

    public int importAlarmKnowledge(List<KnowledgeAlarmImport> list);

	public int selectAlarmCount();

    ImBizIssuefxs selectIssuefxByNo(String issuefxNo);

    int findKnowledgeToIssuefx(String issuefxNo);

    int insertKnowledgeToIssuefx(KnowledgeContent knowledgeContent);

    int updateKnowledgeToIssuefx(KnowledgeContent knowledgeContent);

    int findKnowledgeToIssuefxById(String id);

    KnowledgeContent selectKnowledgeToIssuefx(KnowledgeContent knowledgeContent);

    String selectBusinessCreateIdById(String id);

    int deleteKnowledgeToIssuefxById(String id);

    KnowledgeContent selectIssuefxIdForKnowledgeId(String id);
}
