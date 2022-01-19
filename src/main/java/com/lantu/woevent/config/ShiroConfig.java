package com.lantu.woevent.config;

import com.lantu.woevent.auth.AuthFilter;
import com.lantu.woevent.auth.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig
{

    @Autowired
    private MyRealm realm;

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager()
    {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        manager.setRememberMeManager(null);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager)
    {
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(securityManager);
        //设置过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("auth",new AuthFilter());
        factory.setFilters(filters);

        //设置过滤链
        LinkedHashMap<String,String> filterChain = new LinkedHashMap<>();

        //如果是登录或是安卓端申请资源，则直接放行
        filterChain.put("/login","anon");
        filterChain.put("/adrd/**","anon");
        //filterChain.put("/tip_form","anon");
        //filterChain.put("/l_form","anon");
        //filterChain.put("/tips","anon");

        //其余的请求全部要验证
        filterChain.put("/**","auth");

        factory.setFilterChainDefinitionMap(filterChain);
        return factory;
    }

//    @Bean("lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
    @Bean
   // @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager());
        return advisor;
    }

}
