package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.coupon.client.CouponInfoFeignClient;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import org.jeecg.modules.mgr.service.CouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/6 08:22
 * @Version 1.0
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponInfoFeignClient couponInfoFeignClient;

    /**
     * 优惠卷分页查询
     *
     * @param couponInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CouponInfo> queryPageList(CouponInfo couponInfo, Integer pageNo, Integer pageSize) {
        // 远程调用
        System.out.println(couponInfo);
        return couponInfoFeignClient.queryPageList(couponInfo, pageNo, pageSize).getData();
    }


    /**
     * 添加服务抵扣劵
     * @param couponInfo
     * @return
     */
    @Override
    public String add(CouponInfo couponInfo) {
        // 远程调用
        return couponInfoFeignClient.add(couponInfo).getData();
    }

    /**
     * 修改服务抵扣劵信息
     * @param couponInfo
     * @return
     */
    @Override
    public String edit(CouponInfo couponInfo) {
        return couponInfoFeignClient.edit(couponInfo).getData();
    }

    /**
     * 删除服务抵扣劵
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
        return couponInfoFeignClient.delete(id).getData();
    }

    /**
     * 批量删除服务抵扣劵
     * @param ids
     * @return
     */
    @Override
    public String deleteBatch(String ids) {
        return couponInfoFeignClient.deleteBatch(ids).getData();
    }

    /**
     * 根据id查询服务抵扣劵信息
     * @param id
     * @return
     */
    @Override
    public CouponInfo queryById(String id) {
        CouponInfo data = couponInfoFeignClient.queryById(id).getData();
        return data;
    }
}
