package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserAddressMapper;
import com.atguigu.gmall.ums.entity.UmsUserAddressEntity;
import com.atguigu.gmall.ums.service.UmsUserAddressService;


@Service("umsUserAddressService")
public class UmsUserAddressServiceImpl extends ServiceImpl<UmsUserAddressMapper, UmsUserAddressEntity> implements UmsUserAddressService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserAddressEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserAddressEntity>()
        );

        return new PageResultVo(page);
    }

}