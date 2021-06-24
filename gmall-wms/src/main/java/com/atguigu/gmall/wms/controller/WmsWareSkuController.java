package com.atguigu.gmall.wms.controller;

import java.util.List;

import com.atguigu.gmall.wms.entity.WmsWareEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;
import com.atguigu.gmall.wms.service.WmsWareSkuService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 商品库存
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 18:00:09
 */
@Api(tags = "商品库存 管理")
@RestController
@RequestMapping("wms/waresku")
public class WmsWareSkuController {

    @Autowired
    private WmsWareSkuService wmsWareSkuService;

    @Autowired
    private WmsWareEntity wmsWareEntity;

    /**
     * 获取某个sku的库存信息
     * @param skuId 商品销售信息Id
     * @return 商品库存
     */
    @GetMapping("sku/{skuId}")
    @ApiOperation(" 获取某个sku的库存信息")
    public ResponseVo<List<WmsWareSkuEntity>> queryWmsWareSkuListBySkuId(
            @PathVariable("skuId")Long skuId
    ){
        List<WmsWareSkuEntity> wmsWareSkuEntityList =wmsWareSkuService.list(
                new QueryWrapper<WmsWareSkuEntity>().eq("sku_id",skuId)
        );
        return ResponseVo.ok(wmsWareSkuEntityList);
    }



    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryWmsWareSkuByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = wmsWareSkuService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<WmsWareSkuEntity> queryWmsWareSkuById(@PathVariable("id") Long id){
		WmsWareSkuEntity wmsWareSku = wmsWareSkuService.getById(id);

        return ResponseVo.ok(wmsWareSku);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody WmsWareSkuEntity wmsWareSku){
		wmsWareSkuService.save(wmsWareSku);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody WmsWareSkuEntity wmsWareSku){
		wmsWareSkuService.updateById(wmsWareSku);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		wmsWareSkuService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
