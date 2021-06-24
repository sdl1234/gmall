package com.atguigu.gmall.pms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.PmsSpuMapper;
import com.atguigu.gmall.pms.entity.PmsSpuEntity;
import com.atguigu.gmall.pms.service.PmsSpuService;


@Service("pmsSpuService")
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpuEntity> implements PmsSpuService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsSpuEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsSpuEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public PageResultVo querySpuInfo(Long categoryId, PageParamVo pageParamVo) {
        QueryWrapper<PmsSpuEntity> queryWrapper =new QueryWrapper<>();
        //判断是查本类还是查全站
        if (categoryId != 0){
            queryWrapper.eq("category_id",categoryId);
        }
        //有查询条件
        String key = pageParamVo.getKey();
        //判断条件是否为空
        if (StringUtils.isNotBlank(key)){
            queryWrapper.and(t -> t.like("id",key)
                    .or()
                    .like("name",key));
        }
        IPage<PmsSpuEntity> page = baseMapper.selectPage(pageParamVo.getPage(), queryWrapper);
        return new PageResultVo(page);
//        return new PageResultVo(this.page(pageParamVo.getPage(),queryWrapper));
    }

}