package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UmsUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
public interface UmsUserService extends IService<UmsUserEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    Boolean checkByDataAndType(String data, Integer type);

    ResponseVo register(UmsUserEntity userEntity, String code);

    UmsUserEntity queryUser(String loginName, String password);
}

