package com.atguigu.gmall.cart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class GmallCartApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void contextLoads() {

        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps("l1");

        boundHashOps.put("ww","i");
        boundHashOps.put("w1","i1");
        boundHashOps.put("w2","i2");

//        System.out.println("boundHashOps.get(\"ww\") = " + boundHashOps.get("ww"));
//        System.out.println("boundHashOps.get(\"w1\") = " + boundHashOps.get("w1"));
//        System.out.println("boundHashOps.get(\"w2\") = " + boundHashOps.get("w2"));

        boundHashOps.values().stream().forEach(v ->{
            System.out.println("v = " + v);
        });


        System.out.println("----------------------------------------------------");

        boundHashOps.entries().forEach((k,v) ->{
            System.out.println(" "+ k + " + " + v);
        });
        System.out.println("----------------------------------------------------");

        boundHashOps.keys().forEach(v -> System.out.println(v));

        System.out.println("----------------------------------------------------");

        System.out.println(this.getClass().getName());

        System.out.println("Thread.currentThread().getStackTrace()[1].getMethodName() = " + Thread.currentThread().getStackTrace()[1].getMethodName());

        System.out.println("Thread.currentThread().getStackTrace()[1].getLineNumber() = " + Thread.currentThread().getStackTrace()[1].getLineNumber());

        System.out.println("Thread.currentThread().getStackTrace()[1].getFileName() = " + Thread.currentThread().getStackTrace()[1].getFileName());

    }

}
