package com.atguigu.gmall.pms.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.PmsAttrMapper;
import com.atguigu.gmall.pms.entity.PmsAttrEntity;
import com.atguigu.gmall.pms.service.PmsAttrService;


@Service("pmsAttrService")
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrMapper, PmsAttrEntity> implements PmsAttrService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsAttrEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsAttrEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public List<PmsAttrEntity> queryPmsAttrEntityByCid(Long cid, Integer type, Integer searchType) {
        QueryWrapper<PmsAttrEntity> queryWrapper =new QueryWrapper<>();
        if (cid != 0){
            queryWrapper.eq("category_id",cid);
        }
        if (type != null){
            queryWrapper.eq("type",type);
        }
        if (searchType != null){
            queryWrapper.eq("search_type",searchType);
        }
        return this.list(queryWrapper);

    }

}