package com.atguigu.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.sms.entity.SmsSeckillSkuEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
public interface SmsSeckillSkuService extends IService<SmsSeckillSkuEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

