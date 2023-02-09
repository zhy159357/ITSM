package com.ruoyi.es.service;

import com.ruoyi.es.domain.EsSearchBean;
import com.ruoyi.es.domain.EsUserBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author dayong_sun
 * @version 1.0
 */
public interface EsSearchService extends ElasticsearchRepository<EsSearchBean,String> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    EsSearchBean querySearchById(String id);

}
