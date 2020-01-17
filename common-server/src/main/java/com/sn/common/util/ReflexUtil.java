package com.sn.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songning
 * @date 2019/8/26
 * description 利用反射获取信息
 */
public class ReflexUtil {
    /**
     * 根据属性名获取属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValueByName(Object object, String fieldName) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名，属性值组成的map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> getFieldsMap(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, Object> fieldMap = new HashMap<>(16);
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = getFieldValueByName(object, fieldName);
            fieldMap.put(fieldName, fieldValue);
        }
        return fieldMap;
    }
}
