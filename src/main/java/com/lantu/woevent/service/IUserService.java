package com.lantu.woevent.service;


import com.lantu.woevent.models.auth.User;

public interface IUserService {
    public User findUserByUsername(String username);

    public User findUserById(int id);
}
