package com.atguigu.springboot;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot01HelloworldQuickApplicationTests {

    @Test
    public void contextLoads() {
    	System.out.println("SpringBootTest");
        //盐值用的用的是对用户名的加密（测试用的"sw"）
        ByteSource credentialsSalt01 = ByteSource.Util.bytes("sw");
//        Object salt = null;//盐值
        Object credential = "123456";//密码
        String hashAlgorithmName = "MD5";//加密方式
        //1024指的是加密的次数
        Object simpleHash = new SimpleHash(hashAlgorithmName, credential,
                credentialsSalt01, 1024);
        System.out.println("加密后的值----->" + simpleHash);
    }

}
