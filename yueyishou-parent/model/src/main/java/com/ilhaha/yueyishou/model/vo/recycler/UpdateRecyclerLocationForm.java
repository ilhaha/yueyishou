package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/11 15:07
 * @Version 1.0
 *
 * 更新回收员的地理位置表单
 */
@Data
public class UpdateRecyclerLocationForm {

    /**回收员Id*/
    private Long recyclerId;

    /**经度*/
    private BigDecimal longitude;

    /**纬度*/
    private BigDecimal latitude;

}
