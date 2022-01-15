package com.lantu.woevent.models.auth;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Table(name = "permission")
public class Permission
{
    @IsKey
    @IsAutoIncrement
    @Column(name = "p_id")
    private int pId;

    @Column(name = "permission_name",type = "varchar",length = 50,isNull = false)
    private String permissionName;
}
