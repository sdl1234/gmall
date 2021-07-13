package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.pms.service.PmsSkuAttrValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PmsSkuAttrValueControllerTest {

    @Autowired
    private PmsSkuAttrValueService pmsSkuAttrValueService;

    @Test
    void queryMappingBySpuId() {
        String s = this.pmsSkuAttrValueService.queryMappingBySpuId(17L);
        System.out.println(s);
    }
}