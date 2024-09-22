package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/9/11 17:35
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderMgrQueryForm {

    /**
     * 下单联系人名称
     */
    private String orderContactPerson;

    /**
     * 接单的回收员名称
     */
    private String recyclerName;

    /**
     * 预约结束时间
     */
    private String appointmentEndTime;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer status;

}
