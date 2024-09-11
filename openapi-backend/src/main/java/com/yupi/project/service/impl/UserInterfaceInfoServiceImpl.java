package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.openapicommon.model.entity.InterfaceInfo;
import com.yupi.openapicommon.model.entity.User;
import com.yupi.openapicommon.model.entity.UserInterfaceInfo;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.UserInterfaceInfoMapper;

import com.yupi.project.service.InterfaceInfoService;
import com.yupi.project.service.UserInterfaceInfoService;

import com.yupi.project.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author yess
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service实现
* @createDate 2024-07-29 15:42:49
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Long userId = userInterfaceInfo.getUserId();
        Integer usedNum = userInterfaceInfo.getUsedNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();

        // 添加操作时，所有参数必须非空
        if (add) {
            if (interfaceInfoId < 0 || userId < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id或者接口id有误");
            }
            if(usedNum < 0 || leftNum < 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口次数有误");
            }
        }
        //校验用户是否存在
        User user = userService.getById(userInterfaceInfo.getUserId());
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前用户不存在");
        }
        //校验接口是否存在
        InterfaceInfo curInterface = interfaceInfoService.getById(userInterfaceInfo.getInterfaceInfoId());
        if(curInterface == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前接口不存在");
        }
        boolean result = this.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

    }

    @Override
    public boolean invokeCount(Long interfaceInfoId, Long userId) {
        if (interfaceInfoId < 0 || userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id或者接口id有误");
        }
        //构造更新语句
        // todo 添加事务，方便回滚；添加锁，用于并发
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId);
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.setSql("leftNum = leftNum - 1, usedNum = usedNum + 1");
        return this.update(updateWrapper);
    }

}




