package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.ums.entity.UmsUserLevelEntity;

import java.util.Map;

/**
 * 会员等级表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
public interface UmsUserLevelService extends IService<UmsUserLevelEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

