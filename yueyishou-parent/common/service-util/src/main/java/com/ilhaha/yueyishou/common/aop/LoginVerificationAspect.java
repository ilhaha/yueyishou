package com.ilhaha.yueyishou.common.aop;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author ilhaha
 * @Create 2024/8/18 10:17
 * @Version 1.0
 */
@Aspect
@Component
public class LoginVerificationAspect {

    @Resource
    private RedisTemplate redisTemplate;

    @Before("execution(* com.ilhaha.yueyishou.*.controller.*.*(..)) && @annotation(loginVerification)")
    public void loginVerification(LoginVerification loginVerification) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader("token");

        if (ObjectUtils.isEmpty(token)) {
            throw new YueYiShouException(ResultCodeEnum.LOGIN_AUTH);
        }

        String customerId = (String) redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_KEY_PREFIX + token);
        if (ObjectUtils.isEmpty(customerId)) {
            throw new YueYiShouException(ResultCodeEnum.LOGIN_AUTH);
        }
        AuthContextHolder.setCustomerId(Long.valueOf(customerId));

        String recyclerId = (String) redisTemplate.opsForValue().get(RedisConstant.RECYCLER_INFO_KEY_PREFIX + token);
        if (!ObjectUtils.isEmpty(recyclerId)) {
            AuthContextHolder.setRecyclerId(Long.valueOf(recyclerId));
        }

    }
}
