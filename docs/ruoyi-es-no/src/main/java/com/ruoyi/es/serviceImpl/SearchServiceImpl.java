package com.ruoyi.es.serviceImpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.EsSearchBean;
import com.ruoyi.es.domain.KnowledgeAlarmExample;
import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeSearch;
import com.ruoyi.es.mapper.KnowledgeBusinessContentMapper;
import com.ruoyi.es.mapper.KnowledgeContentMapper;
import com.ruoyi.es.mapper.SearchMapper;
import com.ruoyi.es.service.EsSearchService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.es.service.SearchService;

/**
 * @author dayong_sun
 * @version 1.0
 */
//@Service
public class SearchServiceImpl implements SearchService {

    //private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private KnowledgeContentMapper knowledgeContentMapper;
    @Autowired
    private KnowledgeBusinessContentMapper businessContentMapper;
    @Autowired
    EsSearchService esSearchService;
    @Autowired
    KnowledgeTitleService knowledgeTitleService;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    //private static int  minuend = 0;
    @Override
    public List<KnowledgeSearch> selectSearchList(KnowledgeSearch knowledgeSearch) {
        return searchMapper.selectSearchList(knowledgeSearch);
    }

    @Override
    public KnowledgeSearch selectSearchById(String id) {
        return searchMapper.selectSearchById(id);
    }

