package com.lantu.woevent.models;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.Getter;

@Data
@Table(name = "tips")
public class Tips
{
    @TableId(value = "t_id")
    @Column(name = "t_id",isKey = true,isAutoIncrement = true)
    private Integer tId;

    @Column(isNull = false,type = "varchar",length = 60)
    private String title;

    @Column(isNull = false,type = "longblob")
    private byte[] content;

    @Column(type = "tinyint",isNull = false,name = "is_for_android")
    private boolean isForAndroid;
}
