package com.lantu.woevent.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class TokenUtils
{
    public static String generateToken()
    {
        return UUID.randomUUID().toString();
    }

    public static String getRequestToken(HttpServletRequest request)
    {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty())
        {
            token = request.getParameter("token");
        }
        return token;
    }
}
