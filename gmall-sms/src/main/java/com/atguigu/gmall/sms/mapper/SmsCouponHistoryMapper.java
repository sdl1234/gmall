package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.SmsCouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
@Mapper
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistoryEntity> {
	
}
