package com.lantu.woevent.models;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class News
{
    @TableId(value = "news_date")
    private String newsDate;

    private String title;

    private byte[] content;
}
