package com.lantu.woevent.service.impl;

import com.lantu.woevent.mapper.IQAndAMapper;
import com.lantu.woevent.models.QABase;
import com.lantu.woevent.service.IQAndAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QAndAService implements IQAndAService {

    //注入mapper
    @Autowired
    IQAndAMapper QAndAMapper;

    //返回指定个数问答的方法
    @Override
    public List<QABase> getQAndA(int number)
    {
        return QAndAMapper.getQAndA(number);
    }

    //返回所有问答
    @Override
    public List<QABase> getAllQAndA()
    {
        return QAndAMapper.getAllQAndA();
    }

    //删除特定id的问答
    @Override
    public void delQAndA(int id)
    {
        QAndAMapper.delQAndA(id);
    }

    @Override
    public void addQAndA(String question ,String answer)
    {
        QAndAMapper.addQAndA(question,answer);
    }

    @Override
    public void updateQAndA(QABase qa)
    {
        QAndAMapper.updateById(qa);
    }
}
