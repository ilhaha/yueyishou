package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/11/8 09:56
 * @Version 1.0
 *
 * 查看订单拒收信息
 */
@Data
public class RejectOrderVo {

    /**
     * 拒收上传的图片
     */
    private String rejectActualPhotos;

    /**
     * 拒收理由
     */
    private String cancelMessage;
}
