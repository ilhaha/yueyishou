package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.map.CalculateLineForm;
import com.ilhaha.yueyishou.model.vo.map.DrivingLineVo;

/**
 * @Author ilhaha
 * @Create 2024/10/13 21:46
 * @Version 1.0
 */
public interface MapService {

    /**
     * 计算回收员到回收点的线路
     * @param calculateLineForm
     * @return
     */
    Result<DrivingLineVo> calculateDrivingLine(CalculateLineForm calculateLineForm);
}
