package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.client.MapFeignClient;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.MapService;
import com.ilhaha.yueyishou.model.form.map.CalculateLineForm;
import com.ilhaha.yueyishou.model.vo.map.DrivingLineVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/13 21:46
 * @Version 1.0
 */
@Service
public class MapServiceImpl implements MapService {

    @Resource
    private MapFeignClient mapFeignClient;

    /**
     * 计算回收员到回收点的线路
     * @param calculateLineForm
     * @return
     */
    @Override
    public Result<DrivingLineVo> calculateDrivingLine(CalculateLineForm calculateLineForm) {
        return mapFeignClient.calculateDrivingLine(calculateLineForm);
    }
}
