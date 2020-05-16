package com.uni.dubbo.service;

import com.alibaba.fastjson.JSONObject;

public interface RouterService {

    JSONObject getRoutersByUrl(String url);
}
