package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.PmsAttrEntity;
import com.atguigu.gmall.pms.mapper.PmsAttrMapper;
import com.atguigu.gmall.pms.vo.PmsAttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.PmsAttrGroupMapper;
import com.atguigu.gmall.pms.entity.PmsAttrGroupEntity;
import com.atguigu.gmall.pms.service.PmsAttrGroupService;


@Service("pmsAttrGroupService")
public class PmsAttrGroupServiceImpl extends ServiceImpl<PmsAttrGroupMapper, PmsAttrGroupEntity> implements PmsAttrGroupService {

    @Autowired
    private PmsAttrMapper pmsAttrMapper;


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

}