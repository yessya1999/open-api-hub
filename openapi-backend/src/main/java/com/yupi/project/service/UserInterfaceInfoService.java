package com.yupi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.openapicommon.model.entity.UserInterfaceInfo;

/**
* @author yess
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2024-07-29 15:42:49
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验传入的用户信息和接口信息
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 统计接口调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(Long interfaceInfoId, Long userId);
}
