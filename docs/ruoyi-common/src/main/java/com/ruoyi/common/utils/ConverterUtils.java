package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.VmBizInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.util.*;

/***
 * JavaBean对象与Map对象互相转化
 * @author 14735
 */
public class ConverterUtils {

    public static void mapToObject(Object bean, Map<String, Object> map){
        if (map == null){
            return;
        }
        try{
            BeanUtils.populate(bean,map);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List getDTOList (String jsonString, Class clazz )
    {
        JSONArray array = JSONArray.fromObject(jsonString);
        List list = new ArrayList();
        for (Iterator iter = array.iterator(); iter.hasNext();)
        {
            JSONObject jsonObject = (JSONObject) iter.next();
            list.add(JSONObject.toBean(jsonObject, clazz));
        }
        return list;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("versionInfoId","addjdbjhjnjbd22388");
        map.put("versionInfoNo","版本发布测试");
        VmBizInfo vmBizInfo = new VmBizInfo();
        mapToObject(vmBizInfo,map);
        System.out.println(vmBizInfo);
    }
}
