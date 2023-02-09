package com.ruoyi.form.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BusinessException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 解析design_biz_json_data中的数据
 * 主要是动态填充该json中的key-value
 * 查找依据，查所有的"tag": "el-form-item",key->props:prop:default,value->同级props下的childrens[0].v_model.val
 */
public class VueDataJsonUtil {

    public static String analysisDataJson(String dataJson, Map<String,Object> map) {
        /*if (StringUtils.isNotEmpty(dataJson)) {
            JSONArray json = JSON.parseArray(dataJson);
            JSONObject o = (JSONObject) json.get(0);
            JSONArray childrens = (JSONArray) o.get("childrens");
            System.out.println(childrens.size());
            for (Object children : childrens) {
                JSONObject j = (JSONObject) children;
                JSONArray childrens1 = (JSONArray) j.get("childrens");
                for (Object o1 : childrens1) {
                    JSONObject j1 = (JSONObject) o1;
                    JSONArray childrens2 = (JSONArray) j1.get("childrens");
                    for (Object o2 : childrens2) {
                        JSONObject j2 = (JSONObject) o2;
                        if ("el-form-item".equals(j2.get("tag"))) {
                            JSONObject props = (JSONObject) j2.get("props");
                            String key = (String) ((JSONObject) props.get("prop")).get("default");
                            String label = (String) ((JSONObject) props.get("label")).get("default");

                            JSONArray j3 = (JSONArray) j2.get("childrens");
                            JSONObject vModel = (JSONObject) ((JSONObject)j3.get(0)).get("v_model");
                            if(map.containsKey(key)) {
                                vModel.put("val", map.get(key));
                            }
                            String type = (String) vModel.get("type");
                            Object val = vModel.get("val");
                            if (val == null) {
                                val = "";
                            } else if (val instanceof JSONArray) {
                                JSONArray valArr = (JSONArray) val;
                                val = valArr == null || valArr.size() == 0 ? "" : valArr.get(0);
                            }
                            System.out.println("key=" + key + "====>label=" + label + "||type=" + type + "====>value=" + val);
                        }
                    }
                }
            }
            dataJson = json.toString();
        }*/
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        recursionJson(o, map);
        dataJson = json.toString();
        return dataJson;
    }

    public static void transTest(String dataJson) {
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        Map<String,Object> map = new HashMap<>();
        map.put("eventNo", "1233344");
        long start = System.currentTimeMillis();
        recursionJson(o, map);
        System.out.println(System.currentTimeMillis()-start);
    }

