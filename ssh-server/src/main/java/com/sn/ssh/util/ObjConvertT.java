package com.sn.ssh.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

/**
 * @author sonin
 * @date 2020/10/1 11:49
 */
@Slf4j
public class ObjConvertT {

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object obj) {
        T result = null;
        try {
            Class<?> clazz = obj.getClass();
            Constructor constructor = clazz.getConstructor(String.class);
            result = (T) constructor.newInstance(obj.toString());
        } catch (Exception e) {
            log.error("转换出错: {}", e.getMessage());
        }
        return result;
    }
}