    @Override
    public List<KnowledgeSearch> selectContentList(KnowledgeSearch knowledgeSearch) {
        return searchMapper.selectContentList(knowledgeSearch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(KnowledgeSearch knowledgeSearch) {
        knowledgeSearch.setUpdateId(ShiroUtils.getOgUser().getpId());
        knowledgeSearch.setUpdateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        int row = searchMapper.updateById(knowledgeSearch);
        //修改es
        String id = knowledgeSearch.getId();
        if("1".equals(knowledgeSearch.getStatus())){
            EsSearchBean esBean = esSearchService.querySearchById(id);
        }else {
            EsSearchBean esSearchBean = esSearchService.querySearchById(id);
            if(null==esSearchBean){
                esSearchBean = new EsSearchBean();
                esSearchBean.setId(id); //确保es oracle 的id一样
                esSearchBean.setName(knowledgeSearch.getName());
                esSearchBean.setRemark(knowledgeSearch.getRemark());
                esSearchBean.setContent_id(knowledgeSearch.getContentId());
                esSearchBean.setTitle(knowledgeSearch.getTitle());
                esSearchBean.setCreate_time(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
            }else {
                esSearchBean.setId(knowledgeSearch.getId());
                esSearchBean.setName(knowledgeSearch.getName());
                esSearchBean.setRemark(knowledgeSearch.getRemark());
                esSearchBean.setTitle(knowledgeSearch.getTitle());
                esSearchBean.setContent_id(knowledgeSearch.getContentId());
            }
        }
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(KnowledgeSearch knowledgeSearch) {
        String uuid = UUID.getUUIDStr();
        knowledgeSearch.setId(uuid);
        knowledgeSearch.setCreateId(ShiroUtils.getOgUser().getpId());
        knowledgeSearch.setCreateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        int row = searchMapper.save(knowledgeSearch);
        //向es添加数据
        if("0".equals(knowledgeSearch.getStatus())) {
            EsSearchBean esb = new EsSearchBean();
            esb.setId(uuid); //确保es oracle 的id一样
            esb.setName(knowledgeSearch.getName());
            esb.setRemark(knowledgeSearch.getRemark());
            esb.setContent_id(knowledgeSearch.getContentId());
            esb.setTitle(knowledgeSearch.getTitle());
            esb.setCreate_time(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        }
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String id) {
        int row = searchMapper.deleteById(id);
        //删除es
        EsSearchBean esBean = esSearchService.querySearchById(id);
        if(null!=esBean) {
        }
        return row;
    }

    @Override
    public List<KnowledgeContent> selectContentByIdsAndParams(List<String> ids, Map<String, String> params){
        if("mysql".equals(dbType)){
            return knowledgeContentMapper.selectContentByIdsParamsMySql(ids, params);
        }else {
            return knowledgeContentMapper.selectContentByIdsParams(ids, params);
        }
    }

    @Override
    public List<KnowledgeContent> search(Map<String, String> searchMap) {
        List<KnowledgeContent> resultList = new ArrayList<>();
        String keywords = "";
        String content = "";
        String sectitle = "";
        String threetitle = "";
        String title = "";
        String sysId = "";
        if(null!=searchMap && searchMap.size()>0){
            title = searchMap.get("title");
            content = searchMap.get("content");
            sectitle = searchMap.get("sectitle");
            keywords = searchMap.get("keywords");
            threetitle = searchMap.get("threetitle");
            sysId = searchMap.get("sysId");

            List<KnowledgeContent> selectList = selectContentByIdsAndParams(null, searchMap);

            if("0".equals(searchMap.get("eventType"))){//监控事件单
                if(StringUtils.isNotEmpty(content) || StringUtils.isNotEmpty(sectitle)){
                    if(selectList.isEmpty()){
                        return resultList;
                    }else {
                        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(keywords)) {
                            return selectList;
                        } else if (StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(keywords)) {
                            title = title.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            keywords = keywords.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            List<EsSearchBean> list1 = new ArrayList<>();
                            List<EsSearchBean> list2 = new ArrayList<>();
                            list1 = getMinimumShouldList(list1, "title", title);
                            if (list1.isEmpty()) {
                                return resultList;
                            } else {
                                list2 = getMinimumShouldList(list2, "name", keywords);
                                if (list2.isEmpty()) {
                                    return resultList;
                                } else {
                                    resultList = intersectionList(selectList,list1,list2,keywords);
                                }
                            }
                        } else if (StringUtils.isNotEmpty(title)) {
                            title = title.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            resultList = getMinimumShouldList1("title", title, selectList);
                        } else if (StringUtils.isNotEmpty(keywords)) {
                            keywords = keywords.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            resultList = getMinimumShouldList1("name", keywords, selectList);
                        }
                    }
                }else{
                    if(StringUtils.isEmpty(title)&&StringUtils.isEmpty(keywords)){
                        return resultList;
                    }else if(StringUtils.isNotEmpty(title)&&StringUtils.isNotEmpty(keywords)){
                        title = title.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        keywords = keywords.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        List<EsSearchBean> list1 = new ArrayList<>();
                        List<EsSearchBean> list2 = new ArrayList<>();
                        list1 = getMinimumShouldList(list1,"title",title);
                        if(list1.isEmpty()){
                            return resultList;
                        }else {
                            list2 = getMinimumShouldList(list2, "name", keywords);
                            if (list2.isEmpty()) {
                                return resultList;
                            }else{
                                resultList = intersectionList(selectList,list1,list2,keywords);
                            }
                        }
                    }else if(StringUtils.isNotEmpty(title)){
                        title = title.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        List<EsSearchBean> list1 = new ArrayList<>();
                        list1 = getMinimumShouldList2("title",title);
                        if(list1.isEmpty()){
                            return resultList;
                        }else {
                            HashSet<String> set = new HashSet<>();
                            for(EsSearchBean esb:list1){
                                String contentId = esb.getContent_id();
                                if(StringUtils.isNotEmpty(contentId)) {
                                    set.add(contentId);
                                }
                            }
                            if(!set.isEmpty()) {
                                List<String> ids = new ArrayList<>(set);
                                resultList.addAll(selectContentByIdsAndParams(ids, searchMap));
                            }
                        }
                        
                    }else if(StringUtils.isNotEmpty(keywords)){
                        keywords = keywords.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        List<EsSearchBean> list1 = new ArrayList<>();
                        list1 = getMinimumShouldList2("name",keywords);
                        if(list1.isEmpty()){
                            return resultList;
                        }else {
                            HashSet<String> set = new HashSet<>();
                            for(EsSearchBean esb:list1){
                                String contentId = esb.getContent_id();
                                if(StringUtils.isNotEmpty(contentId)) {
                                    set.add(contentId);
                                }
                            }
                            if(!set.isEmpty()) {
                                List<String> ids = new ArrayList<>(set);
                                resultList.addAll(selectContentByIdsAndParams(ids, searchMap));
                            }
                        }
                    }
                }
            }else{//业务事件单
                if(StringUtils.isNotEmpty(sysId) || StringUtils.isNotEmpty(content) || StringUtils.isNotEmpty(sectitle) || StringUtils.isNotEmpty(threetitle)) {
                    if (selectList.isEmpty()) {
                        return resultList;
                    } else {
                        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(keywords)) {
                            return selectList;
                        } else if (StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(keywords)) {
                            title = title.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            keywords = keywords.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            List<EsSearchBean> list1 = new ArrayList<>();
                            List<EsSearchBean> list2 = new ArrayList<>();
                            list1 = getMinimumShouldList(list1, "title", title);
                            if (list1.isEmpty()) {
                                return resultList;
                            } else {
                                list2 = getMinimumShouldList(list2, "name", keywords);
                                if (list2.isEmpty()) {
                                    return resultList;
                                } else {
                                    resultList = intersectionList(selectList,list1,list2,keywords);
                                }
                            }
                        } else if (StringUtils.isNotEmpty(title)) {
                            title = title.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            resultList = getMinimumShouldList1("title", title, selectList);
                        } else if (StringUtils.isNotEmpty(keywords)) {
                            keywords = keywords.replaceAll("[\\[\\]]", "").replaceAll("[\\{\\}]", "").replaceAll("[\\(\\)]", "");
                            resultList = getMinimumShouldList1("name", keywords, selectList);
                        }

                    }
                }else{
                    if(StringUtils.isEmpty(title)&&StringUtils.isEmpty(keywords)){
                        return resultList;
                    }else if(StringUtils.isNotEmpty(title)&&StringUtils.isNotEmpty(keywords)){
                        title = title.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        keywords = keywords.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        List<EsSearchBean> list1 = new ArrayList<>();
                        List<EsSearchBean> list2 = new ArrayList<>();
                        list1 = getMinimumShouldList(list1,"title",title);
                        if(list1.isEmpty()){
                            return resultList;
                        }else {
                            list2 = getMinimumShouldList(list2, "name", keywords);
                            if (list2.isEmpty()) {
                                return resultList;
                            }else{
                                resultList = intersectionList(selectList,list1,list2,keywords);
                            }
                        }
                    }else if(StringUtils.isNotEmpty(title)){
                        title = title.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        List<EsSearchBean> list1 = new ArrayList<>();
                        list1 = getMinimumShouldList2("title",title);
                        if(list1.isEmpty()){
                            return resultList;
                        }else {
                            HashSet<String> set = new HashSet<>();
                            for(EsSearchBean esb:list1){
                                String contentId = esb.getContent_id();
                                if(StringUtils.isNotEmpty(contentId)) {
                                    set.add(contentId);
                                }
                            }
                            if(!set.isEmpty()) {
                                List<String> ids = new ArrayList<>(set);
                                resultList.addAll(selectContentByIdsAndParams(ids, searchMap));
                            }
                        }
                    }else if(StringUtils.isNotEmpty(keywords)){
                        keywords = keywords.replaceAll("[\\[\\]]","").replaceAll("[\\{\\}]","").replaceAll("[\\(\\)]","");
                        List<EsSearchBean> list1 = new ArrayList<>();
                        list1 = getMinimumShouldList2("name",keywords);
                        if(list1.isEmpty()){
                            return resultList;
                        }else {
                            HashSet<String> set = new HashSet<>();
                            for(EsSearchBean esb:list1){
                                String contentId = esb.getContent_id();
                                if(StringUtils.isNotEmpty(contentId)) {
                                    set.add(contentId);
                                }
                            }
                            if(!set.isEmpty()) {
                                List<String> ids = new ArrayList<>(set);
                                resultList.addAll(selectContentByIdsAndParams(ids, searchMap));
                            }
                        }
                    }
                }
            }
        }
        LinkedHashSet<KnowledgeContent> hashSet = new LinkedHashSet<>(resultList);
        return new ArrayList<>(hashSet);
    }

    @Override
    public KnowledgeContent selectContentById(String id) {
        if("mysql".equals(dbType)){
            return knowledgeContentMapper.selectContMySqlById(id);
        }else {
            return knowledgeContentMapper.selectContById(id);
        }
    }

    private List<EsSearchBean> getMinimumShouldList(List<EsSearchBean> list, String name, String keywords){
        if(StringUtils.isEmpty(keywords)){
            return new ArrayList<>();
        }
        
      //根据字符串长度获取查询粒度
        int minuend =0;
        int kword = keywords.length();
        int keyLength = kword - minuend;
        
        return list;
    }

    /**
     * 精确匹配
     * @param list
     * @param name
     * @param keywords
     * @return
     */
    private int getMinimumFormatList(List<EsSearchBean> list,EsSearchBean esb, String name, String keywords){  	
    	//根据字符串长度获取查询粒度
    	int minuend =0;
        int kword = keywords.length();
        int keyLength = kword - minuend;
        return minuend;
    }

    @Override
    public List<KnowledgeAlarmExample> alarmAnalizeList(KnowledgeAlarmExample knowledgeAlarmExample) {
        List<KnowledgeAlarmExample> result = knowledgeContentMapper.selectAlarm(knowledgeAlarmExample);

        for(KnowledgeAlarmExample k : result){
            Map<String, String> searchMap = new HashMap<>();
            searchMap.put("title", k.getName());
            searchMap.put("content", StringUtils.isEmpty(k.getContentId())?"-1":k.getContentId());
            searchMap.put("sectitle",StringUtils.isEmpty(k.getSectitleId())?"-1":k.getSectitleId());
            searchMap.put("keywords", null);
            searchMap.put("eventType", "0");

            List<KnowledgeContent> l = searchMonitorApi(searchMap);
            if(!l.isEmpty()){
                KnowledgeContent kc = l.get(0);
                k.setKnowledgeId(kc.getId());
                k.setKnowledgeTitle(kc.getTitle());
                k.setParams(kc.getParams());
            }
        }
        return result;
    }

    @Override
    public List<KnowledgeAlarmExample> alarmExportList(KnowledgeAlarmExample knowledgeAlarmExample) {
        KnowledgeContent kno = new KnowledgeContent();
        List<KnowledgeAlarmExample> allList = knowledgeContentMapper.selectAlarm(knowledgeAlarmExample);
        List<KnowledgeContent> result = knowledgeContentMapper.selectAlarmExport(kno);
        List<KnowledgeContent> resu = new ArrayList<>();
       // long s1=System.currentTimeMillis();
        for(KnowledgeAlarmExample k : allList){
            KnowledgeContent knowc = new KnowledgeContent();
            if(result==null || result.size()==0){
                return allList;
            }
            String title = removeDuplication(k.getName());//告警描述 匹配知识标题和标签
            String oragintitle = k.getName();//未做去重处理的告警描述 用于匹配标签
            if(StringUtils.isEmpty(title) || StringUtils.isEmpty(oragintitle)){//一级标题和二级标题匹配不上，直接返回空结果
                continue;
            }else{//es查询知识标题 且结果在匹配一二级标题的结果集中
                //long s3=System.currentTimeMillis();
                resu = result.stream()
                        .filter((KnowledgeContent kn) -> kn.getContentId().equals(k.getContentId())&& kn.getSectitleId().equals(k.getSectitleId()))
                        .collect(Collectors.toList());
                //long s5=System.currentTimeMillis();
                //logger.debug("-----------searchMonitorApiGMSL result.stream()---------------"+(s5-s3));
                knowc=searchMonitorExport(title, oragintitle, resu);
                //long s4=System.currentTimeMillis();
                //logger.debug("-----------searchMonitorApiGMSL----------------------"+(s5-s4));
            }
            if(null!=knowc && StringUtils.isNotBlank(knowc.getUpdateName())){
                k.setKnowledgeId(knowc.getId());
                k.setKnowledgeTitle(knowc.getTitle());
                k.setParams(knowc.getParams());
            }
        }
        //long s2=System.currentTimeMillis();
        //logger.debug("-------------KnowledgeAlarmExample k : allList-----------------------------------------"+(s2-s1));
        return allList;
    }
    private int wordLength(String str){
    	int len = 0;
    	char c[] = str.toCharArray();
    	StringBuilder word = new StringBuilder();
    	for(int i=0; i<c.length; i++){
    		if((c[i]>=97 && c[i]<=122) || (c[i]>=65 && c[i]<=90) || (c[i]>=48 && c[i]<=57) || c[i]==95 || c[i]==46){
    			word.append(c[i]);
    		}else{
    			if(word.length()>0){
        			len++;
        			word = new StringBuilder();
        		}
    			len++;
    		}
    	}
    	if(word.length()>0){
			len++;
		}
    	return len;
    }
    private String removeDuplication(String str){
    	char c[] = str.toCharArray();
    	StringBuilder sb = new StringBuilder();
    	StringBuilder word = new StringBuilder();
    	for(int i=0; i<c.length; i++){
    		if((c[i]>=97 && c[i]<=122) || (c[i]>=65 && c[i]<=90) || (c[i]>=48 && c[i]<=57) || c[i]==95 || c[i]==46){
    			word.append(c[i]);
    		}else if(String.valueOf(c[i]).matches("[\u4e00-\u9fa5]")){
    			if(word.length()>0 &&sb.indexOf(word.toString()) == -1){
        			sb.append(word);
        			word = new StringBuilder();
        		}
    			if(sb.indexOf(String.valueOf(c[i])) == -1){
        			sb.append(c[i]);
        		}
    		}else{
    			if(word.length()>0 &&sb.indexOf(word.toString()) == -1){
        			sb.append(word).append(" ");
        			word = new StringBuilder();
        		}
    		}
    	}
    	if(word.length()>0 && sb.indexOf(word.toString()) == -1){
    		sb.append(word).append(" ");
    	}
    	return sb.toString();
    }

    /**
     * 监控事件单知识检索（告警分析、知识库接口用）
     * @param searchMap
     * @return
     */
    @Override
    public List<KnowledgeContent> searchMonitorApi(Map<String, String> searchMap){    	
        List<KnowledgeContent> resultList = new ArrayList<>();
        if(searchMap==null || searchMap.isEmpty()){
            return resultList;
        }
        //String content = searchMap.get("content");//对象 匹配一级标题
        //String sectitle = searchMap.get("sectitle");//告警来源 匹配二级标题
        String title = removeDuplication(searchMap.get("title"));//告警描述 匹配知识标题和标签
        String oragintitle = searchMap.get("title");//未做去重处理的告警描述 用于匹配标签
       // long s2=System.currentTimeMillis();
        List<KnowledgeContent> selectList = selectContentByIdsAndParams(null, searchMap);//根据一二级标题匹配
       // long s1=System.currentTimeMillis();
       // logger.debug("-------------selectContentByIdsAndParams-----------------------------------------"+(s1-s2));
        if(selectList.isEmpty()){//一级标题和二级标题匹配不上，直接返回空结果
            return resultList;
        }else if(StringUtils.isEmpty(title) || StringUtils.isEmpty(oragintitle)){//没有告警描述，直接返回根据一二级匹配的结果
            return selectList;
        }else{//es查询知识标题 且结果在匹配一二级标题的结果集中
           // long s3=System.currentTimeMillis();
            resultList=searchMonitorApiGMSL(title, oragintitle, selectList);
          //  long s4=System.currentTimeMillis();
          //  logger.debug("-------------searchMonitorApiGMSL-----------------------------------------"+(s4-s3));
        }
        return resultList;
    }
    //searchMonitorExport 监控导出
    private KnowledgeContent searchMonitorExport( String keywords, String oraginkeywords, List<KnowledgeContent> selectList){
        KnowledgeContent resultKC = new KnowledgeContent();
        List<KnowledgeContent> kcList = new ArrayList<>();
        if(StringUtils.isEmpty(keywords)){
            return resultKC;
        }
        List<EsSearchBean> list = new ArrayList<>();
      //  logger.debug("------------elasticsearchTemplate.queryForList1------------"+(s2-s1));
        if(!list.isEmpty()){
            for(KnowledgeContent kc : selectList){
                for(EsSearchBean esb : list){
                    if(kc.getId().equals(esb.getId()) && searchMonitorApiMK(oraginkeywords, kc)){
                        //根据标题检索到知识，且与标签进行匹配通过时
                        int keyLen = searchMonitorExportMK(keywords,esb);
                        Map<String,Object> m = new HashMap<>();
                        DecimalFormat df = new DecimalFormat("0.00%");
                        df.setRoundingMode(RoundingMode.HALF_UP);
                        double d = (double)keyLen/(double)wordLength(keywords);
                        m.put("match", df.format(d));
                        kc.setParams(m);//计算匹配率
                        kc.setUpdateName(String.valueOf(keyLen));
                        kcList.add(kc);
                        break;
                    }
                }
            }
        }
        if(null!=kcList&&kcList.size()>0) {
            kcList = kcList.stream().sorted(Comparator.comparing(KnowledgeContent::getUpdateName).reversed()).collect(Collectors.toList());
            resultKC = kcList.get(0);
        }
        return resultKC;
    }
    //searchMonitorApi 自用的GetMinimumShouldList方法
    private List<KnowledgeContent> searchMonitorApiGMSL( String keywords, String oraginkeywords, List<KnowledgeContent> selectList){
        List<KnowledgeContent> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(keywords)){
            return resultList;
        }

        //根据字符串长度获取查询粒度
        int minuend2 =0;
        int kword = keywords.length();
        int keyLength = kword - minuend2;
        //long s3=System.currentTimeMillis();
        return resultList;
    }
    //searchMonitorApi 自用的MatchKeywords方法 匹配关键字（标签）
    private int searchMonitorExportMK(String keywords, EsSearchBean es){
        String[] ks = keywords.split("");
        String title = es.getTitle();
        int k = 0;
        for(int i=0;i<ks.length;i++){
            if(title.contains(ks[i])){
                k++;
            }
        }
        return k;
    }
    //searchMonitorApi 自用的MatchKeywords方法 匹配关键字（标签）
    private boolean searchMonitorApiMK(String keywords, KnowledgeContent kc){
        String name = kc.getName();//获取知识kc中的标签，用空格分隔
        if(StringUtils.isEmpty(name)){//该知识没有标签的情况下，不做下述匹配
            return true;
        }
        String[] sa = name.split(" ");
        for(String s : sa){
            if(!keywords.contains(s)){
                //告警描述不包括其中任何一个标签,返回false（不匹配）
                return false;
            }
        }
        return true;
    }

    private List<KnowledgeContent> intersectionList(List<KnowledgeContent> selectList,List<EsSearchBean> list1,List<EsSearchBean> list2,String keywords){
        List<KnowledgeContent> resultList = new ArrayList<>();
        Map<String,EsSearchBean> map = new HashMap<>();
        Map<String,EsSearchBean> rmap = new HashMap<>();
        for(EsSearchBean esSearchBean : list1){
            if(!map.containsKey(esSearchBean.getContent_id())){//满足list1（标题匹配）的数据
                map.put(esSearchBean.getContent_id(),esSearchBean);
            }
        }
        for(EsSearchBean esSearchBean : list2){
            if(map.containsKey(esSearchBean.getContent_id())){//满足list2（标签匹配）的数据中，同时满足list1的
                rmap.put(esSearchBean.getContent_id(),esSearchBean);
            }
        }
        List<EsSearchBean> list3 = new ArrayList<>();
        for(EsSearchBean value : rmap.values()){//把这些同时满足list1,list2的数据 放到list3里
            list3.add(value);
        }
        int arr1[] = new int[list3.size()];
        if(StringUtils.isNotEmpty(list3)) {
            for (int i = 0; i < list3.size(); i++) {//每个满足list1的数据中，对list2数据的匹配程度
                arr1[i] = getMinimumFormatList(new ArrayList<>(), list3.get(i), "name", keywords);
                //minuend = 0;
            }
            int xx = getArrayMin(arr1);//arr1最小的那个，匹配程度最高
            EsSearchBean esb = list3.get(xx);
            if(StringUtils.isNotEmpty(selectList)) {
                for (KnowledgeContent kc : selectList) {                    
                    if (kc.getId().equals(esb.getId())) {//如果这个数据在selectList中，就选择它，不在就返回空的
                        resultList.add(kc);
                    }
                }
            }else{//如果selectList本身是空的，就直接返回那个数据
                KnowledgeContent knowledgeContent = knowledgeContentMapper.selectContById(esb.getContent_id());
                resultList.add(knowledgeContent);
            }
        }
        return resultList;
    }

    private int getArrayMin(int[] data){
        int min = data[0];
        int minid = 0;
        for(int i=1;i<data.length;i++){
            if(min>data[i]){
                min=data[i];
                minid = i;
            }
        }
        return minid;
    }

    @Override
    public List<KnowledgeBusinessContent> businessAnalizeList(KnowledgeBusinessContent businessContent) {
        List<KnowledgeBusinessContent> result = businessContentMapper.selectBusiness(businessContent);
        for(KnowledgeBusinessContent k : result){
            Map<String, String> searchMap = new HashMap<>();
            searchMap.put("sysId", k.getSysId());
            searchMap.put("content", k.getContentId());
            searchMap.put("sectitle",k.getSectitleId());
            searchMap.put("threetitle",k.getThreetitleId());
            searchMap.put("keywords", k.getName());
            searchMap.put("eventType", "1");

            List<KnowledgeContent> l = search(searchMap);
            if(!l.isEmpty()){
                KnowledgeContent kc = l.get(0);
                k.setAtId(kc.getId());
                k.setFilename(kc.getTitle());
            }
        }
        return result;
    }

    private List<KnowledgeContent> getMinimumShouldList1( String name, String keywords, List<KnowledgeContent> selectList){
        List<KnowledgeContent> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(keywords)){
            return resultList;
        }
        
      //根据字符串长度获取查询粒度
        int minuend =0;
        int kword = keywords.length();
        int keyLength = kword - minuend;
        
        return resultList;
    }
    
    private List<EsSearchBean> getMinimumShouldList2( String name, String keywords ){
        List<EsSearchBean> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(keywords)){
            return resultList;
        }
        
      //根据字符串长度获取查询粒度
        int minuend =0;
        int kword = keywords.length();
        int keyLength = kword - minuend;
        
        return resultList;
    }
    
    @Override
    public List<KnowledgeAlarmExample> selectAlarm(KnowledgeAlarmExample knowledgeAlarmExample) {
        return knowledgeContentMapper.selectAlarm(knowledgeAlarmExample);
    }

	@Override
	public int selectAlarmCount() {
		return knowledgeContentMapper.selectAlarmCount();
	}
}
