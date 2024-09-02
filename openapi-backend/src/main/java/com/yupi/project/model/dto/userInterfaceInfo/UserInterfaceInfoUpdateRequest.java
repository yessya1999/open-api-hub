package com.yupi.project.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @author yess
 * @TableName product
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 已调用次数
     */
    private Integer usedNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0：正常，1：禁用
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}