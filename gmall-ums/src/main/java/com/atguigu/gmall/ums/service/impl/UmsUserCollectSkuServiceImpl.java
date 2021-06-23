package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserCollectSkuMapper;
import com.atguigu.gmall.ums.entity.UmsUserCollectSkuEntity;
import com.atguigu.gmall.ums.service.UmsUserCollectSkuService;


@Service("umsUserCollectSkuService")
public class UmsUserCollectSkuServiceImpl extends ServiceImpl<UmsUserCollectSkuMapper, UmsUserCollectSkuEntity> implements UmsUserCollectSkuService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserCollectSkuEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserCollectSkuEntity>()
        );

        return new PageResultVo(page);
    }

}