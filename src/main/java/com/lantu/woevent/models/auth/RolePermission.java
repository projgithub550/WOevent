package com.lantu.woevent.models.auth;


import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "role_permission")
public class RolePermission
{
    @Column(isNull = false,name = "role_id")
    private int roleId;

    @Column(isNull = false,name = "permission_id")
    private int permissionId;
}
