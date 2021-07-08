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

import com.atguigu.gmall.pms.mapper.PmsCategoryMapper;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.atguigu.gmall.pms.service.PmsCategoryService;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategoryEntity> implements PmsCategoryService {

    @Autowired
    private PmsCategoryMapper pmsCategoryMapper;


    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsCategoryEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public List<PmsCategoryEntity> queryCategory(Long parentId) {

        QueryWrapper<PmsCategoryEntity> queryWrapper =new QueryWrapper<>();
        if (parentId != -1){
            queryWrapper.eq("parent_id",parentId);
        }
        return this.pmsCategoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<PmsCategoryEntity> queryLv2WithSubsByPid(Long pid) {
        return this.pmsCategoryMapper.queryLv2WithSubsByPid(pid);
    }

}