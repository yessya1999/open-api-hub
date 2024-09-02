package com.yess.openapiclientsdk.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yess.openapiclientsdk.model.User;
import com.yess.openapiclientsdk.utils.SignUtils;

import java.util.HashMap;


/**
 * @author yess
 */
public class OpenApiClient {

    private final String accessKey;

    private final String secreteKey;

    private final String GATEWAY_ADDRESS = "http://localhost:8010";

    public OpenApiClient(String accessKey, String secreteKey) {
        this.accessKey = accessKey;
        this.secreteKey = secreteKey;
    }

    public String getNameByGet(String name){
        String url = GATEWAY_ADDRESS + "/api/name/";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result2= HttpUtil.get(url, paramMap);
        return result2;
    }

    public String getNameByPost(String name){
        String url = GATEWAY_ADDRESS + "/api/name/post";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result3= HttpUtil.post(url, paramMap);
        return result3;
    }

    public String getUserNameByPost(User user){
        String json = JSONUtil.toJsonStr(user);
        String url = GATEWAY_ADDRESS + "/api/name/user";

        String result2 = HttpRequest.post(url)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute().body();
        return result2;
    }

    /**
     * 使用map存储 构造请求头
     * @return
     */
    private HashMap<String, String> getHeaderMap(String body){
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", accessKey);
        //不在网络中传输密码
//        headerMap.put("secreteKey", secreteKey);
        headerMap.put("body", body);
        headerMap.put("nounce", RandomUtil.randomNumbers(4));
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        headerMap.put("sign", SignUtils.getSign(headerMap, secreteKey));
        return headerMap;
    }


}
