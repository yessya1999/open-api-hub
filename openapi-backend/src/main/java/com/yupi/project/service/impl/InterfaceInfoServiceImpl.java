package com.yupi.project.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.entity.InterfaceInfo;
import com.yupi.project.model.enums.PostGenderEnum;
import com.yupi.project.model.enums.PostReviewStatusEnum;
import com.yupi.project.service.InterfaceInfoService;
import com.yupi.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author yess
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-07-01 22:30:48
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String ip = interfaceInfo.getIp();
        String path = interfaceInfo.getPath();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        // 添加操作时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, description, ip, path, requestHeader, responseHeader, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if(StringUtils.isNotBlank(name) && name.length() > 50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if(ObjectUtils.anyNotNull(id) && id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求id错误");
        }

    }
}




