package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;
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
}
