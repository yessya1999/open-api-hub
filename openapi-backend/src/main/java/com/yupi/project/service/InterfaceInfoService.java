package com.yupi.project.service;

import com.yupi.project.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author yess
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-01 22:30:48
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
