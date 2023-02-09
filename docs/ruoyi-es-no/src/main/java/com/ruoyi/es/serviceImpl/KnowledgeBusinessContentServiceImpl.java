package com.ruoyi.es.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
import com.ruoyi.es.domain.EsSearchBean;
import com.ruoyi.es.domain.ImBizIssuefxs;
import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeBusinessExample;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeSearch;
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

/**
 * @date 2021-05-27
 * dayong_sun
 */
//@Service
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
     * 查询列表
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
     * 查询列表
     */
    @Override
    public List<KnowledgeBusinessContent> businessSearchtList(KnowledgeBusinessContent knowledgeBusinessContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String cstatus = getStatusByUserId(userId);
        List<String> statusList = new ArrayList<>();
        //初始化或者我创建的
        if (StringUtils.isEmpty(knowledgeBusinessContent.getStatus()) || "0".equals(knowledgeBusinessContent.getStatus())) {
            knowledgeBusinessContent.setCreateId(userId);
        } else if ("1".equals(knowledgeBusinessContent.getStatus())) {//待审核的
            if ("4".equals(cstatus)) {//二线管理员
                statusList.add(PostConstants.STATUS_ZERO);
                statusList.add(PostConstants.STATUS_ONE);
                statusList.add(PostConstants.STATUS_FIVE);
                statusList.add(PostConstants.STATUS_SEVEN);
                knowledgeBusinessContent.setTwoAuditorId(userId);
            } else if ("2".equals(cstatus)) {//一线管理员
                statusList.add(PostConstants.STATUS_FOUR);
                statusList.add(PostConstants.STATUS_SIX);
                statusList.add(PostConstants.STATUS_EIGHT);
                knowledgeBusinessContent.setOneAuditorId(userId);
            }
        } else if ("2".equals(knowledgeBusinessContent.getStatus())) {//已审核的
            if ("4".equals(cstatus)) {//二线管理员
                statusList.add(PostConstants.STATUS_TWO);
                statusList.add(PostConstants.STATUS_FOUR);
                statusList.add(PostConstants.STATUS_SIX);
                statusList.add(PostConstants.STATUS_EIGHT);
                statusList.add(PostConstants.STATUS_THREE);
                statusList.add(PostConstants.STATUS_NINE);
                knowledgeBusinessContent.setTwoAuditorId(userId);
            } else if ("2".equals(cstatus)) {//一线管理员
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
     * 审核通过
     */
    @Override
    public int auditBusiness(KnowledgeContent knowledgeContent) {
        KnowledgeContent content = contentService.selectContById(knowledgeContent.getId());
        String kcStatus = content.getStatus();
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);
        String operstatus = "";
        if ("2".equals(status)) {//一线管理员操作
            if (PostConstants.STATUS_FIVE.equals(kcStatus) || PostConstants.STATUS_SIX.equals(kcStatus)) {//待下线(退回修改)
                knowledgeContent.setStatus(PostConstants.STATUS_THREE);
                operstatus = PostConstants.OPERATION_FOURTEEN;
                deleteSearchAndEs(content.getId());
            } else if (PostConstants.STATUS_SEVEN.equals(kcStatus) || PostConstants.STATUS_EIGHT.equals(kcStatus)) {//待作废
                //一线审批通过作废单子
                knowledgeContent.setStatus(PostConstants.STATUS_NINE);
                operstatus = PostConstants.OPERATION_SIXTEEN;
                deleteSearchAndEs(content.getId());
                //作废的知识，删除对应问题单关联
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
     * 审核拒绝
     */
    @Override
    public int auditNoBusiness(KnowledgeContent knowledgeContent) {
        KnowledgeContent content = contentService.selectContById(knowledgeContent.getId());
        String kcStatus = content.getStatus();
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);
        String operstatus = "";
        if ("2".equals(status)) {//一线管理员操作
            if (PostConstants.STATUS_SIX.equals(kcStatus) || PostConstants.STATUS_EIGHT.equals(kcStatus)) {//待下线(退回修改)
                knowledgeContent.setStatus(PostConstants.STATUS_TWO);
                operstatus = PostConstants.OPERATION_FIFTEEN;
            } else if (PostConstants.STATUS_FOUR.equals(kcStatus)) {//待作废
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
     * 提交申请
     */
    @Override
    public int applyBusiness(KnowledgeContent knowledgeContent) {
        String tStatus = knowledgeContent.getStatus();
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        if ("1".equals(tStatus)) {//申请下线
            if ("2".equals(status)) {//一线管理员
                status = PostConstants.OPERATION_EIGHTEEN;
                knowledgeContent.setStatus(PostConstants.STATUS_THREE);//已下线
                deleteSearchAndEs(knowledgeContent.getId());
            } else if ("4".equals(status)) {//二线管理员
                status = PostConstants.OPERATION_EIGHT;
                knowledgeContent.setStatus(PostConstants.STATUS_SIX);//下线初审通过
            } else {//二线厂商
                status = PostConstants.OPERATION_FOUR;
                knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//待下线
            }
        } else {//申请作废
            if ("2".equals(status)) {//一线管理员
                status = PostConstants.OPERATION_NINETEEN;
                knowledgeContent.setStatus(PostConstants.STATUS_NINE);//已作废
                deleteSearchAndEs(knowledgeContent.getId());
            } else if ("4".equals(status)) {//二线管理员
                status = PostConstants.OPERATION_TEN;
                knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//待作废
            } else {//二线厂商
                status = PostConstants.OPERATION_FIVE;
                knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//待作废
            }

        }
        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    /**
     * 已发布的知识保存到表knowledge_search和es服务器
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
        //向es添加数据
        EsSearchBean esb = new EsSearchBean();
        esb.setId(knowledgeSearch.getContentId()); //确保es oracle 的id一样
        esb.setName(knowledgeSearch.getName());
        esb.setRemark(knowledgeSearch.getDescribe());
        esb.setContent_id(knowledgeSearch.getContentId());
        esb.setTitle(knowledgeSearch.getTitle());
        esb.setCreate_time(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
    }
    
    /**
     * 检查并确保知识已存入es
     */
    @Override
    public AjaxResult esCheckAndSave(String id) {
    	EsSearchBean esSearchBean = esSearchService.querySearchById(id);
		if (null == esSearchBean) {			
			try{
				KnowledgeContent knowledgeContent = contentService.selectContById(id);
				saveSearchAndEs(knowledgeContent);
				return AjaxResult.success("ES同步成功");
			}catch(Exception e){
				e.printStackTrace();
				return AjaxResult.error();
			}					
		}else{
			return AjaxResult.success("ES状态正常，无需同步");
		}   	
    }

    /**
     * 删除已发布的知识
     * @param id
     * @return
     */
    @Override
    public void deleteSearchAndEs(String id) {
        EsSearchBean esSearchBean = esSearchService.querySearchById(id);
        if (null != esSearchBean) {
            searchMapper.deleteById(id);
        }
    }

    /**
     * 批量下线
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
                }
            }
        }
        return count;
    }

    /**
     * 根据登录人查询所属岗位
     * @param userId
     * @return
     */
    @Override
    public String getStatusByUserId(String userId) {
        String status = "-1";//其他
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId, null);
        for (OgPost op : list) {
            if (PostConstants.ONEADMIN.equals(op.getPostId())) {
                status = "2";//一线管理员
                break;
            }
        }
        if ("2".equals(status)) {
            return status;
        }
        for (OgPost op : list) {
            if (PostConstants.TWOADMIN.equals(op.getPostId())) {
                status = "4";//二线管理员
                break;
            }
        }
        if ("4".equals(status)) {
            return status;
        }
        for (OgPost op : list) {
            if (PostConstants.TWOFIRM.equals(op.getPostId())) {
                status = "3";//二线厂商
                break;
            }
        }
        if ("3".equals(status)) {
            return status;
        }
        return status;
    }

    /**
     * 查询当前登录人是否是创建人
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
     * 提交保存
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

        /*知识库关联问题单*/
        if(!StringUtils.isEmpty(knowledgeContent.getIssuefxNo())){
            //根据问题单号查询问题单
            ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());
            if(issuefx != null){
                knowledgeContent.setIssuefxId(issuefx.getIssuefxId());
                int resultNum = contentMapper.insertKnowledgeToIssuefx(knowledgeContent);
            }

        }

        return rows;

    }

    @Override
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

        /*知识库关联问题单*/
        //更新

        //判断传入问题单号是否存在
        if(!StringUtils.isEmpty(knowledgeContent.getIssuefxNo())){
            //如果传入问题单号存在，则判断对应关系表是否存在
            int resultNum = contentMapper.findKnowledgeToIssuefxById(knowledgeContent.getId());
            if (resultNum > 0){
                //更新对应关系
                //根据问题单号查询问题单
                ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());
                if(issuefx != null){
                    knowledgeContent.setIssuefxId(issuefx.getIssuefxId());
                }
                int updateNum = contentMapper.updateKnowledgeToIssuefx(knowledgeContent);
            }else {
                //新增对应关系
                //根据问题单号查询问题单
                ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());
                if(issuefx != null){
                    knowledgeContent.setIssuefxId(issuefx.getIssuefxId());
                }
                int insertNum = contentMapper.insertKnowledgeToIssuefx(knowledgeContent);
            }
        }else {
            //未传入问题单号
            //查询对应关系是否存在
            int nums = contentMapper.findKnowledgeToIssuefxById(knowledgeContent.getId());
            if(nums > 0){
                //未传入问题单号，且对应关系存在，则为取消掉此知识库与问题单的对应关系，删除此知识与问题单的绑定
                //int deleteNum = contentMapper.deleteKnowledgeToIssuefxById(knowledgeContent.getId());
                knowledgeContent.setIssuefxId("");
                int updateNum = contentMapper.updateKnowledgeToIssuefx(knowledgeContent);
            }
        }


        return rows;
    }

    /**
     * 审核通过
     * @param knowledgeContent
     * @return
     */
    @Override
    public int auditContent(KnowledgeContent knowledgeContent) {
        //-----------向search表添加数据-----------
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
        //向es添加数据
        if ("1".equals(knowledgeSearch.getStatus())) {
            EsSearchBean esb = new EsSearchBean();
            esb.setId(knowledgeSearch.getContentId()); //确保es oracle 的id一样
            esb.setName(knowledgeSearch.getName());
            esb.setRemark(knowledgeSearch.getDescribe());
            esb.setContent_id(knowledgeSearch.getContentId());
            esb.setTitle(knowledgeSearch.getTitle());
            esb.setCreate_time(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        }
        return contentMapper.auditContent(knowledgeContent);
    }

    /**
     * 导入业务数据
     * @param businessList 业务数据
     * @return 结果
     */
    @Override
    public String importBusinessData(List<KnowledgeBusinessExample> businessList) {
        if (StringUtils.isNull(businessList) || businessList.size() == 0)
        {
            throw new BusinessException("导入数据不能为空！");
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
                String msg = "<br/>" + failureNum + "、所属应用系统 " + bExample.getSystemName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条");
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
     * 提交申请修改下线
     */
    @Override
    public int backApply(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //申请下线
        if ("2".equals(status)) {//一线管理员
            status = PostConstants.OPERATION_EIGHTEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_THREE);//已下线
            deleteSearchAndEs(knowledgeContent.getId());
        } else if ("4".equals(status)) {//二线管理员
            status = PostConstants.OPERATION_EIGHT;
            knowledgeContent.setStatus(PostConstants.STATUS_SIX);//下线初审通过
        } else {//二线厂商
            status = PostConstants.OPERATION_FOUR;
            knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//待下线
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    /**
     * 提交申请作废
     */
    @Override
    public int trashApply(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //申请作废
        if ("2".equals(status)) {//一线管理员
            status = PostConstants.OPERATION_NINETEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_NINE);//已作废
            deleteSearchAndEs(knowledgeContent.getId());
        } else if ("4".equals(status)) {//二线管理员
            status = PostConstants.OPERATION_TEN;
            knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//待作废
        } else {//二线厂商
            status = PostConstants.OPERATION_FIVE;
            knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//待作废
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    @Override
    public int forceBack(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //申请下线
        if ("2".equals(status)) {//一线管理员
            status = PostConstants.OPERATION_EIGHTEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_THREE);//已下线
            deleteSearchAndEs(knowledgeContent.getId());
        } else if ("4".equals(status)) {//二线管理员
            status = PostConstants.OPERATION_EIGHT;
            knowledgeContent.setStatus(PostConstants.STATUS_SIX);//下线初审通过
        } else {//二线厂商
            status = PostConstants.OPERATION_FOUR;
            knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//待下线
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }


    /**
     * 提交申请强制作废
     */
    @Override
    public int forceTrash(KnowledgeContent knowledgeContent) {
        String userId = ShiroUtils.getOgUser().getUserId();
        String status = getStatusByUserId(userId);

        //申请作废
        if ("2".equals(status)) {//一线管理员
            status = PostConstants.OPERATION_NINETEEN;
            knowledgeContent.setStatus(PostConstants.STATUS_NINE);//已作废
            deleteSearchAndEs(knowledgeContent.getId());
            //强制作废时，删除对应知识与问题单的关联
            //deleteKnowledgeToIssuefx(knowledgeContent.getId());
        } else if ("4".equals(status)) {//二线管理员
            status = PostConstants.OPERATION_TEN;
            knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//待作废
        } else {//二线厂商
            status = PostConstants.OPERATION_FIVE;
            knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//待作废
        }

        contentMapper.auditContent(knowledgeContent);
        knowledgeContent.setStatus(status);
        return operationHistoryService.insertOperationHistory(knowledgeContent);
    }

    @Override
    public int selectIssuefxByNo(KnowledgeContent knowledgeContent) {
        //根据问题单号查询问题单
        ImBizIssuefxs issuefx = contentMapper.selectIssuefxByNo(knowledgeContent.getIssuefxNo());

        //查询知识与问题单是否有对应关系
        //int resultNum =  contentMapper.findKnowledgeToIssuefx(knowledgeContent.getIssuefxNo());

        KnowledgeContent kc = new KnowledgeContent();
        kc = contentMapper.selectKnowledgeToIssuefx(knowledgeContent);


        //修改判断，此知识库是否变更问题单
        if(kc == null){
            /*已有知识变更了问题单*/
            //判断是否有在问题单内有此问题单，且在对应关系内无关系
            if(null == issuefx){
                return 0; //无次问题单
            }
//            if(resultNum > 0){
//                return 2; //其它知识已对了此问题单
//            }
        }else {
            /*已有知识没进行修改问题单*/
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
     * 作废的知识，删除对应问题单关联
     * @param id
     */
    private void deleteKnowledgeToIssuefx(String id) {
        //查看知识与问题单关联表是否存在
        int row = contentMapper.findKnowledgeToIssuefxById(id);
        if(row > 0){
            //存在关联，则删除
            //contentMapper.deleteKnowledgeToIssuefxById(id);
        }
    }

}
