package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsGrowthHistoryMapper;
import com.atguigu.gmall.ums.entity.UmsGrowthHistoryEntity;
import com.atguigu.gmall.ums.service.UmsGrowthHistoryService;


@Service("umsGrowthHistoryService")
public class UmsGrowthHistoryServiceImpl extends ServiceImpl<UmsGrowthHistoryMapper, UmsGrowthHistoryEntity> implements UmsGrowthHistoryService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsGrowthHistoryEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsGrowthHistoryEntity>()
        );

        return new PageResultVo(page);
    }

}