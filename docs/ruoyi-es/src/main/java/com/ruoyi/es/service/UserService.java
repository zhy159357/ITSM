package com.ruoyi.es.service;


import com.ruoyi.es.domain.EsUserBean;
import com.ruoyi.es.domain.UserBean;

import java.util.List;
import java.util.Map;

/**
 * @author dayong_sun
 * @version 1.0
 */
public interface UserService {
    List<UserBean> selectUserBeanList(UserBean ub);

    List<UserBean> selectLikeUserBeanList(UserBean ub);

    int updateById(UserBean userBean);

    int save(UserBean userBean);

    int deleteById(String id);

    List<EsUserBean> search(Map<String,String> searchMap);
}
