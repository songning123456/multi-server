package com.sn.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author songning
 * @create 2019/7/23 15:06
 */
public class MapConvertEntityUtil {

    /**
     * 对象转Map的方法
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public static Map<String, Object> EntityToMap(Object entity) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        Method[] methods = entity.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String field = method.getName();
                field = field.substring(field.indexOf("get") + 1);
                field = field.toLowerCase().charAt(0) + field.substring(1);
                Object value = method.invoke(entity, (Object[]) null);
                map.put(field, value);
            }
        }
        return map;
    }

    /**
     * Map转对象的方法
     *
     * @param clazz
     * @param map
     * @return
     * @throws Exception
     */
    public static Object mapToEntity(Class<?> clazz, Map<String, Object> map) throws Exception {
        // 构建对象
        Object javabean = clazz.newInstance();
        // 获取所有方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                // 截取属性名
                String field = method.getName();
                field = field.substring(field.indexOf("set") + 3);
                field = field.toLowerCase().charAt(0) + field.substring(1);
                if (map.containsKey(field)) {
                    method.invoke(javabean, map.get(field));
                }
            }
        }
        return javabean;

    }
}
