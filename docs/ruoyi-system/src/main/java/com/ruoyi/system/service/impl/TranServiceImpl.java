package com.ruoyi.system.service.impl;

import com.gsoft.cps.forward.api.data.ApiPager;
import com.gsoft.cps.forward.api.data.ApiReqData;
import com.gsoft.cps.forward.netty.msg.client.TranMsg;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.esb.data.PubContext;
import com.ruoyi.common.esb.data.ResContext;
import com.ruoyi.common.esb.invoke.ServiceInvoker;
import com.ruoyi.common.netty.filter.AbstractTranFilter;
import com.ruoyi.common.netty.service.TranService;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.SecurityUtils;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("apiTranService")
@Transactional
public class TranServiceImpl implements TranService, ApplicationContextAware {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ServiceInvoker serviceInvoker;
    @Autowired
    private IOgUserService ogUserService;
    @Autowired
    private ISysRoleService iSysRoleService;
    private Map<String, AbstractTranFilter> tranFilters;

    @Override
    public ResContext<?> excute(TranMsg tranMsg) throws Exception {
        ApiReqData apiReqData = tranMsg.getApiReqData();

        ResContext<?> res = new ResContext<Record>();

        AbstractTranFilter tranFilter = tranFilters.get(tranMsg.getTrancode());

        String[] tranArray = tranMsg.getTrancode().split("\\.");
        // 组装pub参数
        String beanName = tranArray[0];
        String methodName = tranArray[1];

        PubContext pubContext = buildPubContext(apiReqData);
        SecurityUtils.addSecurityInfo(pubContext);

        Map<String, List<Object>> params = buildReqMap(apiReqData);

        if (tranFilter != null) {
            tranFilter.reqFilter(params);
        }
        res = serviceInvoker.invoke(beanName, methodName, params, pubContext);

        if (tranFilter != null) {
            tranFilter.resFilter(res);
        }

        return res;
    }

    /**
     * 组装请求
     *
     * @param apiReqData
     * @return
     */
    private Map<String, List<Object>> buildReqMap(ApiReqData apiReqData) {
        Map<String, List<Object>> reqParams = new HashMap<>();
        Map<String, String> params = apiReqData.getParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            List<Object> values = new ArrayList<Object>(1);
            values.add(entry.getValue());
            reqParams.put(entry.getKey(), values);
        }

        ApiPager pager = apiReqData.getPager();
        if (pager != null) {
            List<Object> pageSize = new ArrayList<Object>(1);
            pageSize.add(String.valueOf(pager.getPageSize()));
            reqParams.put("pager:pageSize", pageSize);

            List<Object> pageIndex = new ArrayList<Object>(1);
            pageIndex.add(String.valueOf(pager.getPageIndex()));
            reqParams.put("pager:pageIndex", pageIndex);

            List<Object> pageType = new ArrayList<Object>(1);
            pageType.add(String.valueOf(Pager.QUERY_TYPE_ALL));
            reqParams.put("pager:pageType", pageType);
        }

        return reqParams;
    }

    /**
     * 组装PubContext
     *
     * @param apiReqData
     * @return
     */
    private PubContext buildPubContext(ApiReqData apiReqData) {
        PubContext pubContext = new PubContext();
        OgUser ogUser = ogUserService.selectUserByPhoneNumber(apiReqData.getPhone());
        if (ogUser != null) {
            List<OgRole> roleList = iSysRoleService.selectRolesByUserId(ogUser.getUserId());
            List<String> rlist = new ArrayList<>();
            String roleIds = "";
            if (!roleList.isEmpty()) {
                for (OgRole role : roleList) {
                    rlist.add(role.getRid());
                }
                roleIds = StringUtils.join(rlist, ",");
            }
            pubContext.setUsername(ogUser.getUserId());
            pubContext.setRoles(roleIds);
            pubContext.addParam("userId", ogUser.getUserId());
            pubContext.addParam("roleIds", roleIds);

        } else {
            throw new BusException("手机号不存在:" + apiReqData.getPhone());
        }
        return pubContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initFilters(applicationContext);
    }

    /**
     * 初始化交易拦截器
     *
     * @param applicationContext
     */
    private void initFilters(ApplicationContext applicationContext) {
        if (tranFilters == null) {
            tranFilters = new HashMap<>();
            Map<String, AbstractTranFilter> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, AbstractTranFilter.class, true,
                    false);
            for (Map.Entry<String, AbstractTranFilter> entry : map.entrySet()) {
                tranFilters.put(entry.getValue().getTranName(), entry.getValue());
            }
        }
        logger.info("tran-加载交易filter:" + tranFilters);
    }

}
