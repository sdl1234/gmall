package com.atguigu.gmall.pms.api;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GmallPmsApi {


    /**
     * 分类列表
     * @param parentId 父节点Id -1:查询所有，0：查询一级节点
     * @return  节点集合
     */
    @GetMapping("pms/category/parent/{parentId}")
    public ResponseVo<List<PmsCategoryEntity>> queryCategory(
            @PathVariable("parentId") Long parentId
    );


    /**
     * 查询首页二级分类
     * @param pid
     * @return
     */
    @GetMapping("pms/category/subs/{pid}")
    public ResponseVo<List<PmsCategoryEntity>> queryLv2WithSubsByPid(@PathVariable("pid")Long pid);



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
            @PathVariable("spuId")Long  spuId);

    /**
     * 根据 skuId查询sku信息
     * @param id
     * @return
     */
    @GetMapping("pms/sku/{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSkuEntity> queryPmsSkuById(@PathVariable("id") Long id);

    /**
     * 销售信息
     */
    @GetMapping("pms/category/{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsCategoryEntity> queryPmsCategoryById(@PathVariable("id") Long id);

    /**
     * 根据三级分类Id查询123级分类
     * @param cid
     * @return
     */
    @GetMapping("pms/category/sub/{cid3}")
    public ResponseVo<List<PmsCategoryEntity>> queryLv1123CategoriesByCid(
            @PathVariable("cid3") Long cid);

    /**
     * 品牌信息
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
            @PathVariable("spuId")Long spuId);




    /**
     * 根据skuId查询检索属性及值
     * @param skuId 商品销售属性Id
     * @return
     */
    @ApiOperation("根据skuId查询检索属性及值")
    @GetMapping("pms/skuattrvalue/sku/{skuId}")
    public ResponseVo<List<PmsSkuAttrValueEntity>> querySearchAttrValueBySkuId(
            @PathVariable("skuId")Long skuId);

    /**
     * 根据spuId查询spu下所有销售属性的可取值
     * @param spuId
     * @return
     */
    @GetMapping("pms/skuattrvalue/spu/{spuId}")
    public ResponseVo<List<SaleAttrValueVo>> querySaleAttrValuesBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据skuId查询当前sku的销售属性
     * @param skuId
     * @return
     */
    @GetMapping("pms/skuattrvalue/sku2/{skuId}")
    public ResponseVo<List<PmsSkuAttrValueEntity>> querySkuAttrValuesBySkuId(@PathVariable("skuId")Long skuId);

    /**
     * 根据spuId得到销售属性组合和skuId的映射关系
     * @param spuId
     * @return
     */
    @GetMapping("pms/skuattrvalue/mapping/{spuId}")
    public ResponseVo<String> queryMappingBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据skuId 查询sku的图片信息
     * @param skuId
     * @return
     */
    @GetMapping("pms/skuimages/sku/{skuId}")
    public ResponseVo<List<PmsSkuImagesEntity>> querySkuImagesBySkuId(@PathVariable("skuId")Long skuId);





    /**
     * 根据 cid spuId skuId 查询商品详情标题，以及商品详情数据
     * @param cid
     * @param spuId
     * @param skuId
     * @return
     */
    @GetMapping("pms/attrgroup/with/attr/value/{cid}")
    public ResponseVo<List<ItemGroupVo>> queryGroupsWithAttrValuesByCidAndSpuIdAndSkuId(
            @PathVariable("cid")Long cid,
            @RequestParam("spuId")Long spuId,
            @RequestParam("skuId")Long skuId
    );




    /**
     * 根据spuId查询spu的描述信息
     */
    @GetMapping("pms/spudesc/{spuId}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSpuDescEntity> queryPmsSpuDescById(@PathVariable("spuId") Long spuId);







}
