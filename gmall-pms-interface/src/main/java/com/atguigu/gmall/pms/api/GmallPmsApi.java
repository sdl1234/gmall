package com.atguigu.gmall.pms.api;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GmallPmsApi {


    /**
     * 信息
     */
    @GetMapping("pms/spu/{id}")
    public ResponseVo<PmsSpuEntity> queryPmsSpuById(@PathVariable("id") Long id);


    /**
     * 分页查询已上架的spu
     * @param pageParamVo 分页条件
     * @return
     */
    @PostMapping("pms/spu/page")
    public ResponseVo<List<PmsSpuEntity>> querySpusByPage(@RequestBody PageParamVo pageParamVo);


    /**
     * 查询spu的所有sku信息
     * @param spuId 商品基本信息Id
     * @return 商品销售数据集合
     */
    @GetMapping("pms/sku/spu/{spuId}")
    @ApiOperation("查询spu的所有sku信息")
    public ResponseVo<List<PmsSkuEntity>> queryInfo(
            @PathVariable("spuId")Long  spuId
    );


    /**
     * 信息
     */
    @GetMapping("pms/category/{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsCategoryEntity> queryPmsCategoryById(@PathVariable("id") Long id);



    /**
     * 信息
     */
    @GetMapping("pms/brand/{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsBrandEntity> queryPmsBrandById(@PathVariable("id") Long id);



    /**
     * 根据spuId查询检索属性及值
     * @param spuId 商品基本信息Id
     * @return
     */
    @ApiOperation("根据spuId查询检索属性及值")
    @GetMapping("pms/spuattrvalue/spu/{spuId}")
    public ResponseVo<List<PmsSpuAttrValueEntity>> querySearchAttrValueBySpuId(
            @PathVariable("spuId")Long spuId
    );



    /**
     * 根据skuId查询检索属性及值
     * @param skuId 商品销售属性Id
     * @return
     */
    @ApiOperation("根据skuId查询检索属性及值")
    @GetMapping("pms/skuattrvalue/sku/{skuId}")
    public ResponseVo<List<PmsSkuAttrValueEntity>> querySearchAttrValueBySkuId(
            @PathVariable("skuId")Long skuId
    );




















}
