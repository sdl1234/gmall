package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GmallSmsApi {

    @PostMapping("sms/skubounds/skusale/save")
    @ApiOperation("保存")
    public ResponseVo<Object> saveSkuSaleInfo(@RequestBody SmsSkuSaleVo smsSkuSaleVo);

}
