package com.atguigu.gmall.sms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.entity.SmsSkuBoundsEntity;
import com.atguigu.gmall.sms.service.SmsSkuBoundsService;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品spu积分设置
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
@Api(tags = "商品spu积分设置 管理")
@RestController
@RequestMapping("sms/skubounds")
public class SmsSkuBoundsController {

    @Autowired
    private SmsSkuBoundsService smsSkuBoundsService;


    /**
     * 根据skuId 查询营销信息
     * @param skuId
     * @return
     */
    @GetMapping("sales/{skuId}")
    public ResponseVo<List<ItemSaleVo>> queryItemSalesBySkuId(@PathVariable("skuId") Long skuId) {
        List<ItemSaleVo> itemSaleVos = this.smsSkuBoundsService.queryItemSalesBySkuId(skuId);
        return ResponseVo.ok(itemSaleVos);
    }


    @PostMapping("/skusale/save")
    @ApiOperation("保存")
    public ResponseVo<Object> saveSkuSaleInfo(@RequestBody SmsSkuSaleVo smsSkuSaleVo) {
        smsSkuBoundsService.saveSkuSaleInfo(smsSkuSaleVo);

        return ResponseVo.ok(null);
    }


    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> querySmsSkuBoundsByPage(PageParamVo paramVo) {
        PageResultVo pageResultVo = smsSkuBoundsService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<SmsSkuBoundsEntity> querySmsSkuBoundsById(@PathVariable("id") Long id) {
        SmsSkuBoundsEntity smsSkuBounds = smsSkuBoundsService.getById(id);

        return ResponseVo.ok(smsSkuBounds);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody SmsSkuBoundsEntity smsSkuBounds) {
        smsSkuBoundsService.save(smsSkuBounds);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody SmsSkuBoundsEntity smsSkuBounds) {
        smsSkuBoundsService.updateById(smsSkuBounds);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids) {
        smsSkuBoundsService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
