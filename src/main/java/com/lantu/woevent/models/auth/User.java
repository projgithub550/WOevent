package com.lantu.woevent.models.auth;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Table(name="user")
public class User
{
    @TableId
    @Column(isKey = true,isAutoIncrement = true,name = "u_id")
    private int uId;

    @Column(type = "varchar",length = 60,isNull = false)
    private String username;

    @Column(type = "varchar",length = 60,isNull = false)
    private String password;

    @TableField(exist = false)
    private List<Role> roles;
}
