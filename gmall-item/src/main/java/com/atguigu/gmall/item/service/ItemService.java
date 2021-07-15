package com.atguigu.gmall.item.service;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.exception.ItemException;
import com.atguigu.gmall.item.fegin.GmallPmsClient;
import com.atguigu.gmall.item.fegin.GmallSmsClient;
import com.atguigu.gmall.item.fegin.GmallWmsClient;
import com.atguigu.gmall.item.vo.ItemVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallSmsClient smsClient;

    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private TemplateEngine templateEngine;

    //静态页面生成
    public void creatHtml(ItemVo itemVo){

        Context context =new Context();
        context.setVariable("itemVo",itemVo);

        try (PrintWriter printWriter =new PrintWriter("D:\\atguigu\\class\\html\\"+ itemVo.getSkuId() + ".html")) {
            templateEngine.process("item",context,printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //创建线程 异步执行 静态页面方法
    public void asynExecute(ItemVo itemVo){
        threadPoolExecutor.execute(() ->{
            this.creatHtml(itemVo);
        });
    }






    public ItemVo loadData(Long skuId) {

        ItemVo itemVo =new ItemVo();


//        1.根据skuId查询sku V
        CompletableFuture<PmsSkuEntity> skuFuture = CompletableFuture.supplyAsync(() ->{

            ResponseVo<PmsSkuEntity> pmsSkuEntityResponseVo = this.pmsClient.queryPmsSkuById(skuId);
            PmsSkuEntity skuEntity = pmsSkuEntityResponseVo.getData();
            if (skuEntity == null) {
                throw new ItemException("没有此商品！");
            }
            itemVo.setSkuId(skuEntity.getId());
            itemVo.setTitle(skuEntity.getTitle());
            itemVo.setSubTitle(skuEntity.getSubtitle());
            itemVo.setPrice(skuEntity.getPrice());
            itemVo.setWeight(skuEntity.getWeight());
            itemVo.setDefaultImage(skuEntity.getDefaultImage());
            return skuEntity;
        },threadPoolExecutor);

//        2.根据三级分类Id查询一二三级分类 V
        CompletableFuture<Void> catesFuture  = skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<List<PmsCategoryEntity>> catesResponseVo = this.pmsClient.queryLv1123CategoriesByCid(pmsSkuEntity.getCategoryId());
            List<PmsCategoryEntity> categoryEntities = catesResponseVo.getData();
            itemVo.setCategories(categoryEntities);
        },threadPoolExecutor);
//        3.根据品牌id查询品牌 V
        CompletableFuture<Void> brandFuture = skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<PmsBrandEntity> brandEntityResponseVo = this.pmsClient.queryPmsBrandById(pmsSkuEntity.getBrandId());
            PmsBrandEntity brandEntity = brandEntityResponseVo.getData();
            if (brandEntity != null) {
                itemVo.setBrandId(brandEntity.getId());
                itemVo.setBrandName(brandEntity.getName());
            }
        },threadPoolExecutor);
//        4.根据spuId查询SPU V
        CompletableFuture<Void> spuFuture = skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<PmsSpuEntity> spuEntityResponseVo = this.pmsClient.queryPmsSpuById(pmsSkuEntity.getSpuId());
            PmsSpuEntity spuEntity = spuEntityResponseVo.getData();
            if (spuEntity != null) {
                itemVo.setSpuId(spuEntity.getId());
                itemVo.setSpuName(spuEntity.getName());
            }
        },threadPoolExecutor);
//        5.根据skuId查询营销信息 V
        CompletableFuture<Void> salesFuture = CompletableFuture.runAsync(() -> {
            ResponseVo<List<ItemSaleVo>> salesResponseVo = this.smsClient.queryItemSalesBySkuId(skuId);
            List<ItemSaleVo> itemSaleVos = salesResponseVo.getData();
            itemVo.setSales(itemSaleVos);
        },threadPoolExecutor);
