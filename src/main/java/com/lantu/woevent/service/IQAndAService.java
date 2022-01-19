package com.lantu.woevent.service;

import com.lantu.woevent.models.QAndA;
import com.lantu.woevent.models.QAndASuperEntity;

import java.util.List;

public interface IQAndAService {

   //返回指定个数问答的方法

    List<QAndA> getQAndA(int value);


    //删除方法

       //返回所有问答的方法

    List<QAndA> getallQAndA();


       //删除指定id的方法

    void delQAndA( int id );

    //添加问答的方法

    void addQAndA(String question ,String answer);
}
