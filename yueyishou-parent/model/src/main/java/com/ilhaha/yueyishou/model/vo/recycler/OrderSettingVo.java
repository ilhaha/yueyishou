package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/10 22:03
 * @Version 1.0
 *
 * 回收员接单设置Vo
 */
@Data
public class OrderSettingVo {

    /**id*/
    private Long id;
    /**回收员ID*/
    private Long recyclerId;
    /**回收类型，多个使用逗号隔开*/
    private String recyclingType;
    /**接单里程设置*/
    private BigDecimal acceptDistance;
}
