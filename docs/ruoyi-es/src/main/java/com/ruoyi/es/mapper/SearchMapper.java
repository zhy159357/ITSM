package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SearchMapper {

    List<KnowledgeSearch> selectSearchList(KnowledgeSearch knowledgeSearch);

    KnowledgeSearch selectSearchById(String id);

    List<KnowledgeSearch> selectContentList(KnowledgeSearch knowledgeSearch);

    int updateById(KnowledgeSearch knowledgeSearch);

    int save(KnowledgeSearch knowledgeSearch);

    int deleteById(@Param("id") String id);

}
