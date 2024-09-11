package com.yupi.openapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.openapicommon.model.entity.InterfaceInfo;

/**
* @author yess
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-01 22:30:48
*/
public interface InnerInterfaceInfoService {

    /**
     * 根据请求的接口信息，查询接口是否存在
     * @param ip
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String ip, String path, String method);
}
