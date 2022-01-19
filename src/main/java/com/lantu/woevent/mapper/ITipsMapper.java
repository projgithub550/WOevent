package com.lantu.woevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.woevent.models.News;
import com.lantu.woevent.models.Tips;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ITipsMapper extends BaseMapper<Tips>
{
    @Insert("insert into tips(title,content,is_for_android) values(#{title},#{content},0)")
    int insertTip(String title,byte[] content);
}
