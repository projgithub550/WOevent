package com.lantu.woevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lantu.woevent.mapper.IUserMapper;
import com.lantu.woevent.models.auth.User;
import com.lantu.woevent.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService
{
    @Autowired
    public IUserMapper mapper;

    @Override
    public User findUserByUsername(String username)
    {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = mapper.selectOne(wrapper);
        return user;
    }

    @Override
    public User findUserById(int id)
    {
        return mapper.selectById(id);
    }

}
