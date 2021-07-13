package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.PmsAttrEntity;
import com.atguigu.gmall.pms.entity.PmsAttrGroupEntity;
import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.PmsSpuAttrValueEntity;
import com.atguigu.gmall.pms.mapper.PmsAttrGroupMapper;
import com.atguigu.gmall.pms.mapper.PmsAttrMapper;
import com.atguigu.gmall.pms.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.pms.mapper.PmsSpuAttrValueMapper;
import com.atguigu.gmall.pms.service.PmsAttrGroupService;
import com.atguigu.gmall.pms.vo.AttrValueVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.PmsAttrGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.common.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service("pmsAttrGroupService")
public class PmsAttrGroupServiceImpl extends ServiceImpl<PmsAttrGroupMapper, PmsAttrGroupEntity> implements PmsAttrGroupService {

    @Autowired
    private PmsAttrMapper pmsAttrMapper;

    @Autowired
    private PmsSpuAttrValueMapper spuAttrValueMapper;

    @Autowired
    private PmsSkuAttrValueMapper skuAttrValueMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsAttrGroupEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsAttrGroupEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public List<PmsAttrGroupVo> queryPmsAttrGroupByCatId(Long catId) {
        //查询商品基本数据
        List<PmsAttrGroupEntity> pmsAttrGroupEntityList = this.list(
                new QueryWrapper<PmsAttrGroupEntity>()
                        .eq("category_id",catId)
        );
        //查询需要展示的商品销售数据
        return pmsAttrGroupEntityList.stream().map(pmsAttrGroupEntity -> {
            PmsAttrGroupVo pmsAttrGroupVo =new PmsAttrGroupVo();
            BeanUtils.copyProperties(pmsAttrGroupEntity,pmsAttrGroupVo);
            //查询商品的spu信息，并排除掉sku中要展示的数据
            List<PmsAttrEntity> attrEntities = this.pmsAttrMapper.selectList(
                    new QueryWrapper<PmsAttrEntity>()
                            .eq("group_id",pmsAttrGroupEntity.getId())
                            .eq("type",1)
            );
            pmsAttrGroupVo.setAttrEntities(attrEntities);
            return pmsAttrGroupVo;
        }).collect(Collectors.toList());

    }

    @Override
    public List<ItemGroupVo> queryGroupsWithAttrValuesByCidAndSpuIdAndSkuId(Long cid, Long spuId, Long skuId) {

        //根据 cid 查询cg中符合条件的数据
        List<PmsAttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<PmsAttrGroupEntity>().eq("category_id", cid));
        if (CollectionUtils.isEmpty(attrGroupEntities)){
            return null;
        }
        //遍历数据
        return attrGroupEntities.stream().map(pmsAttrGroupEntity -> {

            //开始保存数据
            ItemGroupVo itemGroupVo =new ItemGroupVo();
            itemGroupVo.setGroupName(pmsAttrGroupEntity.getName());


            //根据遍历出的 id查询attr中的数据
            List<PmsAttrEntity> pmsAttrEntities = this.pmsAttrMapper.selectList(new QueryWrapper<PmsAttrEntity>().eq("group_id", pmsAttrGroupEntity.getId()));
            if (!CollectionUtils.isEmpty(pmsAttrEntities)){

                //遍历详情数据
                List<AttrValueVo> attrValueVos = pmsAttrEntities.stream().map(pmsAttrEntity -> {
                    //存入分组下详情
                    AttrValueVo attrValueVo=new AttrValueVo();
                    attrValueVo.setAttrId(pmsAttrEntity.getId());
                    attrValueVo.setAttrName(pmsAttrEntity.getName());

                    //根据pmsAttrEntity type 查询详细数据
                    if (pmsAttrEntity.getType() == 1){
                        PmsSpuAttrValueEntity spuAttrValueEntity = this.spuAttrValueMapper.selectOne(new QueryWrapper<PmsSpuAttrValueEntity>().eq("spu_id",spuId).eq("attr_id", pmsAttrEntity.getId()));
                        if (spuAttrValueEntity != null) {
                            attrValueVo.setAttrValue(spuAttrValueEntity.getAttrValue());
                        }

                    }else {
                        PmsSkuAttrValueEntity skuAttrValueEntity = this.skuAttrValueMapper.selectOne(new QueryWrapper<PmsSkuAttrValueEntity>().eq("sku_id",skuId).eq("attr_id", pmsAttrEntity.getId()));
                        if (skuAttrValueEntity != null) {
                            attrValueVo.setAttrValue(skuAttrValueEntity.getAttrValue());
                        }
                    }

                    return attrValueVo;
                }).collect(Collectors.toList());


            itemGroupVo.setAttrValues(attrValueVos);
            }

            return itemGroupVo;
        }).collect(Collectors.toList());

    }

}