package com.ruoyi.es.serviceImpl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.EsUserBean;
import com.ruoyi.es.domain.UserBean;
import com.ruoyi.es.mapper.UserMapper;
import com.ruoyi.es.service.UserService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author dayong_sun
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    //将DAO注入Service层
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if(null!=searchMap && searchMap.size()>0){
            String keywords = searchMap.get("keywords");
            if(StringUtils.isNotEmpty(keywords)){
                nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keywords).field("remark"));
            }
        }
        String s = nativeSearchQueryBuilder.toString();
        System.out.println("es的数据查询 === "+s);

        AggregatedPage<EsUserBean> page = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), EsUserBean.class);
        //总页数
        long totalElements = page.getTotalElements();
        //分页数
        int totalPages = page.getTotalPages();
        //获取数据结果集
        return page.getContent();
    }

}
