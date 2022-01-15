package com.lantu.woevent.models.auth;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@ToString
@Data
@Table(name = "role")
public class Role
{
    @IsKey
    @IsAutoIncrement
    @Column(name = "r_id")
    private int rId;

    @Column(name = "role_name",type = "varchar",length = 50,isNull = false)
    private String roleName;

    @TableField(exist = false)
    private Set<Permission> permissions;
}
