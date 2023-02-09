package com.ruoyi.es.serviceImpl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.mapper.KnowledgeCollectionMapper;
import com.ruoyi.es.service.KnowledgeCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类别 Service业务层处理
 * @author dayong_sun
 * @date 2021-04-09
 */
@Service
public class KnowledgeCollectionServiceImpl implements KnowledgeCollectionService {

    @Autowired
    private KnowledgeCollectionMapper collectionMapper;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询收藏 列表
     * @param knowledgeCollect 收藏
     * @return 类别 
     */
    @Override
    public List<KnowledgeCollect> selectKnowledgeCollectionList(KnowledgeCollect knowledgeCollect) {
    	knowledgeCollect.setCreateId(ShiroUtils.getOgUser().getpId());
        if("mysql".equals(dbType)){
            return collectionMapper.selectKnowledgeCollectionMysqlList(knowledgeCollect);
        }else{
            return collectionMapper.selectKnowledgeCollectionList(knowledgeCollect);
        }
    }

    /**
     * 删除类别 对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCollectionByIds(String ids) {
        return collectionMapper.deleteKnowledgeCollectionByIds(Convert.toStrArray(ids));
    }
    
	@Override
	public int insertKnowledgeCollection(KnowledgeCollect knowledgeCollect) {
		return collectionMapper.insertKnowledgeCollection(knowledgeCollect);
	}

	@Override
	public boolean isCollected(String userId, String contentId) {
		return collectionMapper.isCollected(userId, contentId) > 0;
	}

}
