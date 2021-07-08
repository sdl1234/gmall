package com.atguigu.gmall.pms.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PmsCategoryMapperTest {
    @Autowired
    private PmsCategoryMapper pmsCategoryMapper;


    @Test
    void queryLv2WithSubsByPid() {
        this.pmsCategoryMapper.queryLv2WithSubsByPid(1L).forEach(System.out::println);
    }
}