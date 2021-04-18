package com.atguigu.crowd.test;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations = "classpath*:spring*.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StringTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testMd5()
    {
        String source = "123456";
        String encode = passwordEncoder.encode(source);
        System.err.println(encode);
    }
}
