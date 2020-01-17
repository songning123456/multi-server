package com.simple.blog.service;

import java.util.Map;

/**
 * @author songning
 * @date 2020/1/8
 * description
 */
public interface MemoryService {
    void deleteValue(String... name);

    void deleteValues(String... name);

    Object getValue(String... name);

    Map<String, Object> getValues(String... name);

    void setValue(String key, Object value);
}
