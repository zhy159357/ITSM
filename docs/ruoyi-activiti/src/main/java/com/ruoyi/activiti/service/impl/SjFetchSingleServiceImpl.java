package com.ruoyi.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.domain.SjFetchSingle;
import com.ruoyi.activiti.mapper.SjFetchSingleMapper;
import com.ruoyi.activiti.service.ISjFetchSingleService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-04-07
 */
@Service
public class SjFetchSingleServiceImpl implements ISjFetchSingleService
{
    @Autowired
    private SjFetchSingleMapper sjFetchSingleMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询【请填写功能名称】
     * 
     * @param fetchId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SjFetchSingle selectSjFetchSingleById(String fetchId)
    {
        if("mysql".equals(dbType)){
            return sjFetchSingleMapper.selectSjFetchSingleByIdMysql(fetchId);
        }else{
            return sjFetchSingleMapper.selectSjFetchSingleById(fetchId);
        }
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SjFetchSingle> selectSjFetchSingleList(SjFetchSingle sjFetchSingle)
    {
        if("mysql".equals(dbType)){
            return sjFetchSingleMapper.selectSjFetchSingleListMysql(sjFetchSingle);
        }else{
            return sjFetchSingleMapper.selectSjFetchSingleList(sjFetchSingle);
        }
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSjFetchSingle(SjFetchSingle sjFetchSingle)
    {
        return sjFetchSingleMapper.insertSjFetchSingle(sjFetchSingle);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSjFetchSingle(SjFetchSingle sjFetchSingle)
    {
        return sjFetchSingleMapper.updateSjFetchSingle(sjFetchSingle);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSjFetchSingleByIds(String ids)
    {
        return sjFetchSingleMapper.deleteSjFetchSingleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param fetchId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSjFetchSingleById(String fetchId)
    {
        return sjFetchSingleMapper.deleteSjFetchSingleById(fetchId);
    }


    /**
     * 数据提取单业务处理
     * @param sjFetchSingle
     * @param result
     * @return
     */
    @Override
    public AjaxResult createFetchSingle(@Valid @RequestBody SjFetchSingle sjFetchSingle, BindingResult result) {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String,String> map = new HashMap<>();
        //获取校验的错误结果
        if(result.hasErrors()){
            result.getFieldErrors().forEach((item) ->{
                //获取错误的属性的名字
                String fieldError = item.getField();
                //FieldError 获取到错误提示
                String message = item.getDefaultMessage();
                map.put(fieldError,message);
            });
            return ajaxResult.error("输入数据不符合规则",map);
        }else {
            //1.进行需求来源的判断 OA 和大数据
            if("01".equals(sjFetchSingle.getSourceType())) {
                //重试提交的判断（processId）

               /* //判断文件是否丢失
                if(){

                }*/
            }else  if("02".equals(sjFetchSingle.getSourceType())){

            }
            sjFetchSingleMapper.insertSjFetchSingle(sjFetchSingle);
        }
        return ajaxResult;

    }

    @Override
    public SjFetchSingle selectSjFetchSingleByProcessId(String processId) {
        if("mysql".equals(dbType)){
            return sjFetchSingleMapper.selectSjFetchSingleByProcessIdMysql(processId);
        }else{
            return sjFetchSingleMapper.selectSjFetchSingleByProcessId(processId);
        }
    }

    @Override
    public List<SjFetchSingle> selectListByTask(SjFetchSingle sjFetchSingle) {
        if("mysql".equals(dbType)){
            return sjFetchSingleMapper.selectListByTaskMysql(sjFetchSingle);
        }else{
            return sjFetchSingleMapper.selectListByTask(sjFetchSingle);
        }
    }

    @Override
    public int updateSjFetchSingleStatus(SjFetchSingle sjFetchSingle) {
        return sjFetchSingleMapper.updateSjFetchSingleStatus(sjFetchSingle);
    }
}
