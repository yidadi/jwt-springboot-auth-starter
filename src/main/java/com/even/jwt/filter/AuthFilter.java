package com.even.jwt.filter;


import com.alibaba.fastjson.JSON;
import com.even.jwt.config.AuthProperties;
import com.even.jwt.handler.TokenHandler;
import com.even.jwt.vo.ApiResponse;
import com.even.jwt.vo.ApiResponseCode;
import com.even.jwt.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权拦截filter
 */
@Component
public class AuthFilter implements Filter {
    @Autowired
    private AuthProperties authProperties;
    @Autowired
    private TokenHandler tokenHandler;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * 1,doFilter的第一个参数为ServletRequest对象。此对象给过滤器提供了对进入的信息（包括 　
         * 表单数据、cookie和HTTP请求头）的完全访问。第二个参数为ServletResponse，通常在简单的过 　
         * 滤器中忽略此参数。最后一个参数为FilterChain，此参数用来调用servlet或JSP页。
         */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        /**
         * 如果处理HTTP请求，并且需要访问诸如getHeader或getCookies等在ServletRequest中 　
         * 无法得到的方法，就要把此request对象构造成HttpServletRequest
         */
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if (authProperties.getExclude().contains(url)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authToken = request.getHeader("JWTAUTH");
        //未携带会话信息
        if (StringUtils.isEmpty(authToken)) {
            ApiResponse<String> authResponse = new ApiResponse();
            authResponse.setAuthCode(ApiResponseCode.AUHT_TOKEN_NONE);
            authResponse.setAuthMsg("not fund the token info");
            authResponse.setData("");
            response.addHeader("JWTAUTH", "");
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString(authResponse));
            return;
        }

        //用户信息
        UserVo userVo = tokenHandler.parseToken(authToken);
        if (userVo == null) {
            ApiResponse<String> authResponse = new ApiResponse();
            authResponse.setAuthCode(ApiResponseCode.AUHT_TOKEN_EXP);
            authResponse.setAuthMsg("the token expire");
            authResponse.setData("");
            response.addHeader("JWTAUTH", "");
            response.getWriter().write(JSON.toJSONString(authResponse));
            return;
        }
        //解析jwt获取用户信息
        authToken = tokenHandler.createToken(userVo);
        response.addHeader("JWTAUTH", authToken);
        request.setAttribute("userVo", userVo);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
