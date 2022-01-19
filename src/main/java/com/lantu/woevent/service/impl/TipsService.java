package com.lantu.woevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lantu.woevent.mapper.ITipsMapper;
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

    public List<Tips> findAllTips() {
        QueryWrapper<Tips> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title");
        List<Tips> list = mapper.selectList(wrapper);
        return list;
    }

    public boolean addTip(Tips tips) {
        mapper.insert(tips);
        return true;
    }

    public boolean deleteTip(Integer id) {
        if (isExist(id)) {
            mapper.deleteById(id);
            return true;
        }

        return false;
    }


    public boolean updateTip(Tips tip) {
        if (isExist(tip.getId())) {
            UpdateWrapper<Tips> wrapper = new UpdateWrapper<>();
            wrapper.set("title", tip.getTitle());

            if (tip.getContent() != null) {
                wrapper.set("content", tip.getContent());
            }
            wrapper.eq("id", tip.getId());
            mapper.update(tip, wrapper);
            return true;
        }
        return false;
    }


    private boolean isExist(Integer id) {
        QueryWrapper<Tips> wrapper = new QueryWrapper<>();
        wrapper.exists("select id from tips where id = \"" + id + "\"");
        return mapper.exists(wrapper);
    }

}

