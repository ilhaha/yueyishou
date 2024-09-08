package com.ilhaha.yueyishou.form.recycler;

import lombok.Data;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/8 14:13
 * @Version 1.0
 */
@Data
public class RecyclerAuthForm {

    /**
     * 回收员id集合
     */
    private List<Long> recyclerIds;

    /**
     * 审核状态
     */
    private Integer authStatus;
}