//        6.根据skuId查询库存列表 V
        CompletableFuture<Void>  wareFuture = CompletableFuture.runAsync(() -> {
            ResponseVo<List<WmsWareSkuEntity>> wareResponseVo = this.wmsClient.queryWmsWareSkuListBySkuId(skuId);
            List<WmsWareSkuEntity> wareSkuEntityList = wareResponseVo.getData();
            if (!CollectionUtils.isEmpty(wareSkuEntityList)){
                itemVo.setStore(wareSkuEntityList.stream().anyMatch(wmsWareSkuEntity -> wmsWareSkuEntity.getStock() - wmsWareSkuEntity.getStockLocked() > 0));
            }
        },threadPoolExecutor);
//        7.根据skuId查询sku的图片列表  V
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() ->{
            ResponseVo<List<PmsSkuImagesEntity>> imagesResponseVo = this.pmsClient.querySkuImagesBySkuId(skuId);
            List<PmsSkuImagesEntity> skuImagesEntities = imagesResponseVo.getData();
            itemVo.setImages(skuImagesEntities);
        },threadPoolExecutor);
//        8.根据spuId查询spu下所有销售属性的可取值 V
        CompletableFuture<Void> spuSalesFuture = skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<List<SaleAttrValueVo>> saleAttrResponseVo = this.pmsClient.querySaleAttrValuesBySpuId(pmsSkuEntity.getSpuId());
            List<SaleAttrValueVo> saleAttrValueVos = saleAttrResponseVo.getData();
            itemVo.setSaleAttrs(saleAttrValueVos);
        },threadPoolExecutor);
//        9.根据skuId查询当前sku的销售属性 V
        CompletableFuture<Void> skuSalesFuture = CompletableFuture.runAsync(() ->{
            ResponseVo<List<PmsSkuAttrValueEntity>> skuAttrResponseVo = this.pmsClient.querySkuAttrValuesBySkuId(skuId);
            List<PmsSkuAttrValueEntity> skuAttrValueEntities = skuAttrResponseVo.getData();
            if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
               itemVo.setSaleAttr( skuAttrValueEntities.stream().collect(Collectors.toMap(PmsSkuAttrValueEntity::getAttrId,PmsSkuAttrValueEntity::getAttrValue)));
            }
        },threadPoolExecutor);

//        10.根据spuId所有销售属性组合和skuId的映射关系 V
        CompletableFuture<Void> mappingFuture =skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<String> stringResponseVo = this.pmsClient.queryMappingBySpuId(pmsSkuEntity.getSpuId());
            String json = stringResponseVo.getData();
            itemVo.setSkuJsons(json);
        },threadPoolExecutor);
//        11.根据spuId查询spu的描述信息 V
        CompletableFuture<Void> descFuture = skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<PmsSpuDescEntity> spuDescEntityResponseVo = this.pmsClient.queryPmsSpuDescById(skuId);
            PmsSpuDescEntity pmsSpuDescEntity = spuDescEntityResponseVo.getData();
            if (pmsSpuDescEntity != null) {
                itemVo.setSpuImages(Arrays.asList(StringUtils.split(pmsSpuDescEntity.getDecript(),",")));
            }
        },threadPoolExecutor);
//        12.根据分类id、spuId、skuId查询出所有的规格参数组及组下的规格参数和值
        CompletableFuture<Void> groupFuture = skuFuture.thenAcceptAsync(pmsSkuEntity -> {
            ResponseVo<List<ItemGroupVo>> groupResponseVo = this.pmsClient.queryGroupsWithAttrValuesByCidAndSpuIdAndSkuId(pmsSkuEntity.getCategoryId(), pmsSkuEntity.getSpuId(), skuId);
            List<ItemGroupVo> groupVos = groupResponseVo.getData();
            itemVo.setGroups(groupVos);
        },threadPoolExecutor);


        //创建所有线程完成 条件
        CompletableFuture.allOf(catesFuture, brandFuture, spuFuture, salesFuture, wareFuture, imagesFuture,spuSalesFuture, skuSalesFuture, mappingFuture, descFuture, groupFuture).join();
        //返回数据集合
        return itemVo;

    }
}
