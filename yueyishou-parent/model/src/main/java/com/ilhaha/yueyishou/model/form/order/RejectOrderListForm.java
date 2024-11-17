package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/11/17 11:10
 * @Version 1.0
 *
 * 获取申请拒收订单表单类
 */
@Data
public class RejectOrderListForm {

    /**
     * 订单联系人姓名
     */
    private String orderContactPerson;

    /**
     * 回收员姓名
     */
    private String recyclerName;
}
