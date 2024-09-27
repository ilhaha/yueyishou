package com.ilhaha.yueyishou.common.constant;

public class RedisConstant {

    //用户登录
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";
    // 回收员ID
    public static final String RECYCLER_INFO_KEY_PREFIX = "recycler:info:";
    //用户登录过期时间 30分钟
    public static final int USER_LOGIN_KEY_TIMEOUT = 60 * 30;

    //回收员GEO地址
    public static final String RECYCLER_GEO_LOCATION = "cos:geo:location";

    //废品回收品类
    public static final String CATEGORY_TREE = "category:tree";
}
