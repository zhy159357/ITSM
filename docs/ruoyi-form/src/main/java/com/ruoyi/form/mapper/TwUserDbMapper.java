package com.ruoyi.form.mapper;

import com.ruoyi.form.entity.TwUserDb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
public interface TwUserDbMapper extends BaseMapper<TwUserDb> {

    /**
     * 根据id修改userDb
     * @param id
     * @param userDb
     */
    @Update({"UPDATE tw_user_db set user_db=#{userDb} where id=#{id}"})
    void updateTwUserDbById(@Param("id") String id, @Param("userDb") String userDb);

}
