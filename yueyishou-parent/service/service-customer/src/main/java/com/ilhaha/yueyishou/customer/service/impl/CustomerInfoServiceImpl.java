package com.ilhaha.yueyishou.customer.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.constant.RedisConstant;
import com.ilhaha.yueyishou.constant.StartDisabledConstant;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountService;
import com.ilhaha.yueyishou.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.customer.mapper.CustomerInfoMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerInfoService;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Resource
    private WxMaService wxMaService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ICustomerAccountService customerAccountService;

    /**
     * 小程序授权登录
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
        customerInfoLambdaQueryWrapper.eq(CustomerInfo::getWxOpenId,openid);
        CustomerInfo customerInfo = this.getOne(customerInfoLambdaQueryWrapper);
        // 没注册过，记录乘客信息
        if (ObjectUtils.isEmpty(customerInfo)) {
            customerInfo = new CustomerInfo();
            customerInfo.setNickname(String.valueOf(System.currentTimeMillis()));
            customerInfo.setAvatarUrl("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
            customerInfo.setWxOpenId(openid);
            customerInfo.setPhone(com.ilhaha.yueyishou.result.util.PhoneNumberUtils.generateRandomPhoneNumber());
            this.save(customerInfo);
            //  给新顾客创建账户
            CustomerAccount customerAccount = new CustomerAccount();
            customerAccount.setCustomerId(customerInfo.getId());
            customerAccountService.save(customerAccount);
        }else {
            // 判断顾客是否是禁用状态
            if (StartDisabledConstant.DISABLED_STATUS.equals(customerInfo.getStatus())) {
                throw new YueYiShouException(ResultCodeEnum.ACCOUNT_STOP);
            }
        }
        // 根据顾客id生成token
        Long customerInfoId = customerInfo.getId();
        String token = UUID.randomUUID().toString().replaceAll("-","");
        // 将顾客token存到redis，校验登录时使用
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX + token,
                customerInfoId.toString(),
                RedisConstant.USER_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);

        return token;
    }

    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    @Override
    public String switchStatus(UpdateCustomerStatusForm updateCustomerStatusForm) {
        LambdaUpdateWrapper<CustomerInfo> customerInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerInfoLambdaUpdateWrapper.set(CustomerInfo::getStatus,updateCustomerStatusForm.getStatus())
                .in(CustomerInfo::getId,updateCustomerStatusForm.getCustomerIds());
        this.update(customerInfoLambdaUpdateWrapper);
        return "修改成功";
    }
}
