package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/18 08:34
 * @Version 1.0
 *
 * 修改订单表单类
 */
@Data
public class UpdateOrderFrom {


    /**id*/
    private Long id;
    /**订单回收分类父ID*/
    private Long parentCategoryId;
    /**订单回收分类父名称*/
    private String parentCategoryName;
    /**订单回收分类子ID*/
    private Long sonCategoryId;
    /**订单回收分类子名称*/
    private String sonCategoryName;
    /**单价*/
    private BigDecimal unitPrice;
    /**实物照片*/
    private String actualPhotos;
    /**回收重量*/
    private BigDecimal recycleWeigh;
    /**订单预计回收总金额*/
    private BigDecimal estimatedTotalAmount;
    /**顾客预支付平台订单金额*/
    private BigDecimal expectCustomerPlatformAmount;
    /**回收员预支付平台订单金额*/
    private BigDecimal expectRecyclerPlatformAmount;
}
