package com.ruoyi.es.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.es.domain.KnowledgeRanking;
import com.ruoyi.es.service.KnowledgeRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类别Controller
 * @author dayong_sun
 * @date 2021-04-12
 */
@Controller
@RequestMapping("knowledge/ranking")
public class RankingController extends BaseController {
    private String prefix = "knowledge/ranking";

    @Autowired(required=false)
    private KnowledgeRankingService rankingService;

    @GetMapping()
    public String ranking() {
        return prefix + "/ranking";
    }

    /**
     * 查询类别列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeRanking knowledgeRanking) {
        //startPage(); 启动前端排序需注释该方法
        List<KnowledgeRanking> list = rankingService.selectKnowledgeRankingList(knowledgeRanking);
        return getDataTable(list);
    }

}
