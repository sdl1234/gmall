package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.ums.entity.UmsUserCollectSkuEntity;

import java.util.Map;

/**
 * 关注商品表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
public interface UmsUserCollectSkuService extends IService<UmsUserCollectSkuEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

