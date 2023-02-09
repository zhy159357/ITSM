package com.ruoyi.es.service;

import com.ruoyi.es.domain.EsSearchBean;

/**
 * @author dayong_sun
 * @version 1.0
 */
public interface EsSearchService  {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    EsSearchBean querySearchById(String id);

}
