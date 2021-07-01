package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Mapper
@Repository
public interface PmsSkuAttrValueMapper extends BaseMapper<PmsSkuAttrValueEntity> {

    List<PmsSkuAttrValueEntity> querySearchAttrValueBySkuId(Long skuId);
}
