package com.lantu.woevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lantu.woevent.mapper.ITipsMapper;
import com.lantu.woevent.models.News;
import com.lantu.woevent.models.Tips;
import com.lantu.woevent.service.ITipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipsService implements ITipsService {
    @Autowired
    private ITipsMapper mapper;

    @Override
    public Tips findTipByID(Integer id) {
        Tips tips = mapper.selectById(id);
        if (tips != null) {
            return tips;
        }
        return null;
    }

    @Override
    public Tips findTipForAndroid()
    {
        Tips tip = getOldTipForAndroid();

        //如果还没有设置，则返回最后一条
        if (tip == null)
        {
            Long count = mapper.selectCount(new QueryWrapper<Tips>());
            count -- ;
            if (count == -1)
            {
                return null;
            }
            QueryWrapper<Tips> wrapper = new QueryWrapper<>();
            wrapper.last("limit " + count + ",1" );
            return mapper.selectOne(wrapper);
        }

        return tip;
    }

    public List<Tips> findAllTips() {
        QueryWrapper<Tips> wrapper = new QueryWrapper<>();
        wrapper.select("t_id", "title","is_for_android");
        List<Tips> list = mapper.selectList(wrapper);
        return list;
    }

    public boolean addTip(Tips tips)
    {
        mapper.insertTip(tips.getTitle(), tips.getContent());
        return true;
    }

    public boolean deleteTip(Integer id)
    {
        if (isExist(id)) {
            mapper.deleteById(id);
            return true;
        }
        return false;
    }


    public boolean updateTip(Tips tip)
    {
        if (isExist(tip.getTId()))
        {
            UpdateWrapper<Tips> wrapper = new UpdateWrapper<>();
            wrapper.set("title", tip.getTitle());

            if (tip.getContent() != null) {
                wrapper.set("content", tip.getContent());
            }
            wrapper.eq("t_id", tip.getTId());
            mapper.update(tip, wrapper);
            return true;
        }
        return false;
    }

    @Override
    public boolean setTipForAndroid(Integer id)
    {
        if (!isExist(id))
        {
            return false;
        }

        UpdateWrapper<Tips> wrapper = new UpdateWrapper<>();
        Tips tip = getOldTipForAndroid();

        //删除之前设置的安卓新闻
        if (tip != null)
        {
            wrapper.eq("t_id",tip.getTId()).set("is_for_android",0);
            mapper.update(new Tips(),wrapper);
        }

        //设置新的安卓新闻
        wrapper.clear();
        wrapper.eq("t_id",id);
        wrapper.set("is_for_android",1);
        mapper.update(new Tips(),wrapper);
        return true;
    }

    private boolean isExist(Integer id) {
        QueryWrapper<Tips> wrapper = new QueryWrapper<>();
        wrapper.exists("select t_id from tips where t_id = \"" + id + "\"");
        return mapper.exists(wrapper);
    }



    private Tips getOldTipForAndroid()
    {
        QueryWrapper<Tips> wrapper = new QueryWrapper<>();
        wrapper.eq("is_for_android",1);
        return mapper.selectOne(wrapper);
    }
}

