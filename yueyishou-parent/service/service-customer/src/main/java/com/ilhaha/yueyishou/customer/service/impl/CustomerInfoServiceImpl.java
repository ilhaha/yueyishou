package com.ilhaha.yueyishou.customer.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.constant.CustomerConstant;
import com.ilhaha.yueyishou.common.constant.RedisConstant;
import com.ilhaha.yueyishou.model.constant.StartDisabledConstant;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.customer.mapper.CustomerInfoMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerInfoService;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.common.util.PhoneNumberUtils;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
    private CosFeignClient cosFeignClient;

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
        pageList.setRecords(pageList.getRecords().stream().map(item -> {
            item.setAvatarUrl(ObjectUtils.isEmpty(item.getAvatarUrl()) ? item.getAvatarUrl() : cosFeignClient.getImageUrl(item.getAvatarUrl()).getData());
            return item;
        }).collect(Collectors.toList()));
        return pageList;
    }

    /**
     * 获取顾客登录之后的顾客信息
     * @param customerId
     * @return
     */
    @Override
    public CustomerLoginInfoVo getLoginInfo(Long customerId) {
        CustomerInfo customerInfoDB = this.getById(customerId);
        if (ObjectUtils.isEmpty(customerInfoDB)) throw new YueYiShouException(ResultCodeEnum.CUSTOMER_INFO_NOT_EXIST);
        CustomerLoginInfoVo customerLoginInfoVo = new CustomerLoginInfoVo();
        BeanUtils.copyProperties(customerInfoDB,customerLoginInfoVo);
        customerLoginInfoVo.setAvatarUrl(cosFeignClient.getImageUrl(customerLoginInfoVo.getAvatarUrl()).getData());
        return customerLoginInfoVo;
    }
}
