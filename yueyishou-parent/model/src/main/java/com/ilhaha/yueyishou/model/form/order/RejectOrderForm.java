package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/11/7 18:20
 * @Version 1.0
 *
 * 拒收订单表单类
 */
@Data
public class RejectOrderForm {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 拒收上传的图片
     */
    private String rejectActualPhotos;

    /**
     * 拒收理由
     */
    private String cancelMessage;

    /**
     * 回收员接单时间
     */
    private Date acceptTime;
    /**
     * 回收员到达时间
     */
    private Date arriveTime;
}
