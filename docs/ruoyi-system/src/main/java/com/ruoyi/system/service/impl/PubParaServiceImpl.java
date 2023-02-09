package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.PubPara;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.PubParaMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.service.IPubParaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class PubParaServiceImpl  implements IPubParaService {

    private static final Logger log = LoggerFactory.getLogger(PubParaServiceImpl.class);

    @Autowired
    private PubParaMapper pubParaMapper;

    @Autowired
    private PubParaValueMapper pubParaValueMapper;

    /**是否开启字典缓存*/
    @Value("${itsm.dictCache}")
    private boolean dictCache;

    /**
     * 项目启动时，加载所有字典项列表到缓存
     */
    @PostConstruct
    public void init() {
        log.debug("字典缓存配置开关[dictCache=" + dictCache + "],该项配置为true启动时加载字典项到redis");
        if(dictCache){
            List<PubPara> pubParas = selectPubParaList(new PubPara());
            for(PubPara para : pubParas){
                String paraName = para.getParaName();
                List<PubParaValue> values = pubParaValueMapper.selectPubParaValueByParaName(paraName);
                if(StringUtils.isNotEmpty(values)){
                    DictUtils.setPubParaCache(paraName, values);
                }
            }
        }
    }

    @Override
    public List<PubPara> selectPubParaList(PubPara pubPara) {
        return pubParaMapper.selectPubParaList(pubPara);
    }

    @Override
    public String checkParaNameUnique(PubPara pubPara) {
        PubPara pub_para = pubParaMapper.checkParaNameUnique(pubPara.getParaName());
        if (StringUtils.isNotNull(pub_para)){
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    @Override
    public int insertPubPara(PubPara pubPara) {
        return pubParaMapper.insertPubPara(pubPara);
    }

    @Override
    public PubPara selectPubParaById(String paraId) {
        return pubParaMapper.selectPubParaById(paraId);
    }

    @Override
    public int updatePubParaById(PubPara pubPara) {
        return pubParaMapper.updatePubParaById(pubPara);
    }
    @Override
    public int deleteByParaId(String paraId){
        return pubParaMapper.deleteByParaId(paraId);
    }
}
