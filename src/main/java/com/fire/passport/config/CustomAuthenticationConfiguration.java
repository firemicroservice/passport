package com.fire.passport.config;

import com.fire.passport.CustomUsernamePasswordAuthentication;
import com.fire.passport.mapper.CasUserMapper;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration //("CustomAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Resource
    private ServicesManager servicesManager;

    @Resource
    private CasUserMapper casUserMapper;

    @Bean
    public AuthenticationHandler mysqlAuthenticationHandler() {
        // 参数: name, servicesManager, principalFactory, order
        // 定义为优先使用它进行认证
        return new CustomUsernamePasswordAuthentication(CustomUsernamePasswordAuthentication.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1, casUserMapper);
    }

    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(mysqlAuthenticationHandler());
    }
}
