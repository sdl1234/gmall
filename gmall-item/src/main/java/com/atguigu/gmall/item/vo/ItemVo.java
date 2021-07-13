package com.atguigu.gmall.item.vo;

import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.atguigu.gmall.pms.entity.PmsSkuImagesEntity;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ItemVo {

    //面包屑(同一个表 多次查咨即可)
    private List<PmsCategoryEntity> categories;

    //品牌
    private Long brandId;
    private String brandName;

    //spu
    private Long spuId;
    private String spuName;

    //sku
    private Long skuId;
    private String title;
    private String subTitle;
    private BigDecimal price;
    private Integer weight;
    private String defaltImage;

    //sku图片
    private List<PmsSkuImagesEntity> images;

    //营销信息
    private List<ItemSaleVo> sales;

    //是否有货
    private Boolean store = false;

    //当前商品--商品品牌--商品attr属性
    private List<SaleAttrValueVo> saleAttrs;

    //当前商品的全部销售分类
    private Map<Long,String> saleAttr;

    //商品列表
    private String skusJson;

    //spu海报
    private List<String> spuImages;

    //规格参数以及值
    private List<ItemGroupVo> groups;



}















































