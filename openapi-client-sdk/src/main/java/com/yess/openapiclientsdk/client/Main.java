package com.yess.openapiclientsdk.client;


import com.yess.openapiclientsdk.model.User;

public class Main {

    public static void main(String[] args) {
        OpenApiClient openApiClient = new OpenApiClient("yess", "1234abc");
        User user = new User();
        user.setUserName("user");
        System.out.println(openApiClient.getNameByGet("yess"));
        System.out.println(openApiClient.getNameByPost("yess"));
        System.out.println(openApiClient.getUserNameByPost(user));
    }
}
