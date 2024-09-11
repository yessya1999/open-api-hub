package com.yupi.openapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.openapicommon.model.entity.UserInterfaceInfo;

/**
* @author yess
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2024-07-29 15:42:49
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 统计接口调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(Long interfaceInfoId, Long userId);
}
