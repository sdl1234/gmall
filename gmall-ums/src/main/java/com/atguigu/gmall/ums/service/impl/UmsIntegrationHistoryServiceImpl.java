package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsIntegrationHistoryMapper;
import com.atguigu.gmall.ums.entity.UmsIntegrationHistoryEntity;
import com.atguigu.gmall.ums.service.UmsIntegrationHistoryService;


@Service("umsIntegrationHistoryService")
public class UmsIntegrationHistoryServiceImpl extends ServiceImpl<UmsIntegrationHistoryMapper, UmsIntegrationHistoryEntity> implements UmsIntegrationHistoryService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsIntegrationHistoryEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsIntegrationHistoryEntity>()
        );

        return new PageResultVo(page);
    }

}