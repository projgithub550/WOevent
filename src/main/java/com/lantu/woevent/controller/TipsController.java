package com.lantu.woevent.controller;

import com.lantu.woevent.models.News;
import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.models.Tips;
import com.lantu.woevent.service.ITipsService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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

    @GetMapping("/tip_form")
    public String toTips()
    {
        //System.out.println("sss");
        return "tip_page";
    }


    @GetMapping("/adrd/tips")
    public ResponseEntity<ResultInfo<Tips>> getTip()
    {
        ResultInfo<Tips> ri= new ResultInfo<Tips>();

        Tips tip = service.findTipForAndroid();

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

    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
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

    @GetMapping("/tips/for_adrd")
    @RequiresRoles("admin")
    @RequiresPermissions("set")
    public ResponseEntity<ResultInfo<Tips>> setTipForAndroid(@RequestParam(value = "id",required = false) Integer id)
    {
        ResultInfo<Tips> ri = new ResultInfo<>();
        //判断参数是否有效
        if (id == null)
        {
            ri.setEntity(null);
            ri.setMessage("请给出科普知识ID");
            ri.setSuccess(false);
            return new ResponseEntity<ResultInfo<Tips>>(ri, HttpStatus.BAD_REQUEST);
        }

        boolean success = service.setTipForAndroid(id);
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
            ri.setMessage("科普不存在，设置失败");
            ri.setSuccess(false);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<ResultInfo<Tips>>(ri,status);
    }

    @RequiresRoles(value = {"admin","user"},logical = Logical.OR)
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

    @RequiresRoles("admin")
    @PostMapping("/tips")
    public ResponseEntity<ResultInfo<Tips>> addTip(@RequestParam(value = "title",required = false) String title,
                                                    @RequestParam(value = "tips_file",required = false) MultipartFile file)
    {
        ResultInfo<Tips> ri = new ResultInfo<Tips>();

        //判断参数有效性
        if (title == null || "".equals(title)
        || file == null || file.isEmpty())
        {
            ri.setEntity(null);
            ri.setSuccess(false);
            ri.setMessage("提交的信息不完整，无法添加科普知识");
            return new ResponseEntity<ResultInfo<Tips>>(ri,HttpStatus.BAD_REQUEST);
        }

        Tips tip = new Tips();
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
            service.addTip(tip);

            //如果要添加的科普知识先前不存在，则添加成功，返回成功结果
            ri.setEntity(null);
            ri.setSuccess(true);
            ri.setMessage("添加科普知识成功");
            status = HttpStatus.OK;
        }
        return new ResponseEntity<ResultInfo<Tips>>(ri,status);
    }

    @RequiresRoles("admin")
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

    @RequiresRoles("admin")
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
        tip.setTId(id);
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
        InputStream ins;
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
