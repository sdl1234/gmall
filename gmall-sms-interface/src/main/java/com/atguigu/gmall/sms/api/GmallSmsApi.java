package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GmallSmsApi {

    @PostMapping("sms/skubounds/skusale/save")
    @ApiOperation("保存")
    public ResponseVo<Object> saveSkuSaleInfo(@RequestBody SmsSkuSaleVo smsSkuSaleVo);

    /**
     * 根据skuId 查询营销信息
     * @param skuId
     * @return
     */
    @GetMapping("sms/skubounds/sales/{skuId}")
    public ResponseVo<List<ItemSaleVo>> queryItemSalesBySkuId(@PathVariable("skuId") Long skuId);


}
