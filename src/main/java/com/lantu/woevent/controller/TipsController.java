package com.lantu.woevent.controller;

import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.models.Tips;
import com.lantu.woevent.service.ITipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class TipsController
{
    @Autowired
    private ITipsService service;
    /*
    @GetMapping("/form")
    public String toNews()
    {
        //System.out.println("sss");
        return "news_page";
    }*/

    @GetMapping("/tips")
    public ResponseEntity<ResultInfo<Tips>> getTip(@RequestParam(value = "id",required = false) Integer id)
    {
        ResultInfo<Tips> ri= new ResultInfo<Tips>();
        //判断参数是否有效
        if (id == null)
        {
            ri.setEntity(null);
            ri.setMessage("请给出科普知识ID");
            ri.setSuccess(false);
           return new ResponseEntity<ResultInfo<Tips>>(ri, HttpStatus.BAD_REQUEST);
        }

        Tips tip = service.findTipByID(id);

        //如果查到，返回成功的结果
        if (tip != null)
        {
            ri.setEntity(tip);
            ri.setMessage("查询成功");
            ri.setSuccess(true);
            return new ResponseEntity<ResultInfo<Tips>>(ri, HttpStatus.OK);
        }
        else
        {
            ri.setEntity(null);
            ri.setMessage("科普知识不存在，查询失败");
            ri.setSuccess(false);
            return new ResponseEntity<ResultInfo<Tips>>(ri, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/tips/list")
    public ResponseEntity<ResultInfo<List<Tips>>> showAllTips()
    {
        ResultInfo<List<Tips>> ri = new ResultInfo<>();
        ri.setSuccess(true);

        List<Tips> list = service.findAllTips();
        ri.setEntity(list);
        ri.setMessage("科普知识总数为：" + list.size());
        return new ResponseEntity<ResultInfo<List<Tips>>>(ri,HttpStatus.OK);
    }

    @GetMapping("/tips/{id}")
    public void downloadTip(@PathVariable(value = "id") Integer id, HttpServletResponse response)
    {
        Tips tip = service.findTipByID(id);

        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        try {
            response.addHeader("Content-Disposition","inline;filename=" +
                    URLEncoder.encode(tip.getTitle() + ".pdf","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            byte[] content = tip.getContent();
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/tips")
    public ResponseEntity<ResultInfo<Tips>> addTip(@RequestParam(value = "id",required = false) Integer id,
                                                    @RequestParam(value = "title",required = false) String title,
                                                    @RequestParam(value = "tips_file",required = false)MultipartFile file)
    {
        ResultInfo<Tips> ri = new ResultInfo<Tips>();

        //判断参数有效性
        if (id == null
        || title == null || "".equals(title)
        || file == null || file.isEmpty())
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("提交的信息不完整，无法添加科普知识");
            return new ResponseEntity<ResultInfo<Tips>>(ri,HttpStatus.BAD_REQUEST);
        }

        Tips tip = new Tips();
        tip.setId(id);
        tip.setTitle(title);

        //将文件转为字节流
        byte[] data = toByte(file);
        HttpStatus status;

        if (data == null)
        {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        else
        {
            tip.setContent(data);
            boolean success = service.addTip(tip);

            //如果要添加的科普知识先前不存在，则添加成功，返回成功结果
            ri.setEntity(null);
            if (success)
            {
                ri.setSuccess(true);
                ri.setMessage("添加科普知识成功");
                status = HttpStatus.OK;
            }
            else {
                ri.setSuccess(false);
                ri.setMessage("该ID下的科普知识已经存在，添加失败");
                status = HttpStatus.BAD_REQUEST;
            }
        }
        return new ResponseEntity<ResultInfo<Tips>>(ri,status);
    }

    @DeleteMapping("/tips/{id}")
    public ResponseEntity<ResultInfo<Tips>> removeTip(@PathVariable(value = "id",
                                                required = false) Integer id)
    {
        ResultInfo<Tips> ri = new ResultInfo<Tips>();
        if (id == null)
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("请给出科普知识ID");
            return new ResponseEntity<ResultInfo<Tips>>(ri,HttpStatus.BAD_REQUEST);
        }

        boolean success = service.deleteTip(id);

        //如果存在，则可以删除，返回成功结果
        HttpStatus status;
        if(success)
        {
            ri.setSuccess(true);
            ri.setMessage("删除科普知识成功");
            status = HttpStatus.OK;
        }
        else
        {
            ri.setSuccess(false);
            ri.setMessage("科普知识不存在，删除失败");
            status = HttpStatus.NOT_FOUND;
        }

        ri.setEntity(null);
        return new ResponseEntity<ResultInfo<Tips>>(ri,status);
    }

    @PutMapping("/tips/{id}")
    public ResponseEntity<ResultInfo<Tips>> updateTip(@PathVariable(value = "id",required = false) Integer id,
                                                       @RequestParam(value = "title",required = false) String title,
                                                       @RequestParam(value = "tips_file",required = false) MultipartFile file)
    {
        ResultInfo<Tips> ri = new ResultInfo<Tips>();
        //参数有效性判断
        if(id == null)
        {
            ri.setMessage("请给出ID");
            ri.setSuccess(false);
            ri.setEntity(null);
            return new ResponseEntity<ResultInfo<Tips>>(ri,HttpStatus.BAD_REQUEST);
        }

        if (title == null || "".equals(title))
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("提交的信息不完整，无法更新科普文件");
            return new ResponseEntity<ResultInfo<Tips>>(ri,HttpStatus.BAD_REQUEST);
        }

        //若上传了文件，则将文件转为字节流
        byte data[] = null;
        if(file != null && !file.isEmpty())
        {
            data = toByte(file);
        }

        Tips tip = new Tips();
        tip.setId(id);
        tip.setContent(data);
        tip.setTitle(title);

        boolean success = service.updateTip(tip);
        HttpStatus status;
        if (success)
        {
            ri.setSuccess(true);
            ri.setMessage("科普知识修改成功");
            status = HttpStatus.OK;
        }
        else {
            ri.setSuccess(false);
            ri.setMessage("要修改的科普知识不存在，更新失败");
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<ResultInfo<Tips>>(ri,status);
    }


    private byte[] toByte(MultipartFile file)
    {
        InputStream ins = null;
        try {
            //将文件以字节流形式写入变量data,借助buffer缓存
            ins = file.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            while ((len = ins.read(buffer)) != -1) {
                bas.write(buffer, 0, len);
            }
            bas.flush();
            return bas.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
