package com.lantu.woevent.controller;

import com.lantu.woevent.models.News;
import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.service.INewsService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.List;

@Controller
public class NewsController
{
    @Autowired
    private INewsService service;

    @GetMapping("/form")
    public String toNews()
    {
        System.out.println("sss");
        return "news_page";
    }

    @GetMapping("/adrd/news")
    public ResponseEntity<ResultInfo<News>> getNews()
    {
        ResultInfo<News> ri= new ResultInfo<>();

        News news = service.findNewsForAndroid();

        //如果查到，返回成功的结果
        if (news != null)
        {
            ri.setEntity(news);
            ri.setMessage("查询成功");
            ri.setSuccess(true);
            return new ResponseEntity<ResultInfo<News>>(ri, HttpStatus.OK);
        }
        else
        {
            ri.setEntity(null);
            ri.setMessage("新闻不存在，查询失败");
            ri.setSuccess(false);
            return new ResponseEntity<ResultInfo<News>>(ri, HttpStatus.NOT_FOUND);
        }


    }



    @GetMapping("/news/list")
    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    public ResponseEntity<ResultInfo<List<News>>> showAllNews()
    {
        ResultInfo<List<News>> ri = new ResultInfo<>();
        ri.setSuccess(true);

        List<News> list = service.findAllNews();
        ri.setEntity(list);
        ri.setMessage("新闻总数为：" + list.size());
        return new ResponseEntity<ResultInfo<List<News>>>(ri,HttpStatus.OK);
    }

    @GetMapping("/news/for_adrd")
    @RequiresRoles("admin")
    @RequiresPermissions("set")
    public ResponseEntity<ResultInfo<News>> setNewsForAndroid(@RequestParam(value = "date",required = false) String date)
    {
        ResultInfo<News> ri = new ResultInfo<>();
        //判断参数是否有效
        if (date == null || "".equals(date))
        {
            ri.setEntity(null);
            ri.setMessage("请给出新闻日期");
            ri.setSuccess(false);
            return new ResponseEntity<ResultInfo<News>>(ri, HttpStatus.BAD_REQUEST);
        }

        boolean success = service.setNewsForAndroid(date);
        HttpStatus status;
        if (success)
        {
            ri.setEntity(null);
            ri.setMessage("设置成功");
            ri.setSuccess(true);
            status = HttpStatus.OK;
        }
        else
        {
            ri.setEntity(null);
            ri.setMessage("新闻不存在，设置失败");
            ri.setSuccess(false);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<ResultInfo<News>>(ri,status);
    }

    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    @GetMapping("/news/{news_date}")
    public void downloadNews(@PathVariable(value = "news_date") String date, HttpServletResponse response)
    {
        //因为下载按钮在列表项之后，所以这里默认一定存在该日期的新闻，不再做合法性判断
        News news = service.findNewsByDate(date);

        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        try {
            response.addHeader("Content-Disposition","inline;filename=" +
                    URLEncoder.encode(news.getTitle() + ".pdf","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            byte[] content = news.getContent();
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresRoles({"admin"})
    @PostMapping("/news")
    public ResponseEntity<ResultInfo<News>> addNews(@RequestParam(value = "date",required = false) String date,
                                                    @RequestParam(value = "title",required = false) String title,
                                                    @RequestParam(value = "news_file",required = false)MultipartFile file)
    {
        ResultInfo<News> ri = new ResultInfo<>();

        //判断参数有效性
        if (date == null || "".equals(date)
        || title == null || "".equals(title)
        || file == null || file.isEmpty())
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("提交的信息不完整，无法添加新闻");
            return new ResponseEntity<ResultInfo<News>>(ri,HttpStatus.BAD_REQUEST);
        }

        News news = new News();
        news.setNewsDate(date);
        news.setTitle(title);

        //将文件转为字节流
        byte[] data = toByte(file);
        HttpStatus status;

        if (data == null)
        {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        else
        {
            news.setContent(data);
            boolean success = service.addNews(news);

            //如果要添加的新闻先前不存在，则添加成功，返回成功结果
            if (success)
            {
                ri.setEntity(null);
                ri.setSuccess(true);
                ri.setMessage("添加新闻成功");
                status = HttpStatus.OK;
            }
            else {
                ri.setEntity(null);
                ri.setSuccess(false);
                ri.setMessage("该日期下的新闻已经存在，添加失败");
                status = HttpStatus.BAD_REQUEST;
            }
        }
        return new ResponseEntity<ResultInfo<News>>(ri,status);
    }

    @RequiresRoles({"admin"})
    @DeleteMapping("/news/{news_date}")
    public ResponseEntity<ResultInfo<News>> removeNews(@PathVariable(value = "news_date",
                                                required = false) String date)
    {
        ResultInfo<News> ri = new ResultInfo<>();
        if (date == null || "".equals(date))
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("请给出新闻日期");
            return new ResponseEntity<ResultInfo<News>>(ri,HttpStatus.BAD_REQUEST);
        }

        boolean success = service.deleteNews(date);

        //如果存在，则可以删除，返回成功结果
        HttpStatus status;
        if(success)
        {
            ri.setSuccess(true);
            ri.setMessage("删除新闻成功");
            status = HttpStatus.OK;
        }
        else
        {
            ri.setSuccess(false);
            ri.setMessage("新闻不存在，删除失败");
            status = HttpStatus.NOT_FOUND;
        }

        ri.setEntity(null);
        return new ResponseEntity<ResultInfo<News>>(ri,status);
    }

    @RequiresRoles({"admin"})
    @PutMapping("/news/{news_date}")
    public ResponseEntity<ResultInfo<News>> updateNews(@PathVariable(value = "news_date",required = false) String date,
                                                       @RequestParam(value = "title",required = false)String title,
                                                       @RequestParam(value = "news_file",required = false) MultipartFile file)
    {
        ResultInfo<News> ri = new ResultInfo<>();
        //参数有效性判断
        if(date == null || "".equals(date))
        {
            ri.setMessage("请给出新闻日期");
            ri.setSuccess(false);
            ri.setEntity(null);
            return new ResponseEntity<ResultInfo<News>>(ri,HttpStatus.BAD_REQUEST);
        }

        if (title == null || "".equals(title))
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("提交的信息不完整，无法更新新闻");
            return new ResponseEntity<ResultInfo<News>>(ri,HttpStatus.BAD_REQUEST);
        }

        //若上传了文件，则将文件转为字节流
        byte data[] = null;
        if(file != null && !file.isEmpty())
        {
            data = toByte(file);
        }

        News news = new News();
        news.setNewsDate(date);
        news.setContent(data);
        news.setTitle(title);

        boolean success = service.updateNews(news);
        HttpStatus status;
        if (success)
        {
            ri.setSuccess(true);
            ri.setMessage("新闻修改成功");
            status = HttpStatus.OK;
        }
        else {
            ri.setSuccess(false);
            ri.setMessage("要修改的新闻不存在，更新失败");
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<ResultInfo<News>>(ri,status);
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
