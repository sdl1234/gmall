package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserMapper;
import com.atguigu.gmall.ums.entity.UmsUserEntity;
import com.atguigu.gmall.ums.service.UmsUserService;


@Service("umsUserService")
public class UmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUserEntity> implements UmsUserService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserEntity>()
        );

        return new PageResultVo(page);
    }

}