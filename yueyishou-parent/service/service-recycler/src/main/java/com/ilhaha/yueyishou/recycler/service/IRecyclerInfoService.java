package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRateForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerBaseInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.UpdateRecyclerLocationForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

public interface IRecyclerInfoService extends IService<RecyclerInfo> {

    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    String switchStatus(UpdateRecyclerStatusForm updateRecyclerStatusForm);

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    String auth(RecyclerAuthForm recyclerAuthForm);

    /**
     * 回收员分页查询
     * @param recyclerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<RecyclerInfo> queryPageList(RecyclerInfo recyclerInfo, Integer pageNo, Integer pageSize);

    /**
     * 回收员上传图片
     * @param file
     * @return
     */
     Result<CosUploadVo> upload(MultipartFile file, String path);

    /**
     * 根据顾客Id查询回收员认证信息
     * @param customerId
     * @return
     */
    RecyclerInfo getAuth(Long customerId);

    /**
     * 根据顾客Id获取回收员认证图片信息
     * @param customerId
     * @return
     */
    RecyclerAuthImagesVo getAuthImages(Long customerId);

    /**
     * 获取回收员基本信息
     * @param recyclerId
     * @return
     */
    RecyclerBaseInfoVo getBaseInfo(Long recyclerId);

    /**
     * 以防如果用户还未退出登录就已经认证成功成为回收员出现信息不全问题
     * 也就是redis中无该回收员Id
     *
     * @param customerId
     * @param token
     * @return
     */
    Boolean replenishInfo(Long customerId, String token);

    /**
     * 增加回收员单量
     * @param recyclerId
     */
    void updateOrderCount(Long recyclerId);

    /**
     * 修改回收员评分
     * @param updateRateForm
     * @return
     */
    Boolean updateRate(UpdateRateForm updateRateForm);
}
