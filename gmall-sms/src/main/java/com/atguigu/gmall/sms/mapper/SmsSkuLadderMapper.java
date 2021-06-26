package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.SmsSkuLadderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品阶梯价格
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
@Mapper
@Repository
public interface SmsSkuLadderMapper extends BaseMapper<SmsSkuLadderEntity> {
	
}
