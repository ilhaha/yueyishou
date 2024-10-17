package com.ilhaha.yueyishou.model.constant;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/17 10:03
 * @Version 1.0
 *
 * 注册免费发放的服务抵扣劵ID
 */
@Data
public class CouponIssueConstant {

    /**
     * 注册免费发放的服务抵扣劵ID集合
     */
    public static final List<Long> COUPON_FREE_ISSUE_ID = Arrays.asList(13L, 14L);
}
