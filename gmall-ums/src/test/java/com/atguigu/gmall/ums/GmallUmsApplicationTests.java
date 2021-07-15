package com.atguigu.gmall.ums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class GmallUmsApplicationTests {

    @Test
    void contextLoads() {

/*        Date datTime= new Date();
        SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sf.format(datTime));*/

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));


    }

}
