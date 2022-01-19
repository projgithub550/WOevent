package com.lantu.woevent.service;

import com.lantu.woevent.models.auth.Role;
import com.lantu.woevent.models.auth.SysToken;

import java.util.List;

public interface IAuthService
{
    public String createToken(int userId);

    public SysToken findTokenByName(String token);

    public SysToken findTokenByUserId(int id);

}
