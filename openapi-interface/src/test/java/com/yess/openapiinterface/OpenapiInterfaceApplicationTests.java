package com.yess.openapiinterface;

import com.yess.openapiclientsdk.client.OpenApiClient;
import com.yess.openapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class OpenapiInterfaceApplicationTests {

    @Resource
    private OpenApiClient openApiClient;

    @Test
    public void testOpenApiClient(){
        System.out.println(openApiClient.getNameByGet("yess"));
        System.out.println(openApiClient.getNameByPost("yess"));
        User user = new User();
        user.setUserName("yess");
        System.out.println(openApiClient.getUserNameByPost(user));
    }
}
