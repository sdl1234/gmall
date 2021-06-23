package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.PmsSpuDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
public interface PmsSpuDescService extends IService<PmsSpuDescEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

