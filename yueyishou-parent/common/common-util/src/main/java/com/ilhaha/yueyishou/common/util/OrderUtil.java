package com.ilhaha.yueyishou.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderUtil {

    public static String generateOrderNumber() {
        // 获取当前时间
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 生成一个随机数
        int randomNum = new Random().nextInt(1000); // 生成0到999的随机数
        // 格式化为三位数
        String formattedRandomNum = String.format("%03d", randomNum);
        // 组合生成订单号
        return timestamp + formattedRandomNum;
    }
}
