package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.ums.entity.UmsIntegrationHistoryEntity;

import java.util.Map;

/**
 * 购物积分记录表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
public interface UmsIntegrationHistoryService extends IService<UmsIntegrationHistoryEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

