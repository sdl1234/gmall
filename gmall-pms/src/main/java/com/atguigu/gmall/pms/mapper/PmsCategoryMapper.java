package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品三级分类
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Mapper
@Repository
public interface PmsCategoryMapper extends BaseMapper<PmsCategoryEntity> {
	
}
