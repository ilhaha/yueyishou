package com.ilhaha.yueyishou.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.form.order.RejectOrderListForm;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import com.ilhaha.yueyishou.model.vo.order.RejectOrderListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 订单分页查询
     * @param page
     * @param orderMgrQueryForm
     * @return
     */
    Page<OrderMgrQueryVo> queryPageList(@Param("page") Page<OrderMgrQueryVo> page,
                                        @Param("orderMgrQueryForm") OrderMgrQueryForm orderMgrQueryForm,
                                        @Param("rejectOrderStatus") Integer rejectOrderStatus,
                                        @Param("rejectStatusList") List<Integer> rejectStatusList);

    /**
     * 获取申请拒收订单列表
     *
     * @param page
     * @param rejectStatus
     * @param rejectOrderListForm
     * @return
     */
    Page<RejectOrderListVo> getRejectOrderList(@Param("page") Page<RejectOrderListVo> page,
                                               @Param("rejectStatus") Integer rejectStatus,
                                               @Param("rejectOrderListForm") RejectOrderListForm rejectOrderListForm);
}
