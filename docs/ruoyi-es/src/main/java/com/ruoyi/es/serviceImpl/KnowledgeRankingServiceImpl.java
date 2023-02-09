package com.ruoyi.es.serviceImpl;

import com.ruoyi.es.domain.KnowledgeRanking;
import com.ruoyi.es.mapper.KnowledgeRankingMapper;
import com.ruoyi.es.service.KnowledgeRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

import static com.ruoyi.es.utils.MathUtil.numberRate;

/**
 * 类别 Service业务层处理
 * @author dayong_sun
 * @date 2021-03-12
 */
@Service
public class KnowledgeRankingServiceImpl implements KnowledgeRankingService {

    @Autowired
    private KnowledgeRankingMapper rankingMapper;

    /**
     * 查询排名列表
     * @param knowledgeRanking 知识排名
     * @return 集合
     */
    @Override
    public List<KnowledgeRanking> selectKnowledgeRankingList(KnowledgeRanking knowledgeRanking) {
        List<KnowledgeRanking> knowledgeRankings = rankingMapper.selectKnowledgeRankingList(knowledgeRanking);
        for(KnowledgeRanking ranking : knowledgeRankings){
            int goodNum = 0;
            int badNum = 0;
            int cumulativecGood = 0;
            int cumulativeBad = 0;
            KnowledgeRanking kranking = new KnowledgeRanking();
            kranking.setCreateId(ranking.getCreateId());
            List<KnowledgeRanking> rankingList = rankingMapper.selectContentByCreateId(ranking);
            int visitednum = rankingMapper.selectVisitsByCreateId(kranking);
            for(KnowledgeRanking rank : rankingList){
                rank.setStatus("0");
                int count = rankingMapper.selectRankingById(rank);
                if(count>0){
                    goodNum++;
                }
                cumulativecGood += count;
            }
            for(KnowledgeRanking rank : rankingList){
                rank.setStatus("1");
                int count = rankingMapper.selectRankingById(rank);
                if(count>0){
                    badNum++;
                }
                cumulativeBad += count;
            }
            int totalNum = ranking.getTotalNum();
            ranking.setGoodNum(goodNum);
            ranking.setBadNum(badNum);
            ranking.setGoodRate(numberRate(goodNum,totalNum));
            ranking.setVisitedNum(visitednum);
            ranking.setCumulativecGood(cumulativecGood);
            ranking.setCumulativeBad(cumulativeBad);
        }

        return knowledgeRankings;
    }

}
