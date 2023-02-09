package com.ruoyi.form.service;

import com.ruoyi.form.entity.DbUserAndTableMessageVO;
import com.ruoyi.form.entity.TwUserDb;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
public interface ITwUserDbService extends IService<TwUserDb> {
    /**
     * 根据workNode id 查询userDb
     *
     * @param id
     * @return
     */
    List<TwUserDb> getUserByWorkNodeId(String  id);

    /**
     * @param id
     * @return
     */
    List<DbUserAndTableMessageVO> getDbUserAndTableMessage(String id);

    /**
     * 新增数据详细用户信息
     * @param dbUserAndTableMessageVO
     */
    void addUserDbForDetail(DbUserAndTableMessageVO dbUserAndTableMessageVO);

    /**
     *
     * @param id
     * @param onlyKey
     */
   void deleteUserDbDetail(String id, String onlyKey);

    TwUserDb selectone(TwUserDb twWorkOrder);

    List<TwUserDb> getUserDbByWorkNodeId(String id);

    List<TwUserDb> getUserSqlDbByWorkNodeId(String id,String classify,String dbType);

}
