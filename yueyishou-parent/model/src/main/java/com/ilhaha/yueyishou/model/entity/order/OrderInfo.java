package com.ilhaha.yueyishou.model.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class OrderInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**客户ID*/
    private Long customerId;
	/**订单号*/
    private String orderNo;
	/**客户地点*/
    private String customerLocation;
	/**客户地点经度*/
    private BigDecimal customerPointLongitude;
	/**客户地点伟度*/
    private BigDecimal customerPointLatitude;
	/**订单回收分类父ID*/
    private Long parentCategoryId;
    /**订单回收分类父名称*/
    private String parentCategoryName;
    /**订单回收分类子ID*/
    private Long sonCategoryId;
    /**订单回收分类子名称*/
    private String sonCategoryName;
    /**单价*/
    private BigDecimal unitPrice;
    /**预约时间*/
    private Date appointmentTime;
    /**实物照片*/
    private String actualPhotos;
    /**回收重量*/
    private BigDecimal recycleWeigh;
	/**回收员ID*/
    private Long recyclerId;
	/**回收员接单时间*/
    private Date acceptTime;
	/**回收员到达时间*/
    private Date arriveTime;
	/**订单状态：1等待接单，2回收员已接单，3待顾客确认，4回收员未付款，5回收员已付款待评价，6已完成，7订单已取消*/
    private Integer status;
	/**取消订单信息*/
    private String cancelMessage;
	/**订单备注信息*/
    private String remark;
    /**订单联系人*/
    private String orderContactPerson;
    /**订单联系电话*/
    private String orderContactPhone;
	/**订单预计回收总金额*/
    private BigDecimal estimatedTotalAmount;
	/**顾客预支付平台订单金额*/
    private BigDecimal expectCustomerPlatformAmount;
    /**回收员预支付平台订单金额*/
    private BigDecimal expectRecyclerPlatformAmount;
}
