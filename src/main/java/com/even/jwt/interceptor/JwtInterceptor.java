package com.even.jwt.interceptor;

import com.alibaba.fastjson.JSON;
import com.even.jwt.annation.ValidateLogin;
import com.even.jwt.handler.TokenHandler;
import com.even.jwt.vo.ApiResponse;
import com.even.jwt.vo.ApiResponseCode;
import com.even.jwt.vo.UserVo;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    private TokenHandler tokenHandler;

    public JwtInterceptor(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod)handler;
            ValidateLogin validateLogin = method.getMethodAnnotation(ValidateLogin.class);
            if(validateLogin == null){
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
                    return false;
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
                    return false;
                }
                //解析jwt获取用户信息
                authToken = tokenHandler.createToken(userVo);
                response.addHeader("JWTAUTH", authToken);
                request.setAttribute("userVo", userVo);
            }
        }
        return super.preHandle(request, response, handler);
    }
}
