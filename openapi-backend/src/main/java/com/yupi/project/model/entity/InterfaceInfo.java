package com.yupi.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 接口信息
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}