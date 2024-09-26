package com.ilhaha.yueyishou.model.form.tencentcloud;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/9/25 13:50
 * @Version 1.0
 */
@Data
public class FaceModelForm {
    /**
     * 当前认证的顾客id
     */
    private Long customerId;
    /**
     * 当前认证的顾客姓名
     */
    private String name;
    /**
     * 当前认证的顾客性别
     */
    private Integer gender;

}
