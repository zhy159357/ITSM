package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据提取单controller
 * @author 14735
 */
@Controller
@RequestMapping("/draw")
public class DrawController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl = "http://localhost:9999/draw";
    private String receiveUrl = "/receive";

    @PostMapping("/send")
    @ResponseBody
    public JSONObject send() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("uuid", "12345678");
        params.add("companyId", "6666666");
        File file1 = new File("D:/image/812661601.png");
        File file2 = new File("D:/Java8新特性.pptx");
        FileSystemResource resource1 = new FileSystemResource(file1);
        FileSystemResource resource2 = new FileSystemResource(file2);
        List<FileSystemResource> files = new ArrayList<>();
        files.add(resource1);
        files.add(resource2);
        for(FileSystemResource file : files) {
            params.add("fileArray",file);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestBody = new HttpEntity<>(params,headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl + receiveUrl, requestBody, String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        System.out.println(jsonObject.toString());
        return jsonObject;
    }

    @PostMapping("/receive")
    @ResponseBody
    public Map receive(@RequestParam("fileArray") MultipartFile[] files, String uuid, String companyId){
        Map<String, Object> hashMap = new HashMap<>();
        for(MultipartFile file : files){
            String originalFilename = file.getOriginalFilename();
            hashMap.put("fileName",originalFilename);
        }
        hashMap.put("uuid",uuid);
        hashMap.put("companyId",companyId);
        return hashMap;
    }
}
