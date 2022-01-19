package com.lantu.woevent.controller;

import com.lantu.woevent.models.QABase;
import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.service.impl.QAndAService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class QAndAController {


    //注入service
    @Autowired
    private QAndAService QAndAService;


    //获取指定个数问答的方法
    @GetMapping(value="/adrd/qa")
    public  ResponseEntity<ResultInfo<List<QABase>>> getQAndA(@RequestParam(value = "number",required = false) Integer number)
    {
        ResultInfo<List<QABase>> ri = new ResultInfo<>();

        List<QABase> qas = null;
        //判断参数是否有效
        if(number == null || number == 0)
        {
            ri.setSuccess(false);
            ri.setMessage("请给出需要的问题答案个数");
            ri.setEntity(null);
            return new ResponseEntity<>(ri,HttpStatus.BAD_REQUEST);
        }

        qas = QAndAService.getQAndA(number);
        HttpStatus status;
        if (qas.size() == 0)
        {
            ri.setSuccess(false);
            ri.setMessage("问答库无记录");
            ri.setEntity(null);
            status = HttpStatus.NOT_FOUND;
        }
        else
        {
            ri.setSuccess(true);
            ri.setMessage("查找成功");
            ri.setEntity(qas);
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(ri,status);
    }

    // 一 返回所有问答
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    @GetMapping(value="/qa/list")
    public ResponseEntity<ResultInfo<List<QABase>>> getAllQAndA()
    {
        ResultInfo<List<QABase>> ri = new ResultInfo<>();
        ri.setSuccess(true);

        List<QABase> list = QAndAService.getAllQAndA();
        ri.setMessage("问答库的总数为：" + list.size());
        ri.setEntity(list);

        return new ResponseEntity<>(ri,HttpStatus.OK);

    }


    //二 删除单个问答
    @RequiresRoles("admin")
    @DeleteMapping(value = "/qa/{id}")
    public  ResponseEntity<ResultInfo<QABase>> delQAndA(@PathVariable(value = "id",required = false) Integer id)
    {

        ResultInfo<QABase> QA = new ResultInfo<>();

        //1 判断参数是否有效
        if (id == null)
        {
            QA.setEntity(null);
            QA.setSuccess(false);
            QA.setMessage("请给出要删除的问答id");
            return new ResponseEntity<>(QA,HttpStatus.BAD_REQUEST);
        }

        //2 操作数据库
        QAndAService.delQAndA(id);

        //3 设置返回参数
         QA.setEntity(null);
         QA.setSuccess(true);
         QA.setMessage("删除成功");
        return new ResponseEntity<ResultInfo<QABase>>(QA,HttpStatus.OK);
    }

    //添加问答的方法
    @RequiresRoles("admin")
    @PostMapping(value="/qa")
    public ResponseEntity<ResultInfo<QABase>> addQAndA(@RequestParam(value = "question",required = false) String question,
                                                      @RequestParam(value = "answer",required = false) String answer){


        //判断参数是否有效
        ResultInfo<QABase> QA = new ResultInfo<>();

        //1 判断参数是否有效
        if (question == null || "".equals(question)
                || answer == null || "".equals(answer))
        {
            QA.setEntity(null);
            QA.setSuccess(false);
            QA.setMessage("没有指定添加内容");
            return new ResponseEntity<ResultInfo<QABase>>(QA,HttpStatus.BAD_REQUEST);
        }

        //操作数据库
        QAndAService.addQAndA(question , answer);
        QA.setEntity(null);
        QA.setSuccess(true);
        QA.setMessage("添加成功");

        return new ResponseEntity<ResultInfo<QABase>>(QA,HttpStatus.OK);
    }

    @PutMapping("/qa/{id}")
    public ResponseEntity<ResultInfo<QABase>> updateQAndA(@PathVariable(value = "id",required = false) Integer id,
                                                            @RequestParam(value = "question",required = false) String question,
                                                          @RequestParam(value = "answer",required = false) String answer)
    {
        //判断参数是否有效
        ResultInfo<QABase> QA = new ResultInfo<>();

        if (id == null)
        {
            QA.setSuccess(false);
            QA.setEntity(null);
            QA.setMessage("请给出要更新的问答ID");
            return new ResponseEntity<>(QA,HttpStatus.BAD_REQUEST);
        }

        if (question == null || "".equals(question)
                || answer == null || "".equals(answer))
        {
            QA.setEntity(null);
            QA.setSuccess(false);
            QA.setMessage("问题和答案不能为空");
            return new ResponseEntity<ResultInfo<QABase>>(QA,HttpStatus.BAD_REQUEST);
        }

        QABase base = new QABase();
        base.setQId(id);
        base.setQuestion(question);
        base.setAnswer(answer);
        QAndAService.updateQAndA(base);

        QA.setEntity(null);
        QA.setSuccess(true);
        QA.setMessage("修改成功");
        return new ResponseEntity<ResultInfo<QABase>>(QA,HttpStatus.OK);
    }
}
