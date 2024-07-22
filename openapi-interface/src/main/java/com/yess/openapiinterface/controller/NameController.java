package com.yess.openapiinterface.controller;

import com.yess.openapiclientsdk.model.User;
import com.yess.openapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 用户名称 api
 *
 * @author yess
 */
@RestController
@RequestMapping("name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String name){
        return "get-你的名字是-" + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "post-你的名字是-" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request){
        String accessKey = request.getHeader("accessKey");
//        String secreteKey = request.getHeader("secreteKey");
        String body = request.getHeader("body");
        String nounce = request.getHeader("nounce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");

        // todo 从数据库读取accessKey信息校验
        if(!"yess".equals(accessKey)){
            throw new RuntimeException("无调用权限");
        }
        // todo 校验随机数，检查数据库中是否重复，存入数据库
        long nounceL = Long.parseLong(nounce);
        if(nounceL < 0 || nounceL >= 10000){
            throw new RuntimeException("无调用权限");
        }

        // todo 时间不超过五分钟

        // todo 从数据库中读取secreteKey做校验
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", accessKey);
        headerMap.put("body", body);
        headerMap.put("nounce", nounce);
        headerMap.put("timestamp", timestamp);
        String serverSign = SignUtils.getSign(headerMap, "1234abcd");
        if(!serverSign.equals(sign)){
            throw new RuntimeException("无调用权限");
        }

        return "post-你的名字是-" + user.getUserName();
    }
}
