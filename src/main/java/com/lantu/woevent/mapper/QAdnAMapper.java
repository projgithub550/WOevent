package com.lantu.woevent.mapper;

import com.lantu.woevent.models.QAndA;
import com.lantu.woevent.models.QAndASuperEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QAdnAMapper  {


    //返回指定个数问答的方法
    @Select("SELECT * FROM qa ORDER BY RAND() LIMIT #{value}")
    List<QAndA> getQAndA(int value);

    //删除方法

        //返回所有问答
    @Select("SELECT * FROM qa")
    List<QAndA> getallQAndA();

        //删除删除指定id的行
    @Delete("DELETE FROM qa WHERE id = #{id}")
    void delQAndA(int id);


    //添加方法
    @Insert("INSERT INTO qa(question,answer)VALUES(#{question},#{answer})")
     void addQAndA(String question ,String answer);





}
