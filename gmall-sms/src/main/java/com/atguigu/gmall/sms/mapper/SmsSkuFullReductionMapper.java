package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.SmsSkuFullReductionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品满减信息
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:56:45
 */
@Mapper
@Repository
public interface SmsSkuFullReductionMapper extends BaseMapper<SmsSkuFullReductionEntity> {
	
}
