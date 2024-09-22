package com.ilhaha.yueyishou.model.vo.order;

import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author ilhaha
 * @Create 2024/9/11 17:26
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderMgrQueryVo extends OrderInfo {
    /**
     * 回收员姓名
     */
    private String recyclerName;
    /**
     * 分类名称
     */
    private String categoryName;
}
