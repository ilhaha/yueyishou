package com.ilhaha.yueyishou.map.service.impl;

import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.map.service.LocationService;
import com.ilhaha.yueyishou.model.vo.recycler.UpdateRecyclerLocationForm;
import jakarta.annotation.Resource;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/11 19:48
 * @Version 1.0
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 上传回收员实时位置
     * @param updateRecyclerLocationForm
     * @return
     */
    @Override
    public Boolean updateRecyclerLocation(UpdateRecyclerLocationForm updateRecyclerLocationForm) {
        Point point = new Point(updateRecyclerLocationForm.getLongitude().doubleValue(),
                updateRecyclerLocationForm.getLatitude().doubleValue());
        redisTemplate.opsForGeo().add(RedisConstant.RECYCLER_GEO_LOCATION,
                point,
                updateRecyclerLocationForm.getRecyclerId().toString());
        return true;
    }

    /**
     * 删除回收员位置信息
     * @param recyclerId
     * @return
     */
    @Override
    public Boolean removeRecyclerLocation(Long recyclerId) {
        redisTemplate.opsForGeo().remove(RedisConstant.RECYCLER_GEO_LOCATION, recyclerId.toString());
        return true;
    }
}
