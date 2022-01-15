package com.lantu.woevent.models.auth;


import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "role_permission")
public class RolePermission
{
    @Column(name = "role_id",isNull = false)
    private int roleId;

    @Column(name = "permission_id",isNull = false)
    private int permissionId;
}
