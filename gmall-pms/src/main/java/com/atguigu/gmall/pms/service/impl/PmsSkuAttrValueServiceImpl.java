package com.atguigu.gmall.pms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.service.PmsSkuAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSkuAttrValueService")
public class PmsSkuAttrValueServiceImpl extends ServiceImpl<PmsSkuAttrValueMapper, PmsSkuAttrValueEntity> implements PmsSkuAttrValueService {


    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;


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

}