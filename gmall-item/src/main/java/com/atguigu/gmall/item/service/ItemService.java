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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallSmsClient smsClient;

    @Autowired
    private GmallWmsClient wmsClient;


    public ItemVo loadData(Long skuId) {

        ItemVo itemVo =new ItemVo();


//        1.根据skuId查询sku V
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
        itemVo.setDefaltImage(skuEntity.getDefaultImage());

//        2.根据三级分类Id查询一二三级分类 V
        ResponseVo<List<PmsCategoryEntity>> catesResponseVo = this.pmsClient.queryLv1123CategoriesByCid(skuEntity.getCategoryId());
        List<PmsCategoryEntity> categoryEntities = catesResponseVo.getData();
        itemVo.setCategories(categoryEntities);
//        3.根据品牌id查询品牌 V
        ResponseVo<PmsBrandEntity> brandEntityResponseVo = this.pmsClient.queryPmsBrandById(skuEntity.getBrandId());
        PmsBrandEntity brandEntity = brandEntityResponseVo.getData();
        if (brandEntity != null) {
            itemVo.setBrandId(brandEntity.getId());
            itemVo.setBrandName(brandEntity.getName());
        }
//        4.根据spuId查询SPU V
        ResponseVo<PmsSpuEntity> spuEntityResponseVo = this.pmsClient.queryPmsSpuById(skuEntity.getSpuId());
        PmsSpuEntity spuEntity = spuEntityResponseVo.getData();
        if (spuEntity != null) {
            itemVo.setSpuId(spuEntity.getId());
            itemVo.setSpuName(spuEntity.getName());
        }
//        5.根据skuId查询营销信息 V
        ResponseVo<List<ItemSaleVo>> salesResponseVo = this.smsClient.queryItemSalesBySkuId(skuId);
        List<ItemSaleVo> itemSaleVos = salesResponseVo.getData();
        itemVo.setSales(itemSaleVos);
//        6.根据skuId查询库存列表 V
        ResponseVo<List<WmsWareSkuEntity>> wareResponseVo = this.wmsClient.queryWmsWareSkuListBySkuId(skuId);
        List<WmsWareSkuEntity> wareSkuEntityList = wareResponseVo.getData();
        if (!CollectionUtils.isEmpty(wareSkuEntityList)){
            itemVo.setStore(wareSkuEntityList.stream().anyMatch(wmsWareSkuEntity -> wmsWareSkuEntity.getStock() - wmsWareSkuEntity.getStockLocked() > 0));
        }
//        7.根据skuId查询sku的图片列表  V
        ResponseVo<List<PmsSkuImagesEntity>> imagesResponseVo = this.pmsClient.querySkuImagesBySkuId(skuId);
        List<PmsSkuImagesEntity> skuImagesEntities = imagesResponseVo.getData();
        itemVo.setImages(skuImagesEntities);
//        8.根据spuId查询spu下所有销售属性的可取值 V
        ResponseVo<List<SaleAttrValueVo>> saleAttrResponseVo = this.pmsClient.querySaleAttrValuesBySpuId(skuEntity.getSpuId());
        List<SaleAttrValueVo> saleAttrValueVos = saleAttrResponseVo.getData();
        itemVo.setSaleAttrs(saleAttrValueVos);
//        9.根据skuId查询当前sku的销售属性 V
        ResponseVo<List<PmsSkuAttrValueEntity>> skuAttrResponseVo = this.pmsClient.querySkuAttrValuesBySkuId(skuId);
        List<PmsSkuAttrValueEntity> skuAttrValueEntities = skuAttrResponseVo.getData();
        if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
            skuAttrValueEntities.stream().collect(Collectors.toMap(PmsSkuAttrValueEntity::getAttrId,PmsSkuAttrValueEntity::getAttrValue));

        }

//        10.根据spuId所有销售属性组合和skuId的映射关系 V
        ResponseVo<String> stringResponseVo = this.pmsClient.queryMappingBySpuId(skuEntity.getSpuId());
        String json = stringResponseVo.getData();
        itemVo.setSkusJson(json);
//        11.根据spuId查询spu的描述信息 V
        ResponseVo<PmsSpuDescEntity> spuDescEntityResponseVo = this.pmsClient.queryPmsSpuDescById(skuId);
        PmsSpuDescEntity pmsSpuDescEntity = spuDescEntityResponseVo.getData();
        if (pmsSpuDescEntity != null) {
            itemVo.setSpuImages(Arrays.asList(StringUtils.split(pmsSpuDescEntity.getDecript(),",")));
        }
//        12.根据分类id、spuId、skuId查询出所有的规格参数组及组下的规格参数和值
        ResponseVo<List<ItemGroupVo>> groupResponseVo = this.pmsClient.queryGroupsWithAttrValuesByCidAndSpuIdAndSkuId(skuEntity.getCategoryId(), skuEntity.getSpuId(), skuId);
        List<ItemGroupVo> groupVos = groupResponseVo.getData();
        itemVo.setGroups(groupVos);


        return itemVo;

    }
}
