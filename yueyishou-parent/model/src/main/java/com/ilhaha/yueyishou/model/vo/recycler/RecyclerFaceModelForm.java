package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/10 16:32
 * @Version 1.0
 *
 * 回收员每日人脸校验表单类
 */
@Data
public class RecyclerFaceModelForm {

    /**顾客ID*/
    private Long customerId;

    /**回收员ID*/
    private Long recyclerId;

    /**人脸图片 base64 数据*/
    private String imageBase64 ;
}
