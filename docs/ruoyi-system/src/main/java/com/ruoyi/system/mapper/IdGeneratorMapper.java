package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.IdGenerator;

/**
 *
 * @author
 */
public interface IdGeneratorMapper {

    /**
     * 根据类型查询
     * @param generator
     * @return
     */
    public IdGenerator selectIdGeneratorByType(IdGenerator generator);
    IdGenerator selectIdGeneratorByTypeMysql(IdGenerator generator);

    /**
     * 修改
     * @param generator
     * @return
     */
    public int updateGenerator(IdGenerator generator);

    /**
     * 新增
     * @param generator
     * @return
     */
    public int insertGenerator(IdGenerator generator);
}
