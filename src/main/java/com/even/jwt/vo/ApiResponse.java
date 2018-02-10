package com.even.jwt.vo;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {

    /**
     * 授权响应码
     */
    private Integer authCode;

    /**
     * 授权响应信息
     */
    private String authMsg;

    /**
     * 具体参数
     */
    private T data;

    public Integer getAuthCode() {
        return authCode;
    }

    public void setAuthCode(Integer authCode) {
        this.authCode = authCode;
    }

    public String getAuthMsg() {
        return authMsg;
    }

    public void setAuthMsg(String authMsg) {
        this.authMsg = authMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
