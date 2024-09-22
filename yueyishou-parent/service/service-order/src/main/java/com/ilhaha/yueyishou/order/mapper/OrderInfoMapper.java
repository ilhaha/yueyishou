package com.ilhaha.yueyishou.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import org.apache.ibatis.annotations.Param;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 订单分页查询
     * @param page
     * @param orderMgrQueryForm
     * @return
     */
    Page<OrderMgrQueryVo> queryPageList(@Param("page") Page<OrderMgrQueryVo> page,
                                        @Param("orderMgrQueryForm") OrderMgrQueryForm orderMgrQueryForm);
}
