package com.simple.blog.service.impl;

import com.simple.blog.service.MemoryService;
import com.sn.common.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songning
 * @date 2020/1/8
 * description
 */
@Service
public class MemoryServiceImpl implements MemoryService {

    private final Map<String, Object> memoryMap = new ConcurrentHashMap<>();

    @Override
    public void deleteValue(String... name) {
        String key = StringUtil.getString(name);
        memoryMap.remove(key);
    }

    @Override
    public void deleteValues(String... name) {
        String prefixKey = StringUtil.getString(name);
        memoryMap.keySet().removeIf(key -> key.contains(prefixKey));
    }

    @Override
    public Object getValue(String... name) {
        String key = StringUtil.getString(name);
        return memoryMap.get(key);
    }

    @Override
    public Map<String, Object> getValues(String... name) {
        Map<String, Object> result = new TreeMap<>((o1, o2) -> {
            String c1 = o1.split(":")[o1.split(":").length - 1];
            String c2 = o2.split(":")[o2.split(":").length - 1];
            return c1.compareTo(c2);
        });
        String prefixKey = StringUtil.getString(name);
        for (Map.Entry<String, Object> entry : memoryMap.entrySet()) {
            if (entry.getKey().contains(prefixKey)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public void setValue(String key, Object value) {
        memoryMap.put(key, value);
    }
}
