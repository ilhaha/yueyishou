package com.ilhaha.yueyishou.recycler.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ilhaha
 * @Create 2024/9/2 20:12
 * @Version 1.0
 */
@FeignClient("service-recycler")
public interface RecyclerPersonalizationFeignClient {

    /**
     * 修改回收员接单设置
     * @param orderSettingForm
     * @return
     */
    @PostMapping("/recyclerPersonalization/update/order/setting")
    Result<Boolean> updateOrderSetting(@RequestBody OrderSettingForm orderSettingForm);

    /**
     * 获取回收员的接单设置
     * @param recyclerId
     * @return
     */
    @GetMapping("/recyclerPersonalization/order/setting/{recyclerId}")
    Result<OrderSettingVo> orderSetting(@PathVariable("recyclerId") Long recyclerId);

    /**
     * 修改回收员服务状态
     * @param recyclerId
     * @param serviceStatus
     * @return
     */
    @PostMapping("/recyclerPersonalization/switch/service/{recyclerId}/{serviceStatus}")
    Result<Boolean> takeOrders(@PathVariable("recyclerId") Long recyclerId,
                                      @PathVariable("serviceStatus") Integer serviceStatus);

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @param recyclerId
     * @return
     */
    @GetMapping("/recyclerPersonalization/info/by/{recyclerId}")
    Result<RecyclerPersonalization> getPersonalizationByRecyclerId(@PathVariable("recyclerId") Long recyclerId);


    /**
     *   添加回收员个性化设置
     *
     * @param recyclerPersonalization
     * @return
     */
    @PostMapping(value = "/recyclerPersonalization/add")
    Result<String> add(@RequestBody RecyclerPersonalization recyclerPersonalization);
}
