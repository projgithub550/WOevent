package com.lantu.woevent.models.auth;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "sys_token")
public class SysToken
{
    @Column(isNull = false,name = "user_id")
    private int userId;

    @IsKey
    @Column(type = "char",length = 36)
    private String token;
}
