package com.ruoyi.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

public class JsonUtil
{
    /**
     * 从一个JSON数组得到一个java对象集合
     *
     * @param
     * @param clazz
     * @return
     */
    public static List getDTOList ( String jsonString, Class clazz )
    {
        setDataFormat2JAVA();
        JSONArray array = JSONArray.fromObject(jsonString);
        List list = new ArrayList();
        for (Iterator iter = array.iterator(); iter.hasNext();)
        {
            JSONObject jsonObject = (JSONObject) iter.next();
            list.add(JSONObject.toBean(jsonObject, clazz));
        }
        return list;
    }

    private static void setDataFormat2JAVA ()
    {
        JSONUtils.getMorpherRegistry()
                .registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }));
    }

    /**
     * 从json HASH表达式中获取一个map，该map支持嵌套功能 形如：{"id" : "johncon", "name" : "小强"}
     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap
     *
     * @param
     * @return
     */
    public static Map getMapFromJson ( String jsonString )
    {
        setDataFormat2JAVA();
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Map map = new HashMap();
        for (Iterator iter = jsonObject.keys(); iter.hasNext();)
        {
            String key = (String) iter.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }

    /**
     * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性
     *
     * @param
     * @param clazz
     * @param map 集合属性的类型
     * @return
     */
    public static List getDTOList ( String jsonString, Class clazz, Map map )
    {
        setDataFormat2JAVA();
        JSONArray array = JSONArray.fromObject(jsonString);
        List list = new ArrayList();
        for (Iterator iter = array.iterator(); iter.hasNext();)
        {
            JSONObject jsonObject = (JSONObject) iter.next();
            list.add(JSONObject.toBean(jsonObject, clazz, map));
        }
        return list;
    }

    /**
     * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" :
     * nameValue}, ...]
     *
     * @param
     * @param clazz
     * @param map
     * @return
     */
    public static Object[] getDTOArray ( String jsonString, Class clazz, Map map )
    {
        setDataFormat2JAVA();
        JSONArray array = JSONArray.fromObject(jsonString);
        Object[] obj = new Object[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject jsonObject = array.getJSONObject(i);
            obj[i] = JSONObject.toBean(jsonObject, clazz, map);
        }
        return obj;
    }

    /**
     * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" :
     * nameValue}, ...]
     *
     * @param
     * @param clazz
     * @return
     */
    public static Object[] getDTOArray ( String jsonString, Class clazz )
    {
        setDataFormat2JAVA();
        JSONArray array = JSONArray.fromObject(jsonString);
        Object[] obj = new Object[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject jsonObject = array.getJSONObject(i);
            obj[i] = JSONObject.toBean(jsonObject, clazz);
        }
        return obj;
    }

    /**
     * 从一个JSON 对象字符格式中得到一个java对象，其中beansList是一类的集合，形如： {"id" : idValue, "name" : nameValue, "aBean"
     * : {"aBeanId" : aBeanIdValue, ...}, beansList:[{}, {}, ...]}
     *
     * @param jsonString
     * @param clazz
     * @param map 集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" : Bean.class)
     * @return
     */
    public static Object getDTO ( String jsonString, Class clazz, Map map )
    {
        JSONObject jsonObject = null;
        try
        {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return JSONObject.toBean(jsonObject, clazz, map);
    }

    /**
     * 从一个JSON 对象字符格式中得到一个java对象，形如： {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" :
     * aBeanIdValue, ...}}
     *
     * @param
     * @param clazz
     * @return
     */
    public static Object getDTO ( String jsonString, Class clazz )
    {
        JSONObject jsonObject = null;
        try
        {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return JSONObject.toBean(jsonObject, clazz);
    }

    /**
     * string to list
     * @Title: getMapFromJsonList
     * @Description: string to list
     * @param jsonString
     * @return
     * @author: Administrator
     * @date:   2019年2月15日 上午8:24:41
     */
    public static List getMapFromJsonList ( String jsonString )
    {
        setDataFormat2JAVA();
        List list = new ArrayList();
        JSONArray array = JSONArray.fromObject(jsonString);
        Object[] obj = new Object[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            Map map = new HashMap();
            JSONObject jsonObject = array.getJSONObject(i);
            obj[i] = JSONObject.toBean(jsonObject);
            for (Iterator iter = jsonObject.keys(); iter.hasNext();)
            {
                String key = (String) iter.next();
                map.put(key, jsonObject.get(key));
            }
            list.add(map);
        }
        return list;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void sort ( List<Map<String, Object>> targetList, final String sortField, final String sortMode )
    {
        Collections.sort(targetList, new Comparator<Map<String, Object>>()
        {
            public int compare ( Map<String, Object> o1, Map<String, Object> o2 )
            {
                Integer name1 = Integer.valueOf(o1.get(sortField).toString());// name1是从你list里面拿出来的一个
                Integer name2 = Integer.valueOf(o2.get(sortField).toString()); // name1是从你list里面拿出来的第二个name
                return name1.compareTo(name2);
            }
        });
    }

    public static List getJsonToList ( String jsonString, Class clazz )
    {
        setDataFormat2JAVA();
        JSONArray array = JSONArray.fromObject(jsonString);
        List list = new LinkedList();
        for (Iterator iter = array.iterator(); iter.hasNext();)
        {
            JSONObject jsonObject = (JSONObject) iter.next();
            list.add(JSONObject.toBean(jsonObject, clazz));
        }
        return list;
    }

    /**
     *
     * <li>Description:Json转ListBean</li>
     * @author jian_wu
     * 2019年2月18日
     * @param json
     * @param clazz
     * @return
     * return List<T>
     */
    public static <T> List<T> getListModelByJson ( String json, Class<T> clazz )
    {
        return JSON.parseArray(json, clazz);
    }

    /**
     *
     * <li>Description：ListBean转Json</li>
     * @author jian_wu
     * 2019年2月18日
     * @param list
     * @return
     * return String
     */
    public static <T> String getJsonByListModel ( List<T> list )
    {
        String result = "";
        if (null != list)
        {
            result = JSON.toJSONString(list);
        }
        return result;
    }

    public static <T> String getJsonByMapModel ( Map map )
    {
        String result = "";
        if (null != map)
        {
            result = JSON.toJSONString(map);
        }
        return result;
    }

    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static String readJsonFile ( String fileName )
    {
        String jsonStr = "";
        Reader reader = null;
        try
        {
            File jsonFile = new File(fileName);
            reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1)
            {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            try
            {
                if (null != reader)
                {
                    reader.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从json HASH表达式中获取一个LinkedHashMap，该map支持嵌套功能 形如：{"id" : "johncon", "name" : "小强"}
     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap
     *
     * @param
     * @return
     */
    public static Map getLinkedHashMapFromJson ( String jsonString )
    {
        setDataFormat2JAVA();
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Map map = new LinkedHashMap();
        for (Iterator iter = jsonObject.keys(); iter.hasNext();)
        {
            String key = (String) iter.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }


    public static Object jsonStr(String json,String key){
        JSONObject jsonObject = JSONObject.fromObject(json);
        return jsonObject.get(key);
    }
}