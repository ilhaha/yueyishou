package com.ilhaha.yueyishou.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RecyclerIDGenerator {

    // 定义前缀
    private static final String PREFIX = "RECYCLER";

    // 定义随机数生成器
    private static final Random RANDOM = new Random();

    /**
     * 生成唯一的工号
     * @return 工号
     */
    public static String generateEmployeeID() {
        // 获取当前时间的时间戳
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 生成随机数（3位）
        int randomNum = RANDOM.nextInt(900) + 100; // 100到999的随机数
        // 拼接工号
        return PREFIX + timestamp + randomNum;
    }
}
