package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.PmsSkuImagesEntity;
import com.atguigu.gmall.pms.service.PmsSkuImagesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * sku图片
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "sku图片 管理")
@RestController
@RequestMapping("pms/skuimages")
public class PmsSkuImagesController {

    @Autowired
    private PmsSkuImagesService pmsSkuImagesService;


    /**
     * 根据skuId 查询sku的图片信息
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public ResponseVo<List<PmsSkuImagesEntity>> querySkuImagesBySkuId(@PathVariable("skuId")Long skuId){
        List<PmsSkuImagesEntity> skuImagesEntities = this.pmsSkuImagesService.list(new QueryWrapper<PmsSkuImagesEntity>().eq("sku_id", skuId));
        return ResponseVo.ok(skuImagesEntities);
    }



    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsSkuImagesByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsSkuImagesService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSkuImagesEntity> queryPmsSkuImagesById(@PathVariable("id") Long id){
		PmsSkuImagesEntity pmsSkuImages = pmsSkuImagesService.getById(id);

        return ResponseVo.ok(pmsSkuImages);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsSkuImagesEntity pmsSkuImages){
		pmsSkuImagesService.save(pmsSkuImages);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsSkuImagesEntity pmsSkuImages){
		pmsSkuImagesService.updateById(pmsSkuImages);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsSkuImagesService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
