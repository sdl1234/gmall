package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.SmsSeckillSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动商品关联
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
@Mapper
public interface SmsSeckillSkuMapper extends BaseMapper<SmsSeckillSkuEntity> {
	
}
