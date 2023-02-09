package com.ruoyi.es.serviceImpl;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.es.api.KnowledgeApi;
import com.ruoyi.es.constant.PostConstants;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.mapper.KnowledgeTitleMapper;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysWorkService;
import com.ruoyi.system.service.OgPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("knowledgeTitleService")
public class KnowledgeTitleServiceImpl implements KnowledgeTitleService {

    @Autowired
    private KnowledgeTitleMapper knowledgeTitleMapper;

    @Autowired
    private OgPostService ogPostService;

    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;
    
    @Autowired
    private KnowledgeBusinessContentService businessContentService;
    
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private KnowledgeApi knowledgeApi;
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询标题列表
     */
    @Override
    public List<KnowledgeTitle> selectKnowledgeTitleList(KnowledgeTitle knowledgeTitle) {
        if("mysql".equals(dbType)){
            return knowledgeTitleMapper.selectKnowledgeTitleMySqlList(knowledgeTitle);
        }else {
            return knowledgeTitleMapper.selectKnowledgeTitleList(knowledgeTitle);
        }
    }

    /**
     * 新增标题
     */
    @Override
    public int insertKnowledgeTitle(KnowledgeTitle knowledgeTitle) {
        return knowledgeTitleMapper.insertKnowledgeTitle(knowledgeTitle);
    }

    /**
     * id查询标题
     */
    @Override
    public KnowledgeTitle selectKnowledgeTitleById(String id) {
        return knowledgeTitleMapper.selectKnowledgeTitleById(id);
    }

    /**
     * 修改标题
     */
    @Override
    public int updateKnowledgeTitle(KnowledgeTitle knowledgeTitle) {
        return knowledgeTitleMapper.updateKnowledgeTitle(knowledgeTitle);
    }

    /**
     * 删除标题
     */
    @Override
    public int deleteKnowledgeTitleById(String id) {
        return knowledgeTitleMapper.deleteKnowledgeTitleById(id);
    }

    /**
     * 标题是否被使用
     */
    @Override
    public boolean isUsed(String id) {
        return knowledgeTitleMapper.isUsed(id) > 0;
    }


    /**
     * 获取有工作组的应用系统
     *
     * @return
     */
    public List<OgSys> getOgSys() {
        String pId = ShiroUtils.getOgUser().getpId();
        List<OgPost> plist = ogPostService.selectLoginUserOgPosts(pId);
        StringBuilder s = new StringBuilder();
        List<OgSys> result = new ArrayList<>();
        for (OgPost p : plist) {
            s.append(p.getPostId()).append(";");
        }
        if (s.toString().contains(PostConstants.SUPERADMIN) || s.toString().contains(PostConstants.ONEADMIN) || s.toString().contains(PostConstants.TWOADMIN)) {
            //管理员admin 或 一二线管理
            OgSys sys = new OgSys();
            sys.setInvalidationMark("1");
            result = sysApplicationManagerService.selectOgSysListByCondition(sys);//必须为有工作组的系统
        }
        return result;
    }

    /**
     * 获取全部应用系统
     *
     * @return
     */
    public List<OgSys> getOgSysAll() {
        String pId = ShiroUtils.getOgUser().getpId();
        List<OgPost> plist = ogPostService.selectLoginUserOgPosts(pId);
        StringBuilder s = new StringBuilder();
        List<OgSys> result = new ArrayList<>();
        for (OgPost p : plist) {
            s.append(p.getPostId()).append(";");
        }
        if (s.toString().contains(PostConstants.SUPERADMIN) || s.toString().contains(PostConstants.ONEADMIN) || s.toString().contains(PostConstants.TWOADMIN)) {
            //管理员admin 或 一二线管理 
            OgSys sys = new OgSys();
            sys.setInvalidationMark("1");
            result = sysApplicationManagerService.selectOgSysList(sys);
        }
        return result;
    }

    /**
     * 修改标题=应用系统的标题
     */
    @Override
    public int updateKnowledgeTitleSys(KnowledgeTitle knowledgeTitle) {
        return knowledgeTitleMapper.updateKnowledgeTitleSys(knowledgeTitle);
    }

    /**
     * 检测标题=应用系统的标题唯一性
     */
    @Override
    public boolean checkSysUnique(KnowledgeTitle knowledgeTitle) {
        if (StringUtils.isEmpty(knowledgeTitle.getSysId())) {
            return false;//没有sysid不做校验
        }
        return knowledgeTitleMapper.checkSysUnique(knowledgeTitle) > 0;
    }

