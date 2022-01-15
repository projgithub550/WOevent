package com.lantu.woevent.config;

import com.lantu.woevent.auth.AuthFilter;
import com.lantu.woevent.auth.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig
{
    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private MyRealm realm;

    @Bean
    public SecurityManager defaultWebSecurityManager()
    {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        manager.setRememberMeManager(null);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean()
    {
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(defaultWebSecurityManager());
        //设置过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("auth",authFilter);
        factory.setFilters(filters);

        //设置过滤链
        LinkedHashMap<String,String> filterChain = new LinkedHashMap<>();

        //如果是登录或是安卓端申请资源，则直接放行
        filterChain.put("/login","anon");
        filterChain.put("/adrd/**","anon");

        //其余的请求全部要验证
        filterChain.put("/**","auth");

        factory.setFilterChainDefinitionMap(filterChain);
        return factory;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
