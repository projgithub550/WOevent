package com.lantu.woevent.controller;

import com.lantu.woevent.models.QAndA;
import com.lantu.woevent.models.QAndASuperEntity;
import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.service.impl.QAndAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class QAndAController {


    //注入service
    @Autowired
    QAndAService QAndAService;


    //获取指定个数问答的方法

    @ResponseBody
    @GetMapping(value="/get_number")
    public  List<QAndA> getQAndA(@RequestParam("value") int value){


       List<QAndA> qAndA = null;
        //判断参数是否有效
        if(value == 0 ){


            return qAndA;
        }

         qAndA = QAndAService.getQAndA(value);


        return qAndA;
    }

    //删除操作
        // 一 返回所有问答
    @ResponseBody
    @GetMapping(value="/get_all")
    public List<QAndA> getAllQAndA(){


            //2 操作数据库
        return QAndAService.getallQAndA();

    }
        //二 删除单个问答

    @DeleteMapping(value = "/delete")
    public  ResponseEntity<ResultInfo<QAndASuperEntity>> delQAndA(@RequestParam(value = "id")
                                     int id){

        ResultInfo<QAndASuperEntity> QA = new ResultInfo<>();

            //1 判断参数是否有效
        if (id == 0 ){

            QA.setEntity(null);
            QA.setSuccess(false);
            QA.setMessage("没有指定所删除问答的id");

            return new ResponseEntity<ResultInfo<QAndASuperEntity>>(QA,HttpStatus.BAD_REQUEST);
        }

            //2 操作数据库
        QAndAService.delQAndA(id);

            //3 设置返回参数
         QA.setEntity(null);
         QA.setSuccess(true);
         QA.setMessage("删除成功");

        return new ResponseEntity<ResultInfo<QAndASuperEntity>>(QA,HttpStatus.OK);

    }

    //添加问答的方法 //单个添加

    @PostMapping(value="/add")
    public ResponseEntity<ResultInfo<QAndA>> addQAndA(@RequestParam(value = "question") String question,
                                                      @RequestParam(value = "question") String answer){





        //判断参数是否有效
        ResultInfo<QAndA> QA = new ResultInfo<>();

        //1 判断参数是否有效
        if (question == null || answer == null){

            QA.setEntity(null);
            QA.setSuccess(false);
            QA.setMessage("没有指定添加内容");

            return new ResponseEntity<ResultInfo<QAndA>>(QA,HttpStatus.BAD_REQUEST);
        }

           //操作数据库
        QAndAService.addQAndA(question , answer);

        QA.setEntity(null);
        QA.setSuccess(true);
        QA.setMessage("添加成功");

        return new ResponseEntity<ResultInfo<QAndA>>(QA,HttpStatus.OK);


    }
}
