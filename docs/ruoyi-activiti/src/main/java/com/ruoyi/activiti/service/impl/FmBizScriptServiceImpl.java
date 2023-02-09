package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.FmBizScriptMapper;
import com.ruoyi.activiti.service.IFmBizScriptService;
import com.ruoyi.common.core.domain.entity.FmBizScript;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.service.IEntegorServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-30
 */
@Service
public class FmBizScriptServiceImpl implements IFmBizScriptService {

    private static final Logger log = LoggerFactory.getLogger(FmBizScriptServiceImpl.class);
    @Autowired
    private FmBizScriptMapper fmBizScriptMapper;
    @Autowired
    private IEntegorServer entegorServer;

    /**
     * 查询【请填写功能名称】
     *
     * @param fbsId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public FmBizScript selectFmBizScriptById(String fbsId) {
        return fmBizScriptMapper.selectFmBizScriptById(fbsId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param fmBizScript 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FmBizScript> selectFmBizScriptList(FmBizScript fmBizScript) {
        return fmBizScriptMapper.selectFmBizScriptList(fmBizScript);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param fmBizScript 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFmBizScript(FmBizScript fmBizScript) {
        return fmBizScriptMapper.insertFmBizScript(fmBizScript);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param fmBizScript 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFmBizScript(FmBizScript fmBizScript) {
        return fmBizScriptMapper.updateFmBizScript(fmBizScript);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizScriptByIds(String ids) {
        return fmBizScriptMapper.deleteFmBizScriptByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param fbsId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteFmBizScriptById(String fbsId) {
        return fmBizScriptMapper.deleteFmBizScriptById(fbsId);
    }

    /**
     * 根据条件搜索未执行完成的脚本进行结果获取
     *
     * @param fbs
     * @return
     */
    @Override
    public FmBizScript getAutoResult(FmBizScript fbs) {
        String result = "";
        if ("运行中".equals(fbs.getResultStatus())) {//如果为终态不再去自动化查询
            Map<String, Object> map = entegorServer.monitorActivityForFlowIdLog(fbs.getFlowId());
            Map<String, Object> map2 = (Map<String, Object>) map.get("content");
            List<Map<String, Object>> map3 = (List<Map<String, Object>>) map2.get("coats");
            List<Map<String, Object>> map4 = (List<Map<String, Object>>) map3.get(0).get("instances");

            String status = map4.get(0).get("flowStateCn").toString();//解析json拿到执行结果
            log.info("flowId为：" + fbs.getFlowId() + "的脚本执行状态为：" + status);
            result = map4.get(0).get("stdout") + "|" + map4.get(0).get("stderror");//解析json拿到执行结果
            log.info("flowId为：" + fbs.getFlowId() + "的脚本stdout执行结果为：" + result);
            fbs.setResultStatus(status);
            fbs.setExecutorResult(result);
            updateFmBizScript(fbs);
        }
        return fbs;
    }
}
