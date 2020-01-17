package com.sn.common.util;

/**
 * @author songning
 * @date 2019/10/23
 * description
 */
public class CssStyleUtil {

    public static String boldAndItalicFont(String text) {
        String cssStart = "<span style='font-style:italic;font-weight:bold'>";
        String cssEnd = "</span>";
        return cssStart + text + cssEnd;
    }

    public static String spans(String... texts) {
        StringBuilder stringBuilder = new StringBuilder();
        String cssStart = "<span>";
        String cssEnd = "</span>";
        for (String text : texts) {
            stringBuilder.append(cssStart).append(text).append(cssEnd);
        }
        return stringBuilder.toString();
    }
}
