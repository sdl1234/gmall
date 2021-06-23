package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.ums.entity.UmsUserLoginLogEntity;

import java.util.Map;

/**
 * 用户登陆记录表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
public interface UmsUserLoginLogService extends IService<UmsUserLoginLogEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

