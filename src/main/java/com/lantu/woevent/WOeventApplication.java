package com.lantu.woevent;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan({"com.gitee.sunchenbin.mybatis.actable.dao.*","com.lantu.woevent.mapper"} )
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*","com.lantu.woevent"})
@SpringBootApplication
public class WOeventApplication {

    public static void main(String[] args)
    {
//        Subject s = SecurityUtils.getSubject();
//        s.login();
        SpringApplication.run(WOeventApplication.class, args);
    }

}
