package com.ilhaha.yueyishou.common.util;

/**
 * 获取当前用户信息帮助类
 */
public class AuthContextHolder {

    private static ThreadLocal<Long> customerId = new ThreadLocal<Long>();

    public static void setCustomerId(Long _userId) {
        customerId.set(_userId);
    }

    public static Long getCustomerId() {
        return customerId.get();
    }

    public static void removeUserId() {
        customerId.remove();
    }

}
