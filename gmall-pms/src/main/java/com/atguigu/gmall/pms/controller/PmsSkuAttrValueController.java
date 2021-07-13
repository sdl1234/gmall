package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.service.PmsSkuAttrValueService;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "sku销售属性&值 管理")
@RestController
@RequestMapping("pms/skuattrvalue")
public class PmsSkuAttrValueController {

    @Autowired
    private PmsSkuAttrValueService pmsSkuAttrValueService;


    /**
     * 根据分类id，skuId
     * @param cid
     * @param skuId
     * @return
     */
    @GetMapping("search/{cid}")
    public ResponseVo<List<PmsSkuAttrValueEntity>> querySearchAttrValuesBySkuId(
            @PathVariable("cid")Long cid,
            @RequestParam("skuId") Long skuId
    ){
         List<PmsSkuAttrValueEntity> skuAttrValueEntities =this.pmsSkuAttrValueService.querySearchAttrValuesBySkuId(cid,skuId);
         return ResponseVo.ok(skuAttrValueEntities);
    }



    /**
     * 根据spuId得到销售属性组合和skuId的映射关系
     * @param spuId
     * @return
     */
    @GetMapping("mapping/{spuId}")
    public ResponseVo<String> queryMappingBySpuId(@PathVariable("spuId")Long spuId){
         String json =this.pmsSkuAttrValueService.queryMappingBySpuId(spuId);
        return ResponseVo.ok(json);
    }



    /**
     * 根据skuId查询当前sku的销售属性
     * @param skuId
     * @return
     */
    @GetMapping("sku2/{skuId}")
    public ResponseVo<List<PmsSkuAttrValueEntity>> querySkuAttrValuesBySkuId(@PathVariable("skuId")Long skuId){
        List<PmsSkuAttrValueEntity> skuAttrValueEntities = this.pmsSkuAttrValueService.list(new QueryWrapper<PmsSkuAttrValueEntity>().eq("sku_id", skuId));
        return ResponseVo.ok(skuAttrValueEntities);

    }


    /**
     * 根据spuId查询spu下所有销售属性的可取值
     * @param spuId
     * @return
     */
    @GetMapping("spu/{spuId}")
    public ResponseVo<List<SaleAttrValueVo>> querySaleAttrValuesBySpuId(@PathVariable("spuId")Long spuId){
        List<SaleAttrValueVo> saleAttrValueVos =this.pmsSkuAttrValueService.querySaleAttrValuesBySpuId(spuId);
        return ResponseVo.ok(saleAttrValueVos);
    }




    /**
     * 根据skuId查询检索属性及值
     * @param skuId 商品销售属性Id
     * @return
     */
    @ApiOperation("根据skuId查询检索属性及值")
    @GetMapping("sku/{skuId}")
    public ResponseVo<List<PmsSkuAttrValueEntity>> querySearchAttrValueBySkuId(
            @PathVariable("skuId")Long skuId
    ){
        List<PmsSkuAttrValueEntity> attrValueEntities=
                pmsSkuAttrValueService.querySearchAttrValueBySkuId(skuId);
        return ResponseVo.ok(attrValueEntities);
    }



    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsSkuAttrValueByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsSkuAttrValueService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSkuAttrValueEntity> queryPmsSkuAttrValueById(@PathVariable("id") Long id){
		PmsSkuAttrValueEntity pmsSkuAttrValue = pmsSkuAttrValueService.getById(id);

        return ResponseVo.ok(pmsSkuAttrValue);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsSkuAttrValueEntity pmsSkuAttrValue){
		pmsSkuAttrValueService.save(pmsSkuAttrValue);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsSkuAttrValueEntity pmsSkuAttrValue){
		pmsSkuAttrValueService.updateById(pmsSkuAttrValue);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsSkuAttrValueService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
