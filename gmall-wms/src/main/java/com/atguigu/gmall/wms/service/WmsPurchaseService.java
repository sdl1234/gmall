package com.atguigu.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.wms.entity.WmsPurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 18:00:09
 */
public interface WmsPurchaseService extends IService<WmsPurchaseEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

