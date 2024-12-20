package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;
import com.ilhaha.yueyishou.recycler.service.PersonalizationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/9/26 15:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/personalization")
public class PersonalizationController {

    @Resource
    private PersonalizationService personalizationService;

    /**
     * 修改回收员接单设置
     * @param orderSettingForm
     * @return
     */
    @LoginVerification
    @PostMapping("/update/order/setting")
    public Result<Boolean> updateOrderSetting(@RequestBody OrderSettingForm orderSettingForm) {
        return personalizationService.updateOrderSetting(orderSettingForm);
    }


    /**
     * 获取回收员的接单设置
     * @return
     */
    @LoginVerification
    @GetMapping("/order/setting")
    public Result<OrderSettingVo> orderSetting() {
        return personalizationService.orderSetting();
    }

    /**
     * 修改回收员服务状态
     * @param serviceStatus
     * @return
     */
    @LoginVerification
    @PostMapping("/switch/service/{serviceStatus}")
    public Result<Boolean> takeOrders(@PathVariable("serviceStatus") Integer serviceStatus){
        return personalizationService.takeOrders(serviceStatus);
    }

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @param
     * @return
     */
    @GetMapping("/info/by/{recyclerId}")
    public Result<RecyclerPersonalization> getPersonalizationByRecyclerId(){
        return personalizationService.getPersonalizationByRecyclerId();
    }

}
