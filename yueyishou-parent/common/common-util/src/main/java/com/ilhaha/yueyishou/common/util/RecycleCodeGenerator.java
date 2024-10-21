package com.ilhaha.yueyishou.common.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RecycleCodeGenerator {

    private static final Set<Integer> generatedNumbers = new HashSet<>();
    private static final Random random = new Random();

    public static String generateUniqueSixDigitNumber() {
        int randomNumber;

        // 生成6位数并确保不重复
        do {
            randomNumber = 100000 + random.nextInt(900000); // 生成100000到999999之间的随机数
        } while (generatedNumbers.contains(randomNumber));

        generatedNumbers.add(randomNumber); // 添加到已生成的集合中

        // 返回六位数字的字符串
        return String.valueOf(randomNumber);
    }
}