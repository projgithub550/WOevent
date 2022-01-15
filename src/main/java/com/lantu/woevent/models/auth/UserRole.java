package com.lantu.woevent.models.auth;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "user_role")
public class UserRole
{
    @Column(name="user_id",isNull = false)
    private int userId;

    @Column(name="role_id",isNull = false)
    private int roleId;
}
