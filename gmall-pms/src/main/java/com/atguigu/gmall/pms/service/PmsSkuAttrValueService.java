package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;

import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
public interface PmsSkuAttrValueService extends IService<PmsSkuAttrValueEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

