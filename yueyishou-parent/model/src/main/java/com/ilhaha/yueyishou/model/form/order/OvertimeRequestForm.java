package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

/**
 * 计算回收员超时费用的表单类
 */
@Data
public class OvertimeRequestForm {

    /**超时分钟数*/
    private Integer overtimeMinutes;

}