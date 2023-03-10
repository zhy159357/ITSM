package com.ruoyi.es.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.*;
import com.ruoyi.es.mapper.KnowledgeBusinessContentMapper;
import com.ruoyi.es.mapper.KnowledgeContentMapper;
import com.ruoyi.es.mapper.SearchMapper;
import com.ruoyi.es.service.EsSearchService;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeOperationHistoryService;
import com.ruoyi.es.thread.InsertDescribeThread;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.OgPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2021-05-27
 * dayong_sun
 */
@Service
public class KnowledgeBusinessContentServiceImpl implements KnowledgeBusinessContentService {

    @Autowired
    private KnowledgeBusinessContentMapper businessContentMapper;
    @Autowired
    private KnowledgeContentMapper contentMapper;
    @Autowired
    private KnowledgeOperationHistoryService operationHistoryService;
    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    EsSearchService esSearchService;
    @Autowired
    private OgPostService ogPostService;
    @Autowired
    KnowledgeContentService contentService;
    @Autowired
    KnowledgeBusinessContentService businessContentService;
    @Autowired
    private IOgPersonService personService;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * ????????????
     */
    @Override
    public List<KnowledgeBusinessContent> businessContentList(KnowledgeBusinessContent knowledgeBusinessContent) {
        if("mysql".equals(dbType)){
            return businessContentMapper.businessContentMySqlList(knowledgeBusinessContent);
        }else {
            return businessContentMapper.businessContentList(knowledgeBusinessContent);
        }
    }

    /**
     * ????????????
     */
    @Override
    public List<KnowledgeBusinessContent> businessSearchtList(KnowledgeBusinessContent knowledgeBusinessContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String cstatus = getStatusByUserId(userId);
        List<String> statusList = new ArrayList<>();
        //???????????????????????????
        if (StringUtils.isEmpty(knowledgeBusinessContent.getStatus()) || "0".equals(knowledgeBusinessContent.getStatus())) {
            knowledgeBusinessContent.setCreateId(userId);
        } else if ("1".equals(knowledgeBusinessContent.getStatus())) {//????????????
            if ("4".equals(cstatus)) {//???????????????
                statusList.add(PostConstants.STATUS_ZERO);
                statusList.add(PostConstants.STATUS_ONE);
                statusList.add(PostConstants.STATUS_FIVE);
                statusList.add(PostConstants.STATUS_SEVEN);
                knowledgeBusinessContent.setTwoAuditorId(userId);
            } else if ("2".equals(cstatus)) {//???????????????
                statusList.add(PostConstants.STATUS_FOUR);
                statusList.add(PostConstants.STATUS_SIX);
                statusList.add(PostConstants.STATUS_EIGHT);
                knowledgeBusinessContent.setOneAuditorId(userId);
            }
        } else if ("2".equals(knowledgeBusinessContent.getStatus())) {//????????????
            if ("4".equals(cstatus)) {//???????????????
                statusList.add(PostConstants.STATUS_TWO);
                statusList.add(PostConstants.STATUS_FOUR);
                statusList.add(PostConstants.STATUS_SIX);
                statusList.add(PostConstants.STATUS_EIGHT);
                statusList.add(PostConstants.STATUS_THREE);
                statusList.add(PostConstants.STATUS_NINE);
                knowledgeBusinessContent.setTwoAuditorId(userId);
            } else if ("2".equals(cstatus)) {//???????????????
                statusList.add(PostConstants.STATUS_TWO);
                statusList.add(PostConstants.STATUS_THREE);
                statusList.add(PostConstants.STATUS_NINE);
                knowledgeBusinessContent.setOneAuditorId(userId);
            }
        }
        knowledgeBusinessContent.setStatusList(statusList);
        startPage();
        if("mysql".equals(dbType)){
            return businessContentMapper.businessSearchtMySqlList(knowledgeBusinessContent);
        }else {
            return businessContentMapper.businessSearchtList(knowledgeBusinessContent);
        }
    }