    public static void recursionJson (JSONObject object, Map<String, Object> map) {
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject vModel = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                    if(map.containsKey(key)) {
                        vModel.put("val", map.get(key));
                    }
                } else {
                    recursionJson(j, map);
                }
            }
        } else {
            return;
        }
    }

    /**
     * 设置json 中的默认值
     * @param dataJson
     * @param map
     * @return
     */
    public static String setJsonValue(String dataJson,Map<String,Object> map){
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        setJsonKeyValue(o, map);
        dataJson = json.toString();
        return dataJson;

    }
    public static void setJsonKeyValue(JSONObject object,Map<String,Object> map){
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject j3=j2.getJSONObject(0);
                    //默认值
                    JSONObject vModel =j3.getJSONObject("v_model");
                    if(map.containsKey(key)) {
                        if (!CollectionUtils.isEmpty(j3)) {
                            vModel.put("val",map.get(key));
                        }
                    }
                }else {
                    setJsonKeyValue(j,map);
                }
            }

        }else {
            return;
        }

    }
    /**
     * 设置下拉框下拉列表及默认值
     * @param dataJson
     * @param map
     * @return
     */
    public static String analysisDataJsonSelect(String dataJson, Map<String,Object> map) {

        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        recursionJsonSelect(o, map);
        dataJson = json.toString();
        return dataJson;
    }
    public static String analysisDataJsonSelect(String dataJson, List<Map<String,String>> list, String id) {

        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        recursionJsonSelect(o, list, id);
        dataJson = json.toString();
        return dataJson;
    }
    public static String setOriDept(String dataJson, Map<String,Object> map) {
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject object = (JSONObject) json.get(0);
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {

            aaa: for  (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if ("问题发起人标识".equals(j.get("title"))) {
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    if (!CollectionUtils.isEmpty(j2)) {
                        for (Object ob : jsonArray) {
                            JSONObject ob1 = (JSONObject) ob;
                            JSONArray j3 = (JSONArray) ob1.get("childrens");
                            for (Object b3 : j3) {
                                JSONObject o3 = (JSONObject) b3;
                                JSONArray j4 = (JSONArray) o3.get("childrens");
                                if ("问题发起人".equals(((JSONObject)(j4.get(0))).get("title"))) {
                                    JSONArray o4 = (JSONArray) o3.get("childrens");
                                    JSONObject o1 = (JSONObject) o4.get(0);
                                    JSONArray o5 = (JSONArray)o1.get("childrens");
                                    JSONObject o6 = (JSONObject) o5.get(0);
                                    Object prop = o6.get("props");
                                    JSONObject json1 = (JSONObject) prop;
                                    Object disabled = json1.get("disabled");
                                    JSONObject dis = (JSONObject) disabled;
                                    dis.put("default", false);
                                    break aaa;
                                }
                            }
                        }
                    }
                }
            }
            return json.toString();
        }
        return null;
    }

    public static void recursionJsonSelect (JSONObject object, Map<String, Object> map) {
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject vModel = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                    if(map.containsKey(key)) {
                        JSONArray j3 = (JSONArray) ((JSONObject) j2.get(0)).get("childrens");
                        if (!CollectionUtils.isEmpty(j3)) {
                            JSONObject select=j3.getJSONObject(0);
                            j3.remove(select);
                            Map<String,Object> rMap= (Map<String, Object>) map.get(key);
                            for(Map.Entry<String,Object> entry:rMap.entrySet()){
                                if(!entry.getKey().equals("default")){
                                    JSONObject select1=new JSONObject();
                                    Iterator iterator=select.entrySet().iterator();
                                    while (iterator.hasNext()){
                                        Map.Entry entrys= (Map.Entry) iterator.next();
                                        select1.put((String) entrys.getKey(),entrys.getValue());
                                    }
                                    String jsString="{\"disabled\":{\"sourceDefault\":false,\"default\":false},\"label\":{\"sourceDefault\":\"\",\"default\":\"管理员\"},\"value\":{\"sourceDefault\":\"\",\"default\":\"8b8080f457fffe39015800015ce60006\"}}";
                                    JSONObject js=JSONObject.parseObject(jsString);
                                    js.getJSONObject("label").put("default",entry.getKey());//显示名称
                                    js.getJSONObject("value").put("default",entry.getValue());//键值value
                                    select1.put("props",js);
                                    j3.add(select1);
                                }
                                //默认值
                                if(entry.getKey().equals("default")){
                                    vModel.put("val",entry.getValue());
                                }

                            }
                            ((JSONObject) j2.get(0)).put("childrens",j3);
                        }
                    }
                } else {
                    recursionJsonSelect(j, map);
                }
            }
        } else {
            return;
        }
    }


    public static void recursionJsonSelect (JSONObject object, List<Map<String, String>> list, String id) {
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            aaa: for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("问题发起人标识".equals(j.get("title"))) {
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    if(!CollectionUtils.isEmpty(j2)) {
                        for (Object o1 : j2) {
                            JSONObject j3 = (JSONObject) o1;
                            if("问题发起人标识2".equals(j3.get("title"))) {
                                JSONArray childrensTwo = (JSONArray) j3.get("childrens");
                                if (!CollectionUtils.isEmpty(childrensTwo)) {
                                    JSONArray childrens = (JSONArray) ((JSONObject)childrensTwo.get(0)).get("childrens");
                                    JSONObject props = (JSONObject) ((JSONObject)childrens.get(0)).get("props");
                                    JSONObject vModel = (JSONObject) ((JSONObject)childrens.get(0)).get("v_model");
                                    JSONObject options = (JSONObject)props.get("options");
                                    options.put("default", list);
                                    vModel.put("val", id);
                                    break aaa;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 设置下拉框下拉列表及默认值
     * @param dataJson
     * @param map
     * @return
     */
    public static String anElDataJsonSelect(String dataJson, Map<String,Object> map) {

        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        recursionJsonSelectEl(o, map);
        dataJson = json.toString();
        return dataJson;
    }
    public static void recursionJsonSelectEl (JSONObject object, Map<String, Object> map) {
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject vModel = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                    if(map.containsKey(key)) {
                        JSONArray j3 = (JSONArray) ((JSONObject) j2.get(0)).get("childrens");
                        if (!CollectionUtils.isEmpty(j3)) {
                            JSONObject select=j3.getJSONObject(0);
                            Map<String,Object> rMap= (Map<String, Object>) map.get(key);
                            for(Map.Entry<String,Object> entry:rMap.entrySet()){
                                //默认值
                                if(entry.getKey().equals("default")){
                                    vModel.put("val",entry.getValue());
                                }

                            }
                            ((JSONObject) j2.get(0)).put("childrens",j3);
                        }
                    }
                } else {
                    recursionJsonSelectEl(j, map);
                }
            }
        } else {
            return;
        }
    }
    /**
     * 获取JSON中的指定字段
     * @param
     * @param
     */
    public static String resultValue(JSONObject  object, String key){
        String UserId="";
        try{
            recursionJsonSelectKeyValue(object,key);
        }catch (Exception E){
            UserId=E.getMessage();
        }
        return UserId;
    }
    public static void recursionJsonSelectKeyValue (JSONObject  object, String key) {
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!StringUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key1 = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject vModel = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                    if(key.equals(key1)) {
                        throw new BusinessException(vModel.getString("val"));
                    }
                } else {
                    recursionJsonSelectKeyValue(j, key);
                }
            }
        }
    }
    /**
     * 设置输入框默认值
     */
    public static String analysisDataJsonInput(String dataJson, Map<String,Object> map) {

        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        recursionJsonInput(o, map);
        dataJson = json.toString();
        return dataJson;
    }

    public static void recursionJsonInput (JSONObject object, Map<String, Object> map) {
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject vModel = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                    if(map.containsKey(key)) {
                        JSONArray j3 = (JSONArray) ((JSONObject) j2.get(0)).get("childrens");
                        if (!CollectionUtils.isEmpty(j3)) {
                            JSONObject select=j3.getJSONObject(0);
                            j3.remove(select);
                            Map<String,Object> rMap= (Map<String, Object>) map.get(key);
                            for(Map.Entry<String,Object> entry:rMap.entrySet()){
                                //默认值
                                if(entry.getKey().equals("default")){
                                    vModel.put("val",entry.getValue());
                                }

                            }
                            ((JSONObject) j2.get(0)).put("childrens",j3);
                        }
                    }
                } else {
                    recursionJsonSelect(j, map);
                }
            }
        } else {
            return;
        }
    }
    public static String analysisDataJsonForProblemGeneral(String dataJson, List<Map<String,Object>> peopleList) {
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject object = (JSONObject) json.get(0);
        JSONArray childrensOne = (JSONArray) object.get("childrens");
        if (!CollectionUtils.isEmpty(childrensOne)) {
            for (Object o : childrensOne) {
                JSONObject j = (JSONObject) o;
                    JSONArray childrensTwo = (JSONArray) j.get("childrens");
                    if (!CollectionUtils.isEmpty(childrensTwo)) {
                        JSONObject propsTwo = (JSONObject) ((JSONObject)childrensTwo.get(0)).get("props");
                        JSONObject options = (JSONObject)propsTwo.get("options");
                        options.put("default", peopleList);
                    }
            }
        }
        dataJson = json.toString();
        return dataJson;
    }
    public static void analysisDataJsonForProblemName(JSONObject dataJson, List<Map<String,Object>> peopleList,String name) {

        JSONArray jsonArray = (JSONArray) dataJson.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if("el-form-item".equals(j.get("tag"))) {
                    JSONObject props = (JSONObject) j.get("props");
                    String key = (String) ((JSONObject) props.get("prop")).get("default");
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    JSONObject vModel = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                    if(name.equals(key)) {
                        if (!CollectionUtils.isEmpty(j2)) {
                                JSONObject propsTwo = (JSONObject) ((JSONObject)j2.get(0)).get("props");
                                JSONObject options = (JSONObject)propsTwo.get("options");
                                options.put("default", peopleList);
                                JSONObject model = (JSONObject) ((JSONObject)j2.get(0)).get("v_model");
                                model.put("val", peopleList.get(0).get("value"));
                                break;
                        }
                    }
                }
                else {
                    analysisDataJsonForProblemName(j, peopleList,name);
                }
            }
        }
    }
    /**
     * 根据json中的标签id显示或影藏
     * @param dataJson
     * @param showFlag
     */
    public static String showLabel(String dataJson, String labelId, boolean showFlag) {
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject o = (JSONObject) json.get(0);
        JSONArray jsonArray = (JSONArray)o.get("childrens");
        for(Object j : jsonArray) {
            JSONObject object = (JSONObject) j;
            JSONArray array = (JSONArray)object.get("childrens");
            for(Object a : array) {
                JSONObject aa = (JSONObject) a;
                if(labelId.equals(aa.get("id"))) {
                    aa.put("display", showFlag);
                }
            }
        }
        dataJson = json.toString();
        return dataJson;
    }

    public static String setDisable(String dataJson, boolean flag) {
        JSONArray json = JSON.parseArray(dataJson);
        JSONObject object = (JSONObject) json.get(0);
        JSONArray jsonArray = (JSONArray) object.get("childrens");
        if(!CollectionUtils.isEmpty(jsonArray)) {

            aaa: for  (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if ("问题发起人标识".equals(j.get("title"))) {
                    JSONArray j2 = (JSONArray) j.get("childrens");
                    if (!CollectionUtils.isEmpty(j2)) {
                        for (Object ob : jsonArray) {
                            JSONObject ob1 = (JSONObject) ob;
                            JSONArray j3 = (JSONArray) ob1.get("childrens");
                            for (Object b3 : j3) {
                                JSONObject o3 = (JSONObject) b3;
                                JSONArray j4 = (JSONArray) o3.get("childrens");
                                if ("问题发起人".equals(((JSONObject)(j4.get(0))).get("title"))) {
                                    JSONArray o4 = (JSONArray) o3.get("childrens");
                                    JSONObject o1 = (JSONObject) o4.get(0);
                                    JSONArray o5 = (JSONArray)o1.get("childrens");
                                    JSONObject o6 = (JSONObject) o5.get(0);
                                    Object prop = o6.get("props");
                                    JSONObject json1 = (JSONObject) prop;
                                    Object disabled = json1.get("disabled");
                                    JSONObject dis = (JSONObject) disabled;
                                    dis.put("default", flag);
                                    break aaa;
                                }
                            }
                        }
                    }
                }
            }
            return json.toString();
        }
        return null;
    }
}
