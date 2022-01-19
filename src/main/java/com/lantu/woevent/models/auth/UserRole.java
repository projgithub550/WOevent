package com.lantu.woevent.models.auth;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "user_role")
public class UserRole
{
    @Column(isNull = false,name = "user_id")
    private int userId;

    @Column(isNull = false,name = "role_id")
    private int roleId;
}
