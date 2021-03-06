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

import com.atguigu.gmall.pms.mapper.PmsSpuAttrValueMapper;
import com.atguigu.gmall.pms.entity.PmsSpuAttrValueEntity;
import com.atguigu.gmall.pms.service.PmsSpuAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSpuAttrValueService")
public class PmsSpuAttrValueServiceImpl extends ServiceImpl<PmsSpuAttrValueMapper, PmsSpuAttrValueEntity> implements PmsSpuAttrValueService {

    @Autowired
    private PmsSpuAttrValueMapper pmsSpuAttrValueMapper;


    @Transactional
    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<PmsSpuAttrValueEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<PmsSpuAttrValueEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public List<PmsSpuAttrValueEntity> querySearchAttrValueBySpuId(Long spuId) {

         return this.pmsSpuAttrValueMapper.querySearchAttrValueBySpuId(spuId);


    }

}