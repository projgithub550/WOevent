package com.lantu.woevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lantu.woevent.mapper.ITokenMapper;
import com.lantu.woevent.models.auth.Role;
import com.lantu.woevent.models.auth.SysToken;
import com.lantu.woevent.service.IAuthService;
import com.lantu.woevent.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Service
public class AuthService implements IAuthService
{
    @Autowired
    private ITokenMapper mapper;

    @Override
    public String createToken(int userId)
    {
        SysToken token = new SysToken();

        String tok = findTokenByUserId(userId);
        if (tok == null || "".isEmpty())
        {
            token.setUserId(userId);
            token.setToken(TokenUtils.generateToken());
            mapper.insert(token);
            tok = token.getToken();
        }

        return tok;
    }

    @Override
    public SysToken findTokenByName(String token)
    {
        QueryWrapper<SysToken> wrapper = new QueryWrapper<>();
        wrapper.eq("token",token);

        SysToken sysToken = mapper.selectOne(wrapper);
        return sysToken;
    }

    @Override
    public String findTokenByUserId(int id)
    {
        QueryWrapper<SysToken> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id).select("token");
        String token = mapper.selectOne(wrapper).getToken();
        return token;
    }
}
