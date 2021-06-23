package com.atguigu.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.sms.entity.SmsCouponSpuEntity;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
public interface SmsCouponSpuService extends IService<SmsCouponSpuEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

