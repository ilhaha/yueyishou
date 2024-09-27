package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.recycler.service.PersonalizationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 根据回收员ID获取回收员的个性化设置
     * @param
     * @return
     */
    @GetMapping("/info/by/{recyclerId}")
    public Result<RecyclerPersonalization> getPersonalizationByRecyclerId(){
        return personalizationService.getPersonalizationByRecyclerId();
    }

}
