package com.lantu.woevent.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.models.auth.User;
import com.lantu.woevent.utils.TokenUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends AuthenticatingFilter
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        String token = TokenUtils.getRequestToken((HttpServletRequest) servletRequest);

        return new AuthToken(token);
    }

    //第一步，拦截出option方法外的所有请求
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
    {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name()))
        {
            return true;
        }return false;

    }

    //第二步，获取token,执行登录
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception
    {
        String token = TokenUtils.getRequestToken((HttpServletRequest) servletRequest);

        //如果token不存在
        if (token == null || token.isEmpty())
        {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(400);

            //返回错误信息
            ResultInfo<User> info = new ResultInfo();
            info.setSuccess(false);
            info.setMessage("请登录");
            info.setEntity(null);

            String json = MAPPER.writeValueAsString(info);
            response.getWriter().write(json);

            return false;
        }

        //执行登录
        return executeLogin(servletRequest,servletResponse);
    }
}
