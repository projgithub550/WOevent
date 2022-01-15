package com.lantu.woevent.models;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "news")
public class News
{
    @TableId
    @IsKey
    @Column(name = "news_date",type = "date")
    private String newsDate;

    @Column(isNull = false,type = "varchar",length = 60)
    private String title;

    @Column(isNull = false,type = "longblob")
    private byte[] content;
}
