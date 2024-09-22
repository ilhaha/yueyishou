package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.constant.StartDisabledConstant;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.enums.RecyclerAuthStatus;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerInfoMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerInfoService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.model.vo.cos.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RecyclerInfoServiceImpl extends ServiceImpl<RecyclerInfoMapper, RecyclerInfo> implements IRecyclerInfoService {

    @Resource
    private CosFeignClient cosFeignClient;

    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    @Override
    public String switchStatus(UpdateRecyclerStatusForm updateRecyclerStatusForm) {
        LambdaUpdateWrapper<RecyclerInfo> recyclerInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerInfoLambdaUpdateWrapper.set(RecyclerInfo::getStatus,updateRecyclerStatusForm.getStatus())
                .in(RecyclerInfo::getId,updateRecyclerStatusForm.getRecyclerIds());
        this.update(recyclerInfoLambdaUpdateWrapper);
        return "修改成功";
    }

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    @Override
    public String auth(RecyclerAuthForm recyclerAuthForm) {
        LambdaUpdateWrapper<RecyclerInfo> recyclerInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerInfoLambdaUpdateWrapper.set(RecyclerInfo::getAuthStatus,recyclerAuthForm.getAuthStatus())
                .set(RecyclerAuthStatus.CERTIFICATION_PASSED.getStatus().equals(recyclerAuthForm.getAuthStatus()),RecyclerInfo::getStatus, StartDisabledConstant.START_STATUS)
                .set(RecyclerAuthStatus.UNDER_REVIEW.getStatus().equals(recyclerAuthForm.getAuthStatus()),RecyclerInfo::getStatus,StartDisabledConstant.DISABLED_STATUS)
                .in(RecyclerInfo::getId,recyclerAuthForm.getRecyclerIds());
        this.update(recyclerInfoLambdaUpdateWrapper);
        return "已审核";
    }

    /**
     * 回收员分页查询
     * @param recyclerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<RecyclerInfo> queryPageList(RecyclerInfo recyclerInfo, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<RecyclerInfo> recyclerInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerInfoLambdaQueryWrapper
                .like(StringUtils.hasText(recyclerInfo.getName()), RecyclerInfo::getName, recyclerInfo.getName())
                .eq(!ObjectUtils.isEmpty(recyclerInfo.getAuthStatus()), RecyclerInfo::getAuthStatus, recyclerInfo.getAuthStatus());

        Page<RecyclerInfo> page = new Page<>(pageNo, pageSize);
        Page<RecyclerInfo> pageList = this.page(page, recyclerInfoLambdaQueryWrapper);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (RecyclerInfo item : pageList.getRecords()) {
            // 异步获取 Avatar URL
            CompletableFuture<Void> avatarFuture = CompletableFuture.supplyAsync(() ->
                    ObjectUtils.isEmpty(item.getAvatarUrl()) ? item.getAvatarUrl() : cosFeignClient.getImageUrl(item.getAvatarUrl()).getData()
            ).thenAccept(item::setAvatarUrl);
            futures.add(avatarFuture);

            // 异步获取 idcardFrontUrl
            CompletableFuture<Void> idcardFrontFuture = CompletableFuture.supplyAsync(() ->
                    cosFeignClient.getImageUrl(item.getIdcardFrontUrl()).getData()
            ).thenAccept(item::setIdcardFrontUrl);
            futures.add(idcardFrontFuture);

            // 异步获取 idcardBackUrl
            CompletableFuture<Void> idcardBackFuture = CompletableFuture.supplyAsync(() ->
                    cosFeignClient.getImageUrl(item.getIdcardBackUrl()).getData()
            ).thenAccept(item::setIdcardBackUrl);
            futures.add(idcardBackFuture);

            // 异步获取 idcardHandUrl
            CompletableFuture<Void> idcardHandFuture = CompletableFuture.supplyAsync(() ->
                    cosFeignClient.getImageUrl(item.getIdcardHandUrl()).getData()
            ).thenAccept(item::setIdcardHandUrl);
            futures.add(idcardHandFuture);
        }

        // 等待所有异步任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return pageList;
    }

    /**
     * 回收员上传图片
     * @param file
     * @return
     */
    @Override
    public Result<CosUploadVo> upload(MultipartFile file, String path) {
        return cosFeignClient.upload(file,path);
    }

}
