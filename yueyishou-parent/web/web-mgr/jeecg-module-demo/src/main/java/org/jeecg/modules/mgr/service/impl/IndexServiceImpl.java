package org.jeecg.modules.mgr.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.order.CollectVo;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import org.jeecg.modules.mgr.service.IndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/10/30 15:39
 * @Version 1.0
 */
@Service
public class IndexServiceImpl implements IndexService {


    @Resource
    private OrderInfoFeignClient orderInfoFeignClient;

    @Override
    public Result<CollectVo> collect() {
        return orderInfoFeignClient.collect();
    }
}
