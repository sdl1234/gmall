package com.atguigu.gmall.search;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.api.GmallPmsApi;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.fegin.GmallPmsClient;
import com.atguigu.gmall.search.fegin.GmallWmsClient;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchAttrValue;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.wms.api.GmallWmsApi;
import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    void contextLoads() {

        //创建索引及映射
        this.restTemplate.createIndex(Goods.class);
        this.restTemplate.putMapping(Goods.class);

        //定义假分页数据
        Integer pageNum =1;
        Integer pageSize =100;

        do {

            //分页查询
            PageParamVo pageParamVo =new PageParamVo();
            pageParamVo.setPageNum(pageNum);
            pageParamVo.setPageSize(pageSize);
            ResponseVo<List<PmsSpuEntity>> spuResp = this.pmsClient.querySpusByPage(pageParamVo);
            List<PmsSpuEntity> spus = spuResp.getData();

            //遍历spu 查询sku
            for (PmsSpuEntity pmsSpuEntity : spus) {
                ResponseVo<List<PmsSkuEntity>> skuResp = this.pmsClient.queryInfo(pmsSpuEntity.getId());
                List<PmsSkuEntity> skuEntities = skuResp.getData();

                if (!CollectionUtils.isEmpty(skuEntities)) {
                    //将sku对象转换成goods对象
                    List<Goods> goodsList =  skuEntities.stream().map(pmsSkuEntity -> {
                        Goods goods = new Goods();

                        //获取spu的索引属性及值
                        ResponseVo<List<PmsSpuAttrValueEntity>> arrtValueResp =
                                this.pmsClient.querySearchAttrValueBySpuId(pmsSpuEntity.getId());
                        List<PmsSpuAttrValueEntity> attrValueEntities = arrtValueResp.getData();

                        List<SearchAttrValue> searchAttrValues =new ArrayList<>();

                        if (!CollectionUtils.isEmpty(attrValueEntities)) {
                            searchAttrValues = attrValueEntities.stream().map(pmsSpuAttrValueEntity -> {
                                SearchAttrValue searchAttrValue =new SearchAttrValue();
                                searchAttrValue.setAttrId(pmsSpuAttrValueEntity.getAttrId());
                                searchAttrValue.setAttrName(pmsSpuAttrValueEntity.getAttrName());
                                searchAttrValue.setAttrValue(pmsSpuAttrValueEntity.getAttrValue());
                                return searchAttrValue;
                            }).collect(Collectors.toList());
                        }

                        //查询sku搜索属性及值
                        ResponseVo<List<PmsSkuAttrValueEntity>> skuAttrValueResp =
                                this.pmsClient.querySearchAttrValueBySkuId(pmsSpuEntity.getId());
                        List<PmsSkuAttrValueEntity> skuAttrValueEntities = skuAttrValueResp.getData();
                        List<SearchAttrValue> searchSkuAttrValues= new ArrayList<>();
                        if (!CollectionUtils.isEmpty(skuAttrValueEntities)){
                            searchSkuAttrValues = skuAttrValueEntities.stream().map(pmsSkuAttrValueEntity -> {
                                SearchAttrValue searchAttrValue=new SearchAttrValue();
                                searchAttrValue.setAttrId(pmsSkuAttrValueEntity.getAttrId());
                                searchAttrValue.setAttrName(pmsSkuAttrValueEntity.getAttrName());
                                searchAttrValue.setAttrValue(pmsSkuAttrValueEntity.getAttrValue());
                                return searchAttrValue;
                            }).collect(Collectors.toList());
                        }
                        searchAttrValues.addAll(searchSkuAttrValues);
                        goods.setSearchAttrs(searchAttrValues);

                        //查询品牌
                        ResponseVo<PmsBrandEntity> brandEntityResp=
                                this.pmsClient.queryPmsBrandById(pmsSkuEntity.getId());
                        PmsBrandEntity brandEntity = brandEntityResp.getData();
                        if (brandEntity != null){
                            goods.setBrandId(pmsSkuEntity.getBrandId());
                            goods.setBrandName(pmsSkuEntity.getName());
                            goods.setLogo(brandEntity.getLogo());
                        }

                        //查询分类
                        ResponseVo<PmsCategoryEntity> categoryEntityResp =
                                this.pmsClient.queryPmsCategoryById(pmsSkuEntity.getId());
                        PmsCategoryEntity categoryEntity = categoryEntityResp.getData();
                        if (categoryEntity != null){
                            goods.setCategoryId(pmsSkuEntity.getCategoryId());
                            goods.setCategoryName(categoryEntity.getName());
                        }

                        goods.setCreatTime(pmsSpuEntity.getCreateTime());
                        goods.setDefaultImage(pmsSkuEntity.getDefaultImage());
                        goods.setSubTitle(pmsSkuEntity.getSubtitle());
                        goods.setPrice(pmsSkuEntity.getPrice().doubleValue());
                        goods.setSales(0L);
                        goods.setSkuId(pmsSkuEntity.getId());

                        //查询库存信息
                        ResponseVo<List<WmsWareSkuEntity>> listResp =
                                this.wmsClient.queryWmsWareSkuListBySkuId(pmsSkuEntity.getId());
                        List<WmsWareSkuEntity> wareSkuEntities = listResp.getData();
                        if (!CollectionUtils.isEmpty(wareSkuEntities)){
                            boolean flag = wareSkuEntities.stream().anyMatch(wmsWareSkuEntity -> wmsWareSkuEntity.getStock() >0);
                            goods.setStore(flag);
                        }
                        goods.setTitle(pmsSkuEntity.getTitle());
                        return goods;
                    }).collect(Collectors.toList());


                    //导入索引库
                    this.goodsRepository.saveAll(goodsList);
                }
            }
            pageSize =spus.size();
            pageNum++;

        }while (pageSize == 100);



    }

}
