package com.sn.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author songning
 * @date 2019/9/27
 * description
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObject2String(Object object) {
        String string = null;
        try {
            string = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static <T> T convertString2Object(String s, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(s, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
}
