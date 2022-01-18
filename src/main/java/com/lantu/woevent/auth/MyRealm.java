package com.lantu.woevent.auth;


import com.lantu.woevent.models.auth.Permission;
import com.lantu.woevent.models.auth.Role;
import com.lantu.woevent.models.auth.SysToken;
import com.lantu.woevent.models.auth.User;
import com.lantu.woevent.service.IAuthService;
import com.lantu.woevent.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MyRealm extends AuthorizingRealm
{
    @Autowired
    private IUserService service_u;

    @Autowired
    private IAuthService service_a;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        User user = (User) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo info  = new SimpleAuthorizationInfo();

        //获取用户的角色和权限
        List<Role> roles = service_u.findRolesByUserId(user.getUId());
        user.setRoles(roles);
        for (Role role:roles)
        {
            info.addRole(role.getRoleName());
            List<Permission> permissions = service_u.findPermissionsByRoleId(role.getRId());
            for (Permission permission: permissions)
            {
                info.addStringPermission(permission.getPermissionName());
            }
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        String uToken = (String) token.getPrincipal();

        SysToken sysToken = service_a.findTokenByName(uToken);

        //token不存在
        if (sysToken == null)
        {
            throw new ExpiredCredentialsException("请重新登录");
        }


        User user = service_u.findUserById(sysToken.getUserId());

        //user不存在
        if (user == null)
        {
            throw new UnknownAccountException("用户不存在");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,uToken,this.getName());

        return authenticationInfo;
    }
}
