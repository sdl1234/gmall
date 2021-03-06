package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserStatisticsMapper;
import com.atguigu.gmall.ums.entity.UmsUserStatisticsEntity;
import com.atguigu.gmall.ums.service.UmsUserStatisticsService;


@Service("umsUserStatisticsService")
public class UmsUserStatisticsServiceImpl extends ServiceImpl<UmsUserStatisticsMapper, UmsUserStatisticsEntity> implements UmsUserStatisticsService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserStatisticsEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserStatisticsEntity>()
        );

        return new PageResultVo(page);
    }

}