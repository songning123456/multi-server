package com.uni.dubbo.service;

import java.util.Map;

/**
 * @author sonin
 * @date 2020/5/25 19:43
 */
public interface GenerateIdService {

    /**
     * 获取雪花Id
     *
     * @return
     */
    Map<String, Object> getSnowflakeId();
}
