package com.lantu.woevent.models;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Tips {
    @TableId(value = "id")
    private Integer id;

    private String title;

    private byte[] content;
}
