package com.yupi.project.model.dto.interfaceInfo;

import com.yupi.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 接口的名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 请求接口所在的ip地址
     */
    private String ip;

    /**
     * 请求接口方法所在的请求路径
     */
    private String path;

    /**
     * 请求接口的请求头
     */
    private String requestHeader;

    /**
     * 请求接口的响应头
     */
    private String responseHeader;

    /**
     * 请求方法类型
     */
    private String method;

    /**
     * 0：关闭，1：开启
     */
    private Integer status;

    /**
     * 接口创建人
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}