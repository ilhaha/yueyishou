package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.recycler.client.RecyclerPersonalizationFeignClient;
import com.ilhaha.yueyishou.recycler.service.PersonalizationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/9/26 15:09
 * @Version 1.0
 */
@Service
public class PersonalizationServiceImpl implements PersonalizationService {

    @Resource
    private RecyclerPersonalizationFeignClient recyclerPersonalizationFeignClient;

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @return
     */
    @LoginVerification
    @Override
    public Result<RecyclerPersonalization> getPersonalizationByRecyclerId() {
        return recyclerPersonalizationFeignClient.getPersonalizationByRecyclerId(AuthContextHolder.getRecyclerId());
    }
}
