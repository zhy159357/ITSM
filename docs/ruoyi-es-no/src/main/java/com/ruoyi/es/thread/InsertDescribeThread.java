package com.ruoyi.es.thread;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.service.KnowledgeContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 向表knowledge_content插入大字段线程
 * @author sdy
 * @date 2021年7月8日
 */
public class InsertDescribeThread extends Thread {

    //private static final Logger logger = LoggerFactory.getLogger(InsertDescribeThread.class);

    private KnowledgeContent knowledgeContent;

    /**
     * @param knowledgeContent
     */
    public void setKnowledgeContent(KnowledgeContent knowledgeContent) {
        this.knowledgeContent = knowledgeContent;
    }

    /**
     * @see Thread#run()
     */
    @SuppressWarnings("resource")
    @Override
    public void run() {
        //logger.debug("输入id："+knowledgeContent.getId());
        String describe = knowledgeContent.getDescribes();
        KnowledgeContentService contentService = SpringUtils.getBean(KnowledgeContentService.class);
        try {
            if(StringUtils.isNotEmpty(describe)){
                KnowledgeContent initContent = new KnowledgeContent();
                initContent.setId(knowledgeContent.getId());
                initContent.setDescribes("");
                contentService.updateDescribeCont(initContent);
                //logger.debug("=======describe.length()======="+describe.length());
                if(describe.length()>4000) {
                    int dlength = describe.length() / 4000;
                   // logger.debug("=======dlength======="+dlength);
                    String sub = "";
                    for (int i = 0; i <= dlength; i++) {
                        if(i==dlength){
                            sub = describe.substring(i*4000,describe.length());
                        }else {
                            sub = describe.substring(i * 4000, (i + 1) * 4000);
                        }
                        knowledgeContent.setDescribes(sub);
                        contentService.updateConcatDescribeCont(knowledgeContent);
                    }
                }else{
                    contentService.updateConcatDescribeCont(knowledgeContent);
                }
            }
            //logger.debug(knowledgeContent.getId() + " - 更新完成");
        } catch (Exception e) {
        	e.printStackTrace();
            //logger.error(knowledgeContent.getId() + " - 更新失败", e);
        }

    }
}