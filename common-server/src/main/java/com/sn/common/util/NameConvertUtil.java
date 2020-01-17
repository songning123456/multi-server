package com.sn.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author songning
 * @create 2019/7/22 8:27
 */
public class NameConvertUtil {
    /**
     * 驼峰转下划线命名
     *
     * @param camelName
     * @return
     */
    public static String camelToUnderline(String camelName) {
        return camelName.replaceAll("([A-Z]+)", "_$1").toLowerCase();
    }

    /**
     * 下划线转驼峰
     *
     * @param underlineName
     * @return
     */
    public static String underlineToCamel(String underlineName) {
        Matcher matcher = Pattern.compile("(_[a-z]{1})").matcher(underlineName);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String replacement = matcher.group(1);
            matcher.appendReplacement(result, replacement.replace("_", "").toUpperCase());
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
