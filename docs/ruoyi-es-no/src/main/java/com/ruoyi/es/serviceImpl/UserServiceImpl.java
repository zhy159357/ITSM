package com.ruoyi.es.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.EsUserBean;
import com.ruoyi.es.domain.UserBean;
import com.ruoyi.es.mapper.UserMapper;
import com.ruoyi.es.service.UserService;

/**
 * @author dayong_sun
 * @version 1.0
 */
//@Service
public class UserServiceImpl implements UserService {

    //将DAO注入Service层
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserBean> selectUserBeanList(UserBean ub) {
        return userMapper.selectUserBeanList(ub);
    }

    @Override
    public List<UserBean> selectLikeUserBeanList(UserBean ub) {
        return userMapper.selectLikeUserBeanList(ub);
    }

    @Override
    public int updateById(UserBean userBean) {
        return userMapper.updateById(userBean);
    }

    @Override
    public int save(UserBean userBean) {
        userBean.setId(UUID.getUUIDStr());
        return userMapper.save(userBean);
    }

    @Override
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }

    @Override
    public List<EsUserBean> search(Map<String, String> searchMap) {
        return  new ArrayList();
    }

}
