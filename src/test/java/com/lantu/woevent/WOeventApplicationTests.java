package com.lantu.woevent;

import com.lantu.woevent.mapper.IUserMapper;
import com.lantu.woevent.models.auth.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class WOeventApplicationTests {

    @Autowired
    private IUserMapper mapper;

    @Test
    void contextLoads()
    {
        List<Role> roles = mapper.findRolesByUserId(1);
        System.out.println(roles);
//        System.out.println(UUID.randomUUID().toString());
    }

}
