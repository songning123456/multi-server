package com.uni.dubbo.service;

public interface RouterService {

    String getRoutersByUrl(String url);

    String getRoutersByTypeAndUrl(String requestType, String url);
}
