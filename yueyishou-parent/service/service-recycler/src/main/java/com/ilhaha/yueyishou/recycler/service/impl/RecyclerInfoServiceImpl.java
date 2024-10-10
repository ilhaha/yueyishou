package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.common.constant.RedisConstant;
import com.ilhaha.yueyishou.model.constant.StartDisabledConstant;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.enums.RecyclerAuthStatus;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerBaseInfoVo;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerInfoMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerInfoService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.recycler.service.IRecyclerPersonalizationService;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
public class RecyclerInfoServiceImpl extends ServiceImpl<RecyclerInfoMapper, RecyclerInfo> implements IRecyclerInfoService {

    @Resource
    private CosFeignClient cosFeignClient;

    @Resource
    private IRecyclerPersonalizationService recyclerPersonalizationService;

    @Resource
    private RedisTemplate redisTemplate;

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


    /**
     * 根据顾客Id删除回收员认证信息
     * @param customerId
     * @return
     */
    @Override
    public RecyclerInfo getAuth(Long customerId) {
        LambdaQueryWrapper<RecyclerInfo> recyclerInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerInfoLambdaQueryWrapper.eq(RecyclerInfo::getCustomerId,customerId);
        return this.getOne(recyclerInfoLambdaQueryWrapper);
    }

    /**
     * 根据顾客Id获取回收员认证图片信息
     * @param customerId
     * @return
     */
    @Override
    public RecyclerAuthImagesVo getAuthImages(Long customerId) {
        RecyclerInfo recyclerInfoDB = this.getAuth(customerId);
        RecyclerAuthImagesVo recyclerAuthImagesVo = new RecyclerAuthImagesVo();
        if (!ObjectUtils.isEmpty(recyclerInfoDB)) {
            BeanUtils.copyProperties(recyclerInfoDB,recyclerAuthImagesVo);
        }
        return recyclerAuthImagesVo;
    }

    /**
     * 获取回收员基本信息
     * @param recyclerId
     * @return
     */
    @Override
    public RecyclerBaseInfoVo getBaseInfo(Long recyclerId) {
        // 查询回收员信息
        RecyclerBaseInfoVo recyclerBaseInfoVo = new RecyclerBaseInfoVo();
        RecyclerInfo recyclerInfoDB = this.getById(recyclerId);
        BeanUtils.copyProperties(recyclerInfoDB,recyclerBaseInfoVo);
        // 查询回收设置
        RecyclerPersonalization personalization = recyclerPersonalizationService.getPersonalizationByRecyclerId(recyclerInfoDB.getId());
        BeanUtils.copyProperties(personalization,recyclerBaseInfoVo);
        return recyclerBaseInfoVo;
    }

    /**
     * 以防如果用户还未退出登录就已经认证成功成为回收员出现信息不全问题
     * 也就是redis中无该回收员Id
     *
     * @param customerId
     * @param token
     * @return
     */
    @Override
    public Boolean replenishInfo(Long customerId, String token) {
        LambdaQueryWrapper<RecyclerInfo> recyclerInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerInfoLambdaQueryWrapper.eq(RecyclerInfo::getCustomerId,customerId);
        RecyclerInfo recyclerInfoDB = this.getOne(recyclerInfoLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(recyclerInfoDB)) return false;
        String key = RedisConstant.RECYCLER_INFO_KEY_PREFIX + token;
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, recyclerInfoDB.getId().toString(),
                    RedisConstant.USER_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);
        }
        return true;
    }

}
