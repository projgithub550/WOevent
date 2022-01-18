package com.lantu.woevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.woevent.models.auth.Permission;
import com.lantu.woevent.models.auth.Role;
import com.lantu.woevent.models.auth.User;
import org.apache.ibatis.annotations.Select;
import org.w3c.dom.ls.LSInput;

import java.util.List;

public interface IUserMapper extends BaseMapper<User>
{
    @Select("select r_id as rId ,role_name as roleName from role join user_role on r_id = role_id where user_id = #{id}")
    public List<Role> findRolesByUserId(int id);

    @Select("select p_id as pId,permission_name as permissionName from permission join role_permission on p_id = permission_id " +
            "where role_id = #{id}")
    public List<Permission> findPermissionsByRoleId(int id);
}
