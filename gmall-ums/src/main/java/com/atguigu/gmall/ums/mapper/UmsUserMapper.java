package com.atguigu.gmall.ums.mapper;

import com.atguigu.gmall.ums.entity.UmsUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
@Mapper
public interface UmsUserMapper extends BaseMapper<UmsUserEntity> {
	
}
