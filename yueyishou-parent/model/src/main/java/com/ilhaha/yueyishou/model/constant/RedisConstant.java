package com.ilhaha.yueyishou.model.constant;

public class RedisConstant {

    //用户登录
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";

    // 回收员ID
    public static final String RECYCLER_INFO_KEY_PREFIX = "recycler:info:";

    //用户登录过期时间 30分钟
    public static final int USER_LOGIN_KEY_TIMEOUT = 60 * 30;

    //回收员GEO地址
    public static final String RECYCLER_GEO_LOCATION = "recycler:geo:location";

    //废品回收品类
    public static final String CATEGORY_TREE = "category:tree";

    //待接单的订单
    public static final String WAITING_ORDER = "waiting:order:";

    //查询所有待接单的订单key
    public static final String SELECT_WAITING_ORDER = "waiting:order:*";

    // 回收码
    public static final String RECYCLE_CODE = "recycle:code:";
}
