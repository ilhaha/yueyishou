package com.ilhaha.yueyishou.customer.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.category.client.CategoryInfoFeignClient;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.RecyclerIDGenerator;
import com.ilhaha.yueyishou.model.constant.CustomerConstant;
import com.ilhaha.yueyishou.common.constant.RedisConstant;
import com.ilhaha.yueyishou.model.constant.PersonalizationConstant;
import com.ilhaha.yueyishou.model.constant.StartDisabledConstant;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountService;
import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.customer.mapper.CustomerInfoMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerInfoService;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.enums.RecyclerAuthStatus;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerBaseInfoForm;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import com.ilhaha.yueyishou.recycler.client.RecyclerPersonalizationFeignClient;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.common.util.PhoneNumberUtils;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import com.ilhaha.yueyishou.tencentcloud.client.FaceModelFeignClient;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Resource
    private WxMaService wxMaService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ICustomerAccountService customerAccountService;

    @Resource
    private RecyclerInfoFeignClient recyclerInfoFeignClient;

    @Resource
    private RecyclerPersonalizationFeignClient recyclerPersonalizationFeignClient;

    @Resource
    private CategoryInfoFeignClient categoryInfoFeignClient;


    /**
     * 小程序授权登录
     *
     * @param code
     * @return
     */
    @Transactional
    @Override
    public String login(String code) {
        String openid = null;
        try {
            // 根据code请求微信接口后台，获取openId
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            openid = sessionInfo.getOpenid();
        } catch (WxErrorException e) {
            throw new YueYiShouException(ResultCodeEnum.INCORRECT_LOGIN_INFORMATION);
        }
        // 判断乘客是否注册过
        LambdaQueryWrapper<CustomerInfo> customerInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerInfoLambdaQueryWrapper.eq(CustomerInfo::getWxOpenId, openid);
        CustomerInfo customerInfo = this.getOne(customerInfoLambdaQueryWrapper);
        // 没注册过，记录乘客信息
        if (ObjectUtils.isEmpty(customerInfo)) {
            customerInfo = new CustomerInfo();
            customerInfo.setNickname(CustomerConstant.DEFAULT_NICKNAME);
            customerInfo.setAvatarUrl(CustomerConstant.DEFAULT_AVATAR);
            customerInfo.setWxOpenId(openid);
            customerInfo.setPhone(PhoneNumberUtils.generateRandomPhoneNumber());
            this.save(customerInfo);
            //  给新顾客创建账户
            CustomerAccount customerAccount = new CustomerAccount();
            customerAccount.setCustomerId(customerInfo.getId());
            customerAccountService.save(customerAccount);
        } else {
            // 判断顾客是否是禁用状态
            if (StartDisabledConstant.DISABLED_STATUS.equals(customerInfo.getStatus())) {
                throw new YueYiShouException(ResultCodeEnum.ACCOUNT_STOP);
            }
        }
        // 根据顾客id生成token
        Long customerInfoId = customerInfo.getId();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 将顾客token存到redis，校验登录时使用
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX + token,
                customerInfoId.toString(),
                RedisConstant.USER_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);

        // 判断顾客是否是回收员
        RecyclerInfo recyclerDB = recyclerInfoFeignClient.getAuth(customerInfoId).getData();
        if (!ObjectUtils.isEmpty(recyclerDB)) {
            redisTemplate.opsForValue().set(RedisConstant.RECYCLER_INFO_KEY_PREFIX + token,
                    recyclerDB.getId().toString(),
                    RedisConstant.USER_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);
        }
        return token;
    }

    /**
     * 修改客户状态
     *
     * @param updateCustomerStatusForm
     * @return
     */
    @Override
    public String switchStatus(UpdateCustomerStatusForm updateCustomerStatusForm) {
        LambdaUpdateWrapper<CustomerInfo> customerInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerInfoLambdaUpdateWrapper.set(CustomerInfo::getStatus, updateCustomerStatusForm.getStatus())
                .in(CustomerInfo::getId, updateCustomerStatusForm.getCustomerIds());
        this.update(customerInfoLambdaUpdateWrapper);
        return "修改成功";
    }

    /**
     * 分页查询
     *
     * @param page
     * @param customerInfo
     * @return
     */
    @Override
    public Page<CustomerInfo> queryPageList(Page<CustomerInfo> page, CustomerInfo customerInfo) {
        LambdaQueryWrapper<CustomerInfo> customerInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerInfoLambdaQueryWrapper.like(StringUtils.hasText(customerInfo.getNickname()),
                CustomerInfo::getNickname, customerInfo.getNickname());
        Page<CustomerInfo> pageList = this.page(page, customerInfoLambdaQueryWrapper);
        return pageList;
    }

    /**
     * 获取顾客登录之后的顾客信息
     *
     * @param customerId
     * @return
     */
    @Override
    public CustomerLoginInfoVo getLoginInfo(Long customerId) {
        CustomerInfo customerInfoDB = this.getById(customerId);
        if (ObjectUtils.isEmpty(customerInfoDB)) throw new YueYiShouException(ResultCodeEnum.CUSTOMER_INFO_NOT_EXIST);
        CustomerLoginInfoVo customerLoginInfoVo = new CustomerLoginInfoVo();
        BeanUtils.copyProperties(customerInfoDB, customerLoginInfoVo);
        return customerLoginInfoVo;
    }


    /**
     * 认证成为回收员
     *
     * @param recyclerApplyForm
     * @return
     */
    @Override
    public Boolean authRecycler(RecyclerApplyForm recyclerApplyForm) {
        // 先查询是否存在回收员的认证信息，如果有则修改，没有则添加
        RecyclerInfo recyclerInfoDB = recyclerInfoFeignClient.getAuth(recyclerApplyForm.getCustomerId()).getData();
        if (ObjectUtils.isEmpty(recyclerInfoDB)) {
            // 添加查询顾客的基本信息
            CustomerInfo customerInfoDB = this.getById(recyclerApplyForm.getCustomerId());
            if (ObjectUtils.isEmpty(customerInfoDB)) {
                throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
            }
            // 创建新的 RecyclerInfo 对象并设置属性
            RecyclerInfo recyclerInfo = new RecyclerInfo();
            BeanUtils.copyProperties(recyclerApplyForm, recyclerInfo);
            recyclerInfo.setAvatarUrl(customerInfoDB.getAvatarUrl());
            recyclerInfo.setPhone(customerInfoDB.getPhone());
            recyclerInfo.setJobNo(RecyclerIDGenerator.generateEmployeeID());
            recyclerInfo.setAuthStatus(RecyclerAuthStatus.UNDER_REVIEW.getStatus());
            // 添加新的回收员认证信息
            RecyclerInfo newRecycler = recyclerInfoFeignClient.add(recyclerInfo).getData();

            // 获取所有的废品品类
            List<CategoryInfo> categoryInfoList = categoryInfoFeignClient.parentList().getData();

            // 使用流提取 ID 并拼接成逗号分隔的字符串
            String categoryType = categoryInfoList.stream()
                    .map(CategoryInfo::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            // 添加基本的回收员设置
            RecyclerPersonalization recyclerPersonalization = new RecyclerPersonalization();
            recyclerPersonalization.setRecyclerId(newRecycler.getId());
            recyclerPersonalization.setServiceStatus(PersonalizationConstant.STOP_TAKING_ORDERS);
            recyclerPersonalization.setAcceptDistance(PersonalizationConstant.DEFAULT_DISTANCE);
            recyclerPersonalization.setRecyclingType(categoryType);
            recyclerPersonalizationFeignClient.add(recyclerPersonalization);
        } else {
            // 更新现有的回收员认证信息
            BeanUtils.copyProperties(recyclerApplyForm, recyclerInfoDB);
            recyclerInfoDB.setAuthStatus(RecyclerAuthStatus.UNDER_REVIEW.getStatus());
            recyclerInfoFeignClient.edit(recyclerInfoDB);
        }
        return true;
    }

    /**
     * 更新顾客基本信息
     * @param updateCustomerBaseInfoForm
     * @return
     */
    @Override
    public Boolean updateBaseInfo(UpdateCustomerBaseInfoForm updateCustomerBaseInfoForm) {
        LambdaUpdateWrapper<CustomerInfo> customerInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerInfoLambdaUpdateWrapper.eq(CustomerInfo::getId,updateCustomerBaseInfoForm.getId())
                .set(CustomerInfo::getNickname,updateCustomerBaseInfoForm.getNickname())
                .set(CustomerInfo::getAvatarUrl,updateCustomerBaseInfoForm.getAvatarUrl());
        return this.update(customerInfoLambdaUpdateWrapper);
    }

}
