package com.atguigu.gmall.pms.feign;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.api.GmallSmsApi;
import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {

//    @PostMapping("sms/skubounds/skusale/save")
//    @ApiOperation("保存")
//    public ResponseVo<Object> saveSkuSaleInfo(@RequestBody SmsSkuSaleVo smsSkuSaleVo);


}
