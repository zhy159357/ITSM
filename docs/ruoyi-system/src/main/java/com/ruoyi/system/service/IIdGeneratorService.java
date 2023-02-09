package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.IdGenerator;

/**
 * @author
 */
public interface IIdGeneratorService {
    /**
     * 根据类型查询
     *
     * @param generator
     * @return
     */
    public String selectIdGeneratorByType(IdGenerator generator);

    /**
     * 修改
     *
     * @param generator
     * @return
     */
    public void updateGenerator(IdGenerator generator);

    public String createNoAsLength(String type, int length);

    public String createProblemTaskNo(String problemNo);
}
