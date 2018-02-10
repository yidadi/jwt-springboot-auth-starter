package com.even.jwt.vo;

public class ApiResponseCode {
    /**
     * 认证授权成功
     */
    public static final Integer AUHT_TOKEN_SUCCESS = 10001;
    /**
     * 认证授权信息不存在
     */
    public static final Integer AUHT_TOKEN_NONE = 10002;
    /**
     * 认证授权信息过期
     */
    public static final Integer AUHT_TOKEN_EXP = 10003;
}
