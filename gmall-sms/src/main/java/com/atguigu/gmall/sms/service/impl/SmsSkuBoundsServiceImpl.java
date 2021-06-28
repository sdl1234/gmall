package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.entity.SmsSkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SmsSkuLadderEntity;
import com.atguigu.gmall.sms.mapper.SmsSkuFullReductionMapper;
import com.atguigu.gmall.sms.mapper.SmsSkuLadderMapper;
import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.sms.mapper.SmsSkuBoundsMapper;
import com.atguigu.gmall.sms.entity.SmsSkuBoundsEntity;
import com.atguigu.gmall.sms.service.SmsSkuBoundsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("smsSkuBoundsService")
public class SmsSkuBoundsServiceImpl extends ServiceImpl<SmsSkuBoundsMapper, SmsSkuBoundsEntity> implements SmsSkuBoundsService {

    @Autowired
    private SmsSkuFullReductionMapper smsSkuFullReductionMapper;

    @Autowired
    private SmsSkuLadderMapper smsSkuLadderMapper;

    @Transactional
    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SmsSkuBoundsEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<SmsSkuBoundsEntity>()
        );

        return new PageResultVo(page);
    }

    @Transactional
    @Override
    public void saveSkuSaleInfo(SmsSkuSaleVo smsSkuSaleVo) {
        //积分
        SmsSkuBoundsEntity smsSkuBoundsEntity =new SmsSkuBoundsEntity();
        BeanUtils.copyProperties(smsSkuSaleVo,smsSkuBoundsEntity);
        List<Integer> work = smsSkuSaleVo.getWork();
        if (!CollectionUtils.isEmpty(work)){
            smsSkuBoundsEntity.setWork(work.get(0) * 8
                     + work.get(1) * 4
                     + work.get(2) * 2
                     + work.get(3));
        }
        this.save(smsSkuBoundsEntity);

        //满减
        SmsSkuFullReductionEntity smsSkuFullReductionEntity =new SmsSkuFullReductionEntity();
        BeanUtils.copyProperties(smsSkuSaleVo,smsSkuFullReductionEntity);
        smsSkuFullReductionEntity.setAddOther(smsSkuSaleVo.getFullAddOther());
        this.smsSkuFullReductionMapper.insert(smsSkuFullReductionEntity);

        //折扣
        SmsSkuLadderEntity smsSkuLadderEntity =new SmsSkuLadderEntity();
        BeanUtils.copyProperties(smsSkuSaleVo,smsSkuLadderEntity);
        this.smsSkuLadderMapper.insert(smsSkuLadderEntity);
    }


}