package com.sn.common.util;

import com.sn.common.util.classconvert.ClassConvertContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sn
 */
public class ClassConvertUtil {

    /**
     * 将dto和entity之间的属性互相转换,dto中属性一般为String等基本类型
     * 但是entity中可能有复合主键等复杂类型,需要注意同名问题
     *
     * @param src
     * @param target
     */
    public static void populate(Object src, Object target) {

        // 获取当前类定义的所有方法，不包括父类和接口的
        Method[] srcMethods = src.getClass().getDeclaredMethods();
        Method[] targetMethods = target.getClass().getDeclaredMethods();

        for (Method srcMethod : srcMethods) {
            String srcMethodName = srcMethod.getName();
            if (srcMethodName.startsWith("get")) {
                try {
                    Object result = srcMethod.invoke(src);
                    // 获取 get 方法返回值类型
                    Class<?> srcConvertClass = srcMethod.getReturnType();

                    for (Method targetMethod : targetMethods) {
                        String targetMethodName = targetMethod.getName();
                        if (targetMethodName.startsWith("set") && targetMethodName.substring(3).equals(srcMethodName.substring(3))) {
                            // 获取 set 方法 参数类型
                            Class<?> targetConvertClass = targetMethod.getParameterTypes()[0];
                            // 执行 类型转化
                            if (result != null && srcConvertClass != targetConvertClass) {
                                result = ClassConvertContext.getInstance(srcConvertClass, targetConvertClass).classConvert(result);
                            }
                            targetMethod.invoke(target, result);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * dto集合和entity集合间的互相属性映射
     *
     * @param src
     * @param target
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S, T> List<T> populateList(List<S> src, List<T> target, Class<?> targetClass) {
        for (int i = 0; i < src.size(); i++) {
            try {
                Object object = targetClass.newInstance();
                target.add((T) object);
                populate(src.get(i), object);
            } catch (Exception e) {
                // 某个方法反射异常
                continue;
            }
        }
        return target;
    }

    /**
     * @param src
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> void castEntityList(List<Object[]> src, List<T> target, Class<T> clazz) throws Exception {
        if (src.isEmpty()) {
            throw new Exception("数组列表转换实体类不能为空!");
        }
        if (!target.isEmpty()) {
            target.clear();
        }
        Object[] co = src.get(0);
        Class[] c2 = new Class[co.length];

        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }
        for (Object[] o : src) {
            Constructor<T> constructor = clazz.getConstructor(c2);
            target.add(constructor.newInstance(o));
        }
    }

    /**
     * @param src
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T caseEntity(Object src, Class<T> clazz) throws Exception {
        Object[] co = (Object[]) src;
        Class[] c2 = new Class[co.length];
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }
        Constructor<T> constructor = clazz.getConstructor(c2);
        T result = constructor.newInstance(co);
        return result;
    }
}
