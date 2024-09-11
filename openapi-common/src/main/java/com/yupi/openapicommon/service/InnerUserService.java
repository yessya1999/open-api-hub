package com.yupi.openapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.openapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {

    /**
     * 根据accessKey查数据库是否存在该用户，secreteKey是否存在
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
