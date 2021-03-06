package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserCollectShopMapper;
import com.atguigu.gmall.ums.entity.UmsUserCollectShopEntity;
import com.atguigu.gmall.ums.service.UmsUserCollectShopService;


@Service("umsUserCollectShopService")
public class UmsUserCollectShopServiceImpl extends ServiceImpl<UmsUserCollectShopMapper, UmsUserCollectShopEntity> implements UmsUserCollectShopService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserCollectShopEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserCollectShopEntity>()
        );

        return new PageResultVo(page);
    }

}