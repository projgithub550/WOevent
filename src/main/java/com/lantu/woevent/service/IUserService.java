package com.lantu.woevent.service;


import com.lantu.woevent.models.auth.Permission;
import com.lantu.woevent.models.auth.Role;
import com.lantu.woevent.models.auth.User;

import java.util.List;

public interface IUserService {
    public User findUserByUsername(String username);

    public User findUserById(int id);

    public List<Role> findRolesByUserId(int id);

    public List<Permission> findPermissionsByRoleId(int id);
}
