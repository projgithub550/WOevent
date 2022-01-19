package com.lantu.woevent.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

@Data
@Table(name = "qa_base")
@TableName("qa_base")
public class QABase
{
    @TableId
    @IsKey
    @Column(isAutoIncrement = true,name = "q_id")
    private int qId;

    @Column(isNull = false,length = 70,type = "varchar")
    private String question;

    @Column(isNull = false,length = 100,type = "varchar")
    private String answer;
}