    @Override
    public List<KnowledgeBusinessContent> businessListAud(KnowledgeBusinessContent knowledgeBusinessContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
        List<String> statusList = new ArrayList<>();
        if("2".equals(status)){
            knowledgeBusinessContent.setOneAuditorId(userId);
            statusList.add(PostConstants.STATUS_FOUR);
            statusList.add(PostConstants.STATUS_SIX);
            statusList.add(PostConstants.STATUS_EIGHT);
        }else{
            knowledgeBusinessContent.setTwoAuditorId(userId);
            statusList.add(PostConstants.STATUS_ONE);
            statusList.add(PostConstants.STATUS_FIVE);
            statusList.add(PostConstants.STATUS_SEVEN);
        }
        knowledgeBusinessContent.setStatusList(statusList);
        startPage();
        if("mysql".equals(dbType)){
            return businessContentMapper.businessListAudMySql(knowledgeBusinessContent);
        }else {
            return businessContentMapper.businessListAud(knowledgeBusinessContent);
        }
    }

    /**
     * ????????????
     */
    @Override
    @Transactional
    public int auditBusiness(KnowledgeContent knowledgeContent) {
        KnowledgeContent content = contentService.selectContById(knowledgeContent.getId());
        String kcStatus = content.getStatus();
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);
        String operstatus = "";
        if ("2".equals(status)) {//?????????????????????
            if (PostConstants.STATUS_FIVE.equals(kcStatus) || PostConstants.STATUS_SIX.equals(kcStatus)) {//?????????(????????????)
                knowledgeContent.setStatus(PostConstants.STATUS_THREE);
                operstatus = PostConstants.OPERATION_FOURTEEN;
                deleteSearchAndEs(content.getId());
            } else if (PostConstants.STATUS_SEVEN.equals(kcStatus) || PostConstants.STATUS_EIGHT.equals(kcStatus)) {//?????????
                //??????????????????????????????
                knowledgeContent.setStatus(PostConstants.STATUS_NINE);
                operstatus = PostConstants.OPERATION_SIXTEEN;
                deleteSearchAndEs(content.getId());
                //?????????????????????????????????????????????
                //deleteKnowledgeToIssuefx(content.getId());
            } else {
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_TWELVE;
                saveSearchAndEs(knowledgeContent);
            }
        } else {
            if (PostConstants.STATUS_FIVE.equals(kcStatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_SIX);
                operstatus = PostConstants.OPERATION_EIGHT;
            } else if (PostConstants.STATUS_SEVEN.equals(kcStatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);
                operstatus = PostConstants.OPERATION_TEN;
            } else {
                knowledgeContent.setStatus(PostConstants.STATUS_FOUR);
                operstatus = PostConstants.OPERATION_TWO;
            }
        }
        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(operstatus);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }


    /**
     * ????????????
     */
    @Override
    @Transactional
    public int auditNoBusiness(KnowledgeContent knowledgeContent) {
        KnowledgeContent content = contentService.selectContById(knowledgeContent.getId());
        String kcStatus = content.getStatus();
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);
        String operstatus = "";
        if ("2".equals(status)) {//?????????????????????
            if (PostConstants.STATUS_SIX.equals(kcStatus) || PostConstants.STATUS_EIGHT.equals(kcStatus)) {//?????????(????????????)
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_FIFTEEN;
            } else if (PostConstants.STATUS_FOUR.equals(kcStatus)) {//?????????
                knowledgeContent.setStatus(PostConstants.STATUS_ZERO);
                operstatus = PostConstants.OPERATION_THIRTEEN;
            } else {
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_SEVENTEEN;
            }
        } else {
            if (PostConstants.STATUS_ONE.equals(kcStatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_ZERO);
                operstatus = PostConstants.OPERATION_THREE;
            } else if (PostConstants.STATUS_FIVE.equals(kcStatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_NINE;
            } else {
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_ELEVEN;
            }
        }
        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(operstatus);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    /**
     * ????????????
     */
    @Override
    @Transactional
    public int applyBusiness(KnowledgeContent knowledgeContent) {
        String tStatus = knowledgeContent.getStatus();
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        if ("1".equals(tStatus)) {//????????????
            if ("2".equals(status)) {//???????????????
                status = PostConstants.OPERATION_EIGHTEEN;
                knowledgeContent.setStatus(PostConstants.STATUS_THREE);//?????????
                deleteSearchAndEs(knowledgeContent.getId());
            } else if ("4".equals(status)) {//???????????????
                status = PostConstants.OPERATION_EIGHT;
                knowledgeContent.setStatus(PostConstants.STATUS_SIX);//??????????????????
            } else {//????????????
                status = PostConstants.OPERATION_FOUR;
                knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//?????????
            }
        } else {//????????????
            if ("2".equals(status)) {//???????????????
                status = PostConstants.OPERATION_NINETEEN;
                knowledgeContent.setStatus(PostConstants.STATUS_NINE);//?????????
                deleteSearchAndEs(knowledgeContent.getId());
            } else if ("4".equals(status)) {//???????????????
                status = PostConstants.OPERATION_TEN;
                knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//?????????
            } else {//????????????
                status = PostConstants.OPERATION_FIVE;
                knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//?????????
            }

        }
        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    /**
     * ??????????????????????????????knowledge_search???es?????????
     * @param knowledgeContent
     * @return
     */
    @Override
    public void saveSearchAndEs(KnowledgeContent knowledgeContent) {
        String uuid = UUID.getUUIDStr();
        KnowledgeSearch knowledgeSearch = new KnowledgeSearch();
        knowledgeSearch.setName(knowledgeContent.getName());
        knowledgeSearch.setContentId(knowledgeContent.getId());
        knowledgeSearch.setStatus(knowledgeContent.getStatus());
        knowledgeSearch.setTitle(knowledgeContent.getTitle());
        knowledgeSearch.setDescribe(knowledgeContent.getDescribes());
        knowledgeSearch.setId(uuid);
        knowledgeSearch.setCreateId(knowledgeContent.getCreateId());
        searchMapper.save(knowledgeSearch);
        //???es????????????
        EsSearchBean esb = new EsSearchBean();
        esb.setId(knowledgeSearch.getContentId()); //??????es oracle ???id??????
        esb.setName(knowledgeSearch.getName());
        esb.setRemark(knowledgeSearch.getDescribe());
        esb.setContent_id(knowledgeSearch.getContentId());
        esb.setTitle(knowledgeSearch.getTitle());
        esb.setCreate_time(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        esSearchService.save(esb);
    }
    
    /**
     * ??????????????????????????????es
     */
    @Override
    public AjaxResult esCheckAndSave(String id) {
    	EsSearchBean esSearchBean = esSearchService.querySearchById(id);
		if (null == esSearchBean) {			
			try{
				KnowledgeContent knowledgeContent = contentService.selectContById(id);
				saveSearchAndEs(knowledgeContent);
				return AjaxResult.success("ES????????????");
			}catch(Exception e){
				e.printStackTrace();
				return AjaxResult.error();
			}					
		}else{
			return AjaxResult.success("ES???????????????????????????");
		}   	
    }

    /**
     * ????????????????????????
     * @param id
     * @return
     */
    @Override
    public void deleteSearchAndEs(String id) {
        EsSearchBean esSearchBean = esSearchService.querySearchById(id);
        if (null != esSearchBean) {
            searchMapper.deleteById(id);
            esSearchService.delete(esSearchBean);
        }
    }

    /**
     * ????????????
     * @param ids
     * @return
     */
    @Override
    public int offlineBusinessByIds(String ids) {
        String[] strs = Convert.toStrArray(ids);
        int count = businessContentMapper.offlineBusinessByIds(strs);
        if (count > 0) {
            for (String str : strs) {
                EsSearchBean esSearchBean = esSearchService.querySearchById(str);
                if (null != esSearchBean) {
                    searchMapper.deleteById(str);
                    esSearchService.delete(esSearchBean);
                }
            }
        }
        return count;
    }

    /**
     * ?????????????????????????????????
     * @param userId
     * @return
     */
    @Override
    public String getStatusByUserId(String userId) {
        String status = "-1";//??????
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId, null);
        for (OgPost op : list) {
            if (PostConstants.ONEADMIN.equals(op.getPostId())) {
                status = "2";//???????????????
                break;
            }
        }
        if ("2".equals(status)) {
            return status;
        }
        for (OgPost op : list) {
            if (PostConstants.TWOADMIN.equals(op.getPostId())) {
                status = "4";//???????????????
                break;
            }
        }
        if ("4".equals(status)) {
            return status;
        }
        for (OgPost op : list) {
            if (PostConstants.TWOFIRM.equals(op.getPostId())) {
                status = "3";//????????????
                break;
            }
        }
        if ("3".equals(status)) {
            return status;
        }
        return status;
    }

    /**
     * ???????????????????????????????????????
     * @param id
     * @return
     */
    @Override
    public int selectCreateIdById(String id) {
        int result = 0;
        KnowledgeContent knowledgeContent = contentService.selectContById(id);
        if (null != knowledgeContent && knowledgeContent.getCreateId().equals(ShiroUtils.getOgUser().getUserId())) {
            result = 1;
        }
        return result;
    }

    /**
     * ????????????
     * @param knowledgeContent
     * @return
     */
    @Override
    public int insertContent(KnowledgeContent knowledgeContent) {
        String describe = knowledgeContent.getDescribes();
//        knowledgeContent.setDescribes("");
        int rows = 0;
        if ("1".equals(knowledgeContent.getStatus())) {
            String userId = ShiroUtils.getOgUser().getUserId();
            String cstatus = getStatusByUserId(userId);
            String operstatus = "";
            if ("2".equals(cstatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_TWELVE;
                saveSearchAndEs(knowledgeContent);
            } else if ("4".equals(cstatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_FOUR);
                operstatus = PostConstants.OPERATION_TWO;
            } else {
                operstatus = PostConstants.OPERATION_ONE;
            }
            contentMapper.insertContent(knowledgeContent);
            knowledgeContent.setStatus(operstatus);
            rows = operationHistoryService.insertOperationHistory(knowledgeContent);
        } else {
            contentMapper.insertContent(knowledgeContent);
            rows = operationHistoryService.insertOperationHistory(knowledgeContent);
        }
        InsertDescribeThread thread = new InsertDescribeThread();
        KnowledgeContent kc = new KnowledgeContent();
        kc.setId(knowledgeContent.getId());
        kc.setDescribes(describe);
        thread.setKnowledgeContent(kc);
        thread.start();

        /*????????????????????????*/
        if(!StringUtils.isEmpty(knowledgeContent.getIssuefxNo())){
            //?????????????????????????????????
            ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());
            if(issuefx != null){
                knowledgeContent.setIssuefxId(issuefx.getIssuefxId());
                int resultNum = contentMapper.insertKnowledgeToIssuefx(knowledgeContent);
            }

        }

        return rows;

    }

    @Override
    @Transactional
    public int updateCont(KnowledgeContent knowledgeContent) {
        String describe = knowledgeContent.getDescribes();
//        knowledgeContent.setDescribes("");
        int rows = 0;
        if ("1".equals(knowledgeContent.getStatus())) {
            String userId = ShiroUtils.getOgUser().getUserId();
            String cstatus = getStatusByUserId(userId);
            String operstatus = "";
            if ("2".equals(cstatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_TWELVE;
                saveSearchAndEs(knowledgeContent);
            } else if ("4".equals(cstatus)) {
                knowledgeContent.setStatus(PostConstants.STATUS_FOUR);
                operstatus = PostConstants.OPERATION_TWO;
            } else {
                operstatus = PostConstants.OPERATION_ONE;
            }
            contentMapper.updateCont(knowledgeContent);
            knowledgeContent.setStatus(operstatus);
            rows = operationHistoryService.insertOperationHistory(knowledgeContent);
        } else {
            contentMapper.updateCont(knowledgeContent);
            rows = operationHistoryService.insertOperationHistory(knowledgeContent);
        }
        InsertDescribeThread thread = new InsertDescribeThread();
        KnowledgeContent kc = new KnowledgeContent();
        kc.setId(knowledgeContent.getId());
        kc.setDescribes(describe);
        thread.setKnowledgeContent(kc);
        thread.start();

        /*????????????????????????*/
        //??????

        //????????????????????????????????????
        if(!StringUtils.isEmpty(knowledgeContent.getIssuefxNo())){
            //?????????????????????????????????????????????????????????????????????
            int resultNum = contentMapper.findKnowledgeToIssuefxById(knowledgeContent.getId());
            if (resultNum > 0){
                //??????????????????
                //?????????????????????????????????
                ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());
                if(issuefx != null){
                    knowledgeContent.setIssuefxId(issuefx.getIssuefxId());
                }
                int updateNum = contentMapper.updateKnowledgeToIssuefx(knowledgeContent);
            }else {
                //??????????????????
                //?????????????????????????????????
                ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());
                if(issuefx != null){
                    knowledgeContent.setIssuefxId(issuefx.getIssuefxId());
                }
                int insertNum = contentMapper.insertKnowledgeToIssuefx(knowledgeContent);
            }
        }else {
            //?????????????????????
            //??????????????????????????????
            int nums = contentMapper.findKnowledgeToIssuefxById(knowledgeContent.getId());
            if(nums > 0){
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                //int deleteNum = contentMapper.deleteKnowledgeToIssuefxById(knowledgeContent.getId());
                knowledgeContent.setIssuefxId("");
                int updateNum = contentMapper.updateKnowledgeToIssuefx(knowledgeContent);
            }
        }


        return rows;
    }

    /**
     * ????????????
     * @param knowledgeContent
     * @return
     */
    @Override
    @Transactional
    public int auditContent(KnowledgeContent knowledgeContent) {
        //-----------???search???????????????-----------
        String uuid = UUID.getUUIDStr();
        KnowledgeSearch knowledgeSearch = new KnowledgeSearch();
        knowledgeSearch.setName(knowledgeContent.getName());
        knowledgeSearch.setContentId(knowledgeContent.getId());
        knowledgeSearch.setStatus(knowledgeContent.getStatus());
        knowledgeSearch.setTitle(knowledgeContent.getTitle());
        knowledgeSearch.setDescribe(knowledgeContent.getDescribes());
        knowledgeSearch.setId(uuid);
        knowledgeSearch.setCreateId(ShiroUtils.getOgUser().getpId());
        searchMapper.save(knowledgeSearch);
        //???es????????????
        if ("1".equals(knowledgeSearch.getStatus())) {
            EsSearchBean esb = new EsSearchBean();
            esb.setId(knowledgeSearch.getContentId()); //??????es oracle ???id??????
            esb.setName(knowledgeSearch.getName());
            esb.setRemark(knowledgeSearch.getDescribe());
            esb.setContent_id(knowledgeSearch.getContentId());
            esb.setTitle(knowledgeSearch.getTitle());
            esb.setCreate_time(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
            esSearchService.save(esb);
        }
        return contentMapper.auditContent(knowledgeContent);
    }

    /**
     * ??????????????????
     * @param businessList ????????????
     * @return ??????
     */
    @Override
    @Transactional
    public String importBusinessData(List<KnowledgeBusinessExample> businessList) {
        if (StringUtils.isNull(businessList) || businessList.size() == 0)
        {
            throw new BusinessException("???????????????????????????");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        businessContentMapper.deleteBusinessData();
        List<KnowledgeBusinessExample> list = new ArrayList<>();
        for (KnowledgeBusinessExample bExample : businessList)
        {
            try
            {
                bExample.setId(UUID.getUUIDStr());
                list.add(bExample);
                successNum++;
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "????????????????????? " + bExample.getSystemName() + " ???????????????";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "?????????????????????????????? " + failureNum + " ????????????????????????");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "????????????????????????????????????????????? " + successNum + " ???");
        }
        businessContentMapper.insertBusinessData(list);
        return successMsg.toString();
    }
    
    private void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }


    /**
     * ????????????????????????
     */
    @Override
    @Transactional
    public int backApply(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //????????????
        if ("2".equals(status)) {//???????????????
            status = PostConstants.OPERATION_EIGHTEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_THREE);//?????????
            deleteSearchAndEs(knowledgeContent.getId());
        } else if ("4".equals(status)) {//???????????????
            status = PostConstants.OPERATION_EIGHT;
            knowledgeContent.setStatus(PostConstants.STATUS_SIX);//??????????????????
        } else {//????????????
            status = PostConstants.OPERATION_FOUR;
            knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//?????????
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    /**
     * ??????????????????
     */
    @Override
    @Transactional
    public int trashApply(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //????????????
        if ("2".equals(status)) {//???????????????
            status = PostConstants.OPERATION_NINETEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_NINE);//?????????
            deleteSearchAndEs(knowledgeContent.getId());
        } else if ("4".equals(status)) {//???????????????
            status = PostConstants.OPERATION_TEN;
            knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//?????????
        } else {//????????????
            status = PostConstants.OPERATION_FIVE;
            knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//?????????
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    @Override
    @Transactional
    public int forceBack(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //????????????
        if ("2".equals(status)) {//???????????????
            status = PostConstants.OPERATION_EIGHTEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_THREE);//?????????
            deleteSearchAndEs(knowledgeContent.getId());
        } else if ("4".equals(status)) {//???????????????
            status = PostConstants.OPERATION_EIGHT;
            knowledgeContent.setStatus(PostConstants.STATUS_SIX);//??????????????????
        } else {//????????????
            status = PostConstants.OPERATION_FOUR;
            knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//?????????
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }


    /**
     * ????????????????????????
     */
    @Override
    @Transactional
    public int forceTrash(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //????????????
        if ("2".equals(status)) {//???????????????
            status = PostConstants.OPERATION_NINETEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_NINE);//?????????
            deleteSearchAndEs(knowledgeContent.getId());
            //?????????????????????????????????????????????????????????
            //deleteKnowledgeToIssuefx(knowledgeContent.getId());
        } else if ("4".equals(status)) {//???????????????
            status = PostConstants.OPERATION_TEN;
            knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//?????????
        } else {//????????????
            status = PostConstants.OPERATION_FIVE;
            knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//?????????
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    @Override
    public int selectIssuefxByNo(KnowledgeContent knowledgeContent) {
        //?????????????????????????????????
        ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());

        //?????????????????????????????????????????????
        //int resultNum =  contentMapper.findKnowledgeToIssuefx(knowledgeContent.getIssuefxNo());

        KnowledgeContent kc = new KnowledgeContent();
        kc = contentMapper.selectKnowledgeToIssuefx(knowledgeContent);


        //????????????????????????????????????????????????
        if(kc == null){
            /*??????????????????????????????*/
            //??????????????????????????????????????????????????????????????????????????????
            if(null == issuefx){
                return 0; //???????????????
            }
//            if(resultNum > 0){
//                return 2; //?????????????????????????????????
//            }
        }else {
            /*????????????????????????????????????*/
            return 1;
        }
        return -1;
    }

    @Override
    public String selectBusinessCreateIdById(String id) {
        return contentMapper.selectBusinessCreateIdById(id);
    }

    @Override
    public KnowledgeContent selectIssuefxIdForKnowledgeId(String id) {
        return contentMapper.selectIssuefxIdForKnowledgeId(id);
    }

    /**
     * ?????????????????????????????????????????????
     * @param id
     */
    private void deleteKnowledgeToIssuefx(String id) {
        //?????????????????????????????????????????????
        int row = contentMapper.findKnowledgeToIssuefxById(id);
        if(row > 0){
            //????????????????????????
            //contentMapper.deleteKnowledgeToIssuefxById(id);
        }
    }

}
