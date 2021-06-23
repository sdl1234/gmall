package com.atguigu.gmall.ums.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UmsUserCollectSubjectMapper;
import com.atguigu.gmall.ums.entity.UmsUserCollectSubjectEntity;
import com.atguigu.gmall.ums.service.UmsUserCollectSubjectService;


@Service("umsUserCollectSubjectService")
public class UmsUserCollectSubjectServiceImpl extends ServiceImpl<UmsUserCollectSubjectMapper, UmsUserCollectSubjectEntity> implements UmsUserCollectSubjectService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserCollectSubjectEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserCollectSubjectEntity>()
        );

        return new PageResultVo(page);
    }

}