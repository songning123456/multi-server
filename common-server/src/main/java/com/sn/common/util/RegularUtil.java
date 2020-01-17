package com.sn.common.util;

import java.util.regex.Pattern;

/**
 * @Author songning
 * @create 2019/8/15 18:56
 */
public class RegularUtil {

    /**
     * 判断是否为整数
     *
     * @param str
     * @return
     */
    public static Boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
