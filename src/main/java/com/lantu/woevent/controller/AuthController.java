package com.lantu.woevent.controller;

import com.lantu.woevent.models.dto.LoginDTO;
import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.models.auth.User;
import com.lantu.woevent.service.IAuthService;
import com.lantu.woevent.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController
{
    @Autowired
    private IUserService uService;

    @Autowired
    private IAuthService aService;

    @GetMapping("/l_form")
    public String toLogin()
    {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<ResultInfo<String>> login(@Validated LoginDTO loginDTO,
                                              BindingResult bindingResult)
    {
        //进行错误处理
        ResultInfo<String> ri = new ResultInfo<>();
        if (bindingResult.hasErrors())
        {
            ri.setSuccess(false);
            ri.setMessage(bindingResult.getFieldError().getDefaultMessage());
            ri.setEntity(null);
            return new ResponseEntity<>(ri, HttpStatus.BAD_REQUEST);
        }

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User user = uService.findUserByUsername(username);
        HttpStatus status;

        //若用户不存在或密码不匹配
        if (user == null || !user.getPassword().equals(password))
        {
            ri.setSuccess(false);
            ri.setMessage("用户名或密码有误");
            ri.setEntity(null);
            status = HttpStatus.BAD_REQUEST;
        }
        else
        {
            //登录成功则返回一个token
            ri.setSuccess(true);
            ri.setMessage("登录成功");
            String token = aService.createToken(user.getUId());
            ri.setEntity(token);
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(ri,status);
    }
}
