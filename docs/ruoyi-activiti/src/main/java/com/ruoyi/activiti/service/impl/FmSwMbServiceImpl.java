package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.FmSwMb;
import com.ruoyi.activiti.mapper.FmSwMbMapper;
import com.ruoyi.activiti.service.IFmSwMbService;
import com.ruoyi.common.core.text.Convert;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FmSwMbServiceImpl implements IFmSwMbService {

    @Autowired
    private FmSwMbMapper fmSwMbMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;
    /**
     * 添加模板
     * @param fmSwMb
     * @return
     */
    @Override
    public int insertFmSwMb(FmSwMb fmSwMb) {
        return fmSwMbMapper.insertFmSwMb(fmSwMb);
    }

    /**
     * 根据条件查询对应的模板信息
     * @param fmSwMb
     * @return
     */
    @Override
    public List<FmSwMb> selectFmSwMbList(FmSwMb fmSwMb) {
        if("mysql".equals(dbType)){
            return fmSwMbMapper.selectFmSwMbListMysql(fmSwMb);
        }else{
            return fmSwMbMapper.selectFmSwMbList(fmSwMb);
        }
    }

    /**
     * 根据模板id删除模板数据
     * @param ids
     * @return
     */
    @Override
    public int deleteFmSwMbByIds(String ids) {
        String[] fmSwMbByIds = Convert.toStrArray(ids);
        return fmSwMbMapper.deleteFmSwMbByIds(fmSwMbByIds);
    }

    /**
     * 根据id查询对应的模板信息
     * @param id
     * @return
     */
    @Override
    public FmSwMb selectById(String id) {
        return fmSwMbMapper.selectById(id);
    }

    /**
     * 更新模板信息
     * @param fmSwMb
     * @return
     */
    @Override
    public int updateFmSwMb(FmSwMb fmSwMb) {
        return fmSwMbMapper.updateFmSwMb(fmSwMb);
    }

    /**
     * 根据受理处室和请求事项判断模板信息是否存在
     * @param dealOrgId
     * @param faultKind
     * @return
     */
    @Override
    public Boolean checkExist(@Param("dealOrgId") String dealOrgId, @Param("faultKind") String faultKind) {
        FmSwMb fmSwMb = new FmSwMb();
        fmSwMb.setDealOrgId(dealOrgId);
        fmSwMb.setFaultKind(faultKind);
        FmSwMb selFmSwMb = fmSwMbMapper.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
        if(selFmSwMb!=null){
            return true;
        }
        return false;
    }

    /**
     * 根据受理处室和请求事项查询对应的模板信息
     * @param fmSwMb
     * @return
     */
    @Override
    public FmSwMb selectFmSwMbByDealOrgIdAndFaultKind(FmSwMb fmSwMb) {
        return fmSwMbMapper.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
    }

    /**
     * 根据受理处室查询对应的模板信息
     * @param fmSwMb
     * @return
     */
    @Override
    public List<FmSwMb> selectFmSwMbByDealOrgId(FmSwMb fmSwMb) {
        return fmSwMbMapper.selectFmSwMbByDealOrgId(fmSwMb);
    }
}
