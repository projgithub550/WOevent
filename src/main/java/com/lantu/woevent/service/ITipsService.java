package com.lantu.woevent.service;

import com.lantu.woevent.models.Tips;

import java.util.List;


public interface ITipsService {
    public Tips findTipByID(Integer id);

    public List<Tips> findAllTips();

    public boolean addTip(Tips tip);

    public boolean deleteTip(Integer id);

    public boolean updateTip(Tips tip);
}
