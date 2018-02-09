package com.even.jwt.aop;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;
import java.util.List;

public class AuthMethodInterceptor implements MethodInterceptor {
    private List<String> exclude;

    public AuthMethodInterceptor(String[] exclude) {
        this.exclude = Arrays.asList(exclude);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String methodName = methodInvocation.getMethod().getName();
        if (exclude.contains(methodName)) {
            return methodInvocation.proceed();
        }
        return null;
    }
}
