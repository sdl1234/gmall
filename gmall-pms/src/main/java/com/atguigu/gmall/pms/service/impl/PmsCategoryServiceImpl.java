package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.atguigu.gmall.pms.mapper.PmsCategoryMapper;
import com.atguigu.gmall.pms.service.PmsCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


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

    @Override
    public List<PmsCategoryEntity> queryLv1123CategoriesByCid(Long cid) {
        //查询三级分类
        PmsCategoryEntity categoryEntity3 = this.getById(cid);
        if (categoryEntity3 == null) {
            return null ;
        }

        //查询二级分类
        PmsCategoryEntity categoryEntity2 = this.getById(categoryEntity3.getParentId());
        //查询一级分类
        PmsCategoryEntity categoryEntity = this.getById(categoryEntity2.getParentId());

        return Arrays.asList(categoryEntity,categoryEntity2,categoryEntity3);
    }

}