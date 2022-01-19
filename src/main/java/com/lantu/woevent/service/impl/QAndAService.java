package com.lantu.woevent.service.impl;

import com.lantu.woevent.mapper.QAdnAMapper;
import com.lantu.woevent.models.QAndA;
import com.lantu.woevent.models.QAndASuperEntity;
import com.lantu.woevent.service.IQAndAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QAndAService implements IQAndAService {

    //注入mapper
    @Autowired
    QAdnAMapper QAdnAMapper;

    //返回指定个数问答的方法
    @Override
    public List<QAndA> getQAndA(int value) {

        return QAdnAMapper.getQAndA(value);
    }

    //删除方法
         //返回所有问答
    @Override
    public List<QAndA> getallQAndA() {


        return QAdnAMapper.getallQAndA();
    }
        //删除特定id的问答
    @Override
    public void delQAndA( int id) {

        QAdnAMapper.delQAndA(id );

    }

    @Override
    public void addQAndA(String question ,String answer) {

        QAdnAMapper.addQAndA( question,answer);

    }
}
