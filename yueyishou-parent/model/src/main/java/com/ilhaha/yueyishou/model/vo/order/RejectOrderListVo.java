package com.ilhaha.yueyishou.model.vo.order;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/11/8 11:36
 * @Version 1.0
 * <p>
 * 申请拒收订单列表Vo
 */
@Data
public class RejectOrderListVo {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单联系人
     */
    private String orderContactPerson;
    /**
     * 顾客Id
     */
    private Long customerId;
    /**
     * 订单联系电话
     */
    private String orderContactPhone;
    /**
     * 客户地点
     */
    private String customerLocation;
    /**
     * 回收重量
     */
    private BigDecimal recycleWeigh;
    /**
     * 实物照片
     */
    private String actualPhotos;
    /**
     * 回收员ID
     */
    private Long recyclerId;
    /**
     * 回收员姓名
     */
    private String recyclerName;
    /**
     * 拒收状态，0-不拒收 1-拒收 2-已拒收 1-拒收未通过
     */
    private Integer rejectStatus;
    /**
     * 拒收上传的图片
     */
    private String rejectActualPhotos;
    /**
     * 回收员拒收得到补偿
     */
    private BigDecimal rejectCompensation;
    /**
     * 废品品类ID
     */
    private Long categoryId;
    /**
     * 废品品类名称
     */
    private String categoryName;

    /**
     * 拒收理由
     */
    private String cancelMessage;

    /**
     * 拒收申请时间
     */
    private Date cancelTime;
}
