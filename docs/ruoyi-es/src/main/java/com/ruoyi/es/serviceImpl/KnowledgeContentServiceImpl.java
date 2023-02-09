package com.ruoyi.es.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.*;
import com.ruoyi.es.mapper.KnowledgeContentMapper;
import com.ruoyi.es.service.EsSearchService;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeOperationHistoryService;
import com.ruoyi.es.thread.InsertDescribeThread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.utils.DictUtils.getCacheName;

/**
 * 类别 Service业务层处理
 * @date 2021-03-31
 */
@Service
public class KnowledgeContentServiceImpl implements KnowledgeContentService {

    @Autowired
    private KnowledgeContentMapper contentMapper;
    @Autowired
    private EsSearchService esSearchService;
    @Autowired
    private KnowledgeBusinessContentService businessContentService;
    @Autowired
    private KnowledgeOperationHistoryService operationHistoryService;
	@Value("${pagehelper.helperDialect}")
	private String dbType;

    private static int importDataCount = 5000;

     /**
     * 查询列表
     */
    @Override
    public List<KnowledgeContent> ContentList(KnowledgeContent knowledgeContent) {
        List<KnowledgeContent> list;
        if("mysql".equals(dbType)){
            list = contentMapper.ContentListMysql(knowledgeContent);
        }else{
            list = contentMapper.ContentList(knowledgeContent);
        }
        return list;
    }

    @Override
    public List<KnowledgeContent> ContentListAud(KnowledgeContent knowledgeContent) {
    	String userId = ShiroUtils.getOgUser().getUserId();
    	String status = businessContentService.getStatusByUserId(userId);    
 		if ("4".equals(status)) {//当前人员=4=二线管理员
 			knowledgeContent.setTwoAuditorId(userId);
 		}else {//当前人员=2=一线管理员
 			knowledgeContent.setOneAuditorId(userId);
 		}
 	    startPage();
        return contentMapper.ContentListAud(knowledgeContent);
    }

    @Override
    public KnowledgeContent selectContById(String id)
    {
		if("mysql".equals(dbType)){
			return contentMapper.selectContMySqlById(id);
		}else {
			return contentMapper.selectContById(id);
		}
    }

    @Override
    public KnowledgeContent SearchStatus(String id)
    {
        return contentMapper.SearchStatus(id);
    }

    @Override
    public int deleteContByIds(String ids) {
        String[] str = Convert.toStrArray(ids);
        int count = contentMapper.deleteContByIds(str);
        if (count > 0) {
            for (String s :str) {
                EsSearchBean esSearchBean = esSearchService.querySearchById(s);
                if(null!=esSearchBean){
                    esSearchService.delete(esSearchBean);
                }
                //CacheUtils.removeAll(getCacheName());
            }

        }
        return count;
    }

    //----------------加载列表树
    @Override
    public List<Ztree> selectCategoryTree(KnowledgeCategory knowledgeCategory)
    {
      	return initZtree(contentMapper.selectCategoryTree(knowledgeCategory));
    }
    public List<Ztree> initZtree(List<KnowledgeCategory> categoryList)
    {
        return initZtree(categoryList, null);
    }

