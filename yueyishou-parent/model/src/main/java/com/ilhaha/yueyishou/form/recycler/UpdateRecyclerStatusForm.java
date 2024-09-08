package com.ilhaha.yueyishou.form.recycler;

import lombok.Data;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/8 14:11
 * @Version 1.0
 */
@Data
public class UpdateRecyclerStatusForm {

    /**
     * 回收员id集合
     */
    private List<Long> RecyclerIds;

    /**
     * 状态
     */
    private Integer status;
}
