package com.lantu.woevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.woevent.models.News;
import com.lantu.woevent.models.Tips;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface ITipsMapper extends BaseMapper<Tips>{
}
