package com.atguigu.gmall.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.PmsAttrEntity;
import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.PmsSkuEntity;
import com.atguigu.gmall.pms.mapper.PmsAttrMapper;
import com.atguigu.gmall.pms.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.pms.mapper.PmsSkuMapper;
import com.atguigu.gmall.pms.service.PmsSkuAttrValueService;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("pmsSkuAttrValueService")
public class PmsSkuAttrValueServiceImpl extends ServiceImpl<PmsSkuAttrValueMapper, PmsSkuAttrValueEntity> implements PmsSkuAttrValueService {


    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    private PmsSkuMapper skuMapper;

    @Autowired
    private PmsAttrMapper attrMapper;

    @Transactional
    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsSkuAttrValueEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsSkuAttrValueEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public List<PmsSkuAttrValueEntity> querySearchAttrValueBySkuId(Long skuId) {

        return this.pmsSkuAttrValueMapper.querySearchAttrValueBySkuId(skuId);
    }

    @Override
    public List<SaleAttrValueVo> querySaleAttrValuesBySpuId(Long spuId) {
        //根据spuId 查询所有该spu下的所有sku
        List<PmsSkuEntity> skuEntities = this.skuMapper.selectList(new QueryWrapper<PmsSkuEntity>().eq("spu_id", spuId));
        if (CollectionUtils.isEmpty(skuEntities)){
            return null;
        }
        //遍历skuEntities获取skuIds
        List<Long> skuIds = skuEntities.stream().map(PmsSkuEntity::getId).collect(Collectors.toList());

        //查询所有sku属性
        List<PmsSkuAttrValueEntity> skuAttrValueEntities = this.list(new QueryWrapper<PmsSkuAttrValueEntity>().in("sku_id", skuIds).orderByAsc("attr_id"));
        if (CollectionUtils.isEmpty(skuAttrValueEntities)){
            return  null;
        }

        List<SaleAttrValueVo> saleAttrValueVos =new ArrayList<>();

        //将数据以attrId为键 对应的数据为值 存进map
        Map<Long, List<PmsSkuAttrValueEntity>> map = skuAttrValueEntities.stream().collect(Collectors.groupingBy(PmsSkuAttrValueEntity::getAttrId));
        //遍历map数据
        map.forEach((attrId,skuAttrValueEntityList) ->{
            SaleAttrValueVo saleAttrValueVo=new SaleAttrValueVo();
            saleAttrValueVo.setAttrId(attrId);
            saleAttrValueVo.setAttrName(skuAttrValueEntityList.get(0).getAttrName());
            //使用set集合
            saleAttrValueVo.setAttrValues(skuAttrValueEntityList.stream().map(PmsSkuAttrValueEntity::getAttrValue).collect(Collectors.toSet()));
            saleAttrValueVos.add(saleAttrValueVo);
        });


        return saleAttrValueVos;
    }

    @Override
    public String queryMappingBySpuId(Long spuId) {

        //根据spuId查询sku列表
        List<PmsSkuEntity> skuEntities = this.skuMapper.selectList(new QueryWrapper<PmsSkuEntity>().eq("spu_id", spuId));
        if (CollectionUtils.isEmpty(skuEntities)){
            return null;
        }
        //获取skuId集合
        List<Long> skuIds = skuEntities.stream().map(PmsSkuEntity::getId).collect(Collectors.toList());
        //获取映射关系
         List<Map<String,Object>> maps =this.pmsSkuAttrValueMapper.queryMappingBySpuId(skuIds);
         if (CollectionUtils.isEmpty(maps)){
             return null;
         }

         //将list转化成map
        Map<String, Long> mappiingMap = maps.stream().collect(Collectors.toMap(map -> map.get("attr_values").toString(), map -> (Long) map.get("sku_id")));

        //使用JSON将map转换成Json返回
        return JSON.toJSONString(mappiingMap);
    }

    @Override
    public List<PmsSkuAttrValueEntity> querySearchAttrValuesBySkuId(Long cid, Long skuId) {
        //查询检索类型的规格参数
        List<PmsAttrEntity> pmsAttrEntities = this.attrMapper.selectList(new QueryWrapper<PmsAttrEntity>().eq("category_id", cid).eq("search_type", 1));
        if (CollectionUtils.isEmpty(pmsAttrEntities)){
            return  null;
        }

        List<Long> attrIds = pmsAttrEntities.stream().map(PmsAttrEntity::getId).collect(Collectors.toList());
        return this.list(new QueryWrapper<PmsSkuAttrValueEntity>().eq("sku_id",skuId).in("attr_id",attrIds));
    }


}