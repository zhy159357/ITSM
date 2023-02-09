package com.ruoyi.activiti.controller.itsm.common;

import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用controller
 *
 * @author 14735
 */
@Controller
@RequestMapping("/public")
public class PublicCommonController extends BaseController {

    @Autowired
    private ICommonService commonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService ogPersonService;

    /**
     * 方法说明，
     * pflag复用为标识，如果为1，则查询条件是rid和机构层级码右like查询
     * 如果不为1使用rid和orgId查询，需要前端页面传递参数
     * person对象中的rid参数必传
     *
     * @param person
     * @return
     */
    @PostMapping("/selectPersonByOrgIdOrOrgLvAndRole")
    @ResponseBody
    public TableDataInfo selectPersonByOrgOrOrgLvAndRole(OgPerson person) {
    //    OgPerson person1 = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
//        OgOrg org = deptService.selectDeptById(person1.getOrgId());
//        String under = person.getPflag();
//        String orgId = org.getOrgId();
//        if ("1".equals(under)) {
//            while (true) {
//                String level = org.getOrgLv();
//                // 当前登录人为一级机构，邮政金融运维，返回一级机构id
//                if ("2".equals(level) || "1".equals(level)) {
//                    orgId = org.getOrgId();
//                    break;
//                } else {
//                    if (StringUtils.isNotEmpty(org.getParentId())) {
//                        org = deptService.selectDeptById(org.getParentId());
//                    }
//                }
//            }
//            OgOrg org2 = deptService.selectDeptById(orgId);
//
//            String levelCode = org2.getLevelCode();
//            OgOrg org1 = new OgOrg();
//            org1.setLevelCode(levelCode);
//            person.setOrg(org1);
//        }
        if (person.getParams() != null && person.getParams().containsKey("isselect") && "1".equals(person.getParams().get("isselect"))) {
        } else {
            startPage();
        }
        person.setrId(null);
        List<OgPerson> list = commonService.selectPersonByOrgAndRole(person);
        return getDataTable(list);
    }

    /**
     * 方法说明
     * 查询人员信息根据根据机构或者机构层级和角色id
     *
     * @param person
     * @return
     */
    @PostMapping("/selPersonByOrgOrOrgLvAndRole")
    @ResponseBody
    public AjaxResult selPersonByOrgOrOrgLvAndRole(OgPerson person) {
        AjaxResult ajaxResult = new AjaxResult();
        OgPerson person1 = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        OgOrg org = deptService.selectDeptById(person1.getOrgId());
        String orgid = org.getOrgId();
        String under = person.getPflag();
        if ("1".equals(under)) {
            while (true) {
                String level = org.getOrgLv();
                if ("2".equals(level) || "1".equals(level)) {// 当前登录人为一级机构，邮政金融运维，返回一级机构id---liuyang
                    orgid = org.getOrgId();
                    break;
                } else {
                    if (org.getParentId() != null) {
                        org = deptService.selectDeptById(org.getParentId());
                    }
                }
            }
            String levelCode = deptService.selectDeptById(orgid).getLevelCode();
            OgOrg org1 = new OgOrg();
            org1.setLevelCode(levelCode);
            person.setOrg(org1);
        }
        List<OgPerson> list = commonService.selectPersonByOrgAndRole(person);
        ajaxResult.put("data", list);
        return ajaxResult;
    }

    /**
     * 查询数据中心和科技部人员信息
     * @param person
     * @return
     */
    @PostMapping("/selectOgPersonByPostIds")
    @ResponseBody
    public TableDataInfo selectOgPersonByPostIds(OgPerson person){
        List<String> postIds = new ArrayList();
        // 岗位id为数据中心人员｜数据中心领导｜科技部人员｜科技部领导
        postIds.add("0015");
        postIds.add("0016");
        postIds.add("0010");
        postIds.add("0011");
        person.getParams().put("postIds",postIds);
        startPage();
        return getDataTable(ogPersonService.selectOgPersonByPostIds(person));
    }
}
