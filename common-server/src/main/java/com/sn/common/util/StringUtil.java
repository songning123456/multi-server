package com.sn.common.util;

import java.util.Random;

/**
 * @Author songning
 * @create 2019/7/31 14:24
 */
public class StringUtil {

    /**
     * 基础字符串拼接
     *
     * @param strings
     * @return
     * @throws Exception
     */
    public static String getString(String... strings) {
        StringBuilder b = new StringBuilder();
        for (String string : strings) {
            b.append(string);
        }
        return b.toString();
    }

    /**
     * 按指定字符分割字符串; 并返回第n个
     *
     * @param string
     * @return
     */
    public static String splitString(String string, String character, Integer n) {
        String[] strings = string.split(character);
        if (n < strings.length) {
            return strings[n];
        } else {
            return strings[strings.length - 1];
        }
    }

    /**
     * 获取指定位数的随机数字字符串
     *
     * @param bit
     * @return
     */
    public static String getRandomNumString(int bit) {
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bit; i++) {
            s.append(random.nextInt(9));
        }
        return s.toString();
    }
}
