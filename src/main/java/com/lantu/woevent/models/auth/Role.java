package com.lantu.woevent.models.auth;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@ToString
@Data
@Table(name = "role")
public class Role
{
    @Column(isKey = true,isAutoIncrement = true,name = "r_id")
    private int rId;

    @Column(type = "varchar",length = 50,isNull = false,name = "role_name")
    private String roleName;

    @TableField(exist = false)
    private List<Permission> permissions;
}
