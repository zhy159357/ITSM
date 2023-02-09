package com.ruoyi.es.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeOperationHistory;
import com.ruoyi.es.mapper.KnowledgeOperationHistoryMapper;
import com.ruoyi.es.service.KnowledgeOperationHistoryService;

//@Service
public class KnowledgeOperationHistoryServiceImpl implements KnowledgeOperationHistoryService {

	@Autowired
    private KnowledgeOperationHistoryMapper operationHistoryMapper;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

	@Override
	public List<KnowledgeOperationHistory> selectKnowledgeOperationHistoryList(KnowledgeOperationHistory knowledgeOperationHistory) {
        if("mysql".equals(dbType)){
            return operationHistoryMapper.selectKnowledgeOperationHistoryMySqlList(knowledgeOperationHistory);
        }else {
            return operationHistoryMapper.selectKnowledgeOperationHistoryList(knowledgeOperationHistory);
        }
	}

	@Override
	public int insertOperationHistory(KnowledgeContent knowledgeContent) {
        KnowledgeOperationHistory knowledgeOperationHistory = new KnowledgeOperationHistory();
        knowledgeOperationHistory.setId(UUID.getUUIDStr());
        knowledgeOperationHistory.setContentId(knowledgeContent.getId());
        knowledgeOperationHistory.setOperation(knowledgeContent.getStatus());
        knowledgeOperationHistory.setOperId(ShiroUtils.getOgUser().getpId());
        knowledgeOperationHistory.setOperTime(DateUtils.getTime());
        knowledgeOperationHistory.setRemark(knowledgeContent.getReason());
        return operationHistoryMapper.insertKnowledgeOperationHistory(knowledgeOperationHistory);

    }

	@Override
	public int insertOperationHistory(String contentId, String status, String reason) {
		KnowledgeOperationHistory knowledgeOperationHistory = new KnowledgeOperationHistory();
        knowledgeOperationHistory.setId(UUID.getUUIDStr());
        knowledgeOperationHistory.setContentId(contentId);
        knowledgeOperationHistory.setOperation(status);
        knowledgeOperationHistory.setOperId(ShiroUtils.getOgUser().getpId());
        knowledgeOperationHistory.setOperTime(DateUtils.getTime());
        knowledgeOperationHistory.setRemark(reason);
        return operationHistoryMapper.insertKnowledgeOperationHistory(knowledgeOperationHistory);
	}

}
