package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import org.jeecg.modules.mgr.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/11 14:32
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderInfoFeignClient orderInfoFeignClient;

    /**
     * 订单分页查询
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Integer pageNo, Integer pageSize) {
        return orderInfoFeignClient.queryPageList(orderMgrQueryForm,pageNo,pageSize).getData();
    }

    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderInfo queryById(String id) {
        return orderInfoFeignClient.queryById(id).getData();
    }
}
