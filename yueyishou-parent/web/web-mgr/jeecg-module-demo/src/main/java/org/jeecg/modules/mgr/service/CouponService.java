package org.jeecg.modules.mgr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.coupon.CouponInfo;

/**
 * @Author ilhaha
 * @Create 2024/9/6 08:22
 * @Version 1.0
 */
public interface CouponService {

    /**
     * 服务抵扣劵分页查询
     *
     * @param couponInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<CouponInfo> queryPageList(CouponInfo couponInfo, Integer pageNo, Integer pageSize);


    /**
     * 添加服务抵扣劵
     * @param couponInfo
     * @return
     */
    String add(CouponInfo couponInfo);

    /**
     * 修改服务抵扣劵信息
     * @param couponInfo
     * @return
     */
    String edit(CouponInfo couponInfo);

    /**
     * 删除服务抵扣劵
     * @param id
     * @return
     */
    String delete(String id);

    /**
     * 批量删除服务抵扣劵
     * @param ids
     * @return
     */
    String deleteBatch(String ids);

    /**
     * 根据id查询服务抵扣劵信息
     * @param id
     * @return
     */
    CouponInfo queryById(String id);
}
