package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserLevelMapper;
import com.atguigu.gmall.ums.entity.UmsUserLevelEntity;
import com.atguigu.gmall.ums.service.UmsUserLevelService;


@Service("umsUserLevelService")
public class UmsUserLevelServiceImpl extends ServiceImpl<UmsUserLevelMapper, UmsUserLevelEntity> implements UmsUserLevelService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserLevelEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserLevelEntity>()
        );

        return new PageResultVo(page);
    }

}