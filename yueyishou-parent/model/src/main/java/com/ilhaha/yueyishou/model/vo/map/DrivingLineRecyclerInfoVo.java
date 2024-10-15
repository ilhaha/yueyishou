package com.ilhaha.yueyishou.model.vo.map;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/15 11:03
 * @Version 1.0
 */
@Data
public class DrivingLineRecyclerInfoVo {
    /**回收员ID*/
    private Long recyclerId;
    /**电话*/
    private String phone;
    /**姓名*/
    private String name;
    /**性别 1:男 2：女*/
    private String gender;
    /**正脸照*/
    private String fullFaceUrl;
    /**评分*/
    private BigDecimal score;
    /**订单量统计*/
    private Integer orderCount;


}