    public List<Ztree> initZtree(List<KnowledgeCategory> categoryList, List<String> roleDeptList)
    {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (KnowledgeCategory category : categoryList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(category.getCategoryId());
            ztree.setpId(category.getParentId());
            ztree.setName(category.getCategoryName());
            ztree.setTitle(category.getCategoryName());
            if (isCheck)
            {
                ztree.setChecked(roleDeptList.contains(category.getCategoryId() + category.getCategoryName()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }
    @Override
    public KnowledgeCategory selectCateById(String folder)
    {
        return contentMapper.selectCateById(folder);
    }

    @Override
    public int insertContTree(KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setCategoryId(UUID.getUUIDStr());
        String parent = knowledgeCategory.getCategoryId();
        KnowledgeCategory pa= contentMapper.selectCateById(parent);
        if(pa!=null){
            knowledgeCategory.setParentId(pa.getParentId());
        }
        return contentMapper.insertContTree(knowledgeCategory);
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public String selectParentName(String par) {
        return contentMapper.selectParentName(par);
    }
    @Override
    @Transactional
    public int saveContTree(KnowledgeCategory knowledgeCategory) {
        return contentMapper.saveContTree(knowledgeCategory);
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public int deleteCont(String categoryId) {
        // 修改角色信息
        return contentMapper.deleteCont(categoryId);
    }

    @Override
    public List<KnowledgeContent> countContentList(String categoryId) {
    	if("mysql".equals(dbType)){
			List<String> clist = getCategoryIdList(categoryId,new ArrayList<>());
			return contentMapper.selectContentMySqlList(clist);
		}else {
			return contentMapper.CountContentList(categoryId);
		}
    }

    @Override
    public int updateCont(KnowledgeContent knowledgeContent) {
        return contentMapper.updateCont(knowledgeContent);
    }

    public List<String> getCategoryIdList(String categoryId,List<String> rlist){
		KnowledgeCategory knowledgeCategory = new KnowledgeCategory();
		knowledgeCategory.setParentId(categoryId);
		List<KnowledgeCategory> list = contentMapper.selectCategoryTree(knowledgeCategory);
		if(null==list||list.size()==0){
			return rlist;
		}
		for(KnowledgeCategory kc : list){
			rlist.add(kc.getCategoryId());
			getCategoryIdList(kc.getCategoryId(),rlist);
		}
		return rlist;
	}

    /**
     * 暂存（新增、修改的暂存）
     */
	@Override
	@Transactional
	public int save(KnowledgeContent knowledgeContent) {
		String describe = knowledgeContent.getDescribes();
		knowledgeContent.setDescribes("");
		int result = 0;
		knowledgeContent.setStatus(PostConstants.STATUS_ZERO);//暂存操作后状态为0=草稿
		if(StringUtils.isEmpty(knowledgeContent.getId())){//新增的数据
			knowledgeContent.setId(UUID.getUUIDStr());
			knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
			knowledgeContent.setCreateTime(DateUtils.getTime());
			result+=contentMapper.insertContent(knowledgeContent);
		}else{//修改的数据
			knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
			result+=contentMapper.updateCont(knowledgeContent);
			updateDescribeCont(knowledgeContent);
		}
		InsertDescribeThread thread = new InsertDescribeThread();
		KnowledgeContent kc = new KnowledgeContent();
		kc.setId(knowledgeContent.getId());
		kc.setDescribes(describe);
		thread.setKnowledgeContent(kc);
		thread.start();
		//保存操作记录 - 暂存=0
		result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_ZERO,"");
		return result;
	}
	
	/**
     * 提交（新增、修改的提交）
     */
	@Override
	@Transactional
	public int submit(KnowledgeContent knowledgeContent) {
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
		if ("4".equals(status)||"2".equals(status)) {//当前人员=4/2=二/一线管理员，提交操作转为初/终审通过操作
			//保存操作记录 - 提交=1
			//result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_ONE,"");
			result += auditPass(knowledgeContent);
		}else {
			String describe = knowledgeContent.getDescribes();
			knowledgeContent.setDescribes("");
			knowledgeContent.setStatus(PostConstants.STATUS_ONE);//提交操作后状态为1=待审核
			if(StringUtils.isEmpty(knowledgeContent.getId())){//新增的数据
				knowledgeContent.setId(UUID.getUUIDStr());
				knowledgeContent.setCreateId(userId);
				knowledgeContent.setCreateTime(DateUtils.getTime());
				result+=contentMapper.insertContent(knowledgeContent);
			}else{//修改的数据
				knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
				result+=contentMapper.updateCont(knowledgeContent);
				updateDescribeCont(knowledgeContent);
			}
			InsertDescribeThread thread = new InsertDescribeThread();
			KnowledgeContent kc = new KnowledgeContent();
			kc.setId(knowledgeContent.getId());
			kc.setDescribes(describe);
			thread.setKnowledgeContent(kc);
			thread.start();
			//保存操作记录 - 提交=1
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_ONE,"");
        }

		return result;
	}
	
	/**
	 * 申请下线
	 */
	@Override
	@Transactional
	public int backApply(KnowledgeContent knowledgeContent){
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
        if ("4".equals(status)) {//当前人员=4=二线管理员，申请下线操作转为下线初审通过操作
        	//保存操作记录 - 申请下线=4
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_FOUR,knowledgeContent.getReason());
			result += backPass(knowledgeContent);
		} else {
			knowledgeContent.setStatus(PostConstants.STATUS_FIVE);//申请下线操作后状态为5=待下线
			result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 申请下线=4
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_FOUR,knowledgeContent.getReason());
		}
        return result;
	}
	/**
	 * 申请作废
	 */
	@Override
	@Transactional
	public int trashApply(KnowledgeContent knowledgeContent){
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
        if ("4".equals(status)) {//当前人员=4=二线管理员，申请作废操作转为作废初审通过操作
        	//保存操作记录 - 申请作废=5
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_FIVE,knowledgeContent.getReason());
			result += trashPass(knowledgeContent);
		} else {
			knowledgeContent.setStatus(PostConstants.STATUS_SEVEN);//申请作废操作后状态为7=待作废
			result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 申请作废=5
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_FIVE,knowledgeContent.getReason());
		}
        return result;
	}
	/**
	 * （上线申请）审核通过
	 */
	@Override
	@Transactional
	public int auditPass(KnowledgeContent knowledgeContent){
		String describe = knowledgeContent.getDescribes();
		knowledgeContent.setDescribes("");
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
		if ("4".equals(status)) {//当前人员=4=二线管理员，操作为初审通过
			knowledgeContent.setStatus(PostConstants.STATUS_FOUR);//初审通过操作后状态为4=初审通过
			if(StringUtils.isEmpty(knowledgeContent.getId())){//新增的数据
				knowledgeContent.setId(UUID.getUUIDStr());
				knowledgeContent.setCreateId(userId);
				knowledgeContent.setCreateTime(DateUtils.getTime());
				result+=contentMapper.insertContent(knowledgeContent);
			}else{//修改的数据
				result+=contentMapper.updateCont(knowledgeContent);
			}
			//保存操作记录 - 初审通过=2
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_TWO,knowledgeContent.getReason());
		}else if("2".equals(status)){//当前人员=2=一线管理员，操作为终审通过
			knowledgeContent.setStatus(PostConstants.STATUS_TWO);//终审通过操作后状态为2=已发布
			if(StringUtils.isEmpty(knowledgeContent.getId())){//新增的数据
				knowledgeContent.setId(UUID.getUUIDStr());
				knowledgeContent.setCreateId(userId);
				knowledgeContent.setCreateTime(DateUtils.getTime());
				result+=contentMapper.insertContent(knowledgeContent);
			}else{//修改的数据
				result+=contentMapper.updateCont(knowledgeContent);
			}
			//向ES中插入
			businessContentService.saveSearchAndEs(knowledgeContent);
			//保存操作记录 - 综审通过=12
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_TWELVE,knowledgeContent.getReason());
		}
		InsertDescribeThread thread = new InsertDescribeThread();
		KnowledgeContent kc = new KnowledgeContent();
		kc.setId(knowledgeContent.getId());
		kc.setDescribes(describe);
		thread.setKnowledgeContent(kc);
		thread.start();
		return result;
	}
	/**
	 * （上线申请）审核拒绝
	 */
	@Override
	@Transactional
	public int auditRefuse(KnowledgeContent knowledgeContent){
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId); 
		if ("4".equals(status)) {//当前人员=4=二线管理员，操作为初审拒绝
			knowledgeContent.setStatus(PostConstants.STATUS_ZERO);//初审终审拒绝操作后状态都为0=草稿
	        result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 初审拒绝=3
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_THREE,knowledgeContent.getReason());
		}else if("2".equals(status)){//当前人员=2=一线管理员，操作为终审拒绝
			knowledgeContent.setStatus(PostConstants.STATUS_ZERO);//初审终审拒绝操作后状态都为0=草稿
	        result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 终审拒绝=13
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_THIRTEEN,knowledgeContent.getReason());
		}
		return result;
	}

	/**
     * （下线申请）审核通过
     */
	@Override
	@Transactional
	public int backPass(KnowledgeContent knowledgeContent) {
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
		if ("4".equals(status)) {//当前人员=4=二线管理员，操作为初审同意下线
			knowledgeContent.setStatus(PostConstants.STATUS_SIX);//初审同意下线操作后状态为6=下线初审通过
			result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 初审同意下线=8
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_EIGHT,knowledgeContent.getReason());
		}else if("2".equals(status)){//当前人员=2=一线管理员，操作为综审同意下线
			knowledgeContent.setStatus(PostConstants.STATUS_THREE);//初审同意下线操作后状态为3=已下线
			result+=contentMapper.updateCont(knowledgeContent);
			//从ES中删除
			businessContentService.deleteSearchAndEs(knowledgeContent.getId());
			//保存操作记录 - 综审同意下线=14
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_FOURTEEN,knowledgeContent.getReason());

		}
		return result;
	}

	/**
     * （下线申请）审核拒绝
     */
	@Override
	@Transactional
	public int backRefuse(KnowledgeContent knowledgeContent) {
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);    
		if ("4".equals(status)) {//当前人员=4=二线管理员，操作为初审拒绝下线
			knowledgeContent.setStatus(PostConstants.STATUS_TWO);//初审终审拒绝下线操作后状态都为2=已发布
	        result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 初审拒绝下线=9
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_NINE,knowledgeContent.getReason());
		}else if("2".equals(status)){//当前人员=2=一线管理员，操作为终审拒绝下线
			knowledgeContent.setStatus(PostConstants.STATUS_TWO);//初审终审拒绝下线操作后状态都为2=已发布
	        result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 终审拒绝下线=15
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_FIFTEEN,knowledgeContent.getReason());
		}
		return result;
	}
	/**
     * （作废申请）审核通过
     */
	@Override
	@Transactional
	public int trashPass(KnowledgeContent knowledgeContent) {
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
		if ("4".equals(status)) {//当前人员=4=二线管理员，操作为初审同意作废
			knowledgeContent.setStatus(PostConstants.STATUS_EIGHT);//初审同意作废操作后状态为8=下线作废通过
			result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 初审同意作废=10
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_TEN,knowledgeContent.getReason());
		}else if("2".equals(status)){//当前人员=2=一线管理员，操作为综审同意作废
			knowledgeContent.setStatus(PostConstants.STATUS_NINE);//综审同意作废操作后状态为9=已下线
			result+=contentMapper.updateCont(knowledgeContent);
			//从ES中删除
			businessContentService.deleteSearchAndEs(knowledgeContent.getId());
			//保存操作记录 - 终审同意作废=16
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_SIXTEEN,knowledgeContent.getReason());
		}
		return result;
	}
	/**
     * （作废申请）审核拒绝
     */
	@Override
	@Transactional
	public int trashRefuse(KnowledgeContent knowledgeContent) {
		int result = 0;
		String userId = ShiroUtils.getOgUser().getUserId();
        String status = businessContentService.getStatusByUserId(userId);
		if ("4".equals(status)) {//当前人员=4=二线管理员，操作为初审拒绝作废
			knowledgeContent.setStatus(PostConstants.STATUS_TWO);//初审终审拒绝作废操作后状态都为2=已发布
	        result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 初审拒绝作废=11
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_ELEVEN,knowledgeContent.getReason());
		}else if("2".equals(status)){//当前人员=2=一线管理员，操作为综审拒绝作废
			knowledgeContent.setStatus(PostConstants.STATUS_TWO);//初审终审拒绝作废操作后状态都为2=已发布
	        result+=contentMapper.updateCont(knowledgeContent);
			//保存操作记录 - 综审拒绝作废=17
			result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_SEVENTEEN,knowledgeContent.getReason());
		}
		return result;
	}
	/**
     * 强制下线
     */
	@Override
	@Transactional
	public int forceBack(KnowledgeContent knowledgeContent) {
		int result = 0;
		knowledgeContent.setStatus(PostConstants.STATUS_THREE);//强制下线操作后状态为3=已下线
		result+=contentMapper.updateCont(knowledgeContent);
		//从ES中删除
		businessContentService.deleteSearchAndEs(knowledgeContent.getId());
		//保存操作记录 - 强制下线=18
		result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_EIGHTEEN,knowledgeContent.getReason());
		return result;
	}
	/**
     * 强制作废
     */
	@Override
	@Transactional
	public int forceTrash(KnowledgeContent knowledgeContent) {
		int result = 0;
		knowledgeContent.setStatus(PostConstants.STATUS_NINE);//强制作废操作后状态为9=已作废
		result+=contentMapper.updateCont(knowledgeContent);
		//从ES中删除
		businessContentService.deleteSearchAndEs(knowledgeContent.getId());
		//保存操作记录 - 强制下线=19
		result+=operationHistoryService.insertOperationHistory(knowledgeContent.getId(),PostConstants.OPERATION_NINETEEN,knowledgeContent.getReason());
		return result;
	}

	@Override
	public List<KnowledgeContent> monitoringSearchtList(KnowledgeContent knowledgeContent) {
		String userId = ShiroUtils.getOgUser().getUserId();
		List<String> status = new ArrayList<>();
        String cstatus = businessContentService.getStatusByUserId(userId);
        
        //初始化或者我创建的
        if(StringUtils.isEmpty(knowledgeContent.getStatus()) || "0".equals(knowledgeContent.getStatus())){
            knowledgeContent.setCreateId(userId);
        }else if("1".equals(knowledgeContent.getStatus())){//待审核的
            if("4".equals(cstatus)){//二线管理员
                status.add(PostConstants.STATUS_ZERO);
                status.add(PostConstants.STATUS_ONE);
                status.add(PostConstants.STATUS_FIVE);
                status.add(PostConstants.STATUS_SEVEN);
                knowledgeContent.setTwoAuditorId(userId);
            }else {//一线管理员
                status.add(PostConstants.STATUS_FOUR);
                status.add(PostConstants.STATUS_SIX);
                status.add(PostConstants.STATUS_EIGHT);
                knowledgeContent.setOneAuditorId(userId);
            }
        }else if("2".equals(knowledgeContent.getStatus())){//已审核的
            if("4".equals(cstatus)){//二线管理员
                status.add(PostConstants.STATUS_TWO);
                status.add(PostConstants.STATUS_THREE);
                status.add(PostConstants.STATUS_FOUR);
                status.add(PostConstants.STATUS_SIX);
                status.add(PostConstants.STATUS_EIGHT);
                status.add(PostConstants.STATUS_NINE);
                knowledgeContent.setTwoAuditorId(userId);
            }else {//一线管理员
                status.add(PostConstants.STATUS_TWO);
                status.add(PostConstants.STATUS_THREE);
                status.add(PostConstants.STATUS_NINE);
                knowledgeContent.setOneAuditorId(userId);
            }
        }
        startPage();
		return contentMapper.monitoringSearchtList(knowledgeContent,status.isEmpty()?null:status);
	}

	/**
	 * 导入告警数据
	 * @param alarmList 告警数据
	 * @return 结果
	 */
	@Override
	@Transactional
	public String importAlarmData(List<KnowledgeAlarmExample> alarmList) {
		if (StringUtils.isNull(alarmList) || alarmList.size() == 0)
		{
			throw new BusinessException("导入数据不能为空！");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		contentMapper.deleteAlarmData();
		List<KnowledgeAlarmExample> list = new ArrayList<>();
		for (KnowledgeAlarmExample alarmExample : alarmList)
		{
			try
			{
				alarmExample.setId(UUID.getUUIDStr());
				list.add(alarmExample);
				successNum++;
			}
			catch (Exception e)
			{
				failureNum++;
				String msg = "<br/>" + failureNum + "、对象 " + alarmExample.getContent() + " 导入失败：";
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
		contentMapper.insertAlarmData(list);
		return successMsg.toString();
	}

	/**
	 * 导入告警数据
	 * @param alarmList 告警数据
	 * @return 结果
	 */
	@Override
	@Transactional
	public String importAlarmKnowledge(List<KnowledgeAlarmImport> alarmList) {
		if (StringUtils.isNull(alarmList) || alarmList.size() == 0)
		{
			throw new BusinessException("导入数据不能为空！");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		contentMapper.deleteAlarmData();
		List<KnowledgeAlarmImport> list = new ArrayList<>();
		for (KnowledgeAlarmImport alarmExample : alarmList)
		{
			try
			{
				if(list.size()>importDataCount){
					contentMapper.importAlarmKnowledge(list);
					list = new ArrayList<>();
				}
				alarmExample.setId(UUID.getUUIDStr());
				list.add(alarmExample);
				successNum++;
			}
			catch (Exception e)
			{
				failureNum++;
				String msg = "<br/>" + failureNum + "、对象 " + alarmExample.getContent() + " 导入失败：";
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
		contentMapper.importAlarmKnowledge(list);
		return successMsg.toString();
	}

	@Override
	@Transactional
	public void updateDescribeCont(KnowledgeContent knowledgeContent){
		contentMapper.updateDescribeCont(knowledgeContent);
	}

	@Override
	@Transactional
	public void updateConcatDescribeCont(KnowledgeContent knowledgeContent){
		contentMapper.updateConcatDescribeCont(knowledgeContent);
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
	 * 保存模板
	 * @param knowledgeContent
	 * @return
	 */
	@Override
	@Transactional
	public int savetemplate(KnowledgeContent knowledgeContent){
		knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
		knowledgeContent.setCreateTime(DateUtils.getTime());
		knowledgeContent.setStatus("-1");
		if(contentMapper.selectContById(knowledgeContent.getId()) == null){
			return contentMapper.insertContent(knowledgeContent);
		}else{
			updateDescribeCont(knowledgeContent);
			return contentMapper.updateCont(knowledgeContent);
		}
	}
}
