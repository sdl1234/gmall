package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.PmsBrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
public interface PmsBrandService extends IService<PmsBrandEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

