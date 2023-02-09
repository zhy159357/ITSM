package com.ruoyi.es.service;

import com.ruoyi.es.domain.EsUserBean;

/**
 * @author dayong_sun
 * @version 1.0
 */
public interface EsService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    EsUserBean queryEmployeeById(String id);

}
