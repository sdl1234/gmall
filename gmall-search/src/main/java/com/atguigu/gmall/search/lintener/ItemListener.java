package com.atguigu.gmall.search.lintener;


import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.fegin.GmallPmsClient;
import com.atguigu.gmall.search.fegin.GmallWmsClient;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchAttrValue;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ItemListener {


    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("SEARCH_ITEM_QUEUE"),
            exchange = @Exchange(value = "PMS_ITEM_EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.insert"}
    ))
    public void listener(Long spuId, Channel channel, Message message) throws IOException {
        //判断是否为垃圾消息
        if (spuId == null) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        //根据spuId 查询spu

            ResponseVo<PmsSpuEntity> pmsSpuEntityResponseVo = this.pmsClient.queryPmsSpuById(spuId);
            PmsSpuEntity pmsSpuEntity = pmsSpuEntityResponseVo.getData();
            //判断获取到的spu是否为空
            if (pmsSpuEntity == null) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            //遍历spu 查询sku
            ResponseVo<List<PmsSkuEntity>> skuResp = this.pmsClient.queryInfo(spuId);
            List<PmsSkuEntity> skuEntities = skuResp.getData();

            if (!CollectionUtils.isEmpty(skuEntities)) {
                //将sku对象转换成goods对象
                List<Goods> goodsList = skuEntities.stream().map(pmsSkuEntity -> {
                    Goods goods = new Goods();

                    //获取spu的索引属性及值
                    ResponseVo<List<PmsSpuAttrValueEntity>> attrValueResp =
                            this.pmsClient.querySearchAttrValueBySpuId(pmsSpuEntity.getId());
                    List<PmsSpuAttrValueEntity> attrValueEntities = attrValueResp.getData();

                    List<SearchAttrValue> searchAttrValues = new ArrayList<>();

                    if (!CollectionUtils.isEmpty(attrValueEntities)) {
                        searchAttrValues = attrValueEntities.stream().map(pmsSpuAttrValueEntity -> {
                            SearchAttrValue searchAttrValue = new SearchAttrValue();
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
                    List<SearchAttrValue> searchSkuAttrValues = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
                        searchSkuAttrValues = skuAttrValueEntities.stream().map(pmsSkuAttrValueEntity -> {
                            SearchAttrValue searchAttrValue = new SearchAttrValue();
                            searchAttrValue.setAttrId(pmsSkuAttrValueEntity.getAttrId());
                            searchAttrValue.setAttrName(pmsSkuAttrValueEntity.getAttrName());
                            searchAttrValue.setAttrValue(pmsSkuAttrValueEntity.getAttrValue());
                            return searchAttrValue;
                        }).collect(Collectors.toList());
                    }
                    searchAttrValues.addAll(searchSkuAttrValues);
                    goods.setSearchAttrs(searchAttrValues);

                    //查询品牌
                    ResponseVo<PmsBrandEntity> brandEntityResp =
                            this.pmsClient.queryPmsBrandById(pmsSkuEntity.getBrandId());
                    PmsBrandEntity brandEntity = brandEntityResp.getData();
                    if (brandEntity != null) {
                        goods.setBrandId(brandEntity.getId());
                        goods.setBrandName(brandEntity.getName());
                        goods.setLogo(brandEntity.getLogo());
                    }

                    //查询分类
                    ResponseVo<PmsCategoryEntity> categoryEntityResp =
                            this.pmsClient.queryPmsCategoryById(pmsSkuEntity.getCategoryId());
                    PmsCategoryEntity categoryEntity = categoryEntityResp.getData();
                    if (categoryEntity != null) {
                        goods.setCategoryId(categoryEntity.getId());
                        goods.setCategoryName(categoryEntity.getName());
                    }

                    goods.setCreateTime(pmsSpuEntity.getCreateTime());
                    goods.setDefaultImage(pmsSkuEntity.getDefaultImage());
                    goods.setSubTitle(pmsSkuEntity.getSubtitle());
                    goods.setPrice(pmsSkuEntity.getPrice().doubleValue());
                    goods.setSales(0L);
                    goods.setSkuId(pmsSkuEntity.getId());


                    //查询库存信息
                    ResponseVo<List<WmsWareSkuEntity>> listResp =
                            this.wmsClient.queryWmsWareSkuListBySkuId(pmsSkuEntity.getId());
                    List<WmsWareSkuEntity> wareSkuEntities = listResp.getData();
                    if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                        boolean flag = wareSkuEntities.stream().anyMatch(wmsWareSkuEntity -> wmsWareSkuEntity.getStock() > 0);
                        goods.setStore(flag);
                    }
                    goods.setTitle(pmsSkuEntity.getTitle());
                    return goods;
                }).collect(Collectors.toList());


                //导入索引库
                this.goodsRepository.saveAll(goodsList);
            }

            try {
                //消费消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                if (message.getMessageProperties().getRedelivered()) {
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                } else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            }
        }


    }
