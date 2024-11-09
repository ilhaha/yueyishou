package com.ilhaha.yueyishou.model.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 客户地点
     */
    private String customerLocation;
    /**
     * 客户地点经度
     */
    private BigDecimal customerPointLongitude;
    /**
     * 客户地点伟度
     */
    private BigDecimal customerPointLatitude;
    /**
     * 订单回收分类父ID
     */
    private Long parentCategoryId;
    /**
     * 订单回收分类父名称
     */
    private String parentCategoryName;
    /**
     * 订单回收分类子ID
     */
    private Long sonCategoryId;
    /**
     * 订单回收分类子名称
     */
    private String sonCategoryName;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 预约时间
     */
    private Date appointmentTime;
    /**
     * 实物照片
     */
    private String actualPhotos;
    /**
     * 回收重量
     */
    private BigDecimal recycleWeigh;
    /**
     * 回收员ID
     */
    private Long recyclerId;
    /**
     * 回收员接单时间
     */
    private Date acceptTime;
    /**
     * 回收员到达时间
     */
    private Date arriveTime;
    /**
     * 订单状态：1等待接单，2回收员已接单，3待回收员确认，4待顾客确认，5回收员未付款，6回收员已付款待评价，7已完成，8订单已取消
     */
    private Integer status;
    /**
     * 取消订单信息
     */
    private String cancelMessage;
    /**
     * 取消时间
     */
    private Date cancelTime;
    /**
     * 订单备注信息
     */
    private String remark;
    /**
     * 订单联系人
     */
    private String orderContactPerson;
    /**
     * 订单联系电话
     */
    private String orderContactPhone;
    /**
     * 订单预计回收总金额
     */
    private BigDecimal estimatedTotalAmount;
    /**
     * 顾客预支付平台订单金额
     */
    private BigDecimal expectCustomerPlatformAmount;
    /**
     * 回收员预支付平台订单金额
     */
    private BigDecimal expectRecyclerPlatformAmount;
    /**
     * 接单后，回收员在预约时间未到达取消订单赔偿
     */
    private BigDecimal serviceOvertimePenalty;
    /**
     * 接单后，顾客未在免费取消订单时间内取消赔偿
     */
    private BigDecimal customerLateCancellationFee;
    /**
     * 接单后，回收员未在免费取消订单时间内取消赔偿
     */
    private BigDecimal recyclerLateCancellationFee;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("create_time")
    private Date createTime = new Date();

    @JsonIgnore
    @TableField("update_time")
    private Date updateTime = new Date();

    /**
     * 顾客是否删除 0-未删除 1-已删除
     */
    private Integer customerIsDeleted;

    /**
     * 回收员是否删除 0-未删除 1-已删除
     */
    private Integer recyclerIsDeleted;

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
}
