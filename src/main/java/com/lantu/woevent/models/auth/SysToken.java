package com.lantu.woevent.models.auth;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "token")
public class SysToken
{
    @Column(name = "user_id",isNull = false)
    private int userId;

    @IsKey
    @Column(type = "char",length = 36)
    private String token;
}