    /**
     * 二线厂商,查询标题=应用系统时仅能查询自身所属机构的
     */
    @Override
    public List<KnowledgeTitle> selectKnowledgeTitleSysList(KnowledgeTitle knowledgeTitle) {
        String pId = ShiroUtils.getOgUser().getpId();
        List<KnowledgeTitle> result;       
        String status = businessContentService.getStatusByUserId(ShiroUtils.getOgUser().getUserId());

        if("mysql".equals(dbType)){
            if("3".equals(status)){//当前为二线厂商
                String orgId = ogPersonService.selectOgPersonById(pId).getOrgId();
                result = knowledgeTitleMapper.selectKnowledgeTitleSysMySqlList(knowledgeTitle, orgId);
            } else {
                result = knowledgeTitleMapper.selectKnowledgeTitleMySqlList(knowledgeTitle);
            }
        }else {
            if("3".equals(status)) {//当前为二线厂商
                String orgId = ogPersonService.selectOgPersonById(pId).getOrgId();
                result = knowledgeTitleMapper.selectKnowledgeTitleSysList(knowledgeTitle, orgId);
            }else {
                result = knowledgeTitleMapper.selectKnowledgeTitleList(knowledgeTitle);
            }
        }
        return result;
    }

    public List<Ztree> selectTitleTree(KnowledgeTitle knowledgeTitle) {
        return initZtree(knowledgeTitleMapper.selectTitleTree(knowledgeTitle));
    }

    public List<Ztree> initZtree(List<KnowledgeTitle> categoryList) {
        return initZtree(categoryList, null);
    }

    public List<Ztree> initZtree(List<KnowledgeTitle> categoryList, List<String> roleDeptList) {
        List<Ztree> ztrees = new ArrayList<>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (KnowledgeTitle category : categoryList) {
            if ("0".equals(category.getCategory())) {
                category.setName(category.getSysName());
            }
            Ztree ztree = new Ztree();
            ztree.setId(category.getId());
            ztree.setpId(category.getParentId());
            ztree.setName(category.getName());
            ztree.setTitle(category.getName());
            if (isCheck) {
                ztree.setChecked(roleDeptList.contains(category.getId() + category.getName()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 获取有工作组的应用系统
     *
     * @return
     */
    public List<OgSys> getOgSyss() {
        String userId = ShiroUtils.getOgUser().getUserId();
        List<OgGroup> groupList = iSysWorkService.selectLoginGroups(userId);//获取当前登陆人拥有哪些工作组
        List<OgSys> result = new ArrayList<>();
        List<String> StringList = new ArrayList<>();
        if (!groupList.isEmpty()) {
            for (OgGroup group : groupList) {
                String str[] = group.getSysId().split(",");
                StringList.addAll(Arrays.stream(str).collect(Collectors.toList()));
            }
            result = sysApplicationManagerService.selectOgSysByIds(StringList);//必须为有工作组的系统
        }
        return result;
    }

    @Override
    public List<KnowledgeTitle> selectKnowledgeTitleList2(KnowledgeTitle knowledgeTitle) {
        return knowledgeTitleMapper.selectKnowledgeTitleList2(knowledgeTitle);
    }

    /**
     * 判断给一、二线工作组
     */
    @Override
    public FmBiz getGroupIdByKnowledgek(FmBiz fmBiz) {
        String s = "2";
        List<OgGroup> list = new ArrayList<>();
        //调用知识库接口查询是否存在知识且为一个知识
        List<String> nameList = new ArrayList<>();
        String str = fmBiz.getKeywords();
        if (StringUtils.isNotEmpty(str)) {
            String[] sName = str.split(",");
            if (sName.length > 0) {
                for (String ss : sName) {
                    nameList.add(ss);
                }
            }
        }
        List<KnowledgeContent> knowledgeList = knowledgeApi.searchKnowledge(fmBiz.getSysid(), "", fmBiz.getOneType(), fmBiz.getTwoType(), fmBiz.getThreeType(), nameList);
        if (!knowledgeList.isEmpty() && knowledgeList.size() == 1) {
            fmBiz.setKnowledgeId(knowledgeList.get(0).getId());
            s = "1";
        }
        OgGroup group = new OgGroup();
        group.setSysId(fmBiz.getSysid());
        group.setGroupType(s);
        list = iSysWorkService.selectOgGroupList(group);
        if (list.isEmpty()) {
            OgGroup group2 = new OgGroup();
            group2.setSysId(fmBiz.getSysid());
            group2.setGroupType("2");
            list = iSysWorkService.selectOgGroupList(group2);
        }
        fmBiz.setGroupId(list.get(0).getGroupId());
        return fmBiz;
    }

	@Override
	public List<KnowledgeTitle> selectKnowledgeTitleByName(
			KnowledgeTitle knowledgeTitle) {
		return knowledgeTitleMapper.selectKnowledgeTitleByName(knowledgeTitle);
	}

	@Override
	public void synchronizeMonitoringContent() {
		knowledgeTitleMapper.synchronizeMonitoringContent();		
	}
}
