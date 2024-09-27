package com.ilhaha.yueyishou.model.form.customer;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/9/26 22:22
 * @Version 1.0
 */
@Data
public class UpdateCustomerBaseInfoForm {
    /**
     * 顾客id
     */
    private Long id;
    /**
     * 客户昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatarUrl;
}
