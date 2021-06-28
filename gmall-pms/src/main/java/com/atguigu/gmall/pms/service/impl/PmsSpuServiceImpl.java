package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.mapper.*;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.PmsSkuVo;
import com.atguigu.gmall.pms.vo.PmsSpuVo;
import com.atguigu.gmall.pms.vo.ProductAttrValueVo;
import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import org.springframework.util.CollectionUtils;


@Service("pmsSpuService")
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpuEntity> implements PmsSpuService {

    @Autowired
    private PmsSpuDescMapper pmsSpuDescMapper;

    @Autowired
    private PmsSpuAttrValueService pmsSpuAttrValueService;

    @Autowired
    private PmsSkuMapper pmsSkuMapper;

    @Autowired
    private PmsSkuImagesService pmsSkuImagesService;

    @Autowired
    private PmsSkuAttrValueService pmsSkuAttrValueService;

    @Autowired
    private GmallSmsClient gmallSmsClient;

    @GlobalTransactional
    @Override
    public void bigSave(PmsSpuVo pmsSpuVo) {

        //保存关于spu的数据
            //1，保存spu的基本信息
                //是否发布（默认为发布）
                pmsSpuVo.setPublishStatus(1);
                //创建时间
                pmsSpuVo.setCreateTime(new Date());
                //修改时间（因为要保证修改该时间和创建时间相同 上面已经设置修改该时间 此处直接.就可以）
                pmsSpuVo.setUpdateTime(pmsSpuVo.getCreateTime());
                //将数据按名称保存到数据库
                this.save(pmsSpuVo);
                //获取保存后返回的ID
                Long spuId = pmsSpuVo.getId();
            //2，保存spu的描述信息
                //new新对象
                PmsSpuDescEntity pmsSpuDescEntity =new PmsSpuDescEntity();
                //因为此表的Id并非自增保存 所以需要手动设置 实体类中也要修改主键类型
                pmsSpuDescEntity.setSpuId(spuId);
                //保存图片地址 并用,分开
                if (!CollectionUtils.isEmpty(pmsSpuVo.getSpuImages())){
                    pmsSpuDescEntity.setDecript(StringUtils.join(pmsSpuVo.getSpuImages(),","));
                }
                //保存对象
                this.pmsSpuDescMapper.insert(pmsSpuDescEntity);
            //3. 保存spu的规格参数信息
                List<ProductAttrValueVo> baseAttrs = pmsSpuVo.getBaseAttrs();
                if (!CollectionUtils.isEmpty(baseAttrs)){
                    List<PmsSpuAttrValueEntity> pmsSpuAttrValueEntities=
                            baseAttrs.stream()
                                    .filter(productAttrValueVo -> productAttrValueVo.getAttrValue() != null)
                                    .map(productAttrValueVo -> {
                                    productAttrValueVo.setSpuId(spuId);
                                    productAttrValueVo.setSort(0);
                                    return productAttrValueVo;
                            }).collect(Collectors.toList());
                    //因为是保存数组 所以此处装配PmsSpuAttrValueService
                    this.pmsSpuAttrValueService.saveBatch(pmsSpuAttrValueEntities);
                }
        //保存关于sku的数据
            //1.保存sku基本信息
                //判断sku是否为空
                List<PmsSkuVo> pmsSkuVos =pmsSpuVo.getSkus();
                if (CollectionUtils.isEmpty(pmsSkuVos)){
                    return;
                }
                //遍历并且保存sku的基本信息
                pmsSkuVos.forEach(pmsSkuVo ->{
                    //保存基本信息
                    PmsSkuEntity pmsSkuEntity =new PmsSkuVo();
                    BeanUtils.copyProperties(pmsSkuVo,pmsSkuEntity);
                    //保存分类和品牌信息
                    pmsSkuEntity.setCategoryId(pmsSpuVo.getCategoryId());
                    pmsSkuEntity.setBrandId(pmsSpuVo.getBrandId());
                    //保存默认图片
                    List<String> images =pmsSkuVo.getImages();
                    if (!CollectionUtils.isEmpty(images)){
                        pmsSkuEntity.setDefaultImage(pmsSkuEntity.getDefaultImage() == null ?
                                images.get(0) : pmsSkuEntity.getDefaultImage()
                                );
                    }
                    pmsSkuEntity.setSpuId(spuId);
                    this.pmsSkuMapper.insert(pmsSkuEntity);

                    //获取保存后的skuId
                    Long skuId = pmsSkuEntity.getId();
                //2.保存图片
                    if (!CollectionUtils.isEmpty(images)) {
                        String defaultImage = images.get(0);
                        List<PmsSkuImagesEntity> skuImages = images.stream().map(image -> {
                            PmsSkuImagesEntity pmsSkuImagesEntity =new PmsSkuImagesEntity();
                            pmsSkuImagesEntity.setSkuId(skuId);
                            pmsSkuImagesEntity.setUrl(image);
                            pmsSkuImagesEntity.setSort(0);
                            pmsSkuImagesEntity.setDefaultStatus(
                                    StringUtils.equals(defaultImage,image) ? 1 : 0);
                            return pmsSkuImagesEntity;
                        }).collect(Collectors.toList());
                        this. pmsSkuImagesService.saveBatch(skuImages);
                    }
                // 3.保存销售信息
                    List<PmsSkuAttrValueEntity> saleAttrs = pmsSkuVo.getSaleAttrs();
                    saleAttrs.forEach(saleAttr -> {
                        saleAttr.setSort(0);
                        saleAttr.setSkuId(skuId);
                    });
                    this.pmsSkuAttrValueService.saveBatch(saleAttrs);

           //保存营销信息

                    SmsSkuSaleVo smsSkuSaleVo =new SmsSkuSaleVo();
                    BeanUtils.copyProperties(pmsSkuVo,smsSkuSaleVo);
                    smsSkuSaleVo.setSkuId(skuId);
                    this.gmallSmsClient.saveSkuSaleInfo(smsSkuSaleVo);
                } );

//                int i = 1 / 0;

    }


    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsSpuEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsSpuEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public PageResultVo querySpuInfo(Long categoryId, PageParamVo pageParamVo) {
        QueryWrapper<PmsSpuEntity> queryWrapper =new QueryWrapper<>();
        //判断是查本类还是查全站
        if (categoryId != 0){
            queryWrapper.eq("category_id",categoryId);
        }
        //有查询条件
        String key = pageParamVo.getKey();
        //判断条件是否为空
        if (StringUtils.isNotBlank(key)){
            queryWrapper.and(t -> t.like("id",key)
                    .or()
                    .like("name",key));
        }
        IPage<PmsSpuEntity> page = baseMapper.selectPage(pageParamVo.getPage(), queryWrapper);
        return new PageResultVo(page);
//        return new PageResultVo(this.page(pageParamVo.getPage(),queryWrapper));
    }



}