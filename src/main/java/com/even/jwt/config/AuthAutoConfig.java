package com.even.jwt.config;

import com.even.jwt.annation.JwtAuth;
import com.even.jwt.aop.AuthMethodInterceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties
public class AuthAutoConfig extends AbstractPointcutAdvisor {
    private Pointcut pointcut;

    private Advice advice;

    @Autowired
    private AuthProperties authProperties;

    @PostConstruct
    public void init() {
        this.pointcut = new AnnotationMatchingPointcut(null, JwtAuth.class);
        this.advice = new AuthMethodInterceptor(authProperties.getExcludeArr());
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }
}
