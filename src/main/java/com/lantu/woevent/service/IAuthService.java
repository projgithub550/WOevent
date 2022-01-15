package com.lantu.woevent.service;

import com.lantu.woevent.models.auth.SysToken;

public interface IAuthService
{
    public String createToken(int userId);

    public SysToken findTokenByName(String token);

}
