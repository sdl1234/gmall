package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.vo.SmsSkuSaleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.sms.entity.SmsSkuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
public interface SmsSkuBoundsService extends IService<SmsSkuBoundsEntity> {

    PageResultVo queryPage(PageParamVo paramVo);


    void saveSkuSaleInfo(SmsSkuSaleVo smsSkuSaleVo);
}

