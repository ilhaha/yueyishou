package com.ilhaha.yueyishou.model.form.coupon;

import lombok.Data;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/17 10:09
 * @Version 1.0
 *
 * 免费发放服务抵扣劵表单类
 */
@Data
public class FreeIssueForm {
    /**服务费抵扣券ID*/
    private Long couponId;
    /**顾客ID*/
    private Long customerId;
    /**回收员ID*/
    private Long recyclerId;
    /**领取时间*/
    private Date receiveTime;
}
