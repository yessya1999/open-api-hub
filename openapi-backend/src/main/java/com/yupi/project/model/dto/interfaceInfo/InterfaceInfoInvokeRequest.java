package com.yupi.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author yess
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private Long id;

    /**
     * 请求接口的请求参数
     */
    private String requestParams;

    private static final long serialVersionUID = 1L;
}