package com.atguigu.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 18:00:09
 */
public interface WmsWareSkuService extends IService<WmsWareSkuEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

