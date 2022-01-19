package com.lantu.woevent.service;

import com.lantu.woevent.models.QABase;

import java.util.List;

public interface IQAndAService
{

   //返回指定个数问答的方法

    List<QABase> getQAndA(int number);

    //返回所有问答的方法
    List<QABase> getAllQAndA();

    //删除指定id的方法
    void delQAndA(int id);

    //添加问答的方法
    void addQAndA(String question ,String answer);

    void updateQAndA(QABase qa);
}
