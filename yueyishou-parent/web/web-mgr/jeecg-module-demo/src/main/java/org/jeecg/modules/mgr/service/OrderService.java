package org.jeecg.modules.mgr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;

/**
 * @Author ilhaha
 * @Create 2024/9/11 14:32
 * @Version 1.0
 */
public interface OrderService {
    /**
     * 订单分页查询
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Integer pageNo, Integer pageSize);

    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    OrderInfo queryById(String id);
}
