package com.yess.openapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.Map;

/**
 * @author yess
 */
public class SignUtils {

    public static String getSign(Map<String, String> hashMap, String secreteKey){
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        String content = hashMap.toString() + "." + secreteKey;
        return sha256.digestHex(content);
    }
}
