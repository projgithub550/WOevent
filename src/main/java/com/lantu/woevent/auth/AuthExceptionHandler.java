package com.lantu.woevent.auth;

import com.lantu.woevent.models.ResultInfo;
import com.lantu.woevent.models.auth.User;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler
{
    @ExceptionHandler(ExpiredCredentialsException.class)
    public ResponseEntity<ResultInfo<User>> handleExpired(ExpiredCredentialsException e)
    {
        ResultInfo<User> ri = new ResultInfo<>();
        ri.setEntity(null);
        ri.setMessage("请重新登录");
        ri.setSuccess(false);
        return  new ResponseEntity<ResultInfo<User>>(ri, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownAccountException.class)
    public ResponseEntity<ResultInfo<User>> handleUnknownAccount(UnknownAccountException e)
    {
        ResultInfo<User> ri = new ResultInfo<>();
        ri.setEntity(null);
        ri.setMessage("用户不存在");
        ri.setSuccess(false);
        return  new ResponseEntity<ResultInfo<User>>(ri, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<ResultInfo<User>> handleException(AuthorizationException e) {

        ResultInfo<User> ri = new ResultInfo<>();
        ri.setEntity(null);

        ri.setSuccess(false);

        //获取错误中中括号的内容
        String message = e.getMessage();
        String msg=message.substring(message.indexOf("[")+1,message.indexOf("]"));
        //判断是角色错误还是权限错误
        String error;
        if (message.contains("role")) {
           error = " 对不起，您没有" + msg + "角色";
        } else if (message.contains("permission")) {
            error = "对不起，您没有" + msg + "权限";
        } else {
            error = "对不起，您的权限有误";
        }

        ri.setMessage(error);
        return new ResponseEntity<>(ri,HttpStatus.BAD_REQUEST);
    }

}
