package com.lantu.woevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.woevent.models.QABase;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IQAndAMapper extends BaseMapper<QABase>
{


    //返回指定个数问答的方法
    @Select("SELECT * FROM qa_base ORDER BY RAND() LIMIT #{number}")
    List<QABase> getQAndA(int number);

    //返回所有问答
    @Select("SELECT * FROM qa_base")
    List<QABase> getAllQAndA();

    //删除删除指定id的行
    @Delete("DELETE FROM qa_base WHERE q_id = #{id}")
    void delQAndA(int id);

    //添加方法
    @Insert("INSERT INTO qa_base(question,answer) VALUES(#{question},#{answer})")
    void addQAndA(String question ,String answer);


}
