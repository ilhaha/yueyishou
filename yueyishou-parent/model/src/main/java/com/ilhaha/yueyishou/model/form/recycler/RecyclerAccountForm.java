package com.ilhaha.yueyishou.model.form.recycler;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/22 14:51
 * @Version 1.0
 *
 * 查询回收员账户信息表单类
 */
@Data
public class RecyclerAccountForm {

    /**回收员Id*/
    private Long recyclerId;

    /**明细日期*/
    private String detailsTime;
}
