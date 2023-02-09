package com.ruoyi.activiti.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.gosft.tools.core.util.ObjectUtil;
import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.mapper.FmSawoMapper;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.activiti.utils.RestTemplateUtil;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【态势感知工单】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Service
public class FmSawoServiceImpl implements IFmSawoService
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FmSawoServiceImpl.class);
    @Autowired
    private FmSawoMapper fmSawoMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IOgPersonService personService;

    @Value("${tsgz.url}")
    private String url;

    @Value("${tsgz.secret}")
    private String secret_;

    @Value("${tsgz.appid}")
    private String appid_;

    /*@Value("${signature.apps.id}")
    private String id;

    @Value("${signature.apps.secret}")
    private String secret;*/

    /**
     * 查询【请填写功能名称】
     *
     * @param ordId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public FmSawo selectFmSawoById(String ordId)
    {
        return fmSawoMapper.selectFmSawoById(ordId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param fmSawo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FmSawo> selectFmSawoList(FmSawo fmSawo)
    {
        return fmSawoMapper.selectFmSawoList(fmSawo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param fmSawo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFmSawo(FmSawo fmSawo)
    {
        return fmSawoMapper.insertFmSawo(fmSawo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param fmSawo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFmSawo(FmSawo fmSawo)
    {
        return fmSawoMapper.updateFmSawo(fmSawo);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmSawoByIds(String ids)
    {
        return fmSawoMapper.deleteFmSawoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param ordId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteFmSawoById(String ordId)
    {
        return fmSawoMapper.deleteFmSawoById(ordId);
    }

    @Override
    public void sendHttpClient(FmSawo fmSawo) {

        //https://20.198.81.136/openapi/v1/itsm/orderStatus
        //String url = "https://127.0.0.1:8443/sawo/newly/addSave";
        //测试环境地址
        //String url = "https://20.198.81.136/openapi/v1/itsm/orderStatus";
        //调态势感知的方法
        JSONObject parameters = new JSONObject();
        //工单编号
        parameters.put("orderId", fmSawo.getOrderId());
        //工单状态
        parameters.put("orderStatus", fmSawo.getDisposeResult());
        //处理人名称
        OgPerson ogPerson = personService.selectOgPersonById(fmSawo.getDealId());
        parameters.put("dealWithName", ogPerson.getpName());//转换成姓名
        //处置人所属机构
        parameters.put("dealWithOrganization", fmSawo.getDealDept());
        String time = null;
        //处置时间
        if(StringUtils.isNotEmpty(fmSawo.getDealTime())){
            try{
                time = DateUtils.timeToTimeMillis(fmSawo.getDealTime());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        parameters.put("disposalTime", time);
        //备注
        parameters.put("remark", fmSawo.getRem());

        //map封装
        Map<JSONObject, JSONObject> map=new HashMap<JSONObject,JSONObject>();
        map.put(parameters , parameters);
        Map<String ,String> mapHeader = getSign();

        int status =httpsPost(url,parameters,mapHeader);
        if(status != 200){
            log.debug("数据异常————————————————————————————>" +status);
        }
//        HttpURLConnection con = null;
//        try {
//            URL realUrl = new URL(url);
//            con = (HttpURLConnection) realUrl.openConnection();
//            con.setConnectTimeout(3000);
//            con.setReadTimeout(3000);
//            con.setUseCaches(false);
//            con.setDoOutput(true);
//            con.setRequestMethod("POST");
//            con.setRequestProperty("content-type", "application/json;charset=utf-8");
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
//            writer.print(parameters.toString());
//            writer.close();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
//            String line = reader.readLine();
//            if (line != null && !"".equals(line)) {
//                net.sf.json.JSONObject aa = net.sf.json.JSONObject.fromObject(line);
//                String a = aa.getString("orderStatus");
//                if ("4".equals(a)) {
//                    AjaxResult.error("工单关闭成功");
//                } else {
//                    AjaxResult.error("工单关闭失败");
//                }
//            }
//            reader.close();
//            con.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new BusException("JSON数据异常");
//        } finally {
//            if (con != null) {
//                con.disconnect();
//            }
//        }
    }

    public int httpsPost(String url, JSONObject object, Map<String, String> mapHeader) {
        int status = 0;
        try {
            log.info("https请求------->" + url);
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            if(ObjectUtil.isNotEmpty(mapHeader)){
                mapHeader.entrySet().stream().forEach(p->{
                    headers.add(p.getKey(),p.getValue());
                });
            }
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> httpEntity = new HttpEntity<String>(object.toString(), headers);
            if (url.startsWith("https")) {
                restTemplate = RestTemplateUtil.restTemplate();
            }
            ResponseEntity<String> exchange = restTemplate.postForEntity(url, httpEntity, String.class);
            if (exchange != null) {
                status = exchange.getStatusCodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("请求异常！");
        }
        return status;
    }

    private Map<String, String> getSign() {
        long time = System.currentTimeMillis();
        String appid = appid_;//配置文件
        String timestamp = String.valueOf(time);
        String nonce = "012345678901234567";//18位
        String secret = secret_;//配置文件
        String str =appid+nonce+timestamp+secret;
        String sign = sign(str);
        Map map = new HashMap();
        map.put("appId","stgz0001");
        map.put("timestamp",String.valueOf(time));
        map.put("nonce","012345678901234567");
        map.put("sign",sign);
        return map;
    }

    private static String sign(String str) {
        return SecureUtil.sha256(str).toUpperCase();
    }
}
